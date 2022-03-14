package com.dstz.bpm.plugin.core.model;

import com.dstz.base.core.model.BaseModel;

public class BpmCarbonCopyRecord extends BaseModel {
   private static final long serialVersionUID = 3978044694562910321L;
   private String instId;
   private String taskId;
   private String nodeId;
   private String nodeName;
   private String formType;
   private String event;
   private String triggerUserId;
   private String triggerUserName;
   private String subject;
   private String content;
   private Integer rev;

   public String getInstId() {
      return this.instId;
   }

   public void setInstId(String instId) {
      this.instId = instId;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }

   public String getNodeId() {
      return this.nodeId;
   }

   public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public void setNodeName(String nodeName) {
      this.nodeName = nodeName;
   }

   public String getFormType() {
      return this.formType;
   }

   public void setFormType(String formType) {
      this.formType = formType;
   }

   public String getEvent() {
      return this.event;
   }

   public void setEvent(String event) {
      this.event = event;
   }

   public String getTriggerUserId() {
      return this.triggerUserId;
   }

   public void setTriggerUserId(String triggerUserId) {
      this.triggerUserId = triggerUserId;
   }

   public String getTriggerUserName() {
      return this.triggerUserName;
   }

   public void setTriggerUserName(String triggerUserName) {
      this.triggerUserName = triggerUserName;
   }

   public String getSubject() {
      return this.subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public Integer getRev() {
      return this.rev;
   }

   public void setRev(Integer rev) {
      this.rev = rev;
   }
}
