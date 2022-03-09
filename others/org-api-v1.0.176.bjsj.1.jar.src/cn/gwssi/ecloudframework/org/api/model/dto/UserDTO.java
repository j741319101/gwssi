/*     */ package cn.gwssi.ecloudframework.org.api.model.dto;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUserRole;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserDTO
/*     */   implements IUser
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   public static final String FROM_SYSTEM = "system";
/*     */   public static final String FROM_HUMAN_RESOURCE = "humanResource";
/*     */   protected String id;
/*     */   protected String fullname;
/*     */   protected String account;
/*     */   protected String password;
/*     */   protected String email;
/*     */   protected String mobile;
/*     */   protected String weixin;
/*     */   protected String telephone;
/*     */   protected Date createTime;
/*     */   protected String address;
/*     */   protected String photo;
/*     */   protected String sex;
/*     */   protected String from;
/*     */   protected Integer status;
/*     */   protected String openid;
/*     */   protected Integer sn;
/*     */   protected String orgId;
/*     */   protected String orgName;
/*     */   protected String orgCode;
/*     */   protected String postId;
/*     */   protected String postName;
/*     */   protected String postCode;
/*     */   protected String fristLogin;
/*     */   protected List<IUserRole> roles;
/*     */   protected List<RelationDTO> orgRelationList;
/*     */   protected String type;
/*     */   protected List<String> managerGroupIdList;
/*     */   
/*     */   public UserDTO() {
/* 159 */     this.from = "system";
/*     */   }
/*     */   
/*     */   public UserDTO(String account) {
/* 163 */     this.account = account;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserDTO fromHumanResource() {
/* 172 */     this.from = "humanResource";
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/* 177 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 181 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserId() {
/* 186 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUserId(String userId) {
/* 191 */     this.id = userId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFullname() {
/* 196 */     return this.fullname;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFullname(String fullname) {
/* 201 */     this.fullname = fullname;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAccount() {
/* 206 */     return this.account;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAccount(String account) {
/* 211 */     this.account = account;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 216 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 220 */     this.password = password;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getEmail() {
/* 225 */     return this.email;
/*     */   }
/*     */   
/*     */   public void setEmail(String email) {
/* 229 */     this.email = email;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMobile() {
/* 234 */     return this.mobile;
/*     */   }
/*     */   
/*     */   public void setMobile(String mobile) {
/* 238 */     this.mobile = mobile;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWeixin() {
/* 243 */     return this.weixin;
/*     */   }
/*     */   
/*     */   public void setWeixin(String weixin) {
/* 247 */     this.weixin = weixin;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTelephone() {
/* 252 */     return this.telephone;
/*     */   }
/*     */   
/*     */   public void setTelephone(String telephone) {
/* 256 */     this.telephone = telephone;
/*     */   }
/*     */   
/*     */   public Date getCreateTime() {
/* 260 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 264 */     this.createTime = createTime;
/*     */   }
/*     */   
/*     */   public String getAddress() {
/* 268 */     return this.address;
/*     */   }
/*     */   
/*     */   public void setAddress(String address) {
/* 272 */     this.address = address;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPhoto() {
/* 277 */     return this.photo;
/*     */   }
/*     */   
/*     */   public void setPhoto(String photo) {
/* 281 */     this.photo = photo;
/*     */   }
/*     */   
/*     */   public String getSex() {
/* 285 */     return this.sex;
/*     */   }
/*     */   
/*     */   public void setSex(String sex) {
/* 289 */     this.sex = sex;
/*     */   }
/*     */   
/*     */   public String getFrom() {
/* 293 */     return this.from;
/*     */   }
/*     */   
/*     */   public void setFrom(String from) {
/* 297 */     this.from = from;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getStatus() {
/* 302 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(Integer status) {
/* 306 */     this.status = status;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOpenid() {
/* 311 */     return this.openid;
/*     */   }
/*     */   
/*     */   public void setOpenid(String openid) {
/* 315 */     this.openid = openid;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/* 320 */     return this.sn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSn(Integer sn) {
/* 325 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOrgId() {
/* 330 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 334 */     this.orgId = orgId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPostId() {
/* 339 */     return this.postId;
/*     */   }
/*     */   
/*     */   public void setPostId(String postId) {
/* 343 */     this.postId = postId;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<IUserRole> getRoles() {
/* 348 */     return this.roles;
/*     */   }
/*     */   
/*     */   public void setRoles(List<IUserRole> roles) {
/* 352 */     this.roles = roles;
/*     */   }
/*     */   
/*     */   public List<RelationDTO> getOrgRelationList() {
/* 356 */     return this.orgRelationList;
/*     */   }
/*     */   
/*     */   public void setOrgRelationList(List<RelationDTO> orgRelationList) {
/* 360 */     this.orgRelationList = orgRelationList;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOrgName() {
/* 365 */     return this.orgName;
/*     */   }
/*     */   
/*     */   public void setOrgName(String orgName) {
/* 369 */     this.orgName = orgName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPostName() {
/* 374 */     return this.postName;
/*     */   }
/*     */   
/*     */   public void setPostName(String postName) {
/* 378 */     this.postName = postName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 384 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 388 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getManagerGroupIdList() {
/* 393 */     return this.managerGroupIdList;
/*     */   }
/*     */   
/*     */   public void setManagerGroupIdList(List<String> managerGroupIdList) {
/* 397 */     this.managerGroupIdList = managerGroupIdList;
/*     */   }
/*     */   
/*     */   public String getFristLogin() {
/* 401 */     return this.fristLogin;
/*     */   }
/*     */   
/*     */   public void setFristLogin(String fristLogin) {
/* 405 */     this.fristLogin = fristLogin;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOrgCode() {
/* 410 */     return this.orgCode;
/*     */   }
/*     */   
/*     */   public void setOrgCode(String orgCode) {
/* 414 */     this.orgCode = orgCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPostCode() {
/* 419 */     return this.postCode;
/*     */   }
/*     */   
/*     */   public void setPostCode(String postCode) {
/* 423 */     this.postCode = postCode;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/dto/UserDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */