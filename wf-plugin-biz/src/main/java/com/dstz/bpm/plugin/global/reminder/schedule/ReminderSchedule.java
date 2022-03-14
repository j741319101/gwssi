//package com.dstz.bpm.plugin.global.reminder.schedule;
//
// todo 定时任务先去掉
//import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
//import com.dstz.bpm.act.service.ActTaskService;
//import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
//import com.dstz.bpm.api.model.inst.IBpmInstance;
//import com.dstz.bpm.api.model.task.IBpmTask;
//import com.dstz.bpm.core.manager.BpmInstanceManager;
//import com.dstz.bpm.core.manager.BpmTaskManager;
//import com.dstz.bpm.core.model.BpmTask;
//import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
//import com.dstz.bpm.plugin.core.manager.BpmReminderTriggerManager;
//import com.dstz.bpm.plugin.core.model.BpmReminderLog;
//import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
//import com.dstz.base.api.aop.annotion.ECloudScheduled;
//import com.dstz.base.api.response.impl.ResultMsg;
//import com.dstz.base.core.util.StringUtil;
//import com.dstz.org.api.model.IUser;
//import com.dstz.sys.api.freemark.IFreemarkerEngine;
//import com.dstz.sys.api.groovy.IGroovyScriptEngine;
//import com.dstz.sys.api.jms.model.DefaultJmsDTO;
//import com.dstz.sys.api.jms.model.JmsDTO;
//import com.dstz.sys.api.jms.model.msg.NotifyMessage;
//import com.dstz.sys.api.jms.producer.JmsProducer;
//import com.dstz.sys.api.model.SysIdentity;
//import com.dstz.sys.api.service.CalendarService;
//import com.dstz.sys.util.ContextUtil;
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.date.DateField;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.map.MapUtil;
//import com.alibaba.fastjson.JSON;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.IJobHandler;
//import com.xxl.job.core.handler.annotation.JobHandler;
//import com.xxl.job.core.log.XxlJobLogger;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.function.Consumer;
//import java.util.function.Predicate;
//import org.apache.commons.collections.CollectionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@JobHandler("eCloudReminderJob")
//@Component
//public class ReminderSchedule extends IJobHandler {
//   @Autowired
//   BpmReminderTriggerManager reminderTriggerManager;
//   @Autowired
//   BpmTaskManager taskManager;
//   @Autowired
//   BpmInstanceManager bpmInstanceManager;
//   @Autowired
//   BpmFlowDataAccessor bpmFlowDataAccessor;
//   @Autowired
//   ActTaskService actTaskService;
//   @Autowired
//   IGroovyScriptEngine scriptEngine;
//   @Autowired
//   JmsProducer jmsProducer;
//   @Autowired
//   IFreemarkerEngine freemarkerEngine;
//   @Autowired
//   CalendarService calendarService;
//   @Autowired
//   BpmReminderLogManager reminderLogManager;
//   protected Logger LOG = LoggerFactory.getLogger(this.getClass());
//
//   @Transactional
//   @ECloudScheduled(
//      cron = "0/30 * * * * ?"
//   )
//   public ReturnT<String> execute(String s) throws Exception {
//      List<BpmReminderTrigger> reminderTriggerList = this.reminderTriggerManager.getDueMinders();
//      if (CollectionUtils.isEmpty(reminderTriggerList)) {
//         XxlJobLogger.log("【 No tasks to be reminded 】", new Object[0]);
//         return IJobHandler.SUCCESS;
//      } else {
//         reminderTriggerList.forEach((reminder) -> {
//            this.triggerReminder(reminder);
//         });
//         XxlJobLogger.log("【 reminded {} task 】", new Object[]{reminderTriggerList.size()});
//         return IJobHandler.SUCCESS;
//      }
//   }
//
//   public void triggerReminder(BpmReminderTrigger reminder) {
//      BpmTask task = (BpmTask)this.taskManager.get(reminder.getTaskId());
//      if (task == null) {
//         this.LOG.warn("催办目标任务已经丢失，删除催办触发器 {}", JSON.toJSONString(reminder));
//         this.reminderTriggerManager.remove(reminder.getId());
//      } else {
//         Map<String, Object> variables = this.getVariables(task);
//         variables.put("reminderTimes", reminder.getReminderTimes());
//         BpmReminderLog reminderLog = this.getReminderLog(task, reminder);
//         if (StringUtil.isNotEmpty(reminder.getBeforeScript())) {
//            Boolean r = this.scriptEngine.executeBoolean(reminder.getBeforeScript(), variables);
//            reminderLog.setExtend(reminder.getBeforeScript());
//            if (r != null && !r) {
//               reminderLog.setExtend(reminder.getBeforeScript() + "[Before Script] return false; skipped reminder");
//               this.calcNextTrigger(reminder);
//               return;
//            }
//         }
//
//         reminderLog.setIsUrgent(String.valueOf(reminder.getIsUrgent()));
//         List<JmsDTO> message = this.getJmsMsgVoAndSaveLog(reminder, variables, task, reminderLog);
//         if (CollectionUtil.isNotEmpty(message)) {
//            this.jmsProducer.sendToQueue(message);
//         }
//
//         if (reminder.getIsUrgent() == 1) {
//            task.setPriority(task.getPriority() + 3);
//         }
//
//         task.setPriority(task.getPriority() + 4);
//         this.taskManager.update(task);
//         this.calcNextTrigger(reminder);
//      }
//   }
//
//   private void calcNextTrigger(BpmReminderTrigger reminder) {
//      reminder.setReminderTimes(reminder.getReminderTimes() + 1);
//      if (reminder.getReminderTimes() >= reminder.getMaxReminderTimes()) {
//         this.reminderTriggerManager.remove(reminder.getId());
//      }
//
//      int minute = reminder.getReminderCycle();
//      Object dueDate;
//      if (reminder.getIsCalcWorkday() == 0) {
//         dueDate = DateUtil.date().offset(DateField.MINUTE, minute);
//      } else {
//         ResultMsg<Date> dateResult = this.calendarService.getEndWorkDayByMinute(new Date(), minute);
//         dueDate = (Date)dateResult.getData();
//      }
//
//      if (dueDate == null) {
//         this.LOG.error("下次催办日期计算异常,请检查具体原因催办已经延期一天计算 {}", JSON.toJSONString(reminder));
//         reminder.setDuedate(DateUtil.tomorrow());
//      }
//
//      reminder.setDuedate((Date)dueDate);
//      this.reminderTriggerManager.update(reminder);
//   }
//
//   private List<JmsDTO> getJmsMsgVoAndSaveLog(BpmReminderTrigger reminder, Map<String, Object> variables, BpmTask task, BpmReminderLog reminderLog) {
//      if (StringUtil.isEmpty(reminder.getMsgType())) {
//         return Collections.emptyList();
//      } else {
//         String[] msgType = reminder.getMsgType().split(",");
//         List<SysIdentity> userList = this.taskManager.getAssignUserById(task);
//         if (CollectionUtil.isEmpty(userList)) {
//            this.LOG.info("【催办定时任务】没有需要发送的消息！原因：接收消息人员为空。 催办标题：{}，流程标题：{}，任务名：{}", new Object[]{reminder.getReminderDesc(), task.getSubject(), task.getName()});
//            return Collections.emptyList();
//         } else {
//            String htmlTemplate = reminder.getHtmlMsg();
//            String textTemplate = reminder.getTextMsg();
//
//            try {
//               if (StringUtil.isNotEmpty(htmlTemplate)) {
//                  htmlTemplate = htmlTemplate.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
//                  htmlTemplate = this.freemarkerEngine.parseByString(htmlTemplate, variables);
//               }
//
//               if (StringUtil.isNotEmpty(textTemplate)) {
//                  this.freemarkerEngine.parseByString(textTemplate, variables);
//               }
//            } catch (Exception var17) {
//               this.LOG.error("htmlTemplate:{};textTempalte:{}", htmlTemplate, textTemplate);
//               this.LOG.error("taskId[{}]消息发送插件解析消息模板失败，可能原因为:{}", new Object[]{reminder.getTaskId(), var17.getMessage(), var17});
//            }
//
//            Map<String, Object> extendVars = new HashMap();
//            extendVars.put("detailId", task.getId());
//            extendVars.put("name", ContextUtil.getCurrentUser() == null ? "系统催办配置" : ContextUtil.getCurrentUser().getFullname());
//            if (reminder.getIsUrgent() == 1) {
//               extendVars.put("statue", "urgent,remind");
//            } else {
//               extendVars.put("statue", "remind");
//            }
//
//            String title = "%s于%s发起了待办任务《%s》";
//            extendVars.put("title", String.format(title, ContextUtil.getCurrentUser() == null ? "系统催办配置" : ContextUtil.getCurrentUser().getFullname(), DateUtil.formatDate(task.getCreateTime()), task.getSubject()));
//            extendVars.put("type", "待办");
//            extendVars.put("head", "待办");
//            List<JmsDTO> jmsDto = new ArrayList();
//            String[] var12 = msgType;
//            int var13 = msgType.length;
//
//            for(int var14 = 0; var14 < var13; ++var14) {
//               String type = var12[var14];
//               NotifyMessage message = new NotifyMessage(reminder.getReminderDesc(), htmlTemplate, (IUser)null, userList);
//               message.setTag("待办任务");
//               message.setExtendVars(extendVars);
//               jmsDto.add(new DefaultJmsDTO(type, message));
//            }
//
//            reminderLog.setExtend(htmlTemplate);
//            StringBuffer userids = new StringBuffer();
//            StringBuffer userNames = new StringBuffer();
//            userList.stream().filter((user) -> {
//               return userids.indexOf(user.getId()) == -1;
//            }).forEach((user) -> {
//               userids.append(user.getId()).append(",");
//               userNames.append(user.getName()).append(",");
//            });
//            reminderLog.setReminderUserids(userids.substring(0, userids.length() - 1));
//            reminderLog.setReminderUsers(userNames.substring(0, userNames.length() - 1));
//            this.reminderLogManager.create(reminderLog);
//            return jmsDto;
//         }
//      }
//   }
//
//   private Map<String, Object> getVariables(IBpmTask task) {
//      String taskId = task.getId();
//      Map<String, Object> variables = new HashMap();
//      Map businessDataMap;
//      if (StringUtil.isNotEmpty(task.getTaskId())) {
//         businessDataMap = this.actTaskService.getVariables(task.getTaskId());
//         if (MapUtil.isNotEmpty(businessDataMap)) {
//            variables.putAll(businessDataMap);
//         }
//      }
//
//      businessDataMap = this.bpmFlowDataAccessor.getTaskBusData(taskId);
//      if (MapUtil.isNotEmpty(businessDataMap)) {
//         Iterator var5 = businessDataMap.entrySet().iterator();
//
//         while(var5.hasNext()) {
//            Entry<String, IBusinessData> bos = (Entry)var5.next();
//            variables.put(bos.getKey(), bos.getValue());
//         }
//      }
//
//      IBpmInstance instance = (IBpmInstance)this.bpmInstanceManager.get(task.getInstId());
//      variables.put("bpmTask", task);
//      variables.put("bpmInstance", instance);
//      return variables;
//   }
//
//   private BpmReminderLog getReminderLog(BpmTask task, BpmReminderTrigger reminder) {
//      BpmReminderLog reminderLog = new BpmReminderLog();
//      reminderLog.setInstanceId(task.getInstId());
//      reminderLog.setNodeId(task.getNodeId());
//      reminderLog.setNodeName(task.getName());
//      reminderLog.setReminderDate(new Date());
//      reminderLog.setSubject(task.getSubject());
//      reminderLog.setReminderTitle(reminder.getReminderDesc());
//      reminderLog.setMsgType(reminder.getMsgType());
//      reminderLog.setExtend(reminder.getHtmlMsg());
//      reminderLog.setFromUserIds("system");
//      reminderLog.setFromUsers("系统自动催办");
//      reminderLog.setTaskId(task.getId());
//      return reminderLog;
//   }
//}
