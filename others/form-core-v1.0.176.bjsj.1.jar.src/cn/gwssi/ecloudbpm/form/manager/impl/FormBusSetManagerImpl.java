/*    */ package cn.gwssi.ecloudbpm.form.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.dao.FormBusSetDao;
/*    */ import cn.gwssi.ecloudbpm.form.manager.FormBusSetManager;
/*    */ import cn.gwssi.ecloudbpm.form.model.FormBusSet;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("formBusSetManager")
/*    */ public class FormBusSetManagerImpl
/*    */   extends BaseManager<String, FormBusSet>
/*    */   implements FormBusSetManager
/*    */ {
/*    */   @Resource
/*    */   FormBusSetDao formBusSetDao;
/*    */   
/*    */   public FormBusSet getByFormKey(String formKey) {
/* 24 */     return this.formBusSetDao.getByFormKey(formKey);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/impl/FormBusSetManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */