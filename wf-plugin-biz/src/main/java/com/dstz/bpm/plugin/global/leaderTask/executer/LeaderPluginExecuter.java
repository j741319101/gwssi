/*    */ package com.dstz.bpm.plugin.global.leaderTask.executer;
/*    */ 
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.constant.TaskStatus;
/*    */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*    */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmLeaderTaskLogManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmLeaderTaskLog;
/*    */ import com.dstz.bpm.plugin.global.leaderTask.def.LeaderTaskPluginDef;
/*    */ import com.dstz.org.api.service.LeaderService;
/*    */ import dm.jdbc.util.StringUtil;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class LeaderPluginExecuter
/*    */   extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, LeaderTaskPluginDef> {
/*    */   @Autowired
/*    */   private BpmLeaderTaskLogManager bpmLeaderTaskLogManager;
/*    */   @Autowired
/*    */   private BpmTaskManager bpmTaskManager;
/*    */   @Autowired
/*    */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*    */   
/*    */   public Void execute(DefaultBpmTaskPluginSession pluginSession, LeaderTaskPluginDef pluginDef) {
/* 32 */     BpmTask task = (BpmTask)pluginSession.getBpmTask();
/* 33 */     if (pluginSession.getEventType() == EventType.TASK_POST_CREATE_EVENT && 
/* 34 */       !StringUtil.equals("0", task.getAssigneeId())) {
/*    */       
/* 36 */       LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 37 */       if (!leaderService.isLeaderByLeaderId(task.getAssigneeId())) {
/* 38 */         return null;
/*    */       }
/*    */       
/* 41 */       task.setStatus(TaskStatus.LEADER.getKey());
/* 42 */       this.bpmTaskManager.update(task);
/* 43 */       BpmLeaderTaskLog taskLog = new BpmLeaderTaskLog();
/* 44 */       taskLog.setTaskId(task.getId());
/* 45 */       taskLog.setInstId(task.getInstId());
/* 46 */       taskLog.setLeaderId(task.getAssigneeId());
/* 47 */       taskLog.setLeaderName(task.getAssigneeNames());
/* 48 */       this.bpmLeaderTaskLogManager.create(taskLog);
/*    */     } 
/*    */ 
/*    */     
/* 52 */     if (pluginSession.getEventType() == EventType.TASK_POST_COMPLETE_EVENT)
/*    */     {
/* 54 */       if (!StringUtil.equals("0", task.getAssigneeId()) && StringUtils.equals(task.getStatus(), TaskStatus.LEADER.getKey())) {
/* 55 */         BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(task.getId());
/* 56 */         bpmTaskOpinion.setApprover(task.getAssigneeId());
/* 57 */         bpmTaskOpinion.setApproverName(task.getAssigneeNames());
/* 58 */         this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/*    */       } 
/*    */     }
/* 61 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/leaderTask/executer/LeaderPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */