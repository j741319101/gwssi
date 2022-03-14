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
/*     */ public class Post
/*     */   extends BaseModel
/*     */   implements IGroup
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   protected String name;
/*     */   protected String code;
/*     */   protected String type;
/*     */   protected String desc;
/*     */   protected Integer isCivilServant;
/*     */   protected String level;
/*     */   protected String orgId;
/*     */   protected Integer userNum;
/*     */   
/*     */   public String getName() {
/*  53 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  57 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getCode() {
/*  61 */     return this.code;
/*     */   }
/*     */   
/*     */   public void setCode(String code) {
/*  65 */     this.code = code;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  69 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  73 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/*  77 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/*  81 */     this.desc = desc;
/*     */   }
/*     */   
/*     */   public Integer getIsCivilServant() {
/*  85 */     return this.isCivilServant;
/*     */   }
/*     */   
/*     */   public void setIsCivilServant(Integer isCivilServant) {
/*  89 */     this.isCivilServant = isCivilServant;
/*     */   }
/*     */   
/*     */   public String getLevel() {
/*  93 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(String level) {
/*  97 */     this.level = level;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupId() {
/* 102 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupName() {
/* 107 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupCode() {
/* 112 */     return this.code;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupType() {
/* 117 */     return GroupTypeConstant.POST.key();
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getGroupLevel() {
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 137 */     return null;
/*     */   }
/*     */   
/*     */   public String getOrgId() {
/* 141 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 145 */     this.orgId = orgId;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getUserNum() {
/* 150 */     return this.userNum;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSimple() {
/* 155 */     return null;
/*     */   }
/*     */   
/*     */   public void setUserNum(Integer userNum) {
/* 159 */     this.userNum = userNum;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/model/Post.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */