/*     */ package com.dstz.base.core.util.sm3;
/*     */ 
/*     */ import org.apache.commons.codec.binary.Hex;
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
/*     */ public class SM3Digest
/*     */ {
/*     */   private static final int BYTE_LENGTH = 32;
/*     */   private static final int BLOCK_LENGTH = 64;
/*     */   private static final int BUFFER_LENGTH = 64;
/*  26 */   private byte[] xBuf = new byte[64];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int xBufOff;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   private byte[] V = (byte[])SM3.iv.clone();
/*     */   
/*  38 */   private int cntBlock = 0;
/*     */ 
/*     */   
/*     */   public SM3Digest() {}
/*     */   
/*     */   public SM3Digest(SM3Digest t) {
/*  44 */     System.arraycopy(t.xBuf, 0, this.xBuf, 0, t.xBuf.length);
/*  45 */     this.xBufOff = t.xBufOff;
/*  46 */     System.arraycopy(t.V, 0, this.V, 0, t.V.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] out, int outOff) {
/*  57 */     byte[] tmp = doFinal();
/*  58 */     System.arraycopy(tmp, 0, out, 0, tmp.length);
/*  59 */     return 32;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  63 */     this.xBufOff = 0;
/*  64 */     this.cntBlock = 0;
/*  65 */     this.V = (byte[])SM3.iv.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(byte[] in, int inOff, int len) {
/*  76 */     int partLen = 64 - this.xBufOff;
/*  77 */     int inputLen = len;
/*  78 */     int dPos = inOff;
/*  79 */     if (partLen < inputLen) {
/*  80 */       System.arraycopy(in, dPos, this.xBuf, this.xBufOff, partLen);
/*  81 */       inputLen -= partLen;
/*  82 */       dPos += partLen;
/*  83 */       doUpdate();
/*  84 */       while (inputLen > 64) {
/*  85 */         System.arraycopy(in, dPos, this.xBuf, 0, 64);
/*  86 */         inputLen -= 64;
/*  87 */         dPos += 64;
/*  88 */         doUpdate();
/*     */       } 
/*     */     } 
/*     */     
/*  92 */     System.arraycopy(in, dPos, this.xBuf, this.xBufOff, inputLen);
/*  93 */     this.xBufOff += inputLen;
/*     */   }
/*     */   
/*     */   private void doUpdate() {
/*  97 */     byte[] B = new byte[64];
/*  98 */     for (int i = 0; i < 64; i += 64) {
/*  99 */       System.arraycopy(this.xBuf, i, B, 0, B.length);
/* 100 */       doHash(B);
/*     */     } 
/* 102 */     this.xBufOff = 0;
/*     */   }
/*     */   
/*     */   private void doHash(byte[] B) {
/* 106 */     byte[] tmp = SM3.CF(this.V, B);
/* 107 */     System.arraycopy(tmp, 0, this.V, 0, this.V.length);
/* 108 */     this.cntBlock++;
/*     */   }
/*     */   
/*     */   private byte[] doFinal() {
/* 112 */     byte[] B = new byte[64];
/* 113 */     byte[] buffer = new byte[this.xBufOff];
/* 114 */     System.arraycopy(this.xBuf, 0, buffer, 0, buffer.length);
/* 115 */     byte[] tmp = SM3.padding(buffer, this.cntBlock);
/* 116 */     for (int i = 0; i < tmp.length; i += 64) {
/* 117 */       System.arraycopy(tmp, i, B, 0, B.length);
/* 118 */       doHash(B);
/*     */     } 
/* 120 */     return this.V;
/*     */   }
/*     */   
/*     */   public void update(byte in) {
/* 124 */     byte[] buffer = { in };
/* 125 */     update(buffer, 0, 1);
/*     */   }
/*     */   
/*     */   public int getDigestSize() {
/* 129 */     return 32;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 133 */     byte[] md = new byte[32];
/* 134 */     byte[] msg1 = "abc".getBytes();
/* 135 */     SM3Digest sm3 = new SM3Digest();
/* 136 */     sm3.update(msg1, 0, msg1.length);
/* 137 */     sm3.doFinal(md, 0);
/* 138 */     String s = new String(Hex.encodeHex(md));
/* 139 */     System.out.println(s);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/sm3/SM3Digest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */