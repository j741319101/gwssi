/*    */ package cn.gwssi.ecloudbpm.wf.core.vo;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskApprove;
/*    */ 
/*    */ public class BpmTaskApproveVO extends BpmTaskApprove {
/*    */   protected String approveStatus;
/*    */   protected String taskNames;
/*    */   private String taskUsers;
/*    */   private String nodeName;
/*    */   private String lastStatus;
/*    */   
/*    */   public String getTaskNames() {
/* 13 */     return this.taskNames;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTaskNames(String taskNames) {
/* 18 */     this.taskNames = taskNames;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTaskUsers() {
/* 23 */     return this.taskUsers;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTaskUsers(String taskUsers) {
/* 28 */     this.taskUsers = taskUsers;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getApproveStatus() {
/* 33 */     return this.approveStatus;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setApproveStatus(String approveStatus) {
/* 38 */     this.approveStatus = approveStatus;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNodeName() {
/* 43 */     return this.nodeName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setNodeName(String nodeName) {
/* 48 */     this.nodeName = nodeName;
/*    */   }
/*    */   
/*    */   public String getLastStatus() {
/* 52 */     return this.lastStatus;
/*    */   }
/*    */   
/*    */   public void setLastStatus(String lastStatus) {
/* 56 */     this.lastStatus = lastStatus;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/vo/BpmTaskApproveVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */