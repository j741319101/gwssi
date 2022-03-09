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
/*    */ public enum FormCustDialogConditionFieldValueSource
/*    */ {
/* 16 */   PARAM("param", "参数传入", new String[] { FormCustDialogStyle.LIST.getKey(), FormCustDialogStyle.TREE.getKey()
/*    */ 
/*    */     
/*    */     }),
/* 20 */   FIXED_VALUE("fixedValue", "固定值", new String[] { FormCustDialogStyle.LIST.getKey(), FormCustDialogStyle.TREE.getKey()
/*    */ 
/*    */     
/*    */     }),
/* 24 */   SCRIPT("script", "脚本", new String[] { FormCustDialogStyle.LIST.getKey(), FormCustDialogStyle.TREE.getKey() });
/*    */ 
/*    */ 
/*    */   
/*    */   private String key;
/*    */ 
/*    */ 
/*    */   
/*    */   private String desc;
/*    */ 
/*    */   
/*    */   private String[] supports;
/*    */ 
/*    */ 
/*    */   
/*    */   FormCustDialogConditionFieldValueSource(String key, String desc, String[] supports) {
/* 40 */     this.key = key;
/* 41 */     this.desc = desc;
/* 42 */     this.supports = supports;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 46 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 50 */     return this.desc;
/*    */   }
/*    */   
/*    */   public String[] getSupports() {
/* 54 */     return this.supports;
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
/* 66 */     return this.key.equals(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-api/v1.0.176.bjsj.1/form-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/api/constant/FormCustDialogConditionFieldValueSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */