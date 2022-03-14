package com.dstz.bpm.plugin.core.model;

import com.dstz.base.core.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

public class BpmLeaderTaskLog extends BaseModel {
   @ApiModelProperty("ID")
   private String id;
   @ApiModelProperty("任务ID")
   private String taskId;
   @ApiModelProperty("流程实例ID")
   private String instId;
   @ApiModelProperty("领导ID")
   private String leaderId;
   @ApiModelProperty("领导名称")
   private String leaderName;
   @ApiModelProperty("办理人id")
   private String approverId;
   @ApiModelProperty("办理人名称")
   private String approverName;
   private String orgId;
   @ApiModelProperty("标识 0：未呈送，1：呈送，2：返回到秘书")
   private String flag = "0";
   @ApiModelProperty("类型 1：待办，2：抄送")
   private String type = "1";
   @ApiModelProperty("创建人")
   private String createBy;
   @ApiModelProperty("创建时间")
   private Date createTime;
   @ApiModelProperty("更新人")
   private String updateBy;
   @ApiModelProperty("更新时间")
   private Date updateTime;
   private String status = "running";

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }

   public String getInstId() {
      return this.instId;
   }

   public void setInstId(String instId) {
      this.instId = instId;
   }

   public String getLeaderId() {
      return this.leaderId;
   }

   public void setLeaderId(String leaderId) {
      this.leaderId = leaderId;
   }

   public String getLeaderName() {
      return this.leaderName;
   }

   public void setLeaderName(String leaderName) {
      this.leaderName = leaderName;
   }

   public String getApproverId() {
      return this.approverId;
   }

   public void setApproverId(String approverId) {
      this.approverId = approverId;
   }

   public String getApproverName() {
      return this.approverName;
   }

   public void setApproverName(String approverName) {
      this.approverName = approverName;
   }

   public String getFlag() {
      return this.flag;
   }

   public void setFlag(String flag) {
      this.flag = flag;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
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

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getOrgId() {
      return this.orgId;
   }

   public void setOrgId(String orgId) {
      this.orgId = orgId;
   }
}
