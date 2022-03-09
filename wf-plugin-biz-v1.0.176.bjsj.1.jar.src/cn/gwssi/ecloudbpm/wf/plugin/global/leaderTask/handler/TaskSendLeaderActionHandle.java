/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.handler;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.task.TaskSaveActionHandler;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmLeaderOptionLogManager;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmLeaderTaskLogManager;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmLeaderOptionLog;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*    */ import cn.gwssi.ecloudframework.org.api.service.LeaderService;
/*    */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class TaskSendLeaderActionHandle
/*    */   extends TaskSaveActionHandler
/*    */ {
/*    */   @Autowired
/*    */   private BpmLeaderTaskLogManager bpmLeaderTaskLogManager;
/*    */   @Resource
/*    */   private BpmLeaderOptionLogManager bpmLeaderOptionLogManager;
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
/* 42 */     BpmTask bpmTask = (BpmTask)actionModel.getBpmTask();
/* 43 */     this.bpmLeaderTaskLogManager.updateByTaskId(bpmTask.getId(), "1", "1");
/* 44 */     BpmLeaderOptionLog optionLog = new BpmLeaderOptionLog();
/* 45 */     IUser secretary = ContextUtil.getCurrentUser();
/* 46 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 47 */     IUser leader = leaderService.getUserBySecretaryId(secretary.getUserId());
/* 48 */     optionLog.setTaskId(bpmTask.getId());
/* 49 */     optionLog.setInstId(bpmTask.getInstId());
/* 50 */     optionLog.setLeaderId(leader.getUserId());
/* 51 */     optionLog.setLeaderName(leader.getFullname());
/* 52 */     optionLog.setSecretaryId(secretary.getUserId());
/* 53 */     optionLog.setSecretaryName(secretary.getFullname());
/* 54 */     optionLog.setOption(actionModel.getOpinion());
/* 55 */     optionLog.setType("1");
/* 56 */     this.bpmLeaderOptionLogManager.create(optionLog);
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
/* 70 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 75 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 80 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/leaderTask/handler/TaskSendLeaderActionHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */