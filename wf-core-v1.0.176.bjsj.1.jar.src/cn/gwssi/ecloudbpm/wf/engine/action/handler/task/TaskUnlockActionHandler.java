/*    */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.task;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskStatus;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class TaskUnlockActionHandler
/*    */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*    */ {
/*    */   public ActionType getActionType() {
/* 19 */     return ActionType.UNLOCK;
/*    */   }
/*    */   @Resource
/*    */   BpmTaskManager bpmTaskMananger;
/*    */   public void execute(DefualtTaskActionCmd model) {
/* 24 */     prepareActionDatas(model);
/* 25 */     checkFlowIsValid((BaseActionCmd)model);
/*    */     
/* 27 */     BpmTask task = (BpmTask)model.getBpmTask();
/* 28 */     if (!task.getStatus().equals(TaskStatus.LOCK.getKey())) {
/* 29 */       throw new BusinessMessage("该任务并非锁定状态,或已经被解锁，解锁失败");
/*    */     }
/*    */     
/* 32 */     this.bpmTaskMananger.unLockTask(task.getId());
/*    */   }
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
/* 47 */     return 6;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 52 */     return Boolean.valueOf(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDefaultGroovyScript() {
/* 57 */     return "return task.getStatus().equals(\"LOCK\");";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 62 */     return "";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/task/TaskUnlockActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */