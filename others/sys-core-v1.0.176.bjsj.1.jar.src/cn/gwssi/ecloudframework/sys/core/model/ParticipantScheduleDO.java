/*     */ package cn.gwssi.ecloudframework.sys.core.model;
/*     */ 
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticipantScheduleDO
/*     */ {
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
/*     */   protected String scheduleId;
/*     */   protected String participantorName;
/*     */   protected String participantor;
/*     */   protected String submitComment;
/*     */   protected Integer rev;
/*     */   
/*     */   public void setBizId(String bizId) {
/* 148 */     this.bizId = bizId;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/* 152 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 160 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/* 164 */     this.title = title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTitle() {
/* 172 */     return this.title;
/*     */   }
/*     */   
/*     */   public void setRemark(String remark) {
/* 176 */     this.remark = remark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemark() {
/* 184 */     return this.remark;
/*     */   }
/*     */   
/*     */   public void setTaskUrl(String taskUrl) {
/* 188 */     this.taskUrl = taskUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskUrl() {
/* 196 */     return this.taskUrl;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 200 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 208 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setOpenType(String openType) {
/* 212 */     this.openType = openType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOpenType() {
/* 220 */     return this.openType;
/*     */   }
/*     */   
/*     */   public void setOwner(String owner) {
/* 224 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwner() {
/* 232 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwnerName(String ownerName) {
/* 236 */     this.ownerName = ownerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwnerName() {
/* 244 */     return this.ownerName;
/*     */   }
/*     */   
/*     */   public void setParticipantNames(String participantNames) {
/* 248 */     this.participantNames = participantNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParticipantNames() {
/* 256 */     return this.participantNames;
/*     */   }
/*     */   
/*     */   public void setStartTime(Date startTime) {
/* 260 */     this.startTime = startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getStartTime() {
/* 268 */     return this.startTime;
/*     */   }
/*     */   
/*     */   public void setEndTime(Date endTime) {
/* 272 */     this.endTime = endTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getEndTime() {
/* 280 */     return this.endTime;
/*     */   }
/*     */   
/*     */   public void setActualStartTime(Date actualStartTime) {
/* 284 */     this.actualStartTime = actualStartTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getActualStartTime() {
/* 292 */     return this.actualStartTime;
/*     */   }
/*     */   
/*     */   public void setCompleteTime(Date completeTime) {
/* 296 */     this.completeTime = completeTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCompleteTime() {
/* 304 */     return this.completeTime;
/*     */   }
/*     */   
/*     */   public void setRateProgress(Integer rateProgress) {
/* 308 */     this.rateProgress = rateProgress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getRateProgress() {
/* 316 */     return this.rateProgress;
/*     */   }
/*     */   
/*     */   public void setSubmitter(String submitter) {
/* 320 */     this.submitter = submitter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubmitter() {
/* 328 */     return this.submitter;
/*     */   }
/*     */   
/*     */   public void setSubmitNamer(String submitNamer) {
/* 332 */     this.submitNamer = submitNamer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubmitNamer() {
/* 340 */     return this.submitNamer;
/*     */   }
/*     */   
/*     */   public void setIsLock(String isLock) {
/* 344 */     this.isLock = isLock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIsLock() {
/* 352 */     return this.isLock;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 356 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 364 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 368 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 376 */     return this.createBy;
/*     */   }
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 380 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 388 */     return this.updateTime;
/*     */   }
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 392 */     this.updateBy = updateBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 400 */     return this.updateBy;
/*     */   }
/*     */   
/*     */   public void setDeleteFlag(String deleteFlag) {
/* 404 */     this.deleteFlag = deleteFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDeleteFlag() {
/* 411 */     return this.deleteFlag;
/*     */   }
/*     */   
/*     */   public void setRev(Integer rev) {
/* 415 */     this.rev = rev;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getRev() {
/* 422 */     return this.rev;
/*     */   }
/*     */   
/*     */   public String getScheduleId() {
/* 426 */     return this.scheduleId;
/*     */   }
/*     */   
/*     */   public void setScheduleId(String scheduleId) {
/* 430 */     this.scheduleId = scheduleId;
/*     */   }
/*     */   
/*     */   public String getParticipantorName() {
/* 434 */     return this.participantorName;
/*     */   }
/*     */   
/*     */   public void setParticipantorName(String participantorName) {
/* 438 */     this.participantorName = participantorName;
/*     */   }
/*     */   
/*     */   public String getParticipantor() {
/* 442 */     return this.participantor;
/*     */   }
/*     */   
/*     */   public void setParticipantor(String participantor) {
/* 446 */     this.participantor = participantor;
/*     */   }
/*     */   
/*     */   public String getSubmitComment() {
/* 450 */     return this.submitComment;
/*     */   }
/*     */   
/*     */   public void setSubmitComment(String submitComment) {
/* 454 */     this.submitComment = submitComment;
/*     */   }
/*     */   
/*     */   public String getBizId() {
/* 458 */     return this.bizId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/ParticipantScheduleDO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */