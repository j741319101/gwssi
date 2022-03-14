/*     */ package com.dstz.sys.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
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
/*     */ public class SysAuthorization
/*     */   extends BaseModel
/*     */ {
/*     */   public static final String RIGHT_TYPE_USER = "user";
/*     */   public static final String RIGHT_TYPE_ALL = "all";
/*     */   protected String rightsId;
/*     */   protected String rightsObject;
/*     */   protected String rightsTarget;
/*     */   protected String rightsType;
/*     */   protected String rightsIdentity;
/*     */   protected String rightsIdentityName;
/*     */   protected String rightsPermissionCode;
/*     */   protected Date rightsCreateTime;
/*     */   protected String rightsCreateBy;
/*     */   
/*     */   public void setId(String rightsId) {
/*  62 */     this.rightsId = rightsId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  67 */     return this.rightsId;
/*     */   }
/*     */   
/*     */   public void setRightsId(String rightsId) {
/*  71 */     this.rightsId = rightsId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRightsId() {
/*  80 */     return this.rightsId;
/*     */   }
/*     */   
/*     */   public void setRightsTarget(String rightsTarget) {
/*  84 */     this.rightsTarget = rightsTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRightsTarget() {
/*  93 */     return this.rightsTarget;
/*     */   }
/*     */   
/*     */   public void setRightsType(String rightsType) {
/*  97 */     this.rightsType = rightsType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRightsType() {
/* 106 */     return this.rightsType;
/*     */   }
/*     */   
/*     */   public void setRightsIdentity(String rightsIdentity) {
/* 110 */     this.rightsIdentity = rightsIdentity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRightsIdentity() {
/* 119 */     return this.rightsIdentity;
/*     */   }
/*     */   
/*     */   public String getRightsObject() {
/* 123 */     return this.rightsObject;
/*     */   }
/*     */   
/*     */   public void setRightsObject(String rightsObject) {
/* 127 */     this.rightsObject = rightsObject;
/*     */   }
/*     */   
/*     */   public void setRightsIdentityName(String rightsIdentityName) {
/* 131 */     this.rightsIdentityName = rightsIdentityName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRightsIdentityName() {
/* 140 */     return this.rightsIdentityName;
/*     */   }
/*     */   
/*     */   public void setRightsPermissionCode(String rightsPermissionCode) {
/* 144 */     this.rightsPermissionCode = rightsPermissionCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRightsPermissionCode() {
/* 153 */     return this.rightsPermissionCode;
/*     */   }
/*     */   
/*     */   public void setRightsCreateTime(Date rightsCreateTime) {
/* 157 */     this.rightsCreateTime = rightsCreateTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getRightsCreateTime() {
/* 166 */     return this.rightsCreateTime;
/*     */   }
/*     */   
/*     */   public void setRightsCreateBy(String rightsCreateBy) {
/* 170 */     this.rightsCreateBy = rightsCreateBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRightsCreateBy() {
/* 179 */     return this.rightsCreateBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 186 */     return (new ToStringBuilder(this))
/* 187 */       .append("rightsId", this.rightsId)
/* 188 */       .append("rightsTarget", this.rightsTarget)
/* 189 */       .append("rightsType", this.rightsType)
/* 190 */       .append("rightsIdentity", this.rightsIdentity)
/* 191 */       .append("rightsIdentityName", this.rightsIdentityName)
/* 192 */       .append("rightsPermissionCode", this.rightsPermissionCode)
/* 193 */       .append("rightsCreateTime", this.rightsCreateTime)
/* 194 */       .append("rightsCreateBy", this.rightsCreateBy)
/* 195 */       .toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/SysAuthorization.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */