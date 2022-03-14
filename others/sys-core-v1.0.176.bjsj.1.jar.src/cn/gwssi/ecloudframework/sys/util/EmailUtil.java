/*    */ package com.dstz.sys.util;
/*    */ 
/*    */ import com.dstz.base.core.util.PropertyUtil;
/*    */ import cn.hutool.core.collection.CollUtil;
/*    */ import cn.hutool.extra.mail.MailAccount;
/*    */ import cn.hutool.extra.mail.MailUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmailUtil
/*    */ {
/*    */   private static MailAccount account;
/*    */   
/*    */   public static void send(String email, String subject, String content) {
/* 35 */     MailUtil.send(account(), CollUtil.newArrayList((Object[])new String[] { email }, ), subject, content, true, new java.io.File[0]);
/*    */   }
/*    */   
/*    */   private static MailAccount account() {
/* 39 */     if (account != null) return account;
/*    */     
/* 41 */     MailAccount mailAccount = new MailAccount();
/* 42 */     String host = PropertyUtil.getProperty("mail.host");
/* 43 */     int port = PropertyUtil.getIntProperty("mail.port").intValue();
/* 44 */     boolean isSSL = PropertyUtil.getBoolProperty("mail.ssl");
/* 45 */     String user = PropertyUtil.getProperty("mail.nickName");
/* 46 */     String from = PropertyUtil.getProperty("mail.address");
/* 47 */     String pass = PropertyUtil.getProperty("mail.password");
/* 48 */     mailAccount.setHost(host);
/* 49 */     mailAccount.setPort(Integer.valueOf(port));
/* 50 */     mailAccount.setFrom(from);
/* 51 */     mailAccount.setUser(user);
/* 52 */     mailAccount.setPass(pass);
/* 53 */     mailAccount.setSslEnable(Boolean.valueOf(isSSL));
/*    */     
/* 55 */     setAccount(mailAccount);
/* 56 */     return mailAccount;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setAccount(MailAccount account) {
/* 64 */     EmailUtil.account = account;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/util/EmailUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */