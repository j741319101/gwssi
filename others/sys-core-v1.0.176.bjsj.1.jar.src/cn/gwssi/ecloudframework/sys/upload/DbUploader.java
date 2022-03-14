/*    */ package com.dstz.sys.upload;
/*    */ 
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.id.IdUtil;
/*    */ import com.dstz.sys.core.model.SysFile;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
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
/*    */ @Service
/*    */ public class DbUploader
/*    */   extends AbstractUploader
/*    */ {
/*    */   @Autowired
/*    */   JdbcTemplate jdbcTemplate;
/*    */   
/*    */   public String type() {
/* 35 */     return "db";
/*    */   }
/*    */ 
/*    */   
/*    */   public String upload(InputStream is, String name) {
/*    */     try {
/* 41 */       String id = IdUtil.getSuid();
/* 42 */       this.jdbcTemplate.update("INSERT INTO db_uploader VALUES (?,?)", new Object[] { id, IOUtils.toByteArray(is) });
/* 43 */       return id;
/* 44 */     } catch (IOException e) {
/* 45 */       throw new BusinessException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream take(String path) {
/* 51 */     List<Map<String, Object>> mapList = this.jdbcTemplate.queryForList("select * from db_uploader where id_ = ?", new Object[] { path });
/* 52 */     byte[] bytes = (byte[])((Map)mapList.get(0)).get("bytes_");
/* 53 */     return new ByteArrayInputStream(bytes);
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(String path) {
/* 58 */     this.jdbcTemplate.update("delete from db_uploader where id_ = ?", new Object[] { path });
/*    */   }
/*    */ 
/*    */   
/*    */   public String uploadUrl(SysFile sysFile) {
/* 63 */     return "/sys/sysFile/upload/";
/*    */   }
/*    */ 
/*    */   
/*    */   public String downloadUrl(SysFile sysFile) {
/* 68 */     return "/sys/sysFile/download?fileId=" + sysFile.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   public String modify(InputStream is, String name, String dbFilePath) {
/*    */     try {
/* 74 */       this.jdbcTemplate.update("UPDATE db_uploader SET bytes_ = ? WHERE id_ = ?", new Object[] { IOUtils.toByteArray(is), dbFilePath });
/* 75 */       return dbFilePath;
/* 76 */     } catch (IOException e) {
/* 77 */       throw new BusinessException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/upload/DbUploader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */