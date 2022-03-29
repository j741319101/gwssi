package com.dstz.bpm.engine.action.handler;
import com.dstz.bus.api.model.IBusinessData;
import com.dstz.bus.api.service.IBusinessDataService;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.TaskSkipType;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
import com.dstz.bpm.api.engine.action.handler.ActionHandler;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.plugin.cmd.ExecutionCommand;
import com.dstz.bpm.api.engine.plugin.cmd.TaskCommand;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.exception.WorkFlowException;
import com.dstz.bpm.api.model.def.BpmDataModel;
import com.dstz.bpm.api.model.def.NodeInit;
import com.dstz.bpm.api.model.form.BpmForm;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.data.handle.BpmBusDataHandle;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.engine.util.HandlerUtil;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.core.util.ThreadMapUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
public abstract class AbsActionHandler<T extends BaseActionCmd>  implements ActionHandler<T> {
    public static final String SKIP_TASKIDS = "SKIP_TASKIDS";
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    protected BpmProcessDefService bpmProcessDefService;
    @Resource
    protected BpmInstanceManager bpmInstanceManager;
    @Resource
    protected BpmBusDataHandle busDataHandle;
    @Resource
    protected TaskIdentityLinkManager taskIdentityLinkManager;
    @Resource
    protected IBusinessDataService iBusinessDataService;
    @Resource
    protected IGroovyScriptEngine iGroovyScriptEngine;
    @Autowired
    protected BpmTaskManager bpmTaskManager;
    @Resource
    private ExecutionCommand executionCommand;
    @Resource
    protected TaskCommand taskCommand;
    @Transactional(rollbackFor = {Exception.class})
    public void execute(T model) {
        boolean isStopExecute = prepareActionDatas(model);
        checkFlowIsValid((BaseActionCmd) model);
        BpmContext.setActionModel((ActionCmd) model);
        executePlugbeforeSaveBusData(model);
        handelBusData(model);
        if (isStopExecute) {
            return;
        }
        doAction(model);
        doSkip(model);
        BpmContext.removeActionModel();
        actionAfter(model);
    }
    private void executePlugbeforeSaveBusData(T model) {
        if (model instanceof InstanceActionCmd) {
            this.executionCommand.execute(EventType.PRE_SAVE_BUS_EVENT, (InstanceActionCmd) model);
        } else if (model instanceof TaskActionCmd) {
            this.taskCommand.execute(EventType.PRE_SAVE_BUS_EVENT, (TaskActionCmd) model);
        }
    }
    protected void handelBusData(T data) {
        executeHandler(data);
        this.LOG.debug("流程启动处理业务数据...");
        this.busDataHandle.saveDataModel((BaseActionCmd) data);
        doActionBefore(data);
    }
    protected void actionAfter(T model) {
        toDoActionAfter(model);
        if (getActionType() == ActionType.DRAFT) {
            return;
        }
        if (model.isSource()) {
            BpmContext.cleanTread();
        }
    }
    private void doSkip(T model) {
        if (getActionType() != ActionType.AGREE && getActionType() != ActionType.OPPOSE && getActionType() != ActionType.START) {
            return;
        }
        ActionCmd actionModel = BpmContext.getActionModel();
        if (actionModel == null || actionModel instanceof InstanceActionCmd) {
            return;
        }
        DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd) BpmContext.getActionModel();
        List<String> taskIds = (List<String>) ThreadMapUtil.removeGet("SKIP_TASKIDS");
        if (CollUtil.isNotEmpty(taskIds)) {
            for (String id : taskIds) {
                BpmTask task = (BpmTask) this.bpmTaskManager.get(id);
                TaskIdentityLink taskIdentityLink = this.taskIdentityLinkManager.getByTaskId(id).get(0);
                DefualtTaskActionCmd defualtTaskActionCmd = new DefualtTaskActionCmd();
                defualtTaskActionCmd.setBpmInstance(taskModel.getBpmInstance());
                defualtTaskActionCmd.setBpmDefinition(taskModel.getBpmDefinition());
                defualtTaskActionCmd.setBizDataMap(taskModel.getBizDataMap());
                defualtTaskActionCmd.setBusinessKey(taskModel.getBusinessKey());
                defualtTaskActionCmd.setActionName(ActionType.AGREE.getKey());
                defualtTaskActionCmd.setBpmTask((IBpmTask) task);
                defualtTaskActionCmd.setTaskLinkId(taskIdentityLink.getId());
                defualtTaskActionCmd.setTaskIdentityLink(taskIdentityLink);
                defualtTaskActionCmd.setApproveOrgId(taskModel.getApproveOrgId());
                defualtTaskActionCmd.setOpinion(TaskSkipType.SCRIPT_SKIP.getValue());
                defualtTaskActionCmd.setDynamicBpmIdentity(model.getDynamicBpmIdentity());
                List<String> list = model.getStartAppointDestinations();
                if (CollectionUtil.isEmpty(list)) {
                    list = ((BaseActionCmd) actionModel).getStartAppointDestinations();
                }
                if (CollectionUtil.isNotEmpty(list)) {
                    defualtTaskActionCmd.setDestinations(list.<String>toArray(new String[list.size()]));
                }
                defualtTaskActionCmd.clearStartAppointDestination();
                defualtTaskActionCmd.setDynamicSubmitTaskName(model.getDynamicSubmitTaskName());
                BpmContext.setActionModel((ActionCmd) defualtTaskActionCmd);
                defualtTaskActionCmd.executeSkipTask();
            }
            BpmContext.setActionModel(actionModel);
            return;
        }
        if (taskModel.isHasSkipThisTask() == TaskSkipType.NO_SKIP)
            return;
        DefualtTaskActionCmd complateModel = new DefualtTaskActionCmd();
        complateModel.setBpmInstance(taskModel.getBpmInstance());
        complateModel.setBpmDefinition(taskModel.getBpmDefinition());
        complateModel.setBizDataMap(taskModel.getBizDataMap());
        complateModel.setBpmIdentities(taskModel.getBpmIdentities());
        complateModel.setBusinessKey(taskModel.getBusinessKey());
        complateModel.setActionName(ActionType.AGREE.getKey());
        complateModel.setBpmTask(taskModel.getBpmTask());
        complateModel.setTaskIdentityLink(taskModel.getTaskIdentityLink());
        complateModel.setTaskLinkId(taskModel.getTaskLinkId());
        complateModel.setApproveOrgId(taskModel.getApproveOrgId());
        complateModel.setOpinion(taskModel.isHasSkipThisTask().getValue());
        complateModel.setDynamicBpmIdentity(model.getDynamicBpmIdentity());
        List<String> startAppointDestinations = model.getStartAppointDestinations();
        if (CollectionUtil.isEmpty(startAppointDestinations)) {
            startAppointDestinations = ((BaseActionCmd) actionModel).getStartAppointDestinations();
        }
        if (CollectionUtil.isNotEmpty(startAppointDestinations)) {
            complateModel.setDestinations(startAppointDestinations.<String>toArray(new String[startAppointDestinations.size()]));
        }
        complateModel.clearStartAppointDestination();
        complateModel.setDynamicSubmitTaskName(model.getDynamicSubmitTaskName());
        complateModel.executeSkipTask();
    }
    public void skipTaskExecute(T model) {
        BpmContext.setActionModel((ActionCmd) model);
        doActionBefore(model);
        doAction(model);
        doSkip(model);
        BpmContext.removeActionModel();
        actionAfter(model);
    }
    protected void toDoActionAfter(T model) {
        doActionAfter(model);
    }
    protected void executeHandler(T actionModel) {
        BpmInstance instance = (BpmInstance) actionModel.getBpmInstance();
        if (StringUtil.isEmpty(actionModel.getBusinessKey()) && StringUtil.isNotEmpty(instance.getBizKey())) {
            actionModel.setBusinessKey(instance.getBizKey());
        }
        String handler = getFormHandler(actionModel);
        if (StringUtil.isNotEmpty(handler)) {
            try {
                HandlerUtil.invokeHandler((ActionCmd) actionModel, handler);
                this.LOG.debug("执行URL表单处理器：{}", handler);
            } catch (Exception ex) {
                throw new WorkFlowException(BpmStatusCode.HANDLER_ERROR, ex);
            }
        }
        if (StringUtil.isNotEmpty(actionModel.getBusinessKey()) && StringUtil.isEmpty(instance.getBizKey())) {
            instance.setBizKey(actionModel.getBusinessKey());
            instance.setHasUpdate(true);
        }
    }
    protected void checkFlowIsValid(BaseActionCmd actionModel) {
        IBpmInstance instance = actionModel.getBpmInstance();
        if (instance.getIsForbidden().shortValue() == 1) {
            throw new BusinessMessage(String.format("流程实例【%s】已经被挂起，请联系管理员", new Object[]{instance.getSubject()}), BpmStatusCode.DEF_FORBIDDEN);
        }
        DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
        if ("forbidden".equals(def.getExtProperties().getStatus())) {
            throw new BusinessMessage(String.format("流程定义【%s】已经被禁用，请联系管理员", new Object[]{def.getName()}), BpmStatusCode.DEF_FORBIDDEN);
        }
        IUser user = ContextUtil.getCurrentUser();
        if (ContextUtil.isAdmin(user))
            return;
        if (actionModel.getIgnoreAuthentication().booleanValue()) {
            return;
        }
        String taskId = null;
        String instId = actionModel.getInstanceId();
        if (actionModel instanceof DefualtTaskActionCmd) {
            IBpmTask task = ((DefualtTaskActionCmd) actionModel).getBpmTask();
            if (user.getUserId().equals(task.getAssigneeId())) {
                return;
            }
            taskId = task.getId();
            instId = null;
        } else {
            if (StringUtil.isNotEmpty(def.getProcessDefinitionId())) {
                return;
            }
            return;
        }
        Boolean hasPermission = this.taskIdentityLinkManager.checkUserOperatorPermission(user.getUserId(), instId, taskId);
        if (!hasPermission.booleanValue()) {
            throw new BusinessMessage("没有该任务的操作权限", BpmStatusCode.NO_PERMISSION);
        }
    }
    protected void parserBusinessData(T actionModel) {
        IBpmInstance instance = actionModel.getBpmInstance();
        JSONObject busData = actionModel.getBusData();
        Map<String, IBusinessData> businessDatas = null;
        if (busData == null || busData.isEmpty()) {
            businessDatas = this.busDataHandle.getInstanceData(null, (BpmInstance) instance);
        }
        DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
        for (BpmDataModel dataModel : bpmProcessDef.getDataModelList()) {
            String modelCode = dataModel.getCode();
            IBusinessData businessData = null;
            if (busData == null) {
                businessData = businessDatas.get(modelCode);
            } else if (busData.containsKey(modelCode)) {
                businessData = this.iBusinessDataService.parseBusinessData(busData.getJSONObject(modelCode), modelCode);
            }
            if (businessData != null) {
                actionModel.getBizDataMap().put(modelCode, businessData);
            }
        }
    }
    protected boolean handelFormInit(BaseActionCmd cmd, BpmNodeDef nodeDef) {
        String nodeId = nodeDef.getNodeId();
        DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(cmd.getBpmInstance().getDefId());
        List<NodeInit> nodeInitList = def.getNodeInitList(nodeId);
        Map<String, IBusinessData> bos = cmd.getBizDataMap();
        if (CollectionUtil.isEmpty(nodeInitList)) return false;
        Map<String, Object> param = new HashMap<>();
        if (bos != null) {
            param.putAll(bos);
        }
        param.put("bpmInstance", cmd.getBpmInstance());
        param.put("actionCmd", cmd);
        ActionType actionType = cmd.getActionType();
        param.put("submitActionDesc", actionType.getName());
        param.put("submitActionName", actionType.getKey());
        param.put("submitOpinion", cmd.getOpinion());
        param.put("isTask", Boolean.valueOf(false));
        if (cmd instanceof DefualtTaskActionCmd) {
            param.put("isTask", Boolean.valueOf(true));
            param.put("bpmTask", ((DefualtTaskActionCmd) cmd).getBpmTask());
        }
        boolean isStopExecute = false;
        BpmContext.setActionModel((ActionCmd) cmd);
        for (NodeInit init : nodeInitList) {
            if (StringUtil.isNotEmpty(init.getWhenSave())) {
                try {
                    Object result = this.iGroovyScriptEngine.executeObject(init.getWhenSave(), param);
                    if (StringUtils.equals(String.valueOf(result), "信号事件执行完成,中断execute")) {
                        isStopExecute = true;
                    }
                } catch (Exception e) {
                    throw new BusinessMessage(e.getMessage(), BpmStatusCode.FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR, e);
                }
                this.LOG.debug("执行节点数据初始化脚本{}", init.getBeforeShow());
            }
        }
        BpmContext.removeActionModel();
        return isStopExecute;
    }
    private String getFormHandler(T actionModel) {
        BpmForm form;
        String defId = actionModel.getDefId();
        if (actionModel instanceof TaskActionCmd) {
            TaskActionCmd cmd = (TaskActionCmd) actionModel;
            String nodeId = cmd.getNodeId();
            BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, nodeId);
            form = nodeDef.getForm();
        } else {
            BpmNodeDef nodeDef = this.bpmProcessDefService.getStartEvent(defId);
            form = nodeDef.getForm();
        }
        if (form == null || form.isFormEmpty()) {
            DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(defId);
            form = def.getGlobalForm();
        }
        if (form != null) {
            return form.getFormHandler();
        }
        return null;
    }
    public Boolean isDefault() {
        return Boolean.valueOf(true);
    }
    public String getDefaultBeforeScript() {
        return "";
    }
    protected abstract void doAction(T paramT);
    protected abstract boolean prepareActionDatas(T paramT);
    protected abstract void doActionBefore(T paramT);
    protected abstract void doActionAfter(T paramT);
}
 