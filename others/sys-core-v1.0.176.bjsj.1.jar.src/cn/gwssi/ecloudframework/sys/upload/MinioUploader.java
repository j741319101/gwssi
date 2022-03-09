/*    */ package cn.gwssi.ecloudframework.sys.upload;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.sys.core.model.SysFile;
/*    */ import cn.gwssi.ecloudframework.sys.oss.MinioOss;
/*    */ import java.io.InputStream;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class MinioUploader
/*    */   extends AbstractUploader {
/* 15 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*    */   @Autowired
/* 17 */   MinioOss minioOss = null;
/*    */ 
/*    */ 
/*    */   
/*    */   public String type() {
/* 22 */     return "minio";
/*    */   }
/*    */ 
/*    */   
/*    */   public String upload(InputStream is, String name) {
/*    */     try {
/* 28 */       return this.minioOss.putObject(name, is);
/* 29 */     } catch (Exception e) {
/* 30 */       this.LOG.error("上传失败", e);
/* 31 */       throw new BusinessException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream take(String name) {
/*    */     try {
/* 38 */       return this.minioOss.getObject(name);
/* 39 */     } catch (Exception e) {
/* 40 */       this.LOG.error("下载失败", e);
/* 41 */       throw new BusinessException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(String name) {
/*    */     try {
/* 48 */       this.minioOss.removeObject(name);
/* 49 */     } catch (Exception e) {
/* 50 */       this.LOG.error("删除失败", e);
/* 51 */       throw new BusinessException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String uploadUrl(SysFile sysFile) {
/*    */     try {
/* 58 */       String fileName = sysFile.getName();
/* 59 */       String ext = fileName.substring(fileName.lastIndexOf('.'));
/* 60 */       String filePath = this.minioOss.getPath().concat(sysFile.getId().concat(ext));
/* 61 */       sysFile.setPath(filePath);
/* 62 */       return this.minioOss.presignedPutObject(filePath);
/* 63 */     } catch (Exception e) {
/* 64 */       this.LOG.error("获取上传链接失败", e);
/* 65 */       throw new BusinessException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String downloadUrl(SysFile sysFile) {
/*    */     try {
/* 72 */       return this.minioOss.presignedGetObject(sysFile.getPath());
/* 73 */     } catch (Exception e) {
/* 74 */       this.LOG.error("获取下载链接失败", e);
/* 75 */       throw new BusinessException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String modify(InputStream is, String name, String dbFilePath) {
/* 81 */     return upload(is, name);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/upload/MinioUploader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */