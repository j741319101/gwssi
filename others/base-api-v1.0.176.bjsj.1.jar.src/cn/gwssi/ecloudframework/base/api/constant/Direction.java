/*    */ package com.dstz.base.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Direction
/*    */ {
/*  9 */   ASC("asc", "升序"), DESC("desc", "降序");
/*    */ 
/*    */   
/*    */   private String desc;
/*    */ 
/*    */   
/*    */   private String key;
/*    */ 
/*    */ 
/*    */   
/*    */   Direction(String key, String desc) {
/* 20 */     this.key = key;
/* 21 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 25 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 29 */     return this.desc;
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
/* 41 */     return this.key.equals(key);
/*    */   }
/*    */   
/*    */   public static Direction fromString(String value) {
/*    */     try {
/* 46 */       return valueOf(value.toUpperCase());
/* 47 */     } catch (Exception e) {
/* 48 */       return ASC;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/constant/Direction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */