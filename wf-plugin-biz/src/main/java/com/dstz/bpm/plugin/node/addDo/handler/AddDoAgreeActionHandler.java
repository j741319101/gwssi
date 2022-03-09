/*    */ package com.dstz.bpm.plugin.node.addDo.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.constant.OpinionStatus;
/*    */ import com.dstz.bpm.api.constant.TaskStatus;
/*    */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*    */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class AddDoAgreeActionHandler
/*    */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*    */ {
/*    */   @Autowired
/*    */   private BpmTaskManager bpmTaskManager;
/*    */   @Autowired
/*    */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*    */   
/*    */   public ActionType getActionType() {
/* 34 */     return ActionType.ADDDOAGREE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 39 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 44 */     return "/bpm/task/taskOpinionDialog.html";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void doAction(DefualtTaskActionCmd actionModel) {
/* 54 */     this.bpmTaskOpinionManager.commonUpdate(actionModel.getTaskId(), OpinionStatus.ADD_DO_AGREE, actionModel.getOpinion());
/* 55 */     this.bpmTaskManager.remove(actionModel.getTaskId());
/*    */     
/* 57 */     BpmTask sourceTask = (BpmTask)this.bpmTaskManager.get(actionModel.getBpmTask().getParentId());
/* 58 */     sourceTask.setAssigneeId(sourceTask.getAssigneeId().substring(1));
/* 59 */     sourceTask.setStatus(TaskStatus.ADDDOED.getKey());
/* 60 */     this.bpmTaskManager.update(sourceTask);
/*    */     
/* 62 */     BpmTaskOpinion sourceOpinion = this.bpmTaskOpinionManager.getByTaskId(sourceTask.getId());
/* 63 */     sourceOpinion.setTaskId("-" + sourceTask.getId());
/* 64 */     this.bpmTaskOpinionManager.update(sourceOpinion);
/*    */ 
/*    */     
/* 67 */     this.bpmTaskOpinionManager.createOpinion((IBpmTask)sourceTask, actionModel.getBpmInstance(), null, null, ActionType.ADDDOAGREE.getKey(), actionModel.getFormId());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 78 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/addDo/handler/AddDoAgreeActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */