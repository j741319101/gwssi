/*    */ package com.dstz.sys.api.model.calendar.dto;
/*    */ 
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import java.io.Serializable;
/*    */ import org.hibernate.validator.constraints.NotBlank;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompleteScheduleModel
/*    */   implements Serializable
/*    */ {
/*    */   @NotBlank(message = "bizId不能为空")
/*    */   private String bizId;
/*    */   private String completeTime;
/*    */   private Integer rate_progress;
/*    */   private String comment;
/*    */   private IUser owner;
/*    */   private String ownerAccount;
/*    */   
/*    */   public String getBizId() {
/* 27 */     return this.bizId;
/*    */   }
/*    */   
/*    */   public void setBizId(String bizId) {
/* 31 */     this.bizId = bizId;
/*    */   }
/*    */ 
/*    */   
/*    */   public IUser getOwner() {
/* 36 */     return this.owner;
/*    */   }
/*    */   
/*    */   public void setOwner(IUser owner) {
/* 40 */     this.owner = owner;
/*    */   }
/*    */   
/*    */   public String getOwnerAccount() {
/* 44 */     return this.ownerAccount;
/*    */   }
/*    */   
/*    */   public void setOwnerAccount(String ownerAccount) {
/* 48 */     this.ownerAccount = ownerAccount;
/*    */   }
/*    */   public String getCompleteTime() {
/* 51 */     return this.completeTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCompleteTime(String completeTime) {
/* 58 */     this.completeTime = completeTime;
/*    */   }
/*    */   
/*    */   public Integer getRate_progress() {
/* 62 */     return this.rate_progress;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRate_progress(Integer rate_progress) {
/* 70 */     this.rate_progress = rate_progress;
/*    */   }
/*    */   public String getComment() {
/* 73 */     return this.comment;
/*    */   }
/*    */   public void setComment(String comment) {
/* 76 */     this.comment = comment;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/calendar/dto/CompleteScheduleModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */