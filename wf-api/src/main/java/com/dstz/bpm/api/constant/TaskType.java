/*    */ package com.dstz.bpm.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum TaskType
/*    */ {
/*  7 */   NORMAL("NORMAL", "普通任务"),
/*  8 */   SIGN("SIGN", "会签任务"),
/*  9 */   SIGN_SOURCE("SIGN_SOURCE", "会签任务_源"),
/* 10 */   ADD_SIGN("ADD_SIGN", "加签任务"),
/* 11 */   ADDDO("ADD_DO", "加办任务"),
/* 12 */   SUBFLOW("SUBFLOW", "子流程任务"),
/* 13 */   AGENT("AGENT", "代理任务"),
/* 14 */   DELIVERTO("DELIVERTO", "转办任务"),
/* 15 */   TRANSFORMING("TRANSFORMING", "事项任务"),
/* 16 */   LEADER("LEADER", "领导任务"),
/* 17 */   SUPERVISE("SUPERVISE", "监管任务");
/*    */ 
/*    */   
/* 20 */   private String key = "";
/*    */   
/* 22 */   private String value = "";
/*    */ 
/*    */   
/*    */   TaskType(String key, String value) {
/* 26 */     this.key = key;
/* 27 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 32 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 36 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 40 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 44 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equalsWithKey(String key) {
/* 61 */     return this.key.equals(key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TaskType fromKey(String key) {
/* 71 */     for (TaskType c : values()) {
/* 72 */       if (c.getKey().equalsIgnoreCase(key))
/* 73 */         return c; 
/*    */     } 
/* 75 */     throw new IllegalArgumentException(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/TaskType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */