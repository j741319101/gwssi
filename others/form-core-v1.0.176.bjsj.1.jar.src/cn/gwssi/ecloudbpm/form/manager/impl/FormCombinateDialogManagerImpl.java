/*    */ package cn.gwssi.ecloudbpm.form.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.dao.FormCombinateDialogDao;
/*    */ import cn.gwssi.ecloudbpm.form.manager.FormCombinateDialogManager;
/*    */ import cn.gwssi.ecloudbpm.form.model.FormCombinateDialog;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*    */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("combinateDialogManager")
/*    */ public class FormCombinateDialogManagerImpl
/*    */   extends BaseManager<String, FormCombinateDialog>
/*    */   implements FormCombinateDialogManager
/*    */ {
/*    */   @Resource
/*    */   FormCombinateDialogDao combinateDialogDao;
/*    */   
/*    */   public FormCombinateDialog getByAlias(String alias) {
/* 29 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 30 */     defaultQueryFilter.addFilter("alias_", alias, QueryOP.EQUAL);
/* 31 */     List<FormCombinateDialog> combinateDialogs = query((QueryFilter)defaultQueryFilter);
/* 32 */     if (combinateDialogs == null || combinateDialogs.isEmpty())
/* 33 */       return null; 
/* 34 */     return combinateDialogs.get(0);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/impl/FormCombinateDialogManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */