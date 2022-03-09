/*     */ package com.dstz.bpm.core.model;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
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
/*     */ public class TaskIdentityLink
/*     */   implements Serializable
/*     */ {
/*     */   public static final String RIGHT_TYPE_USER = "user";
/*     */   protected String id;
/*     */   protected String taskId;
/*     */   protected String instId;
/*     */   protected String type;
/*     */   protected String identityName;
/*     */   protected String identity;
/*     */   protected String permissionCode;
/*     */   private String checkStatus;
/*     */   private Date checkTime;
/*     */   protected String taskType;
/*     */   protected String orgId;
/*     */   
/*     */   public void setId(String id) {
/*  69 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  77 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  81 */     this.taskId = taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskId() {
/*  89 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/*  93 */     this.instId = instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstId() {
/* 101 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 105 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 113 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setIdentityName(String identityName) {
/* 117 */     this.identityName = identityName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIdentityName() {
/* 125 */     return this.identityName;
/*     */   }
/*     */   
/*     */   public void setIdentity(String identity) {
/* 129 */     this.identity = identity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIdentity() {
/* 137 */     return this.identity;
/*     */   }
/*     */   
/*     */   public void setPermissionCode(String permissionCode) {
/* 141 */     this.permissionCode = permissionCode;
/*     */   }
/*     */   
/*     */   public String getCheckStatus() {
/* 145 */     return this.checkStatus;
/*     */   }
/*     */   
/*     */   public void setCheckStatus(String checkStatus) {
/* 149 */     this.checkStatus = checkStatus;
/*     */   }
/*     */   
/*     */   public Date getCheckTime() {
/* 153 */     return this.checkTime;
/*     */   }
/*     */   
/*     */   public void setCheckTime(Date checkTime) {
/* 157 */     this.checkTime = checkTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPermissionCode() {
/* 165 */     return this.permissionCode;
/*     */   }
/*     */   
/*     */   public String getTaskType() {
/* 169 */     return this.taskType;
/*     */   }
/*     */   
/*     */   public void setTaskType(String taskType) {
/* 173 */     this.taskType = taskType;
/*     */   }
/*     */   
/*     */   public String getOrgId() {
/* 177 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 181 */     this.orgId = orgId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 188 */     return (new ToStringBuilder(this))
/* 189 */       .append("id", this.id)
/* 190 */       .append("taskId", this.taskId)
/* 191 */       .append("instId", this.instId)
/* 192 */       .append("type", this.type)
/* 193 */       .append("identityName", this.identityName)
/* 194 */       .append("identity", this.identity)
/* 195 */       .append("permissionCode", this.permissionCode)
/* 196 */       .toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/TaskIdentityLink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */