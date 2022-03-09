/*    */ package cn.gwssi.ecloudbpm.form.api.constant;
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
/*    */ public enum FormTemplateType
/*    */ {
/* 16 */   MAIN("main", "主表模板"),
/*    */ 
/*    */ 
/*    */   
/* 20 */   SUB_TABLE("subTable", "子表模板"),
/*    */   
/* 22 */   MAIN_OVERALLARRANGEMENT("mainFormOverallArrangement", "主表[布局版]"),
/*    */   
/* 24 */   SUB_OVERALLARRANGEMENT("subTableFormOverallArrangement", "子表[布局版]");
/*    */ 
/*    */ 
/*    */   
/*    */   private String key;
/*    */ 
/*    */   
/*    */   private String desc;
/*    */ 
/*    */ 
/*    */   
/*    */   FormTemplateType(String key, String desc) {
/* 36 */     this.key = key;
/* 37 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 41 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 45 */     return this.desc;
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
/* 57 */     return this.key.equals(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-api/v1.0.176.bjsj.1/form-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/api/constant/FormTemplateType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */