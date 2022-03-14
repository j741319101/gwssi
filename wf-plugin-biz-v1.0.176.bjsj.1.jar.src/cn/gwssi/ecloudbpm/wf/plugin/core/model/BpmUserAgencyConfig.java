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
/*     */ public class BpmUserAgencyConfig
/*     */   implements IBaseModel
/*     */ {
/*     */   private static final long serialVersionUID = -5472201936456269888L;
/*     */   private String id;
/*     */   private Date startDatetime;
/*     */   private Date endDatetime;
/*     */   private String agencyFlowKey;
/*     */   private String agencyFlowName;
/*     */   private String configUserId;
/*     */   private String targetUserId;
/*     */   private String targetUserName;
/*  59 */   private Boolean enable = Boolean.valueOf(true);
/*     */ 
/*     */ 
/*     */   
/*     */   private String createBy;
/*     */ 
/*     */ 
/*     */   
/*     */   private Date createTime;
/*     */ 
/*     */ 
/*     */   
/*     */   private String createOrgId;
/*     */ 
/*     */ 
/*     */   
/*     */   private String updateBy;
/*     */ 
/*     */   
/*     */   private Date updateTime;
/*     */ 
/*     */   
/*     */   private String comment;
/*     */ 
/*     */   
/*     */   private String configUserName;
/*     */ 
/*     */   
/*     */   private Integer rev;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  92 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/*  97 */     this.id = id;
/*     */   }
/*     */   
/*     */   public Date getStartDatetime() {
/* 101 */     return this.startDatetime;
/*     */   }
/*     */   
/*     */   public void setStartDatetime(Date startDatetime) {
/* 105 */     this.startDatetime = startDatetime;
/*     */   }
/*     */   
/*     */   public Date getEndDatetime() {
/* 109 */     return this.endDatetime;
/*     */   }
/*     */   
/*     */   public void setEndDatetime(Date endDatetime) {
/* 113 */     this.endDatetime = endDatetime;
/*     */   }
/*     */   
/*     */   public String getAgencyFlowKey() {
/* 117 */     return this.agencyFlowKey;
/*     */   }
/*     */   
/*     */   public void setAgencyFlowKey(String agencyFlowKey) {
/* 121 */     this.agencyFlowKey = agencyFlowKey;
/*     */   }
/*     */   
/*     */   public String getAgencyFlowName() {
/* 125 */     return this.agencyFlowName;
/*     */   }
/*     */   
/*     */   public void setAgencyFlowName(String agencyFlowName) {
/* 129 */     this.agencyFlowName = agencyFlowName;
/*     */   }
/*     */   
/*     */   public String getConfigUserId() {
/* 133 */     return this.configUserId;
/*     */   }
/*     */   
/*     */   public void setConfigUserId(String configUserId) {
/* 137 */     this.configUserId = configUserId;
/*     */   }
/*     */   
/*     */   public String getTargetUserId() {
/* 141 */     return this.targetUserId;
/*     */   }
/*     */   
/*     */   public void setTargetUserId(String targetUserId) {
/* 145 */     this.targetUserId = targetUserId;
/*     */   }
/*     */   
/*     */   public String getTargetUserName() {
/* 149 */     return this.targetUserName;
/*     */   }
/*     */   
/*     */   public void setTargetUserName(String targetUserName) {
/* 153 */     this.targetUserName = targetUserName;
/*     */   }
/*     */   
/*     */   public Boolean getEnable() {
/* 157 */     return this.enable;
/*     */   }
/*     */   
/*     */   public void setEnable(Boolean enable) {
/* 161 */     this.enable = enable;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 166 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 171 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 176 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 181 */     this.createTime = createTime;
/*     */   }
/*     */   
/*     */   public String getCreateOrgId() {
/* 185 */     return this.createOrgId;
/*     */   }
/*     */   
/*     */   public void setCreateOrgId(String createOrgId) {
/* 189 */     this.createOrgId = createOrgId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 194 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 199 */     this.updateBy = updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 204 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 209 */     this.updateTime = updateTime;
/*     */   }
/*     */   
/*     */   public Integer getRev() {
/* 213 */     return this.rev;
/*     */   }
/*     */   
/*     */   public void setRev(Integer rev) {
/* 217 */     this.rev = rev;
/*     */   }
/*     */   
/*     */   public String getConfigUserName() {
/* 221 */     return this.configUserName;
/*     */   }
/*     */   
/*     */   public void setConfigUserName(String configUserName) {
/* 225 */     this.configUserName = configUserName;
/*     */   }
/*     */   
/*     */   public String getComment() {
/* 229 */     return this.comment;
/*     */   }
/*     */   
/*     */   public void setComment(String comment) {
/* 233 */     this.comment = comment;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/model/BpmUserAgencyConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */