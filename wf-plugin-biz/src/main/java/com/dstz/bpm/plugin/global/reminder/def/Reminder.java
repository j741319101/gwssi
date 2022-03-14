package com.dstz.bpm.plugin.global.reminder.def;

import java.io.Serializable;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotBlank;

public class Reminder implements Serializable {
   private String nodeId;
   @NotBlank(
      message = "催办配置描述不能为空"
   )
   private String desc;
   private String conditionScript;
   @Min(
      value = 3L,
      message = "任务超时，请最少设置3分钟"
   )
   private Integer timeLimit;
   @Min(1L)
   private int maxReminderTimes = 1;
   private int reminderCycle = 43200;
   private Boolean isCalcWorkDay = true;
   private Boolean isUrgent = true;
   private String beforeScript;
   @NotBlank
   private String msgType;
   private String htmlTemplate;
   private String textTemplate;

   public String getNodeId() {
      return this.nodeId;
   }

   public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public int getMaxReminderTimes() {
      return this.maxReminderTimes;
   }

   public void setMaxReminderTimes(int maxReminderTimes) {
      this.maxReminderTimes = maxReminderTimes;
   }

   public Boolean getIsCalcWorkDay() {
      return this.isCalcWorkDay;
   }

   public void setIsCalcWorkDay(Boolean isCalcWorkDay) {
      this.isCalcWorkDay = isCalcWorkDay;
   }

   public Boolean getIsUrgent() {
      return this.isUrgent;
   }

   public void setIsUrgent(Boolean isUrgent) {
      this.isUrgent = isUrgent;
   }

   public String getConditionScript() {
      return this.conditionScript;
   }

   public void setConditionScript(String conditionScript) {
      this.conditionScript = conditionScript;
   }

   public String getMsgType() {
      return this.msgType;
   }

   public void setMsgType(String msgType) {
      this.msgType = msgType;
   }

   public String getHtmlTemplate() {
      return this.htmlTemplate;
   }

   public void setHtmlTemplate(String htmlTemplate) {
      this.htmlTemplate = htmlTemplate;
   }

   public String getTextTemplate() {
      return this.textTemplate;
   }

   public int getReminderCycle() {
      return this.reminderCycle;
   }

   public void setReminderCycle(int reminderCycle) {
      this.reminderCycle = reminderCycle;
   }

   public Integer getTimeLimit() {
      return this.timeLimit;
   }

   public void setTimeLimit(Integer timeLimit) {
      this.timeLimit = timeLimit;
   }

   public void setTextTemplate(String textTemplate) {
      this.textTemplate = textTemplate;
   }

   public String getBeforeScript() {
      return this.beforeScript;
   }

   public void setBeforeScript(String beforeScript) {
      this.beforeScript = beforeScript;
   }
}
