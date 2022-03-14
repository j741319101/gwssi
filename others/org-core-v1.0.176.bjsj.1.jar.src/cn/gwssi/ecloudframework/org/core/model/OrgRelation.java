/*     */ package com.dstz.org.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.org.api.model.IRelation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OrgRelation
/*     */   extends BaseModel
/*     */   implements IRelation
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   protected String groupId;
/*     */   protected String userId;
/*     */   protected Integer isMaster;
/*     */   protected Integer status;
/*     */   protected String type;
/*     */   protected String hasChild;
/*     */   protected String groupName;
/*     */   protected String userName;
/*     */   protected String userAccount;
/*     */   protected String roleName;
/*     */   protected String roleAlias;
/*     */   protected String photo;
/*     */   protected String sex;
/*     */   protected String isMasters;
/*     */   protected String mobile;
/*     */   protected String sn;
/*     */   protected String oldGroupId;
/*     */   protected String roleId;
/*     */   protected String postId;
/*     */   protected String postName;
/*     */   protected String unitId;
/*     */   protected String unitName;
/*     */   protected Integer userStatus;
/*     */   protected Integer userActiveStatus;
/*     */   protected Date userCreateTime;
/*     */   protected String parentOrgName;
/*     */   
/*     */   public OrgRelation() {}
/*     */   
/*     */   public OrgRelation(String groupId, String userId, String type) {
/* 113 */     this.groupId = groupId;
/* 114 */     this.userId = userId;
/* 115 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getUnitId() {
/* 119 */     return this.unitId;
/*     */   }
/*     */   
/*     */   public void setUnitId(String unitId) {
/* 123 */     this.unitId = unitId;
/*     */   }
/*     */   
/*     */   public String getUnitName() {
/* 127 */     return this.unitName;
/*     */   }
/*     */   
/*     */   public void setUnitName(String unitName) {
/* 131 */     this.unitName = unitName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupId() {
/* 136 */     return this.groupId;
/*     */   }
/*     */   
/*     */   public void setGroupId(String groupId) {
/* 140 */     this.groupId = groupId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUserId(String userId) {
/* 145 */     this.userId = userId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserId() {
/* 150 */     return this.userId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsMaster(Integer isMaster) {
/* 155 */     this.isMaster = isMaster;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getIsMaster() {
/* 160 */     return this.isMaster;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupName() {
/* 165 */     return this.groupName;
/*     */   }
/*     */   
/*     */   public void setGroupName(String groupName) {
/* 169 */     this.groupName = groupName;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 173 */     return this.userName;
/*     */   }
/*     */   
/*     */   public Integer getStatus() {
/* 177 */     return this.status;
/*     */   }
/*     */   
/*     */   public String getRoleAlias() {
/* 181 */     return this.roleAlias;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRoleAlias(String roleAlias) {
/* 186 */     this.roleAlias = roleAlias;
/*     */   }
/*     */   
/*     */   public String getPhoto() {
/* 190 */     return this.photo;
/*     */   }
/*     */   
/*     */   public void setPhoto(String photo) {
/* 194 */     this.photo = photo;
/*     */   }
/*     */   
/*     */   public String getSex() {
/* 198 */     return this.sex;
/*     */   }
/*     */   
/*     */   public void setSex(String sex) {
/* 202 */     this.sex = sex;
/*     */   }
/*     */   
/*     */   public void setStatus(Integer status) {
/* 206 */     this.status = status;
/*     */   }
/*     */   
/*     */   public void setUserName(String userName) {
/* 210 */     this.userName = userName;
/*     */   }
/*     */   
/*     */   public String getRoleName() {
/* 214 */     return this.roleName;
/*     */   }
/*     */   
/*     */   public void setRoleName(String roleName) {
/* 218 */     this.roleName = roleName;
/*     */   }
/*     */   
/*     */   public void setRoleId(String roleId) {
/* 222 */     this.roleId = roleId;
/*     */   }
/*     */   
/*     */   public String getRoleId() {
/* 226 */     return this.roleId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setType(String type) {
/* 231 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/* 236 */     return this.type;
/*     */   }
/*     */   
/*     */   public String getUserAccount() {
/* 240 */     return this.userAccount;
/*     */   }
/*     */   
/*     */   public void setUserAccount(String userAccount) {
/* 244 */     this.userAccount = userAccount;
/*     */   }
/*     */   
/*     */   public String getMobile() {
/* 248 */     return this.mobile;
/*     */   }
/*     */   
/*     */   public void setMobile(String mobile) {
/* 252 */     this.mobile = mobile;
/*     */   }
/*     */   
/*     */   public String getSn() {
/* 256 */     return this.sn;
/*     */   }
/*     */   
/*     */   public void setSn(String sn) {
/* 260 */     this.sn = sn;
/*     */   }
/*     */   
/*     */   public String getIsMasters() {
/* 264 */     return this.isMasters;
/*     */   }
/*     */   
/*     */   public void setIsMasters(String isMasters) {
/* 268 */     this.isMasters = isMasters;
/*     */   }
/*     */   
/*     */   public String getOldGroupId() {
/* 272 */     return this.oldGroupId;
/*     */   }
/*     */   
/*     */   public void setOldGroupId(String oldGroupId) {
/* 276 */     this.oldGroupId = oldGroupId;
/*     */   }
/*     */   
/*     */   public String getPostId() {
/* 280 */     return this.postId;
/*     */   }
/*     */   
/*     */   public void setPostId(String postId) {
/* 284 */     this.postId = postId;
/*     */   }
/*     */   
/*     */   public String getPostName() {
/* 288 */     return this.postName;
/*     */   }
/*     */   
/*     */   public void setPostName(String postName) {
/* 292 */     this.postName = postName;
/*     */   }
/*     */   
/*     */   public Integer getUserStatus() {
/* 296 */     return this.userStatus;
/*     */   }
/*     */   
/*     */   public void setUserStatus(Integer userStatus) {
/* 300 */     this.userStatus = userStatus;
/*     */   }
/*     */   
/*     */   public Integer getUserActiveStatus() {
/* 304 */     return this.userActiveStatus;
/*     */   }
/*     */   
/*     */   public void setUserActiveStatus(Integer userActiveStatus) {
/* 308 */     this.userActiveStatus = userActiveStatus;
/*     */   }
/*     */   
/*     */   public Date getUserCreateTime() {
/* 312 */     return this.userCreateTime;
/*     */   }
/*     */   
/*     */   public void setUserCreateTime(Date userCreateTime) {
/* 316 */     this.userCreateTime = userCreateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHasChild() {
/* 321 */     return this.hasChild;
/*     */   }
/*     */   
/*     */   public void setHasChild(String hasChild) {
/* 325 */     this.hasChild = hasChild;
/*     */   }
/*     */   
/*     */   public String getParentOrgName() {
/* 329 */     return this.parentOrgName;
/*     */   }
/*     */   
/*     */   public void setParentOrgName(String parentOrgName) {
/* 333 */     this.parentOrgName = parentOrgName;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/model/OrgRelation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */