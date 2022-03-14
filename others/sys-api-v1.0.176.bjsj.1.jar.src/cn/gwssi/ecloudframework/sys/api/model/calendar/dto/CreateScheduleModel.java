/*     */ package com.dstz.sys.api.model.calendar.dto;
/*     */ 
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.validation.constraints.NotNull;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreateScheduleModel
/*     */   implements Serializable
/*     */ {
/*     */   @NotBlank(message = "业务ID")
/*     */   private String bizId;
/*     */   @NotBlank(message = "业务ID")
/*     */   private String source;
/*     */   @NotBlank(message = "标题不能为空")
/*     */   private String title;
/*     */   @NotBlank(message = "详细描述不能为空")
/*     */   private String remark;
/*     */   @NotNull(message = "日程开始时间不能为空")
/*     */   private Date startTime;
/*     */   @NotNull(message = "日程结束时间不能为空")
/*     */   private Date endTime;
/*     */   private String taskUrl;
/*     */   private String openType;
/*     */   private IUser owner;
/*     */   private String ownerAccount;
/*     */   @NotBlank(message = "日程类型不能为空")
/*  41 */   private String type = "single";
/*     */ 
/*     */   
/*     */   private List<IUser> user;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBizId() {
/*  49 */     return this.bizId;
/*     */   }
/*     */   
/*     */   public void setBizId(String bizId) {
/*  53 */     this.bizId = bizId;
/*     */   }
/*     */   
/*     */   public String getTitle() {
/*  57 */     return this.title;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/*  61 */     this.title = title;
/*     */   }
/*     */   
/*     */   public String getRemark() {
/*  65 */     return this.remark;
/*     */   }
/*     */   
/*     */   public void setRemark(String remark) {
/*  69 */     this.remark = remark;
/*     */   }
/*     */   
/*     */   public Date getStartTime() {
/*  73 */     return this.startTime;
/*     */   }
/*     */   
/*     */   public void setStartTime(Date startTime) {
/*  77 */     this.startTime = startTime;
/*     */   }
/*     */   
/*     */   public Date getEndTime() {
/*  81 */     return this.endTime;
/*     */   }
/*     */   
/*     */   public void setEndTime(Date endTime) {
/*  85 */     this.endTime = endTime;
/*     */   }
/*     */   
/*     */   public String getTaskUrl() {
/*  89 */     return this.taskUrl;
/*     */   }
/*     */   
/*     */   public void setTaskUrl(String taskUrl) {
/*  93 */     this.taskUrl = taskUrl;
/*     */   }
/*     */   
/*     */   public String getOpenType() {
/*  97 */     return this.openType;
/*     */   }
/*     */   
/*     */   public void setOpenType(String openType) {
/* 101 */     this.openType = openType;
/*     */   }
/*     */   
/*     */   public IUser getOwner() {
/* 105 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(IUser owner) {
/* 109 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public String getOwnerAccount() {
/* 113 */     return this.ownerAccount;
/*     */   }
/*     */   
/*     */   public void setOwnerAccount(String ownerAccount) {
/* 117 */     this.ownerAccount = ownerAccount;
/*     */   }
/*     */   
/*     */   public String getType() {
/* 121 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 125 */     this.type = type;
/*     */   }
/*     */   
/*     */   public List<IUser> getUser() {
/* 129 */     return this.user;
/*     */   }
/*     */   
/*     */   public void setUser(List<IUser> user) {
/* 133 */     this.user = user;
/*     */   }
/*     */   
/*     */   public String getSource() {
/* 137 */     return this.source;
/*     */   }
/*     */   public void setSource(String source) {
/* 140 */     this.source = source;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/calendar/dto/CreateScheduleModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */