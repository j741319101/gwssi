/*     */ package com.dstz.bpm.rest.vo;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
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
/*     */ public class DocAndEnclosureDto
/*     */   implements Serializable
/*     */ {
/*     */   private String contentId;
/*     */   private String businessId;
/*     */   private String documentName;
/*     */   private String contentCode;
/*     */   private String documentBody;
/*     */   private String source;
/*     */   private String describe;
/*     */   List<DocumentEnclosureDto> documentEnclosureDtos;
/*     */   
/*     */   public String getContentId() {
/*  44 */     return this.contentId;
/*     */   }
/*     */   
/*     */   public void setContentId(String contentId) {
/*  48 */     this.contentId = contentId;
/*     */   }
/*     */   
/*     */   public String getBusinessId() {
/*  52 */     return this.businessId;
/*     */   }
/*     */   
/*     */   public void setBusinessId(String businessId) {
/*  56 */     this.businessId = businessId;
/*     */   }
/*     */   
/*     */   public String getDocumentName() {
/*  60 */     return this.documentName;
/*     */   }
/*     */   
/*     */   public void setDocumentName(String documentName) {
/*  64 */     this.documentName = documentName;
/*     */   }
/*     */   
/*     */   public String getContentCode() {
/*  68 */     return this.contentCode;
/*     */   }
/*     */   
/*     */   public void setContentCode(String contentCode) {
/*  72 */     this.contentCode = contentCode;
/*     */   }
/*     */   
/*     */   public String getDocumentBody() {
/*  76 */     return this.documentBody;
/*     */   }
/*     */   
/*     */   public void setDocumentBody(String documentBody) {
/*  80 */     this.documentBody = documentBody;
/*     */   }
/*     */   
/*     */   public String getSource() {
/*  84 */     return this.source;
/*     */   }
/*     */   
/*     */   public void setSource(String source) {
/*  88 */     this.source = source;
/*     */   }
/*     */   
/*     */   public String getDescribe() {
/*  92 */     return this.describe;
/*     */   }
/*     */   
/*     */   public void setDescribe(String describe) {
/*  96 */     this.describe = describe;
/*     */   }
/*     */   
/*     */   public List<DocumentEnclosureDto> getDocumentEnclosureDtos() {
/* 100 */     return this.documentEnclosureDtos;
/*     */   }
/*     */   
/*     */   public void setDocumentEnclosureDtos(List<DocumentEnclosureDto> documentEnclosureDtos) {
/* 104 */     this.documentEnclosureDtos = documentEnclosureDtos;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/vo/DocAndEnclosureDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */