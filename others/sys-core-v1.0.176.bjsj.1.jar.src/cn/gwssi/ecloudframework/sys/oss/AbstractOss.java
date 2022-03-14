/*    */ package com.dstz.sys.oss;
/*    */ 
/*    */ import cn.hutool.core.date.DateUtil;
/*    */ import java.io.InputStream;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ public abstract class AbstractOss
/*    */ {
/* 10 */   protected String bucketName = null;
/* 11 */   protected String path = null;
/*    */   
/*    */   public String getBucketName() {
/* 14 */     return this.bucketName;
/*    */   }
/*    */   
/*    */   public void setBucketName(String bucketName) {
/* 18 */     this.bucketName = bucketName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPath(String path) {
/* 23 */     this.path = path + DateUtil.format(new Date(), "yyyyMMdd") + "/";
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 27 */     return this.path;
/*    */   }
/*    */   
/*    */   public abstract String putObject(String paramString, InputStream paramInputStream);
/*    */   
/*    */   public abstract InputStream getObject(String paramString);
/*    */   
/*    */   public abstract void removeObject(String paramString);
/*    */   
/*    */   public abstract String presignedPutObject(String paramString);
/*    */   
/*    */   public abstract String presignedGetObject(String paramString);
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/oss/AbstractOss.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */