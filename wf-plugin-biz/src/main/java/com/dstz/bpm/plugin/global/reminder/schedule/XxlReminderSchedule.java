///*    */ package com.dstz.bpm.plugin.global.reminder.schedule;
///*    */
///*    */ import com.dstz.bpm.plugin.core.manager.BpmReminderTriggerManager;
///*    */ import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
///*    */ import com.xxl.job.core.biz.model.ReturnT;
///*    */ import com.xxl.job.core.handler.IJobHandler;
///*    */ import com.xxl.job.core.handler.annotation.JobHandler;
///*    */ import com.xxl.job.core.log.XxlJobLogger;
///*    */ import java.util.List;
///*    */ import org.apache.commons.collections.CollectionUtils;
///*    */ import org.springframework.beans.factory.annotation.Autowired;
///*    */ import org.springframework.stereotype.Component;
///*    */ import org.springframework.transaction.annotation.Transactional;
///*    */
///*    */
///*    */
///*    */
///*    */
///*    */
///*    */
///*    */
///*    */
///*    */ @JobHandler("abReminderJob")
///*    */ @Component
///*    */ public class XxlReminderSchedule
///*    */   extends IJobHandler
///*    */ {
///*    */   @Autowired
///*    */   BpmReminderTriggerManager reminderTriggerManager;
///*    */   @Autowired
///*    */   ReminderSchedule reminderSchedule;
///*    */
///*    */   @Transactional
///*    */   public ReturnT<String> execute(String s) throws Exception {
///* 35 */     List<BpmReminderTrigger> reminderTriggerList = this.reminderTriggerManager.getDueMinders();
///* 36 */     if (CollectionUtils.isEmpty(reminderTriggerList)) {
///* 37 */       XxlJobLogger.log("【 No tasks to be reminded 】", new Object[0]);
///* 38 */       return IJobHandler.SUCCESS;
///*    */     }
///* 40 */     reminderTriggerList.forEach(reminder -> this.reminderSchedule.triggerReminder(reminder));
///* 41 */     XxlJobLogger.log("【 reminded {} task 】", new Object[] { Integer.valueOf(reminderTriggerList.size()) });
///* 42 */     return IJobHandler.SUCCESS;
///*    */   }
///*    */ }
//
//
///* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/reminder/schedule/XxlReminderSchedule.class
// * Java compiler version: 8 (52.0)
// * JD-Core Version:       1.1.3
// */