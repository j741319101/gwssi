/*    */ package com.dstz.bpm.plugin.global.leaderTask.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import com.dstz.bpm.engine.action.handler.task.TaskSaveActionHandler;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmLeaderOptionLogManager;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmLeaderTaskLogManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmLeaderOptionLog;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.org.api.service.LeaderService;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class TaskLeaderSaveActionHandler
/*    */   extends TaskSaveActionHandler
/*    */ {
/*    */   @Autowired
/*    */   private BpmLeaderTaskLogManager bpmLeaderTaskLogManager;
/*    */   @Resource
/*    */   private BpmLeaderOptionLogManager bpmLeaderOptionLogManager;
/*    */   
/*    */   public ActionType getActionType() {
/* 33 */     return ActionType.LEADERSAVE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void doAction(DefualtTaskActionCmd actionModel) {
/* 40 */     BpmTask task = (BpmTask)actionModel.getBpmTask();
/* 41 */     this.bpmLeaderTaskLogManager.updateByTaskId(task.getId(), "1", "2");
/* 42 */     IUser leader = ContextUtil.getCurrentUser();
/* 43 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 44 */     IUser secretary = leaderService.getUserByLeaderId(leader.getUserId());
/* 45 */     BpmLeaderOptionLog optionLog = new BpmLeaderOptionLog();
/* 46 */     optionLog.setTaskId(task.getId());
/* 47 */     optionLog.setInstId(task.getInstId());
/* 48 */     optionLog.setLeaderId(leader.getUserId());
/* 49 */     optionLog.setLeaderName(leader.getFullname());
/* 50 */     optionLog.setSecretaryId(secretary.getUserId());
/* 51 */     optionLog.setSecretaryName(secretary.getFullname());
/* 52 */     optionLog.setOption(actionModel.getOpinion());
/* 53 */     optionLog.setType("2");
/* 54 */     this.bpmLeaderOptionLogManager.create(optionLog);
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
/* 68 */     return 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 74 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 79 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/leaderTask/handler/TaskLeaderSaveActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */