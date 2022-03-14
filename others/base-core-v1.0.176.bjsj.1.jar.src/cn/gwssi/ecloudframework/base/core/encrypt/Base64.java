/*    */ package com.dstz.base.core.encrypt;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
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
/*    */ public class Base64
/*    */ {
/*    */   public static String getBase64(String s) throws UnsupportedEncodingException {
/* 19 */     byte[] bytes = org.apache.commons.codec.binary.Base64.encodeBase64(s
/* 20 */         .getBytes("utf-8"));
/* 21 */     return new String(bytes, "utf-8");
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
/*    */   
/*    */   public static String getFromBase64(String s) throws UnsupportedEncodingException {
/* 34 */     byte[] bytes = s.getBytes("GBK");
/*    */     
/* 36 */     byte[] convertBytes = org.apache.commons.codec.binary.Base64.decodeBase64(bytes);
/* 37 */     return new String(convertBytes, "GBK");
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/encrypt/Base64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */