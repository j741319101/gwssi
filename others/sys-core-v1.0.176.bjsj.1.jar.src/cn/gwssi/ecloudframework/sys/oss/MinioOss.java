/*    */ package com.dstz.sys.oss;
/*    */ 
/*    */ import io.minio.MinioClient;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ 
/*    */ public class MinioOss
/*    */   extends AbstractOss {
/*    */   @Autowired
/* 10 */   private MinioClient minioClient = null;
/*    */ 
/*    */ 
/*    */   
/*    */   public String putObject(String fileName, InputStream stream) {
/*    */     try {
/* 16 */       String objectName = this.path.concat(fileName);
/* 17 */       this.minioClient.putObject(this.bucketName, objectName, stream, "application/octet-stream");
/* 18 */       return objectName;
/* 19 */     } catch (Exception e) {
/* 20 */       throw new RuntimeException("minio上传失败", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getObject(String objectName) {
/*    */     try {
/* 27 */       return this.minioClient.getObject(this.bucketName, objectName);
/* 28 */     } catch (Exception e) {
/* 29 */       throw new RuntimeException("下载失败", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeObject(String objectName) {
/*    */     try {
/* 36 */       this.minioClient.removeObject(this.bucketName, objectName);
/* 37 */     } catch (Exception e) {
/* 38 */       throw new RuntimeException("删除失败", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String presignedPutObject(String objectName) {
/*    */     try {
/* 46 */       return this.minioClient.presignedPutObject(this.bucketName, objectName, Integer.valueOf(86400));
/* 47 */     } catch (Exception e) {
/* 48 */       throw new RuntimeException("获取上传地址失败", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String presignedGetObject(String objectName) {
/*    */     try {
/* 55 */       return this.minioClient.presignedGetObject(this.bucketName, objectName, Integer.valueOf(86400));
/* 56 */     } catch (Exception e) {
/* 57 */       throw new RuntimeException("获取下载地址失败", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/oss/MinioOss.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */