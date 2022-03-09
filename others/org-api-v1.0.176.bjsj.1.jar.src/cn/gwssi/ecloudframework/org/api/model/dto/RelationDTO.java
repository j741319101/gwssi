/*     */ package cn.gwssi.ecloudframework.org.api.model.dto;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.org.api.model.IRelation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RelationDTO
/*     */   implements IRelation
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   protected String groupId;
/*     */   protected String userId;
/*     */   protected Integer isMaster;
/*     */   protected Integer status;
/*     */   protected String type;
/*     */   protected String hasChild;
/*     */   protected String groupCode;
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
/*     */   protected String postCode;
/*     */   protected String postName;
/*     */   protected String unitId;
/*     */   protected String unitName;
/*     */   protected Integer userStatus;
/*     */   protected Integer userActiveStatus;
/*     */   protected Date userCreateTime;
/*     */   protected String parentOrgName;
/*     */   
/*     */   public RelationDTO() {}
/*     */   
/*     */   public RelationDTO(String groupId, String userId, String type) {
/* 117 */     this.groupId = groupId;
/* 118 */     this.userId = userId;
/* 119 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getUnitId() {
/* 123 */     return this.unitId;
/*     */   }
/*     */   
/*     */   public void setUnitId(String unitId) {
/* 127 */     this.unitId = unitId;
/*     */   }
/*     */   
/*     */   public String getUnitName() {
/* 131 */     return this.unitName;
/*     */   }
/*     */   
/*     */   public void setUnitName(String unitName) {
/* 135 */     this.unitName = unitName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupId() {
/* 140 */     return this.groupId;
/*     */   }
/*     */   
/*     */   public void setGroupId(String groupId) {
/* 144 */     this.groupId = groupId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUserId(String userId) {
/* 149 */     this.userId = userId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserId() {
/* 154 */     return this.userId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsMaster(Integer isMaster) {
/* 159 */     this.isMaster = isMaster;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getIsMaster() {
/* 164 */     return this.isMaster;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupName() {
/* 169 */     return this.groupName;
/*     */   }
/*     */   
/*     */   public void setGroupName(String groupName) {
/* 173 */     this.groupName = groupName;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 177 */     return this.userName;
/*     */   }
/*     */   
/*     */   public Integer getStatus() {
/* 181 */     return this.status;
/*     */   }
/*     */   
/*     */   public String getRoleAlias() {
/* 185 */     return this.roleAlias;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRoleAlias(String roleAlias) {
/* 190 */     this.roleAlias = roleAlias;
/*     */   }
/*     */   
/*     */   public String getPhoto() {
/* 194 */     return this.photo;
/*     */   }
/*     */   
/*     */   public void setPhoto(String photo) {
/* 198 */     this.photo = photo;
/*     */   }
/*     */   
/*     */   public String getSex() {
/* 202 */     return this.sex;
/*     */   }
/*     */   
/*     */   public void setSex(String sex) {
/* 206 */     this.sex = sex;
/*     */   }
/*     */   
/*     */   public void setStatus(Integer status) {
/* 210 */     this.status = status;
/*     */   }
/*     */   
/*     */   public void setUserName(String userName) {
/* 214 */     this.userName = userName;
/*     */   }
/*     */   
/*     */   public String getRoleName() {
/* 218 */     return this.roleName;
/*     */   }
/*     */   
/*     */   public void setRoleName(String roleName) {
/* 222 */     this.roleName = roleName;
/*     */   }
/*     */   
/*     */   public void setRoleId(String roleId) {
/* 226 */     this.roleId = roleId;
/*     */   }
/*     */   
/*     */   public String getRoleId() {
/* 230 */     return this.roleId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setType(String type) {
/* 235 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/* 240 */     return this.type;
/*     */   }
/*     */   
/*     */   public String getUserAccount() {
/* 244 */     return this.userAccount;
/*     */   }
/*     */   
/*     */   public void setUserAccount(String userAccount) {
/* 248 */     this.userAccount = userAccount;
/*     */   }
/*     */   
/*     */   public String getMobile() {
/* 252 */     return this.mobile;
/*     */   }
/*     */   
/*     */   public void setMobile(String mobile) {
/* 256 */     this.mobile = mobile;
/*     */   }
/*     */   
/*     */   public String getSn() {
/* 260 */     return this.sn;
/*     */   }
/*     */   
/*     */   public void setSn(String sn) {
/* 264 */     this.sn = sn;
/*     */   }
/*     */   
/*     */   public String getIsMasters() {
/* 268 */     return this.isMasters;
/*     */   }
/*     */   
/*     */   public void setIsMasters(String isMasters) {
/* 272 */     this.isMasters = isMasters;
/*     */   }
/*     */   
/*     */   public String getOldGroupId() {
/* 276 */     return this.oldGroupId;
/*     */   }
/*     */   
/*     */   public void setOldGroupId(String oldGroupId) {
/* 280 */     this.oldGroupId = oldGroupId;
/*     */   }
/*     */   
/*     */   public String getPostId() {
/* 284 */     return this.postId;
/*     */   }
/*     */   
/*     */   public void setPostId(String postId) {
/* 288 */     this.postId = postId;
/*     */   }
/*     */   
/*     */   public String getPostName() {
/* 292 */     return this.postName;
/*     */   }
/*     */   
/*     */   public void setPostName(String postName) {
/* 296 */     this.postName = postName;
/*     */   }
/*     */   
/*     */   public Integer getUserStatus() {
/* 300 */     return this.userStatus;
/*     */   }
/*     */   
/*     */   public void setUserStatus(Integer userStatus) {
/* 304 */     this.userStatus = userStatus;
/*     */   }
/*     */   
/*     */   public Integer getUserActiveStatus() {
/* 308 */     return this.userActiveStatus;
/*     */   }
/*     */   
/*     */   public void setUserActiveStatus(Integer userActiveStatus) {
/* 312 */     this.userActiveStatus = userActiveStatus;
/*     */   }
/*     */   
/*     */   public Date getUserCreateTime() {
/* 316 */     return this.userCreateTime;
/*     */   }
/*     */   
/*     */   public void setUserCreateTime(Date userCreateTime) {
/* 320 */     this.userCreateTime = userCreateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHasChild() {
/* 325 */     return this.hasChild;
/*     */   }
/*     */   
/*     */   public void setHasChild(String hasChild) {
/* 329 */     this.hasChild = hasChild;
/*     */   }
/*     */   
/*     */   public String getParentOrgName() {
/* 333 */     return this.parentOrgName;
/*     */   }
/*     */   
/*     */   public void setParentOrgName(String parentOrgName) {
/* 337 */     this.parentOrgName = parentOrgName;
/*     */   }
/*     */   
/*     */   public String getGroupCode() {
/* 341 */     return this.groupCode;
/*     */   }
/*     */   
/*     */   public void setGroupCode(String groupCode) {
/* 345 */     this.groupCode = groupCode;
/*     */   }
/*     */   
/*     */   public String getPostCode() {
/* 349 */     return this.postCode;
/*     */   }
/*     */   
/*     */   public void setPostCode(String postCode) {
/* 353 */     this.postCode = postCode;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/dto/RelationDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */