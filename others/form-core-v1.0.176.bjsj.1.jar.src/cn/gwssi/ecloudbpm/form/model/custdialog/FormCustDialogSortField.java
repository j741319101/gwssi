/*    */ package cn.gwssi.ecloudbpm.form.model.custdialog;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.api.model.IFormCustDialogSortField;
/*    */ import java.io.Serializable;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
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
/*    */ public class FormCustDialogSortField
/*    */   implements Serializable, IFormCustDialogSortField
/*    */ {
/*    */   @NotEmpty
/*    */   private String columnName;
/*    */   @NotEmpty
/*    */   private String sortType;
/*    */   
/*    */   public String getColumnName() {
/* 30 */     return this.columnName;
/*    */   }
/*    */   
/*    */   public void setColumnName(String columnName) {
/* 34 */     this.columnName = columnName;
/*    */   }
/*    */   
/*    */   public String getSortType() {
/* 38 */     return this.sortType;
/*    */   }
/*    */   
/*    */   public void setSortType(String sortType) {
/* 42 */     this.sortType = sortType;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/custdialog/FormCustDialogSortField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */