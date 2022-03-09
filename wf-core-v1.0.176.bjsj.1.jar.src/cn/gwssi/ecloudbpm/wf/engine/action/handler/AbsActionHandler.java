/*     */ package cn.gwssi.ecloudbpm.wf.engine.action.handler;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskSkipType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.InstanceActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.ActionHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.cmd.ExecutionCommand;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.cmd.TaskCommand;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.WorkFlowException;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmDataModel;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.NodeInit;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.form.BpmForm;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.TaskIdentityLinkManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.TaskIdentityLink;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.data.handle.BpmBusDataHandle;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.util.HandlerUtil;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.ThreadMapUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Transactional;
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
/*     */ public abstract class AbsActionHandler<T extends BaseActionCmd>
/*     */   implements ActionHandler<T>
/*     */ {
/*     */   public static final String SKIP_TASKIDS = "SKIP_TASKIDS";
/*  67 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   protected BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   @Resource
/*     */   protected BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   protected BpmBusDataHandle busDataHandle;
/*     */   @Resource
/*     */   protected TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   @Resource
/*     */   protected IBusinessDataService iBusinessDataService;
/*     */   @Resource
/*     */   protected IGroovyScriptEngine iGroovyScriptEngine;
/*     */   @Autowired
/*     */   protected BpmTaskManager bpmTaskManager;
/*     */   @Resource
/*     */   private ExecutionCommand executionCommand;
/*     */   @Resource
/*     */   protected TaskCommand taskCommand;
/*     */   
/*     */   @Transactional(rollbackFor = {Exception.class})
/*     */   public void execute(T model) {
/*  91 */     boolean isStopExecute = prepareActionDatas(model);
/*     */ 
/*     */     
/*  94 */     checkFlowIsValid((BaseActionCmd)model);
/*     */ 
/*     */     
/*  97 */     BpmContext.setActionModel((ActionCmd)model);
/*     */ 
/*     */     
/* 100 */     executePlugbeforeSaveBusData(model);
/*     */ 
/*     */     
/* 103 */     handelBusData(model);
/*     */     
/* 105 */     if (isStopExecute) {
/*     */       return;
/*     */     }
/*     */     
/* 109 */     doAction(model);
/*     */     
/* 111 */     doSkip(model);
/*     */ 
/*     */     
/* 114 */     BpmContext.removeActionModel();
/*     */ 
/*     */     
/* 117 */     actionAfter(model);
/*     */   }
/*     */ 
/*     */   
/*     */   private void executePlugbeforeSaveBusData(T model) {
/* 122 */     if (model instanceof InstanceActionCmd) {
/* 123 */       this.executionCommand.execute(EventType.PRE_SAVE_BUS_EVENT, (InstanceActionCmd)model);
/* 124 */     } else if (model instanceof TaskActionCmd) {
/* 125 */       this.taskCommand.execute(EventType.PRE_SAVE_BUS_EVENT, (TaskActionCmd)model);
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
/*     */ 
/*     */   
/*     */   protected void handelBusData(T data) {
/* 150 */     executeHandler(data);
/*     */ 
/*     */     
/* 153 */     this.LOG.debug("流程启动处理业务数据...");
/* 154 */     this.busDataHandle.saveDataModel((BaseActionCmd)data);
/*     */     
/* 156 */     doActionBefore(data);
/*     */   }
/*     */   
/*     */   protected void actionAfter(T model) {
/* 160 */     toDoActionAfter(model);
/*     */     
/* 162 */     if (getActionType() == ActionType.DRAFT) {
/*     */       return;
/*     */     }
/*     */     
/* 166 */     if (model.isSource()) {
/* 167 */       BpmContext.cleanTread();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void doSkip(T model) {
/* 173 */     if (getActionType() != ActionType.AGREE && getActionType() != ActionType.OPPOSE && getActionType() != ActionType.START) {
/*     */       return;
/*     */     }
/* 176 */     ActionCmd actionModel = BpmContext.getActionModel();
/* 177 */     if (actionModel == null || actionModel instanceof InstanceActionCmd) {
/*     */       return;
/*     */     }
/*     */     
/* 181 */     DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 182 */     List<String> taskIds = (List<String>)ThreadMapUtil.remove("SKIP_TASKIDS");
/* 183 */     if (CollUtil.isNotEmpty(taskIds)) {
/* 184 */       for (String id : taskIds) {
/* 185 */         BpmTask task = (BpmTask)this.bpmTaskManager.get(id);
/* 186 */         TaskIdentityLink taskIdentityLink = this.taskIdentityLinkManager.getByTaskId(id).get(0);
/* 187 */         DefualtTaskActionCmd defualtTaskActionCmd = new DefualtTaskActionCmd();
/* 188 */         defualtTaskActionCmd.setBpmInstance(taskModel.getBpmInstance());
/* 189 */         defualtTaskActionCmd.setBpmDefinition(taskModel.getBpmDefinition());
/* 190 */         defualtTaskActionCmd.setBizDataMap(taskModel.getBizDataMap());
/* 191 */         defualtTaskActionCmd.setBusinessKey(taskModel.getBusinessKey());
/* 192 */         defualtTaskActionCmd.setActionName(ActionType.AGREE.getKey());
/* 193 */         defualtTaskActionCmd.setBpmTask((IBpmTask)task);
/* 194 */         defualtTaskActionCmd.setTaskLinkId(taskIdentityLink.getId());
/* 195 */         defualtTaskActionCmd.setTaskIdentityLink(taskIdentityLink);
/* 196 */         defualtTaskActionCmd.setApproveOrgId(taskModel.getApproveOrgId());
/* 197 */         defualtTaskActionCmd.setOpinion(TaskSkipType.SCRIPT_SKIP.getValue());
/* 198 */         defualtTaskActionCmd.setDynamicBpmIdentity(model.getDynamicBpmIdentity());
/*     */         
/* 200 */         List<String> list = model.getStartAppointDestinations();
/* 201 */         if (CollectionUtil.isEmpty(list)) {
/* 202 */           list = ((BaseActionCmd)actionModel).getStartAppointDestinations();
/*     */         }
/* 204 */         if (CollectionUtil.isNotEmpty(list)) {
/* 205 */           defualtTaskActionCmd.setDestinations(list.<String>toArray(new String[list.size()]));
/*     */         }
/* 207 */         defualtTaskActionCmd.clearStartAppointDestination();
/* 208 */         defualtTaskActionCmd.setDynamicSubmitTaskName(model.getDynamicSubmitTaskName());
/* 209 */         BpmContext.setActionModel((ActionCmd)defualtTaskActionCmd);
/* 210 */         defualtTaskActionCmd.executeSkipTask();
/*     */       } 
/* 212 */       BpmContext.setActionModel(actionModel);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 218 */     if (taskModel.isHasSkipThisTask() == TaskSkipType.NO_SKIP)
/*     */       return; 
/* 220 */     DefualtTaskActionCmd complateModel = new DefualtTaskActionCmd();
/* 221 */     complateModel.setBpmInstance(taskModel.getBpmInstance());
/* 222 */     complateModel.setBpmDefinition(taskModel.getBpmDefinition());
/* 223 */     complateModel.setBizDataMap(taskModel.getBizDataMap());
/* 224 */     complateModel.setBpmIdentities(taskModel.getBpmIdentities());
/* 225 */     complateModel.setBusinessKey(taskModel.getBusinessKey());
/* 226 */     complateModel.setActionName(ActionType.AGREE.getKey());
/* 227 */     complateModel.setBpmTask(taskModel.getBpmTask());
/* 228 */     complateModel.setTaskIdentityLink(taskModel.getTaskIdentityLink());
/* 229 */     complateModel.setTaskLinkId(taskModel.getTaskLinkId());
/* 230 */     complateModel.setApproveOrgId(taskModel.getApproveOrgId());
/* 231 */     complateModel.setOpinion(taskModel.isHasSkipThisTask().getValue());
/* 232 */     complateModel.setDynamicBpmIdentity(model.getDynamicBpmIdentity());
/*     */     
/* 234 */     List<String> startAppointDestinations = model.getStartAppointDestinations();
/* 235 */     if (CollectionUtil.isEmpty(startAppointDestinations)) {
/* 236 */       startAppointDestinations = ((BaseActionCmd)actionModel).getStartAppointDestinations();
/*     */     }
/* 238 */     if (CollectionUtil.isNotEmpty(startAppointDestinations)) {
/* 239 */       complateModel.setDestinations(startAppointDestinations.<String>toArray(new String[startAppointDestinations.size()]));
/*     */     }
/* 241 */     complateModel.clearStartAppointDestination();
/* 242 */     complateModel.setDynamicSubmitTaskName(model.getDynamicSubmitTaskName());
/* 243 */     complateModel.executeSkipTask();
/*     */   }
/*     */   
/*     */   public void skipTaskExecute(T model) {
/* 247 */     BpmContext.setActionModel((ActionCmd)model);
/* 248 */     doActionBefore(model);
/* 249 */     doAction(model);
/* 250 */     doSkip(model);
/* 251 */     BpmContext.removeActionModel();
/* 252 */     actionAfter(model);
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
/*     */   protected void toDoActionAfter(T model) {
/* 276 */     doActionAfter(model);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void executeHandler(T actionModel) {
/* 286 */     BpmInstance instance = (BpmInstance)actionModel.getBpmInstance();
/*     */     
/* 288 */     if (StringUtil.isEmpty(actionModel.getBusinessKey()) && StringUtil.isNotEmpty(instance.getBizKey())) {
/* 289 */       actionModel.setBusinessKey(instance.getBizKey());
/*     */     }
/*     */     
/* 292 */     String handler = getFormHandler(actionModel);
/* 293 */     if (StringUtil.isNotEmpty(handler)) {
/*     */       try {
/* 295 */         HandlerUtil.invokeHandler((ActionCmd)actionModel, handler);
/* 296 */         this.LOG.debug("执行URL表单处理器：{}", handler);
/* 297 */       } catch (Exception ex) {
/* 298 */         throw new WorkFlowException(BpmStatusCode.HANDLER_ERROR, ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 303 */     if (StringUtil.isNotEmpty(actionModel.getBusinessKey()) && StringUtil.isEmpty(instance.getBizKey())) {
/* 304 */       instance.setBizKey(actionModel.getBusinessKey());
/* 305 */       instance.setHasUpdate(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkFlowIsValid(BaseActionCmd actionModel) {
/* 311 */     IBpmInstance instance = actionModel.getBpmInstance();
/*     */     
/* 313 */     if (instance.getIsForbidden().shortValue() == 1) {
/* 314 */       throw new BusinessMessage(String.format("流程实例【%s】已经被挂起，请联系管理员", new Object[] { instance.getSubject() }), BpmStatusCode.DEF_FORBIDDEN);
/*     */     }
/*     */     
/* 317 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/* 318 */     if ("forbidden".equals(def.getExtProperties().getStatus())) {
/* 319 */       throw new BusinessMessage(String.format("流程定义【%s】已经被禁用，请联系管理员", new Object[] { def.getName() }), BpmStatusCode.DEF_FORBIDDEN);
/*     */     }
/*     */     
/* 322 */     IUser user = ContextUtil.getCurrentUser();
/* 323 */     if (ContextUtil.isAdmin(user))
/*     */       return; 
/* 325 */     if (actionModel.getIgnoreAuthentication().booleanValue()) {
/*     */       return;
/*     */     }
/* 328 */     String taskId = null;
/* 329 */     String instId = actionModel.getInstanceId();
/* 330 */     if (actionModel instanceof DefualtTaskActionCmd)
/* 331 */     { IBpmTask task = ((DefualtTaskActionCmd)actionModel).getBpmTask();
/* 332 */       if (user.getUserId().equals(task.getAssigneeId())) {
/*     */         return;
/*     */       }
/*     */       
/* 336 */       taskId = task.getId();
/* 337 */       instId = null; }
/* 338 */     else { if (StringUtil.isNotEmpty(def.getProcessDefinitionId())) {
/*     */         return;
/*     */       }
/*     */       
/*     */       return; }
/*     */ 
/*     */     
/* 345 */     Boolean hasPermission = this.taskIdentityLinkManager.checkUserOperatorPermission(user.getUserId(), instId, taskId);
/* 346 */     if (!hasPermission.booleanValue()) {
/* 347 */       throw new BusinessMessage("没有该任务的操作权限", BpmStatusCode.NO_PERMISSION);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parserBusinessData(T actionModel) {
/* 357 */     IBpmInstance instance = actionModel.getBpmInstance();
/*     */     
/* 359 */     JSONObject busData = actionModel.getBusData();
/* 360 */     Map<String, IBusinessData> businessDatas = null;
/* 361 */     if (busData == null || busData.isEmpty()) {
/* 362 */       businessDatas = this.busDataHandle.getInstanceData(null, (BpmInstance)instance);
/*     */     }
/*     */     
/* 365 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/*     */ 
/*     */     
/* 368 */     for (BpmDataModel dataModel : bpmProcessDef.getDataModelList()) {
/* 369 */       String modelCode = dataModel.getCode();
/* 370 */       IBusinessData businessData = null;
/* 371 */       if (busData == null) {
/* 372 */         businessData = businessDatas.get(modelCode);
/*     */       }
/* 374 */       else if (busData.containsKey(modelCode)) {
/* 375 */         businessData = this.iBusinessDataService.parseBusinessData(busData.getJSONObject(modelCode), modelCode);
/*     */       } 
/*     */       
/* 378 */       if (businessData != null) {
/* 379 */         actionModel.getBizDataMap().put(modelCode, businessData);
/*     */       }
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
/*     */   
/*     */   protected boolean handelFormInit(BaseActionCmd cmd, BpmNodeDef nodeDef) {
/* 393 */     String nodeId = nodeDef.getNodeId();
/*     */     
/* 395 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(cmd.getBpmInstance().getDefId());
/* 396 */     List<NodeInit> nodeInitList = def.getNodeInitList(nodeId);
/*     */     
/* 398 */     Map<String, IBusinessData> bos = cmd.getBizDataMap();
/* 399 */     if (CollectionUtil.isEmpty(nodeInitList)) return false;
/*     */     
/* 401 */     Map<String, Object> param = new HashMap<>();
/* 402 */     if (bos != null) {
/* 403 */       param.putAll(bos);
/*     */     }
/* 405 */     param.put("bpmInstance", cmd.getBpmInstance());
/* 406 */     param.put("actionCmd", cmd);
/* 407 */     ActionType actionType = cmd.getActionType();
/* 408 */     param.put("submitActionDesc", actionType.getName());
/* 409 */     param.put("submitActionName", actionType.getKey());
/* 410 */     param.put("submitOpinion", cmd.getOpinion());
/* 411 */     param.put("isTask", Boolean.valueOf(false));
/*     */ 
/*     */     
/* 414 */     if (cmd instanceof DefualtTaskActionCmd) {
/* 415 */       param.put("isTask", Boolean.valueOf(true));
/* 416 */       param.put("bpmTask", ((DefualtTaskActionCmd)cmd).getBpmTask());
/*     */     } 
/*     */ 
/*     */     
/* 420 */     boolean isStopExecute = false;
/* 421 */     BpmContext.setActionModel((ActionCmd)cmd);
/* 422 */     for (NodeInit init : nodeInitList) {
/* 423 */       if (StringUtil.isNotEmpty(init.getWhenSave())) {
/*     */         try {
/* 425 */           Object result = this.iGroovyScriptEngine.executeObject(init.getWhenSave(), param);
/* 426 */           if (StringUtils.equals(String.valueOf(result), "信号事件执行完成,中断execute")) {
/* 427 */             isStopExecute = true;
/*     */           }
/* 429 */         } catch (Exception e) {
/* 430 */           throw new BusinessMessage(e.getMessage(), BpmStatusCode.FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR, e);
/*     */         } 
/* 432 */         this.LOG.debug("执行节点数据初始化脚本{}", init.getBeforeShow());
/*     */       } 
/*     */     } 
/* 435 */     BpmContext.removeActionModel();
/* 436 */     return isStopExecute;
/*     */   }
/*     */   private String getFormHandler(T actionModel) {
/*     */     BpmForm form;
/* 440 */     String defId = actionModel.getDefId();
/*     */ 
/*     */     
/* 443 */     if (actionModel instanceof TaskActionCmd) {
/* 444 */       TaskActionCmd cmd = (TaskActionCmd)actionModel;
/* 445 */       String nodeId = cmd.getNodeId();
/* 446 */       BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, nodeId);
/* 447 */       form = nodeDef.getForm();
/*     */     } else {
/* 449 */       BpmNodeDef nodeDef = this.bpmProcessDefService.getStartEvent(defId);
/* 450 */       form = nodeDef.getForm();
/*     */     } 
/* 452 */     if (form == null || form.isFormEmpty()) {
/* 453 */       DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 454 */       form = def.getGlobalForm();
/*     */     } 
/* 456 */     if (form != null) {
/* 457 */       return form.getFormHandler();
/*     */     }
/*     */     
/* 460 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 466 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 471 */     return "";
/*     */   }
/*     */   
/*     */   protected abstract void doAction(T paramT);
/*     */   
/*     */   protected abstract boolean prepareActionDatas(T paramT);
/*     */   
/*     */   protected abstract void doActionBefore(T paramT);
/*     */   
/*     */   protected abstract void doActionAfter(T paramT);
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/AbsActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */