package com.dstz.bpm.plugin.core.model;

import com.dstz.base.api.model.IBaseModel;
import java.util.Date;

public class BpmUserAgencyLog implements IBaseModel {
   private static final long serialVersionUID = 1133823063010551897L;
   private String id;
   private String configId;
   private String flowInstanceId;
   private String taskId;
   private String taskNodeId;
   private String taskNodeName;
   private String createBy;
   private Date createTime;
   private String createOrgId;
   private String updateBy;
   private Date updateTime;
   private Integer rev;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getConfigId() {
      return this.configId;
   }

   public void setConfigId(String configId) {
      this.configId = configId;
   }

   public String getFlowInstanceId() {
      return this.flowInstanceId;
   }

   public void setFlowInstanceId(String flowInstanceId) {
      this.flowInstanceId = flowInstanceId;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }

   public String getTaskNodeId() {
      return this.taskNodeId;
   }

   public void setTaskNodeId(String taskNodeId) {
      this.taskNodeId = taskNodeId;
   }

   public String getTaskNodeName() {
      return this.taskNodeName;
   }

   public void setTaskNodeName(String taskNodeName) {
      this.taskNodeName = taskNodeName;
   }

   public String getCreateBy() {
      return this.createBy;
   }

   public void setCreateBy(String createBy) {
      this.createBy = createBy;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getCreateOrgId() {
      return this.createOrgId;
   }

   public void setCreateOrgId(String createOrgId) {
      this.createOrgId = createOrgId;
   }

   public String getUpdateBy() {
      return this.updateBy;
   }

   public void setUpdateBy(String updateBy) {
      this.updateBy = updateBy;
   }

   public Date getUpdateTime() {
      return this.updateTime;
   }

   public void setUpdateTime(Date updateTime) {
      this.updateTime = updateTime;
   }

   public Integer getRev() {
      return this.rev;
   }

   public void setRev(Integer rev) {
      this.rev = rev;
   }
}
