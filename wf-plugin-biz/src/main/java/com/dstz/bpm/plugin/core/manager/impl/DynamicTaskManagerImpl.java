/*    */ package com.dstz.bpm.plugin.core.manager.impl;
/*    */ 
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.api.query.QueryOP;
/*    */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import com.dstz.bpm.plugin.core.dao.DynamicTaskDao;
/*    */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*    */
import com.dstz.bpm.plugin.core.model.DynamicTask;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("dynamicTaskManager")
/*    */ public class DynamicTaskManagerImpl
/*    */   extends BaseManager<String, DynamicTask>
/*    */   implements DynamicTaskManager
/*    */ {
/*    */   @Resource
/*    */   DynamicTaskDao dynamicTaskDao;
/*    */   
/*    */   public DynamicTask getByStatus(String nodeId, String actExecutionId, String status) {
/* 27 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 28 */     defaultQueryFilter.addFilter("node_id_", nodeId, QueryOP.EQUAL);
/* 29 */     defaultQueryFilter.addFilter("act_execution_id_", actExecutionId, QueryOP.EQUAL);
/* 30 */     defaultQueryFilter.addFilter("status_", status, QueryOP.EQUAL);
/* 31 */     defaultQueryFilter.addFieldSort("create_time_", "desc");
/* 32 */     return (DynamicTask)queryOne((QueryFilter)defaultQueryFilter);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DynamicTask getDynamicTaskSettingByInstanceId(String instanceId, String nodeId) {
/* 38 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 39 */     defaultQueryFilter.addFilter("inst_id_", instanceId, QueryOP.EQUAL);
/* 40 */     defaultQueryFilter.addFilter("node_id_", nodeId, QueryOP.EQUAL);
/* 41 */     defaultQueryFilter.addFieldSort("status_", "desc");
/* 42 */     defaultQueryFilter.addFieldSort("ammount_", "desc");
/* 43 */     return (DynamicTask)queryOne((QueryFilter)defaultQueryFilter);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/impl/DynamicTaskManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */