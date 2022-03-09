/*    */ package cn.gwssi.ecloudbpm.wf.plugin.core.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.dao.BpmReminderTriggerDao;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmReminderTriggerManager;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmReminderTrigger;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
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


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/impl/BpmReminderTriggerManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */