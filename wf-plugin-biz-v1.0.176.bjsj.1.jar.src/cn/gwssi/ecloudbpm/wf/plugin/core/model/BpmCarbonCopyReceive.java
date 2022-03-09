/*     */ package cn.gwssi.ecloudbpm.wf.plugin.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.CarbonCopyStatus;
/*     */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
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
/*     */ public class BpmCarbonCopyReceive
/*     */   extends BaseModel
/*     */ {
/*     */   private static final long serialVersionUID = -3089259622456880564L;
/*     */   private String ccRecordId;
/*     */   private String receiveUserId;
/*     */   private String receiveUserName;
/*  31 */   private String status = CarbonCopyStatus.UNREAD.getKey();
/*     */ 
/*     */ 
/*     */   
/*     */   private Boolean read;
/*     */ 
/*     */   
/*     */   private Integer rev;
/*     */ 
/*     */   
/*  41 */   private String type = "";
/*     */   
/*     */   public void setCcRecordId(String ccRecordId) {
/*  44 */     this.ccRecordId = ccRecordId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCcRecordId() {
/*  53 */     return this.ccRecordId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReceiveUserId(String receiveUserId) {
/*  58 */     this.receiveUserId = receiveUserId;
/*     */   }
/*     */   
/*     */   public String getReceiveUserName() {
/*  62 */     return this.receiveUserName;
/*     */   }
/*     */   
/*     */   public void setReceiveUserName(String receiveUserName) {
/*  66 */     this.receiveUserName = receiveUserName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReceiveUserId() {
/*  75 */     return this.receiveUserId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRead(Boolean read) {
/*  80 */     this.read = read;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean getRead() {
/*  89 */     return this.read;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRev(Integer rev) {
/*  94 */     this.rev = rev;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getRev() {
/* 103 */     return this.rev;
/*     */   }
/*     */   
/*     */   public String getType() {
/* 107 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 111 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 115 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 119 */     this.status = status;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/model/BpmCarbonCopyReceive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */