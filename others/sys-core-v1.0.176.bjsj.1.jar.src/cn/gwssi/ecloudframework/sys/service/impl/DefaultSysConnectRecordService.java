/*    */ package com.dstz.sys.service.impl;
/*    */ 
/*    */ import com.dstz.base.api.aop.annotion.ParamValidate;
/*    */ import com.dstz.base.api.exception.BusinessMessage;
/*    */ import com.dstz.base.core.util.BeanCopierUtils;
/*    */ import com.dstz.sys.api.model.SysConnectRecordDTO;
/*    */ import com.dstz.sys.api.service.SysConnectRecordService;
/*    */ import com.dstz.sys.core.manager.SysConnectRecordManager;
/*    */ import com.dstz.sys.core.model.SysConnectRecord;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class DefaultSysConnectRecordService
/*    */   implements SysConnectRecordService
/*    */ {
/*    */   @Autowired
/*    */   SysConnectRecordManager connectRecordMananger;
/*    */   
/*    */   public List<SysConnectRecordDTO> getByTargetId(String id, String type) {
/* 25 */     List<SysConnectRecord> list = this.connectRecordMananger.getByTargetId(id, type);
/* 26 */     return BeanCopierUtils.transformList(list, SysConnectRecordDTO.class);
/*    */   }
/*    */   
/*    */   @ParamValidate
/*    */   public void save(List<SysConnectRecordDTO> records) {
/* 31 */     List<SysConnectRecord> recordsList = BeanCopierUtils.transformList(records, SysConnectRecord.class);
/* 32 */     this.connectRecordMananger.bulkCreate(recordsList);
/*    */   }
/*    */ 
/*    */   
/*    */   @ParamValidate
/*    */   public void save(SysConnectRecordDTO records) {
/* 38 */     this.connectRecordMananger.create(BeanCopierUtils.transformBean(records, SysConnectRecord.class));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeBySourceId(String sourceId, String type) {
/* 44 */     this.connectRecordMananger.removeBySourceId(sourceId, type);
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkIsRelatedWithBusinessMessage(String targetId, String type) {
/* 49 */     List<SysConnectRecord> list = this.connectRecordMananger.getByTargetId(targetId, type);
/* 50 */     if (list.isEmpty())
/*    */       return; 
/* 52 */     Set<String> notices = new HashSet<>();
/*    */     
/* 54 */     StringBuilder sb = new StringBuilder();
/* 55 */     list.forEach(record -> {
/*    */           if (!notices.contains(record.getNotice())) {
/*    */             if (sb.length() > 0) {
/*    */               sb.append("<br/>");
/*    */             }
/*    */             
/*    */             sb.append(record.getNotice());
/*    */             
/*    */             notices.add(record.getNotice());
/*    */           } 
/*    */         });
/*    */     
/* 67 */     throw new BusinessMessage(sb.toString());
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/DefaultSysConnectRecordService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */