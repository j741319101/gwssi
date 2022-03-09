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
/*    */ public class TaskSendLeaderActionHandle
/*    */   extends TaskSaveActionHandler {
/*    */   @Autowired
/*    */   private BpmLeaderTaskLogManager bpmLeaderTaskLogManager;
/*    */   
/*    */   public ActionType getActionType() {
/* 34 */     return ActionType.SENDLEADER;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void doAction(DefualtTaskActionCmd actionModel) {
/* 42 */     BpmTask task = (BpmTask)actionModel.getBpmTask();
/* 43 */     this.bpmLeaderTaskLogManager.updateByTaskId(task.getId(), "1", "1");
/*    */     
/* 45 */     sendNotifyMessageToLeader(task);
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
/*    */   
/*    */   public int getSn() {
/* 59 */     return 1;
/*    */   }
/*    */   
/*    */   public static void sendNotifyMessageToLeader(BpmTask task) {
/* 63 */     JmsProducer jmsProducer = (JmsProducer)AppUtil.getBean("redisMessageQueueProducer");
/* 64 */     List<JmsDTO> jmsDto = new ArrayList<>();
/* 65 */     Map<String, Object> extendVars = new HashMap<>();
/* 66 */     extendVars.put("detailId", task.getId());
/* 67 */     IUser secretary = ContextUtil.getCurrentUser();
/* 68 */     extendVars.put("name", secretary.getFullname());
/* 69 */     extendVars.put("statue", "toLeader");
/* 70 */     String title = "%s于%s呈送了待办任务《%s》";
/* 71 */     extendVars.put("title", String.format(title, new Object[] { secretary.getFullname(), DateUtil.formatDate(task.getCreateTime()), task.getSubject() }));
/* 72 */     extendVars.put("type", "待办");
/* 73 */     extendVars.put("head", "待办");
/* 74 */     List<SysIdentity> userList = new ArrayList<>();
/* 75 */     DefaultIdentity defaultIdentity = new DefaultIdentity();
/* 76 */     defaultIdentity.setId(secretary.getUserId());
/* 77 */     defaultIdentity.setName(secretary.getFullname());
/* 78 */     userList.add(defaultIdentity);
/* 79 */     NotifyMessage message = new NotifyMessage("待办呈送", "待办呈送", null, userList);
/* 80 */     message.setTag("待办任务");
/* 81 */     message.setExtendVars(extendVars);
/* 82 */     jmsDto.add(new DefaultJmsDTO("inner", (Serializable)message));
/* 83 */     jmsProducer.sendToQueue(jmsDto);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 88 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 93 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/leaderTask/handler/TaskSendLeaderActionHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */