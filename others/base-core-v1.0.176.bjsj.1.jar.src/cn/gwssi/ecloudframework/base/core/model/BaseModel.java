/*     */ package com.dstz.base.core.model;
/*     */ 
/*     */ import com.dstz.base.api.model.IBaseModel;
/*     */ import com.dstz.base.core.util.ToStringUtil;
/*     */ import com.alibaba.fastjson.JSONObject;
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
/*     */ public abstract class BaseModel
/*     */   extends ToStringUtil
/*     */   implements IBaseModel
/*     */ {
/*     */   protected String id;
/*     */   protected Date createTime;
/*     */   protected String createBy;
/*     */   protected String createUser;
/*     */   protected Date updateTime;
/*     */   protected String updateBy;
/*     */   protected String updateUser;
/*     */   protected Integer version;
/*     */   protected Boolean delete;
/*     */   
/*     */   public String getId() {
/*  60 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/*  65 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/*  70 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/*  75 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/*  80 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/*  85 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/*  90 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/*  95 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 100 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 105 */     this.updateBy = updateBy;
/*     */   }
/*     */   
/*     */   public Integer getVersion() {
/* 109 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(Integer version) {
/* 113 */     this.version = version;
/*     */   }
/*     */   
/*     */   public Boolean getDelete() {
/* 117 */     return this.delete;
/*     */   }
/*     */   
/*     */   public void setDelete(Boolean delete) {
/* 121 */     this.delete = delete;
/*     */   }
/*     */   
/*     */   public String getCreateUser() {
/* 125 */     return this.createUser;
/*     */   }
/*     */   
/*     */   public void setCreateUser(String createUser) {
/* 129 */     this.createUser = createUser;
/*     */   }
/*     */   
/*     */   public String getUpdateUser() {
/* 133 */     return this.updateUser;
/*     */   }
/*     */   
/*     */   public void setUpdateUser(String updateUser) {
/* 137 */     this.updateUser = updateUser;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 142 */     return JSONObject.toJSONString(this);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/model/BaseModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */