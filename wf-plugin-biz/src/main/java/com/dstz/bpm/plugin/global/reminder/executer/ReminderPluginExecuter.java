package com.dstz.bpm.plugin.global.reminder.executer;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.plugin.core.manager.BpmReminderTriggerManager;
import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
import com.dstz.bpm.plugin.global.reminder.def.Reminder;
import com.dstz.bpm.plugin.global.reminder.def.ReminderPluginDef;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.service.CalendarService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReminderPluginExecuter extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, ReminderPluginDef> {
   @Autowired
   BpmReminderTriggerManager bpmReminderTriggerManager;
   @Autowired
   BpmTaskManager taskManager;
   @Autowired
   IGroovyScriptEngine scriptEngine;
   @Autowired
   CalendarService calendarService;

   public Void execute(BpmExecutionPluginSession pluginSession, ReminderPluginDef pluginDef) {
      List<Reminder> reminderList = pluginDef.getReminderList();
      if (CollectionUtil.isEmpty(reminderList)) {
         return null;
      } else if (pluginSession.getEventType() == EventType.TASK_COMPLETE_EVENT) {
         this.removeTaskReminders(reminderList);
         return null;
      } else {
         Iterator var4 = reminderList.iterator();

         while(var4.hasNext()) {
            Reminder reminder = (Reminder)var4.next();
            this.taskReminder(reminder, pluginSession);
         }

         return null;
      }
   }

   private void removeTaskReminders(List<Reminder> reminderList) {
      IBpmTask task = this.getTaskIfTask();
      if (task != null) {
         Iterator var3 = reminderList.iterator();

         Reminder reminder;
         do {
            if (!var3.hasNext()) {
               return;
            }

            reminder = (Reminder)var3.next();
         } while(!StringUtil.isEmpty(reminder.getNodeId()) && !reminder.getNodeId().equals(task.getNodeId()));

         this.bpmReminderTriggerManager.removeByTaskId(task.getId());
      }
   }

   private void taskReminder(Reminder reminder, BpmExecutionPluginSession pluginSession) {
      if (!StringUtil.isNotEmpty(reminder.getNodeId()) || reminder.getNodeId().equals(this.getActivitiId(pluginSession))) {
         if (StringUtil.isNotEmpty(reminder.getConditionScript())) {
            Boolean support = this.scriptEngine.executeBoolean(reminder.getConditionScript(), pluginSession);
            if (!support) {
               return;
            }
         }

         BpmReminderTrigger reminderTrigger = this.getReminderTriggerByDef(reminder);
         Date dueDate = this.calcDueDate(reminder);
         if (dueDate == null) {
            throw new BusinessException("系统节假日不可用，请联系管理员维护相关信息！");
         } else {
            reminderTrigger.setDuedate(dueDate);
            IBpmTask task = this.getTaskIfTask();
            if (StringUtils.equals(task.getTaskType(), TaskType.SIGN_SOURCE.getKey())) {
               List<BpmTask> tasks = this.taskManager.getByInstId(task.getInstId());
               tasks.stream().filter((t) -> {
                  return StringUtils.equals(t.getTaskType(), TaskType.SIGN.getKey());
               }).forEach((t) -> {
                  reminderTrigger.setId((String)null);
                  reminderTrigger.setTaskId(t.getId());
                  this.bpmReminderTriggerManager.create(reminderTrigger);
               });
            } else {
               reminderTrigger.setTaskId(task.getId());
               this.bpmReminderTriggerManager.create(reminderTrigger);
            }
         }
      }
   }

   private Date calcDueDate(Reminder reminder) {
      if (!reminder.getIsCalcWorkDay()) {
         return DateUtil.date().offset(DateField.MINUTE, reminder.getTimeLimit());
      } else {
         int minute = reminder.getTimeLimit();
         ResultMsg<Date> dateResult = this.calendarService.getEndWorkDayByMinute(new Date(), minute);
         return (Date)dateResult.getData();
      }
   }

   private BpmReminderTrigger getReminderTriggerByDef(Reminder reminder) {
      BpmReminderTrigger reminderTrigger = new BpmReminderTrigger();
      reminderTrigger.setBeforeScript(reminder.getBeforeScript());
      reminderTrigger.setHtmlMsg(reminder.getHtmlTemplate());
      reminderTrigger.setIsCalcWorkday(reminder.getIsCalcWorkDay() ? 1 : 0);
      reminderTrigger.setIsUrgent(reminder.getIsUrgent() ? 1 : 0);
      reminderTrigger.setMaxReminderTimes(reminder.getMaxReminderTimes());
      reminderTrigger.setReminderCycle(reminder.getReminderCycle());
      reminderTrigger.setReminderDesc(reminder.getDesc());
      reminderTrigger.setTextMsg(reminder.getTextTemplate());
      reminderTrigger.setMsgType(reminder.getMsgType());
      reminderTrigger.setReminderTimes(0);
      return reminderTrigger;
   }
}
