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
/*  69 */     this.taskId = taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskId() {
/*  77 */     return this.taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderDesc(String reminderDesc) {
/*  84 */     this.reminderDesc = reminderDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReminderDesc() {
/*  92 */     return this.reminderDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeforeScript(String beforeScript) {
/*  99 */     this.beforeScript = beforeScript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeforeScript() {
/* 107 */     return this.beforeScript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMsgType(String msgType) {
/* 114 */     this.msgType = msgType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMsgType() {
/* 122 */     return this.msgType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHtmlMsg(String htmlMsg) {
/* 129 */     this.htmlMsg = htmlMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHtmlMsg() {
/* 137 */     return this.htmlMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextMsg(String textMsg) {
/* 144 */     this.textMsg = textMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTextMsg() {
/* 152 */     return this.textMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsCalcWorkday(Integer isCalcWorkday) {
/* 159 */     this.isCalcWorkday = isCalcWorkday;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getIsCalcWorkday() {
/* 167 */     return this.isCalcWorkday;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsUrgent(Integer isUrgent) {
/* 174 */     this.isUrgent = isUrgent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getIsUrgent() {
/* 182 */     return this.isUrgent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxReminderTimes(Integer maxReminderTimes) {
/* 189 */     this.maxReminderTimes = maxReminderTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getMaxReminderTimes() {
/* 197 */     return this.maxReminderTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderTimes(Integer reminderTimes) {
/* 204 */     this.reminderTimes = reminderTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getReminderTimes() {
/* 212 */     return this.reminderTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReminderCycle(Integer reminderCycle) {
/* 219 */     this.reminderCycle = reminderCycle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getReminderCycle() {
/* 227 */     return this.reminderCycle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDuedate(Date duedate) {
/* 234 */     this.duedate = duedate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDuedate() {
/* 242 */     return this.duedate;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/model/BpmReminderTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */