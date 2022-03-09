/*     */ package com.dstz.bpm.plugin.global.leaderTask.executer;
/*     */ 
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.bpm.api.constant.TaskStatus;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmLeaderOptionLogManager;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmLeaderTaskLogManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmLeaderTaskLog;
/*     */ import com.dstz.bpm.plugin.global.leaderTask.context.LeaderTaskPlaginContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.LeaderService;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import com.alibaba.druid.util.StringUtils;
/*     */ import dm.jdbc.util.StringUtil;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class LeaderTaskAction
/*     */   extends DefaultExtendTaskAction
/*     */ {
/*     */   @Autowired
/*     */   private BpmLeaderTaskLogManager bpmLeaderTaskLogManager;
/*     */   @Autowired
/*     */   private BpmLeaderOptionLogManager bpmLeaderOptionLogManager;
/*     */   @Autowired
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   
/*     */   public void turnLeaderTask(IBpmTask bpmTask) {
/*  37 */     LeaderTaskPlaginContext leaderTaskPlaginContext = (LeaderTaskPlaginContext)AppUtil.getBean("leaderTaskPlaginContext");
/*  38 */     if (leaderTaskPlaginContext.isEffective()) {
/*  39 */       BpmTask task = (BpmTask)bpmTask;
/*  40 */       LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/*  41 */       if (!StringUtil.equals("0", task.getAssigneeId()) && leaderService.isLeaderByLeaderId(task.getAssigneeId())) {
/*     */         
/*  43 */         task.setStatus(TaskStatus.LEADER.getKey());
/*  44 */         this.bpmTaskManager.update(task);
/*  45 */         BpmLeaderTaskLog taskLog = new BpmLeaderTaskLog();
/*  46 */         taskLog.setTaskId(task.getId());
/*  47 */         taskLog.setInstId(task.getInstId());
/*  48 */         taskLog.setLeaderId(task.getAssigneeId());
/*  49 */         taskLog.setLeaderName(task.getAssigneeNames());
/*  50 */         taskLog.setType("1");
/*  51 */         this.bpmLeaderTaskLogManager.create(taskLog);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean carbonCopyLeaderTask(String receiverId, String receId, IBpmTask bpmTask) {
/*  58 */     LeaderTaskPlaginContext leaderTaskPlaginContext = (LeaderTaskPlaginContext)AppUtil.getBean("leaderTaskPlaginContext");
/*  59 */     if (leaderTaskPlaginContext.isEffective()) {
/*  60 */       BpmTask task = (BpmTask)bpmTask;
/*     */       
/*  62 */       LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/*  63 */       if (leaderService.isLeaderByLeaderId(receiverId)) {
/*  64 */         IUser secretary = ContextUtil.getCurrentUser();
/*  65 */         IUser leader = leaderService.getUserBySecretaryId(secretary.getUserId());
/*     */         
/*  67 */         if (leader != null && !StringUtils.equals(receiverId, leader.getUserId())) {
/*     */           
/*  69 */           BpmLeaderTaskLog taskLog = new BpmLeaderTaskLog();
/*  70 */           taskLog.setTaskId(task.getId());
/*  71 */           taskLog.setInstId(task.getInstId());
/*  72 */           taskLog.setLeaderId(task.getAssigneeId());
/*  73 */           taskLog.setLeaderName(task.getAssigneeNames());
/*  74 */           taskLog.setType("2");
/*  75 */           this.bpmLeaderTaskLogManager.create(taskLog);
/*  76 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean carbonInstCopyLeaderTask(String receiverId, String receId, IBpmInstance bpmInstance) {
/*  85 */     LeaderTaskPlaginContext leaderTaskPlaginContext = (LeaderTaskPlaginContext)AppUtil.getBean("leaderTaskPlaginContext");
/*  86 */     if (leaderTaskPlaginContext.isEffective()) {
/*  87 */       LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/*  88 */       BpmInstance inst = (BpmInstance)bpmInstance;
/*     */       
/*  90 */       if (leaderService.isLeaderByLeaderId(receiverId)) {
/*  91 */         IUser secretary = ContextUtil.getCurrentUser();
/*  92 */         IUser leader = leaderService.getUserBySecretaryId(secretary.getUserId());
/*     */         
/*  94 */         if (!StringUtils.equals(receiverId, leader.getUserId())) {
/*     */           
/*  96 */           BpmLeaderTaskLog taskLog = new BpmLeaderTaskLog();
/*  97 */           taskLog.setInstId(inst.getId());
/*  98 */           taskLog.setLeaderId(leader.getUserId());
/*  99 */           taskLog.setLeaderName(leader.getFullname());
/* 100 */           taskLog.setType("2");
/* 101 */           this.bpmLeaderTaskLogManager.create(taskLog);
/* 102 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   public String getLeaderUserRights() {
/* 110 */     LeaderTaskPlaginContext leaderTaskPlaginContext = (LeaderTaskPlaginContext)AppUtil.getBean("leaderTaskPlaginContext");
/*     */     
/* 112 */     if (leaderTaskPlaginContext.isEffective()) {
/* 113 */       LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/*     */       
/* 115 */       IUser leader = leaderService.getUserBySecretaryId(ContextUtil.getCurrentUserId());
/* 116 */       if (leader != null) {
/* 117 */         return String.format("%s-%s", new Object[] { leader.getUserId(), "user" });
/*     */       }
/*     */     } 
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteDataByInstId(String instId) {
/* 125 */     this.bpmLeaderOptionLogManager.removeByInstId(instId);
/* 126 */     this.bpmLeaderTaskLogManager.removeByInstId(instId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/leaderTask/executer/LeaderTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */