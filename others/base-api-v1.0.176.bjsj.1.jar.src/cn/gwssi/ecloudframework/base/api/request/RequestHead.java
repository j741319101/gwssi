/*    */ package com.dstz.base.api.request;
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
/*    */ public class RequestHead
/*    */ {
/*    */   private String sourceSystem;
/*    */   private String operator;
/*    */   private String memo;
/*    */   private String ip;
/*    */   private Boolean isEncryptData;
/*    */   private String secreKey;
/*    */   private String traceId;
/*    */   
/*    */   public String getSourceSystem() {
/* 23 */     return this.sourceSystem;
/*    */   }
/*    */   
/*    */   public void setSourceSystem(String sourceSystem) {
/* 27 */     this.sourceSystem = sourceSystem;
/*    */   }
/*    */   
/*    */   public String getOperator() {
/* 31 */     return this.operator;
/*    */   }
/*    */   
/*    */   public void setOperator(String operator) {
/* 35 */     this.operator = operator;
/*    */   }
/*    */   
/*    */   public String getMemo() {
/* 39 */     return this.memo;
/*    */   }
/*    */   
/*    */   public void setMemo(String memo) {
/* 43 */     this.memo = memo;
/*    */   }
/*    */   
/*    */   public String getIp() {
/* 47 */     return this.ip;
/*    */   }
/*    */   
/*    */   public void setIp(String ip) {
/* 51 */     this.ip = ip;
/*    */   }
/*    */   
/*    */   public Boolean getIsEncryptData() {
/* 55 */     return this.isEncryptData;
/*    */   }
/*    */   
/*    */   public void setIsEncryptData(Boolean isEncryptData) {
/* 59 */     this.isEncryptData = isEncryptData;
/*    */   }
/*    */   
/*    */   public String getSecreKey() {
/* 63 */     return this.secreKey;
/*    */   }
/*    */   
/*    */   public void setSecreKey(String secreKey) {
/* 67 */     this.secreKey = secreKey;
/*    */   }
/*    */   
/*    */   public String getTraceId() {
/* 71 */     return this.traceId;
/*    */   }
/*    */   
/*    */   public void setTraceId(String traceId) {
/* 75 */     this.traceId = traceId;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/request/RequestHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */