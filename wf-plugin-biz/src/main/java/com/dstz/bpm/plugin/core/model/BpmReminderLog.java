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
/*  68 */     this.instanceId = instanceId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstanceId() {
/*  76 */     return this.instanceId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderTitle(String reminderTitle) {
/*  83 */     this.reminderTitle = reminderTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReminderTitle() {
/*  91 */     return this.reminderTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubject(String subject) {
/*  98 */     this.subject = subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubject() {
/* 106 */     return this.subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 113 */     this.nodeId = nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/* 121 */     return this.nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMsgType(String msgType) {
/* 128 */     this.msgType = msgType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMsgType() {
/* 136 */     return this.msgType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderUsers(String reminderUsers) {
/* 143 */     this.reminderUsers = reminderUsers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReminderUsers() {
/* 151 */     return this.reminderUsers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderUserids(String reminderUserids) {
/* 158 */     this.reminderUserids = reminderUserids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReminderUserids() {
/* 166 */     return this.reminderUserids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderDate(Date reminderDate) {
/* 173 */     this.reminderDate = reminderDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getReminderDate() {
/* 181 */     return this.reminderDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtend(String extend) {
/* 188 */     this.extend = extend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtend() {
/* 196 */     return this.extend;
/*     */   }
/*     */   
/*     */   public String getStatue() {
/* 200 */     return this.statue;
/*     */   }
/*     */   
/*     */   public void setStatue(String statue) {
/* 204 */     this.statue = statue;
/*     */   }
/*     */   
/*     */   public String getFeedback() {
/* 208 */     return this.feedback;
/*     */   }
/*     */   
/*     */   public void setFeedback(String feedback) {
/* 212 */     this.feedback = feedback;
/*     */   }
/*     */   
/*     */   public String getFromUsers() {
/* 216 */     return this.fromUsers;
/*     */   }
/*     */   
/*     */   public void setFromUsers(String fromUsers) {
/* 220 */     this.fromUsers = fromUsers;
/*     */   }
/*     */   
/*     */   public String getFromUserIds() {
/* 224 */     return this.fromUserIds;
/*     */   }
/*     */   
/*     */   public void setFromUserIds(String fromUserIds) {
/* 228 */     this.fromUserIds = fromUserIds;
/*     */   }
/*     */   
/*     */   public String getNodeName() {
/* 232 */     return this.nodeName;
/*     */   }
/*     */   
/*     */   public void setNodeName(String nodeName) {
/* 236 */     this.nodeName = nodeName;
/*     */   }
/*     */   
/*     */   public String getIsUrgent() {
/* 240 */     return this.isUrgent;
/*     */   }
/*     */   
/*     */   public void setIsUrgent(String isUrgent) {
/* 244 */     this.isUrgent = isUrgent;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 248 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 252 */     this.taskId = taskId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/model/BpmReminderLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */