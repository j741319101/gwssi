/*    */ package com.dstz.bpm.api.model.form;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.api.model.FormCategory;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultForm
/*    */   implements BpmForm
/*    */ {
/*    */   private String name;
/*    */   private FormCategory type;
/*    */   private String formValue;
/*    */   private String formHandler;
/*    */   private String formHtml;
/*    */   private String nodeId;
/*    */   
/*    */   public String getFormHandler() {
/* 19 */     if (FormCategory.INNER == this.type) {
/* 20 */       return null;
/*    */     }
/* 22 */     return this.formHandler;
/*    */   }
/*    */   
/*    */   public void setFormHandler(String formHandler) {
/* 26 */     this.formHandler = formHandler;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 30 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 34 */     this.name = name;
/*    */   }
/*    */   
/*    */   public FormCategory getType() {
/* 38 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(FormCategory type) {
/* 42 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getFormValue() {
/* 46 */     return this.formValue;
/*    */   }
/*    */   
/*    */   public void setFormValue(String formValue) {
/* 50 */     this.formValue = formValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setId(String id) {}
/*    */ 
/*    */   
/*    */   public String getId() {
/* 58 */     return "";
/*    */   }
/*    */   
/*    */   public boolean isFormEmpty() {
/* 62 */     boolean isEmpty = StringUtil.isEmpty(this.formValue);
/* 63 */     return isEmpty;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFormHtml() {
/* 68 */     return this.formHtml;
/*    */   }
/*    */   
/*    */   public void setFormHtml(String formHtml) {
/* 72 */     this.formHtml = formHtml;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/form/DefaultForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */