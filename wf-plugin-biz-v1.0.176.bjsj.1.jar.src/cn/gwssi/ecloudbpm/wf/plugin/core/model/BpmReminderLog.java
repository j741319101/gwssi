/*     */ package com.dstz.bpm.plugin.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
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
/*     */ public class BpmReminderLog
/*     */   extends BaseModel
/*     */ {
/*     */   protected String instanceId;
/*     */   protected String reminderTitle;
/*     */   protected String subject;
/*     */   protected String nodeId;
/*     */   protected String nodeName;
/*     */   protected String msgType;
/*     */   protected String reminderUsers;
/*     */   protected String reminderUserids;
/*     */   protected Date reminderDate;
/*     */   protected String extend;
/*     */   private String statue;
/*     */   private String feedback;
/*     */   private String fromUsers;
/*     */   private String fromUserIds;
/*     */   private String isUrgent;
/*     */   private String taskId;
/*     */   
/*     */   public void setInstanceId(String instanceId) {
/*  66 */     this.instanceId = instanceId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstanceId() {
/*  74 */     return this.instanceId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderTitle(String reminderTitle) {
/*  81 */     this.reminderTitle = reminderTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReminderTitle() {
/*  89 */     return this.reminderTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubject(String subject) {
/*  96 */     this.subject = subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubject() {
/* 104 */     return this.subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 111 */     this.nodeId = nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/* 119 */     return this.nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMsgType(String msgType) {
/* 126 */     this.msgType = msgType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMsgType() {
/* 134 */     return this.msgType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderUsers(String reminderUsers) {
/* 141 */     this.reminderUsers = reminderUsers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReminderUsers() {
/* 149 */     return this.reminderUsers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderUserids(String reminderUserids) {
/* 156 */     this.reminderUserids = reminderUserids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReminderUserids() {
/* 164 */     return this.reminderUserids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderDate(Date reminderDate) {
/* 171 */     this.reminderDate = reminderDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getReminderDate() {
/* 179 */     return this.reminderDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtend(String extend) {
/* 186 */     this.extend = extend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtend() {
/* 194 */     return this.extend;
/*     */   }
/*     */   
/*     */   public String getStatue() {
/* 198 */     return this.statue;
/*     */   }
/*     */   
/*     */   public void setStatue(String statue) {
/* 202 */     this.statue = statue;
/*     */   }
/*     */   
/*     */   public String getFeedback() {
/* 206 */     return this.feedback;
/*     */   }
/*     */   
/*     */   public void setFeedback(String feedback) {
/* 210 */     this.feedback = feedback;
/*     */   }
/*     */   
/*     */   public String getFromUsers() {
/* 214 */     return this.fromUsers;
/*     */   }
/*     */   
/*     */   public void setFromUsers(String fromUsers) {
/* 218 */     this.fromUsers = fromUsers;
/*     */   }
/*     */   
/*     */   public String getFromUserIds() {
/* 222 */     return this.fromUserIds;
/*     */   }
/*     */   
/*     */   public void setFromUserIds(String fromUserIds) {
/* 226 */     this.fromUserIds = fromUserIds;
/*     */   }
/*     */   
/*     */   public String getNodeName() {
/* 230 */     return this.nodeName;
/*     */   }
/*     */   
/*     */   public void setNodeName(String nodeName) {
/* 234 */     this.nodeName = nodeName;
/*     */   }
/*     */   
/*     */   public String getIsUrgent() {
/* 238 */     return this.isUrgent;
/*     */   }
/*     */   
/*     */   public void setIsUrgent(String isUrgent) {
/* 242 */     this.isUrgent = isUrgent;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 246 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 250 */     this.taskId = taskId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/model/BpmReminderLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */