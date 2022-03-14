/*     */ package com.dstz.bpm.plugin.global.reminder.executer;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmReminderTriggerManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
/*     */ import com.dstz.bpm.plugin.global.reminder.def.Reminder;
/*     */ import com.dstz.bpm.plugin.global.reminder.def.ReminderPluginDef;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.service.CalendarService;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateField;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class ReminderPluginExecuter
/*     */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, ReminderPluginDef>
/*     */ {
/*     */   @Autowired
/*     */   BpmReminderTriggerManager bpmReminderTriggerManager;
/*     */   @Autowired
/*     */   BpmTaskManager taskManager;
/*     */   @Autowired
/*     */   IGroovyScriptEngine scriptEngine;
/*     */   @Autowired
/*     */   CalendarService calendarService;
/*     */   
/*     */   public Void execute(BpmExecutionPluginSession pluginSession, ReminderPluginDef pluginDef) {
/*  51 */     List<Reminder> reminderList = pluginDef.getReminderList();
/*  52 */     if (CollectionUtil.isEmpty(reminderList)) return null;
/*     */ 
/*     */     
/*  55 */     if (pluginSession.getEventType() == EventType.TASK_COMPLETE_EVENT) {
/*  56 */       removeTaskReminders(reminderList);
/*  57 */       return null;
/*     */     } 
/*     */     
/*  60 */     for (Reminder reminder : reminderList) {
/*  61 */       taskReminder(reminder, pluginSession);
/*     */     }
/*     */     
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeTaskReminders(List<Reminder> reminderList) {
/*  69 */     IBpmTask task = getTaskIfTask();
/*  70 */     if (task == null) {
/*     */       return;
/*     */     }
/*  73 */     for (Reminder reminder : reminderList) {
/*  74 */       if (StringUtil.isEmpty(reminder.getNodeId()) || reminder.getNodeId().equals(task.getNodeId())) {
/*  75 */         this.bpmReminderTriggerManager.removeByTaskId(task.getId());
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void taskReminder(Reminder reminder, BpmExecutionPluginSession pluginSession) {
/*  88 */     if (StringUtil.isNotEmpty(reminder.getNodeId()) && 
/*  89 */       !reminder.getNodeId().equals(getActivitiId(pluginSession))) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     if (StringUtil.isNotEmpty(reminder.getConditionScript())) {
/*  94 */       Boolean support = Boolean.valueOf(this.scriptEngine.executeBoolean(reminder.getConditionScript(), (Map)pluginSession));
/*  95 */       if (!support.booleanValue()) {
/*     */         return;
/*     */       }
/*     */     } 
/*  99 */     BpmReminderTrigger reminderTrigger = getReminderTriggerByDef(reminder);
/*     */ 
/*     */     
/* 102 */     Date dueDate = calcDueDate(reminder);
/* 103 */     if (dueDate == null) {
/* 104 */       throw new BusinessException("系统节假日不可用，请联系管理员维护相关信息！");
/*     */     }
/*     */     
/* 107 */     reminderTrigger.setDuedate(dueDate);
/* 108 */     IBpmTask task = getTaskIfTask();
/* 109 */     if (StringUtils.equals(task.getTaskType(), TaskType.SIGN_SOURCE.getKey())) {
/* 110 */       List<BpmTask> tasks = this.taskManager.getByInstId(task.getInstId());
/* 111 */       tasks.stream().filter(t -> StringUtils.equals(t.getTaskType(), TaskType.SIGN.getKey())).forEach(t -> {
/*     */             reminderTrigger.setId(null);
/*     */             reminderTrigger.setTaskId(t.getId());
/*     */             this.bpmReminderTriggerManager.create(reminderTrigger);
/*     */           });
/*     */       return;
/*     */     } 
/* 118 */     reminderTrigger.setTaskId(task.getId());
/* 119 */     this.bpmReminderTriggerManager.create(reminderTrigger);
/*     */   }
/*     */ 
/*     */   
/*     */   private Date calcDueDate(Reminder reminder) {
/* 124 */     if (!reminder.getIsCalcWorkDay().booleanValue()) {
/* 125 */       return (Date)DateUtil.date().offset(DateField.MINUTE, reminder.getTimeLimit().intValue());
/*     */     }
/*     */ 
/*     */     
/* 129 */     int minute = reminder.getTimeLimit().intValue();
/* 130 */     ResultMsg<Date> dateResult = this.calendarService.getEndWorkDayByMinute(new Date(), minute);
/* 131 */     return (Date)dateResult.getData();
/*     */   }
/*     */   
/*     */   private BpmReminderTrigger getReminderTriggerByDef(Reminder reminder) {
/* 135 */     BpmReminderTrigger reminderTrigger = new BpmReminderTrigger();
/* 136 */     reminderTrigger.setBeforeScript(reminder.getBeforeScript());
/* 137 */     reminderTrigger.setHtmlMsg(reminder.getHtmlTemplate());
/* 138 */     reminderTrigger.setIsCalcWorkday(Integer.valueOf(reminder.getIsCalcWorkDay().booleanValue() ? 1 : 0));
/* 139 */     reminderTrigger.setIsUrgent(Integer.valueOf(reminder.getIsUrgent().booleanValue() ? 1 : 0));
/* 140 */     reminderTrigger.setMaxReminderTimes(Integer.valueOf(reminder.getMaxReminderTimes()));
/* 141 */     reminderTrigger.setReminderCycle(Integer.valueOf(reminder.getReminderCycle()));
/* 142 */     reminderTrigger.setReminderDesc(reminder.getDesc());
/* 143 */     reminderTrigger.setTextMsg(reminder.getTextTemplate());
/* 144 */     reminderTrigger.setMsgType(reminder.getMsgType());
/* 145 */     reminderTrigger.setReminderTimes(Integer.valueOf(0));
/* 146 */     return reminderTrigger;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/reminder/executer/ReminderPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */