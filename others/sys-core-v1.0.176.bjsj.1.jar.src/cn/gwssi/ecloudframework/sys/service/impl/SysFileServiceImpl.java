/*    */ package cn.gwssi.ecloudframework.sys.service.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.util.BeanCopierUtils;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.dto.SysFileDTO;
/*    */ import cn.gwssi.ecloudframework.sys.api.service.SysFileService;
/*    */ import cn.gwssi.ecloudframework.sys.core.manager.SysFileManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.model.SysFile;
/*    */ import cn.hutool.core.util.ArrayUtil;
/*    */ import java.io.InputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ 
/*    */ @Service
/*    */ public class SysFileServiceImpl
/*    */   implements SysFileService
/*    */ {
/*    */   @Autowired
/*    */   private SysFileManager sysFileMananger;
/*    */   
/*    */   public SysFileDTO upload(InputStream is, String fileName) {
/* 25 */     SysFile file = this.sysFileMananger.upload(is, fileName);
/* 26 */     return (SysFileDTO)BeanCopierUtils.transformBean(file, SysFileDTO.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public SysFileDTO upload(InputStream is, SysFileDTO sysFileDTO) {
/* 31 */     SysFile sysFile = (SysFile)BeanCopierUtils.transformBean(sysFileDTO, SysFile.class);
/* 32 */     this.sysFileMananger.upload(is, sysFile);
/* 33 */     return (SysFileDTO)BeanCopierUtils.transformBean(sysFile, SysFileDTO.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream download(String fileId) {
/* 38 */     return this.sysFileMananger.download(fileId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void delete(String... fileId) {
/* 43 */     if (ArrayUtil.isEmpty((Object[])fileId))
/*    */       return; 
/* 45 */     for (String id : fileId) {
/* 46 */       this.sysFileMananger.delete(id);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateInstid(String instId, String fileId) {
/* 52 */     if (StringUtil.isEmpty(instId) || StringUtil.isEmpty(fileId))
/*    */       return; 
/* 54 */     this.sysFileMananger.updateInstid(instId, fileId);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SysFileDTO> getFileByInstId(String instanceId) {
/* 59 */     List<SysFile> sysFiles = this.sysFileMananger.getFileByInstId(instanceId);
/* 60 */     List<SysFileDTO> dtos = new ArrayList<>(0);
/* 61 */     if (!CollectionUtils.isEmpty(sysFiles)) {
/* 62 */       for (SysFile sysFile : sysFiles) {
/* 63 */         dtos.add(BeanCopierUtils.transformBean(sysFile, SysFileDTO.class));
/*    */       }
/*    */     }
/* 66 */     return dtos;
/*    */   }
/*    */ 
/*    */   
/*    */   public SysFileDTO get(String id) {
/* 71 */     SysFile sysFile = this.sysFileMananger.get(id);
/* 72 */     if (null != sysFile) {
/* 73 */       return (SysFileDTO)BeanCopierUtils.transformBean(this.sysFileMananger.get(id), SysFileDTO.class);
/*    */     }
/* 75 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/SysFileServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */