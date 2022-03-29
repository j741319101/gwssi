package com.dstz.bpm.plugin.global.reminder.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.engine.action.handler.ActionDisplayHandler;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
import com.dstz.bpm.plugin.core.model.BpmReminderLog;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.JsonUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.freemark.IFreemarkerEngine;
import com.dstz.sys.api.jms.model.DefaultJmsDTO;
import com.dstz.sys.api.jms.model.JmsDTO;
import com.dstz.sys.api.jms.model.msg.NotifyMessage;
import com.dstz.sys.api.jms.producer.JmsProducer;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component todo 暂时去掉催办功能
//public class InstanceReminderActionHandler implements ActionDisplayHandler<DefaultInstanceActionCmd> {
public class InstanceReminderActionHandler{
   @Autowired
   BpmTaskManager taskMananger;
   @Autowired
   JmsProducer jmsProducer;
   @Autowired
   IFreemarkerEngine freemarkerEngine;
   @Autowired
   BpmReminderLogManager reminderLogManager;
   @Autowired
   BpmInstanceManager bpmInstanceManager;
   @Autowired
   UserService userService;

   public void execute(DefaultInstanceActionCmd model) {
      String opinion = model.getOpinion();
      BpmInstance instance = (BpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
      Boolean isUrgent = JsonUtil.getBoolean(model.getExtendConf(), "isUrgent");
      String msgType = JsonUtil.getString(model.getExtendConf(), "msgType");
      String content = JsonUtil.getString(model.getExtendConf(), "content");
      if (StringUtil.isEmpty(opinion)) {
         throw new BusinessMessage("催办提醒内容不可为空！");
      } else {
         QueryFilter queryFilter = new DefaultQueryFilter(true);
         queryFilter.addFilter("inst_id_", model.getInstanceId(), QueryOP.EQUAL);
         queryFilter.addFilter("task_type_", "SIGN_SOURCE", QueryOP.NOT_EQUAL);
         List<BpmTask> taskList = this.taskMananger.query(queryFilter);
         if (CollectionUtil.isEmpty(taskList)) {
            throw new BusinessMessage("当前实例任务不存在，无需催办！");
         } else {
            for(int i = 0; i < taskList.size(); ++i) {
               BpmTask task = (BpmTask)taskList.get(i);
               List<SysIdentity> identitys = this.taskMananger.getAssignUserById(task);
               if (isUrgent) {
                  task.setPriority(task.getPriority() + 3);
               }

               task.setPriority(task.getPriority() + 4);
               this.taskMananger.update(task);

               try {
                  if (StringUtil.isNotEmpty(content)) {
                     content = content.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
                     content = this.freemarkerEngine.parseByString(content, this.getVariables(instance));
                  }
               } catch (Exception var20) {
                  ;
               }

               identitys = this.extractBpmIdentity(identitys);
               if (!CollectionUtil.isEmpty(identitys) && !StringUtil.isEmpty(opinion)) {
                  opinion = opinion.replaceAll("subject", ((BpmTask)taskList.get(0)).getSubject());
                  List<JmsDTO> jmsDto = new ArrayList();
                  BpmReminderLog log = this.getReminderLog(instance, msgType, opinion, content, task);
                  Map<String, Object> extendVars = new HashMap();
                  extendVars.put("detailId", task.getId());
                  extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
                  if (isUrgent) {
                     extendVars.put("statue", "urgent,remind");
                  } else {
                     extendVars.put("statue", "remind");
                  }

                  extendVars.put("title", String.format("%s于%s发起了待办任务《%s》", ContextUtil.getCurrentUser().getFullname(), DateUtil.formatDate(task.getCreateTime()), task.getSubject()));
                  extendVars.put("type", "待办");
                  extendVars.put("head", "待办");
                  String[] var15 = msgType.split(",");
                  int var16 = var15.length;

                  for(int var17 = 0; var17 < var16; ++var17) {
                     String type = var15[var17];
                     NotifyMessage message = new NotifyMessage(opinion, content, ContextUtil.getCurrentUser(), identitys);
                     message.setTag("待办任务");
                     message.setExtendVars(extendVars);
                     jmsDto.add(new DefaultJmsDTO(type, message));
                  }

                  this.jmsProducer.sendToQueue(jmsDto);
                  log.setIsUrgent(isUrgent ? "1" : "0");
                  StringBuffer userids = new StringBuffer();
                  StringBuffer userNames = new StringBuffer();
                  identitys.stream().filter((user) -> {
                     return userids.indexOf(user.getId()) == -1;
                  }).forEach((user) -> {
                     userids.append(user.getId()).append(",");
                     userNames.append(user.getName()).append(",");
                  });
                  log.setReminderUserids(userids.substring(0, userids.length() - 1));
                  log.setReminderUsers(userNames.substring(0, userNames.length() - 1));
                  this.reminderLogManager.create(log);
               }
            }

         }
      }
   }

   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
      return true;
   }

   private Map<String, Object> getVariables(BpmInstance instance) {
      Map<String, Object> variables = new HashMap();
      variables.put("bpmInstance", instance);
      return variables;
   }

   public ActionType getActionType() {
      return ActionType.REMINDER;
   }

   public int getSn() {
      return 7;
   }

   public Boolean isSupport(BpmNodeDef nodeDef) {
      System.out.println();
//      return nodeDef == null || nodeDef.getType() == null;
      return false;//todo 暂时去掉按钮
   }

   public Boolean isDefault() {
      return true;
   }

   public String getConfigPage() {
      return "/bpm/task/reminderActionDialog.html";
   }

   public String getDefaultGroovyScript() {
      return "return bpmInstance.getStatus().equals(\"running\") || bpmInstance.getStatus().equals(\"back\");";
   }

   public String getDefaultBeforeScript() {
      return null;
   }

   private BpmReminderLog getReminderLog(BpmInstance instance, String msgType, String reminderTitle, String reminderExtend, BpmTask task) {
      BpmReminderLog reminderLog = new BpmReminderLog();
      reminderLog.setInstanceId(instance.getId());
      reminderLog.setReminderDate(new Date());
      reminderLog.setNodeId(task.getNodeId());
      reminderLog.setNodeName(task.getName());
      reminderLog.setSubject(instance.getSubject());
      reminderLog.setReminderTitle(reminderTitle);
      reminderLog.setExtend(reminderExtend);
      reminderLog.setMsgType(msgType);
      IUser user = ContextUtil.getCurrentUser();
      reminderLog.setFromUserIds(user.getUserId());
      reminderLog.setFromUsers(user.getFullname());
      reminderLog.setTaskId(task.getId());
      return reminderLog;
   }

   private List<SysIdentity> extractBpmIdentity(List<SysIdentity> identities) {
      List<SysIdentity> results = new ArrayList();
      Iterator var3 = identities.iterator();

      while(true) {
         while(var3.hasNext()) {
            SysIdentity bpmIdentity = (SysIdentity)var3.next();
            if ("user".equals(bpmIdentity.getType())) {
               results.add(bpmIdentity);
            } else {
               List<IUser> users =(List<IUser>) this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
               Iterator var6 = users.iterator();

               while(var6.hasNext()) {
                  IUser user = (IUser)var6.next();
                  results.add(new DefaultIdentity(user));
               }
            }
         }

         return results;
      }
   }
}
