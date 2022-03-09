/*     */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.core.dao.SysFileDao;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.SysFileManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.SysFile;
/*     */ import cn.gwssi.ecloudframework.sys.upload.IUploader;
/*     */ import cn.gwssi.ecloudframework.sys.upload.UploaderFactory;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("sysFileManager")
/*     */ public class SysFileManagerImpl
/*     */   extends BaseManager<String, SysFile>
/*     */   implements SysFileManager
/*     */ {
/*     */   @Resource
/*     */   SysFileDao sysFileDao;
/*     */   @Resource
/*     */   UserService userService;
/*     */   
/*     */   public SysFile upload(InputStream is, String fileName) {
/*  37 */     String ext = (fileName.lastIndexOf('.') != -1) ? fileName.substring(fileName.lastIndexOf('.')) : "";
/*  38 */     String id = IdUtil.getSuid();
/*     */     
/*  40 */     IUploader uploader = UploaderFactory.getDefault();
/*     */     
/*  42 */     String path = uploader.upload(is, id + ext);
/*     */ 
/*     */     
/*  45 */     SysFile sysFile = new SysFile();
/*  46 */     sysFile.setId(id);
/*  47 */     sysFile.setName(fileName);
/*  48 */     sysFile.setUploader(uploader.type());
/*  49 */     sysFile.setPath(path);
/*  50 */     create((Serializable)sysFile);
/*     */     
/*  52 */     return sysFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public void upload(InputStream is, SysFile sysFile) {
/*  57 */     String fileName = sysFile.getName();
/*  58 */     String uploaderType = sysFile.getUploader();
/*  59 */     String ext = (fileName.lastIndexOf('.') != -1) ? fileName.substring(fileName.lastIndexOf('.')) : "";
/*  60 */     String id = IdUtil.getSuid();
/*     */     
/*  62 */     IUploader uploader = UploaderFactory.getDefault();
/*  63 */     if (StringUtil.isNotEmpty(uploaderType)) {
/*  64 */       uploader = UploaderFactory.getUploader(uploaderType);
/*     */     } else {
/*  66 */       sysFile.setUploader(uploader.type());
/*     */     } 
/*     */ 
/*     */     
/*  70 */     String path = uploader.upload(is, id + ext);
/*     */ 
/*     */     
/*  73 */     sysFile.setId(id);
/*  74 */     sysFile.setPath(path);
/*  75 */     create((Serializable)sysFile);
/*     */   }
/*     */ 
/*     */   
/*     */   public void modify(InputStream inputStream, SysFile newFileInfo, String fileId) {
/*     */     IUploader newUploader;
/*  81 */     SysFile oldFileInfo = get(fileId);
/*  82 */     String oldUploaderStr = oldFileInfo.getUploader(), newUploaderStr = newFileInfo.getUploader();
/*  83 */     IUploader oldUploader = UploaderFactory.getUploader(oldUploaderStr);
/*  84 */     if (StringUtil.isNotEmpty(newUploaderStr)) {
/*  85 */       newUploader = UploaderFactory.getUploader(newUploaderStr);
/*  86 */       oldFileInfo.setUploader(newUploaderStr);
/*     */     } else {
/*  88 */       newUploader = oldUploader;
/*     */     } 
/*  90 */     String newFileInfoName = newFileInfo.getName();
/*  91 */     String ext = (newFileInfoName.lastIndexOf('.') != -1) ? newFileInfoName.substring(newFileInfoName.lastIndexOf('.')) : "";
/*  92 */     String oldPath = oldFileInfo.getPath();
/*  93 */     String path = newUploader.modify(inputStream, fileId + ext, oldPath);
/*     */ 
/*     */     
/*  96 */     if (!oldPath.equals(path)) {
/*     */       
/*  98 */       oldUploader.remove(oldFileInfo.getPath());
/*  99 */       oldFileInfo.setPath(path);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 104 */     oldFileInfo.setName(newFileInfoName);
/* 105 */     update((Serializable)oldFileInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map uploadUrl(String fileName, String remark, String uploaderType) {
/* 111 */     IUploader uploader = UploaderFactory.getDefault();
/* 112 */     if (StringUtil.isNotEmpty(uploaderType)) {
/* 113 */       uploader = UploaderFactory.getUploader(uploaderType);
/*     */     }
/*     */     
/* 116 */     String id = IdUtil.getSuid();
/* 117 */     SysFile sysFile = new SysFile();
/* 118 */     sysFile.setId(id);
/* 119 */     sysFile.setName(fileName);
/* 120 */     sysFile.setUploader(uploader.type());
/* 121 */     sysFile.setRemark(remark);
/*     */     
/* 123 */     String url = uploader.uploadUrl(sysFile);
/* 124 */     create((Serializable)sysFile);
/* 125 */     Map<String, String> uploadMap = new HashMap<>();
/* 126 */     uploadMap.put("fileid", sysFile.getId());
/* 127 */     uploadMap.put("path", sysFile.getPath());
/* 128 */     uploadMap.put("url", url);
/* 129 */     return uploadMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String downloadUrl(String fileId) {
/* 134 */     SysFile sysFile = get(fileId);
/* 135 */     IUploader uploader = UploaderFactory.getUploader(sysFile.getUploader());
/* 136 */     return uploader.downloadUrl(sysFile);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream download(String fileId) {
/* 141 */     SysFile sysFile = get(fileId);
/* 142 */     IUploader uploader = UploaderFactory.getUploader(sysFile.getUploader());
/* 143 */     return uploader.take(sysFile.getPath());
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(String fileId) {
/* 148 */     SysFile sysFile = get(fileId);
/* 149 */     IUploader uploader = UploaderFactory.getUploader(sysFile.getUploader());
/* 150 */     uploader.remove(sysFile.getPath());
/* 151 */     remove(fileId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateInstid(String instId, String fileId) {
/* 159 */     this.sysFileDao.updateInstid(instId, fileId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SysFile> getFileByInstId(String instId) {
/* 164 */     return this.sysFileDao.getFileByInstId(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SysFile> query(QueryFilter queryFilter) {
/* 169 */     List<SysFile> sysFiles = this.dao.query(queryFilter);
/* 170 */     sysFiles.forEach(sysFile -> {
/*     */           IUser user = this.userService.getUserById(sysFile.getCreateBy());
/*     */           if (user != null) {
/*     */             sysFile.setCreator(user.getFullname());
/*     */           }
/*     */         });
/* 176 */     return sysFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SysFile get(String id) {
/* 182 */     return (SysFile)this.sysFileDao.get(id);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SysFileManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */