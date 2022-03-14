package com.dstz.bpm.plugin.core.model;

import com.dstz.base.core.model.BaseModel;
import java.util.Date;

public class BpmReminderTrigger extends BaseModel {
   protected String taskId;
   protected String reminderDesc;
   protected String beforeScript;
   protected String msgType;
   protected String htmlMsg;
   protected String textMsg;
   protected Integer isCalcWorkday;
   protected Integer isUrgent;
   protected Integer maxReminderTimes;
   protected Integer reminderTimes;
   protected Integer reminderCycle;
   protected Date duedate;

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setReminderDesc(String reminderDesc) {
      this.reminderDesc = reminderDesc;
   }

   public String getReminderDesc() {
      return this.reminderDesc;
   }

   public void setBeforeScript(String beforeScript) {
      this.beforeScript = beforeScript;
   }

   public String getBeforeScript() {
      return this.beforeScript;
   }

   public void setMsgType(String msgType) {
      this.msgType = msgType;
   }

   public String getMsgType() {
      return this.msgType;
   }

   public void setHtmlMsg(String htmlMsg) {
      this.htmlMsg = htmlMsg;
   }

   public String getHtmlMsg() {
      return this.htmlMsg;
   }

   public void setTextMsg(String textMsg) {
      this.textMsg = textMsg;
   }

   public String getTextMsg() {
      return this.textMsg;
   }

   public void setIsCalcWorkday(Integer isCalcWorkday) {
      this.isCalcWorkday = isCalcWorkday;
   }

   public Integer getIsCalcWorkday() {
      return this.isCalcWorkday;
   }

   public void setIsUrgent(Integer isUrgent) {
      this.isUrgent = isUrgent;
   }

   public Integer getIsUrgent() {
      return this.isUrgent;
   }

   public void setMaxReminderTimes(Integer maxReminderTimes) {
      this.maxReminderTimes = maxReminderTimes;
   }

   public Integer getMaxReminderTimes() {
      return this.maxReminderTimes;
   }

   public void setReminderTimes(Integer reminderTimes) {
      this.reminderTimes = reminderTimes;
   }

   public Integer getReminderTimes() {
      return this.reminderTimes;
   }

   public void setReminderCycle(Integer reminderCycle) {
      this.reminderCycle = reminderCycle;
   }

   public Integer getReminderCycle() {
      return this.reminderCycle;
   }

   public void setDuedate(Date duedate) {
      this.duedate = duedate;
   }

   public Date getDuedate() {
      return this.duedate;
   }
}
