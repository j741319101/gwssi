/*    */ package com.dstz.bpm.plugin.core.manager.impl;
/*    */ 
/*    */ import com.dstz.bpm.plugin.core.dao.BpmReminderLogDao;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmReminderLog;
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("bpmReminderLogManager")
/*    */ public class BpmReminderLogManagerImpl
/*    */   extends BaseManager<String, BpmReminderLog>
/*    */   implements BpmReminderLogManager
/*    */ {
/*    */   @Resource
/*    */   BpmReminderLogDao bpmReminderLogDao;
/*    */   
/*    */   public List<BpmReminderLog> getList(QueryFilter queryFilter) {
/* 26 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeByInstId(String instId) {
/* 31 */     this.bpmReminderLogDao.removeByInstId(instId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/impl/BpmReminderLogManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */