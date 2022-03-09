/*    */ package cn.gwssi.ecloudbpm.form.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum FormCustDialogObjType
/*    */ {
/* 12 */   TABLE("table", "表"), VIEW("view", "视图");
/*    */ 
/*    */   
/*    */   private String desc;
/*    */ 
/*    */   
/*    */   private String key;
/*    */ 
/*    */ 
/*    */   
/*    */   FormCustDialogObjType(String key, String desc) {
/* 23 */     this.key = key;
/* 24 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 28 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 32 */     return this.desc;
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
/* 44 */     return this.key.equals(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-api/v1.0.176.bjsj.1/form-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/api/constant/FormCustDialogObjType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */