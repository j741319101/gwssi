package com.dstz.bpm.plugin.vo;

import java.io.Serializable;
import java.util.Date;

public class BpmUserReceiveCarbonCopyRecordVO implements Serializable {
   private static final long serialVersionUID = -8661005770159558772L;
   private String id;
   private String recordId;
   private String subject;
   private String nodeId;
   private String nodeName;
   private String instId;
   private String taskId;
   private String formType;
   private String event;
   private String triggerUserId;
   private String triggerUserName;
   private Date receiveTime;
   private Date updateTime;
   private Boolean read;
   private String receiveUserName;
   private String docNum;
   private String zbsj;
   private String taskNames;
   private String taskUsers;
   private String status;
   private String defName;
   private String creator;
   private String nodeTypeName;
   private String nodeTypeKey;
   private String instCreateTime;
   private String content;
   private String sendLeaderFlag;

   public String getSendLeaderFlag() {
      return this.sendLeaderFlag;
   }

   public void setSendLeaderFlag(String sendLeaderFlag) {
      this.sendLeaderFlag = sendLeaderFlag;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public String getInstCreateTime() {
      return this.instCreateTime;
   }

   public void setInstCreateTime(String instCreateTime) {
      this.instCreateTime = instCreateTime;
   }

   public String getNodeTypeKey() {
      return this.nodeTypeKey;
   }

   public void setNodeTypeKey(String nodeTypeKey) {
      this.nodeTypeKey = nodeTypeKey;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getRecordId() {
      return this.recordId;
   }

   public void setRecordId(String recordId) {
      this.recordId = recordId;
   }

   public String getSubject() {
      return this.subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
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

   public Date getReceiveTime() {
      return this.receiveTime;
   }

   public void setReceiveTime(Date receiveTime) {
      this.receiveTime = receiveTime;
   }

   public Boolean getRead() {
      return this.read;
   }

   public void setRead(Boolean read) {
      this.read = read;
   }

   public String getDocNum() {
      return this.docNum;
   }

   public void setDocNum(String docNum) {
      this.docNum = docNum;
   }

   public String getZbsj() {
      return this.zbsj;
   }

   public void setZbsj(String zbsj) {
      this.zbsj = zbsj;
   }

   public String getTaskNames() {
      return this.taskNames;
   }

   public void setTaskNames(String taskNames) {
      this.taskNames = taskNames;
   }

   public String getTaskUsers() {
      return this.taskUsers;
   }

   public void setTaskUsers(String taskUsers) {
      this.taskUsers = taskUsers;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getDefName() {
      return this.defName;
   }

   public void setDefName(String defName) {
      this.defName = defName;
   }

   public String getCreator() {
      return this.creator;
   }

   public void setCreator(String creator) {
      this.creator = creator;
   }

   public String getNodeTypeName() {
      return this.nodeTypeName;
   }

   public void setNodeTypeName(String nodeTypeName) {
      this.nodeTypeName = nodeTypeName;
   }

   public String getReceiveUserName() {
      return this.receiveUserName;
   }

   public void setReceiveUserName(String receiveUserName) {
      this.receiveUserName = receiveUserName;
   }

   public Date getUpdateTime() {
      return this.updateTime;
   }

   public void setUpdateTime(Date updateTime) {
      this.updateTime = updateTime;
   }

   public String toString() {
      return "BpmUserReceiveCarbonCopyRecordVO{id='" + this.id + '\'' + ", recordId='" + this.recordId + '\'' + ", subject='" + this.subject + '\'' + ", nodeId='" + this.nodeId + '\'' + ", nodeName='" + this.nodeName + '\'' + ", instId='" + this.instId + '\'' + ", taskId='" + this.taskId + '\'' + ", formType='" + this.formType + '\'' + ", event='" + this.event + '\'' + ", triggerUserId='" + this.triggerUserId + '\'' + ", triggerUserName='" + this.triggerUserName + '\'' + ", receiveTime=" + this.receiveTime + ", read=" + this.read + '}';
   }
}
