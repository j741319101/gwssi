/*    */ package com.dstz.sys.api.constant;
/*    */ 
/*    */ public enum RightsObjectConstants
/*    */ {
/*  5 */   FLOW("FLOW", "流程定义"),
/*  6 */   WORKBENCH("WORKBENCH", "工作台");
/*    */   
/*    */   private String key;
/*    */   private String label;
/*    */   
/*    */   RightsObjectConstants(String key, String label) {
/* 12 */     this.key = key;
/* 13 */     this.label = label;
/*    */   }
/*    */   public String key() {
/* 16 */     return this.key;
/*    */   }
/*    */   public String label() {
/* 19 */     return this.label;
/*    */   }
/*    */   
/*    */   public static RightsObjectConstants getByKey(String key) {
/* 23 */     for (RightsObjectConstants rights : values()) {
/* 24 */       if (rights.key.equals(key)) {
/* 25 */         return rights;
/*    */       }
/*    */     } 
/* 28 */     throw new RuntimeException(String.format(" key [%s] 对应RightsObjectConstants 不存在的权限常亮定义，请核查！", new Object[] { key }));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/constant/RightsObjectConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */