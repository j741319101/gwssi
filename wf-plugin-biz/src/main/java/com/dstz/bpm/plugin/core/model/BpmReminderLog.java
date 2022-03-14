package com.dstz.bpm.plugin.core.model;

import com.dstz.base.core.model.BaseModel;
import java.util.Date;

public class BpmReminderLog extends BaseModel {
   protected String instanceId;
   protected String reminderTitle;
   protected String subject;
   protected String nodeId;
   protected String nodeName;
   protected String msgType;
   protected String reminderUsers;
   protected String reminderUserids;
   protected Date reminderDate;
   protected String extend;
   private String statue;
   private String feedback;
   private String fromUsers;
   private String fromUserIds;
   private String isUrgent;
   private String taskId;

   public void setInstanceId(String instanceId) {
      this.instanceId = instanceId;
   }

   public String getInstanceId() {
      return this.instanceId;
   }

   public void setReminderTitle(String reminderTitle) {
      this.reminderTitle = reminderTitle;
   }

   public String getReminderTitle() {
      return this.reminderTitle;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getSubject() {
      return this.subject;
   }

   public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
   }

   public String getNodeId() {
      return this.nodeId;
   }

   public void setMsgType(String msgType) {
      this.msgType = msgType;
   }

   public String getMsgType() {
      return this.msgType;
   }

   public void setReminderUsers(String reminderUsers) {
      this.reminderUsers = reminderUsers;
   }

   public String getReminderUsers() {
      return this.reminderUsers;
   }

   public void setReminderUserids(String reminderUserids) {
      this.reminderUserids = reminderUserids;
   }

   public String getReminderUserids() {
      return this.reminderUserids;
   }

   public void setReminderDate(Date reminderDate) {
      this.reminderDate = reminderDate;
   }

   public Date getReminderDate() {
      return this.reminderDate;
   }

   public void setExtend(String extend) {
      this.extend = extend;
   }

   public String getExtend() {
      return this.extend;
   }

   public String getStatue() {
      return this.statue;
   }

   public void setStatue(String statue) {
      this.statue = statue;
   }

   public String getFeedback() {
      return this.feedback;
   }

   public void setFeedback(String feedback) {
      this.feedback = feedback;
   }

   public String getFromUsers() {
      return this.fromUsers;
   }

   public void setFromUsers(String fromUsers) {
      this.fromUsers = fromUsers;
   }

   public String getFromUserIds() {
      return this.fromUserIds;
   }

   public void setFromUserIds(String fromUserIds) {
      this.fromUserIds = fromUserIds;
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public void setNodeName(String nodeName) {
      this.nodeName = nodeName;
   }

   public String getIsUrgent() {
      return this.isUrgent;
   }

   public void setIsUrgent(String isUrgent) {
      this.isUrgent = isUrgent;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }
}
