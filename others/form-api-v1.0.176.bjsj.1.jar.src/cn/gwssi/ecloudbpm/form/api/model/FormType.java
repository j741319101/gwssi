/*    */ package cn.gwssi.ecloudbpm.form.api.model;
/*    */ 
/*    */ public enum FormType {
/*  4 */   PC("pc"),
/*  5 */   PC_VUE("pc_vue"),
/*  6 */   MOBILE("mobile"),
/*  7 */   PC_IVIEW("pc_iview");
/*    */   
/*    */   private final String value;
/*    */   
/*    */   FormType(String v) {
/* 12 */     this.value = v;
/*    */   }
/*    */   
/*    */   public String value() {
/* 16 */     return this.value;
/*    */   }
/*    */   
/*    */   public static FormType fromValue(String v) {
/* 20 */     for (FormType c : values()) {
/* 21 */       if (c.value.equals(v)) {
/* 22 */         return c;
/*    */       }
/*    */     } 
/* 25 */     throw new IllegalArgumentException(v);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-api/v1.0.176.bjsj.1/form-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/api/model/FormType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */