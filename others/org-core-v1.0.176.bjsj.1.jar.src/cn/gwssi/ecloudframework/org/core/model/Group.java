/*     */ package com.dstz.org.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.org.api.constant.GroupTypeConstant;
/*     */ import com.dstz.org.api.model.IGroup;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Group
/*     */   extends BaseModel
/*     */   implements IGroup
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   protected String name;
/*     */   protected String parentId;
/*     */   protected String code;
/*     */   protected Integer type;
/*     */   protected String desc;
/*     */   protected String path;
/*     */   protected Integer sn;
/*     */   protected String simple;
/*     */   protected Integer userNum;
/*     */   protected String parentName;
/*     */   
/*     */   public void setName(String name) {
/*  62 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  71 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParentId(String parentId) {
/*  76 */     this.parentId = parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentId() {
/*  86 */     return this.parentId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCode(String code) {
/*  91 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCode() {
/* 100 */     return this.code;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setType(Integer type) {
/* 105 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getType() {
/* 114 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentName() {
/* 120 */     return this.parentName;
/*     */   }
/*     */   
/*     */   public void setParentName(String parentName) {
/* 124 */     this.parentName = parentName;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/* 128 */     this.desc = desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDesc() {
/* 137 */     return this.desc;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPath(String path) {
/* 142 */     this.path = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 152 */     return this.path;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSn(Integer sn) {
/* 157 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/* 167 */     return this.sn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupId() {
/* 172 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupCode() {
/* 177 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGroupType() {
/* 183 */     return GroupTypeConstant.ORG.key();
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getGroupLevel() {
/* 188 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupName() {
/* 193 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getUserNum() {
/* 198 */     return this.userNum;
/*     */   }
/*     */   
/*     */   public void setUserNum(Integer userNum) {
/* 202 */     this.userNum = userNum;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSimple() {
/* 207 */     return this.simple;
/*     */   }
/*     */   
/*     */   public void setSimple(String simple) {
/* 211 */     this.simple = simple;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/model/Group.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */