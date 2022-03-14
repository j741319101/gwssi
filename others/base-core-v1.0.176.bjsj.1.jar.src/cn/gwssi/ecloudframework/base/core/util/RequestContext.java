/*    */ package com.dstz.base.core.util;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestContext
/*    */ {
/* 11 */   private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<>();
/*    */   
/* 13 */   private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<>();
/*    */   
/*    */   public static void setHttpServletRequest(HttpServletRequest request) {
/* 16 */     requestLocal.set(request);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void clearHttpReqResponse() {
/* 23 */     requestLocal.remove();
/* 24 */     responseLocal.remove();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setHttpServletResponse(HttpServletResponse response) {
/* 33 */     responseLocal.set(response);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpServletRequest getHttpServletRequest() {
/* 42 */     return requestLocal.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpServletResponse getHttpServletResponse() {
/* 51 */     return responseLocal.get();
/*    */   }
/*    */   
/*    */   public static String getHttpCtx() {
/* 55 */     if (requestLocal.get() != null) {
/* 56 */       return ((HttpServletRequest)requestLocal.get()).getContextPath();
/*    */     }
/* 58 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getUrl(String url) {
/* 70 */     if (url.startsWith("http://")) {
/* 71 */       return url;
/*    */     }
/* 73 */     return getHttpCtx() + "/" + url;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/RequestContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */