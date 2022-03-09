/*     */ package cn.gwssi.ecloudbpm.form.model;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ public class FormTemplate
/*     */   implements Serializable
/*     */ {
/*     */   private String id;
/*     */   private String key;
/*     */   private String name;
/*     */   private String desc;
/*     */   private String type;
/*     */   private String formType;
/*     */   private String html;
/*     */   private boolean editable;
/*     */   
/*     */   public String getId() {
/*  50 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  54 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  58 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  62 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  66 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  70 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/*  74 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/*  78 */     this.desc = desc;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  82 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  86 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getHtml() {
/*  90 */     return this.html;
/*     */   }
/*     */   
/*     */   public void setHtml(String html) {
/*  94 */     this.html = html;
/*     */   }
/*     */   
/*     */   public String getFormType() {
/*  98 */     return this.formType;
/*     */   }
/*     */   
/*     */   public void setFormType(String formType) {
/* 102 */     this.formType = formType;
/*     */   }
/*     */   
/*     */   public boolean isEditable() {
/* 106 */     return this.editable;
/*     */   }
/*     */   
/*     */   public void setEditable(boolean editable) {
/* 110 */     this.editable = editable;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */