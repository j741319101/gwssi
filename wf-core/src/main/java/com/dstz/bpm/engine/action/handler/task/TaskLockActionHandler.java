/*    */ package com.dstz.bpm.engine.action.handler.task;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.constant.TaskStatus;
/*    */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*    */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*    */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*    */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.api.exception.BusinessMessage;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import javax.annotation.Resource;
/*    */
import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class TaskLockActionHandler
/*    */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*    */ {
/*    */   public ActionType getActionType() {
/* 24 */     return ActionType.LOCK;
/*    */   }
/*    */ 
/*    */   
/*    */   @Resource
/*    */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*    */   
/*    */   public void execute(DefualtTaskActionCmd model) {
/* 32 */     prepareActionDatas(model);
/* 33 */     checkFlowIsValid((BaseActionCmd)model);
/*    */     
/* 35 */     BpmTask task = (BpmTask)model.getBpmTask();
/* 36 */     if (!task.getAssigneeId().equals("0")) {
/* 37 */       throw new BusinessMessage("该任务只有一个候选人没有锁定的必要。");
/*    */     }
/* 39 */     if (StringUtils.isEmpty(model.getTaskLinkId())) {
/* 40 */       throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
/*    */     }
/* 42 */     TaskIdentityLink taskIdentityLink = (TaskIdentityLink)this.taskIdentityLinkManager.get(model.getTaskLinkId());
/* 43 */     if (taskIdentityLink == null) {
/* 44 */       throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
/*    */     }
/* 46 */     task.setAssigneeId(ContextUtil.getCurrentUserId());
/* 47 */     task.setAssigneeNames(ContextUtil.getCurrentUser().getFullname());
/* 48 */     task.setStatus(TaskStatus.LOCK.getKey());
/* 49 */     this.taskManager.update(task);
/* 50 */     BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(task.getId());
/* 51 */     taskOpinion.setAssignInfo("user-" + task.getAssigneeNames() + "-" + ContextUtil.getCurrentUserId() + "-" + taskIdentityLink.getOrgId() + ",");
/* 52 */     this.bpmTaskOpinionManager.update(taskOpinion);
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
/* 67 */     return 6;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 72 */     return Boolean.valueOf(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDefaultGroovyScript() {
/* 77 */     return "return task.getAssigneeId().equals(\"0\")";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 82 */     return "";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/task/TaskLockActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */