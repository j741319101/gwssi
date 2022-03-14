/*    */ package com.dstz.base.api.executor;
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
/*    */ public enum ExecutorType
/*    */ {
/* 16 */   NECESSARY("necessary", "必要性执行器"),
/*    */ 
/*    */ 
/*    */   
/* 20 */   UNNECESSARY("unnecessary", "非必要性执行器");
/*    */   
/*    */   private String key;
/*    */   private String desc;
/*    */   
/*    */   ExecutorType(String key, String desc) {
/* 26 */     this.key = key;
/* 27 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 31 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 35 */     return this.desc;
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
/* 47 */     return this.key.equals(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/executor/ExecutorType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */