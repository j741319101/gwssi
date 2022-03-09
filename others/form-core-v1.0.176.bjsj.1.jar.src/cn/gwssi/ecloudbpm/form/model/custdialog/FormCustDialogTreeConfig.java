/*     */ package cn.gwssi.ecloudbpm.form.model.custdialog;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.form.api.model.IFormCustDialogTreeConfig;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormCustDialogTreeConfig
/*     */   implements Serializable, IFormCustDialogTreeConfig
/*     */ {
/*     */   @NotEmpty
/*     */   private String id;
/*     */   @NotEmpty
/*     */   private String pid;
/*     */   private String pidInitVal;
/*     */   private boolean pidInitValScript;
/*     */   @NotEmpty
/*     */   private String showColumn;
/*     */   private Boolean showFilter;
/*     */   private Boolean checkStrictly;
/*     */   
/*     */   public String getId() {
/*  52 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  56 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPid() {
/*  61 */     return this.pid;
/*     */   }
/*     */   
/*     */   public void setPid(String pid) {
/*  65 */     this.pid = pid;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPidInitVal() {
/*  70 */     return this.pidInitVal;
/*     */   }
/*     */   
/*     */   public void setPidInitVal(String pidInitVal) {
/*  74 */     this.pidInitVal = pidInitVal;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPidInitValScript() {
/*  79 */     return this.pidInitValScript;
/*     */   }
/*     */   
/*     */   public void setPidInitValScript(boolean pidInitValScript) {
/*  83 */     this.pidInitValScript = pidInitValScript;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getShowColumn() {
/*  88 */     return this.showColumn;
/*     */   }
/*     */   
/*     */   public void setShowColumn(String showColumn) {
/*  92 */     this.showColumn = showColumn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean getShowFilter() {
/*  97 */     return this.showFilter;
/*     */   }
/*     */   
/*     */   public void setShowFilter(Boolean showFilter) {
/* 101 */     this.showFilter = showFilter;
/*     */   }
/*     */   
/*     */   public Boolean getCheckStrictly() {
/* 105 */     return this.checkStrictly;
/*     */   }
/*     */   
/*     */   public void setCheckStrictly(Boolean checkStrictly) {
/* 109 */     this.checkStrictly = checkStrictly;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/custdialog/FormCustDialogTreeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */