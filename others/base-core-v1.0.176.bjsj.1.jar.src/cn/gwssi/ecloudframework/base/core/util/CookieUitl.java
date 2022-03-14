/*    */ package com.dstz.base.core.util;
/*    */ 
/*    */ import javax.servlet.http.Cookie;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ public class CookieUitl
/*    */ {
/*    */   public static final int cookie_auto_expire = -1;
/*    */   public static final int cookie_no_expire = 31536000;
/*    */   
/*    */   public static void addCookie(String name, String value) {
/* 26 */     addCookie(name, value, -1, RequestContext.getHttpCtx(), RequestContext.getHttpServletResponse());
/*    */   }
/*    */   
/*    */   public static void addCookie(String name, String value, int timeout) {
/* 30 */     addCookie(name, value, timeout, RequestContext.getHttpCtx(), RequestContext.getHttpServletResponse());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void addCookie(String name, String value, int maxAge, String contextPath, HttpServletResponse response) {
/* 36 */     if (StringUtil.isEmpty(contextPath)) {
/* 37 */       contextPath = "/";
/*    */     }
/*    */     
/* 40 */     Cookie cookie = new Cookie(name, value);
/* 41 */     cookie.setPath(contextPath);
/* 42 */     if (maxAge > 0) {
/* 43 */       cookie.setMaxAge(maxAge);
/*    */     }
/* 45 */     response.addCookie(cookie);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void delCookie(String name, HttpServletRequest request, HttpServletResponse response) {
/* 56 */     Cookie uid = new Cookie(name, null);
/* 57 */     uid.setPath("/");
/* 58 */     uid.setMaxAge(0);
/* 59 */     response.addCookie(uid);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getValueByName(String cookieName, HttpServletRequest request) {
/* 64 */     if (request == null) {
/* 65 */       return null;
/*    */     }
/* 67 */     Cookie[] cookies = request.getCookies();
/* 68 */     if (cookies != null)
/* 69 */       for (Cookie cookie : cookies) {
/* 70 */         if (cookie.getName().equals(cookieName)) {
/* 71 */           return cookie.getValue();
/*    */         }
/*    */       }  
/* 74 */     return null;
/*    */   }
/*    */   
/*    */   public static String getValueByName(String name) {
/* 78 */     return getValueByName(name, RequestContext.getHttpServletRequest());
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/CookieUitl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */