/*     */ package com.dstz.base.core.model;
/*     */ 
/*     */ import com.dstz.base.api.model.IGBaseModel;
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
/*     */ public abstract class GBaseModel
/*     */   extends ToStringUtil
/*     */   implements IGBaseModel
/*     */ {
/*     */   protected String id;
/*     */   protected Date createTime;
/*     */   protected String createBy;
/*     */   protected Date updateTime;
/*     */   protected String updateBy;
/*  33 */   protected int version = 0;
/*     */   
/*     */   protected boolean delete = false;
/*     */   
/*     */   private String createUser;
/*     */   
/*     */   private String updateUser;
/*     */ 
/*     */   
/*     */   public String getId() {
/*  43 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/*  48 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/*  53 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/*  58 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/*  63 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/*  68 */     this.createBy = createBy;
/*     */   }
/*     */   
/*     */   public Date getUpdateTime() {
/*  72 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/*  77 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/*  82 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/*  87 */     this.updateBy = updateBy;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/*  91 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(int version) {
/*  95 */     this.version = version;
/*     */   }
/*     */   
/*     */   public boolean isDelete() {
/*  99 */     return this.delete;
/*     */   }
/*     */   
/*     */   public void setDelete(boolean delete) {
/* 103 */     this.delete = delete;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateUser() {
/* 109 */     return this.createUser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateUser(String createUser) {
/* 114 */     this.createUser = createUser;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateUser() {
/* 119 */     return this.updateUser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateUser(String updateUser) {
/* 124 */     this.updateUser = updateUser;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     return JSONObject.toJSONString(this);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/model/GBaseModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */