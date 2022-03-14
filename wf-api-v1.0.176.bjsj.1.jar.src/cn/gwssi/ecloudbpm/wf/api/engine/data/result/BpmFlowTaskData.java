/*    */ package com.dstz.bpm.api.engine.data.result;
/*    */ 
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ 
/*    */ public class BpmFlowTaskData
/*    */   extends BpmFlowData {
/*    */   @ApiModelProperty("流程任务信息")
/*    */   private IBpmTask task;
/* 10 */   private Boolean isEndTask = Boolean.valueOf(false);
/* 11 */   private String javaScript = "";
/*    */   
/*    */   public IBpmTask getTask() {
/* 14 */     return this.task;
/*    */   }
/*    */   private String taskOrgId;
/*    */   public void setTask(IBpmTask task) {
/* 18 */     this.task = task;
/* 19 */     this.defId = task.getDefId();
/*    */   }
/*    */   
/*    */   public Boolean getEndTask() {
/* 23 */     return this.isEndTask;
/*    */   }
/*    */   
/*    */   public void setEndTask(Boolean endTask) {
/* 27 */     this.isEndTask = endTask;
/*    */   }
/*    */   
/*    */   public String getJavaScript() {
/* 31 */     return this.javaScript;
/*    */   }
/*    */   
/*    */   public void setJavaScript(String javaScript) {
/* 35 */     this.javaScript = javaScript;
/*    */   }
/*    */   
/*    */   public String getTaskOrgId() {
/* 39 */     return this.taskOrgId;
/*    */   }
/*    */   
/*    */   public void setTaskOrgId(String taskOrgId) {
/* 43 */     this.taskOrgId = taskOrgId;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/data/result/BpmFlowTaskData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */