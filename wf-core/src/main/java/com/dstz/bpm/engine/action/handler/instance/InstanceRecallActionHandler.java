package com.dstz.bpm.engine.action.handler.instance;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.JsonUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.bpm.act.service.ActTaskService;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.action.handler.ActionDisplayHandler;
import com.dstz.bpm.api.engine.action.handler.ActionHandler;
import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.data.result.BpmFlowData;
import com.dstz.bpm.api.engine.data.result.BpmFlowTaskData;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.api.service.BpmRightsFormService;
import com.dstz.bpm.core.manager.*;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bus.api.model.IBusinessData;
import com.dstz.bus.api.model.IBusinessPermission;
import com.dstz.form.api.model.FormType;
import com.dstz.sys.api.jms.model.DefaultJmsDTO;
import com.dstz.sys.api.jms.model.JmsDTO;
import com.dstz.sys.api.jms.model.msg.NotifyMessage;
import com.dstz.sys.api.jms.producer.JmsProducer;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class InstanceRecallActionHandler
        implements ActionHandler<DefualtTaskActionCmd>, ActionDisplayHandler<DefualtTaskActionCmd> {
    @Autowired
    private BpmTaskManager taskManager;
    @Resource
    private JmsProducer jmsProducer;
    @Autowired
    private BpmProcessDefService bpmProcessDefService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private BpmRightsFormService bpmRightsFormService;
    @Autowired
    private BpmInstanceManager bpmInstanceManager;
    @Autowired
    private IBpmBusDataHandle bpmBusDataHandle;
    @Autowired
    private BpmTaskOpinionManager bpmTaskOpinionManager;
    @Autowired
    private TaskIdentityLinkManager identityLinkManager;
    @Resource
    private BpmTaskStackManager bpmTaskStackManager;
    public Boolean isDefault() {
        return Boolean.TRUE;
    }
    public String getDefaultBeforeScript() {
        return "";
    }
    public void execute(DefualtTaskActionCmd model) {
        if (model.getBpmInstance() == null) {
            model.setBpmInstance((IBpmInstance) this.bpmInstanceManager.get(model.getInstanceId()));
        }
        BpmTask bpmTask = new BpmTask();
        bpmTask.setId(model.getTaskId());
        String nodeId = model.getExtendConf().getString("nodeId");
        BpmNodeDef bpmNodeDef = null;
        if (StringUtil.isEmpty(nodeId)) {
            bpmNodeDef = this.bpmProcessDefService.getStartEvent(model.getDefId());
            nodeId = ((BpmNodeDef) bpmNodeDef.getOutcomeTaskNodes().get(0)).getNodeId();
        } else {
            bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), nodeId);
        }
        if (!isDisplay(false, bpmNodeDef, model.getBpmInstance(), (IBpmTask) bpmTask)) {
            throw new BusinessException("操作受限,您没有该操作权限", BpmStatusCode.NO_PERMISSION);
        }
        List<BpmTask> bpmTasks = this.taskManager.getByInstId(model.getInstanceId());
        BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(model.getTaskId());
        String node = nodeId;
        List<SysIdentity> sysIdentities = model.getBpmIdentity(nodeId);
        DefaultIdentity defaultIdentity = new DefaultIdentity();
        if (CollectionUtils.isNotEmpty(sysIdentities)) {
            SysIdentity sysIdentity = sysIdentities.get(0);
        } else {
            defaultIdentity = new DefaultIdentity();
            defaultIdentity.setId(bpmTaskOpinion.getApprover());
            defaultIdentity.setName(bpmTaskOpinion.getApproverName());
            defaultIdentity.setOrgId(bpmTaskOpinion.getTaskOrgId());
            defaultIdentity.setType("user");
        }
        model.setApproveOrgId(bpmTaskOpinion.getTaskOrgId());
        for (BpmTask task : bpmTasks) {
            if (!StringUtils.equals(task.getTaskType(), TaskType.SIGN.getKey())) {
                List<JmsDTO> notifyMessageList = recallNode(model, (BpmInstance) model.getBpmInstance(), task, node, (SysIdentity) defaultIdentity);
                if (CollectionUtils.isNotEmpty(notifyMessageList)) {
                    this.jmsProducer.sendToQueue(notifyMessageList);
                }
            }
            if (StringUtils.equals(task.getTaskType(), TaskType.SIGN.getKey())) {
                removeSignTaskIdentity(task);
            }
        }
    }
    private List<JmsDTO> recallNode(DefualtTaskActionCmd model, BpmInstance bpmInstance, BpmTask bpmTask, String nodeId, SysIdentity identitys) {
        this.taskManager.recycleTask(bpmTask.getId(), OpinionStatus.RECALL, "用户撤回需要回收该任务");
        DefualtTaskActionCmd defualtTaskActionCmd = new DefualtTaskActionCmd();
        defualtTaskActionCmd.addBpmIdentity(nodeId, identitys);
        defualtTaskActionCmd.setDefId(model.getDefId());
        defualtTaskActionCmd.setBpmInstance((IBpmInstance) bpmInstance);
        defualtTaskActionCmd.setInstanceId(bpmInstance.getId());
        defualtTaskActionCmd.setOpinion(model.getOpinion());
        defualtTaskActionCmd.setDestination(nodeId);
        defualtTaskActionCmd.setDataMode("bo");
        defualtTaskActionCmd.setActionName(ActionType.RECALL.getKey());
        defualtTaskActionCmd.setApproveOrgId(model.getApproveOrgId());
        String[] msgTypes = StringUtils.split(JsonUtil.getString(model.getExtendConf(), "msgType"), ',');
        List<JmsDTO> notifyMessageList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty((Object[]) msgTypes)) {
            NotifyMessage message = new NotifyMessage(String.format("[%s]任务撤回提醒", new Object[]{bpmTask.getName()}), model.getOpinion(), ContextUtil.getCurrentUser(), this.taskManager.getAssignUserById(bpmTask));
            for (String type : msgTypes) {
                notifyMessageList.add(new DefaultJmsDTO(type, message));
            }
        }
        BpmFlowTaskData bpmFlowTaskData = new BpmFlowTaskData();
        bpmFlowTaskData.setTask((IBpmTask) bpmTask);
        IBusinessPermission businessPermission = this.bpmRightsFormService.getInstanceFormPermission((BpmFlowData) bpmFlowTaskData, bpmTask.getNodeId(), FormType.PC, false);
        Map<String, IBusinessData> taskBusData = this.bpmBusDataHandle.getInstanceBusData(bpmTask.getInstId(), businessPermission);
        defualtTaskActionCmd.setTaskId(bpmTask.getTaskId());
        defualtTaskActionCmd.setBpmTask((IBpmTask) bpmTask);
        defualtTaskActionCmd.setBizDataMap(taskBusData);
        try {
            BpmContext.setActionModel((ActionCmd) defualtTaskActionCmd);
            this.actTaskService.completeTask(bpmTask.getTaskId(), new String[]{nodeId});
        } finally {
            BpmContext.cleanTread();
        }
        return notifyMessageList;
    }
    private void removeSignTaskIdentity(BpmTask bpmTask) {
        this.identityLinkManager.removeByTaskId(bpmTask.getId());
        this.taskManager.remove(bpmTask.getId());
        this.bpmTaskOpinionManager.removeByTaskId(bpmTask.getId());
    }
    public ActionType getActionType() {
        return ActionType.RECALL;
    }
    public int getSn() {
        return 7;
    }
    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf((nodeDef == null || nodeDef.getType() == null));
    }
    public String getConfigPage() {
        return "/bpm/task/taskOpinionDialog.html";
    }
    public String getDefaultGroovyScript() {
        return "";
    }
    public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
        if (bpmInstance == null || bpmNodeDef == null) return false;
        if (bpmInstance.getEndTime() != null || bpmInstance.getIsForbidden().shortValue() == 1 || "forbidden".equals(((DefaultBpmProcessDef) bpmNodeDef.getBpmProcessDef()).getExtProperties().getStatus())) {
            return false;
        }
        if (Boolean.FALSE.equals(bpmNodeDef.getNodeProperties().getAllowRecall()))
            return false;
        if (bpmNodeDef.getNodeProperties().getAllowRecall() == null) {
            DefaultBpmProcessDef defaultBpmProcessDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(bpmInstance.getDefId());
            if (Boolean.FALSE.equals(Boolean.valueOf(defaultBpmProcessDef.getExtProperties().isAllowRecall()))) {
                return false;
            }
        }
        if (NodeType.START.equals(bpmNodeDef.getType())) {
            return false;
        }
        if (bpmTask == null || StringUtil.isEmpty(bpmTask.getId())) {
            return false;
        }
        BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
        if (bpmTaskOpinion == null || StringUtils.contains(bpmTaskOpinion.getStatus(), "sign")) {
            return false;
        }
        ((BpmTask) bpmTask).setInstId(bpmTaskOpinion.getInstId());
        ((BpmTask) bpmTask).setNodeId(bpmTaskOpinion.getTaskKey());
        List<BpmTask> bpmTasks = this.taskManager.getByInstId(bpmInstance.getId());
        if (CollectionUtils.isEmpty(bpmTasks)) {
            return false;
        }
        List<IExtendTaskAction> taskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
        for (IExtendTaskAction taskAction : taskActions) {
            if (taskAction.isContainNode(bpmNodeDef.getNodeId(), this.bpmProcessDefService.getBpmProcessDef(bpmInstance.getDefId()), "none")) {
                return false;
            }
        }
        DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
        defaultQueryFilter.addParamsFilter("taskId", bpmTask.getId());
        defaultQueryFilter.addFieldSort("level", "asc");
        defaultQueryFilter.addParamsFilter("prior", "FORWARD");
        defaultQueryFilter.addFieldSort("id_", "asc");
        defaultQueryFilter.addFilter("level", Integer.valueOf(1), QueryOP.GREAT);
        defaultQueryFilter.addFilter("level", Integer.valueOf(5), QueryOP.LESS);
        List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter) defaultQueryFilter);
        BpmNodeDef parentNodeDef = bpmNodeDef.getParentBpmNodeDef();
        SubProcessNodeDef isSubNodeDef = null;
        if (parentNodeDef != null && parentNodeDef instanceof SubProcessNodeDef) {
            isSubNodeDef = (SubProcessNodeDef) parentNodeDef;
        }
        boolean canCall = false;
        for (BpmTaskStack stack : bpmTaskStacks) {
            for (BpmTask existTask : bpmTasks) {
                if (StringUtils.equals(existTask.getId(), stack.getTaskId())) {
                    canCall = true;
                    BpmNodeDef eBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), existTask.getNodeId());
                    BpmNodeDef eParentNodeDef = eBpmNodeDef.getParentBpmNodeDef();
                    if (isSubNodeDef == null && eParentNodeDef != null && eParentNodeDef instanceof SubProcessNodeDef) {
                        canCall = false;
                    }
                    if (isSubNodeDef != null) {
                        if (eParentNodeDef == null || !(eParentNodeDef instanceof SubProcessNodeDef)) {
                            canCall = false;
                        }
                        if (eParentNodeDef != null && eParentNodeDef instanceof SubProcessNodeDef &&
                                !StringUtils.equals(eParentNodeDef.getNodeId(), isSubNodeDef.getNodeId())) {
                            canCall = false;
                        }
                    }
                    if (StringUtils.equals(TaskType.SUPERVISE.getKey(), existTask.getTaskType())) {
                        return false;
                    }
                    if (StringUtils.equals("SIGN", existTask.getTaskType())) {
                        return false;
                    }
                    for (IExtendTaskAction taskAction : taskActions) {
                        if (!taskAction.canRecall((IBpmTask) existTask)) {
                            return false;
                        }
                    }
                }
            }
        }
        return canCall;
    }
}
