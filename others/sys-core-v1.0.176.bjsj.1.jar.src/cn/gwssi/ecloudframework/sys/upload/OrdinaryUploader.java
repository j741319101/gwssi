/*    */ package cn.gwssi.ecloudframework.sys.upload;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.core.util.PropertyUtil;
/*    */ import cn.gwssi.ecloudframework.sys.core.model.SysFile;
/*    */ import cn.hutool.core.date.DateUtil;
/*    */ import cn.hutool.core.io.FileUtil;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.util.Date;
/*    */ import org.springframework.stereotype.Service;
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
/*    */ @Service
/*    */ public class OrdinaryUploader
/*    */   extends AbstractUploader
/*    */ {
/*    */   public String type() {
/* 33 */     return "ordinary";
/*    */   }
/*    */ 
/*    */   
/*    */   public String upload(InputStream is, String name) {
/* 38 */     FileUtil.writeFromStream(is, getPath(name));
/* 39 */     return getPath(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream take(String path) {
/*    */     try {
/* 45 */       return new FileInputStream(new File(path));
/* 46 */     } catch (Exception e) {
/* 47 */       throw new BusinessException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(String path) {
/* 53 */     FileUtil.del(path);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String uploadUrl(SysFile sysFile) {
/* 59 */     return "/sys/sysFile/upload/";
/*    */   }
/*    */ 
/*    */   
/*    */   public String downloadUrl(SysFile sysFile) {
/* 64 */     return "/sys/sysFile/download?fileId=" + sysFile.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   public String modify(InputStream is, String name, String dbFilePath) {
/* 69 */     return upload(is, name);
/*    */   }
/*    */   
/*    */   private String getPath(String name) {
/* 73 */     return PropertyUtil.getProperty("ecloud.uploader.ordinary.path") + DateUtil.format(new Date(), "yyyyMMdd") + File.separator + name;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/upload/OrdinaryUploader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */