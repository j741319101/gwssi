/*     */ package com.dstz.bpm.plugin.node.recrease.handler;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.act.util.ActivitiUtil;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.constant.OpinionStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.def.NodeInit;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.api.service.BpmRuntimeService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.data.DefaultBpmFlowDataAccessor;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*     */ import com.dstz.bpm.plugin.core.model.DynamicTask;
/*     */ import com.dstz.bpm.plugin.global.script.executer.GlobalScriptPluginExecutor;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.org.api.model.IGroup;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.GroupService;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.map.MapUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class RecreaseDynamicTaskExecuter {
/*     */   @Resource
/*     */   private DynamicTaskManager dynamicTaskManager;
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   @Resource
/*     */   private RecreaseTaskAction recreaseTaskAction;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   private IBpmBusDataHandle bpmBusDataHandle;
/*     */   @Resource
/*     */   private IBusinessDataService iBusinessDataService;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Resource
/*     */   protected ActTaskService actTaskService;
/*     */   
/*     */   public void increase(DefualtTaskActionCmd model) {
/*  96 */     JSONObject extendConf = model.getExtendConf();
/*  97 */     BpmTask bpmTask = new BpmTask();
/*  98 */     bpmTask.setId(model.getTaskId());
/*  99 */     List<DynamicTask> dynamicTasks = this.dynamicTaskManager.getByTaskId(bpmTask.getId());
/* 100 */     boolean isDynamic = false;
/* 101 */     Boolean forceIncrease = extendConf.getBoolean("forceIncrease");
/* 102 */     for (DynamicTask dynamicTask : dynamicTasks) {
/* 103 */       if (StringUtils.equals("runtime", dynamicTask.getStatus())) {
/* 104 */         isDynamic = true;
/*     */       }
/*     */     } 
/* 107 */     if (!isDynamic && forceIncrease != null && !forceIncrease.booleanValue()) {
/* 108 */       throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
/*     */     }
/*     */     
/* 111 */     IBpmInstance bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
/*     */     
/* 113 */     JSONObject existAliveTask = this.recreaseTaskAction.getExistAliveTask(bpmInstance, (IBpmTask)bpmTask);
/* 114 */     Boolean b = existAliveTask.getBoolean("existAliveTask");
/* 115 */     if (!b.booleanValue() && (forceIncrease == null || !forceIncrease.booleanValue())) {
/* 116 */       throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
/*     */     }
/* 118 */     JSONObject dynamicNodes = extendConf.getJSONObject("dynamicNodes");
/* 119 */     bpmTask.setDefId(bpmInstance.getDefId());
/* 120 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 121 */     bpmTask.setNodeId(bpmTaskOpinion.getTaskKey());
/* 122 */     bpmTask.setInstId(bpmTaskOpinion.getInstId());
/* 123 */     model.setBpmTask((IBpmTask)bpmTask);
/* 124 */     model.setBpmInstance(bpmInstance);
/* 125 */     String startOrgId = bpmTaskOpinion.getTaskOrgId();
/* 126 */     if (bpmTaskOpinion.getApproveTime() == null) {
/* 127 */       TaskIdentityLink taskIdentityLink = this.taskIdentityLinkManager.getByTaskId(bpmTask.getId()).get(0);
/* 128 */       startOrgId = taskIdentityLink.getOrgId();
/*     */     } 
/* 130 */     model.setApproveOrgId(startOrgId);
/* 131 */     BpmNodeDef disBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), bpmTask.getNodeId());
/* 132 */     if (dynamicNodes != null || !dynamicNodes.isEmpty())
/* 133 */       for (String dynamicNodeId : dynamicNodes.keySet()) {
/* 134 */         String dynamicDestination = null;
/* 135 */         JSONArray inUsers = dynamicNodes.getJSONArray(dynamicNodeId);
/* 136 */         if (StringUtils.indexOf(dynamicNodeId, "&") > -1) {
/* 137 */           String[] pgIds = dynamicNodeId.split("&");
/* 138 */           dynamicNodeId = pgIds[0];
/* 139 */           dynamicDestination = pgIds[1];
/*     */         } 
/* 141 */         DynamicTask dynamicTask = getDynamicTask(dynamicTasks, dynamicNodeId);
/* 142 */         if (dynamicTask == null) {
/* 143 */           dynamicTask = new DynamicTask((IBpmTask)bpmTask);
/* 144 */           dynamicTask.setNodeId(dynamicNodeId);
/* 145 */           this.dynamicTaskManager.create(dynamicTask);
/*     */         } 
/* 147 */         for (Object user : inUsers) {
/* 148 */           BpmNodeDef dynamicBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), dynamicNodeId);
/* 149 */           String clazzStr = ((JSONObject)user).getString("clazz");
/* 150 */           Class<SysIdentity> clazz = SysIdentity.class;
/* 151 */           if (StringUtils.isNotEmpty(clazzStr)) {
/* 152 */             ServiceLoader<SysIdentity> loader = ServiceLoader.load(SysIdentity.class);
/* 153 */             for (SysIdentity sysIdentity : loader) {
/* 154 */               if (StringUtils.equals(clazzStr, sysIdentity.getClass().getSimpleName())) {
/* 155 */                 clazz = (Class)sysIdentity.getClass();
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/* 160 */           SysIdentity bpmInentity = (SysIdentity)JSON.toJavaObject((JSON)user, clazz);
/*     */           
/* 162 */           if (StringUtils.equals("user", bpmInentity.getType())) {
/* 163 */             IUser iUser = this.userService.getUserById(bpmInentity.getId());
/* 164 */             if (iUser == null) {
/* 165 */               throw new BusinessMessage(bpmInentity.getName() + " 用户丢失");
/*     */             }
/* 167 */             bpmInentity.setName(iUser.getFullname());
/*     */           } else {
/* 169 */             IGroup group = this.groupService.getById(bpmInentity.getType(), bpmInentity.getId());
/* 170 */             if (group == null) {
/* 171 */               throw new BusinessMessage(bpmInentity.getName() + " 组丢失");
/*     */             }
/* 173 */             bpmInentity.setName(group.getGroupName());
/*     */           } 
/* 175 */           dynamicTask.setAmmount(Integer.valueOf(dynamicTask.getAmmount().intValue() + 1));
/* 176 */           if (dynamicTask.getCurrentIndex().intValue() < dynamicTask.getAmmount().intValue()) {
/* 177 */             dynamicTask.setStatus("runtime");
/*     */           }
/* 179 */           List<JSONObject> identitys = JSONObject.parseArray(dynamicTask.getIdentityNode(), JSONObject.class);
/* 180 */           if (identitys == null) {
/* 181 */             identitys = new ArrayList<>();
/*     */           }
/* 183 */           JSONObject identity = new JSONObject();
/* 184 */           JSONArray users = new JSONArray();
/* 185 */           JSONObject addUser = new JSONObject();
/* 186 */           addUser.put("id", bpmInentity.getId());
/* 187 */           addUser.put("name", bpmInentity.getName());
/* 188 */           addUser.put("type", bpmInentity.getType());
/* 189 */           addUser.put("orgId", bpmInentity.getOrgId());
/* 190 */           users.add(addUser);
/* 191 */           identity.put("nodeIdentitys", users);
/* 192 */           identity.put("taskName", extendConf.getString("taskName"));
/* 193 */           identitys.add(identity);
/* 194 */           dynamicTask.setIdentityNode(JSONObject.toJSONString(identitys));
/* 195 */           if (dynamicTask.getIsParallel()) {
/* 196 */             List<SysIdentity> sysIdentities = new ArrayList<>();
/* 197 */             sysIdentities.add(bpmInentity);
/* 198 */             BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(bpmTask.getId());
/* 199 */             bpmTaskStack.setNodeId(dynamicBpmNodeDef.getNodeId());
/* 200 */             if (dynamicBpmNodeDef.getParentBpmNodeDef() != null && dynamicBpmNodeDef.getParentBpmNodeDef() instanceof SubProcessNodeDef) {
/* 201 */               bpmTaskStack.setNodeId(dynamicBpmNodeDef.getParentBpmNodeDef().getNodeId());
/*     */             }
/* 203 */             model.setExecutionStack((BpmExecutionStack)bpmTaskStack);
/* 204 */             BpmContext.setThreadDynamictaskStack(bpmTaskStack.getNodeId(), (BpmExecutionStack)bpmTaskStack);
/* 205 */             if (StringUtils.equalsIgnoreCase(dynamicBpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
/* 206 */               String callFlowKey = ((CallActivityNodeDef)dynamicBpmNodeDef).getFlowKey();
/*     */               
/* 208 */               BpmNodeDef startNode = this.bpmProcessDefService.getBpmProcessDef(this.bpmProcessDefService.getDefinitionByKey(callFlowKey).getId()).getStartNodes().get(0);
/* 209 */               String callTargetNodeId = startNode.getNodeId();
/* 210 */               model.setDynamicSubmitTaskName(startNode.getName());
/* 211 */               model.setBpmIdentity(callTargetNodeId, sysIdentities);
/* 212 */               BpmInstance topInstance = this.bpmInstanceManager.getTopInstance((BpmInstance)bpmInstance);
/* 213 */               if (topInstance == null) {
/* 214 */                 topInstance = (BpmInstance)bpmInstance;
/*     */               }
/* 216 */               Map<String, IBusinessData> data = this.bpmBusDataHandle.getInstanceBusData(topInstance.getId(), null);
/* 217 */               if (CollectionUtil.isNotEmpty(data)) {
/* 218 */                 model.setBizDataMap(data);
/*     */               }
/*     */             } else {
/* 221 */               model.setDynamicSubmitTaskName(dynamicBpmNodeDef.getName());
/* 222 */               model.setBpmIdentity(dynamicBpmNodeDef.getNodeId(), sysIdentities);
/*     */             } 
/* 224 */             model.setDoActionName("追加");
/* 225 */             BpmContext.setActionModel((ActionCmd)model);
/*     */             
/* 227 */             if (StringUtils.isNotEmpty(dynamicDestination)) {
/* 228 */               model.setStartAppointDestinations(Arrays.asList(new String[] { dynamicDestination }));
/*     */             }
/* 230 */             model.setDestination(dynamicBpmNodeDef.getNodeId());
/* 231 */             handelFormInit((BaseActionCmd)model, disBpmNodeDef);
/* 232 */             Map<String, List<List<SysIdentity>>> map = model.getDynamicBpmIdentity();
/* 233 */             if (CollectionUtil.isNotEmpty(map)) {
/* 234 */               map.forEach((nodeId, sysIdentityList) -> {
/*     */                     if (CollectionUtil.isNotEmpty(sysIdentityList)) {
/*     */                       sysIdentityList.forEach(());
/*     */                     }
/*     */                   });
/*     */ 
/*     */               
/* 241 */               map.clear();
/*     */             } 
/* 243 */             this.runtimeService.createNewExecution(model.getInstanceId(), dynamicBpmNodeDef.getNodeId());
/* 244 */             BpmContext.removeActionModel();
/*     */           } 
/* 246 */           this.dynamicTaskManager.update(dynamicTask);
/*     */         } 
/*     */       }  
/*     */   }
/*     */   @Resource
/*     */   private ActInstanceService actInstanceService; @Resource
/*     */   protected IGroovyScriptEngine iGroovyScriptEngine;
/*     */   public void decrease(DefualtTaskActionCmd model) {
/* 254 */     JSONObject extendConf = model.getExtendConf();
/* 255 */     BpmTask bpmTask = new BpmTask();
/* 256 */     bpmTask.setId(model.getTaskId());
/* 257 */     List<DynamicTask> dynamicTasks = this.dynamicTaskManager.getByTaskId(bpmTask.getId());
/* 258 */     boolean isDynamic = false;
/* 259 */     for (DynamicTask dynamicTask : dynamicTasks) {
/* 260 */       if (StringUtils.equals("runtime", dynamicTask.getStatus())) {
/* 261 */         isDynamic = true;
/*     */       }
/*     */     } 
/* 264 */     if (!isDynamic) {
/* 265 */       throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
/*     */     }
/*     */     
/* 268 */     IBpmInstance bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
/* 269 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 270 */     String orgId = null;
/*     */     
/* 272 */     if (bpmTaskOpinion.getApproveTime() == null) {
/* 273 */       bpmTaskOpinion = null;
/* 274 */       String taskLinkId = model.getTaskLinkId();
/* 275 */       if (StringUtils.isEmpty(taskLinkId)) {
/* 276 */         throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
/*     */       }
/* 278 */       TaskIdentityLink taskIdentityLink = (TaskIdentityLink)this.taskIdentityLinkManager.get(taskLinkId);
/* 279 */       if (taskIdentityLink == null) {
/* 280 */         throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
/*     */       }
/* 282 */       orgId = taskIdentityLink.getOrgId();
/*     */     } else {
/* 284 */       orgId = bpmTaskOpinion.getTaskOrgId();
/*     */     } 
/*     */     
/* 287 */     model.setApproveOrgId(orgId);
/* 288 */     JSONObject existAliveTask = this.recreaseTaskAction.getExistAliveTask(bpmInstance, (IBpmTask)bpmTask);
/* 289 */     Boolean b = existAliveTask.getBoolean("existAliveTask");
/* 290 */     if (!b.booleanValue()) {
/* 291 */       throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
/*     */     }
/* 293 */     String callBackId = (String)extendConf.get("callBackId");
/* 294 */     Map<String, JSONObject> tasks = (Map<String, JSONObject>)existAliveTask.get("tasks");
/* 295 */     for (String dynamicNodeId : tasks.keySet()) {
/* 296 */       JSONObject executionTask = tasks.get(dynamicNodeId);
/* 297 */       JSONArray jsonObject = (JSONArray)executionTask.get("tasks");
/*     */       
/* 299 */       BpmNodeDef dynamicNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), dynamicNodeId);
/*     */ 
/*     */       
/* 302 */       JSONObject data = model.getBusData();
/* 303 */       if (data != null && !data.isEmpty()) {
/* 304 */         Map<String, IBusinessData> businessDataMap = new HashMap<>();
/* 305 */         for (String key : data.keySet()) {
/* 306 */           IBusinessData businessData = this.iBusinessDataService.parseBusinessData(data.getJSONObject(key), key);
/* 307 */           businessDataMap.put(key, businessData);
/*     */         } 
/* 309 */         model.setBizDataMap(businessDataMap);
/*     */       } 
/*     */       
/* 312 */       if (data == null) {
/* 313 */         Map<String, IBusinessData> businessDataMap = this.bpmBusDataHandle.getInstanceBusData(bpmInstance.getId(), null);
/* 314 */         model.setBizDataMap(businessDataMap);
/*     */       } 
/*     */       
/* 317 */       for (Object jsonTask : jsonObject) {
/* 318 */         JSONObject task = (JSONObject)jsonTask;
/* 319 */         String id = (String)task.get("id");
/* 320 */         String instId = task.getString("instId");
/* 321 */         String taskId = task.getString("taskId");
/* 322 */         String nodeId = task.getString("nodeId");
/* 323 */         if (StringUtils.indexOf(callBackId, id) > -1) {
/* 324 */           if (task.getBoolean("existAliveTask").booleanValue()) {
/*     */             
/* 326 */             BpmNodeDef parentBpmNodeDef = dynamicNodeDef.getParentBpmNodeDef();
/* 327 */             if (parentBpmNodeDef != null && parentBpmNodeDef instanceof SubProcessNodeDef && (
/* 328 */               (DynamicTaskPluginDef)((DynamicTaskPluginContext)parentBpmNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef()).getEnabled().booleanValue()) {
/* 329 */               dynamicNodeId = parentBpmNodeDef.getNodeId();
/*     */             }
/*     */             
/* 332 */             DynamicTask dynamicTask = getDynamicTask(dynamicTasks, dynamicNodeId);
/* 333 */             dynamicTask.setCurrentIndex(Integer.valueOf(dynamicTask.getCurrentIndex().intValue() + 1));
/* 334 */             if (dynamicTask.getCurrentIndex() == dynamicTask.getAmmount()) {
/* 335 */               dynamicTask.setStatus("completed");
/*     */             }
/*     */             
/* 338 */             if (StringUtils.equalsIgnoreCase(NodeType.CALLACTIVITY.getKey(), dynamicNodeDef.getType().getKey())) {
/*     */               
/* 340 */               BpmInstance subInst = (BpmInstance)this.bpmInstanceManager.get(instId);
/* 341 */               model.setInstanceId(instId);
/* 342 */               model.setBpmInstance((IBpmInstance)subInst);
/* 343 */               handleInstanceInfo(model, Boolean.valueOf(StringUtils.equals("completed", dynamicTask.getStatus())), bpmTaskOpinion);
/*     */             } else {
/* 345 */               model.setBpmTask((IBpmTask)this.bpmTaskManager.get(taskId));
/* 346 */               BpmInstance subInst = (BpmInstance)this.bpmInstanceManager.get(instId);
/* 347 */               model.setTaskId(taskId);
/* 348 */               model.setInstanceId(instId);
/* 349 */               model.setBpmInstance((IBpmInstance)subInst);
/* 350 */               BpmContext.setActionModel((ActionCmd)model);
/*     */               
/* 352 */               BpmNodeDef bpmNodeDef1 = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), nodeId);
/* 353 */               BpmNodeDef subProcessNodeDef = bpmNodeDef1.getParentBpmNodeDef();
/* 354 */               if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
/* 355 */                 String endNodeId = ((BpmNodeDef)((SubProcessNodeDef)subProcessNodeDef).getChildBpmProcessDef().getEndEvents().get(0)).getNodeId();
/* 356 */                 BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(taskId);
/* 357 */                 if (!StringUtils.equals("create", bpmTaskStack.getActionName())) {
/* 358 */                   DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 359 */                   defaultQueryFilter.addParamsFilter("taskId", taskId);
/* 360 */                   defaultQueryFilter.addParamsFilter("prior", "FORWARD");
/* 361 */                   defaultQueryFilter.addFilter("action_name_", "create", QueryOP.EQUAL);
/* 362 */                   defaultQueryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
/* 363 */                   List<BpmTaskStack> bpmTaskStackList = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/* 364 */                   if (CollectionUtil.isEmpty(bpmTaskStackList)) {
/* 365 */                     throw new BusinessException("取回子流程任务失败，查看taskId:" + taskId + "的后置任务");
/*     */                   }
/* 367 */                   taskId = ((BpmTaskStack)bpmTaskStackList.get(0)).getTaskId();
/* 368 */                   model.setBpmTask((IBpmTask)this.bpmTaskManager.get(taskId));
/*     */                 } 
/*     */                 
/* 371 */                 String[] nodeIds = new String[0];
/* 372 */                 if (StringUtils.equals(dynamicTask.getStatus(), "completed") && 
/* 373 */                   bpmTaskOpinion != null) {
/* 374 */                   nodeIds = new String[] { bpmTaskOpinion.getTaskKey() };
/* 375 */                   model.setBpmIdentity(bpmTaskOpinion.getTaskKey(), 
/* 376 */                       Arrays.asList(new SysIdentity[] { (SysIdentity)new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId()) }));
/*     */                 } 
/*     */                 
/* 379 */                 Map<String, Object> activityMap = ActivitiUtil.skipPrepare(subInst.getActDefId(), subProcessNodeDef.getNodeId(), nodeIds);
/*     */                 try {
/* 381 */                   this.actTaskService.completeTask(taskId, model.getActionVariables(), new String[] { endNodeId });
/* 382 */                 } catch (Exception e) {
/* 383 */                   throw new BusinessException(e);
/*     */                 } finally {
/* 385 */                   ActivitiUtil.restoreActivity(activityMap);
/*     */                 } 
/*     */               } else {
/* 388 */                 String[] nodeIds = new String[0];
/*     */                 
/* 390 */                 if (StringUtils.equals(dynamicTask.getStatus(), "completed") && 
/* 391 */                   bpmTaskOpinion != null) {
/* 392 */                   nodeIds = new String[] { bpmTaskOpinion.getTaskKey() };
/* 393 */                   model.setBpmIdentity(bpmTaskOpinion.getTaskKey(), 
/* 394 */                       Arrays.asList(new SysIdentity[] { (SysIdentity)new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId()) }));
/*     */                 } 
/*     */                 
/* 397 */                 Map<String, Object> activityMap = ActivitiUtil.skipPrepare(subInst.getActDefId(), bpmNodeDef1.getNodeId(), nodeIds);
/*     */                 try {
/* 399 */                   this.actTaskService.completeTask(taskId, model.getActionVariables());
/* 400 */                 } catch (Exception e) {
/* 401 */                   throw new BusinessException(e);
/*     */                 } finally {
/* 403 */                   ActivitiUtil.restoreActivity(activityMap);
/*     */                 } 
/*     */               } 
/* 406 */               BpmContext.removeActionModel();
/*     */             } 
/* 408 */             this.dynamicTaskManager.update(dynamicTask); continue;
/*     */           } 
/* 410 */           throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   @Resource
/*     */   private UserService userService; @Resource
/*     */   private GroupService groupService;
/*     */   
/*     */   private DynamicTask getDynamicTask(List<DynamicTask> dynamicTasks, String dynamicNodeId) {
/* 420 */     for (DynamicTask dynamicTask : dynamicTasks) {
/* 421 */       if (StringUtils.equals(dynamicNodeId, dynamicTask.getNodeId())) {
/* 422 */         return dynamicTask;
/*     */       }
/*     */     } 
/* 425 */     return null;
/*     */   } @Resource(name = "defaultBpmRuntimeServiceImpl")
/*     */   private BpmRuntimeService runtimeService; @Resource
/*     */   private DefaultBpmFlowDataAccessor defaultBpmFlowDataAccessor; @Resource
/*     */   private SuperviseTaskExecuter superviseTaskExecuter;
/*     */   private void handleInstanceInfo(DefualtTaskActionCmd model, Boolean isEnd, BpmTaskOpinion bpmTaskOpinion) {
/* 431 */     BpmInstance instance = (BpmInstance)model.getBpmInstance();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 436 */       List<BpmInstance> subs = this.bpmInstanceManager.getByPId(instance.getId());
/* 437 */       Map<String, Integer> subNodeMax = new HashMap<>();
/* 438 */       for (BpmInstance inst : subs) {
/* 439 */         Integer index = subNodeMax.get(inst.getSuperNodeId());
/* 440 */         if (index == null) {
/* 441 */           index = Integer.valueOf(0);
/*     */         }
/* 443 */         subNodeMax.put(inst.getSuperNodeId(), index = Integer.valueOf(index.intValue() + 1));
/*     */       } 
/* 445 */       Map<String, Integer> subNodeIndex = new HashMap<>();
/* 446 */       for (BpmInstance inst : subs) {
/* 447 */         Integer max = subNodeMax.get(inst.getSuperNodeId());
/* 448 */         Integer index = subNodeIndex.get(inst.getSuperNodeId());
/* 449 */         if (index == null) {
/* 450 */           index = Integer.valueOf(0);
/*     */         }
/* 452 */         Integer integer1 = index, integer2 = index = Integer.valueOf(index.intValue() + 1);
/* 453 */         if (index.intValue() < max.intValue()) {
/* 454 */           model.setBpmInstance((IBpmInstance)inst);
/* 455 */           model.setDefId(inst.getDefId());
/* 456 */           handleInstanceInfo(model, Boolean.valueOf(false), null); continue;
/* 457 */         }  if (index == max) {
/* 458 */           model.setBpmInstance((IBpmInstance)inst);
/* 459 */           model.setDefId(inst.getDefId());
/* 460 */           handleInstanceInfo(model, Boolean.valueOf(true), null);
/*     */         } 
/*     */       } 
/* 463 */       model.setBpmInstance((IBpmInstance)instance);
/* 464 */       model.setDefId(instance.getDefId());
/* 465 */       if (isEnd.booleanValue()) {
/* 466 */         BpmInstance supInst = (BpmInstance)this.bpmInstanceManager.get(instance.getParentInstId());
/* 467 */         String[] nodeIds = new String[0];
/* 468 */         if (bpmTaskOpinion != null) {
/* 469 */           nodeIds = new String[] { bpmTaskOpinion.getTaskKey() };
/* 470 */           model.setBpmIdentity(bpmTaskOpinion.getTaskKey(), 
/* 471 */               Arrays.asList(new SysIdentity[] { (SysIdentity)new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId()) }));
/*     */         } 
/* 473 */         Map<String, Object> activityMap = ActivitiUtil.skipPrepare(supInst.getActDefId(), instance.getSuperNodeId(), nodeIds);
/*     */         try {
/* 475 */           BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getEndEvents(instance.getDefId()).get(0);
/* 476 */           List<BpmTask> bpmTasks = this.bpmTaskManager.getByInstId(instance.getId());
/* 477 */           if (CollectionUtil.isNotEmpty(bpmTasks)) {
/* 478 */             for (BpmTask bpmTask : bpmTasks) {
/* 479 */               if (StringUtils.equals("SIGN", bpmTask.getTaskType())) {
/* 480 */                 this.bpmTaskManager.remove(bpmTask.getId());
/* 481 */                 this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
/* 482 */                 BpmTaskOpinion signBpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 483 */                 signBpmTaskOpinion.setApproveTime(new Date());
/* 484 */                 signBpmTaskOpinion.setOpinion(model.getOpinion());
/* 485 */                 signBpmTaskOpinion.setApprover(ContextUtil.getCurrentUserId());
/* 486 */                 signBpmTaskOpinion.setApproverName(ContextUtil.getCurrentUserName());
/* 487 */                 signBpmTaskOpinion.setTaskOrgId(model.getApproveOrgId());
/* 488 */                 this.bpmTaskOpinionManager.update(signBpmTaskOpinion); continue;
/*     */               } 
/* 490 */               model.setBpmTask((IBpmTask)bpmTask);
/* 491 */               BpmContext.setActionModel((ActionCmd)model);
/* 492 */               this.actTaskService.completeTask(bpmTask.getTaskId(), new String[] { bpmNodeDef.getNodeId() });
/* 493 */               BpmContext.removeActionModel();
/*     */             }
/*     */           
/*     */           }
/* 497 */         } catch (Exception e) {
/* 498 */           throw new BusinessException(e);
/*     */         } finally {
/* 500 */           ActivitiUtil.restoreActivity(activityMap);
/*     */         } 
/*     */         
/* 503 */         this.dynamicTaskManager.updateEndByInstId(instance.getId());
/*     */       } else {
/*     */         
/* 506 */         BpmInstance supInst = (BpmInstance)this.bpmInstanceManager.get(instance.getParentInstId());
/* 507 */         Map<String, Object> activityMap = ActivitiUtil.skipPrepare(supInst.getActDefId(), instance.getSuperNodeId(), new String[0]);
/*     */         try {
/* 509 */           BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getEndEvents(instance.getDefId()).get(0);
/* 510 */           List<BpmTask> bpmTasks = this.bpmTaskManager.getByInstId(instance.getId());
/* 511 */           if (CollectionUtil.isNotEmpty(bpmTasks)) {
/* 512 */             for (BpmTask bpmTask : bpmTasks) {
/* 513 */               if (StringUtils.equals("SIGN", bpmTask.getTaskType())) {
/* 514 */                 this.bpmTaskManager.remove(bpmTask.getId());
/* 515 */                 this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
/* 516 */                 BpmTaskOpinion signBpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 517 */                 signBpmTaskOpinion.setApproveTime(new Date());
/* 518 */                 signBpmTaskOpinion.setOpinion(model.getOpinion());
/* 519 */                 signBpmTaskOpinion.setApprover(ContextUtil.getCurrentUserId());
/* 520 */                 signBpmTaskOpinion.setApproverName(ContextUtil.getCurrentUserName());
/* 521 */                 signBpmTaskOpinion.setTaskOrgId(model.getApproveOrgId());
/* 522 */                 signBpmTaskOpinion.setStatus(OpinionStatus.DECREASEDYNAMIC.getKey());
/* 523 */                 this.bpmTaskOpinionManager.update(signBpmTaskOpinion); continue;
/*     */               } 
/* 525 */               model.setBpmTask((IBpmTask)bpmTask);
/* 526 */               BpmContext.setActionModel((ActionCmd)model);
/* 527 */               this.actTaskService.completeTask(bpmTask.getTaskId(), new String[] { bpmNodeDef.getNodeId() });
/* 528 */               BpmContext.removeActionModel();
/*     */             }
/*     */           
/*     */           }
/* 532 */         } catch (Exception e) {
/* 533 */           throw new BusinessException(e);
/*     */         } finally {
/* 535 */           ActivitiUtil.restoreActivity(activityMap);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 544 */       instance.setStatus(InstanceStatus.STATUS_RECOVER.getKey());
/* 545 */       instance.setEndTime(new Date());
/* 546 */       instance.setDuration(Long.valueOf(instance.getEndTime().getTime() - instance.getCreateTime().getTime()));
/* 547 */       this.bpmInstanceManager.update(instance);
/*     */     
/*     */     }
/* 550 */     catch (Exception e) {
/* 551 */       throw new BusinessException(e);
/*     */     } finally {
/* 553 */       GlobalScriptPluginExecutor.clearCanExecute();
/* 554 */       this.superviseTaskExecuter.clearExecForce();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handelFormInit(BaseActionCmd cmd, BpmNodeDef nodeDef) {
/* 559 */     String nodeId = nodeDef.getNodeId();
/* 560 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(cmd.getBpmInstance().getDefId());
/* 561 */     List<NodeInit> nodeInitList = def.getNodeInitList(nodeId);
/*     */     
/* 563 */     Map<String, IBusinessData> bos = cmd.getBizDataMap();
/* 564 */     if (MapUtil.isEmpty(bos)) {
/* 565 */       bos = this.defaultBpmFlowDataAccessor.getTaskBusData(cmd.getTaskId());
/*     */     }
/* 567 */     if (CollectionUtil.isEmpty(nodeInitList))
/*     */       return; 
/* 569 */     Map<String, Object> param = new HashMap<>();
/* 570 */     if (MapUtil.isNotEmpty(bos)) param.putAll(bos); 
/* 571 */     param.put("bpmInstance", cmd.getBpmInstance());
/* 572 */     param.put("actionCmd", cmd);
/* 573 */     ActionType actionType = cmd.getActionType();
/* 574 */     param.put("submitActionDesc", actionType.getName());
/* 575 */     param.put("submitActionName", actionType.getKey());
/* 576 */     param.put("submitOpinion", cmd.getOpinion());
/* 577 */     param.put("isTask", Boolean.valueOf(false));
/*     */ 
/*     */     
/* 580 */     if (cmd instanceof DefualtTaskActionCmd) {
/* 581 */       param.put("isTask", Boolean.valueOf(true));
/* 582 */       param.put("bpmTask", ((DefualtTaskActionCmd)cmd).getBpmTask());
/*     */     } 
/*     */ 
/*     */     
/* 586 */     for (NodeInit init : nodeInitList) {
/* 587 */       if (StringUtil.isNotEmpty(init.getWhenSave()))
/*     */         try {
/* 589 */           this.iGroovyScriptEngine.executeObject(init.getWhenSave(), param);
/* 590 */         } catch (Exception e) {
/* 591 */           throw new BusinessMessage(e.getMessage(), BpmStatusCode.FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR, e);
/*     */         }  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/recrease/handler/RecreaseDynamicTaskExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */