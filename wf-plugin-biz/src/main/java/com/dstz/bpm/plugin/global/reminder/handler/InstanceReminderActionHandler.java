/*     */ package com.dstz.bpm.plugin.global.reminder.handler;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.JsonUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.ActionDisplayHandler;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.model.BpmIdentity;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmReminderLog;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.freemark.IFreemarkerEngine;
/*     */ import com.dstz.sys.api.jms.model.DefaultJmsDTO;
/*     */ import com.dstz.sys.api.jms.model.JmsDTO;
/*     */ import com.dstz.sys.api.jms.model.msg.NotifyMessage;
/*     */ import com.dstz.sys.api.jms.producer.JmsProducer;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class InstanceReminderActionHandler
/*     */   implements ActionDisplayHandler<DefaultInstanceActionCmd>
/*     */ {
/*     */   @Autowired
/*     */   BpmTaskManager taskMananger;
/*     */   @Autowired
/*     */   JmsProducer jmsProducer;
/*     */   @Autowired
/*     */   IFreemarkerEngine freemarkerEngine;
/*     */   @Autowired
/*     */   BpmReminderLogManager reminderLogManager;
/*     */   @Autowired
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   @Autowired
/*     */   UserService userService;
/*     */   
/*     */   public void execute(DefaultInstanceActionCmd model) {
/*  67 */     String opinion = model.getOpinion();
/*  68 */     BpmInstance instance = (BpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
/*  69 */     Boolean isUrgent = Boolean.valueOf(JsonUtil.getBoolean(model.getExtendConf(), "isUrgent"));
/*  70 */     String msgType = JsonUtil.getString(model.getExtendConf(), "msgType");
/*  71 */     String content = JsonUtil.getString(model.getExtendConf(), "content");
/*  72 */     if (StringUtil.isEmpty(opinion)) {
/*  73 */       throw new BusinessMessage("催办提醒内容不可为空！");
/*     */     }
/*     */ 
/*     */     
/*  77 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  78 */     defaultQueryFilter.addFilter("inst_id_", model.getInstanceId(), QueryOP.EQUAL);
/*  79 */     defaultQueryFilter.addFilter("task_type_", "SIGN_SOURCE", QueryOP.NOT_EQUAL);
/*  80 */     List<BpmTask> taskList = this.taskMananger.query((QueryFilter)defaultQueryFilter);
/*  81 */     if (CollectionUtil.isEmpty(taskList)) {
/*  82 */       throw new BusinessMessage("当前实例任务不存在，无需催办！");
/*     */     }
/*     */     
/*  85 */     for (int i = 0; i < taskList.size(); i++) {
/*  86 */       BpmTask task = taskList.get(i);
/*  87 */       List<SysIdentity> identitys = this.taskMananger.getAssignUserById(task);
/*     */       
/*  89 */       if (isUrgent.booleanValue()) {
/*  90 */         task.setPriority(Integer.valueOf(task.getPriority().intValue() + 3));
/*     */       }
/*  92 */       task.setPriority(Integer.valueOf(task.getPriority().intValue() + 4));
/*  93 */       this.taskMananger.update(task);
/*     */       
/*     */       try {
/*  96 */         if (StringUtil.isNotEmpty(content)) {
/*  97 */           content = content.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
/*  98 */           content = this.freemarkerEngine.parseByString(content, getVariables(instance));
/*     */         } 
/* 100 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 103 */       identitys = extractBpmIdentity(identitys);
/* 104 */       if (!CollectionUtil.isEmpty(identitys) && !StringUtil.isEmpty(opinion)) {
/*     */ 
/*     */         
/* 107 */         opinion = opinion.replaceAll("subject", ((BpmTask)taskList.get(0)).getSubject());
/*     */         
/* 109 */         List<JmsDTO> jmsDto = new ArrayList<>();
/* 110 */         BpmReminderLog log = getReminderLog(instance, msgType, opinion, content, task);
/* 111 */         Map<String, Object> extendVars = new HashMap<>();
/* 112 */         extendVars.put("detailId", task.getId());
/* 113 */         extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
/* 114 */         if (isUrgent.booleanValue()) {
/* 115 */           extendVars.put("statue", "urgent,remind");
/*     */         } else {
/* 117 */           extendVars.put("statue", "remind");
/*     */         } 
/* 119 */         extendVars.put("title", String.format("%s于%s发起了待办任务《%s》", new Object[] { ContextUtil.getCurrentUser().getFullname(), 
/* 120 */                 DateUtil.formatDate(task.getCreateTime()), task.getSubject() }));
/* 121 */         extendVars.put("type", "待办");
/* 122 */         extendVars.put("head", "待办");
/*     */         
/* 124 */         for (String type : msgType.split(",")) {
/* 125 */           NotifyMessage message = new NotifyMessage(opinion, content, ContextUtil.getCurrentUser(), identitys);
/* 126 */           message.setTag("待办任务");
/* 127 */           message.setExtendVars(extendVars);
/* 128 */           jmsDto.add(new DefaultJmsDTO(type, (Serializable)message));
/*     */         } 
/* 130 */         this.jmsProducer.sendToQueue(jmsDto);
/*     */         
/* 132 */         log.setIsUrgent(isUrgent.booleanValue() ? "1" : "0");
/* 133 */         StringBuffer userids = new StringBuffer();
/* 134 */         StringBuffer userNames = new StringBuffer();
/* 135 */         identitys.stream().filter(user -> (userids.indexOf(user.getId()) == -1)).forEach(user -> {
/*     */               userids.append(user.getId()).append(",");
/*     */               userNames.append(user.getName()).append(",");
/*     */             });
/* 139 */         log.setReminderUserids(userids.substring(0, userids.length() - 1));
/* 140 */         log.setReminderUsers(userNames.substring(0, userNames.length() - 1));
/* 141 */         this.reminderLogManager.create(log);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance) {
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> getVariables(BpmInstance instance) {
/* 155 */     Map<String, Object> variables = new HashMap<>();
/* 156 */     variables.put("bpmInstance", instance);
/* 157 */     return variables;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 162 */     return ActionType.REMINDER;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 167 */     return 7;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 175 */     System.out.println();
/* 176 */     return Boolean.valueOf((nodeDef == null || nodeDef.getType() == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 181 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 186 */     return "/bpm/task/reminderActionDialog.html";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 192 */     return "return bpmInstance.getStatus().equals(\"running\") || bpmInstance.getStatus().equals(\"back\");";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 198 */     return null;
/*     */   }
/*     */   
/*     */   private BpmReminderLog getReminderLog(BpmInstance instance, String msgType, String reminderTitle, String reminderExtend, BpmTask task) {
/* 202 */     BpmReminderLog reminderLog = new BpmReminderLog();
/* 203 */     reminderLog.setInstanceId(instance.getId());
/* 204 */     reminderLog.setReminderDate(new Date());
/* 205 */     reminderLog.setNodeId(task.getNodeId());
/* 206 */     reminderLog.setNodeName(task.getName());
/* 207 */     reminderLog.setSubject(instance.getSubject());
/* 208 */     reminderLog.setReminderTitle(reminderTitle);
/* 209 */     reminderLog.setExtend(reminderExtend);
/* 210 */     reminderLog.setMsgType(msgType);
/* 211 */     IUser user = ContextUtil.getCurrentUser();
/* 212 */     reminderLog.setFromUserIds(user.getUserId());
/* 213 */     reminderLog.setFromUsers(user.getFullname());
/* 214 */     reminderLog.setTaskId(task.getId());
/* 215 */     return reminderLog;
/*     */   }
/*     */   
/*     */   private List<SysIdentity> extractBpmIdentity(List<SysIdentity> identities) {
/* 219 */     List<SysIdentity> results = new ArrayList<>();
/* 220 */     for (SysIdentity bpmIdentity : identities) {
/* 221 */       if ("user".equals(bpmIdentity.getType())) {
/* 222 */         results.add(bpmIdentity); continue;
/*     */       } 
/* 224 */       List<IUser> users = this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
/* 225 */       for (IUser user : users) {
/* 226 */         results.add(new BpmIdentity(user));
/*     */       }
/*     */     } 
/*     */     
/* 230 */     return results;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/reminder/handler/InstanceReminderActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */