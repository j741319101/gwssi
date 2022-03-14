/*    */ package com.dstz.base.core.util.sm3;
/*    */ 
/*    */ import java.security.MessageDigest;
/*    */ import org.bouncycastle.util.encoders.Hex;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SM3Util
/*    */ {
/*    */   public static String SM3Encode(String str) {
/* 11 */     byte[] md = new byte[32];
/* 12 */     byte[] msg1 = str.getBytes();
/* 13 */     SM3Digest sm3 = new SM3Digest();
/* 14 */     sm3.update(msg1, 0, msg1.length);
/* 15 */     sm3.doFinal(md, 0);
/* 16 */     String s = new String(Hex.encode(md));
/* 17 */     return s;
/*    */   }
/*    */   
/*    */   public static String byte2hexString(byte[] bytes) {
/* 21 */     StringBuilder buf = new StringBuilder(bytes.length * 2);
/*    */     
/* 23 */     for (byte aByte : bytes) {
/* 24 */       if ((aByte & 0xFF) < 16) {
/* 25 */         buf.append("0");
/*    */       }
/*    */       
/* 28 */       buf.append(Long.toString((aByte & 0xFF), 16));
/*    */     } 
/*    */     
/* 31 */     return buf.toString();
/*    */   }
/*    */   
/*    */   public static String SM3EncodePws(String str) {
/*    */     try {
/* 36 */       MessageDigest md = MessageDigest.getInstance("MD5");
/* 37 */       String resultString = byte2hexString(md.digest(str.getBytes()));
/* 38 */       return SM3Encode(resultString);
/* 39 */     } catch (Exception exception) {
/*    */ 
/*    */       
/* 42 */       return "";
/*    */     } 
/*    */   }
/*    */   public static String MD5Encode(String str) {
/*    */     try {
/* 47 */       MessageDigest md = MessageDigest.getInstance("MD5");
/* 48 */       String resultString = byte2hexString(md.digest(str.getBytes()));
/* 49 */       return resultString;
/* 50 */     } catch (Exception exception) {
/*    */ 
/*    */       
/* 53 */       return "";
/*    */     } 
/*    */   }
/*    */   public static void main(String[] args) {
/*    */     try {
/* 58 */       MessageDigest md = MessageDigest.getInstance("MD5");
/* 59 */       String resultString = byte2hexString(md.digest("1".getBytes()));
/* 60 */       System.out.println(resultString);
/* 61 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/sm3/SM3Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */