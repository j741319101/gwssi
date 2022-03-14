/*    */ package com.dstz.org.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CollectTypeConstant
/*    */ {
/* 13 */   COMMON_WORDS("commonWords", "常用语"),
/* 14 */   COLLECT("collect", "收藏菜单");
/*    */   private String key;
/*    */   private String label;
/*    */   
/*    */   CollectTypeConstant(String key, String label) {
/* 19 */     this.key = key;
/* 20 */     this.label = label;
/*    */   }
/*    */   
/*    */   public String key() {
/* 24 */     return this.key;
/*    */   }
/*    */   
/*    */   public String label() {
/* 28 */     return this.label;
/*    */   }
/*    */   
/*    */   public static CollectTypeConstant fromStr(String key) {
/* 32 */     for (CollectTypeConstant e : values()) {
/* 33 */       if (key.equals(e.key)) {
/* 34 */         return e;
/*    */       }
/*    */     } 
/* 37 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/constant/CollectTypeConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */