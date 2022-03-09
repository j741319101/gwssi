/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskType;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.TaskIdentityLinkManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.TaskIdentityLink;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmLeaderTaskLogManager;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmLeaderTaskLog;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.def.LeaderTaskPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.gwssi.ecloudframework.org.api.service.LeaderService;
/*    */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*    */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*    */ import dm.jdbc.util.StringUtil;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class LeaderTaskPluginExecuter
/*    */   extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, LeaderTaskPluginDef> {
/*    */   @Autowired
/*    */   private BpmLeaderTaskLogManager bpmLeaderTaskLogManager;
/*    */   @Autowired
/*    */   private BpmTaskManager bpmTaskManager;
/*    */   @Autowired
/*    */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*    */   @Resource
/*    */   private UserService userService;
/*    */   @Resource
/*    */   private TaskIdentityLinkManager taskIdentityLinkManager;
/*    */   
/*    */   public Void execute(DefaultBpmTaskPluginSession pluginSession, LeaderTaskPluginDef pluginDef) {
/* 41 */     if (!pluginDef.isSignLeaderTask()) {
/* 42 */       return null;
/*    */     }
/* 44 */     BpmTask bpmTaskUpdate = (BpmTask)pluginSession.getBpmTask();
/* 45 */     if (pluginSession.getEventType() == EventType.TASK_POST_CREATE_EVENT) {
/* 46 */       if (StringUtils.equals(bpmTaskUpdate.getTaskType(), TaskType.SIGN_SOURCE.getKey())) {
/*    */         
/* 48 */         this.bpmTaskManager.getByParentId(bpmTaskUpdate.getId()).stream().filter(task -> StringUtils.equals(task.getTaskType(), TaskType.SIGN.getKey()))
/*    */           
/* 50 */           .forEach(task -> createLeaderLog(task));
/* 51 */       } else if (!StringUtil.equals("0", bpmTaskUpdate.getAssigneeId())) {
/* 52 */         createLeaderLog(bpmTaskUpdate);
/*    */       } 
/*    */     }
/*    */     
/* 56 */     if (pluginSession.getEventType() == EventType.TASK_POST_COMPLETE_EVENT) {
/*    */       
/* 58 */       BpmLeaderTaskLog bpmLeaderTaskLog = this.bpmLeaderTaskLogManager.getByTaskId(bpmTaskUpdate.getId());
/* 59 */       if (bpmLeaderTaskLog != null) {
/* 60 */         BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTaskUpdate.getId());
/* 61 */         bpmTaskOpinion.setApprover(bpmTaskUpdate.getAssigneeId());
/* 62 */         bpmTaskOpinion.setApproverName(bpmTaskUpdate.getAssigneeNames());
/* 63 */         this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/* 64 */         bpmLeaderTaskLog.setStatus("end");
/* 65 */         this.bpmLeaderTaskLogManager.update(bpmLeaderTaskLog);
/*    */         
/* 67 */         ContextUtil.setCurrentUser(this.userService.getUserById(bpmTaskUpdate.getAssigneeId()));
/*    */       } 
/*    */     } 
/* 70 */     return null;
/*    */   }
/*    */   
/*    */   private void createLeaderLog(BpmTask bpmTaskUpdate) {
/* 74 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 75 */     if (!leaderService.isLeaderByLeaderId(bpmTaskUpdate.getAssigneeId())) {
/*    */       return;
/*    */     }
/*    */     
/* 79 */     TaskIdentityLink taskIdentityLink = this.taskIdentityLinkManager.getByTaskId(bpmTaskUpdate.getId()).get(0);
/* 80 */     BpmLeaderTaskLog taskLog = new BpmLeaderTaskLog();
/* 81 */     taskLog.setTaskId(bpmTaskUpdate.getId());
/* 82 */     taskLog.setInstId(bpmTaskUpdate.getInstId());
/* 83 */     taskLog.setLeaderId(bpmTaskUpdate.getAssigneeId());
/* 84 */     taskLog.setLeaderName(bpmTaskUpdate.getAssigneeNames());
/* 85 */     taskLog.setOrgId(taskIdentityLink.getOrgId());
/* 86 */     this.bpmLeaderTaskLogManager.create(taskLog);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/leaderTask/executer/LeaderTaskPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */