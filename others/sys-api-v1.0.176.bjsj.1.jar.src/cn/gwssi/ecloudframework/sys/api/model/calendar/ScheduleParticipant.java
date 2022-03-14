/*     */ package com.dstz.sys.api.model.calendar;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
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
/*     */ public class ScheduleParticipant
/*     */   implements Serializable
/*     */ {
/*     */   protected String id;
/*     */   protected String scheduleId;
/*     */   protected String participantorName;
/*     */   protected String participantor;
/*     */   protected Integer rateProgress;
/*     */   protected String submitComment;
/*     */   protected Date createTime;
/*     */   protected Date updateTime;
/*     */   protected Date actualStartTime;
/*     */   protected Date completeTime;
/*     */   
/*     */   public void setId(String id) {
/*  60 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  68 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setScheduleId(String scheduleId) {
/*  72 */     this.scheduleId = scheduleId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScheduleId() {
/*  80 */     return this.scheduleId;
/*     */   }
/*     */   
/*     */   public void setParticipantorName(String participantorName) {
/*  84 */     this.participantorName = participantorName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParticipantorName() {
/*  92 */     return this.participantorName;
/*     */   }
/*     */   
/*     */   public void setParticipantor(String participantor) {
/*  96 */     this.participantor = participantor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParticipantor() {
/* 104 */     return this.participantor;
/*     */   }
/*     */   
/*     */   public void setRateProgress(Integer rateProgress) {
/* 108 */     this.rateProgress = rateProgress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getRateProgress() {
/* 116 */     return this.rateProgress;
/*     */   }
/*     */   
/*     */   public void setSubmitComment(String submitComment) {
/* 120 */     this.submitComment = submitComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubmitComment() {
/* 128 */     return this.submitComment;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 132 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 140 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 144 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 152 */     return this.updateTime;
/*     */   }
/*     */   
/*     */   public void setActualStartTime(Date actualStartTime) {
/* 156 */     this.actualStartTime = actualStartTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getActualStartTime() {
/* 164 */     return this.actualStartTime;
/*     */   }
/*     */   
/*     */   public void setCompleteTime(Date completeTime) {
/* 168 */     this.completeTime = completeTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCompleteTime() {
/* 176 */     return this.completeTime;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 182 */     return (new ToStringBuilder(this))
/* 183 */       .append("id", this.id)
/* 184 */       .append("scheduleId", this.scheduleId)
/* 185 */       .append("participantorName", this.participantorName)
/* 186 */       .append("participantor", this.participantor)
/* 187 */       .append("rateProgress", this.rateProgress)
/* 188 */       .append("submitComment", this.submitComment)
/* 189 */       .append("createTime", this.createTime)
/* 190 */       .append("updateTime", this.updateTime)
/* 191 */       .append("actualStartTime", this.actualStartTime)
/* 192 */       .append("completeTime", this.completeTime)
/* 193 */       .toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/calendar/ScheduleParticipant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */