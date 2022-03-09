/*    */ package com.dstz.bpm.plugin.global.reminder.schedule;
/*    */ 
/*    */ import com.dstz.bpm.plugin.core.manager.BpmReminderTriggerManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
/*    */ import java.util.List;
/*    */ import org.apache.commons.collections.CollectionUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.scheduling.annotation.Scheduled;
/*    */ import org.springframework.stereotype.Component;
/*    */ import org.springframework.transaction.annotation.Transactional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ReninderSpringTaskSchedule
/*    */ {
/*    */   @Value("${job.schedule.spring.task}")
/*    */   private String scheduleType;
/*    */   private static final String cron = "1 * * * * *";
/*    */   @Autowired
/*    */   BpmReminderTriggerManager reminderTriggerManager;
/*    */   @Autowired
/*    */   ReminderSchedule reminderSchedule;
/*    */   
/*    */   @Transactional
/*    */   @Scheduled(cron = "1 * * * * *")
/*    */   public void execute() {
/* 31 */     if (!"true".equals(this.scheduleType)) {
/*    */       return;
/*    */     }
/* 34 */     List<BpmReminderTrigger> reminderTriggerList = this.reminderTriggerManager.getDueMinders();
/* 35 */     if (CollectionUtils.isEmpty(reminderTriggerList)) {
/*    */       return;
/*    */     }
/* 38 */     reminderTriggerList.forEach(reminder -> this.reminderSchedule.triggerReminder(reminder));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/reminder/schedule/ReninderSpringTaskSchedule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */