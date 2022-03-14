/*     */ package com.dstz.org.api.model.dto;
/*     */ 
/*     */ import com.dstz.org.api.model.IUserRole;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserRoleDTO
/*     */   implements IUserRole
/*     */ {
/*     */   protected String roleId;
/*     */   protected String userId;
/*     */   protected String fullname;
/*     */   protected String roleName;
/*     */   protected String alias;
/*     */   protected String account;
/*     */   
/*     */   public UserRoleDTO(String roleId, String roleName, String alias) {
/*  45 */     this.roleId = roleId;
/*  46 */     this.roleName = roleName;
/*  47 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserRoleDTO(String roleId, String userId, String fullname, String roleName) {
/*  52 */     this.roleId = roleId;
/*  53 */     this.userId = userId;
/*  54 */     this.fullname = fullname;
/*  55 */     this.roleName = roleName;
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/*  59 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlias() {
/*  64 */     return this.alias;
/*     */   }
/*     */   
/*     */   public void setFullname(String fullname) {
/*  68 */     this.fullname = fullname;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFullname() {
/*  73 */     return this.fullname;
/*     */   }
/*     */   
/*     */   public void setRoleName(String roleName) {
/*  77 */     this.roleName = roleName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRoleName() {
/*  82 */     return this.roleName;
/*     */   }
/*     */   
/*     */   public void setRoleId(String roleId) {
/*  86 */     this.roleId = roleId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRoleId() {
/*  91 */     return this.roleId;
/*     */   }
/*     */   
/*     */   public void setUserId(String userId) {
/*  95 */     this.userId = userId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserId() {
/* 100 */     return this.userId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAccount() {
/* 105 */     return this.account;
/*     */   }
/*     */   
/*     */   public void setAccount(String account) {
/* 109 */     this.account = account;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/dto/UserRoleDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */