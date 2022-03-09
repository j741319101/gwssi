/*    */ package cn.gwssi.ecloudbpm.form.model;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class FormImportModel
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -700694295167942753L;
/*    */   private String formTypeTreeKey;
/*    */   private String formTypeKey;
/*    */   private FormDef formDef;
/*    */   private FormDef mobileFormDef;
/*    */   private FormCustomConf formCustomConf;
/*    */   private FormCustomConf mobileFormCustomConf;
/*    */   
/*    */   public String getFormTypeTreeKey() {
/* 38 */     return this.formTypeTreeKey;
/*    */   }
/*    */   
/*    */   public void setFormTypeTreeKey(String formTypeTreeKey) {
/* 42 */     this.formTypeTreeKey = formTypeTreeKey;
/*    */   }
/*    */   
/*    */   public String getFormTypeKey() {
/* 46 */     return this.formTypeKey;
/*    */   }
/*    */   
/*    */   public void setFormTypeKey(String formTypeKey) {
/* 50 */     this.formTypeKey = formTypeKey;
/*    */   }
/*    */   
/*    */   public FormDef getFormDef() {
/* 54 */     return this.formDef;
/*    */   }
/*    */   
/*    */   public void setFormDef(FormDef formDef) {
/* 58 */     this.formDef = formDef;
/*    */   }
/*    */   
/*    */   public FormDef getMobileFormDef() {
/* 62 */     return this.mobileFormDef;
/*    */   }
/*    */   
/*    */   public void setMobileFormDef(FormDef mobileFormDef) {
/* 66 */     this.mobileFormDef = mobileFormDef;
/*    */   }
/*    */   
/*    */   public FormCustomConf getFormCustomConf() {
/* 70 */     return this.formCustomConf;
/*    */   }
/*    */   
/*    */   public void setFormCustomConf(FormCustomConf formCustomConf) {
/* 74 */     this.formCustomConf = formCustomConf;
/*    */   }
/*    */   
/*    */   public FormCustomConf getMobileFormCustomConf() {
/* 78 */     return this.mobileFormCustomConf;
/*    */   }
/*    */   
/*    */   public void setMobileFormCustomConf(FormCustomConf mobileFormCustomConf) {
/* 82 */     this.mobileFormCustomConf = mobileFormCustomConf;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormImportModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */