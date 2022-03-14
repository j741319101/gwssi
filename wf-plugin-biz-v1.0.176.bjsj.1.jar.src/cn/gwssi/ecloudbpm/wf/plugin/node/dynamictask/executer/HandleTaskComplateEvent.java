/*    */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.engine.context.BpmContext;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*    */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*    */ import com.dstz.bpm.plugin.core.model.DynamicTask;
/*    */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*    */ import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.api.query.QueryOP;
/*    */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class HandleTaskComplateEvent
/*    */ {
/*    */   @Resource
/*    */   DynamicTaskManager dynamicTaskManager;
/*    */   @Resource
/*    */   HandelRejectAction handelRejectAction;
/*    */   @Resource
/*    */   DynamicInstTaskAction dynamicInstTaskAction;
/*    */   @Resource
/*    */   private SuperviseTaskExecuter superviseTaskExecuter;
/*    */   
/*    */   public void taskComplateEvent(DynamicTaskPluginDef pluginDef, DefaultBpmTaskPluginSession pluginSession) {
/* 34 */     IBpmTask task = pluginSession.getBpmTask();
/*    */     
/* 36 */     this.superviseTaskExecuter.taskComplete(pluginSession);
/* 37 */     boolean isEnabled = pluginDef.getIsEnabled().booleanValue();
/* 38 */     BpmNodeDef bpmNodeDef = this.dynamicInstTaskAction.getDynamicInstNodeDef(pluginSession);
/* 39 */     DefualtTaskActionCmd taskActionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 40 */     boolean isInstDy = false;
/* 41 */     if (!isEnabled) {
/* 42 */       isInstDy = this.dynamicInstTaskAction.isEndDynamicInstTask(bpmNodeDef, pluginSession);
/* 43 */       isEnabled = isInstDy;
/*    */     } 
/*    */     
/* 46 */     if (!isEnabled) {
/*    */       return;
/*    */     }
/*    */     
/* 50 */     if (!isInstDy) {
/* 51 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 52 */       defaultQueryFilter.addFilter("inst_id_", task.getInstId(), QueryOP.EQUAL);
/* 53 */       defaultQueryFilter.addFilter("node_id_", task.getNodeId(), QueryOP.EQUAL);
/* 54 */       defaultQueryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
/* 55 */       List<DynamicTask> dynamicTasks = this.dynamicTaskManager.query((QueryFilter)defaultQueryFilter);
/*    */       
/* 57 */       if (dynamicTasks == null || dynamicTasks.size() == 0) {
/*    */         return;
/*    */       }
/*    */ 
/*    */       
/* 62 */       DynamicTask dynamicTask = dynamicTasks.get(0);
/* 63 */       if (taskActionCmd.getActionType() == ActionType.REJECT) {
/* 64 */         this.handelRejectAction.handelReject(taskActionCmd, dynamicTask);
/*    */         
/*    */         return;
/*    */       } 
/* 68 */       dynamicTask.setCurrentIndex(Integer.valueOf(dynamicTask.getCurrentIndex().intValue() + 1));
/* 69 */       if (dynamicTask.getCurrentIndex().intValue() >= dynamicTask.getAmmount().intValue()) {
/* 70 */         dynamicTask.setStatus("completed");
/* 71 */         this.dynamicTaskManager.update(dynamicTask);
/*    */         return;
/*    */       } 
/* 74 */       if (pluginDef.getIsParallel().booleanValue()) {
/* 75 */         taskActionCmd.setDestinations(new String[0]);
/*    */       } else {
/* 77 */         taskActionCmd.setDestination(task.getNodeId());
/*    */       } 
/* 79 */       this.dynamicTaskManager.update(dynamicTask);
/*    */     } else {
/*    */       
/* 82 */       this.dynamicInstTaskAction.dynamicInstTaskComplate(pluginSession, bpmNodeDef);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/dynamictask/executer/HandleTaskComplateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */