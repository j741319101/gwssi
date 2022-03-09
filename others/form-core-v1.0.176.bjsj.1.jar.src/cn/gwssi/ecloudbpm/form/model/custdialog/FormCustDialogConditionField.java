/*     */ package cn.gwssi.ecloudbpm.form.model.custdialog;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.form.api.model.IFormCustDialogConditionField;
/*     */ import java.io.Serializable;
/*     */ import org.hibernate.validator.constraints.NotEmpty;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormCustDialogConditionField
/*     */   implements Serializable, IFormCustDialogConditionField
/*     */ {
/*     */   @NotEmpty
/*     */   private String columnName;
/*     */   @NotEmpty
/*     */   private String dbType;
/*     */   @NotEmpty
/*     */   private String showName;
/*     */   @NotEmpty
/*     */   private String condition;
/*     */   @NotEmpty
/*     */   private String valueSource;
/*     */   @NotEmpty
/*     */   private Value value;
/*     */   
/*     */   public String getColumnName() {
/*  50 */     return this.columnName;
/*     */   }
/*     */   
/*     */   public void setColumnName(String columnName) {
/*  54 */     this.columnName = columnName;
/*     */   }
/*     */   
/*     */   public String getShowName() {
/*  58 */     return this.showName;
/*     */   }
/*     */   
/*     */   public void setShowName(String showName) {
/*  62 */     this.showName = showName;
/*     */   }
/*     */   
/*     */   public String getCondition() {
/*  66 */     return this.condition;
/*     */   }
/*     */   
/*     */   public void setCondition(String condition) {
/*  70 */     this.condition = condition;
/*     */   }
/*     */   
/*     */   public String getValueSource() {
/*  74 */     return this.valueSource;
/*     */   }
/*     */   
/*     */   public void setValueSource(String valueSource) {
/*  78 */     this.valueSource = valueSource;
/*     */   }
/*     */   
/*     */   public String getDbType() {
/*  82 */     return this.dbType;
/*     */   }
/*     */   
/*     */   public void setDbType(String dbType) {
/*  86 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public Value getValue() {
/*  90 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(Value value) {
/*  94 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Value
/*     */     extends IFormCustDialogConditionField.ConditionValue
/*     */     implements Serializable
/*     */   {
/*     */     private String ctrlType;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String ctrlKey;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String ctrlReturn;
/*     */ 
/*     */ 
/*     */     
/*     */     private String text;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getCtrlType() {
/* 125 */       return this.ctrlType;
/*     */     }
/*     */     
/*     */     public void setCtrlType(String ctrlType) {
/* 129 */       this.ctrlType = ctrlType;
/*     */     }
/*     */     
/*     */     public String getCtrlKey() {
/* 133 */       return this.ctrlKey;
/*     */     }
/*     */     
/*     */     public void setCtrlKey(String ctrlKey) {
/* 137 */       this.ctrlKey = ctrlKey;
/*     */     }
/*     */     
/*     */     public String getCtrlReturn() {
/* 141 */       return this.ctrlReturn;
/*     */     }
/*     */     
/*     */     public void setCtrlReturn(String ctrlReturn) {
/* 145 */       this.ctrlReturn = ctrlReturn;
/*     */     }
/*     */     
/*     */     public String getText() {
/* 149 */       return this.text;
/*     */     }
/*     */     
/*     */     public void setText(String text) {
/* 153 */       this.text = text;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/custdialog/FormCustDialogConditionField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */