/*    */ package com.dstz.base.core.util;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.exception.ExceptionUtils;
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
/*    */ public class ExceptionUtil
/*    */ {
/*    */   public static String getExceptionMessage(Throwable e) {
/* 25 */     StringWriter sw = new StringWriter();
/* 26 */     e.printStackTrace(new PrintWriter(sw, true));
/* 27 */     String str = sw.toString();
/*    */     
/* 29 */     return str;
/*    */   }
/*    */   
/*    */   public static String getRootErrorMseeage(Exception e) {
/* 33 */     Throwable root = ExceptionUtils.getRootCause(e);
/* 34 */     root = (root == null) ? e : root;
/*    */     
/* 36 */     if (root == null) return "";
/*    */     
/* 38 */     String msg = root.getMessage();
/*    */     
/* 40 */     if (msg == null) return "null";
/*    */     
/* 42 */     return StringUtils.defaultString(msg);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/ExceptionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */