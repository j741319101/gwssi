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
/*     */   private Date updateTime;
/*     */   private Boolean read;
/*     */   private String receiveUserName;
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
/*  97 */     return this.sendLeaderFlag;
/*     */   }
/*     */   
/*     */   public void setSendLeaderFlag(String sendLeaderFlag) {
/* 101 */     this.sendLeaderFlag = sendLeaderFlag;
/*     */   }
/*     */   
/*     */   public String getContent() {
/* 105 */     return this.content;
/*     */   }
/*     */   
/*     */   public void setContent(String content) {
/* 109 */     this.content = content;
/*     */   }
/*     */   
/*     */   public String getInstCreateTime() {
/* 113 */     return this.instCreateTime;
/*     */   }
/*     */   
/*     */   public void setInstCreateTime(String instCreateTime) {
/* 117 */     this.instCreateTime = instCreateTime;
/*     */   }
/*     */   
/*     */   public String getNodeTypeKey() {
/* 121 */     return this.nodeTypeKey;
/*     */   }
/*     */   
/*     */   public void setNodeTypeKey(String nodeTypeKey) {
/* 125 */     this.nodeTypeKey = nodeTypeKey;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 129 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/* 133 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getRecordId() {
/* 137 */     return this.recordId;
/*     */   }
/*     */   
/*     */   public void setRecordId(String recordId) {
/* 141 */     this.recordId = recordId;
/*     */   }
/*     */   
/*     */   public String getSubject() {
/* 145 */     return this.subject;
/*     */   }
/*     */   
/*     */   public void setSubject(String subject) {
/* 149 */     this.subject = subject;
/*     */   }
/*     */   
/*     */   public String getNodeId() {
/* 153 */     return this.nodeId;
/*     */   }
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 157 */     this.nodeId = nodeId;
/*     */   }
/*     */   
/*     */   public String getNodeName() {
/* 161 */     return this.nodeName;
/*     */   }
/*     */   
/*     */   public void setNodeName(String nodeName) {
/* 165 */     this.nodeName = nodeName;
/*     */   }
/*     */   
/*     */   public String getInstId() {
/* 169 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/* 173 */     this.instId = instId;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 177 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 181 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public String getFormType() {
/* 185 */     return this.formType;
/*     */   }
/*     */   
/*     */   public void setFormType(String formType) {
/* 189 */     this.formType = formType;
/*     */   }
/*     */   
/*     */   public String getEvent() {
/* 193 */     return this.event;
/*     */   }
/*     */   
/*     */   public void setEvent(String event) {
/* 197 */     this.event = event;
/*     */   }
/*     */   
/*     */   public String getTriggerUserId() {
/* 201 */     return this.triggerUserId;
/*     */   }
/*     */   
/*     */   public void setTriggerUserId(String triggerUserId) {
/* 205 */     this.triggerUserId = triggerUserId;
/*     */   }
/*     */   
/*     */   public String getTriggerUserName() {
/* 209 */     return this.triggerUserName;
/*     */   }
/*     */   
/*     */   public void setTriggerUserName(String triggerUserName) {
/* 213 */     this.triggerUserName = triggerUserName;
/*     */   }
/*     */   
/*     */   public Date getReceiveTime() {
/* 217 */     return this.receiveTime;
/*     */   }
/*     */   
/*     */   public void setReceiveTime(Date receiveTime) {
/* 221 */     this.receiveTime = receiveTime;
/*     */   }
/*     */   
/*     */   public Boolean getRead() {
/* 225 */     return this.read;
/*     */   }
/*     */   
/*     */   public void setRead(Boolean read) {
/* 229 */     this.read = read;
/*     */   }
/*     */   
/*     */   public String getDocNum() {
/* 233 */     return this.docNum;
/*     */   }
/*     */   
/*     */   public void setDocNum(String docNum) {
/* 237 */     this.docNum = docNum;
/*     */   }
/*     */   
/*     */   public String getZbsj() {
/* 241 */     return this.zbsj;
/*     */   }
/*     */   
/*     */   public void setZbsj(String zbsj) {
/* 245 */     this.zbsj = zbsj;
/*     */   }
/*     */   
/*     */   public String getTaskNames() {
/* 249 */     return this.taskNames;
/*     */   }
/*     */   
/*     */   public void setTaskNames(String taskNames) {
/* 253 */     this.taskNames = taskNames;
/*     */   }
/*     */   
/*     */   public String getTaskUsers() {
/* 257 */     return this.taskUsers;
/*     */   }
/*     */   
/*     */   public void setTaskUsers(String taskUsers) {
/* 261 */     this.taskUsers = taskUsers;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 265 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 269 */     this.status = status;
/*     */   }
/*     */   
/*     */   public String getDefName() {
/* 273 */     return this.defName;
/*     */   }
/*     */   
/*     */   public void setDefName(String defName) {
/* 277 */     this.defName = defName;
/*     */   }
/*     */   
/*     */   public String getCreator() {
/* 281 */     return this.creator;
/*     */   }
/*     */   
/*     */   public void setCreator(String creator) {
/* 285 */     this.creator = creator;
/*     */   }
/*     */   
/*     */   public String getNodeTypeName() {
/* 289 */     return this.nodeTypeName;
/*     */   }
/*     */   
/*     */   public void setNodeTypeName(String nodeTypeName) {
/* 293 */     this.nodeTypeName = nodeTypeName;
/*     */   }
/*     */   
/*     */   public String getReceiveUserName() {
/* 297 */     return this.receiveUserName;
/*     */   }
/*     */   
/*     */   public void setReceiveUserName(String receiveUserName) {
/* 301 */     this.receiveUserName = receiveUserName;
/*     */   }
/*     */   
/*     */   public Date getUpdateTime() {
/* 305 */     return this.updateTime;
/*     */   }
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 309 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 314 */     return "BpmUserReceiveCarbonCopyRecordVO{id='" + this.id + '\'' + ", recordId='" + this.recordId + '\'' + ", subject='" + this.subject + '\'' + ", nodeId='" + this.nodeId + '\'' + ", nodeName='" + this.nodeName + '\'' + ", instId='" + this.instId + '\'' + ", taskId='" + this.taskId + '\'' + ", formType='" + this.formType + '\'' + ", event='" + this.event + '\'' + ", triggerUserId='" + this.triggerUserId + '\'' + ", triggerUserName='" + this.triggerUserName + '\'' + ", receiveTime=" + this.receiveTime + ", read=" + this.read + '}';
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/vo/BpmUserReceiveCarbonCopyRecordVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */