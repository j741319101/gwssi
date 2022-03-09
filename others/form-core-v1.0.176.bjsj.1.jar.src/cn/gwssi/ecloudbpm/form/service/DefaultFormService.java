/*    */ package cn.gwssi.ecloudbpm.form.service;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.api.model.IFormDef;
/*    */ import cn.gwssi.ecloudbpm.form.api.service.FormService;
/*    */ import cn.gwssi.ecloudbpm.form.manager.FormDefManager;
/*    */ import cn.gwssi.ecloudbpm.form.model.FormDef;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ @Service("formService")
/*    */ public class DefaultFormService
/*    */   implements FormService
/*    */ {
/*    */   @Resource
/*    */   private FormDefManager formDefManager;
/*    */   
/*    */   public IFormDef getByFormKey(String formKey) {
/* 22 */     return (IFormDef)this.formDefManager.getByKey(formKey);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IFormDef getByFormId(String formId) {
/* 29 */     return (IFormDef)this.formDefManager.get(formId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getFormExportXml(Set<String> formKeys) {
/* 35 */     List<String> id = new ArrayList<>();
/* 36 */     for (String formKey : formKeys) {
/* 37 */       FormDef form = this.formDefManager.getByKey(formKey);
/* 38 */       id.add(form.getId());
/*    */     } 
/*    */ 
/*    */     
/* 42 */     return null;
/*    */   }
/*    */   
/*    */   public void importForm(String formXmlStr) {}
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/service/DefaultFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */