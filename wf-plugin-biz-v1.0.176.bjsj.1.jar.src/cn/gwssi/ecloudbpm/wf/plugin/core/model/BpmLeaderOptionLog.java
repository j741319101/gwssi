/*    */ package cn.gwssi.ecloudbpm.wf.plugin.core.model;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*    */ 
/*    */ 
/*    */ public class BpmLeaderOptionLog
/*    */   extends BaseModel
/*    */ {
/*    */   private String leaderId;
/*    */   private String leaderName;
/*    */   private String secretaryId;
/*    */   private String secretaryName;
/*    */   private String taskId;
/*    */   private String instId;
/*    */   private String option;
/*    */   private String type;
/*    */   
/*    */   public String getLeaderId() {
/* 19 */     return this.leaderId;
/*    */   }
/*    */   
/*    */   public String getLeaderName() {
/* 23 */     return this.leaderName;
/*    */   }
/*    */   
/*    */   public String getInstId() {
/* 27 */     return this.instId;
/*    */   }
/*    */   
/*    */   public void setInstId(String instId) {
/* 31 */     this.instId = instId;
/*    */   }
/*    */   
/*    */   public void setLeaderName(String leaderName) {
/* 35 */     this.leaderName = leaderName;
/*    */   }
/*    */   
/*    */   public String getSecretaryName() {
/* 39 */     return this.secretaryName;
/*    */   }
/*    */   
/*    */   public void setSecretaryName(String secretaryName) {
/* 43 */     this.secretaryName = secretaryName;
/*    */   }
/*    */   
/*    */   public void setLeaderId(String leaderId) {
/* 47 */     this.leaderId = leaderId;
/*    */   }
/*    */   
/*    */   public String getSecretaryId() {
/* 51 */     return this.secretaryId;
/*    */   }
/*    */   
/*    */   public void setSecretaryId(String secretaryId) {
/* 55 */     this.secretaryId = secretaryId;
/*    */   }
/*    */   
/*    */   public String getTaskId() {
/* 59 */     return this.taskId;
/*    */   }
/*    */   
/*    */   public void setTaskId(String taskId) {
/* 63 */     this.taskId = taskId;
/*    */   }
/*    */   
/*    */   public String getOption() {
/* 67 */     return this.option;
/*    */   }
/*    */   
/*    */   public void setOption(String option) {
/* 71 */     this.option = option;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 75 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 79 */     this.type = type;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/model/BpmLeaderOptionLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */