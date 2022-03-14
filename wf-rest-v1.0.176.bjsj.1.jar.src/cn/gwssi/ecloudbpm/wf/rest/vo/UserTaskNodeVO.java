/*    */ package com.dstz.bpm.rest.vo;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserTaskNodeVO
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -587907919367773210L;
/*    */   private String nodeId;
/*    */   private String nodeName;
/*    */   
/*    */   public UserTaskNodeVO() {}
/*    */   
/*    */   public UserTaskNodeVO(String nodeId, String nodeName) {
/* 27 */     this.nodeId = nodeId;
/* 28 */     this.nodeName = nodeName;
/*    */   }
/*    */   
/*    */   public String getNodeId() {
/* 32 */     return this.nodeId;
/*    */   }
/*    */   
/*    */   public void setNodeId(String nodeId) {
/* 36 */     this.nodeId = nodeId;
/*    */   }
/*    */   
/*    */   public String getNodeName() {
/* 40 */     return this.nodeName;
/*    */   }
/*    */   
/*    */   public void setNodeName(String nodeName) {
/* 44 */     this.nodeName = nodeName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return "UserTaskNodeVO{nodeId='" + this.nodeId + '\'' + ", nodeName='" + this.nodeName + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/vo/UserTaskNodeVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */