/*     */ package com.dstz.bpm.plugin.global.reminder.schedule;
/*     */ 
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.act.service.ActTaskService;
/*     */ import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmReminderTriggerManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmReminderLog;
/*     */ import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
/*     */ import com.dstz.bus.api.model.IBusinessData;
/*     */ import com.dstz.sys.api.freemark.IFreemarkerEngine;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.jms.model.DefaultJmsDTO;
/*     */ import com.dstz.sys.api.jms.model.JmsDTO;
/*     */ import com.dstz.sys.api.jms.model.msg.NotifyMessage;
/*     */ import com.dstz.sys.api.jms.producer.JmsProducer;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.api.service.CalendarService;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateField;
/*     */ import cn.hutool.core.date.DateTime;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import cn.hutool.core.map.MapUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ @Component
/*     */ public class ReminderSchedule
/*     */ {
/*     */   @Autowired
/*     */   BpmReminderTriggerManager reminderTriggerManager;
/*     */   @Autowired
/*     */   BpmTaskManager taskManager;
/*     */   @Autowired
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   @Autowired
/*     */   BpmFlowDataAccessor bpmFlowDataAccessor;
/*     */   @Autowired
/*     */   ActTaskService actTaskService;
/*  64 */   protected Logger LOG = LoggerFactory.getLogger(getClass()); @Autowired
/*     */   IGroovyScriptEngine scriptEngine; @Autowired
/*     */   JmsProducer jmsProducer;
/*     */   @Autowired
/*     */   IFreemarkerEngine freemarkerEngine;
/*     */   @Autowired
/*     */   CalendarService calendarService;
/*     */   @Autowired
/*     */   BpmReminderLogManager reminderLogManager;
/*     */   
/*     */   public void triggerReminder(BpmReminderTrigger reminder) {
/*  75 */     BpmTask task = (BpmTask)this.taskManager.get(reminder.getTaskId());
/*     */     
/*  77 */     if (task == null) {
/*  78 */       this.LOG.warn("催办目标任务已经丢失，删除催办触发器 {}", JSON.toJSONString(reminder));
/*  79 */       this.reminderTriggerManager.remove(reminder.getId());
/*     */       
/*     */       return;
/*     */     } 
/*  83 */     Map<String, Object> variables = getVariables((IBpmTask)task);
/*  84 */     variables.put("reminderTimes", reminder.getReminderTimes());
/*  85 */     BpmReminderLog reminderLog = getReminderLog(task, reminder);
/*     */     
/*  87 */     if (StringUtil.isNotEmpty(reminder.getBeforeScript())) {
/*  88 */       Boolean r = Boolean.valueOf(this.scriptEngine.executeBoolean(reminder.getBeforeScript(), variables));
/*  89 */       reminderLog.setExtend(reminder.getBeforeScript());
/*  90 */       if (r != null && !r.booleanValue()) {
/*  91 */         reminderLog.setExtend(reminder.getBeforeScript() + "[Before Script] return false; skipped reminder");
/*  92 */         calcNextTrigger(reminder);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     reminderLog.setIsUrgent(String.valueOf(reminder.getIsUrgent()));
/*  99 */     List<JmsDTO> message = getJmsMsgVoAndSaveLog(reminder, variables, task, reminderLog);
/* 100 */     if (CollectionUtil.isNotEmpty(message)) {
/* 101 */       this.jmsProducer.sendToQueue(message);
/*     */     }
/*     */     
/* 104 */     if (reminder.getIsUrgent().intValue() == 1) {
/* 105 */       task.setPriority(Integer.valueOf(task.getPriority().intValue() + 3));
/*     */     }
/* 107 */     task.setPriority(Integer.valueOf(task.getPriority().intValue() + 4));
/* 108 */     this.taskManager.update(task);
/* 109 */     calcNextTrigger(reminder);
/*     */   }
/*     */ 
/*     */   
/*     */   private void calcNextTrigger(BpmReminderTrigger reminder) {
/*     */     Date dueDate;
/* 115 */     reminder.setReminderTimes(Integer.valueOf(reminder.getReminderTimes().intValue() + 1));
/* 116 */     if (reminder.getReminderTimes().intValue() >= reminder.getMaxReminderTimes().intValue()) {
/* 117 */       this.reminderTriggerManager.remove(reminder.getId());
/*     */     }
/*     */ 
/*     */     
/* 121 */     int minute = reminder.getReminderCycle().intValue();
/*     */     
/* 123 */     if (reminder.getIsCalcWorkday().intValue() == 0) {
/* 124 */       DateTime dateTime = DateUtil.date().offset(DateField.MINUTE, minute);
/*     */     }
/*     */     else {
/*     */       
/* 128 */       ResultMsg<Date> dateResult = this.calendarService.getEndWorkDayByMinute(new Date(), minute);
/* 129 */       dueDate = (Date)dateResult.getData();
/*     */     } 
/*     */     
/* 132 */     if (dueDate == null) {
/* 133 */       this.LOG.error("下次催办日期计算异常,请检查具体原因催办已经延期一天计算 {}", JSON.toJSONString(reminder));
/*     */       
/* 135 */       reminder.setDuedate((Date)DateUtil.tomorrow());
/*     */     } 
/* 137 */     reminder.setDuedate(dueDate);
/*     */     
/* 139 */     this.reminderTriggerManager.update(reminder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<JmsDTO> getJmsMsgVoAndSaveLog(BpmReminderTrigger reminder, Map<String, Object> variables, BpmTask task, BpmReminderLog reminderLog) {
/* 152 */     if (StringUtil.isEmpty(reminder.getMsgType())) return Collections.emptyList(); 
/* 153 */     String[] msgType = reminder.getMsgType().split(",");
/*     */     
/* 155 */     List<SysIdentity> userList = this.taskManager.getAssignUserById(task);
/* 156 */     if (CollectionUtil.isEmpty(userList)) {
/* 157 */       this.LOG.info("【催办定时任务】没有需要发送的消息！原因：接收消息人员为空。 催办标题：{}，流程标题：{}，任务名：{}", new Object[] { reminder.getReminderDesc(), task.getSubject(), task.getName() });
/* 158 */       return Collections.emptyList();
/*     */     } 
/*     */ 
/*     */     
/* 162 */     String htmlTemplate = reminder.getHtmlMsg();
/* 163 */     String textTemplate = reminder.getTextMsg();
/*     */     try {
/* 165 */       if (StringUtil.isNotEmpty(htmlTemplate)) {
/* 166 */         htmlTemplate = htmlTemplate.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
/*     */         
/* 168 */         htmlTemplate = this.freemarkerEngine.parseByString(htmlTemplate, variables);
/*     */       } 
/* 170 */       if (StringUtil.isNotEmpty(textTemplate)) {
/* 171 */         textTemplate = this.freemarkerEngine.parseByString(textTemplate, variables);
/*     */       }
/* 173 */     } catch (Exception e) {
/* 174 */       this.LOG.error("htmlTemplate:{};textTempalte:{}", htmlTemplate, textTemplate);
/* 175 */       this.LOG.error("taskId[{}]消息发送插件解析消息模板失败，可能原因为:{}", new Object[] { reminder.getTaskId(), e.getMessage(), e });
/*     */     } 
/*     */ 
/*     */     
/* 179 */     Map<String, Object> extendVars = new HashMap<>();
/* 180 */     extendVars.put("detailId", task.getId());
/* 181 */     extendVars.put("name", (ContextUtil.getCurrentUser() == null) ? "系统催办配置" : ContextUtil.getCurrentUser().getFullname());
/* 182 */     if (reminder.getIsUrgent().intValue() == 1) {
/* 183 */       extendVars.put("statue", "urgent,remind");
/*     */     } else {
/* 185 */       extendVars.put("statue", "remind");
/*     */     } 
/* 187 */     String title = "%s于%s发起了待办任务《%s》";
/* 188 */     extendVars.put("title", String.format(title, new Object[] { (ContextUtil.getCurrentUser() == null) ? "系统催办配置" : ContextUtil.getCurrentUser().getFullname(), 
/* 189 */             DateUtil.formatDate(task.getCreateTime()), task.getSubject() }));
/* 190 */     extendVars.put("type", "待办");
/* 191 */     extendVars.put("head", "待办");
/* 192 */     List<JmsDTO> jmsDto = new ArrayList<>();
/* 193 */     for (String type : msgType) {
/* 194 */       NotifyMessage message = new NotifyMessage("呈送领导", htmlTemplate, null, userList);
/* 195 */       message.setTag("待办任务");
/* 196 */       message.setExtendVars(extendVars);
/* 197 */       jmsDto.add(new DefaultJmsDTO(type, (Serializable)message));
/*     */     } 
/* 199 */     reminderLog.setExtend(htmlTemplate);
/*     */ 
/*     */     
/* 202 */     StringBuffer userids = new StringBuffer();
/* 203 */     StringBuffer userNames = new StringBuffer();
/* 204 */     userList.stream().filter(user -> (userids.indexOf(user.getId()) == -1)).forEach(user -> {
/*     */           userids.append(user.getId()).append(",");
/*     */           userNames.append(user.getName()).append(",");
/*     */         });
/* 208 */     reminderLog.setReminderUserids(userids.substring(0, userids.length() - 1));
/* 209 */     reminderLog.setReminderUsers(userNames.substring(0, userNames.length() - 1));
/* 210 */     this.reminderLogManager.create(reminderLog);
/* 211 */     return jmsDto;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, Object> getVariables(IBpmTask task) {
/* 216 */     String taskId = task.getId();
/* 217 */     Map<String, Object> variables = new HashMap<>();
/*     */     
/* 219 */     if (StringUtil.isNotEmpty(task.getTaskId())) {
/* 220 */       Map<String, Object> vars = this.actTaskService.getVariables(task.getTaskId());
/* 221 */       if (MapUtil.isNotEmpty(vars)) {
/* 222 */         variables.putAll(vars);
/*     */       }
/*     */     } 
/*     */     
/* 226 */     Map<String, IBusinessData> businessDataMap = this.bpmFlowDataAccessor.getTaskBusData(taskId);
/* 227 */     if (MapUtil.isNotEmpty(businessDataMap)) {
/* 228 */       for (Map.Entry<String, IBusinessData> bos : businessDataMap.entrySet()) {
/* 229 */         variables.put(bos.getKey(), bos.getValue());
/*     */       }
/*     */     }
/*     */     
/* 233 */     IBpmInstance instance = (IBpmInstance)this.bpmInstanceManager.get(task.getInstId());
/* 234 */     variables.put("bpmTask", task);
/* 235 */     variables.put("bpmInstance", instance);
/*     */     
/* 237 */     return variables;
/*     */   }
/*     */   
/*     */   private BpmReminderLog getReminderLog(BpmTask task, BpmReminderTrigger reminder) {
/* 241 */     BpmReminderLog reminderLog = new BpmReminderLog();
/* 242 */     reminderLog.setInstanceId(task.getInstId());
/* 243 */     reminderLog.setNodeId(task.getNodeId());
/* 244 */     reminderLog.setNodeName(task.getName());
/* 245 */     reminderLog.setReminderDate(new Date());
/* 246 */     reminderLog.setSubject(task.getSubject());
/* 247 */     reminderLog.setReminderTitle(reminder.getReminderDesc());
/* 248 */     reminderLog.setMsgType(reminder.getMsgType());
/* 249 */     reminderLog.setExtend(reminder.getHtmlMsg());
/* 250 */     reminderLog.setFromUserIds("system");
/* 251 */     reminderLog.setFromUsers("系统自动催办");
/* 252 */     reminderLog.setTaskId(task.getId());
/* 253 */     return reminderLog;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/reminder/schedule/ReminderSchedule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */