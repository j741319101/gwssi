/*    */ package com.dstz.bpm.plugin.core.manager.impl;
/*    */ 
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import com.dstz.bpm.plugin.core.dao.BpmReminderLogDao;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
/*    */
import com.dstz.bpm.plugin.core.model.BpmReminderLog;
/*    */ import com.dstz.bpm.plugin.global.leaderTask.context.LeaderTaskPlaginContext;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.org.api.service.LeaderService;
/*    */ import com.dstz.sys.util.ContextUtil;
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
/* 31 */     LeaderTaskPlaginContext taskPlaginContext = (LeaderTaskPlaginContext)AppUtil.getBean("leaderTaskPlaginContext");
/* 32 */     if (taskPlaginContext.isEffective()) {
/* 33 */       LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 34 */       IUser leader = leaderService.getUserBySecretaryId(ContextUtil.getCurrentUserId());
/* 35 */       if (leader != null);
/*    */     } 
/*    */ 
/*    */     
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeByInstId(String instId) {
/* 44 */     this.bpmReminderLogDao.removeByInstId(instId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/impl/BpmReminderLogManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */