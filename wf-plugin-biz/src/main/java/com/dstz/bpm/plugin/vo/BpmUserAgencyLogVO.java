/*     */ package com.dstz.bpm.plugin.vo;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class BpmUserAgencyLogVO
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6725378203602574252L;
/*     */   private String id;
/*     */   private String flowInstanceId;
/*     */   private String flowInstanceName;
/*     */   private String taskKey;
/*     */   private String taskName;
/*     */   private String taskId;
/*     */   private String approverName;
/*     */   private String approveStatus;
/*     */   private String assignInfo;
/*     */   private Date approveTime;
/*     */   private Date createTime;
/*     */   private String configId;
/*     */   private String configUserId;
/*     */   private String configUserName;
/*     */   private String targetUserName;
/*     */   
/*     */   public String getId() {
/*  65 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  69 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getFlowInstanceId() {
/*  73 */     return this.flowInstanceId;
/*     */   }
/*     */   
/*     */   public void setFlowInstanceId(String flowInstanceId) {
/*  77 */     this.flowInstanceId = flowInstanceId;
/*     */   }
/*     */   
/*     */   public String getFlowInstanceName() {
/*  81 */     return this.flowInstanceName;
/*     */   }
/*     */   
/*     */   public void setFlowInstanceName(String flowInstanceName) {
/*  85 */     this.flowInstanceName = flowInstanceName;
/*     */   }
/*     */   
/*     */   public String getTaskKey() {
/*  89 */     return this.taskKey;
/*     */   }
/*     */   
/*     */   public void setTaskKey(String taskKey) {
/*  93 */     this.taskKey = taskKey;
/*     */   }
/*     */   
/*     */   public String getTaskName() {
/*  97 */     return this.taskName;
/*     */   }
/*     */   
/*     */   public void setTaskName(String taskName) {
/* 101 */     this.taskName = taskName;
/*     */   }
/*     */   
/*     */   public String getApproverName() {
/* 105 */     return this.approverName;
/*     */   }
/*     */   
/*     */   public void setApproverName(String approverName) {
/* 109 */     this.approverName = approverName;
/*     */   }
/*     */   
/*     */   public Date getApproveTime() {
/* 113 */     return this.approveTime;
/*     */   }
/*     */   
/*     */   public String getApproveStatus() {
/* 117 */     return this.approveStatus;
/*     */   }
/*     */   
/*     */   public void setApproveStatus(String approveStatus) {
/* 121 */     this.approveStatus = approveStatus;
/*     */   }
/*     */   
/*     */   public void setApproveTime(Date approveTime) {
/* 125 */     this.approveTime = approveTime;
/*     */   }
/*     */   
/*     */   public String getAssignInfo() {
/* 129 */     return this.assignInfo;
/*     */   }
/*     */   
/*     */   public void setAssignInfo(String assignInfo) {
/* 133 */     this.assignInfo = assignInfo;
/*     */   }
/*     */   
/*     */   public Date getCreateTime() {
/* 137 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 141 */     this.createTime = createTime;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 145 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 149 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public String getConfigId() {
/* 153 */     return this.configId;
/*     */   }
/*     */   
/*     */   public void setConfigId(String configId) {
/* 157 */     this.configId = configId;
/*     */   }
/*     */   
/*     */   public String getConfigUserId() {
/* 161 */     return this.configUserId;
/*     */   }
/*     */   
/*     */   public void setConfigUserId(String configUserId) {
/* 165 */     this.configUserId = configUserId;
/*     */   }
/*     */   
/*     */   public String getConfigUserName() {
/* 169 */     return this.configUserName;
/*     */   }
/*     */   
/*     */   public void setConfigUserName(String configUserName) {
/* 173 */     this.configUserName = configUserName;
/*     */   }
/*     */   
/*     */   public String getTargetUserName() {
/* 177 */     return this.targetUserName;
/*     */   }
/*     */   
/*     */   public void setTargetUserName(String targetUserName) {
/* 181 */     this.targetUserName = targetUserName;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/vo/BpmUserAgencyLogVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */