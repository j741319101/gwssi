/*     */ package cn.gwssi.ecloudframework.org.api.model.dto;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GroupDTO
/*     */   implements IGroup
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   String identityType;
/*     */   String groupId;
/*     */   String groupName;
/*     */   String groupCode;
/*     */   Integer sn;
/*     */   String groupType;
/*     */   String parentId;
/*     */   String path;
/*     */   Integer groupLevel;
/*     */   Integer userNum;
/*     */   protected String simple;
/*     */   protected String parentName;
/*     */   
/*     */   public GroupDTO(IGroup group) {
/*  41 */     this.groupCode = group.getGroupCode();
/*  42 */     this.groupId = group.getGroupId();
/*  43 */     this.groupType = group.getGroupType();
/*  44 */     this.parentId = group.getParentId();
/*  45 */     this.groupName = group.getGroupName();
/*  46 */     this.groupLevel = group.getGroupLevel();
/*  47 */     this.path = group.getPath();
/*  48 */     this.sn = group.getSn();
/*  49 */     this.userNum = group.getUserNum();
/*  50 */     this.simple = group.getSimple();
/*  51 */     this.parentName = group.getParentName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupDTO() {}
/*     */ 
/*     */   
/*     */   public GroupDTO(String groupId, String groupName, String groupType) {
/*  60 */     this.groupId = groupId;
/*  61 */     this.groupName = groupName;
/*  62 */     this.groupType = groupType;
/*     */   }
/*     */   
/*     */   public String getIdentityType() {
/*  66 */     return this.identityType;
/*     */   }
/*     */   
/*     */   public void setIdentityType(String identityType) {
/*  70 */     this.identityType = identityType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupId() {
/*  75 */     return this.groupId;
/*     */   }
/*     */   
/*     */   public void setGroupId(String groupId) {
/*  79 */     this.groupId = groupId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupName() {
/*  84 */     return this.groupName;
/*     */   }
/*     */   
/*     */   public void setGroupName(String groupName) {
/*  88 */     this.groupName = groupName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupCode() {
/*  93 */     return this.groupCode;
/*     */   }
/*     */   
/*     */   public void setGroupCode(String groupCode) {
/*  97 */     this.groupCode = groupCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/* 102 */     return this.sn;
/*     */   }
/*     */   
/*     */   public void setSn(Integer sn) {
/* 106 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupType() {
/* 111 */     return this.groupType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getGroupLevel() {
/* 116 */     return this.groupLevel;
/*     */   }
/*     */   
/*     */   public void setGroupType(String groupType) {
/* 120 */     this.groupType = groupType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 125 */     return this.parentId;
/*     */   }
/*     */   
/*     */   public void setParentId(String parentId) {
/* 129 */     this.parentId = parentId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 134 */     return this.path;
/*     */   }
/*     */   
/*     */   public void setGroupLevel(Integer groupLevel) {
/* 138 */     this.groupLevel = groupLevel;
/*     */   }
/*     */   
/*     */   public void setPath(String path) {
/* 142 */     this.path = path;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getUserNum() {
/* 147 */     return this.userNum;
/*     */   }
/*     */   
/*     */   public void setUserNum(Integer userNum) {
/* 151 */     this.userNum = userNum;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSimple() {
/* 156 */     return this.simple;
/*     */   }
/*     */   
/*     */   public void setSimple(String simple) {
/* 160 */     this.simple = simple;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParentName() {
/* 165 */     return this.parentName;
/*     */   }
/*     */   
/*     */   public void setParentName(String parentName) {
/* 169 */     this.parentName = parentName;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/dto/GroupDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */