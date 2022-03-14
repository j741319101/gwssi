package com.dstz.bpm.plugin.vo;

import java.io.Serializable;
import java.util.Date;

public class BpmUserAgencyLogVO implements Serializable {
   private static final long serialVersionUID = -6725378203602574252L;
   private String id;
   private String flowInstanceId;
   private String flowInstanceName;
   private String taskKey;
   private String taskName;
   private String taskId;
   private String approverName;
   private String approveStatus;
   private String assignInfo;
   private Date approveTime;
   private Date createTime;
   private String configId;
   private String configUserId;
   private String configUserName;
   private String targetUserName;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getFlowInstanceId() {
      return this.flowInstanceId;
   }

   public void setFlowInstanceId(String flowInstanceId) {
      this.flowInstanceId = flowInstanceId;
   }

   public String getFlowInstanceName() {
      return this.flowInstanceName;
   }

   public void setFlowInstanceName(String flowInstanceName) {
      this.flowInstanceName = flowInstanceName;
   }

   public String getTaskKey() {
      return this.taskKey;
   }

   public void setTaskKey(String taskKey) {
      this.taskKey = taskKey;
   }

   public String getTaskName() {
      return this.taskName;
   }

   public void setTaskName(String taskName) {
      this.taskName = taskName;
   }

   public String getApproverName() {
      return this.approverName;
   }

   public void setApproverName(String approverName) {
      this.approverName = approverName;
   }

   public Date getApproveTime() {
      return this.approveTime;
   }

   public String getApproveStatus() {
      return this.approveStatus;
   }

   public void setApproveStatus(String approveStatus) {
      this.approveStatus = approveStatus;
   }

   public void setApproveTime(Date approveTime) {
      this.approveTime = approveTime;
   }

   public String getAssignInfo() {
      return this.assignInfo;
   }

   public void setAssignInfo(String assignInfo) {
      this.assignInfo = assignInfo;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }

   public String getConfigId() {
      return this.configId;
   }

   public void setConfigId(String configId) {
      this.configId = configId;
   }

   public String getConfigUserId() {
      return this.configUserId;
   }

   public void setConfigUserId(String configUserId) {
      this.configUserId = configUserId;
   }

   public String getConfigUserName() {
      return this.configUserName;
   }

   public void setConfigUserName(String configUserName) {
      this.configUserName = configUserName;
   }

   public String getTargetUserName() {
      return this.targetUserName;
   }

   public void setTargetUserName(String targetUserName) {
      this.targetUserName = targetUserName;
   }
}
