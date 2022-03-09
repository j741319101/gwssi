/*    */ package com.dstz.bpm.plugin.core.manager.impl;
/*    */ 
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.api.query.QueryOP;
/*    */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import com.dstz.bpm.plugin.core.dao.BpmCarbonCopyReceiveDao;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*    */
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
/*    */ import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("bpmCarbonCopyReceiveManager")
/*    */ public class BpmCarbonCopyReceiveManagerImpl
/*    */   extends BaseManager<String, BpmCarbonCopyReceive>
/*    */   implements BpmCarbonCopyReceiveManager
/*    */ {
/*    */   @Autowired
/*    */   private BpmCarbonCopyReceiveDao bpmCarbonCopyReceiveDao;
/*    */   
/*    */   public int createList(List<BpmCarbonCopyReceive> records) {
/* 31 */     return this.bpmCarbonCopyReceiveDao.createList(records);
/*    */   }
/*    */ 
/*    */   
/*    */   public int updateRead(BpmCarbonCopyReceive record, Set<String> primaryKeys) {
/* 36 */     return this.bpmCarbonCopyReceiveDao.updateRead(record, primaryKeys);
/*    */   }
/*    */ 
/*    */   
/*    */   public int updateReadByUser() {
/* 41 */     return this.bpmCarbonCopyReceiveDao.updateReadByUser(ContextUtil.getCurrentUserId());
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter queryFilter) {
/* 46 */     return this.bpmCarbonCopyReceiveDao.listUserReceiveList(queryFilter);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeByInstId(String instId) {
/* 51 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 52 */     defaultQueryFilter.addFilter("b.inst_id", instId, QueryOP.EQUAL);
/* 53 */     this.bpmCarbonCopyReceiveDao.listUserReceiveList((QueryFilter)defaultQueryFilter).forEach(bpmUserReceive -> this.bpmCarbonCopyReceiveDao.remove(bpmUserReceive.getId()));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/impl/BpmCarbonCopyReceiveManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */