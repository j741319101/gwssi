/*     */ package com.dstz.bpm.plugin.core.manager.impl;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.bpm.api.constant.TaskStatus;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskApprove;
/*     */ import com.dstz.bpm.core.vo.BpmTaskVO;
/*     */ import com.dstz.bpm.plugin.core.dao.BpmLeaderTaskLogDao;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmLeaderOptionLogManager;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmLeaderTaskLogManager;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
/*     */
import com.dstz.bpm.plugin.core.model.BpmLeaderOptionLog;
/*     */ import com.dstz.bpm.plugin.core.model.BpmLeaderTaskLog;
/*     */ import com.dstz.bpm.plugin.core.model.BpmReminderLog;
/*     */ import com.dstz.bpm.plugin.global.leaderTask.handler.TaskLeaderSaveActionHandler;
/*     */ import com.dstz.bpm.plugin.global.leaderTask.handler.TaskSendLeaderActionHandle;
/*     */ import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.LeaderService;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service("BpmLeaderTaskLogManager")
/*     */ public class BpmLeaderTaskLogManagerImpl
/*     */   extends BaseManager<String, BpmLeaderTaskLog>
/*     */   implements BpmLeaderTaskLogManager {
/*     */   @Resource
/*     */   private BpmLeaderTaskLogDao bpmLeaderTaskLogDao;
/*     */   @Resource
/*     */ BpmReminderLogManager bpmReminderLogManager;
/*     */   @Resource
/*     */ BpmLeaderOptionLogManager bpmLeaderOptionLogManager;
/*     */   @Resource
/*     */   BpmTaskManager bpmTaskManager;
/*     */   
/*     */   public Integer updateByTaskId(String taskId, String type, String flag) {
/*  49 */     BpmLeaderTaskLog taskLog = new BpmLeaderTaskLog();
/*  50 */     taskLog.setTaskId(taskId);
/*  51 */     taskLog.setFlag(flag);
/*  52 */     taskLog.setType(type);
/*  53 */     return this.bpmLeaderTaskLogDao.updateByTaskId(taskLog);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskVO> getTodoList(QueryFilter queryFilter) {
/*  58 */     IUser user = ContextUtil.getCurrentUser();
/*  59 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/*  60 */     IUser leader = leaderService.getUserBySecretaryId(user.getUserId());
/*  61 */     if (leader == null) {
/*  62 */       return new ArrayList<>();
/*     */     }
/*  64 */     queryFilter.addFilter("task.ASSIGNEE_ID_", leader.getUserId(), QueryOP.EQUAL);
/*  65 */     List<BpmTaskVO> taskVOS = this.bpmLeaderTaskLogDao.getTodoList(queryFilter);
/*  66 */     taskVOS.forEach(task -> {
/*     */           int priority = Integer.valueOf(task.getPriority().intValue()).intValue();
/*     */           
/*     */           if (priority > 50) {
/*     */             DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*     */             
/*     */             defaultQueryFilter.addFilter("task_id_", task.getId(), QueryOP.EQUAL);
/*     */             
/*     */             List<BpmReminderLog> logs = this.bpmReminderLogManager.query((QueryFilter)defaultQueryFilter);
/*     */             
/*     */             AtomicReference<String> remindStatus = new AtomicReference<>();
/*     */             if (logs.size() > 0) {
/*     */               remindStatus.set("remind");
/*     */               logs.forEach(());
/*     */             } 
/*     */             if (StringUtils.equals(task.getStatus(), TaskStatus.BACK.getKey())) {
/*     */               if (StringUtil.isEmpty(remindStatus.get())) {
/*     */                 remindStatus.set("back");
/*     */               } else {
/*     */                 remindStatus.set((String)remindStatus.get() + ",back");
/*     */               } 
/*     */             }
/*     */             task.setRemindStatus(remindStatus.get());
/*     */           } 
/*     */         });
/*  91 */     return taskVOS;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskApprove> getApproveHistoryList(QueryFilter queryFilter) {
/*  96 */     IUser user = ContextUtil.getCurrentUser();
/*  97 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/*  98 */     IUser leader = leaderService.getUserBySecretaryId(user.getUserId());
/*  99 */     if (leader == null) {
/* 100 */       return new ArrayList<>();
/*     */     }
/* 102 */     queryFilter.addFilter("o.approver_", leader.getUserId(), QueryOP.EQUAL);
/* 103 */     return this.bpmLeaderTaskLogDao.getApproveHistoryList(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter queryFilter) {
/* 108 */     IUser user = ContextUtil.getCurrentUser();
/* 109 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 110 */     IUser leader = leaderService.getUserBySecretaryId(user.getUserId());
/* 111 */     if (leader == null) {
/* 112 */       return new ArrayList<>();
/*     */     }
/* 114 */     queryFilter.addFilter("a.receive_user_id", leader.getUserId(), QueryOP.EQUAL);
/* 115 */     return this.bpmLeaderTaskLogDao.listUserReceive(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveOptionAndUpdateStatusToLeader(String taskId, String option) {
/* 120 */     IUser secretary = ContextUtil.getCurrentUser();
/* 121 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 122 */     IUser leader = leaderService.getUserBySecretaryId(secretary.getUserId());
/* 123 */     if (leader != null) {
/* 124 */       BpmLeaderOptionLog optionLog = new BpmLeaderOptionLog();
/* 125 */       optionLog.setTaskId(taskId);
/* 126 */       BpmTask bpmTask = (BpmTask)this.bpmTaskManager.get(taskId);
/* 127 */       if (bpmTask == null) {
/* 128 */         throw new BusinessMessage("任务不存在或已处理");
/*     */       }
/* 130 */       optionLog.setInstId(bpmTask.getInstId());
/* 131 */       optionLog.setLeaderId(leader.getUserId());
/* 132 */       optionLog.setLeaderName(leader.getFullname());
/* 133 */       optionLog.setSecretaryId(secretary.getUserId());
/* 134 */       optionLog.setSecretaryName(secretary.getFullname());
/* 135 */       optionLog.setOption(option);
/* 136 */       optionLog.setType("1");
/* 137 */       this.bpmLeaderOptionLogManager.create(optionLog);
/* 138 */       updateByTaskId(taskId, "1", "1");
/*     */       
/* 140 */       BpmTask task = (BpmTask)this.bpmTaskManager.get(taskId);
/* 141 */       TaskSendLeaderActionHandle.sendNotifyMessageToLeader(task);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveOptionAndUpdateStatusToSecretary(String taskId, String option) {
/* 147 */     IUser leader = ContextUtil.getCurrentUser();
/* 148 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 149 */     IUser secretary = leaderService.getUserByLeaderId(leader.getUserId());
/* 150 */     if (secretary != null) {
/* 151 */       BpmLeaderOptionLog optionLog = new BpmLeaderOptionLog();
/* 152 */       optionLog.setTaskId(taskId);
/* 153 */       BpmTask bpmTask = (BpmTask)this.bpmTaskManager.get(taskId);
/* 154 */       if (bpmTask == null) {
/* 155 */         throw new BusinessMessage("任务不存在或已处理");
/*     */       }
/* 157 */       optionLog.setInstId(bpmTask.getInstId());
/* 158 */       optionLog.setLeaderId(leader.getUserId());
/* 159 */       optionLog.setLeaderName(leader.getFullname());
/* 160 */       optionLog.setSecretaryId(secretary.getUserId());
/* 161 */       optionLog.setSecretaryName(secretary.getFullname());
/* 162 */       optionLog.setOption(option);
/* 163 */       optionLog.setType("2");
/* 164 */       this.bpmLeaderOptionLogManager.create(optionLog);
/* 165 */       updateByTaskId(taskId, "1", "2");
/*     */       
/* 167 */       BpmTask task = (BpmTask)this.bpmTaskManager.get(taskId);
/* 168 */       TaskLeaderSaveActionHandler.sendNotifyMessageToSecretary(task);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByInstId(String instId) {
/* 174 */     this.bpmLeaderTaskLogDao.removeByInstId(instId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/impl/BpmLeaderTaskLogManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */