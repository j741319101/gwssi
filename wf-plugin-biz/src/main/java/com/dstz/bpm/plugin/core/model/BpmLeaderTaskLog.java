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
/*     */   @ApiModelProperty("标识 0：未呈送，1：呈送，2：返回到秘书")
/*  47 */   private String flag = "0";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("类型 1：待办，2：抄送")
/*  53 */   private String type = "1";
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("创建人")
/*     */   private String createBy;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("创建时间")
/*     */   private Date createTime;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("更新人")
/*     */   private String updateBy;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("更新时间")
/*     */   private Date updateTime;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("催办日志记录id")
/*     */   private String receiveId;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReceiveId() {
/*  79 */     return this.receiveId;
/*     */   }
/*     */   
/*     */   public void setReceiveId(String receiveId) {
/*  83 */     this.receiveId = receiveId;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  87 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  91 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/*  95 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  99 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public String getInstId() {
/* 103 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/* 107 */     this.instId = instId;
/*     */   }
/*     */   
/*     */   public String getLeaderId() {
/* 111 */     return this.leaderId;
/*     */   }
/*     */   
/*     */   public void setLeaderId(String leaderId) {
/* 115 */     this.leaderId = leaderId;
/*     */   }
/*     */   
/*     */   public String getLeaderName() {
/* 119 */     return this.leaderName;
/*     */   }
/*     */   
/*     */   public void setLeaderName(String leaderName) {
/* 123 */     this.leaderName = leaderName;
/*     */   }
/*     */   
/*     */   public String getApproverId() {
/* 127 */     return this.approverId;
/*     */   }
/*     */   
/*     */   public void setApproverId(String approverId) {
/* 131 */     this.approverId = approverId;
/*     */   }
/*     */   
/*     */   public String getApproverName() {
/* 135 */     return this.approverName;
/*     */   }
/*     */   
/*     */   public void setApproverName(String approverName) {
/* 139 */     this.approverName = approverName;
/*     */   }
/*     */   
/*     */   public String getFlag() {
/* 143 */     return this.flag;
/*     */   }
/*     */   
/*     */   public void setFlag(String flag) {
/* 147 */     this.flag = flag;
/*     */   }
/*     */   
/*     */   public String getType() {
/* 151 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 155 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 160 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 165 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 170 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 175 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 180 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 185 */     this.updateBy = updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 190 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 195 */     this.updateTime = updateTime;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/model/BpmLeaderTaskLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */