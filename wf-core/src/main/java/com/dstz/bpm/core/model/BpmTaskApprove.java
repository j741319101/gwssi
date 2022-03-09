/*     */ package com.dstz.bpm.core.model;
/*     */ 
/*     */ import com.dstz.bpm.api.model.task.IBpmTaskApprove;
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
/*     */ public class BpmTaskApprove
/*     */   implements IBpmTaskApprove
/*     */ {
/*     */   protected String id;
/*     */   protected String nodeName;
/*     */   protected String nodeId;
/*     */   protected Date approveTime;
/*     */   protected Long durMs;
/*     */   protected String approveStatus;
/*     */   protected String subject;
/*     */   protected String defName;
/*     */   protected String status;
/*     */   protected Date endTime;
/*     */   protected Long duration;
/*     */   protected String typeId;
/*     */   protected String actInstId;
/*     */   protected String createBy;
/*     */   protected String creator;
/*     */   protected Date createTime;
/*     */   protected String docNum;
/*     */   protected String zbsj;
/*     */   private String nodeTypeKey;
/*     */   private String nodeTypeName;
/*     */   private String remindNum;
/*     */   private String taskNames;
/*     */   private String taskUsers;
/*     */   private String optionId;
/*     */   private String taskId;
/*     */   
/*     */   public String getOptionId() {
/*  86 */     return this.optionId;
/*     */   }
/*     */   
/*     */   public void setOptionId(String optionId) {
/*  90 */     this.optionId = optionId;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  94 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  98 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getNodeName() {
/* 102 */     return this.nodeName;
/*     */   }
/*     */   
/*     */   public void setNodeName(String nodeName) {
/* 106 */     this.nodeName = nodeName;
/*     */   }
/*     */   
/*     */   public Date getApproveTime() {
/* 110 */     return this.approveTime;
/*     */   }
/*     */   
/*     */   public void setApproveTime(Date approveTime) {
/* 114 */     this.approveTime = approveTime;
/*     */   }
/*     */   
/*     */   public Long getDurMs() {
/* 118 */     return this.durMs;
/*     */   }
/*     */   
/*     */   public void setDurMs(Long durMs) {
/* 122 */     this.durMs = durMs;
/*     */   }
/*     */   
/*     */   public String getApproveStatus() {
/* 126 */     return this.approveStatus;
/*     */   }
/*     */   
/*     */   public void setApproveStatus(String approveStatus) {
/* 130 */     this.approveStatus = approveStatus;
/*     */   }
/*     */   
/*     */   public String getSubject() {
/* 134 */     return this.subject;
/*     */   }
/*     */   
/*     */   public void setSubject(String subject) {
/* 138 */     this.subject = subject;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 142 */     return this.status;
/*     */   }
/*     */   
/*     */   public String getNodeId() {
/* 146 */     return this.nodeId;
/*     */   }
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 150 */     this.nodeId = nodeId;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 154 */     this.status = status;
/*     */   }
/*     */   
/*     */   public Date getEndTime() {
/* 158 */     return this.endTime;
/*     */   }
/*     */   
/*     */   public void setEndTime(Date endTime) {
/* 162 */     this.endTime = endTime;
/*     */   }
/*     */   
/*     */   public Long getDuration() {
/* 166 */     return this.duration;
/*     */   }
/*     */   
/*     */   public void setDuration(Long duration) {
/* 170 */     this.duration = duration;
/*     */   }
/*     */   
/*     */   public String getTypeId() {
/* 174 */     return this.typeId;
/*     */   }
/*     */   
/*     */   public void setTypeId(String typeId) {
/* 178 */     this.typeId = typeId;
/*     */   }
/*     */   
/*     */   public String getActInstId() {
/* 182 */     return this.actInstId;
/*     */   }
/*     */   
/*     */   public String getDefName() {
/* 186 */     return this.defName;
/*     */   }
/*     */   
/*     */   public void setDefName(String defName) {
/* 190 */     this.defName = defName;
/*     */   }
/*     */   
/*     */   public void setActInstId(String actInstId) {
/* 194 */     this.actInstId = actInstId;
/*     */   }
/*     */   
/*     */   public String getCreateBy() {
/* 198 */     return this.createBy;
/*     */   }
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 202 */     this.createBy = createBy;
/*     */   }
/*     */   
/*     */   public String getCreator() {
/* 206 */     return this.creator;
/*     */   }
/*     */   
/*     */   public void setCreator(String creator) {
/* 210 */     this.creator = creator;
/*     */   }
/*     */   
/*     */   public Date getCreateTime() {
/* 214 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 218 */     this.createTime = createTime;
/*     */   }
/*     */   
/*     */   public String getDocNum() {
/* 222 */     return this.docNum;
/*     */   }
/*     */   
/*     */   public void setDocNum(String docNum) {
/* 226 */     this.docNum = docNum;
/*     */   }
/*     */   
/*     */   public String getZbsj() {
/* 230 */     return this.zbsj;
/*     */   }
/*     */   
/*     */   public void setZbsj(String zbsj) {
/* 234 */     this.zbsj = zbsj;
/*     */   }
/*     */   
/*     */   public String getNodeTypeKey() {
/* 238 */     return this.nodeTypeKey;
/*     */   }
/*     */   
/*     */   public void setNodeTypeKey(String nodeTypeKey) {
/* 242 */     this.nodeTypeKey = nodeTypeKey;
/*     */   }
/*     */   
/*     */   public String getNodeTypeName() {
/* 246 */     return this.nodeTypeName;
/*     */   }
/*     */   
/*     */   public void setNodeTypeName(String nodeTypeName) {
/* 250 */     this.nodeTypeName = nodeTypeName;
/*     */   }
/*     */   
/*     */   public String getRemindNum() {
/* 254 */     return this.remindNum;
/*     */   }
/*     */   
/*     */   public void setRemindNum(String remindNum) {
/* 258 */     this.remindNum = remindNum;
/*     */   }
/*     */   
/*     */   public String getTaskNames() {
/* 262 */     return this.taskNames;
/*     */   }
/*     */   
/*     */   public void setTaskNames(String taskNames) {
/* 266 */     this.taskNames = taskNames;
/*     */   }
/*     */   
/*     */   public String getTaskUsers() {
/* 270 */     return this.taskUsers;
/*     */   }
/*     */   
/*     */   public void setTaskUsers(String taskUsers) {
/* 274 */     this.taskUsers = taskUsers;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 278 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 282 */     this.taskId = taskId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmTaskApprove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */