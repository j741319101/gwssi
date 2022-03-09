/*     */ package cn.gwssi.ecloudbpm.wf.plugin.global.reminder.handler;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.ActionDisplayHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmReminderLogManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmReminderLog;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.JsonUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.api.freemark.IFreemarkerEngine;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.model.DefaultJmsDTO;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.model.JmsDTO;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.model.msg.NotifyMessage;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.producer.JmsProducer;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
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
/*  63 */     String opinion = model.getOpinion();
/*  64 */     BpmInstance instance = (BpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
/*  65 */     Boolean isUrgent = Boolean.valueOf(JsonUtil.getBoolean(model.getExtendConf(), "isUrgent"));
/*  66 */     String msgType = JsonUtil.getString(model.getExtendConf(), "msgType");
/*  67 */     String content = JsonUtil.getString(model.getExtendConf(), "content");
/*  68 */     if (StringUtil.isEmpty(opinion)) {
/*  69 */       throw new BusinessMessage("催办提醒内容不可为空！");
/*     */     }
/*     */ 
/*     */     
/*  73 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/*  74 */     defaultQueryFilter.addFilter("inst_id_", model.getInstanceId(), QueryOP.EQUAL);
/*  75 */     defaultQueryFilter.addFilter("task_type_", "SIGN_SOURCE", QueryOP.NOT_EQUAL);
/*  76 */     List<BpmTask> taskList = this.taskMananger.query((QueryFilter)defaultQueryFilter);
/*  77 */     if (CollectionUtil.isEmpty(taskList)) {
/*  78 */       throw new BusinessMessage("当前实例任务不存在，无需催办！");
/*     */     }
/*     */     
/*  81 */     for (int i = 0; i < taskList.size(); i++) {
/*  82 */       BpmTask task = taskList.get(i);
/*  83 */       List<SysIdentity> identitys = this.taskMananger.getAssignUserById(task);
/*     */       
/*  85 */       if (isUrgent.booleanValue()) {
/*  86 */         task.setPriority(Integer.valueOf(task.getPriority().intValue() + 3));
/*     */       }
/*  88 */       task.setPriority(Integer.valueOf(task.getPriority().intValue() + 4));
/*  89 */       this.taskMananger.update(task);
/*     */       
/*     */       try {
/*  92 */         if (StringUtil.isNotEmpty(content)) {
/*  93 */           content = content.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
/*  94 */           content = this.freemarkerEngine.parseByString(content, getVariables(instance));
/*     */         } 
/*  96 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/*  99 */       identitys = extractBpmIdentity(identitys);
/* 100 */       if (!CollectionUtil.isEmpty(identitys) && !StringUtil.isEmpty(opinion)) {
/*     */ 
/*     */         
/* 103 */         opinion = opinion.replaceAll("subject", ((BpmTask)taskList.get(0)).getSubject());
/*     */         
/* 105 */         List<JmsDTO> jmsDto = new ArrayList<>();
/* 106 */         BpmReminderLog log = getReminderLog(instance, msgType, opinion, content, task);
/* 107 */         Map<String, Object> extendVars = new HashMap<>();
/* 108 */         extendVars.put("detailId", task.getId());
/* 109 */         extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
/* 110 */         if (isUrgent.booleanValue()) {
/* 111 */           extendVars.put("statue", "urgent,remind");
/*     */         } else {
/* 113 */           extendVars.put("statue", "remind");
/*     */         } 
/* 115 */         extendVars.put("title", String.format("%s于%s发起了待办任务《%s》", new Object[] { ContextUtil.getCurrentUser().getFullname(), 
/* 116 */                 DateUtil.formatDate(task.getCreateTime()), task.getSubject() }));
/* 117 */         extendVars.put("type", "待办");
/* 118 */         extendVars.put("head", "待办");
/*     */         
/* 120 */         for (String type : msgType.split(",")) {
/* 121 */           NotifyMessage message = new NotifyMessage(opinion, content, ContextUtil.getCurrentUser(), identitys);
/* 122 */           message.setTag("待办任务");
/* 123 */           message.setExtendVars(extendVars);
/* 124 */           jmsDto.add(new DefaultJmsDTO(type, (Serializable)message));
/*     */         } 
/* 126 */         this.jmsProducer.sendToQueue(jmsDto);
/*     */         
/* 128 */         log.setIsUrgent(isUrgent.booleanValue() ? "1" : "0");
/* 129 */         StringBuffer userids = new StringBuffer();
/* 130 */         StringBuffer userNames = new StringBuffer();
/* 131 */         identitys.stream().filter(user -> (userids.indexOf(user.getId()) == -1)).forEach(user -> {
/*     */               userids.append(user.getId()).append(",");
/*     */               userNames.append(user.getName()).append(",");
/*     */             });
/* 135 */         log.setReminderUserids(userids.substring(0, userids.length() - 1));
/* 136 */         log.setReminderUsers(userNames.substring(0, userNames.length() - 1));
/* 137 */         this.reminderLogManager.create(log);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> getVariables(BpmInstance instance) {
/* 151 */     Map<String, Object> variables = new HashMap<>();
/* 152 */     variables.put("bpmInstance", instance);
/* 153 */     return variables;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 158 */     return ActionType.REMINDER;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 163 */     return 7;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 171 */     System.out.println();
/* 172 */     return Boolean.valueOf((nodeDef == null || nodeDef.getType() == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 177 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 182 */     return "/bpm/task/reminderActionDialog.html";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 188 */     return "return bpmInstance.getStatus().equals(\"running\") || bpmInstance.getStatus().equals(\"back\");";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 194 */     return null;
/*     */   }
/*     */   
/*     */   private BpmReminderLog getReminderLog(BpmInstance instance, String msgType, String reminderTitle, String reminderExtend, BpmTask task) {
/* 198 */     BpmReminderLog reminderLog = new BpmReminderLog();
/* 199 */     reminderLog.setInstanceId(instance.getId());
/* 200 */     reminderLog.setReminderDate(new Date());
/* 201 */     reminderLog.setNodeId(task.getNodeId());
/* 202 */     reminderLog.setNodeName(task.getName());
/* 203 */     reminderLog.setSubject(instance.getSubject());
/* 204 */     reminderLog.setReminderTitle(reminderTitle);
/* 205 */     reminderLog.setExtend(reminderExtend);
/* 206 */     reminderLog.setMsgType(msgType);
/* 207 */     IUser user = ContextUtil.getCurrentUser();
/* 208 */     reminderLog.setFromUserIds(user.getUserId());
/* 209 */     reminderLog.setFromUsers(user.getFullname());
/* 210 */     reminderLog.setTaskId(task.getId());
/* 211 */     return reminderLog;
/*     */   }
/*     */   
/*     */   private List<SysIdentity> extractBpmIdentity(List<SysIdentity> identities) {
/* 215 */     List<SysIdentity> results = new ArrayList<>();
/* 216 */     for (SysIdentity bpmIdentity : identities) {
/* 217 */       if ("user".equals(bpmIdentity.getType())) {
/* 218 */         results.add(bpmIdentity); continue;
/*     */       } 
/* 220 */       List<IUser> users = this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
/* 221 */       for (IUser user : users) {
/* 222 */         results.add(new DefaultIdentity(user));
/*     */       }
/*     */     } 
/*     */     
/* 226 */     return results;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/reminder/handler/InstanceReminderActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */