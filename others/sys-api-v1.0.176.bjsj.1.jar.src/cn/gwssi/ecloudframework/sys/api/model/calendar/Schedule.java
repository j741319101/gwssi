/*     */ package com.dstz.sys.api.model.calendar;
/*     */ 
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Schedule
/*     */   implements IDModel, Serializable
/*     */ {
/*     */   public static final String TYPE_SINGLE = "single";
/*     */   public static final String TYPE_SHARE = "share";
/*     */   public static final String TYPE_ILKA = "ilka";
/*     */   protected String id;
/*     */   protected String bizId;
/*     */   protected String title;
/*     */   protected String remark;
/*     */   protected String taskUrl;
/*     */   protected String type;
/*     */   protected String openType;
/*     */   protected String owner;
/*     */   protected String ownerName;
/*     */   protected String participantNames;
/*     */   protected Date startTime;
/*     */   protected Date endTime;
/*     */   protected Date actualStartTime;
/*     */   protected Date completeTime;
/*     */   protected Integer rateProgress;
/*     */   protected String submitter;
/*     */   protected String submitNamer;
/*     */   protected String isLock;
/*     */   protected Date createTime;
/*     */   protected String createBy;
/*     */   protected Date updateTime;
/*     */   protected String updateBy;
/*     */   protected String deleteFlag;
/*     */   protected Integer rev;
/*     */   
/*     */   public void setBizId(String bizId) {
/* 144 */     this.bizId = bizId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   protected List<ScheduleParticipant> scheduleParticipantList = new ArrayList<>();
/*     */   
/*     */   public void setId(String id) {
/* 153 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 161 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/* 165 */     this.title = title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTitle() {
/* 173 */     return this.title;
/*     */   }
/*     */   
/*     */   public void setRemark(String remark) {
/* 177 */     this.remark = remark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemark() {
/* 185 */     return this.remark;
/*     */   }
/*     */   
/*     */   public void setTaskUrl(String taskUrl) {
/* 189 */     this.taskUrl = taskUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskUrl() {
/* 197 */     return this.taskUrl;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 201 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 209 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setOpenType(String openType) {
/* 213 */     this.openType = openType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOpenType() {
/* 221 */     return this.openType;
/*     */   }
/*     */   
/*     */   public void setOwner(String owner) {
/* 225 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwner() {
/* 233 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwnerName(String ownerName) {
/* 237 */     this.ownerName = ownerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwnerName() {
/* 245 */     return this.ownerName;
/*     */   }
/*     */   
/*     */   public void setParticipantNames(String participantNames) {
/* 249 */     this.participantNames = participantNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParticipantNames() {
/* 257 */     return this.participantNames;
/*     */   }
/*     */   
/*     */   public void setStartTime(Date startTime) {
/* 261 */     this.startTime = startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getStartTime() {
/* 269 */     return this.startTime;
/*     */   }
/*     */   
/*     */   public void setEndTime(Date endTime) {
/* 273 */     this.endTime = endTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getEndTime() {
/* 281 */     return this.endTime;
/*     */   }
/*     */   
/*     */   public void setActualStartTime(Date actualStartTime) {
/* 285 */     this.actualStartTime = actualStartTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getActualStartTime() {
/* 293 */     return this.actualStartTime;
/*     */   }
/*     */   
/*     */   public void setCompleteTime(Date completeTime) {
/* 297 */     this.completeTime = completeTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCompleteTime() {
/* 305 */     return this.completeTime;
/*     */   }
/*     */   
/*     */   public void setRateProgress(Integer rateProgress) {
/* 309 */     this.rateProgress = rateProgress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getRateProgress() {
/* 317 */     return this.rateProgress;
/*     */   }
/*     */   
/*     */   public void setSubmitter(String submitter) {
/* 321 */     this.submitter = submitter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubmitter() {
/* 329 */     return this.submitter;
/*     */   }
/*     */   
/*     */   public void setSubmitNamer(String submitNamer) {
/* 333 */     this.submitNamer = submitNamer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubmitNamer() {
/* 341 */     return this.submitNamer;
/*     */   }
/*     */   
/*     */   public void setIsLock(String isLock) {
/* 345 */     this.isLock = isLock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIsLock() {
/* 353 */     return this.isLock;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 357 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 365 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 369 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 377 */     return this.createBy;
/*     */   }
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 381 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 389 */     return this.updateTime;
/*     */   }
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 393 */     this.updateBy = updateBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 401 */     return this.updateBy;
/*     */   }
/*     */   
/*     */   public void setDeleteFlag(String deleteFlag) {
/* 405 */     this.deleteFlag = deleteFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDeleteFlag() {
/* 412 */     return this.deleteFlag;
/*     */   }
/*     */   
/*     */   public void setRev(Integer rev) {
/* 416 */     this.rev = rev;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getRev() {
/* 423 */     return this.rev;
/*     */   }
/*     */   
/*     */   public void setScheduleParticipantList(List<ScheduleParticipant> scheduleParticipantList) {
/* 427 */     this.scheduleParticipantList = scheduleParticipantList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ScheduleParticipant> getScheduleParticipantList() {
/* 434 */     return this.scheduleParticipantList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 441 */     return (new ToStringBuilder(this))
/* 442 */       .append("id", this.id)
/* 443 */       .append("title", this.title)
/* 444 */       .append("remark", this.remark)
/* 445 */       .append("taskUrl", this.taskUrl)
/* 446 */       .append("type", this.type)
/* 447 */       .append("openType", this.openType)
/* 448 */       .append("owner", this.owner)
/* 449 */       .append("ownerName", this.ownerName)
/* 450 */       .append("participantNames", this.participantNames)
/* 451 */       .append("startTime", this.startTime)
/* 452 */       .append("endTime", this.endTime)
/* 453 */       .append("actualStartTime", this.actualStartTime)
/* 454 */       .append("completeTime", this.completeTime)
/* 455 */       .append("rateProgress", this.rateProgress)
/* 456 */       .append("submitter", this.submitter)
/* 457 */       .append("submitNamer", this.submitNamer)
/* 458 */       .append("isLock", this.isLock)
/* 459 */       .append("createTime", this.createTime)
/* 460 */       .append("createBy", this.createBy)
/* 461 */       .append("updateTime", this.updateTime)
/* 462 */       .append("updateBy", this.updateBy)
/* 463 */       .append("deleteFlag", this.deleteFlag)
/* 464 */       .append("rev", this.rev)
/* 465 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBizId() {
/* 470 */     return this.bizId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/calendar/Schedule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */