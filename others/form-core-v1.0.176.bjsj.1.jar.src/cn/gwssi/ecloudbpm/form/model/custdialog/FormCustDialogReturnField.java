/*    */ package cn.gwssi.ecloudbpm.form.model.custdialog;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.api.model.IFormCustDialogReturnField;
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
/*    */ public class FormCustDialogReturnField
/*    */   implements Serializable, IFormCustDialogReturnField
/*    */ {
/*    */   @NotEmpty
/*    */   private String columnName;
/*    */   @NotEmpty
/*    */   private String returnName;
/*    */   
/*    */   public String getColumnName() {
/* 30 */     return this.columnName;
/*    */   }
/*    */   
/*    */   public void setColumnName(String columnName) {
/* 34 */     this.columnName = columnName;
/*    */   }
/*    */   
/*    */   public String getReturnName() {
/* 38 */     return this.returnName;
/*    */   }
/*    */   
/*    */   public void setReturnName(String returnName) {
/* 42 */     this.returnName = returnName;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/custdialog/FormCustDialogReturnField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */