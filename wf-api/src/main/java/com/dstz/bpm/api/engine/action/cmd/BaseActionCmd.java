/*     */ package com.dstz.bpm.api.engine.action.cmd;
/*     */ 
/*     */ import com.dstz.bus.api.model.IBusinessData;
/*     */ import com.dstz.form.api.model.FormCategory;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.engine.action.handler.ActionHandler;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.def.IBpmDefinition;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.api.service.BpmTaskService;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */
import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseActionCmd
/*     */   implements ActionCmd
/*     */ {
/*  41 */   protected Map<String, Object> variable_ = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*  45 */   protected Map<String, List<SysIdentity>> identityMap_ = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JSONObject busData;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   protected IBpmDefinition bpmDefinition = null;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String defId;
/*     */ 
/*     */   
/*  63 */   protected IBpmInstance bpmInstance = null;
/*     */ 
/*     */   
/*     */   protected String instanceId;
/*     */ 
/*     */   
/*     */   protected String taskId;
/*     */   
/*  71 */   protected Map<String, IBusinessData> bizDataMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private String actionName;
/*     */ 
/*     */ 
/*     */   
/*     */   private String doActionName;
/*     */ 
/*     */ 
/*     */   
/*     */   private String businessKey;
/*     */ 
/*     */ 
/*     */   
/*     */   private String dataMode;
/*     */ 
/*     */ 
/*     */   
/*     */   private String opinion;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] destinations;
/*     */ 
/*     */   
/*     */   protected String formId;
/*     */ 
/*     */   
/*     */   protected List<String> startAppointDestinations;
/*     */ 
/*     */   
/*     */   protected String dynamicSubmitTaskName;
/*     */ 
/*     */   
/* 107 */   protected Map<String, List<List<SysIdentity>>> dynamicIdentityMap_ = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSource = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private BpmExecutionStack executionStack;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ifremindNextUser = false;
/*     */ 
/*     */ 
/*     */   
/*     */   protected JSONObject extendConf;
/*     */ 
/*     */   
/* 126 */   protected Boolean ignoreAuthentication = Boolean.valueOf(false);
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
/*     */   protected String taskLinkId;
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
/*     */   protected String approveOrgId;
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
/*     */   protected boolean hasExecuted;
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
/*     */   private void handleUserSetting(JSONObject jsonObject) {
/* 194 */     if (jsonObject.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 198 */     Map<String, List<SysIdentity>> map = new HashMap<>();
/*     */     
/* 200 */     Set<String> nodeIds = jsonObject.keySet();
/* 201 */     UserService userService = AppUtil.getImplInstanceArray(UserService.class).get(0);
/* 202 */     for (String nodeId : nodeIds) {
/* 203 */       JSONArray users = jsonObject.getJSONArray(nodeId);
/* 204 */       if (users == null || users.isEmpty()) {
/*     */         continue;
/*     */       }
/* 207 */       List<SysIdentity> userList = new ArrayList<>();
/*     */       
/* 209 */       for (Object userObj : users) {
/* 210 */         JSONObject user = (JSONObject)userObj;
/* 211 */         String clazzStr = user.getString("clazz");
/* 212 */         Class<SysIdentity> clazz = SysIdentity.class;
/* 213 */         if (StringUtils.isNotEmpty(clazzStr)) {
/* 214 */           ServiceLoader<SysIdentity> loader = ServiceLoader.load(SysIdentity.class);
/* 215 */           for (SysIdentity sysIdentity : loader) {
/* 216 */             System.out.println(sysIdentity.getClass());
/* 217 */             if (StringUtils.equals(clazzStr, sysIdentity.getClass().getSimpleName())) {
/* 218 */               clazz = (Class)sysIdentity.getClass();
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 224 */         SysIdentity bpmInentity = (SysIdentity)JSON.toJavaObject((JSON)user, clazz);
/*     */         
/* 226 */         if (StringUtil.isEmpty(bpmInentity.getId())) {
/*     */           continue;
/*     */         }
/*     */         
/* 230 */         if (StringUtils.equals(bpmInentity.getType(), "user")) {
/* 231 */           IUser user1 = userService.getUserById(bpmInentity.getId());
/* 232 */           if (user1 != null) {
/* 233 */             bpmInentity.setName(user1.getFullname());
/*     */           }
/*     */         } 
/* 236 */         userList.add(bpmInentity);
/*     */       } 
/* 238 */       if (StringUtils.contains(nodeId, "&")) {
/* 239 */         map.put(nodeId.split("&")[1], userList); continue;
/*     */       } 
/* 241 */       map.put(nodeId, userList);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 246 */     setBpmIdentities(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getActionVariables() {
/* 256 */     return this.variable_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActionVariables(Map<String, Object> variables) {
/* 266 */     this.variable_ = variables;
/*     */   }
/*     */   
/*     */   public Boolean getIgnoreAuthentication() {
/* 270 */     return this.ignoreAuthentication;
/*     */   }
/*     */   
/*     */   public void setIgnoreAuthentication(Boolean ignoreAuthentication) {
/* 274 */     this.ignoreAuthentication = ignoreAuthentication;
/*     */   }
/*     */   
/*     */   public void setBpmIdentities(Map<String, List<SysIdentity>> map) {
/* 278 */     this.identityMap_ = map;
/*     */   }
/*     */   
/*     */   public void clearBpmIdentities() {
/* 282 */     this.identityMap_.clear();
/*     */   }
/*     */   
/*     */   public void addBpmIdentity(String key, SysIdentity bpmIdentity) {
/* 286 */     List<SysIdentity> list = this.identityMap_.get(key);
/* 287 */     if (CollectionUtil.isEmpty(list)) {
/* 288 */       list = new ArrayList<>();
/* 289 */       list.add(bpmIdentity);
/* 290 */       this.identityMap_.put(key, list);
/*     */     } else {
/* 292 */       list.add(bpmIdentity);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBpmIdentity(String key, List<SysIdentity> bpmIdentityList) {
/* 303 */     List<SysIdentity> list = this.identityMap_.get(key);
/* 304 */     if (CollectionUtil.isEmpty(list)) {
/* 305 */       list = new ArrayList<>();
/* 306 */       list.addAll(bpmIdentityList);
/* 307 */       this.identityMap_.put(key, list);
/*     */     } else {
/* 309 */       list.addAll(bpmIdentityList);
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
/*     */   public void setBpmIdentity(String key, List<SysIdentity> bpmIdentityList) {
/* 321 */     List<SysIdentity> list = this.identityMap_.get(key);
/* 322 */     if (CollectionUtil.isEmpty(list)) {
/* 323 */       list = new ArrayList<>();
/* 324 */       list.addAll(bpmIdentityList);
/* 325 */       this.identityMap_.put(key, list);
/*     */     } else {
/* 327 */       list.clear();
/* 328 */       list.addAll(bpmIdentityList);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDynamicBpmIdentity(String key, List<List<SysIdentity>> bpmIdentityList) {
/* 339 */     List<List<SysIdentity>> list = this.dynamicIdentityMap_.get(key);
/* 340 */     if (CollectionUtil.isEmpty(list)) {
/* 341 */       list = new ArrayList<>();
/* 342 */       list.addAll(bpmIdentityList);
/* 343 */       this.dynamicIdentityMap_.put(key, list);
/*     */     } else {
/* 345 */       list.clear();
/* 346 */       list.addAll(bpmIdentityList);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDynamicBpmIdentity(Map<String, List<List<SysIdentity>>> map) {
/* 351 */     this.dynamicIdentityMap_ = map;
/*     */   }
/*     */   
/*     */   public Map<String, List<List<SysIdentity>>> getDynamicBpmIdentity() {
/* 355 */     return this.dynamicIdentityMap_;
/*     */   }
/*     */   
/*     */   public List<List<SysIdentity>> getDynamicBpmIdentity(String nodeId) {
/* 359 */     return this.dynamicIdentityMap_.get(nodeId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SysIdentity> getBpmIdentity(String nodeId) {
/* 364 */     return this.identityMap_.get(nodeId);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, List<SysIdentity>> getBpmIdentities() {
/* 369 */     return this.identityMap_;
/*     */   }
/*     */   
/*     */   public boolean isSource() {
/* 373 */     return this.isSource;
/*     */   }
/*     */   
/*     */   public String getDoActionName() {
/* 377 */     return this.doActionName;
/*     */   }
/*     */   
/*     */   public void setDoActionName(String doActionName) {
/* 381 */     this.doActionName = doActionName;
/*     */   }
/*     */   
/*     */   public void setSource(boolean isSource) {
/* 385 */     this.isSource = isSource;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getBusData() {
/* 390 */     return this.busData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBusData(JSONObject busData) {
/* 395 */     this.busData = busData;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBpmInstance getBpmInstance() {
/* 400 */     return this.bpmInstance;
/*     */   }
/*     */   
/*     */   public void setBpmInstance(IBpmInstance bpmInstance) {
/* 404 */     this.bpmInstance = bpmInstance;
/*     */   }
/*     */   
/*     */   public boolean isIfremindNextUser() {
/* 408 */     return this.ifremindNextUser;
/*     */   }
/*     */   
/*     */   public void setIfremindNextUser(boolean ifremindNextUser) {
/* 412 */     this.ifremindNextUser = ifremindNextUser;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDataMode() {
/* 417 */     return this.dataMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDataMode(String mode) {
/* 422 */     this.dataMode = mode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBusinessKey() {
/* 427 */     return this.businessKey;
/*     */   }
/*     */   
/*     */   public List<String> getStartAppointDestinations() {
/* 431 */     return this.startAppointDestinations;
/*     */   }
/*     */   
/*     */   public void setStartAppointDestinations(List<String> startAppointDestinations) {
/* 435 */     this.startAppointDestinations = startAppointDestinations;
/*     */   }
/*     */   
/*     */   public void setStartAppointDestination(String startAppointDestination) {
/* 439 */     this.startAppointDestinations = Arrays.asList(new String[] { startAppointDestination });
/*     */   }
/*     */   
/*     */   public void addStartAppointDestination(String startAppointDestination) {
/* 443 */     if (this.startAppointDestinations == null) {
/* 444 */       this.startAppointDestinations = new ArrayList<>();
/*     */     }
/* 446 */     this.startAppointDestinations.add(startAppointDestination);
/*     */   }
/*     */   
/*     */   public void clearStartAppointDestination() {
/* 450 */     if (!CollectionUtils.isEmpty(this.startAppointDestinations)) {
/* 451 */       this.startAppointDestinations.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDynamicSubmitTaskName() {
/* 456 */     return this.dynamicSubmitTaskName;
/*     */   }
/*     */   
/*     */   public void setDynamicSubmitTaskName(String dynamicSubmitTaskName) {
/* 460 */     this.dynamicSubmitTaskName = dynamicSubmitTaskName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormId() {
/* 465 */     return this.formId;
/*     */   }
/*     */   
/*     */   public BpmExecutionStack getExecutionStack() {
/* 469 */     return this.executionStack;
/*     */   }
/*     */   
/*     */   public void setExecutionStack(BpmExecutionStack executionStack) {
/* 473 */     this.executionStack = executionStack;
/*     */   }
/*     */   
/*     */   public String getInstanceId() {
/* 477 */     if (StringUtil.isEmpty(this.instanceId) && this.bpmInstance != null) {
/* 478 */       return this.bpmInstance.getId();
/*     */     }
/* 480 */     return this.instanceId;
/*     */   }
/*     */   
/*     */   public String getTaskLinkId() {
/* 484 */     return this.taskLinkId;
/*     */   }
/*     */   
/*     */   public void setTaskLinkId(String taskLinkId) {
/* 488 */     this.taskLinkId = taskLinkId;
/*     */   }
/*     */   
/*     */   public void setInstanceId(String instanceId) {
/* 492 */     this.instanceId = instanceId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefId() {
/* 497 */     if (StringUtil.isEmpty(this.defId) && this.bpmInstance != null) {
/* 498 */       return this.bpmInstance.getDefId();
/*     */     }
/*     */     
/* 501 */     return this.defId;
/*     */   }
/*     */   
/*     */   public JSONObject getExtendConf() {
/* 505 */     return this.extendConf;
/*     */   }
/*     */   
/*     */   public void setExtendConf(JSONObject extendConf) {
/* 509 */     this.extendConf = extendConf;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/* 513 */     this.defId = defId;
/*     */   }
/*     */   
/*     */   public String getOpinion() {
/* 517 */     return this.opinion;
/*     */   }
/*     */   
/*     */   public void setOpinion(String opinion) {
/* 521 */     this.opinion = opinion;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, IBusinessData> getBizDataMap() {
/* 526 */     return this.bizDataMap;
/*     */   }
/*     */   
/*     */   public void setBizDataMap(Map<String, IBusinessData> bizDataMap) {
/* 530 */     this.bizDataMap = bizDataMap;
/*     */   }
/*     */   
/*     */   public void setFormId(String formId) {
/* 534 */     this.formId = formId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBusinessKey(String businessKey) {
/* 539 */     this.businessKey = businessKey;
/*     */   }
/*     */   
/*     */   public String getApproveOrgId() {
/* 543 */     return this.approveOrgId;
/*     */   }
/*     */   
/*     */   public void setApproveOrgId(String approveOrgId) {
/* 547 */     this.approveOrgId = approveOrgId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurAccount(String curAccount) {
/* 557 */     UserService userService = (UserService)AppUtil.getBean(UserService.class);
/* 558 */     IUser user = userService.getUserByAccount(curAccount);
/* 559 */     ContextUtil.setCurrentUser(user);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getActionName() {
/* 564 */     return this.actionName;
/*     */   }
/*     */   
/*     */   public ActionType getActionType() {
/* 568 */     return ActionType.fromKey(getActionName());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActionName(String actionName) {
/* 573 */     this.actionName = actionName;
/*     */   }
/*     */   
/*     */   public IBpmDefinition getBpmDefinition() {
/* 577 */     return this.bpmDefinition;
/*     */   }
/*     */   
/*     */   public void setBpmDefinition(IBpmDefinition bpmDefinition) {
/* 581 */     this.bpmDefinition = bpmDefinition;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getDestinations() {
/* 586 */     return this.destinations;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDestination() {
/* 591 */     if (this.destinations == null || this.destinations.length == 0) {
/* 592 */       return null;
/*     */     }
/* 594 */     return this.destinations[0];
/*     */   }
/*     */   
/*     */   public void setDestination(String destination) {
/* 598 */     if (StringUtil.isNotEmpty(destination)) {
/* 599 */       setDestinations(new String[] { destination });
/*     */     }
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 604 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 608 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public void setDestinations(String[] destinations) {
/* 612 */     this.destinations = destinations;
/*     */   }
/*     */   
/*     */   public BaseActionCmd() {
/* 616 */     this.hasExecuted = false; } public BaseActionCmd(FlowRequestParam flowParam) { this.hasExecuted = false; this.isSource = true; setActionName(flowParam.getAction()); setDoActionName(flowParam.getActionName()); setDefId(flowParam.getDefId()); setInstanceId(flowParam.getInstanceId()); setTaskId(flowParam.getTaskId()); setBusinessKey(flowParam.getBusinessKey()); String destination = flowParam.getDestination(); setActionVariables(flowParam.getVariables()); if (StringUtil.isNotEmpty(destination))
/*     */       if (destination.contains(",")) { String[] destinations = destination.split(","); for (int i = 0; i < destinations.length; i++) { if (StringUtils.contains(destinations[i], "&"))
/*     */             destinations[i] = destinations[i].split("&")[0];  }  setDestinations(destinations); } else if (StringUtils.isNotEmpty(destination) && StringUtils.contains(destination, "&")) { setDestination(destination.split("&")[0]); } else { setDestination(destination); }   this.approveOrgId = flowParam.getStartOrgId(); initSpecialParam(flowParam); if (CollectionUtil.isNotEmpty((Map)flowParam.getNodeUsers()))
/*     */       handleUserSetting(flowParam.getNodeUsers());  setBusData(flowParam.getData()); String formType = FormCategory.INNER.value(); if (StringUtil.isNotEmpty(flowParam.getFormType()))
/* 620 */       formType = flowParam.getFormType();  if (FormCategory.INNER.value().equals(formType)) { setDataMode("bo"); } else { setDataMode("pk"); }  setExtendConf(flowParam.getExtendConf()); setOpinion(flowParam.getOpinion()); } public synchronized String executeCmd() { if (this.hasExecuted) {
/* 621 */       throw new BusinessException("action cmd caonot be invoked twice", BpmStatusCode.NO_PERMISSION);
/*     */     }
/* 623 */     this.hasExecuted = true;
/* 624 */     BpmNodeDef bpmNodeDef = null;
/* 625 */     String taskId = getTaskId();
/* 626 */     BpmTaskService bpmTaskService = AppUtil.getImplInstanceArray(BpmTaskService.class).get(0);
/* 627 */     if (!StringUtils.isEmpty(taskId)) {
/* 628 */       bpmNodeDef = bpmTaskService.getBpmNodeDef(taskId);
/* 629 */     } else if (StringUtils.equals("start", getActionName())) {
/* 630 */       BpmProcessDefService bpmProcessDefService = AppUtil.getImplInstanceArray(BpmProcessDefService.class).get(0);
/* 631 */       bpmNodeDef = bpmProcessDefService.getStartEvent(getDefId());
/*     */     } 
/* 633 */     ActionType actonType = ActionType.fromKey(getActionName());
/* 634 */     setIfremindNextUser((StringUtils.indexOf("start,agree,signAgree,oppose,signOppose,reject,reject2Start,recall,addDoAgree", getActionName()) > -1));
/* 635 */     ActionHandler handler = (ActionHandler)AppUtil.getBean(actonType.getBeanId());
/* 636 */     if (handler == null) {
/* 637 */       throw new BusinessException("action beanId cannot be found :" + actonType.getName(), BpmStatusCode.NO_TASK_ACTION);
/*     */     }
/*     */     
/* 640 */     BpmContext.cleanTread();
/* 641 */     BpmContext.removeThreadNextIdentitys();
/* 642 */     handler.execute(this);
/* 643 */     String remindStr = "执行操作成功";
/* 644 */     if (bpmNodeDef != null) {
/* 645 */       String reminderScript = bpmNodeDef.getNodeProperties().getReminderScript();
/* 646 */       Map<String, Object> params = new HashMap<>();
/* 647 */       params.put("submitTaskName", bpmNodeDef.getName());
/* 648 */       params.put("destination", getDestination());
/* 649 */       remindStr = bpmTaskService.getTaskReminderStr(reminderScript, params);
/*     */     } 
/* 651 */     return remindStr; }
/*     */ 
/*     */   
/*     */   public abstract void initSpecialParam(FlowRequestParam paramFlowRequestParam);
/*     */   
/*     */   public abstract String getNodeId();
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/cmd/BaseActionCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */