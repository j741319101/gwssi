/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.addDo.handler;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskStatus;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.task.AbstractTaskActionHandler;
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


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/addDo/handler/AddDoAgreeActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */