/*     */ package com.dstz.bpm.api.model.def;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.hibernate.validator.constraints.NotBlank;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BpmDefProperties
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3157546646728816168L;
/*     */   @NotBlank
/*  19 */   protected String subjectRule = "{发起人:startorName}在{发起时间:startDate}发起{流程标题:title}";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  24 */   protected String description = "";
/*     */ 
/*     */   
/*     */   protected boolean allowExecutorEmpty = true;
/*     */ 
/*     */   
/*  30 */   protected Integer supportMobile = Integer.valueOf(0);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean logSubmitData = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean requiredOpinion = true;
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean allowRecall = true;
/*     */ 
/*     */ 
/*     */   
/*     */   @NotBlank
/*  49 */   protected String status = "draft";
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean officialDocumentEnable = false;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String officialDocumentTemplate;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String officialPrintTemplate;
/*     */ 
/*     */   
/*     */   protected String labels;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubjectRule() {
/*  69 */     return this.subjectRule;
/*     */   }
/*     */   
/*     */   public void setSubjectRule(String subjectRule) {
/*  73 */     this.subjectRule = subjectRule;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  77 */     if (this.description == null) return ""; 
/*  78 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDescription(String description) {
/*  83 */     this.description = description;
/*     */   }
/*     */   
/*     */   public boolean isAllowExecutorEmpty() {
/*  87 */     return this.allowExecutorEmpty;
/*     */   }
/*     */   
/*     */   public void setAllowExecutorEmpty(boolean allowExecutorEmpty) {
/*  91 */     this.allowExecutorEmpty = allowExecutorEmpty;
/*     */   }
/*     */   
/*     */   public void setSupportMobile(Integer supportMobile) {
/*  95 */     this.supportMobile = supportMobile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getSupportMobile() {
/* 103 */     return this.supportMobile;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 107 */     return this.status;
/*     */   }
/*     */   
/*     */   public boolean isLogSubmitData() {
/* 111 */     return this.logSubmitData;
/*     */   }
/*     */   
/*     */   public void setLogSubmitData(boolean logSubmitData) {
/* 115 */     this.logSubmitData = logSubmitData;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 119 */     this.status = status;
/*     */   }
/*     */   
/*     */   public boolean isOfficialDocumentEnable() {
/* 123 */     return this.officialDocumentEnable;
/*     */   }
/*     */   
/*     */   public void setOfficialDocumentEnable(boolean officialDocumentEnable) {
/* 127 */     this.officialDocumentEnable = officialDocumentEnable;
/*     */   }
/*     */   
/*     */   public String getOfficialDocumentTemplate() {
/* 131 */     return this.officialDocumentTemplate;
/*     */   }
/*     */   
/*     */   public void setOfficialDocumentTemplate(String officialDocumentTemplate) {
/* 135 */     this.officialDocumentTemplate = officialDocumentTemplate;
/*     */   }
/*     */   
/*     */   public boolean isRequiredOpinion() {
/* 139 */     return this.requiredOpinion;
/*     */   }
/*     */   
/*     */   public void setRequiredOpinion(boolean requiredOpinion) {
/* 143 */     this.requiredOpinion = requiredOpinion;
/*     */   }
/*     */   
/*     */   public boolean isAllowRecall() {
/* 147 */     return this.allowRecall;
/*     */   }
/*     */   
/*     */   public void setAllowRecall(boolean allowRecall) {
/* 151 */     this.allowRecall = allowRecall;
/*     */   }
/*     */   
/*     */   public String getOfficialPrintTemplate() {
/* 155 */     return this.officialPrintTemplate;
/*     */   }
/*     */   
/*     */   public void setOfficialPrintTemplate(String officialPrintTemplate) {
/* 159 */     this.officialPrintTemplate = officialPrintTemplate;
/*     */   }
/*     */   
/*     */   public String getLabels() {
/* 163 */     return this.labels;
/*     */   }
/*     */   
/*     */   public void setLabels(String labels) {
/* 167 */     this.labels = labels;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/def/BpmDefProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */