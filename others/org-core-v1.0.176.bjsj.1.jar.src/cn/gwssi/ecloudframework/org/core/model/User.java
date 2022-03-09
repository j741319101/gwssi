/*     */ package cn.gwssi.ecloudframework.org.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUserRole;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.hibernate.validator.constraints.NotBlank;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class User
/*     */   extends BaseModel
/*     */   implements IUser
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   @NotBlank(message = "用户姓名不能为空")
/*     */   protected String fullname;
/*     */   @NotBlank(message = "用户账户不能为空")
/*     */   protected String account;
/*     */   protected String password;
/*     */   protected String email;
/*     */   protected String mobile;
/*     */   protected String weixin;
/*     */   protected String openid;
/*     */   protected Date createTime;
/*     */   protected String address;
/*     */   protected String photo;
/*     */   protected String sex;
/*  81 */   protected String from = "system";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer status;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer sn;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String telephone;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer activeStatus;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer secretLevel;
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<OrgRelation> orgRelationList;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String type;
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<String> managerGroupIdList;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String orgId;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String orgName;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String orgCode;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String postId;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String postName;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String postCode;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFullname(String fullname) {
/* 146 */     this.fullname = fullname;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFullname() {
/* 151 */     return this.fullname;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAccount(String account) {
/* 156 */     this.account = account;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAccount() {
/* 161 */     return this.account;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 165 */     this.password = password;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 170 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setEmail(String email) {
/* 174 */     this.email = email;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getEmail() {
/* 179 */     return this.email;
/*     */   }
/*     */   
/*     */   public void setMobile(String mobile) {
/* 183 */     this.mobile = mobile;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMobile() {
/* 188 */     return this.mobile;
/*     */   }
/*     */   
/*     */   public void setWeixin(String weixin) {
/* 192 */     this.weixin = weixin;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWeixin() {
/* 197 */     return this.weixin;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 202 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 207 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setAddress(String address) {
/* 211 */     this.address = address;
/*     */   }
/*     */   
/*     */   public String getAddress() {
/* 215 */     return this.address;
/*     */   }
/*     */   
/*     */   public void setPhoto(String photo) {
/* 219 */     this.photo = photo;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPhoto() {
/* 224 */     return this.photo;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/* 229 */     return this.sn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSn(Integer sn) {
/* 234 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<IUserRole> getRoles() {
/* 239 */     return null;
/*     */   }
/*     */   
/*     */   public void setSex(String sex) {
/* 243 */     this.sex = sex;
/*     */   }
/*     */   
/*     */   public String getSex() {
/* 247 */     return this.sex;
/*     */   }
/*     */   
/*     */   public void setFrom(String from) {
/* 251 */     this.from = from;
/*     */   }
/*     */   
/*     */   public String getFrom() {
/* 255 */     return this.from;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOpenid() {
/* 260 */     return this.openid;
/*     */   }
/*     */   
/*     */   public void setOpenid(String openid) {
/* 264 */     this.openid = openid;
/*     */   }
/*     */   
/*     */   public List<OrgRelation> getOrgRelationList() {
/* 268 */     return this.orgRelationList;
/*     */   }
/*     */   
/*     */   public void setOrgRelationList(List<OrgRelation> orgRelationList) {
/* 272 */     this.orgRelationList = orgRelationList;
/*     */   }
/*     */   
/*     */   public void setStatus(Integer status) {
/* 276 */     this.status = status;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getStatus() {
/* 281 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserId() {
/* 286 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUserId(String userId) {
/* 291 */     this.id = userId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTelephone() {
/* 296 */     return this.telephone;
/*     */   }
/*     */   
/*     */   public void setTelephone(String telephone) {
/* 300 */     this.telephone = telephone;
/*     */   }
/*     */   
/*     */   public Integer getActiveStatus() {
/* 304 */     return this.activeStatus;
/*     */   }
/*     */   
/*     */   public void setActiveStatus(Integer activeStatus) {
/* 308 */     this.activeStatus = activeStatus;
/*     */   }
/*     */   
/*     */   public Integer getSecretLevel() {
/* 312 */     return this.secretLevel;
/*     */   }
/*     */   
/*     */   public void setSecretLevel(Integer secretLevel) {
/* 316 */     this.secretLevel = secretLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/* 321 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 325 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getManagerGroupIdList() {
/* 330 */     return this.managerGroupIdList;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOrgId() {
/* 335 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 339 */     this.orgId = orgId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOrgName() {
/* 344 */     return this.orgName;
/*     */   }
/*     */   
/*     */   public void setOrgName(String orgName) {
/* 348 */     this.orgName = orgName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOrgCode() {
/* 353 */     return this.orgCode;
/*     */   }
/*     */   
/*     */   public void setOrgCode(String orgCode) {
/* 357 */     this.orgCode = orgCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPostId() {
/* 362 */     return this.postId;
/*     */   }
/*     */   
/*     */   public void setPostId(String postId) {
/* 366 */     this.postId = postId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPostName() {
/* 371 */     return this.postName;
/*     */   }
/*     */   
/*     */   public void setPostName(String postName) {
/* 375 */     this.postName = postName;
/*     */   }
/*     */   
/*     */   public void setManagerGroupIdList(List<String> managerGroupIdList) {
/* 379 */     this.managerGroupIdList = managerGroupIdList;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPostCode() {
/* 384 */     return this.postCode;
/*     */   }
/*     */   
/*     */   public void setPostCode(String postCode) {
/* 388 */     this.postCode = postCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 393 */     return "User{fullname='" + this.fullname + '\'' + ", account='" + this.account + '\'' + ", password='" + this.password + '\'' + ", email='" + this.email + '\'' + ", mobile='" + this.mobile + '\'' + ", weixin='" + this.weixin + '\'' + ", openid='" + this.openid + '\'' + ", createTime=" + this.createTime + ", address='" + this.address + '\'' + ", photo='" + this.photo + '\'' + ", sex='" + this.sex + '\'' + ", from='" + this.from + '\'' + ", status=" + this.status + ", sn=" + this.sn + ", telephone='" + this.telephone + '\'' + ", activeStatus=" + this.activeStatus + ", orgRelationList=" + this.orgRelationList + ", id='" + this.id + '\'' + ", createTime=" + this.createTime + ", createBy='" + this.createBy + '\'' + ", updateTime=" + this.updateTime + ", updateBy='" + this.updateBy + '\'' + ", version=" + this.version + ", delete=" + this.delete + '}';
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/model/User.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */