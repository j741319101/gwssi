/*     */ package com.dstz.bpm.plugin.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
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
/*     */ public class BpmLeaderTaskLog
/*     */   extends BaseModel
/*     */ {
/*     */   @ApiModelProperty("ID")
/*     */   private String id;
/*     */   @ApiModelProperty("任务ID")
/*     */   private String taskId;
/*     */   @ApiModelProperty("流程实例ID")
/*     */   private String instId;
/*     */   @ApiModelProperty("领导ID")
/*     */   private String leaderId;
/*     */   @ApiModelProperty("领导名称")
/*     */   private String leaderName;
/*     */   @ApiModelProperty("办理人id")
/*     */   private String approverId;
/*     */   @ApiModelProperty("办理人名称")
/*     */   private String approverName;
/*     */   private String orgId;
/*     */   @ApiModelProperty("标识 0：未呈送，1：呈送，2：返回到秘书")
/*  49 */   private String flag = "0";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("类型 1：待办，2：抄送")
/*  55 */   private String type = "1";
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("创建人")
/*     */   private String createBy;
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("创建时间")
/*     */   private Date createTime;
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("更新人")
/*     */   private String updateBy;
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("更新时间")
/*     */   private Date updateTime;
/*     */ 
/*     */   
/*  78 */   private String status = "running";
/*     */   
/*     */   public String getId() {
/*  81 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  85 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/*  89 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  93 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public String getInstId() {
/*  97 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/* 101 */     this.instId = instId;
/*     */   }
/*     */   
/*     */   public String getLeaderId() {
/* 105 */     return this.leaderId;
/*     */   }
/*     */   
/*     */   public void setLeaderId(String leaderId) {
/* 109 */     this.leaderId = leaderId;
/*     */   }
/*     */   
/*     */   public String getLeaderName() {
/* 113 */     return this.leaderName;
/*     */   }
/*     */   
/*     */   public void setLeaderName(String leaderName) {
/* 117 */     this.leaderName = leaderName;
/*     */   }
/*     */   
/*     */   public String getApproverId() {
/* 121 */     return this.approverId;
/*     */   }
/*     */   
/*     */   public void setApproverId(String approverId) {
/* 125 */     this.approverId = approverId;
/*     */   }
/*     */   
/*     */   public String getApproverName() {
/* 129 */     return this.approverName;
/*     */   }
/*     */   
/*     */   public void setApproverName(String approverName) {
/* 133 */     this.approverName = approverName;
/*     */   }
/*     */   
/*     */   public String getFlag() {
/* 137 */     return this.flag;
/*     */   }
/*     */   
/*     */   public void setFlag(String flag) {
/* 141 */     this.flag = flag;
/*     */   }
/*     */   
/*     */   public String getType() {
/* 145 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 149 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 154 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 159 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 164 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 169 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 174 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 179 */     this.updateBy = updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 184 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 189 */     this.updateTime = updateTime;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 193 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 197 */     this.status = status;
/*     */   }
/*     */   
/*     */   public String getOrgId() {
/* 201 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 205 */     this.orgId = orgId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/model/BpmLeaderTaskLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */