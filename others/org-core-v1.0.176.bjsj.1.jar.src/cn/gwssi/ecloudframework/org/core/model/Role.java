/*     */ package com.dstz.org.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.org.api.constant.GroupTypeConstant;
/*     */ import com.dstz.org.api.model.IGroup;
/*     */ import java.util.Map;
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
/*     */ public class Role
/*     */   extends BaseModel
/*     */   implements IGroup
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   protected String name;
/*     */   protected String alias;
/*     */   protected Integer enabled;
/*     */   protected String description;
/*     */   private Integer sn;
/*     */   protected String orgId;
/*     */   protected Integer userNum;
/*     */   
/*     */   public void setName(String name) {
/*  53 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  57 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/*  61 */     this.alias = alias;
/*     */   }
/*     */   
/*     */   public String getAlias() {
/*  65 */     return this.alias;
/*     */   }
/*     */   
/*     */   public void setEnabled(Integer enabled) {
/*  69 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public Integer getEnabled() {
/*  73 */     return this.enabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupId() {
/*  78 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupCode() {
/*  83 */     return this.alias;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/*  88 */     return this.sn;
/*     */   }
/*     */   
/*     */   public void setSn(Integer sn) {
/*  92 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupType() {
/*  97 */     return GroupTypeConstant.ROLE.key();
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getGroupLevel() {
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 107 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 112 */     return null;
/*     */   }
/*     */   
/*     */   public Map<String, Object> getParams() {
/* 116 */     return null;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 120 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 124 */     this.description = description;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupName() {
/* 129 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getOrgId() {
/* 133 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 137 */     this.orgId = orgId;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getUserNum() {
/* 142 */     return this.userNum;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSimple() {
/* 147 */     return null;
/*     */   }
/*     */   
/*     */   public void setUserNum(Integer userNum) {
/* 151 */     this.userNum = userNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 159 */     return (new ToStringBuilder(this))
/* 160 */       .append("id", this.id)
/* 161 */       .append("name", this.name)
/* 162 */       .append("alias", this.alias)
/* 163 */       .append("enabled", this.enabled)
/* 164 */       .toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/model/Role.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */