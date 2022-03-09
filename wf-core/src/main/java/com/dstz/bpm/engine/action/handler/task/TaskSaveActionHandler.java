/*    */ package com.dstz.bpm.engine.action.handler.task;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */
import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class TaskSaveActionHandler
/*    */   extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
/*    */   public ActionType getActionType() {
/* 12 */     return ActionType.SAVE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void doAction(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 32 */     return 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 38 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 43 */     return Boolean.valueOf(true);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/task/TaskSaveActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */