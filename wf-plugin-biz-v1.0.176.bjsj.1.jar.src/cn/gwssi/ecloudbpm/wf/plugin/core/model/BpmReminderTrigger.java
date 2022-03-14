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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BpmReminderTrigger
/*     */   extends BaseModel
/*     */ {
/*     */   protected String taskId;
/*     */   protected String reminderDesc;
/*     */   protected String beforeScript;
/*     */   protected String msgType;
/*     */   protected String htmlMsg;
/*     */   protected String textMsg;
/*     */   protected Integer isCalcWorkday;
/*     */   protected Integer isUrgent;
/*     */   protected Integer maxReminderTimes;
/*     */   protected Integer reminderTimes;
/*     */   protected Integer reminderCycle;
/*     */   protected Date duedate;
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  67 */     this.taskId = taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskId() {
/*  75 */     return this.taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderDesc(String reminderDesc) {
/*  82 */     this.reminderDesc = reminderDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReminderDesc() {
/*  90 */     return this.reminderDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeforeScript(String beforeScript) {
/*  97 */     this.beforeScript = beforeScript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeforeScript() {
/* 105 */     return this.beforeScript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMsgType(String msgType) {
/* 112 */     this.msgType = msgType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMsgType() {
/* 120 */     return this.msgType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHtmlMsg(String htmlMsg) {
/* 127 */     this.htmlMsg = htmlMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHtmlMsg() {
/* 135 */     return this.htmlMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextMsg(String textMsg) {
/* 142 */     this.textMsg = textMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTextMsg() {
/* 150 */     return this.textMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsCalcWorkday(Integer isCalcWorkday) {
/* 157 */     this.isCalcWorkday = isCalcWorkday;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getIsCalcWorkday() {
/* 165 */     return this.isCalcWorkday;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsUrgent(Integer isUrgent) {
/* 172 */     this.isUrgent = isUrgent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getIsUrgent() {
/* 180 */     return this.isUrgent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxReminderTimes(Integer maxReminderTimes) {
/* 187 */     this.maxReminderTimes = maxReminderTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getMaxReminderTimes() {
/* 195 */     return this.maxReminderTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderTimes(Integer reminderTimes) {
/* 202 */     this.reminderTimes = reminderTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getReminderTimes() {
/* 210 */     return this.reminderTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderCycle(Integer reminderCycle) {
/* 217 */     this.reminderCycle = reminderCycle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getReminderCycle() {
/* 225 */     return this.reminderCycle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDuedate(Date duedate) {
/* 232 */     this.duedate = duedate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDuedate() {
/* 240 */     return this.duedate;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/model/BpmReminderTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */