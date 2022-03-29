package com.dstz.bpm.engine.data;
import com.dstz.bus.api.model.IBusinessData;
import com.dstz.bus.api.model.IBusinessPermission;
import com.dstz.bus.api.service.IBusinessDataService;
import com.dstz.form.api.model.FormCategory;
import com.dstz.form.api.model.FormType;
import com.dstz.form.api.model.IFormDef;
import com.dstz.form.api.service.FormService;
import com.dstz.bpm.act.service.ActInstanceService;
import com.dstz.bpm.act.service.ActTaskService;
import com.dstz.bpm.api.constant.InstanceStatus;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.engine.action.button.ButtonFactory;
import com.dstz.bpm.api.engine.action.handler.ActionDisplayHandler;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
import com.dstz.bpm.api.engine.data.result.BpmFlowData;
import com.dstz.bpm.api.engine.data.result.BpmFlowInstanceData;
import com.dstz.bpm.api.engine.data.result.BpmFlowTaskData;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.def.BpmDefProperties;
import com.dstz.bpm.api.model.def.BpmVariableDef;
import com.dstz.bpm.api.model.def.NodeInit;
import com.dstz.bpm.api.model.def.NodeProperties;
import com.dstz.bpm.api.model.form.BpmForm;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.Button;
import com.dstz.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.api.service.BpmRightsFormService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.base.api.exception.BusinessError;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.util.RequestContext;
import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Component
public class DefaultBpmFlowDataAccessor
        implements BpmFlowDataAccessor {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    private BpmInstanceManager bpmInstanceManager;
    @Resource
    private BpmRightsFormService bpmRightsFormService;
    @Resource
    private BpmProcessDefService bpmProcessDefService;
    @Resource
    private BpmTaskManager taskManager;
    @Resource
    private FormService formService;
    @Resource
    private IBpmBusDataHandle bpmBusDataHandle;
    @Resource
    private IGroovyScriptEngine iGroovyScriptEngine;
    @Resource
    private IBusinessDataService businessDataService;
    @Resource
    private ActTaskService actTaskService;
    @Resource
    private ActInstanceService actInstanceService;
    @Resource
    private TaskIdentityLinkManager taskIdentityLinkManager;
    public BpmFlowInstanceData getInstanceData(String instanceId, FormType formType, String nodeId, String taskId) {
        BpmContext.cleanTread();
        BpmFlowInstanceData data = new BpmFlowInstanceData();
        BpmInstance instance = (BpmInstance) this.bpmInstanceManager.get(instanceId);
        data.setInstance((IBpmInstance) instance);
        getInstanceFormData((BpmFlowData) data, instanceId, nodeId, formType, true);
        handelBtns((BpmFlowData) data, nodeId, taskId, true);
        BpmNodeDef node = this.bpmProcessDefService.getBpmNodeDef(data.getDefId(), nodeId);
        handelOfficialDocument((BpmFlowData) data, nodeId);
        BpmDefProperties bpmDefProperties = ((DefaultBpmProcessDef) node.getBpmProcessDef()).getExtProperties();
        data.getConfigSpecified().setOfficialPrintTemplate(bpmDefProperties.getOfficialPrintTemplate());
        data.setLabels(bpmDefProperties.getLabels());
        return data;
    }
    public Map<String, IBusinessData> getTaskBusData(String taskId) {
        IBpmTask task = (IBpmTask) this.taskManager.get(taskId);
        if (task == null) return null;
        if (checkIsUrlForm(task)) return null;
        return this.bpmBusDataHandle.getInstanceBusData(task.getInstId(), null);
    }
    private boolean checkIsUrlForm(IBpmTask task) {
        DefaultBpmProcessDef processDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(task.getDefId());
        if (processDef.getGlobalForm() == null) {
            return true;
        }
        return (processDef.getGlobalForm().getType() == FormCategory.FRAME);
    }
    public BpmFlowData getStartFlowData(String defId, String instId, String taskId, FormType formType, Boolean readonly) {
        BpmContext.cleanTread();
        if (StringUtil.isEmpty(instId) && StringUtil.isEmpty(defId)) {
            throw new BusinessException("获取发起流程数据失败!流程定义id或者实例id缺失", BpmStatusCode.PARAM_ILLEGAL);
        }
        BpmFlowInstanceData data = new BpmFlowInstanceData();
        if (StringUtil.isEmpty(instId)) {
            data.setDefId(defId);
            getStartFormData(data, formType);
            DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(defId);
            List<BpmVariableDef> bpmVariableDefs = bpmProcessDef.getVariableList();
            Map<String, Object> variables = new HashMap<>();
            for (BpmVariableDef bpmVariableDef : bpmVariableDefs) {
                variables.put(bpmVariableDef.getKey(), bpmVariableDef.getDefaultVal());
            }
            data.setVariables(variables);
        } else {
            BpmInstance instance = (BpmInstance) this.bpmInstanceManager.get(instId);
            data.setInstance((IBpmInstance) instance);
            BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getStartEvent(instance.getDefId());
            getInstanceFormData((BpmFlowData) data, instId, readonly.booleanValue() ? "" : bpmNodeDef.getNodeId(), formType, readonly.booleanValue());
            defId = instance.getDefId();
            try {
                if (!StringUtils.equals(InstanceStatus.STATUS_END.getKey(), instance.getStatus()) &&
                        !StringUtils.equals(InstanceStatus.STATUS_MANUAL_END.getKey(), instance.getStatus()) &&
                        !StringUtils.equals(InstanceStatus.STATUS_DRAFT.getKey(), instance.getStatus()) &&
                        !StringUtils.equals(InstanceStatus.STATUS_REVOKE.getKey(), instance.getStatus())) {
                    data.setVariables(this.actInstanceService.getVariables(instance.getActInstId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BpmNodeDef startNode = this.bpmProcessDefService.getStartEvent(data.getDefId());
        handelBtns((BpmFlowData) data, startNode.getNodeId(), taskId, readonly.booleanValue());
        handelOfficialDocument((BpmFlowData) data, startNode.getNodeId());
        BpmDefProperties bpmDefProperties = ((DefaultBpmProcessDef) startNode.getBpmProcessDef()).getExtProperties();
        data.getConfigSpecified().setOfficialPrintTemplate(bpmDefProperties.getOfficialPrintTemplate());
        if (!readonly.booleanValue() && StringUtils.isNotEmpty(startNode.getNodeProperties().getLabels())) {
            data.setLabels(startNode.getNodeProperties().getLabels());
        } else {
            data.setLabels(bpmDefProperties.getLabels());
        }
        return (BpmFlowData) data;
    }
    public BpmFlowData getFlowTaskData(String taskId, String taskLinkId, FormType formType) {
        BpmContext.cleanTread();
        BpmFlowTaskData taskData = new BpmFlowTaskData();
        IBpmTask task = (IBpmTask) this.taskManager.get(taskId);
        if (task == null) {
            throw new BusinessMessage("任务可能已经办理完成", BpmStatusCode.TASK_NOT_FOUND);
        }
        ((BpmTask) task).setDefKey(this.bpmProcessDefService.getBpmProcessDef(task.getDefId()).getDefKey());
        taskData.setTask(task);
        if (StringUtils.isNotEmpty(taskLinkId)) {
            TaskIdentityLink taskIdentityLink = (TaskIdentityLink) this.taskIdentityLinkManager.get(taskLinkId);
            if (taskIdentityLink != null) {
                taskData.setTaskOrgId(taskIdentityLink.getOrgId());
            }
        }
        getInstanceFormData((BpmFlowData) taskData, task.getInstId(), task.getNodeId(), formType, false);
        BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
        DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(task.getDefId());
        handelBtns((BpmFlowData) taskData, task.getNodeId(), task.getId(), false);
        handelOfficialDocument((BpmFlowData) taskData, task.getNodeId());
        try {
            taskData.setVariables(this.actTaskService.getVariables(task.getTaskId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        taskData.getConfigSpecified().setOfficialPrintTemplate(def.getExtProperties().getOfficialPrintTemplate());
        bpmNodeDef.getOutcomeNodes().forEach(eTask -> {
            if (StringUtils.equals(NodeType.END.getKey(), eTask.getType().getKey())) {
                taskData.setEndTask(Boolean.valueOf(true));
            }
        });
        if (StringUtils.isEmpty(bpmNodeDef.getNodeProperties().getLabels())) {
            taskData.setLabels(def.getExtProperties().getLabels());
        } else {
            taskData.setLabels(bpmNodeDef.getNodeProperties().getLabels());
        }
        taskData.setJavaScript(bpmNodeDef.getNodeProperties().getJavaScript());
        return (BpmFlowData) taskData;
    }
    private void getStartFormData(BpmFlowInstanceData flowData, FormType formType) {
        String defId = flowData.getDefId();
        BpmNodeDef startNode = this.bpmProcessDefService.getStartEvent(defId);
        flowData.setDefName(this.bpmProcessDefService.getBpmProcessDef(defId).getName());
        IBusinessPermission permission = this.bpmRightsFormService.getInstanceFormPermission((BpmFlowData) flowData, startNode.getNodeId(), formType, false);
        BpmForm form = flowData.getForm();
        if (FormCategory.INNER.equals(form.getType())) {
            Map<String, IBusinessData> dataMap = this.bpmBusDataHandle.getInitData(permission, defId);
            IFormDef formDef = this.formService.getByFormKey(form.getFormValue());
            if (formDef == null) {
                throw new BusinessException(form.getName() + "丢失", BpmStatusCode.FLOW_FORM_LOSE);
            }
            form.setFormHtml(formDef.getHtml());
            flowData.setDataMap(dataMap);
            handleShowJsonData((BpmFlowData) flowData, startNode.getNodeId());
        } else {
            handleFormUrl(form, null);
        }
        handleProcessDefSet((BpmFlowData) flowData, startNode.getNodeId());
    }
    private void getInstanceFormData(BpmFlowData flowData, String instaneId, String nodeId, FormType formType, boolean isReadOnly) {
        BpmInstance instance = (BpmInstance) this.bpmInstanceManager.get(instaneId);
        IBusinessPermission businessPermission = this.bpmRightsFormService.getInstanceFormPermission(flowData, nodeId, formType, isReadOnly);
        BpmForm form = flowData.getForm();
        if (FormCategory.INNER.equals(form.getType())) {
            Map<String, IBusinessData> dataModel = this.bpmBusDataHandle.getInstanceData(businessPermission, instance);
            flowData.setDataMap(dataModel);
            IFormDef formDef = this.formService.getByFormKey(form.getFormValue());
            if (formDef == null) {
                throw new BusinessException(form.getFormValue(), BpmStatusCode.FLOW_FORM_LOSE);
            }
            form.setFormHtml(formDef.getHtml());
            handleShowJsonData(flowData, nodeId);
        }
        handleFormUrl(form, (IBpmInstance) instance);
        handleProcessDefSet(flowData, nodeId);
    }
    private void handleShowJsonData(BpmFlowData flowData, String nodeId) {
        Map<String, IBusinessData> bos = flowData.getDataMap();
        if (MapUtil.isEmpty(bos))
            return;
        DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(flowData.getDefId());
        Map<String, Object> param = new HashMap<>();
        param.putAll(bos);
        if (flowData instanceof BpmFlowTaskData) {
            param.put("bpmTask", ((BpmFlowTaskData) flowData).getTask());
        } else if (flowData instanceof BpmFlowInstanceData) {
            param.put("bpmInstance", ((BpmFlowInstanceData) flowData).getInstance());
        }
        for (NodeInit init : def.getNodeInitList(nodeId)) {
            if (StringUtil.isNotEmpty(init.getBeforeShow())) {
                try {
                    this.iGroovyScriptEngine.execute(init.getBeforeShow(), param);
                } catch (Exception e) {
                    throw new BusinessError("执行脚本初始化失败", BpmStatusCode.FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR, e);
                }
                this.LOG.debug("执行节点数据初始化脚本{}", init.getBeforeShow());
            }
        }
        JSONObject json = new JSONObject();
        JSONObject initJson = new JSONObject();
        for (String key : bos.keySet()) {
            IBusinessData bd = bos.get(key);
            JSONObject boJson = this.businessDataService.assemblyFormDefData(bd);
            json.put(key, boJson);
            bd.fullBusDataInitData(initJson);
        }
        flowData.setData(json);
        flowData.setInitData(initJson);
    }
    private void handelOfficialDocument(BpmFlowData flowData, String nodeId) {
        BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(flowData.getDefId(), nodeId);
        NodeProperties nodeProperties = bpmNodeDef.getNodeProperties();
        BpmDefProperties extProperties = ((DefaultBpmProcessDef) bpmNodeDef.getBpmProcessDef()).getExtProperties();
        if (extProperties.isOfficialDocumentEnable()) {
            Map<String, Object> officialDocumentMap = new HashMap<>();
            officialDocumentMap.put("officialDocumentEnable", Boolean.valueOf(extProperties.isOfficialDocumentEnable()));
            officialDocumentMap.put("officialDocumentTemplate", extProperties.getOfficialDocumentTemplate());
            officialDocumentMap.put("officialDocumentPermission", nodeProperties.getOfficialDocumentPermission());
            officialDocumentMap.put("officialDocumentBtnPermission", nodeProperties.getOfficialDocumentBtnPermission());
            flowData.getConfigSpecified().setOfficialDocument(officialDocumentMap);
        }
    }
    private void handelBtns(BpmFlowData flowData, String nodeId, String taskId, boolean isReadOnly) {
        LinkedHashSet<Button> buttons;
        BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(flowData.getDefId(), nodeId);
        DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(flowData.getDefId());
        if (isReadOnly) {
            buttons = new LinkedHashSet<>(def.getInstanceBtnList());
        } else {
            buttons = new LinkedHashSet<>(nodeDef.getButtons());
        }
        buttons.addAll(ButtonFactory.getBuiltButtons());
        Map<String, Object> param = new HashMap<>();
        param.put("bpmProcessDef", def);
        param.put("bpmNodeDef", nodeDef);
        if (MapUtil.isNotEmpty(flowData.getDataMap())) {
            param.putAll(flowData.getDataMap());
        }
        IBpmInstance bpmInstance = null;
        if (flowData instanceof BpmFlowTaskData) {
            IBpmTask task = ((BpmFlowTaskData) flowData).getTask();
            param.put("task", task);
            param.put("bpmTask", task);
            bpmInstance = (IBpmInstance) this.bpmInstanceManager.get(task.getInstId());
            param.put("instance", bpmInstance);
            param.put("bpmInstance", bpmInstance);
        } else if (flowData instanceof BpmFlowInstanceData) {
            bpmInstance = ((BpmFlowInstanceData) flowData).getInstance();
            param.put("instance", bpmInstance);
            param.put("bpmInstance", bpmInstance);
        }
        BpmTask bpmTask = new BpmTask();
        bpmTask.setId(taskId);
        List<Button> btns = new ArrayList<>();
        for (Button btn : buttons) {
            ActionDisplayHandler actionDisplayHandler = ButtonFactory.getActionDisplayHandler(btn.getAlias());
            if (actionDisplayHandler != null && !actionDisplayHandler.isDisplay(isReadOnly, nodeDef, bpmInstance, (IBpmTask) bpmTask)) {
                continue;
            }
            if (StringUtil.isNotEmpty(btn.getGroovyScript())) {
                try {
                    boolean result = this.iGroovyScriptEngine.executeBoolean(btn.getGroovyScript(), param);
                    this.LOG.debug("任务节点按钮Groovy脚本{},执行结果{}", btn.getGroovyScript(), Boolean.valueOf(result));
                    if (!result)
                        continue;
                } catch (Exception e) {
                    throw new BusinessError("按钮脚本执行失败，脚本：" + btn.getGroovyScript(), BpmStatusCode.FLOW_DATA_GET_BUTTONS_ERROR, e);
                }
            }
            btns.add(btn);
        }
        btns = ButtonFactory.specialTaskBtnHandler(btns, flowData);
        btns = ButtonFactory.specialTaskBtnByUserHandler(btns, flowData);
        flowData.setButtonList(btns);
    }
    private void handleFormUrl(BpmForm form, IBpmInstance instance) {
        if (form == null || form.isFormEmpty() || FormCategory.INNER == form.getType())
            return;
        String bizId = (instance == null) ? "" : instance.getBizKey();
        String url = form.getFormValue().replace("{bizId}", bizId);
        if (url.indexOf("{token}") != -1) {
            HttpServletRequest request = RequestContext.getHttpServletRequest();
            String token = "";
            if (request != null && request.getSession().getAttribute("token_") != null) {
                token = (String) request.getSession().getAttribute("token_");
            }
            url = url.replace("{token}", token);
        }
        form.setFormValue(url);
    }
    private void handleProcessDefSet(BpmFlowData flowData, String nodeId) {
        DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(flowData.getDefId());
        if (StringUtil.isNotEmpty(nodeId)) {
            BaseBpmNodeDef nodeDef = (BaseBpmNodeDef) this.bpmProcessDefService.getBpmNodeDef(flowData.getDefId(), nodeId);
            if (nodeDef != null) {
                Boolean isRequiredOpinion = nodeDef.getNodeProperties().getRequiredOpinion();
                if (isRequiredOpinion == null) {
                    isRequiredOpinion = Boolean.valueOf(def.getExtProperties().isRequiredOpinion());
                }
                flowData.getConfigSpecified().setRequiredOpinion(isRequiredOpinion);
                return;
            }
        }
        flowData.getConfigSpecified().setRequiredOpinion(Boolean.valueOf(def.getExtProperties().isRequiredOpinion()));
    }
}
