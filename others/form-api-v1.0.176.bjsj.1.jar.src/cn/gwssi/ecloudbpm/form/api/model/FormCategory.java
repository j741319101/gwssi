/*    */ package cn.gwssi.ecloudbpm.form.api.model;
/*    */ 
/*    */ public enum FormCategory {
/*  4 */   INNER("inner"),
/*  5 */   FRAME("frame");
/*    */   
/*    */   private final String value;
/*    */   
/*    */   FormCategory(String v) {
/* 10 */     this.value = v;
/*    */   }
/*    */   
/*    */   public String value() {
/* 14 */     return this.value;
/*    */   }
/*    */   
/*    */   public static FormCategory fromValue(String v) {
/* 18 */     for (FormCategory c : values()) {
/* 19 */       if (c.value.equals(v)) {
/* 20 */         return c;
/*    */       }
/*    */     } 
/* 23 */     throw new IllegalArgumentException(v);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-api/v1.0.176.bjsj.1/form-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/api/model/FormCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */