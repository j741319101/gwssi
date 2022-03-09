/*    */ package com.dstz.bpm.plugin.core.model;
/*    */ 
/*    */ import com.dstz.base.core.model.BaseModel;
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
/*    */ 
/*    */ 
/*    */ public class BpmCarbonCopyReceive
/*    */   extends BaseModel
/*    */ {
/*    */   private static final long serialVersionUID = -3089259622456880564L;
/*    */   private String ccRecordId;
/*    */   private String receiveUserId;
/*    */   private Boolean read;
/*    */   private Integer rev;
/* 32 */   private String type = "";
/*    */   
/*    */   public void setCcRecordId(String ccRecordId) {
/* 35 */     this.ccRecordId = ccRecordId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCcRecordId() {
/* 44 */     return this.ccRecordId;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setReceiveUserId(String receiveUserId) {
/* 49 */     this.receiveUserId = receiveUserId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getReceiveUserId() {
/* 58 */     return this.receiveUserId;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRead(Boolean read) {
/* 63 */     this.read = read;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean getRead() {
/* 72 */     return this.read;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRev(Integer rev) {
/* 77 */     this.rev = rev;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer getRev() {
/* 86 */     return this.rev;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 90 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 94 */     this.type = type;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/model/BpmCarbonCopyReceive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */