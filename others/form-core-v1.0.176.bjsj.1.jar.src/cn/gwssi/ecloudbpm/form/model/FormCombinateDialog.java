/*     */ package cn.gwssi.ecloudbpm.form.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormCustDialogManager;
/*     */ import cn.gwssi.ecloudframework.base.api.model.IDModel;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormCombinateDialog
/*     */   implements IDModel
/*     */ {
/*     */   protected String id;
/*     */   protected String name;
/*     */   protected String alias;
/*     */   protected Integer width;
/*     */   protected Integer height;
/*     */   protected String treeDialogId;
/*     */   protected String treeDialogName;
/*     */   protected String listDialogId;
/*     */   protected String listDialogName;
/*     */   protected String field;
/*     */   private FormCustDialog treeDialog;
/*     */   private FormCustDialog listDialog;
/*     */   
/*     */   public void setId(String id) {
/*  29 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  38 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  42 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  51 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/*  55 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlias() {
/*  64 */     return this.alias;
/*     */   }
/*     */   
/*     */   public void setWidth(Integer width) {
/*  68 */     this.width = width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getWidth() {
/*  77 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setHeight(Integer height) {
/*  81 */     this.height = height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getHeight() {
/*  90 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setTreeDialogId(String treeDialogId) {
/*  94 */     this.treeDialogId = treeDialogId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTreeDialogId() {
/* 103 */     return this.treeDialogId;
/*     */   }
/*     */   
/*     */   public void setTreeDialogName(String treeDialogName) {
/* 107 */     this.treeDialogName = treeDialogName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTreeDialogName() {
/* 116 */     return this.treeDialogName;
/*     */   }
/*     */   
/*     */   public void setListDialogId(String listDialogId) {
/* 120 */     this.listDialogId = listDialogId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getListDialogId() {
/* 129 */     return this.listDialogId;
/*     */   }
/*     */   
/*     */   public void setListDialogName(String listDialogName) {
/* 133 */     this.listDialogName = listDialogName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getListDialogName() {
/* 142 */     return this.listDialogName;
/*     */   }
/*     */   
/*     */   public void setField(String field) {
/* 146 */     this.field = field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getField() {
/* 155 */     return this.field;
/*     */   }
/*     */   
/*     */   public FormCustDialog getTreeDialog() {
/* 159 */     if (StringUtil.isEmpty(this.treeDialogId)) {
/* 160 */       return null;
/*     */     }
/* 162 */     this.treeDialog = (FormCustDialog)((FormCustDialogManager)AppUtil.getBean(FormCustDialogManager.class)).get(this.treeDialogId);
/* 163 */     return this.treeDialog;
/*     */   }
/*     */   
/*     */   public FormCustDialog getListDialog() {
/* 167 */     if (StringUtil.isEmpty(this.listDialogId)) {
/* 168 */       return null;
/*     */     }
/* 170 */     this.listDialog = (FormCustDialog)((FormCustDialogManager)AppUtil.getBean(FormCustDialogManager.class)).get(this.listDialogId);
/* 171 */     return this.listDialog;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormCombinateDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */