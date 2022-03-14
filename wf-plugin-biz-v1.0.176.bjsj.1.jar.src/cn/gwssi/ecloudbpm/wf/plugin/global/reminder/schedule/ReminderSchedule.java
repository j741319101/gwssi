/*     */ package com.dstz.bpm.plugin.global.reminder.schedule;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
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
/*     */ import com.dstz.base.api.aop.annotion.ECloudScheduled;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.StringUtil;
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
/*     */ import com.xxl.job.core.biz.model.ReturnT;
/*     */ import com.xxl.job.core.handler.IJobHandler;
/*     */ import com.xxl.job.core.handler.annotation.JobHandler;
/*     */ import com.xxl.job.core.log.XxlJobLogger;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ @JobHandler("eCloudReminderJob")
/*     */ @Component
/*     */ public class ReminderSchedule
/*     */   extends IJobHandler
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
/*     */   @Autowired
/*     */   IGroovyScriptEngine scriptEngine;
/*     */   @Autowired
/*     */   JmsProducer jmsProducer;
/*     */   @Autowired
/*     */   IFreemarkerEngine freemarkerEngine;
/*     */   @Autowired
/*     */   CalendarService calendarService;
/*     */   @Autowired
/*     */   BpmReminderLogManager reminderLogManager;
/*  77 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */   
/*     */   @Transactional
/*     */   @ECloudScheduled(cron = "0/30 * * * * ?")
/*     */   public ReturnT<String> execute(String s) throws Exception {
/*  83 */     List<BpmReminderTrigger> reminderTriggerList = this.reminderTriggerManager.getDueMinders();
/*  84 */     if (CollectionUtils.isEmpty(reminderTriggerList)) {
/*  85 */       XxlJobLogger.log("【 No tasks to be reminded 】", new Object[0]);
/*  86 */       return IJobHandler.SUCCESS;
/*     */     } 
/*  88 */     reminderTriggerList.forEach(reminder -> triggerReminder(reminder));
/*  89 */     XxlJobLogger.log("【 reminded {} task 】", new Object[] { Integer.valueOf(reminderTriggerList.size()) });
/*  90 */     return IJobHandler.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void triggerReminder(BpmReminderTrigger reminder) {
/* 101 */     BpmTask task = (BpmTask)this.taskManager.get(reminder.getTaskId());
/*     */     
/* 103 */     if (task == null) {
/* 104 */       this.LOG.warn("催办目标任务已经丢失，删除催办触发器 {}", JSON.toJSONString(reminder));
/* 105 */       this.reminderTriggerManager.remove(reminder.getId());
/*     */       
/*     */       return;
/*     */     } 
/* 109 */     Map<String, Object> variables = getVariables((IBpmTask)task);
/* 110 */     variables.put("reminderTimes", reminder.getReminderTimes());
/* 111 */     BpmReminderLog reminderLog = getReminderLog(task, reminder);
/*     */     
/* 113 */     if (StringUtil.isNotEmpty(reminder.getBeforeScript())) {
/* 114 */       Boolean r = Boolean.valueOf(this.scriptEngine.executeBoolean(reminder.getBeforeScript(), variables));
/* 115 */       reminderLog.setExtend(reminder.getBeforeScript());
/* 116 */       if (r != null && !r.booleanValue()) {
/* 117 */         reminderLog.setExtend(reminder.getBeforeScript() + "[Before Script] return false; skipped reminder");
/* 118 */         calcNextTrigger(reminder);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 124 */     reminderLog.setIsUrgent(String.valueOf(reminder.getIsUrgent()));
/* 125 */     List<JmsDTO> message = getJmsMsgVoAndSaveLog(reminder, variables, task, reminderLog);
/* 126 */     if (CollectionUtil.isNotEmpty(message)) {
/* 127 */       this.jmsProducer.sendToQueue(message);
/*     */     }
/*     */     
/* 130 */     if (reminder.getIsUrgent().intValue() == 1) {
/* 131 */       task.setPriority(Integer.valueOf(task.getPriority().intValue() + 3));
/*     */     }
/* 133 */     task.setPriority(Integer.valueOf(task.getPriority().intValue() + 4));
/* 134 */     this.taskManager.update(task);
/* 135 */     calcNextTrigger(reminder);
/*     */   }
/*     */ 
/*     */   
/*     */   private void calcNextTrigger(BpmReminderTrigger reminder) {
/*     */     Date dueDate;
/* 141 */     reminder.setReminderTimes(Integer.valueOf(reminder.getReminderTimes().intValue() + 1));
/* 142 */     if (reminder.getReminderTimes().intValue() >= reminder.getMaxReminderTimes().intValue()) {
/* 143 */       this.reminderTriggerManager.remove(reminder.getId());
/*     */     }
/*     */ 
/*     */     
/* 147 */     int minute = reminder.getReminderCycle().intValue();
/*     */     
/* 149 */     if (reminder.getIsCalcWorkday().intValue() == 0) {
/* 150 */       DateTime dateTime = DateUtil.date().offset(DateField.MINUTE, minute);
/*     */     }
/*     */     else {
/*     */       
/* 154 */       ResultMsg<Date> dateResult = this.calendarService.getEndWorkDayByMinute(new Date(), minute);
/* 155 */       dueDate = (Date)dateResult.getData();
/*     */     } 
/*     */     
/* 158 */     if (dueDate == null) {
/* 159 */       this.LOG.error("下次催办日期计算异常,请检查具体原因催办已经延期一天计算 {}", JSON.toJSONString(reminder));
/*     */       
/* 161 */       reminder.setDuedate((Date)DateUtil.tomorrow());
/*     */     } 
/* 163 */     reminder.setDuedate(dueDate);
/*     */     
/* 165 */     this.reminderTriggerManager.update(reminder);
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
/* 178 */     if (StringUtil.isEmpty(reminder.getMsgType())) return Collections.emptyList(); 
/* 179 */     String[] msgType = reminder.getMsgType().split(",");
/*     */     
/* 181 */     List<SysIdentity> userList = this.taskManager.getAssignUserById(task);
/* 182 */     if (CollectionUtil.isEmpty(userList)) {
/* 183 */       this.LOG.info("【催办定时任务】没有需要发送的消息！原因：接收消息人员为空。 催办标题：{}，流程标题：{}，任务名：{}", new Object[] { reminder.getReminderDesc(), task.getSubject(), task.getName() });
/* 184 */       return Collections.emptyList();
/*     */     } 
/*     */ 
/*     */     
/* 188 */     String htmlTemplate = reminder.getHtmlMsg();
/* 189 */     String textTemplate = reminder.getTextMsg();
/*     */     try {
/* 191 */       if (StringUtil.isNotEmpty(htmlTemplate)) {
/* 192 */         htmlTemplate = htmlTemplate.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
/*     */         
/* 194 */         htmlTemplate = this.freemarkerEngine.parseByString(htmlTemplate, variables);
/*     */       } 
/* 196 */       if (StringUtil.isNotEmpty(textTemplate)) {
/* 197 */         textTemplate = this.freemarkerEngine.parseByString(textTemplate, variables);
/*     */       }
/* 199 */     } catch (Exception e) {
/* 200 */       this.LOG.error("htmlTemplate:{};textTempalte:{}", htmlTemplate, textTemplate);
/* 201 */       this.LOG.error("taskId[{}]消息发送插件解析消息模板失败，可能原因为:{}", new Object[] { reminder.getTaskId(), e.getMessage(), e });
/*     */     } 
/*     */ 
/*     */     
/* 205 */     Map<String, Object> extendVars = new HashMap<>();
/* 206 */     extendVars.put("detailId", task.getId());
/* 207 */     extendVars.put("name", (ContextUtil.getCurrentUser() == null) ? "系统催办配置" : ContextUtil.getCurrentUser().getFullname());
/* 208 */     if (reminder.getIsUrgent().intValue() == 1) {
/* 209 */       extendVars.put("statue", "urgent,remind");
/*     */     } else {
/* 211 */       extendVars.put("statue", "remind");
/*     */     } 
/* 213 */     String title = "%s于%s发起了待办任务《%s》";
/* 214 */     extendVars.put("title", String.format(title, new Object[] { (ContextUtil.getCurrentUser() == null) ? "系统催办配置" : ContextUtil.getCurrentUser().getFullname(), 
/* 215 */             DateUtil.formatDate(task.getCreateTime()), task.getSubject() }));
/* 216 */     extendVars.put("type", "待办");
/* 217 */     extendVars.put("head", "待办");
/* 218 */     List<JmsDTO> jmsDto = new ArrayList<>();
/* 219 */     for (String type : msgType) {
/* 220 */       NotifyMessage message = new NotifyMessage(reminder.getReminderDesc(), htmlTemplate, null, userList);
/* 221 */       message.setTag("待办任务");
/* 222 */       message.setExtendVars(extendVars);
/* 223 */       jmsDto.add(new DefaultJmsDTO(type, (Serializable)message));
/*     */     } 
/* 225 */     reminderLog.setExtend(htmlTemplate);
/*     */ 
/*     */     
/* 228 */     StringBuffer userids = new StringBuffer();
/* 229 */     StringBuffer userNames = new StringBuffer();
/* 230 */     userList.stream().filter(user -> (userids.indexOf(user.getId()) == -1)).forEach(user -> {
/*     */           userids.append(user.getId()).append(",");
/*     */           userNames.append(user.getName()).append(",");
/*     */         });
/* 234 */     reminderLog.setReminderUserids(userids.substring(0, userids.length() - 1));
/* 235 */     reminderLog.setReminderUsers(userNames.substring(0, userNames.length() - 1));
/* 236 */     this.reminderLogManager.create(reminderLog);
/* 237 */     return jmsDto;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, Object> getVariables(IBpmTask task) {
/* 242 */     String taskId = task.getId();
/* 243 */     Map<String, Object> variables = new HashMap<>();
/*     */     
/* 245 */     if (StringUtil.isNotEmpty(task.getTaskId())) {
/* 246 */       Map<String, Object> vars = this.actTaskService.getVariables(task.getTaskId());
/* 247 */       if (MapUtil.isNotEmpty(vars)) {
/* 248 */         variables.putAll(vars);
/*     */       }
/*     */     } 
/*     */     
/* 252 */     Map<String, IBusinessData> businessDataMap = this.bpmFlowDataAccessor.getTaskBusData(taskId);
/* 253 */     if (MapUtil.isNotEmpty(businessDataMap)) {
/* 254 */       for (Map.Entry<String, IBusinessData> bos : businessDataMap.entrySet()) {
/* 255 */         variables.put(bos.getKey(), bos.getValue());
/*     */       }
/*     */     }
/*     */     
/* 259 */     IBpmInstance instance = (IBpmInstance)this.bpmInstanceManager.get(task.getInstId());
/* 260 */     variables.put("bpmTask", task);
/* 261 */     variables.put("bpmInstance", instance);
/*     */     
/* 263 */     return variables;
/*     */   }
/*     */   
/*     */   private BpmReminderLog getReminderLog(BpmTask task, BpmReminderTrigger reminder) {
/* 267 */     BpmReminderLog reminderLog = new BpmReminderLog();
/* 268 */     reminderLog.setInstanceId(task.getInstId());
/* 269 */     reminderLog.setNodeId(task.getNodeId());
/* 270 */     reminderLog.setNodeName(task.getName());
/* 271 */     reminderLog.setReminderDate(new Date());
/* 272 */     reminderLog.setSubject(task.getSubject());
/* 273 */     reminderLog.setReminderTitle(reminder.getReminderDesc());
/* 274 */     reminderLog.setMsgType(reminder.getMsgType());
/* 275 */     reminderLog.setExtend(reminder.getHtmlMsg());
/* 276 */     reminderLog.setFromUserIds("system");
/* 277 */     reminderLog.setFromUsers("系统自动催办");
/* 278 */     reminderLog.setTaskId(task.getId());
/* 279 */     return reminderLog;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/reminder/schedule/ReminderSchedule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */