/*    */ package com.dstz.bpm.plugin.core.manager.impl;
/*    */ 
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import com.dstz.bpm.plugin.core.dao.BpmReminderTriggerDao;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmReminderTriggerManager;
/*    */
import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
/*    */ import java.util.Date;
/*    */ import java.util.List;
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
/*    */ @Service("bpmReminderTriggerManager")
/*    */ public class BpmReminderTriggerManagerImpl
/*    */   extends BaseManager<String, BpmReminderTrigger>
/*    */   implements BpmReminderTriggerManager
/*    */ {
/*    */   @Resource
/*    */   BpmReminderTriggerDao bpmReminderTriggerDao;
/*    */   
/*    */   public void removeByTaskId(String taskId) {
/* 29 */     if (StringUtil.isEmpty(taskId))
/*    */       return; 
/* 31 */     if (this.bpmReminderTriggerDao.queryTaskReminderCount(taskId) < 1)
/*    */       return; 
/* 33 */     this.bpmReminderTriggerDao.removeByTaskId(taskId);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BpmReminderTrigger> getDueMinders() {
/* 38 */     return this.bpmReminderTriggerDao.getDueMinders(new Date());
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/impl/BpmReminderTriggerManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */