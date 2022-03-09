/*     */ package cn.gwssi.ecloudbpm.wf.engine.data;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import cn.gwssi.ecloudbpm.form.api.model.FormCategory;
/*     */ import cn.gwssi.ecloudbpm.form.api.model.FormType;
/*     */ import cn.gwssi.ecloudbpm.form.api.model.IFormDef;
/*     */ import cn.gwssi.ecloudbpm.form.api.service.FormService;
/*     */ import cn.gwssi.ecloudbpm.wf.act.service.ActInstanceService;
/*     */ import cn.gwssi.ecloudbpm.wf.act.service.ActTaskService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.InstanceStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.button.ButtonFactory;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.ActionDisplayHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.data.BpmFlowDataAccessor;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.data.result.BpmFlowData;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.data.result.BpmFlowInstanceData;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.data.result.BpmFlowTaskData;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmDefProperties;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmVariableDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.NodeInit;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.NodeProperties;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.form.BpmForm;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.Button;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.BaseBpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmRightsFormService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.TaskIdentityLinkManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.TaskIdentityLink;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.data.handle.IBpmBusDataHandle;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessError;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.core.util.RequestContext;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.hutool.core.map.MapUtil;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class DefaultBpmFlowDataAccessor
/*     */   implements BpmFlowDataAccessor {
/*  65 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   @Resource
/*     */   private BpmRightsFormService bpmRightsFormService;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   private BpmTaskManager taskManager;
/*     */   @Resource
/*     */   private FormService formService;
/*     */   @Resource
/*     */   private IBpmBusDataHandle bpmBusDataHandle;
/*     */   @Resource
/*     */   private IGroovyScriptEngine iGroovyScriptEngine;
/*     */   @Resource
/*     */   private IBusinessDataService businessDataService;
/*     */   @Resource
/*     */   private ActTaskService actTaskService;
/*     */   @Resource
/*     */   private ActInstanceService actInstanceService;
/*     */   @Resource
/*     */   private TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   
/*     */   public BpmFlowInstanceData getInstanceData(String instanceId, FormType formType, String nodeId, String taskId) {
/*  92 */     BpmContext.cleanTread();
/*  93 */     BpmFlowInstanceData data = new BpmFlowInstanceData();
/*     */     
/*  95 */     BpmInstance instance = (BpmInstance)this.bpmInstanceManager.get(instanceId);
/*  96 */     data.setInstance((IBpmInstance)instance);
/*     */     
/*  98 */     getInstanceFormData((BpmFlowData)data, instanceId, nodeId, formType, true);
/*  99 */     handelBtns((BpmFlowData)data, nodeId, taskId, true);
/*     */     
/* 101 */     BpmNodeDef node = this.bpmProcessDefService.getBpmNodeDef(data.getDefId(), nodeId);
/*     */     
/* 103 */     handelOfficialDocument((BpmFlowData)data, nodeId);
/*     */     
/* 105 */     BpmDefProperties bpmDefProperties = ((DefaultBpmProcessDef)node.getBpmProcessDef()).getExtProperties();
/* 106 */     data.getConfigSpecified().setOfficialPrintTemplate(bpmDefProperties.getOfficialPrintTemplate());
/* 107 */     data.setLabels(bpmDefProperties.getLabels());
/* 108 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, IBusinessData> getTaskBusData(String taskId) {
/* 113 */     IBpmTask task = (IBpmTask)this.taskManager.get(taskId);
/* 114 */     if (task == null) return null;
/*     */     
/* 116 */     if (checkIsUrlForm(task)) return null;
/*     */     
/* 118 */     return this.bpmBusDataHandle.getInstanceBusData(task.getInstId(), null);
/*     */   }
/*     */   
/*     */   private boolean checkIsUrlForm(IBpmTask task) {
/* 122 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(task.getDefId());
/* 123 */     if (processDef.getGlobalForm() == null) {
/* 124 */       return true;
/*     */     }
/* 126 */     return (processDef.getGlobalForm().getType() == FormCategory.FRAME);
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmFlowData getStartFlowData(String defId, String instId, String taskId, FormType formType, Boolean readonly) {
/* 131 */     BpmContext.cleanTread();
/* 132 */     if (StringUtil.isEmpty(instId) && StringUtil.isEmpty(defId)) {
/* 133 */       throw new BusinessException("获取发起流程数据失败!流程定义id或者实例id缺失", BpmStatusCode.PARAM_ILLEGAL);
/*     */     }
/* 135 */     BpmFlowInstanceData data = new BpmFlowInstanceData();
/*     */     
/* 137 */     if (StringUtil.isEmpty(instId)) {
/* 138 */       data.setDefId(defId);
/* 139 */       getStartFormData(data, formType);
/*     */       
/* 141 */       DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 142 */       List<BpmVariableDef> bpmVariableDefs = bpmProcessDef.getVariableList();
/* 143 */       Map<String, Object> variables = new HashMap<>();
/* 144 */       for (BpmVariableDef bpmVariableDef : bpmVariableDefs) {
/* 145 */         variables.put(bpmVariableDef.getKey(), bpmVariableDef.getDefaultVal());
/*     */       }
/* 147 */       data.setVariables(variables);
/*     */     } else {
/* 149 */       BpmInstance instance = (BpmInstance)this.bpmInstanceManager.get(instId);
/* 150 */       data.setInstance((IBpmInstance)instance);
/* 151 */       BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getStartEvent(instance.getDefId());
/* 152 */       getInstanceFormData((BpmFlowData)data, instId, readonly.booleanValue() ? "" : bpmNodeDef.getNodeId(), formType, readonly.booleanValue());
/* 153 */       defId = instance.getDefId();
/*     */       
/*     */       try {
/* 156 */         if (!StringUtils.equals(InstanceStatus.STATUS_END.getKey(), instance.getStatus()) && 
/* 157 */           !StringUtils.equals(InstanceStatus.STATUS_MANUAL_END.getKey(), instance.getStatus()) && 
/* 158 */           !StringUtils.equals(InstanceStatus.STATUS_DRAFT.getKey(), instance.getStatus()) && 
/* 159 */           !StringUtils.equals(InstanceStatus.STATUS_REVOKE.getKey(), instance.getStatus())) {
/* 160 */           data.setVariables(this.actInstanceService.getVariables(instance.getActInstId()));
/*     */         }
/* 162 */       } catch (Exception e) {
/* 163 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 167 */     BpmNodeDef startNode = this.bpmProcessDefService.getStartEvent(data.getDefId());
/* 168 */     handelBtns((BpmFlowData)data, startNode.getNodeId(), taskId, readonly.booleanValue());
/*     */ 
/*     */     
/* 171 */     handelOfficialDocument((BpmFlowData)data, startNode.getNodeId());
/*     */     
/* 173 */     BpmDefProperties bpmDefProperties = ((DefaultBpmProcessDef)startNode.getBpmProcessDef()).getExtProperties();
/* 174 */     data.getConfigSpecified().setOfficialPrintTemplate(bpmDefProperties.getOfficialPrintTemplate());
/* 175 */     if (!readonly.booleanValue() && StringUtils.isNotEmpty(startNode.getNodeProperties().getLabels())) {
/* 176 */       data.setLabels(startNode.getNodeProperties().getLabels());
/*     */     } else {
/* 178 */       data.setLabels(bpmDefProperties.getLabels());
/*     */     } 
/* 180 */     return (BpmFlowData)data;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmFlowData getFlowTaskData(String taskId, String taskLinkId, FormType formType) {
/* 186 */     BpmContext.cleanTread();
/*     */     
/* 188 */     BpmFlowTaskData taskData = new BpmFlowTaskData();
/*     */     
/* 190 */     IBpmTask task = (IBpmTask)this.taskManager.get(taskId);
/* 191 */     if (task == null) {
/* 192 */       throw new BusinessMessage("任务可能已经办理完成", BpmStatusCode.TASK_NOT_FOUND);
/*     */     }
/* 194 */     ((BpmTask)task).setDefKey(this.bpmProcessDefService.getBpmProcessDef(task.getDefId()).getDefKey());
/* 195 */     taskData.setTask(task);
/* 196 */     if (StringUtils.isNotEmpty(taskLinkId)) {
/* 197 */       TaskIdentityLink taskIdentityLink = (TaskIdentityLink)this.taskIdentityLinkManager.get(taskLinkId);
/* 198 */       if (taskIdentityLink != null) {
/* 199 */         taskData.setTaskOrgId(taskIdentityLink.getOrgId());
/*     */       }
/*     */     } 
/* 202 */     getInstanceFormData((BpmFlowData)taskData, task.getInstId(), task.getNodeId(), formType, false);
/*     */     
/* 204 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
/*     */     
/* 206 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(task.getDefId());
/*     */     
/* 208 */     handelBtns((BpmFlowData)taskData, task.getNodeId(), task.getId(), false);
/*     */     
/* 210 */     handelOfficialDocument((BpmFlowData)taskData, task.getNodeId());
/*     */ 
/*     */     
/*     */     try {
/* 214 */       taskData.setVariables(this.actTaskService.getVariables(task.getTaskId()));
/* 215 */     } catch (Exception e) {
/* 216 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 219 */     taskData.getConfigSpecified().setOfficialPrintTemplate(def.getExtProperties().getOfficialPrintTemplate());
/*     */     
/* 221 */     bpmNodeDef.getOutcomeNodes().forEach(eTask -> {
/*     */           if (StringUtils.equals(NodeType.END.getKey(), eTask.getType().getKey())) {
/*     */             taskData.setEndTask(Boolean.valueOf(true));
/*     */           }
/*     */         });
/* 226 */     if (StringUtils.isEmpty(bpmNodeDef.getNodeProperties().getLabels())) {
/* 227 */       taskData.setLabels(def.getExtProperties().getLabels());
/*     */     } else {
/* 229 */       taskData.setLabels(bpmNodeDef.getNodeProperties().getLabels());
/*     */     } 
/* 231 */     taskData.setJavaScript(bpmNodeDef.getNodeProperties().getJavaScript());
/* 232 */     return (BpmFlowData)taskData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getStartFormData(BpmFlowInstanceData flowData, FormType formType) {
/* 242 */     String defId = flowData.getDefId();
/* 243 */     BpmNodeDef startNode = this.bpmProcessDefService.getStartEvent(defId);
/* 244 */     flowData.setDefName(this.bpmProcessDefService.getBpmProcessDef(defId).getName());
/*     */ 
/*     */     
/* 247 */     IBusinessPermission permission = this.bpmRightsFormService.getInstanceFormPermission((BpmFlowData)flowData, startNode.getNodeId(), formType, false);
/* 248 */     BpmForm form = flowData.getForm();
/*     */     
/* 250 */     if (FormCategory.INNER.equals(form.getType())) {
/* 251 */       Map<String, IBusinessData> dataMap = this.bpmBusDataHandle.getInitData(permission, defId);
/*     */       
/* 253 */       IFormDef formDef = this.formService.getByFormKey(form.getFormValue());
/* 254 */       if (formDef == null) {
/* 255 */         throw new BusinessException(form.getName() + "丢失", BpmStatusCode.FLOW_FORM_LOSE);
/*     */       }
/*     */       
/* 258 */       form.setFormHtml(formDef.getHtml());
/* 259 */       flowData.setDataMap(dataMap);
/*     */       
/* 261 */       handleShowJsonData((BpmFlowData)flowData, startNode.getNodeId());
/*     */     } else {
/* 263 */       handleFormUrl(form, null);
/*     */     } 
/* 265 */     handleProcessDefSet((BpmFlowData)flowData, startNode.getNodeId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getInstanceFormData(BpmFlowData flowData, String instaneId, String nodeId, FormType formType, boolean isReadOnly) {
/* 279 */     BpmInstance instance = (BpmInstance)this.bpmInstanceManager.get(instaneId);
/*     */ 
/*     */     
/* 282 */     IBusinessPermission businessPermission = this.bpmRightsFormService.getInstanceFormPermission(flowData, nodeId, formType, isReadOnly);
/*     */     
/* 284 */     BpmForm form = flowData.getForm();
/* 285 */     if (FormCategory.INNER.equals(form.getType())) {
/* 286 */       Map<String, IBusinessData> dataModel = this.bpmBusDataHandle.getInstanceData(businessPermission, instance);
/* 287 */       flowData.setDataMap(dataModel);
/*     */       
/* 289 */       IFormDef formDef = this.formService.getByFormKey(form.getFormValue());
/* 290 */       if (formDef == null) {
/* 291 */         throw new BusinessException(form.getFormValue(), BpmStatusCode.FLOW_FORM_LOSE);
/*     */       }
/*     */ 
/*     */       
/* 295 */       form.setFormHtml(formDef.getHtml());
/*     */ 
/*     */       
/* 298 */       handleShowJsonData(flowData, nodeId);
/*     */     } 
/* 300 */     handleFormUrl(form, (IBpmInstance)instance);
/* 301 */     handleProcessDefSet(flowData, nodeId);
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleShowJsonData(BpmFlowData flowData, String nodeId) {
/* 306 */     Map<String, IBusinessData> bos = flowData.getDataMap();
/* 307 */     if (MapUtil.isEmpty(bos))
/*     */       return; 
/* 309 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(flowData.getDefId());
/*     */ 
/*     */     
/* 312 */     Map<String, Object> param = new HashMap<>();
/* 313 */     param.putAll(bos);
/* 314 */     if (flowData instanceof BpmFlowTaskData) {
/* 315 */       param.put("bpmTask", ((BpmFlowTaskData)flowData).getTask());
/* 316 */     } else if (flowData instanceof BpmFlowInstanceData) {
/* 317 */       param.put("bpmInstance", ((BpmFlowInstanceData)flowData).getInstance());
/*     */     } 
/*     */ 
/*     */     
/* 321 */     for (NodeInit init : def.getNodeInitList(nodeId)) {
/* 322 */       if (StringUtil.isNotEmpty(init.getBeforeShow())) {
/*     */         try {
/* 324 */           this.iGroovyScriptEngine.execute(init.getBeforeShow(), param);
/* 325 */         } catch (Exception e) {
/* 326 */           throw new BusinessError("执行脚本初始化失败", BpmStatusCode.FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR, e);
/*     */         } 
/* 328 */         this.LOG.debug("执行节点数据初始化脚本{}", init.getBeforeShow());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 333 */     JSONObject json = new JSONObject();
/* 334 */     JSONObject initJson = new JSONObject();
/* 335 */     for (String key : bos.keySet()) {
/* 336 */       IBusinessData bd = bos.get(key);
/* 337 */       JSONObject boJson = this.businessDataService.assemblyFormDefData(bd);
/* 338 */       json.put(key, boJson);
/*     */ 
/*     */       
/* 341 */       bd.fullBusDataInitData(initJson);
/*     */     } 
/*     */     
/* 344 */     flowData.setData(json);
/* 345 */     flowData.setInitData(initJson);
/*     */   }
/*     */ 
/*     */   
/*     */   private void handelOfficialDocument(BpmFlowData flowData, String nodeId) {
/* 350 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(flowData.getDefId(), nodeId);
/* 351 */     NodeProperties nodeProperties = bpmNodeDef.getNodeProperties();
/* 352 */     BpmDefProperties extProperties = ((DefaultBpmProcessDef)bpmNodeDef.getBpmProcessDef()).getExtProperties();
/* 353 */     if (extProperties.isOfficialDocumentEnable()) {
/* 354 */       Map<String, Object> officialDocumentMap = new HashMap<>();
/* 355 */       officialDocumentMap.put("officialDocumentEnable", Boolean.valueOf(extProperties.isOfficialDocumentEnable()));
/* 356 */       officialDocumentMap.put("officialDocumentTemplate", extProperties.getOfficialDocumentTemplate());
/* 357 */       officialDocumentMap.put("officialDocumentPermission", nodeProperties.getOfficialDocumentPermission());
/* 358 */       officialDocumentMap.put("officialDocumentBtnPermission", nodeProperties.getOfficialDocumentBtnPermission());
/* 359 */       flowData.getConfigSpecified().setOfficialDocument(officialDocumentMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handelBtns(BpmFlowData flowData, String nodeId, String taskId, boolean isReadOnly) {
/*     */     LinkedHashSet<Button> buttons;
/* 372 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(flowData.getDefId(), nodeId);
/* 373 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(flowData.getDefId());
/*     */ 
/*     */     
/* 376 */     if (isReadOnly) {
/* 377 */       buttons = new LinkedHashSet<>(def.getInstanceBtnList());
/*     */     } else {
/* 379 */       buttons = new LinkedHashSet<>(nodeDef.getButtons());
/*     */     } 
/* 381 */     buttons.addAll(ButtonFactory.getBuiltButtons());
/*     */     
/* 383 */     Map<String, Object> param = new HashMap<>();
/* 384 */     param.put("bpmProcessDef", def);
/* 385 */     param.put("bpmNodeDef", nodeDef);
/* 386 */     if (MapUtil.isNotEmpty(flowData.getDataMap())) {
/* 387 */       param.putAll(flowData.getDataMap());
/*     */     }
/* 389 */     IBpmInstance bpmInstance = null;
/* 390 */     if (flowData instanceof BpmFlowTaskData) {
/* 391 */       IBpmTask task = ((BpmFlowTaskData)flowData).getTask();
/* 392 */       param.put("task", task);
/* 393 */       param.put("bpmTask", task);
/* 394 */       bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(task.getInstId());
/* 395 */       param.put("instance", bpmInstance);
/* 396 */       param.put("bpmInstance", bpmInstance);
/* 397 */     } else if (flowData instanceof BpmFlowInstanceData) {
/* 398 */       bpmInstance = ((BpmFlowInstanceData)flowData).getInstance();
/* 399 */       param.put("instance", bpmInstance);
/* 400 */       param.put("bpmInstance", bpmInstance);
/*     */     } 
/* 402 */     BpmTask bpmTask = new BpmTask();
/* 403 */     bpmTask.setId(taskId);
/* 404 */     List<Button> btns = new ArrayList<>();
/*     */     
/* 406 */     for (Button btn : buttons) {
/* 407 */       ActionDisplayHandler actionDisplayHandler = ButtonFactory.getActionDisplayHandler(btn.getAlias());
/* 408 */       if (actionDisplayHandler != null && !actionDisplayHandler.isDisplay(isReadOnly, nodeDef, bpmInstance, (IBpmTask)bpmTask)) {
/*     */         continue;
/*     */       }
/* 411 */       if (StringUtil.isNotEmpty(btn.getGroovyScript())) {
/*     */         
/* 413 */         try { boolean result = this.iGroovyScriptEngine.executeBoolean(btn.getGroovyScript(), param);
/* 414 */           this.LOG.debug("任务节点按钮Groovy脚本{},执行结果{}", btn.getGroovyScript(), Boolean.valueOf(result));
/* 415 */           if (!result)
/* 416 */             continue;  } catch (Exception e)
/* 417 */         { throw new BusinessError("按钮脚本执行失败，脚本：" + btn.getGroovyScript(), BpmStatusCode.FLOW_DATA_GET_BUTTONS_ERROR, e); }
/*     */       
/*     */       }
/* 420 */       btns.add(btn);
/*     */     } 
/*     */ 
/*     */     
/* 424 */     btns = ButtonFactory.specialTaskBtnHandler(btns, flowData);
/*     */     
/* 426 */     btns = ButtonFactory.specialTaskBtnByUserHandler(btns, flowData);
/* 427 */     flowData.setButtonList(btns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleFormUrl(BpmForm form, IBpmInstance instance) {
/* 439 */     if (form == null || form.isFormEmpty() || FormCategory.INNER == form.getType())
/*     */       return; 
/* 441 */     String bizId = (instance == null) ? "" : instance.getBizKey();
/*     */ 
/*     */     
/* 444 */     String url = form.getFormValue().replace("{bizId}", bizId);
/*     */ 
/*     */     
/* 447 */     if (url.indexOf("{token}") != -1) {
/* 448 */       HttpServletRequest request = RequestContext.getHttpServletRequest();
/* 449 */       String token = "";
/* 450 */       if (request != null && request.getSession().getAttribute("token_") != null) {
/* 451 */         token = (String)request.getSession().getAttribute("token_");
/*     */       }
/*     */       
/* 454 */       url = url.replace("{token}", token);
/*     */     } 
/*     */     
/* 457 */     form.setFormValue(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleProcessDefSet(BpmFlowData flowData, String nodeId) {
/* 467 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(flowData.getDefId());
/* 468 */     if (StringUtil.isNotEmpty(nodeId)) {
/* 469 */       BaseBpmNodeDef nodeDef = (BaseBpmNodeDef)this.bpmProcessDefService.getBpmNodeDef(flowData.getDefId(), nodeId);
/* 470 */       if (nodeDef != null) {
/* 471 */         Boolean isRequiredOpinion = nodeDef.getNodeProperties().getRequiredOpinion();
/* 472 */         if (isRequiredOpinion == null) {
/* 473 */           isRequiredOpinion = Boolean.valueOf(def.getExtProperties().isRequiredOpinion());
/*     */         }
/* 475 */         flowData.getConfigSpecified().setRequiredOpinion(isRequiredOpinion);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 480 */     flowData.getConfigSpecified().setRequiredOpinion(Boolean.valueOf(def.getExtProperties().isRequiredOpinion()));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/data/DefaultBpmFlowDataAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */