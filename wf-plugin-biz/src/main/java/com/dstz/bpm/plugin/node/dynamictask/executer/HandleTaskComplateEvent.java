/*     */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*     */ import com.dstz.bpm.plugin.core.model.DynamicTask;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class HandleTaskComplateEvent
/*     */ {
/*     */   @Resource
/*     */   DynamicTaskManager dynamicTaskManager;
/*     */   @Resource
/*     */   HandelRejectAction handelRejectAction;
/*     */   @Resource
/*     */   DynamicInstTaskAction dynamicInstTaskAction;
/*     */   
/*     */   public void taskComplateEvent(DynamicTaskPluginDef pluginDef, DefaultBpmTaskPluginSession pluginSession) {
/*  38 */     boolean isEnabled = pluginDef.getIsEnabled().booleanValue();
/*  39 */     BpmNodeDef bpmNodeDef = this.dynamicInstTaskAction.getDynamicInstNodeDef(pluginSession);
/*     */     
/*  41 */     DefualtTaskActionCmd taskActionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  42 */     if (this.dynamicInstTaskAction.isDynamicInstTask(bpmNodeDef))
/*     */     {
/*  44 */       if (taskActionCmd.getActionType() != ActionType.AGREE && taskActionCmd.getActionType() != ActionType.OPPOSE && taskActionCmd
/*  45 */         .getActionType() != ActionType.SIGNAGREE && taskActionCmd.getActionType() != ActionType.SIGNOPPOSE && taskActionCmd
/*  46 */         .getActionType() != ActionType.TASK_FREE_JUMP) {
/*  47 */         throw new BusinessException(String.format("动态多实例任务暂不适用%s操作", new Object[] { taskActionCmd.getActionName() }));
/*     */       }
/*     */     }
/*  50 */     boolean isInstDy = false;
/*  51 */     if (!isEnabled) {
/*  52 */       isInstDy = this.dynamicInstTaskAction.isEndDynamicInstTask(bpmNodeDef, pluginSession);
/*  53 */       isEnabled = isInstDy;
/*     */     } 
/*     */     
/*  56 */     if (!isEnabled) {
/*     */       return;
/*     */     }
/*     */     
/*  60 */     if (!isInstDy) {
/*  61 */       IBpmTask task = pluginSession.getBpmTask();
/*  62 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  63 */       defaultQueryFilter.addFilter("inst_id_", task.getInstId(), QueryOP.EQUAL);
/*  64 */       defaultQueryFilter.addFilter("node_id_", task.getNodeId(), QueryOP.EQUAL);
/*  65 */       defaultQueryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
/*  66 */       List<DynamicTask> dynamicTasks = this.dynamicTaskManager.query((QueryFilter)defaultQueryFilter);
/*     */       
/*  68 */       if (dynamicTasks == null || dynamicTasks.size() == 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  73 */       DynamicTask dynamicTask = dynamicTasks.get(0);
/*  74 */       if (taskActionCmd.getActionType() == ActionType.REJECT) {
/*  75 */         this.handelRejectAction.handelReject(taskActionCmd, dynamicTask);
/*     */         
/*     */         return;
/*     */       } 
/*  79 */       if (dynamicTask.getCurrentIndex().intValue() >= dynamicTask.getAmmount().intValue() - 1) {
/*  80 */         dynamicTask.setStatus("complated");
/*  81 */         this.dynamicTaskManager.update(dynamicTask);
/*     */         
/*     */         return;
/*     */       } 
/*  85 */       dynamicTask.setCurrentIndex(Integer.valueOf(dynamicTask.getCurrentIndex().intValue() + 1));
/*  86 */       if (pluginDef.getIsParallel().booleanValue()) {
/*  87 */         taskActionCmd.setDestinations(new String[0]);
/*     */       } else {
/*  89 */         taskActionCmd.setDestination(task.getNodeId());
/*     */       } 
/*  91 */       this.dynamicTaskManager.update(dynamicTask);
/*     */     } else {
/*     */       
/*  94 */       this.dynamicInstTaskAction.dynamicInstTaskComplate(pluginSession, bpmNodeDef);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkCompleteEvent(DynamicTaskPluginDef pluginDef, DefaultBpmTaskPluginSession pluginSession) {
/* 101 */     BpmNodeDef bpmNodeDef = this.dynamicInstTaskAction.getDynamicInstNodeDef(pluginSession);
/*     */     
/* 103 */     DefualtTaskActionCmd taskActionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 104 */     if (this.dynamicInstTaskAction.isDynamicInstTask(bpmNodeDef))
/*     */     {
/* 106 */       if (taskActionCmd.getActionType() != ActionType.AGREE && taskActionCmd.getActionType() != ActionType.OPPOSE && taskActionCmd
/* 107 */         .getActionType() != ActionType.SIGNAGREE && taskActionCmd.getActionType() != ActionType.SIGNOPPOSE && taskActionCmd
/* 108 */         .getActionType() != ActionType.TASK_FREE_JUMP)
/* 109 */         throw new BusinessException(String.format("动态多实例任务暂不适用%s操作", new Object[] { ActionType.fromKey(taskActionCmd.getActionName()).getName() })); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynamictask/executer/HandleTaskComplateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */