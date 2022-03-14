/*     */ package com.dstz.bpm.plugin.global.reminder.def;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.validation.constraints.Min;
/*     */ import org.hibernate.validator.constraints.NotBlank;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Reminder
/*     */   implements Serializable
/*     */ {
/*     */   private String nodeId;
/*     */   @NotBlank(message = "催办配置描述不能为空")
/*     */   private String desc;
/*     */   private String conditionScript;
/*     */   @Min(value = 3L, message = "任务超时，请最少设置3分钟")
/*     */   private Integer timeLimit;
/*     */   @Min(1L)
/*  35 */   private int maxReminderTimes = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   private int reminderCycle = 43200;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   private Boolean isCalcWorkDay = Boolean.valueOf(true);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private Boolean isUrgent = Boolean.valueOf(true);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String beforeScript;
/*     */ 
/*     */ 
/*     */   
/*     */   @NotBlank
/*     */   private String msgType;
/*     */ 
/*     */ 
/*     */   
/*     */   private String htmlTemplate;
/*     */ 
/*     */ 
/*     */   
/*     */   private String textTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/*  76 */     return this.nodeId;
/*     */   }
/*     */   public void setNodeId(String nodeId) {
/*  79 */     this.nodeId = nodeId;
/*     */   }
/*     */   public String getDesc() {
/*  82 */     return this.desc;
/*     */   }
/*     */   public void setDesc(String desc) {
/*  85 */     this.desc = desc;
/*     */   }
/*     */   
/*     */   public int getMaxReminderTimes() {
/*  89 */     return this.maxReminderTimes;
/*     */   }
/*     */   public void setMaxReminderTimes(int maxReminderTimes) {
/*  92 */     this.maxReminderTimes = maxReminderTimes;
/*     */   }
/*     */   public Boolean getIsCalcWorkDay() {
/*  95 */     return this.isCalcWorkDay;
/*     */   }
/*     */   public void setIsCalcWorkDay(Boolean isCalcWorkDay) {
/*  98 */     this.isCalcWorkDay = isCalcWorkDay;
/*     */   }
/*     */   public Boolean getIsUrgent() {
/* 101 */     return this.isUrgent;
/*     */   }
/*     */   public void setIsUrgent(Boolean isUrgent) {
/* 104 */     this.isUrgent = isUrgent;
/*     */   }
/*     */   public String getConditionScript() {
/* 107 */     return this.conditionScript;
/*     */   }
/*     */   public void setConditionScript(String conditionScript) {
/* 110 */     this.conditionScript = conditionScript;
/*     */   }
/*     */   public String getMsgType() {
/* 113 */     return this.msgType;
/*     */   }
/*     */   public void setMsgType(String msgType) {
/* 116 */     this.msgType = msgType;
/*     */   }
/*     */   public String getHtmlTemplate() {
/* 119 */     return this.htmlTemplate;
/*     */   }
/*     */   public void setHtmlTemplate(String htmlTemplate) {
/* 122 */     this.htmlTemplate = htmlTemplate;
/*     */   }
/*     */   public String getTextTemplate() {
/* 125 */     return this.textTemplate;
/*     */   }
/*     */   public int getReminderCycle() {
/* 128 */     return this.reminderCycle;
/*     */   }
/*     */   public void setReminderCycle(int reminderCycle) {
/* 131 */     this.reminderCycle = reminderCycle;
/*     */   }
/*     */   public Integer getTimeLimit() {
/* 134 */     return this.timeLimit;
/*     */   }
/*     */   public void setTimeLimit(Integer timeLimit) {
/* 137 */     this.timeLimit = timeLimit;
/*     */   }
/*     */   public void setTextTemplate(String textTemplate) {
/* 140 */     this.textTemplate = textTemplate;
/*     */   }
/*     */   public String getBeforeScript() {
/* 143 */     return this.beforeScript;
/*     */   }
/*     */   public void setBeforeScript(String beforeScript) {
/* 146 */     this.beforeScript = beforeScript;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/reminder/def/Reminder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */