/*    */ package com.dstz.bpm.plugin.core.manager.impl;
/*    */ 
/*    */ import com.dstz.bpm.plugin.core.dao.BpmCarbonCopyRecordDao;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("bpmCarbonCopyRecordManager")
/*    */ public class BpmCarbonCopyRecordManagerImpl
/*    */   extends BaseManager<String, BpmCarbonCopyRecord>
/*    */   implements BpmCarbonCopyRecordManager
/*    */ {
/*    */   @Autowired
/*    */   private BpmCarbonCopyRecordDao bpmCarbonCopyRecordDao;
/*    */   
/*    */   public int createList(List<BpmCarbonCopyRecord> records) {
/* 25 */     return this.bpmCarbonCopyRecordDao.createList(records);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeByInstId(String instId) {
/* 30 */     this.bpmCarbonCopyRecordDao.removeByInstId(instId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/impl/BpmCarbonCopyRecordManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */