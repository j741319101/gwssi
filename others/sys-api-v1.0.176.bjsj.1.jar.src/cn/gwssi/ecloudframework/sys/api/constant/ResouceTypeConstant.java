/*    */ package cn.gwssi.ecloudframework.sys.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ResouceTypeConstant
/*    */ {
/*  7 */   MENU("menu", "菜单"),
/*  8 */   LINK("link", "链接"),
/*  9 */   BUTTON("button", "按钮");
/*    */   
/*    */   private String key;
/*    */   
/*    */   private String label;
/*    */   
/*    */   ResouceTypeConstant(String key, String label) {
/* 16 */     setKey(key);
/* 17 */     this.label = label;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String label() {
/* 23 */     return this.label;
/*    */   }
/*    */   
/*    */   public String getLabel() {
/* 27 */     return this.label;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLabel(String label) {
/* 32 */     this.label = label;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 37 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setKey(String key) {
/* 42 */     this.key = key;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/constant/ResouceTypeConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */