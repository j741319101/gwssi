/*     */ package com.dstz.bpm.core.model;
/*     */ 
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.base.api.model.IBaseModel;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.alibaba.fastjson.annotation.JSONField;
/*     */ import java.util.Date;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ public class BpmInstance
/*     */   implements IBaseModel, IBpmInstance
/*     */ {
/*     */   protected String id;
/*     */   protected String subject;
/*     */   protected String defId;
/*     */   protected String actDefId;
/*     */   protected String defKey;
/*     */   protected String defName;
/*     */   protected String bizKey;
/*     */   protected String status;
/*     */   protected Date endTime;
/*     */   protected Long duration;
/*     */   protected String typeId;
/*     */   protected String actInstId;
/*     */   protected String createBy;
/*     */   protected String creator;
/*     */   protected Date createTime;
/*     */   protected String createOrgId;
/*     */   protected String updateBy;
/*     */   protected Date updateTime;
/*     */   protected String isFormmal;
/*     */   protected String parentInstId;
/* 126 */   protected Short isForbidden = Short.valueOf((short)0);
/*     */ 
/*     */ 
/*     */   
/*     */   protected String dataMode;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer supportMobile;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String superNodeId;
/*     */ 
/*     */ 
/*     */   
/*     */   @JSONField(serialize = false)
/* 143 */   protected Boolean isTestData = Boolean.valueOf(false);
/*     */   
/*     */   protected transient boolean hasCreate = true;
/*     */   
/*     */   protected transient boolean hasUpdate = false;
/*     */   
/*     */   protected String taskNames;
/*     */   
/*     */   protected String taskUsers;
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/* 155 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 163 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setSubject(String subject) {
/* 167 */     this.subject = subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubject() {
/* 175 */     return this.subject;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/* 179 */     this.defId = defId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefId() {
/* 187 */     return this.defId;
/*     */   }
/*     */   
/*     */   public void setActDefId(String actDefId) {
/* 191 */     this.actDefId = actDefId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActDefId() {
/* 199 */     return this.actDefId;
/*     */   }
/*     */   
/*     */   public void setDefKey(String defKey) {
/* 203 */     this.defKey = defKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefKey() {
/* 211 */     return this.defKey;
/*     */   }
/*     */   
/*     */   public void setDefName(String defName) {
/* 215 */     this.defName = defName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefName() {
/* 223 */     return this.defName;
/*     */   }
/*     */   
/*     */   public void setBizKey(String bizKey) {
/* 227 */     this.bizKey = bizKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBizKey() {
/* 235 */     if (this.bizKey == null) {
/* 236 */       return "";
/*     */     }
/* 238 */     return this.bizKey;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 242 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 250 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setEndTime(Date endTime) {
/* 254 */     this.endTime = endTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getEndTime() {
/* 262 */     return this.endTime;
/*     */   }
/*     */   
/*     */   public void setDuration(Long duration) {
/* 266 */     this.duration = duration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getDuration() {
/* 274 */     return this.duration;
/*     */   }
/*     */   
/*     */   public void setTypeId(String typeId) {
/* 278 */     this.typeId = typeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeId() {
/* 286 */     return this.typeId;
/*     */   }
/*     */   
/*     */   public void setActInstId(String actInstId) {
/* 290 */     this.actInstId = actInstId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActInstId() {
/* 298 */     return this.actInstId;
/*     */   }
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 302 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 310 */     return this.createBy;
/*     */   }
/*     */   
/*     */   public void setCreator(String creator) {
/* 314 */     this.creator = creator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreator() {
/* 322 */     return this.creator;
/*     */   }
/*     */   
/*     */   public boolean isHasUpdate() {
/* 326 */     return this.hasUpdate;
/*     */   }
/*     */   
/*     */   public void setHasUpdate(boolean hasUpdate) {
/* 330 */     this.hasUpdate = hasUpdate;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 334 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 342 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setCreateOrgId(String createOrgId) {
/* 346 */     this.createOrgId = createOrgId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateOrgId() {
/* 354 */     return this.createOrgId;
/*     */   }
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 358 */     this.updateBy = updateBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 366 */     return this.updateBy;
/*     */   }
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 370 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 378 */     return this.updateTime;
/*     */   }
/*     */   
/*     */   public void setIsFormmal(String isFormmal) {
/* 382 */     this.isFormmal = isFormmal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIsFormmal() {
/* 390 */     return this.isFormmal;
/*     */   }
/*     */   
/*     */   public void setParentInstId(String parentInstId) {
/* 394 */     this.parentInstId = parentInstId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentInstId() {
/* 402 */     return this.parentInstId;
/*     */   }
/*     */   
/*     */   public void setIsForbidden(Short isForbidden) {
/* 406 */     this.isForbidden = isForbidden;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Short getIsForbidden() {
/* 414 */     return this.isForbidden;
/*     */   }
/*     */   
/*     */   public void setDataMode(String dataMode) {
/* 418 */     this.dataMode = dataMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDataMode() {
/* 426 */     return this.dataMode;
/*     */   }
/*     */   
/*     */   public void setSupportMobile(Integer supportMobile) {
/* 430 */     this.supportMobile = supportMobile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getSupportMobile() {
/* 438 */     return this.supportMobile;
/*     */   }
/*     */   
/*     */   public void setSuperNodeId(String superNodeId) {
/* 442 */     this.superNodeId = superNodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSuperNodeId() {
/* 450 */     return this.superNodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 457 */     return (new ToStringBuilder(this))
/* 458 */       .append("id", this.id)
/* 459 */       .append("subject", this.subject)
/* 460 */       .append("defId", this.defId)
/* 461 */       .append("actDefId", this.actDefId)
/* 462 */       .append("defKey", this.defKey)
/* 463 */       .append("defName", this.defName)
/* 464 */       .append("bizKey", this.bizKey)
/* 465 */       .append("status", this.status)
/* 466 */       .append("endTime", this.endTime)
/* 467 */       .append("duration", this.duration)
/* 468 */       .append("typeId", this.typeId)
/* 469 */       .append("actInstId", this.actInstId)
/* 470 */       .append("createBy", this.createBy)
/* 471 */       .append("creator", this.creator)
/* 472 */       .append("createTime", this.createTime)
/* 473 */       .append("createOrgId", this.createOrgId)
/* 474 */       .append("updateBy", this.updateBy)
/* 475 */       .append("updateTime", this.updateTime)
/* 476 */       .append("isFormmal", this.isFormmal)
/* 477 */       .append("parentInstId", this.parentInstId)
/* 478 */       .append("isForbidden", this.isForbidden)
/* 479 */       .append("dataMode", this.dataMode)
/* 480 */       .append("supportMobile", this.supportMobile)
/* 481 */       .append("superNodeId", this.superNodeId)
/* 482 */       .toString();
/*     */   }
/*     */   
/*     */   public String getTaskNames() {
/* 486 */     return this.taskNames;
/*     */   }
/*     */   
/*     */   public void setTaskNames(String taskNames) {
/* 490 */     this.taskNames = taskNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean hasCreate() {
/* 495 */     return Boolean.valueOf(this.hasCreate);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHasCreate(Boolean hasCreate) {
/* 500 */     this.hasCreate = hasCreate.booleanValue();
/*     */   }
/*     */   
/*     */   public String getTaskUsers() {
/* 504 */     return this.taskUsers;
/*     */   }
/*     */   
/*     */   public void setTaskUsers(String taskUsers) {
/* 508 */     this.taskUsers = taskUsers;
/*     */   }
/*     */   
/*     */   public Boolean isTestData() {
/* 512 */     return this.isTestData;
/*     */   }
/*     */   
/*     */   public void setTestData(Boolean testData) {
/* 516 */     this.isTestData = testData;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 521 */     if (this == o) return true; 
/* 522 */     if (o == null || getClass() != o.getClass()) return false; 
/* 523 */     BpmInstance that = (BpmInstance)o;
/* 524 */     return StringUtils.equals(this.id, that.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 529 */     return StringUtil.isNotEmpty(this.id) ? this.id.hashCode() : 0;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */