/*    */ package com.dstz.bpm.engine.plugin.session.impl;
/*    */ 
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.core.model.TaskIdentityLink;

/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultBpmTaskPluginSession
/*    */   extends DefaultBpmExecutionPluginSession
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private IBpmTask bpmTask;
/*    */   private TaskIdentityLink taskIdentityLink;
/*    */   private String currentOrgId;
/*    */   
/*    */   public IBpmTask getBpmTask() {
/* 20 */     return this.bpmTask;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTaskIdentityLink(TaskIdentityLink taskIdentityLink) {
/* 26 */     put("bpmTaskLink", taskIdentityLink);
/* 27 */     this.taskIdentityLink = taskIdentityLink;
/*    */   }
/*    */   
/*    */   public TaskIdentityLink getTaskIdentityLink() {
/* 31 */     return this.taskIdentityLink;
/*    */   }
/*    */   
/*    */   public void setBpmTask(IBpmTask bpmTask) {
/* 35 */     put("bpmTask", bpmTask);
/* 36 */     this.bpmTask = bpmTask;
/*    */   }
/*    */   
/*    */   public String getCurrentOrgId() {
/* 40 */     return this.currentOrgId;
/*    */   }
/*    */   
/*    */   public void setCurrentOrgId(String currentOrgId) {
/* 44 */     this.currentOrgId = currentOrgId;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/session/impl/DefaultBpmTaskPluginSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */