/*    */ package com.dstz.bpm.api.engine.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum LogicType
/*    */ {
/* 10 */   AND("and", "与"),
/* 11 */   OR("or", "或"),
/* 12 */   EXCLUDE("exclude", "非");
/*    */ 
/*    */   
/* 15 */   private String key = "";
/*    */   
/* 17 */   private String value = "";
/*    */ 
/*    */   
/*    */   LogicType(String key, String value) {
/* 21 */     this.key = key;
/* 22 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 27 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 31 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 35 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 39 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LogicType fromKey(String key) {
/* 54 */     for (LogicType c : values()) {
/* 55 */       if (c.getKey().equalsIgnoreCase(key))
/* 56 */         return c; 
/*    */     } 
/* 58 */     throw new IllegalArgumentException(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/constant/LogicType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */