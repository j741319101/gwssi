/*     */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*     */ 
/*     */ import com.dstz.bpm.act.util.ActivitiUtil;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*     */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*     */ import com.dstz.bpm.plugin.core.model.DynamicTask;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class DynamicInstTaskAction
/*     */   extends DefaultExtendTaskAction
/*     */ {
/*     */   @Resource
/*     */   DynamicTaskManager dynamicTaskManager;
/*  50 */   private final ThreadLocal<BpmTask> taskThreadLocal = new ThreadLocal<>();
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   BpmInstanceManager bpmInstanceManager;
/*  55 */   private static ThreadLocal<Map<String, ArrayDeque<Integer>>> threadDynamictaskIndex = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static ThreadLocal<Map<String, ArrayDeque<List<SysIdentity>>>> threadDynamictaskIdentities = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   public void dynamicInstTaskComplate(DefaultBpmTaskPluginSession pluginSession, BpmNodeDef instBpmNodeDef) {
/*  63 */     BpmInstance childBpmInstance = (BpmInstance)pluginSession.getBpmInstance();
/*  64 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  65 */     String parentInstId = "";
/*  66 */     String superNodeId = "";
/*  67 */     if (instBpmNodeDef.getType() == NodeType.CALLACTIVITY) {
/*  68 */       parentInstId = childBpmInstance.getParentInstId();
/*  69 */       superNodeId = childBpmInstance.getSuperNodeId();
/*     */     } else {
/*  71 */       parentInstId = childBpmInstance.getId();
/*  72 */       superNodeId = instBpmNodeDef.getParentBpmNodeDef().getNodeId();
/*     */     } 
/*  74 */     IBpmTask childTask = pluginSession.getBpmTask();
/*  75 */     AtomicBoolean isEndNode = new AtomicBoolean(false);
/*  76 */     String[] destinationNodes = model.getDestinations();
/*  77 */     if (destinationNodes != null && destinationNodes.length > 0) {
/*  78 */       Arrays.<String>asList(destinationNodes).forEach(destinationNodeId -> {
/*     */             BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(childBpmInstance.getDefId(), destinationNodeId);
/*     */             if (bpmNodeDef.getType() == NodeType.END) {
/*     */               isEndNode.set(true);
/*     */             }
/*     */           });
/*     */     } else {
/*  85 */       BpmNodeDef bpmNodeDefEnd = this.bpmProcessDefService.getBpmNodeDef(childBpmInstance.getDefId(), childTask.getNodeId());
/*  86 */       List<BpmNodeDef> outBpmNodeDefs = bpmNodeDefEnd.getOutcomeNodes();
/*  87 */       outBpmNodeDefs.forEach(outNode -> {
/*     */             if (outNode.getType() == NodeType.END) {
/*     */               isEndNode.set(true);
/*     */             }
/*     */           });
/*     */     } 
/*  93 */     if (isEndNode.get()) {
/*  94 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  95 */       defaultQueryFilter.addFilter("inst_id_", parentInstId, QueryOP.EQUAL);
/*  96 */       defaultQueryFilter.addFilter("node_id_", superNodeId, QueryOP.EQUAL);
/*  97 */       defaultQueryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
/*     */       
/*  99 */       List<DynamicTask> dynamicTasks = this.dynamicTaskManager.query((QueryFilter)defaultQueryFilter);
/* 100 */       if (dynamicTasks == null || dynamicTasks.size() == 0) {
/*     */         return;
/*     */       }
/* 103 */       DynamicTask dynamicTask = dynamicTasks.get(0);
/*     */       
/* 105 */       dynamicTask.setCurrentIndex(Integer.valueOf(dynamicTask.getCurrentIndex().intValue() + 1));
/* 106 */       if (dynamicTask.getCurrentIndex().intValue() >= dynamicTask.getAmmount().intValue()) {
/* 107 */         dynamicTask.setStatus("completed");
/* 108 */         this.dynamicTaskManager.update(dynamicTask);
/*     */         
/*     */         return;
/*     */       } 
/* 112 */       BpmInstance parentInstance = (BpmInstance)this.bpmInstanceManager.get(parentInstId);
/* 113 */       BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(parentInstance.getDefId(), superNodeId);
/*     */       
/* 115 */       String[] destinations = new String[0];
/* 116 */       if (!DynamicTaskPluginExecuter.isParallel(bpmNodeDef)) {
/* 117 */         destinations = new String[] { superNodeId };
/*     */       }
/* 119 */       ActivitiUtil.skipPrepare(parentInstance.getActDefId(), superNodeId, destinations);
/* 120 */       this.dynamicTaskManager.update(dynamicTask);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   BpmNodeDef getDynamicInstNodeDef(DefaultBpmTaskPluginSession pluginSession) {
/* 126 */     BpmInstance bpmInstance = (BpmInstance)pluginSession.getBpmInstance();
/* 127 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/* 128 */     BpmNodeDef instNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), bpmTask.getNodeId());
/* 129 */     BpmNodeDef subInstNodeDef = instNodeDef.getParentBpmNodeDef();
/* 130 */     if (subInstNodeDef != null && subInstNodeDef instanceof SubProcessNodeDef) {
/* 131 */       return instNodeDef;
/*     */     }
/* 133 */     String parentInstId = bpmInstance.getParentInstId();
/* 134 */     String superNodeId = bpmInstance.getSuperNodeId();
/*     */     
/* 136 */     BpmNodeDef bpmNodeDef = null;
/* 137 */     if (StringUtil.isNotEmpty(parentInstId)) {
/* 138 */       IBpmInstance parentInst = (IBpmInstance)this.bpmInstanceManager.get(parentInstId);
/* 139 */       if (parentInst != null)
/* 140 */         bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(parentInst.getDefId(), superNodeId); 
/*     */     } 
/* 142 */     return bpmNodeDef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBeginDynamicInstTask(BpmNodeDef bpmNodeDef, DefaultBpmTaskPluginSession pluginSession, boolean isPreDy) {
/* 152 */     AtomicReference<Boolean> isEnabled = new AtomicReference<>(Boolean.valueOf(false));
/* 153 */     if (bpmNodeDef == null) {
/* 154 */       return false;
/*     */     }
/* 156 */     BaseActionCmd baseActionCmd = (BaseActionCmd)BpmContext.getActionModel();
/*     */     
/* 158 */     BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 159 */     if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
/* 160 */       subProcessNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/* 165 */       if (((Boolean)isEnabled.get()).booleanValue()) {
/* 166 */         BpmProcessDef subBpmProcessDef = ((SubProcessNodeDef)subProcessNodeDef).getChildBpmProcessDef();
/*     */         
/* 168 */         String fromTaskName = (String)pluginSession.get("submitTaskName");
/* 169 */         if (StringUtils.isEmpty(fromTaskName)) {
/* 170 */           fromTaskName = baseActionCmd.getDynamicSubmitTaskName();
/*     */         }
/* 172 */         if (StringUtils.isNotEmpty(fromTaskName)) {
/* 173 */           String finalFromTaskName = fromTaskName;
/* 174 */           subBpmProcessDef.getBpmnNodeDefs().forEach(innerNodeDef -> {
/*     */                 if (StringUtils.equals(finalFromTaskName, innerNodeDef.getName())) {
/*     */                   isEnabled.set(Boolean.valueOf(false));
/*     */                 }
/*     */               });
/*     */         } else {
/* 180 */           isEnabled.set(Boolean.valueOf(false));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 185 */     if (StringUtils.equals(bpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
/* 186 */       DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 187 */       if (dynamicTaskPluginContext != null && ((DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled().booleanValue())
/*     */       {
/* 189 */         if (isPreDy) {
/* 190 */           isEnabled.set(Boolean.valueOf(true));
/*     */         } else {
/* 192 */           IBpmTask bpmTask = pluginSession.getBpmTask();
/* 193 */           BpmNodeDef currentNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/* 194 */           List<BpmNodeDef> bpmNodeDefs = this.bpmProcessDefService.getStartNodes(bpmTask.getDefId());
/* 195 */           bpmNodeDefs.forEach(bpmNodeDef1 -> {
/*     */                 if (StringUtils.equals(currentNodeDef.getNodeId(), bpmNodeDef1.getNodeId())) {
/*     */                   isEnabled.set(Boolean.valueOf(true));
/*     */                 }
/*     */               });
/*     */           
/* 201 */           String compareName = baseActionCmd.getDynamicSubmitTaskName();
/* 202 */           if (StringUtil.isEmpty(compareName)) {
/* 203 */             return false;
/*     */           }
/* 205 */           for (BpmNodeDef bpmNodeDef1 : this.bpmProcessDefService.getBpmProcessDef(bpmTask.getDefId()).getBpmnNodeDefs()) {
/* 206 */             if (StringUtils.equals(compareName, bpmNodeDef1.getName())) {
/* 207 */               isEnabled.set(Boolean.valueOf(false));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 213 */     return ((Boolean)isEnabled.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isEndDynamicInstTask(BpmNodeDef bpmNodeDef, DefaultBpmTaskPluginSession pluginSession) {
/* 217 */     AtomicReference<Boolean> isEnabled = new AtomicReference<>(Boolean.valueOf(false));
/* 218 */     if (bpmNodeDef == null) {
/* 219 */       return false;
/*     */     }
/*     */     
/* 222 */     BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 223 */     if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
/* 224 */       subProcessNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/* 229 */       if (((Boolean)isEnabled.get()).booleanValue()) {
/* 230 */         BpmProcessDef subBpmProcessDef = ((SubProcessNodeDef)subProcessNodeDef).getChildBpmProcessDef();
/*     */         
/* 232 */         isEnabled.set(Boolean.valueOf(false));
/* 233 */         if (!((Boolean)isEnabled.get()).booleanValue()) {
/* 234 */           DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 235 */           String[] destinations = model.getDestinations();
/* 236 */           if (destinations != null) {
/* 237 */             if (destinations.length == 1) {
/* 238 */               BpmNodeDef endBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), destinations[0]);
/* 239 */               if (endBpmNodeDef.getType() == NodeType.END) {
/* 240 */                 isEnabled.set(Boolean.valueOf(true));
/*     */               }
/*     */             } 
/*     */           } else {
/* 244 */             List<BpmNodeDef> bpmNodeDefs = bpmNodeDef.getOutcomeNodes();
/* 245 */             if (bpmNodeDefs != null && bpmNodeDefs.size() == 1) {
/* 246 */               bpmNodeDefs.forEach(endNode -> {
/*     */                     if (endNode.getType() == NodeType.END) {
/*     */                       isEnabled.set(Boolean.valueOf(true));
/*     */                     }
/*     */                   });
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     if (StringUtils.equals(bpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
/* 258 */       bpmNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/*     */     }
/* 264 */     return ((Boolean)isEnabled.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean isDynamicTask(BpmNodeDef bpmNodeDef) {
/* 268 */     AtomicReference<Boolean> isEnabled = new AtomicReference<>(Boolean.valueOf(false));
/* 269 */     if (bpmNodeDef == null || bpmNodeDef instanceof com.dstz.bpm.api.model.nodedef.impl.GateWayBpmNodeDef || bpmNodeDef instanceof com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef) {
/* 270 */       return false;
/*     */     }
/* 272 */     bpmNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */           if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */             isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */           }
/*     */         });
/* 277 */     return ((Boolean)isEnabled.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean isDynamicInstTask(BpmNodeDef bpmNodeDef) {
/* 281 */     AtomicReference<Boolean> isEnabled = new AtomicReference<>(Boolean.valueOf(false));
/* 282 */     if (bpmNodeDef == null) {
/* 283 */       return false;
/*     */     }
/*     */     
/* 286 */     BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 287 */     if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
/* 288 */       subProcessNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 295 */     if (StringUtils.equals(bpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
/* 296 */       bpmNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/*     */     }
/* 302 */     return ((Boolean)isEnabled.get()).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void canFreeJump(IBpmTask bpmTask) {
/* 307 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 308 */     if (isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getNodeId())) && 
/* 309 */       !isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getDestination()))) {
/* 310 */       throw new BusinessException("动态子流程暂不适用跳转到子流程外部节点");
/*     */     }
/*     */     
/* 313 */     if (!isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getNodeId())) && 
/* 314 */       isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getDestination()))) {
/* 315 */       throw new BusinessException("动态子流程暂不适用外部节点跳转到子流程");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkEvent(DefaultBpmTaskPluginSession pluginSession) {
/* 321 */     BpmNodeDef instBpmNodeDef = getDynamicInstNodeDef(pluginSession);
/* 322 */     List<String> keys = Arrays.asList(new String[] { ActionType.AGREE.getKey(), ActionType.OPPOSE.getKey(), ActionType.SIGNOPPOSE
/* 323 */           .getKey(), ActionType.SIGNAGREE.getKey(), ActionType.TASK_FREE_JUMP.getKey(), ActionType.CREATE
/* 324 */           .getKey(), ActionType.DECREASEDYNAMIC.getKey(), ActionType.INCREASEDYNAMIC.getKey(), ActionType.START
/* 325 */           .getKey(), ActionType.RECALL.getKey(), ActionType.REJECT.getKey(), ActionType.INSTANCE_RESTART.getKey() });
/* 326 */     if (isDynamicInstTask(instBpmNodeDef) && 
/* 327 */       !CollectionUtil.contains(keys, pluginSession.get("submitActionName").toString())) {
/* 328 */       throw new BusinessException(String.format("动态多实例任务暂不适用%s操作", new Object[] { pluginSession.get("submitActionDesc") }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteDataByInstId(String instId) {
/* 335 */     this.dynamicTaskManager.removeByInstId(instId);
/*     */   }
/*     */   
/*     */   public BpmTask getTaskThreadLocal() {
/* 339 */     return this.taskThreadLocal.get();
/*     */   }
/*     */   
/*     */   public void setTaskThreadLocal(BpmTask bpmTask) {
/* 343 */     this.taskThreadLocal.set(bpmTask);
/*     */   }
/*     */   
/*     */   public void clearTaskThreadLocal() {
/* 347 */     this.taskThreadLocal.remove();
/*     */   }
/*     */   
/*     */   public void clearIdentities() {
/* 351 */     threadDynamictaskIdentities.remove();
/* 352 */     threadDynamictaskIndex.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void pushDynamictaskIndex(String nodeId, Integer index) {
/* 363 */     Map<String, ArrayDeque<Integer>> arrayDequeMap = threadDynamictaskIndex.get();
/* 364 */     if (arrayDequeMap == null) {
/* 365 */       arrayDequeMap = new HashMap<>();
/* 366 */       threadDynamictaskIndex.set(arrayDequeMap);
/*     */     } 
/* 368 */     ArrayDeque<Integer> arrayDeque = arrayDequeMap.get(nodeId);
/* 369 */     if (arrayDeque == null) {
/* 370 */       arrayDeque = new ArrayDeque<>();
/* 371 */       arrayDequeMap.put(nodeId, arrayDeque);
/*     */     } 
/* 373 */     arrayDeque.add(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Integer popDynamictaskIndex(String nodeId) {
/* 382 */     Map<String, ArrayDeque<Integer>> arrayDequeMap = threadDynamictaskIndex.get();
/* 383 */     if (arrayDequeMap == null) {
/* 384 */       return null;
/*     */     }
/* 386 */     ArrayDeque<Integer> arrayDeque = arrayDequeMap.get(nodeId);
/* 387 */     if (arrayDeque.isEmpty()) {
/* 388 */       return null;
/*     */     }
/* 390 */     return arrayDeque.poll();
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
/*     */   public static void pushDynamictaskIdentities(String nodeId, List<SysIdentity> sysIdentity) {
/* 402 */     Map<String, ArrayDeque<List<SysIdentity>>> arrayDequeMap = threadDynamictaskIdentities.get();
/* 403 */     if (CollectionUtil.isEmpty(arrayDequeMap)) {
/* 404 */       arrayDequeMap = new HashMap<>();
/* 405 */       threadDynamictaskIdentities.set(arrayDequeMap);
/*     */     } 
/* 407 */     ArrayDeque<List<SysIdentity>> arrayDeque = arrayDequeMap.get(nodeId);
/* 408 */     if (arrayDeque == null) {
/* 409 */       arrayDeque = new ArrayDeque<>();
/* 410 */       arrayDequeMap.put(nodeId, arrayDeque);
/*     */     } 
/* 412 */     arrayDeque.add(sysIdentity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<SysIdentity> popDynamictaskIdentities(String nodeId) {
/* 421 */     Map<String, ArrayDeque<List<SysIdentity>>> arrayDequeMap = threadDynamictaskIdentities.get();
/* 422 */     if (CollectionUtil.isEmpty(arrayDequeMap)) {
/* 423 */       return null;
/*     */     }
/* 425 */     ArrayDeque<List<SysIdentity>> arrayDeque = arrayDequeMap.get(nodeId);
/* 426 */     if (arrayDeque.isEmpty()) {
/* 427 */       return null;
/*     */     }
/* 429 */     return arrayDeque.poll();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRecall(IBpmTask callTask) {
/* 434 */     BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(callTask.getInstId());
/* 435 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), callTask.getNodeId());
/* 436 */     DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 437 */     DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
/* 438 */     if (dynamicTaskPluginDef.getIsEnabled().booleanValue()) {
/* 439 */       return false;
/*     */     }
/* 441 */     BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 442 */     if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
/* 443 */       DynamicTaskPluginContext supDynamicTaskPluginContext = (DynamicTaskPluginContext)subProcessNodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 444 */       DynamicTaskPluginDef supDynamicTaskPluginDef = (DynamicTaskPluginDef)supDynamicTaskPluginContext.getBpmPluginDef();
/* 445 */       if (supDynamicTaskPluginDef.getIsEnabled().booleanValue()) {
/* 446 */         return false;
/*     */       }
/*     */     } 
/* 449 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canReject(IBpmTask curTask, IBpmTask rejectTask) {
/* 455 */     if (!StringUtils.equals(rejectTask.getInstId(), curTask.getInstId())) {
/* 456 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 460 */     BpmInstance curBpmInstance = (BpmInstance)this.bpmInstanceManager.get(curTask.getInstId());
/* 461 */     BpmNodeDef curBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(curBpmInstance.getDefId(), curTask.getNodeId());
/* 462 */     DynamicTaskPluginContext curDynamicTaskPluginContext = (DynamicTaskPluginContext)curBpmNodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 463 */     if (((DynamicTaskPluginDef)curDynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled().booleanValue()) {
/* 464 */       return false;
/*     */     }
/*     */     
/* 467 */     BpmInstance callBpmInstance = (BpmInstance)this.bpmInstanceManager.get(rejectTask.getInstId());
/* 468 */     BpmNodeDef callBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(callBpmInstance.getDefId(), rejectTask.getNodeId());
/* 469 */     DynamicTaskPluginContext callDynamicTaskPluginContext = (DynamicTaskPluginContext)callBpmNodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 470 */     if (((DynamicTaskPluginDef)callDynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled().booleanValue()) {
/* 471 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 475 */     BpmNodeDef curSubProcessNodeDef = curBpmNodeDef.getParentBpmNodeDef();
/* 476 */     BpmNodeDef callSubProcessNodeDef = callBpmNodeDef.getParentBpmNodeDef();
/* 477 */     if (curSubProcessNodeDef != null && curSubProcessNodeDef instanceof SubProcessNodeDef) {
/* 478 */       DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)curSubProcessNodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 479 */       if (((DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled().booleanValue() && (
/* 480 */         callSubProcessNodeDef == null || callSubProcessNodeDef.getNodeId() != curSubProcessNodeDef.getNodeId())) {
/* 481 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 485 */     if (callSubProcessNodeDef != null && callSubProcessNodeDef instanceof SubProcessNodeDef) {
/* 486 */       DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)callSubProcessNodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 487 */       if (((DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled().booleanValue() && (
/* 488 */         curSubProcessNodeDef == null || callSubProcessNodeDef.getNodeId() != curSubProcessNodeDef.getNodeId())) {
/* 489 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 493 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/dynamictask/executer/DynamicInstTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */