/*    */ package cn.gwssi.ecloudbpm.wf.plugin.core.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.dao.BpmCarbonCopyReceiveDao;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyReceive;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
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
/*    */   @Resource
/*    */   private BpmCarbonCopyReceiveDao bpmCarbonCopyReceiveDao;
/*    */   
/*    */   public int createList(List<BpmCarbonCopyReceive> records) {
/* 34 */     return this.bpmCarbonCopyReceiveDao.createList(records);
/*    */   }
/*    */ 
/*    */   
/*    */   public int updateRead(BpmCarbonCopyReceive record, Set<String> primaryKeys) {
/* 39 */     record.setRead(Boolean.TRUE);
/* 40 */     record.setUpdateTime(new Date());
/* 41 */     return this.bpmCarbonCopyReceiveDao.updateRead(record, primaryKeys);
/*    */   }
/*    */ 
/*    */   
/*    */   public int updateReadByUser(String userId) {
/* 46 */     return this.bpmCarbonCopyReceiveDao.updateReadByUser(userId);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter queryFilter) {
/* 51 */     return this.bpmCarbonCopyReceiveDao.listUserReceiveList(queryFilter);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeByInstId(String instId) {
/* 56 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 57 */     defaultQueryFilter.addFilter("b.inst_id", instId, QueryOP.EQUAL);
/* 58 */     this.bpmCarbonCopyReceiveDao.listUserReceiveList((QueryFilter)defaultQueryFilter).forEach(bpmUserReceive -> this.bpmCarbonCopyReceiveDao.remove(bpmUserReceive.getId()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<BpmCarbonCopyReceive> query2(QueryFilter queryFilter) {
/* 65 */     return this.bpmCarbonCopyReceiveDao.query2(queryFilter);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BpmCarbonCopyReceive> getByParam(String instId, String receiveUserId, String nodeId) {
/* 70 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 71 */     defaultQueryFilter.setPage(null);
/* 72 */     if (StringUtil.isNotEmpty(receiveUserId)) {
/* 73 */       defaultQueryFilter.addFilter("a.receive_user_id", receiveUserId, QueryOP.EQUAL);
/*    */     }
/* 75 */     if (StringUtil.isNotEmpty(instId)) {
/* 76 */       defaultQueryFilter.addFilter("b.inst_id", instId, QueryOP.EQUAL);
/*    */     }
/* 78 */     if (StringUtil.isNotEmpty(nodeId)) {
/* 79 */       defaultQueryFilter.addFilter("b.node_id", nodeId, QueryOP.EQUAL);
/*    */     }
/* 81 */     return query2((QueryFilter)defaultQueryFilter);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/impl/BpmCarbonCopyReceiveManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */