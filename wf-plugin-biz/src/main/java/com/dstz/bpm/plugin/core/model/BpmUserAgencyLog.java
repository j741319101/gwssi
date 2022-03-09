/*     */ package com.dstz.bpm.plugin.core.model;
/*     */ 
/*     */ import com.dstz.base.api.model.IBaseModel;
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
/*     */ public class BpmUserAgencyLog
/*     */   implements IBaseModel
/*     */ {
/*     */   private static final long serialVersionUID = 1133823063010551897L;
/*     */   private String id;
/*     */   private String configId;
/*     */   private String flowInstanceId;
/*     */   private String taskId;
/*     */   private String taskNodeId;
/*     */   private String taskNodeName;
/*     */   private String createBy;
/*     */   private Date createTime;
/*     */   private String createOrgId;
/*     */   private String updateBy;
/*     */   private Date updateTime;
/*     */   private Integer rev;
/*     */   
/*     */   public String getId() {
/*  75 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/*  80 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getConfigId() {
/*  84 */     return this.configId;
/*     */   }
/*     */   
/*     */   public void setConfigId(String configId) {
/*  88 */     this.configId = configId;
/*     */   }
/*     */   
/*     */   public String getFlowInstanceId() {
/*  92 */     return this.flowInstanceId;
/*     */   }
/*     */   
/*     */   public void setFlowInstanceId(String flowInstanceId) {
/*  96 */     this.flowInstanceId = flowInstanceId;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 100 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 104 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public String getTaskNodeId() {
/* 108 */     return this.taskNodeId;
/*     */   }
/*     */   
/*     */   public void setTaskNodeId(String taskNodeId) {
/* 112 */     this.taskNodeId = taskNodeId;
/*     */   }
/*     */   
/*     */   public String getTaskNodeName() {
/* 116 */     return this.taskNodeName;
/*     */   }
/*     */   
/*     */   public void setTaskNodeName(String taskNodeName) {
/* 120 */     this.taskNodeName = taskNodeName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 125 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 130 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 135 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 140 */     this.createTime = createTime;
/*     */   }
/*     */   
/*     */   public String getCreateOrgId() {
/* 144 */     return this.createOrgId;
/*     */   }
/*     */   
/*     */   public void setCreateOrgId(String createOrgId) {
/* 148 */     this.createOrgId = createOrgId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 153 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 158 */     this.updateBy = updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 163 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 168 */     this.updateTime = updateTime;
/*     */   }
/*     */   
/*     */   public Integer getRev() {
/* 172 */     return this.rev;
/*     */   }
/*     */   
/*     */   public void setRev(Integer rev) {
/* 176 */     this.rev = rev;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/model/BpmUserAgencyLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */