/*     */ package com.dstz.base.core.encrypt;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessError;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.SecureRandom;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.KeyGenerator;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.apache.commons.codec.binary.Base64;
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
/*     */ public class EncryptUtil
/*     */ {
/*     */   private static final String CODE = "UTF-8";
/*     */   private static final String KEY = "@#$%^6a7";
/*     */   
/*     */   public static synchronized String encryptSha256(String inputStr) {
/*     */     try {
/*  32 */       MessageDigest md = MessageDigest.getInstance("SHA-256");
/*  33 */       byte[] digest = md.digest(inputStr.getBytes("UTF-8"));
/*  34 */       return new String(Base64.encodeBase64(digest));
/*  35 */     } catch (Exception e) {
/*  36 */       throw new BusinessError(e);
/*     */     } 
/*     */   }
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
/*     */   public static String decrypt(String message) {
/*  53 */     return aesDecrypt(message, "@#$%^6a7");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encrypt(String message) {
/*  64 */     return aesEncrypt(message, "@#$%^6a7");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String aesDecrypt(String encryptContent, String password) {
/*     */     try {
/*  72 */       KeyGenerator keyGen = KeyGenerator.getInstance("AES");
/*  73 */       SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
/*  74 */       secureRandom.setSeed(password.getBytes());
/*  75 */       keyGen.init(128, secureRandom);
/*  76 */       SecretKey secretKey = keyGen.generateKey();
/*  77 */       byte[] enCodeFormat = secretKey.getEncoded();
/*  78 */       SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
/*  79 */       Cipher cipher = Cipher.getInstance("AES");
/*  80 */       cipher.init(2, key);
/*  81 */       return new String(cipher.doFinal(hex2Bytes(encryptContent)));
/*  82 */     } catch (Exception e) {
/*  83 */       e.printStackTrace();
/*  84 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String aesEncrypt(String content, String password) {
/*     */     try {
/*  93 */       KeyGenerator keyGen = KeyGenerator.getInstance("AES");
/*  94 */       SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
/*  95 */       secureRandom.setSeed(password.getBytes());
/*  96 */       keyGen.init(128, secureRandom);
/*  97 */       SecretKey secretKey = keyGen.generateKey();
/*  98 */       byte[] enCodeFormat = secretKey.getEncoded();
/*  99 */       SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
/* 100 */       Cipher cipher = Cipher.getInstance("AES");
/* 101 */       cipher.init(1, key);
/* 102 */       return byte2Hex(cipher.doFinal(content.getBytes("UTF-8")));
/* 103 */     } catch (Exception e) {
/* 104 */       e.printStackTrace();
/* 105 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String byte2Hex(byte[] srcBytes) {
/* 113 */     StringBuilder hexRetSB = new StringBuilder();
/* 114 */     for (byte b : srcBytes) {
/* 115 */       String hexString = Integer.toHexString(0xFF & b);
/* 116 */       hexRetSB.append((hexString.length() == 1) ? Integer.valueOf(0) : "").append(hexString);
/*     */     } 
/* 118 */     return hexRetSB.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] hex2Bytes(String source) {
/* 125 */     byte[] sourceBytes = new byte[source.length() / 2];
/* 126 */     for (int i = 0; i < sourceBytes.length; i++) {
/* 127 */       sourceBytes[i] = (byte)Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
/*     */     }
/* 129 */     return sourceBytes;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/encrypt/EncryptUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */