/*    */ package com.dstz.bpm.plugin.global.leaderTask.handler;
/*    */ 
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import com.dstz.bpm.engine.action.handler.task.TaskSaveActionHandler;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmLeaderTaskLogManager;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.sys.api.jms.model.DefaultJmsDTO;
/*    */ import com.dstz.sys.api.jms.model.JmsDTO;
/*    */ import com.dstz.sys.api.jms.model.msg.NotifyMessage;
/*    */ import com.dstz.sys.api.jms.producer.JmsProducer;
/*    */ import com.dstz.sys.api.model.DefaultIdentity;
/*    */ import com.dstz.sys.api.model.SysIdentity;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import cn.hutool.core.date.DateUtil;
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class TaskLeaderSaveActionHandler
/*    */   extends TaskSaveActionHandler {
/*    */   @Autowired
/*    */   private BpmLeaderTaskLogManager bpmLeaderTaskLogManager;
/*    */   
/*    */   public ActionType getActionType() {
/* 34 */     return ActionType.LEADERSAVE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doAction(DefualtTaskActionCmd actionModel) {
/* 40 */     BpmTask task = (BpmTask)actionModel.getBpmTask();
/* 41 */     this.bpmLeaderTaskLogManager.updateByTaskId(task.getId(), "1", "2");
/*    */     
/* 43 */     sendNotifyMessageToSecretary(task);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */   
/*    */   public static void sendNotifyMessageToSecretary(BpmTask task) {
/* 56 */     JmsProducer jmsProducer = (JmsProducer)AppUtil.getBean("redisMessageQueueProducer");
/* 57 */     List<JmsDTO> jmsDto = new ArrayList<>();
/* 58 */     Map<String, Object> extendVars = new HashMap<>();
/* 59 */     extendVars.put("detailId", task.getId());
/* 60 */     IUser secretary = ContextUtil.getCurrentUser();
/* 61 */     extendVars.put("name", secretary.getFullname());
/* 62 */     extendVars.put("statue", "toSecretary");
/* 63 */     String title = "%s于%s返回了待办任务《%s》";
/* 64 */     extendVars.put("title", String.format(title, new Object[] { secretary.getFullname(), DateUtil.formatDate(task.getCreateTime()), task.getSubject() }));
/* 65 */     extendVars.put("type", "待办");
/* 66 */     extendVars.put("head", "待办");
/* 67 */     List<SysIdentity> userList = new ArrayList<>();
/* 68 */     DefaultIdentity defaultIdentity = new DefaultIdentity();
/* 69 */     defaultIdentity.setId(secretary.getUserId());
/* 70 */     defaultIdentity.setName(secretary.getFullname());
/* 71 */     userList.add(defaultIdentity);
/* 72 */     NotifyMessage message = new NotifyMessage("待办返回", "待办返回", null, userList);
/* 73 */     message.setTag("待办任务");
/* 74 */     message.setExtendVars(extendVars);
/* 75 */     jmsDto.add(new DefaultJmsDTO("inner", (Serializable)message));
/* 76 */     jmsProducer.sendToQueue(jmsDto);
/*    */   }
/*    */   
/*    */   public int getSn() {
/* 80 */     return 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 86 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 91 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/leaderTask/handler/TaskLeaderSaveActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */