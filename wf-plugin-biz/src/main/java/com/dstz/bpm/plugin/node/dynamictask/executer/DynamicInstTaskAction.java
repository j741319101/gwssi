/*     */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.bpm.act.util.ActivitiUtil;
/*     */ import com.dstz.bpm.api.constant.NodeType;
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
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*     */ import com.dstz.bpm.plugin.core.model.DynamicTask;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import com.alibaba.druid.util.StringUtils;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class DynamicInstTaskAction
/*     */   extends DefaultExtendTaskAction
/*     */ {
/*     */   @Resource
/*     */   DynamicTaskManager dynamicTaskManager;
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   public void dynamicInstTaskComplate(DefaultBpmTaskPluginSession pluginSession, BpmNodeDef instBpmNodeDef) {
/*  52 */     BpmInstance childBpmInstance = (BpmInstance)pluginSession.getBpmInstance();
/*  53 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  54 */     String parentInstId = "";
/*  55 */     String superNodeId = "";
/*  56 */     if (instBpmNodeDef.getType() == NodeType.CALLACTIVITY) {
/*  57 */       parentInstId = childBpmInstance.getParentInstId();
/*  58 */       superNodeId = childBpmInstance.getSuperNodeId();
/*     */     } else {
/*  60 */       parentInstId = childBpmInstance.getId();
/*  61 */       superNodeId = instBpmNodeDef.getParentBpmNodeDef().getNodeId();
/*     */     } 
/*  63 */     IBpmTask childTask = pluginSession.getBpmTask();
/*  64 */     AtomicBoolean isEndNode = new AtomicBoolean(false);
/*  65 */     String[] destinationNodes = model.getDestinations();
/*  66 */     if (destinationNodes != null && destinationNodes.length > 0) {
/*  67 */       Arrays.<String>asList(destinationNodes).forEach(destinationNodeId -> {
/*     */             BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(childBpmInstance.getDefId(), destinationNodeId);
/*     */             if (bpmNodeDef.getType() == NodeType.END) {
/*     */               isEndNode.set(true);
/*     */             }
/*     */           });
/*     */     } else {
/*  74 */       BpmNodeDef bpmNodeDefEnd = this.bpmProcessDefService.getBpmNodeDef(childBpmInstance.getDefId(), childTask.getNodeId());
/*  75 */       List<BpmNodeDef> outBpmNodeDefs = bpmNodeDefEnd.getOutcomeNodes();
/*  76 */       outBpmNodeDefs.forEach(outNode -> {
/*     */             if (outNode.getType() == NodeType.END) {
/*     */               isEndNode.set(true);
/*     */             }
/*     */           });
/*     */     } 
/*  82 */     if (isEndNode.get()) {
/*  83 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  84 */       defaultQueryFilter.addFilter("inst_id_", parentInstId, QueryOP.EQUAL);
/*  85 */       defaultQueryFilter.addFilter("node_id_", superNodeId, QueryOP.EQUAL);
/*  86 */       defaultQueryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
/*     */       
/*  88 */       List<DynamicTask> dynamicTasks = this.dynamicTaskManager.query((QueryFilter)defaultQueryFilter);
/*  89 */       if (dynamicTasks == null || dynamicTasks.size() == 0) {
/*     */         return;
/*     */       }
/*  92 */       DynamicTask dynamicTask = dynamicTasks.get(0);
/*  93 */       if (dynamicTask.getCurrentIndex().intValue() >= dynamicTask.getAmmount().intValue() - 1) {
/*  94 */         dynamicTask.setStatus("complated");
/*  95 */         this.dynamicTaskManager.update(dynamicTask);
/*     */         
/*     */         return;
/*     */       } 
/*  99 */       BpmInstance parentInstance = (BpmInstance)this.bpmInstanceManager.get(parentInstId);
/* 100 */       BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(parentInstance.getDefId(), superNodeId);
/*     */       
/* 102 */       dynamicTask.setCurrentIndex(Integer.valueOf(dynamicTask.getCurrentIndex().intValue() + 1));
/* 103 */       String[] destinations = new String[0];
/* 104 */       if (!DynamicTaskPluginExecuter.isParallel(bpmNodeDef)) {
/* 105 */         destinations = new String[] { superNodeId };
/*     */       }
/* 107 */       ActivitiUtil.skipPrepare(parentInstance.getActDefId(), superNodeId, destinations);
/* 108 */       this.dynamicTaskManager.update(dynamicTask);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   BpmNodeDef getDynamicInstNodeDef(DefaultBpmTaskPluginSession pluginSession) {
/* 114 */     BpmInstance bpmInstance = (BpmInstance)pluginSession.getBpmInstance();
/* 115 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/* 116 */     BpmNodeDef instNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), bpmTask.getNodeId());
/* 117 */     BpmNodeDef subInstNodeDef = instNodeDef.getParentBpmNodeDef();
/* 118 */     if (subInstNodeDef != null && subInstNodeDef instanceof SubProcessNodeDef) {
/* 119 */       return instNodeDef;
/*     */     }
/* 121 */     String parentInstId = bpmInstance.getParentInstId();
/* 122 */     String superNodeId = bpmInstance.getSuperNodeId();
/*     */     
/* 124 */     BpmNodeDef bpmNodeDef = null;
/* 125 */     if (StringUtil.isNotEmpty(parentInstId)) {
/* 126 */       IBpmInstance parentInst = (IBpmInstance)this.bpmInstanceManager.get(parentInstId);
/* 127 */       if (parentInst != null)
/* 128 */         bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(parentInst.getDefId(), superNodeId); 
/*     */     } 
/* 130 */     return bpmNodeDef;
/*     */   }
/*     */   
/*     */   public boolean isBeginDynamicInstTask(BpmNodeDef bpmNodeDef, DefaultBpmTaskPluginSession pluginSession) {
/* 134 */     AtomicReference<Boolean> isEnabled = new AtomicReference<>(Boolean.valueOf(false));
/* 135 */     if (bpmNodeDef == null) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 140 */     if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
/* 141 */       subProcessNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/* 146 */       if (((Boolean)isEnabled.get()).booleanValue()) {
/* 147 */         BpmProcessDef subBpmProcessDef = ((SubProcessNodeDef)subProcessNodeDef).getChildBpmProcessDef();
/*     */         
/* 149 */         String fromTaskName = pluginSession.get("submitTaskName").toString();
/* 150 */         subBpmProcessDef.getBpmnNodeDefs().forEach(innerNodeDef -> {
/*     */               if (StringUtils.equals(fromTaskName, innerNodeDef.getName())) {
/*     */                 isEnabled.set(Boolean.valueOf(false));
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */     
/* 158 */     if (StringUtils.equals(bpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
/* 159 */       bpmNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/*     */     }
/* 165 */     return ((Boolean)isEnabled.get()).booleanValue();
/*     */   }
/*     */   public boolean isEndDynamicInstTask(BpmNodeDef bpmNodeDef, DefaultBpmTaskPluginSession pluginSession) {
/* 168 */     AtomicReference<Boolean> isEnabled = new AtomicReference<>(Boolean.valueOf(false));
/* 169 */     if (bpmNodeDef == null) {
/* 170 */       return false;
/*     */     }
/*     */     
/* 173 */     BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 174 */     if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
/* 175 */       subProcessNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/* 180 */       if (((Boolean)isEnabled.get()).booleanValue()) {
/* 181 */         BpmProcessDef subBpmProcessDef = ((SubProcessNodeDef)subProcessNodeDef).getChildBpmProcessDef();
/*     */         
/* 183 */         isEnabled.set(Boolean.valueOf(false));
/* 184 */         if (!((Boolean)isEnabled.get()).booleanValue()) {
/* 185 */           DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 186 */           String[] destinations = model.getDestinations();
/* 187 */           if (destinations != null) {
/* 188 */             if (destinations.length == 1) {
/* 189 */               BpmNodeDef endBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), destinations[0]);
/* 190 */               if (endBpmNodeDef.getType() == NodeType.END) {
/* 191 */                 isEnabled.set(Boolean.valueOf(true));
/*     */               }
/*     */             } 
/*     */           } else {
/* 195 */             List<BpmNodeDef> bpmNodeDefs = bpmNodeDef.getOutcomeNodes();
/* 196 */             if (bpmNodeDefs != null && bpmNodeDefs.size() == 1) {
/* 197 */               bpmNodeDefs.forEach(endNode -> {
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
/* 208 */     if (StringUtils.equals(bpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
/* 209 */       bpmNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/*     */     }
/* 215 */     return ((Boolean)isEnabled.get()).booleanValue();
/*     */   }
/*     */   public boolean isDynamicTask(BpmNodeDef bpmNodeDef) {
/* 218 */     AtomicReference<Boolean> isEnabled = new AtomicReference<>(Boolean.valueOf(false));
/* 219 */     if (bpmNodeDef == null || bpmNodeDef instanceof com.dstz.bpm.api.model.nodedef.impl.GateWayBpmNodeDef) {
/* 220 */       return false;
/*     */     }
/* 222 */     bpmNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */           if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */             isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */           }
/*     */         });
/* 227 */     return ((Boolean)isEnabled.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isDynamicInstTask(BpmNodeDef bpmNodeDef) {
/* 231 */     AtomicReference<Boolean> isEnabled = new AtomicReference<>(Boolean.valueOf(false));
/* 232 */     if (bpmNodeDef == null) {
/* 233 */       return false;
/*     */     }
/*     */     
/* 236 */     BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 237 */     if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
/* 238 */       subProcessNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 245 */     if (StringUtils.equals(bpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
/* 246 */       bpmNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*     */             if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*     */               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
/*     */             }
/*     */           });
/*     */     }
/* 252 */     return ((Boolean)isEnabled.get()).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void canFreeJump(IBpmTask bpmTask) {
/* 257 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 258 */     if (isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getNodeId())) && 
/* 259 */       !isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getDestination())))
/* 260 */       throw new BusinessException("动态子流程禁止跳转到子流程外部节点"); 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynamictask/executer/DynamicInstTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */