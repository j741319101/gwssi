/*    */ package com.dstz.org.sync.orm;
/*    */ 
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.alibaba.fastjson.annotation.JSONField;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserJsonOrm
/*    */ {
/*    */   protected String id;
/*    */   protected String account;
/*    */   protected String fullname;
/* 20 */   protected String password = "n4bQgYhMfWWaL+qgxVrQFaO/TxsrC4Is0V1sFbDwCgg=";
/*    */   
/*    */   protected String email;
/*    */   
/*    */   protected String mobile;
/*    */   
/* 26 */   protected Integer status = Integer.valueOf(1);
/*    */   
/* 28 */   protected String from = "external";
/*    */   
/*    */   public String getId() {
/* 31 */     return this.id;
/*    */   }
/*    */   
/*    */   @JSONField(name = "userId")
/*    */   public void setId(String id) {
/* 36 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getAccount() {
/* 40 */     return this.account;
/*    */   }
/*    */   
/*    */   @JSONField(name = "username")
/*    */   public void setAccount(String account) {
/* 45 */     this.account = account;
/*    */   }
/*    */   
/*    */   public String getFullname() {
/* 49 */     if (StringUtil.isEmpty(this.fullname)) {
/* 50 */       return this.account;
/*    */     }
/* 52 */     return this.fullname;
/*    */   }
/*    */   
/*    */   public void setFullname(String fullname) {
/* 56 */     this.fullname = fullname;
/*    */   }
/*    */   
/*    */   @JSONField(name = "password")
/*    */   public String getPassword() {
/* 61 */     return this.password;
/*    */   }
/*    */   
/*    */   public void setPassword(String password) {
/* 65 */     this.password = password;
/*    */   }
/*    */   
/*    */   public String getEmail() {
/* 69 */     return this.email;
/*    */   }
/*    */   @JSONField(name = "email")
/*    */   public void setEmail(String email) {
/* 73 */     this.email = email;
/*    */   }
/*    */   
/*    */   public String getMobile() {
/* 77 */     return this.mobile;
/*    */   }
/*    */   
/*    */   @JSONField(name = "userPhone")
/*    */   public void setMobile(String mobile) {
/* 82 */     this.mobile = mobile;
/*    */   }
/*    */   
/*    */   public Integer getStatus() {
/* 86 */     return this.status;
/*    */   }
/*    */   
/*    */   public void setStatus(Integer status) {
/* 90 */     this.status = status;
/*    */   }
/*    */   
/*    */   public String getFrom() {
/* 94 */     return this.from;
/*    */   }
/*    */   
/*    */   public void setFrom(String from) {
/* 98 */     this.from = from;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/sync/orm/UserJsonOrm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */