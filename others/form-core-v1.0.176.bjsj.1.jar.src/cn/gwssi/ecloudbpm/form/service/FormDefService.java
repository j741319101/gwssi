/*    */ package cn.gwssi.ecloudbpm.form.service;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.api.model.IFormDef;
/*    */ import cn.gwssi.ecloudbpm.form.api.service.IFormDefService;
/*    */ import cn.gwssi.ecloudbpm.form.manager.FormDefManager;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class FormDefService
/*    */   implements IFormDefService
/*    */ {
/*    */   @Autowired
/*    */   FormDefManager formDefManager;
/*    */   
/*    */   public IFormDef getByKey(String key) {
/* 17 */     return (IFormDef)this.formDefManager.getByKey(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/service/FormDefService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */