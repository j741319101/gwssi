/*     */ package com.dstz.bpm.plugin.vo;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class BpmUserReceiveCarbonCopyRecordVO
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8661005770159558772L;
/*     */   private String id;
/*     */   private String recordId;
/*     */   private String subject;
/*     */   private String nodeId;
/*     */   private String nodeName;
/*     */   private String instId;
/*     */   private String taskId;
/*     */   private String formType;
/*     */   private String event;
/*     */   private String triggerUserId;
/*     */   private String triggerUserName;
/*     */   private Date receiveTime;
/*     */   private Boolean read;
/*     */   private String docNum;
/*     */   private String zbsj;
/*     */   private String taskNames;
/*     */   private String taskUsers;
/*     */   private String status;
/*     */   private String defName;
/*     */   private String creator;
/*     */   private String nodeTypeName;
/*     */   private String nodeTypeKey;
/*     */   private String instCreateTime;
/*     */   private String content;
/*     */   private String sendLeaderFlag;
/*     */   
/*     */   public String getSendLeaderFlag() {
/*  96 */     return this.sendLeaderFlag;
/*     */   }
/*     */   
/*     */   public void setSendLeaderFlag(String sendLeaderFlag) {
/* 100 */     this.sendLeaderFlag = sendLeaderFlag;
/*     */   }
/*     */   
/*     */   public String getContent() {
/* 104 */     return this.content;
/*     */   }
/*     */   
/*     */   public void setContent(String content) {
/* 108 */     this.content = content;
/*     */   }
/*     */   
/*     */   public String getInstCreateTime() {
/* 112 */     return this.instCreateTime;
/*     */   }
/*     */   
/*     */   public void setInstCreateTime(String instCreateTime) {
/* 116 */     this.instCreateTime = instCreateTime;
/*     */   }
/*     */   
/*     */   public String getNodeTypeKey() {
/* 120 */     return this.nodeTypeKey;
/*     */   }
/*     */   
/*     */   public void setNodeTypeKey(String nodeTypeKey) {
/* 124 */     this.nodeTypeKey = nodeTypeKey;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 128 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/* 132 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getRecordId() {
/* 136 */     return this.recordId;
/*     */   }
/*     */   
/*     */   public void setRecordId(String recordId) {
/* 140 */     this.recordId = recordId;
/*     */   }
/*     */   
/*     */   public String getSubject() {
/* 144 */     return this.subject;
/*     */   }
/*     */   
/*     */   public void setSubject(String subject) {
/* 148 */     this.subject = subject;
/*     */   }
/*     */   
/*     */   public String getNodeId() {
/* 152 */     return this.nodeId;
/*     */   }
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 156 */     this.nodeId = nodeId;
/*     */   }
/*     */   
/*     */   public String getNodeName() {
/* 160 */     return this.nodeName;
/*     */   }
/*     */   
/*     */   public void setNodeName(String nodeName) {
/* 164 */     this.nodeName = nodeName;
/*     */   }
/*     */   
/*     */   public String getInstId() {
/* 168 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/* 172 */     this.instId = instId;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 176 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 180 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public String getFormType() {
/* 184 */     return this.formType;
/*     */   }
/*     */   
/*     */   public void setFormType(String formType) {
/* 188 */     this.formType = formType;
/*     */   }
/*     */   
/*     */   public String getEvent() {
/* 192 */     return this.event;
/*     */   }
/*     */   
/*     */   public void setEvent(String event) {
/* 196 */     this.event = event;
/*     */   }
/*     */   
/*     */   public String getTriggerUserId() {
/* 200 */     return this.triggerUserId;
/*     */   }
/*     */   
/*     */   public void setTriggerUserId(String triggerUserId) {
/* 204 */     this.triggerUserId = triggerUserId;
/*     */   }
/*     */   
/*     */   public String getTriggerUserName() {
/* 208 */     return this.triggerUserName;
/*     */   }
/*     */   
/*     */   public void setTriggerUserName(String triggerUserName) {
/* 212 */     this.triggerUserName = triggerUserName;
/*     */   }
/*     */   
/*     */   public Date getReceiveTime() {
/* 216 */     return this.receiveTime;
/*     */   }
/*     */   
/*     */   public void setReceiveTime(Date receiveTime) {
/* 220 */     this.receiveTime = receiveTime;
/*     */   }
/*     */   
/*     */   public Boolean getRead() {
/* 224 */     return this.read;
/*     */   }
/*     */   
/*     */   public void setRead(Boolean read) {
/* 228 */     this.read = read;
/*     */   }
/*     */   
/*     */   public String getDocNum() {
/* 232 */     return this.docNum;
/*     */   }
/*     */   
/*     */   public void setDocNum(String docNum) {
/* 236 */     this.docNum = docNum;
/*     */   }
/*     */   
/*     */   public String getZbsj() {
/* 240 */     return this.zbsj;
/*     */   }
/*     */   
/*     */   public void setZbsj(String zbsj) {
/* 244 */     this.zbsj = zbsj;
/*     */   }
/*     */   
/*     */   public String getTaskNames() {
/* 248 */     return this.taskNames;
/*     */   }
/*     */   
/*     */   public void setTaskNames(String taskNames) {
/* 252 */     this.taskNames = taskNames;
/*     */   }
/*     */   
/*     */   public String getTaskUsers() {
/* 256 */     return this.taskUsers;
/*     */   }
/*     */   
/*     */   public void setTaskUsers(String taskUsers) {
/* 260 */     this.taskUsers = taskUsers;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 264 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 268 */     this.status = status;
/*     */   }
/*     */   
/*     */   public String getDefName() {
/* 272 */     return this.defName;
/*     */   }
/*     */   
/*     */   public void setDefName(String defName) {
/* 276 */     this.defName = defName;
/*     */   }
/*     */   
/*     */   public String getCreator() {
/* 280 */     return this.creator;
/*     */   }
/*     */   
/*     */   public void setCreator(String creator) {
/* 284 */     this.creator = creator;
/*     */   }
/*     */   
/*     */   public String getNodeTypeName() {
/* 288 */     return this.nodeTypeName;
/*     */   }
/*     */   
/*     */   public void setNodeTypeName(String nodeTypeName) {
/* 292 */     this.nodeTypeName = nodeTypeName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 297 */     return "BpmUserReceiveCarbonCopyRecordVO{id='" + this.id + '\'' + ", recordId='" + this.recordId + '\'' + ", subject='" + this.subject + '\'' + ", nodeId='" + this.nodeId + '\'' + ", nodeName='" + this.nodeName + '\'' + ", instId='" + this.instId + '\'' + ", taskId='" + this.taskId + '\'' + ", formType='" + this.formType + '\'' + ", event='" + this.event + '\'' + ", triggerUserId='" + this.triggerUserId + '\'' + ", triggerUserName='" + this.triggerUserName + '\'' + ", receiveTime=" + this.receiveTime + ", read=" + this.read + '}';
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/vo/BpmUserReceiveCarbonCopyRecordVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */