/*    */ package cn.gwssi.ecloudframework.sys.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SysTreeTypeConstants
/*    */ {
/*  9 */   FLOW("flow", "流程分类"),
/* 10 */   DICT("dict", "数据字典");
/*    */   
/*    */   private String key;
/*    */   private String label;
/*    */   
/*    */   SysTreeTypeConstants(String key, String label) {
/* 16 */     this.key = key;
/* 17 */     this.label = label;
/*    */   }
/*    */   public String key() {
/* 20 */     return this.key;
/*    */   }
/*    */   public String label() {
/* 23 */     return this.label;
/*    */   }
/*    */   
/*    */   public static SysTreeTypeConstants getByKey(String key) {
/* 27 */     for (SysTreeTypeConstants rights : values()) {
/* 28 */       if (rights.key.equals(key)) {
/* 29 */         return rights;
/*    */       }
/*    */     } 
/* 32 */     throw new RuntimeException(String.format(" key [%s] 对应RightsObjectConstants 不存在的权限常亮定义，请核查！", new Object[] { key }));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/constant/SysTreeTypeConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */