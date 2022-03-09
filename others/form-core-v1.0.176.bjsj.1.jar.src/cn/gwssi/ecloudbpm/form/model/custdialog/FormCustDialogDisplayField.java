/*    */ package cn.gwssi.ecloudbpm.form.model.custdialog;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.api.model.IFormCustDialogDisplayField;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FormCustDialogDisplayField
/*    */   implements Serializable, IFormCustDialogDisplayField
/*    */ {
/*    */   @NotEmpty
/*    */   private String columnName;
/*    */   @NotEmpty
/*    */   private String showName;
/*    */   private String formatter;
/*    */   
/*    */   public String getColumnName() {
/* 34 */     return this.columnName;
/*    */   }
/*    */   
/*    */   public void setColumnName(String columnName) {
/* 38 */     this.columnName = columnName;
/*    */   }
/*    */   
/*    */   public String getShowName() {
/* 42 */     return this.showName;
/*    */   }
/*    */   
/*    */   public void setShowName(String showName) {
/* 46 */     this.showName = showName;
/*    */   }
/*    */   
/*    */   public String getFormatter() {
/* 50 */     return this.formatter;
/*    */   }
/*    */   
/*    */   public void setFormatter(String formatter) {
/* 54 */     this.formatter = formatter;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/custdialog/FormCustDialogDisplayField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */