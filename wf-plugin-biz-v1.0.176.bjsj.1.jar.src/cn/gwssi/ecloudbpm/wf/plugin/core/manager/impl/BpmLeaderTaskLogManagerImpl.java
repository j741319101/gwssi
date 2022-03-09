/*     */ package cn.gwssi.ecloudbpm.wf.plugin.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskApprove;
/*     */ import cn.gwssi.ecloudbpm.wf.core.vo.BpmTaskVO;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.dao.BpmLeaderTaskLogDao;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmLeaderOptionLogManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmLeaderTaskLogManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmReminderLogManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmLeaderOptionLog;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmLeaderTaskLog;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmReminderLog;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.service.LeaderService;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("BpmLeaderTaskLogManager")
/*     */ public class BpmLeaderTaskLogManagerImpl
/*     */   extends BaseManager<String, BpmLeaderTaskLog>
/*     */   implements BpmLeaderTaskLogManager
/*     */ {
/*     */   @Resource
/*     */   private BpmLeaderTaskLogDao bpmLeaderTaskLogDao;
/*     */   @Resource
/*     */   BpmReminderLogManager bpmReminderLogManager;
/*     */   @Resource
/*     */   BpmLeaderOptionLogManager bpmLeaderOptionLogManager;
/*     */   @Resource
/*     */   BpmTaskManager bpmTaskManager;
/*     */   
/*     */   public Integer updateByTaskId(String taskId, String type, String flag) {
/*  51 */     BpmLeaderTaskLog taskLog = new BpmLeaderTaskLog();
/*  52 */     taskLog.setTaskId(taskId);
/*  53 */     taskLog.setFlag(flag);
/*  54 */     taskLog.setType(type);
/*  55 */     return this.bpmLeaderTaskLogDao.updateByTaskId(taskLog);
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmLeaderTaskLog getByTaskId(String taskId) {
/*  60 */     List<BpmLeaderTaskLog> bpmLeaderTaskLogs = this.bpmLeaderTaskLogDao.getByTaskId(taskId);
/*  61 */     if (CollectionUtils.isNotEmpty(bpmLeaderTaskLogs)) {
/*  62 */       return bpmLeaderTaskLogs.get(0);
/*     */     }
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskVO> getTodoList(QueryFilter queryFilter) {
/*  69 */     IUser user = ContextUtil.getCurrentUser();
/*  70 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/*  71 */     if (leaderService.isLeaderByLeaderId(user.getUserId())) {
/*  72 */       queryFilter.addFilter("task.ASSIGNEE_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/*  73 */       queryFilter.addFilter("LEADER.FLAG_", "1", QueryOP.EQUAL);
/*     */     } else {
/*  75 */       IUser leader = leaderService.getUserBySecretaryId(user.getUserId());
/*  76 */       if (leader == null) {
/*  77 */         return new ArrayList<>();
/*     */       }
/*  79 */       queryFilter.addFilter("task.ASSIGNEE_ID_", leader.getUserId(), QueryOP.EQUAL);
/*     */     } 
/*     */     
/*  82 */     List<BpmTaskVO> taskVOS = this.bpmLeaderTaskLogDao.getTodoList(queryFilter);
/*  83 */     taskVOS.forEach(task -> {
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
/* 108 */     return taskVOS;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskApprove> getApproveHistoryList(QueryFilter queryFilter) {
/* 113 */     IUser user = ContextUtil.getCurrentUser();
/* 114 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 115 */     if (leaderService.isLeaderByLeaderId(user.getUserId())) {
/* 116 */       queryFilter.addFilter("o.approver_", user.getUserId(), QueryOP.EQUAL);
/* 117 */       queryFilter.addFilter("LEADER.FLAG_", "1", QueryOP.EQUAL);
/*     */     } else {
/* 119 */       IUser leader = leaderService.getUserBySecretaryId(user.getUserId());
/* 120 */       if (leader == null) {
/* 121 */         return new ArrayList<>();
/*     */       }
/* 123 */       queryFilter.addFilter("o.approver_", leader.getUserId(), QueryOP.EQUAL);
/*     */     } 
/*     */     
/* 126 */     return this.bpmLeaderTaskLogDao.getApproveHistoryList(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter queryFilter) {
/* 131 */     IUser user = ContextUtil.getCurrentUser();
/* 132 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 133 */     if (leaderService.isLeaderByLeaderId(user.getUserId())) {
/* 134 */       queryFilter.addFilter("a.receive_user_id", user.getUserId(), QueryOP.EQUAL);
/*     */     } else {
/* 136 */       IUser leader = leaderService.getUserBySecretaryId(user.getUserId());
/* 137 */       if (leader == null) {
/* 138 */         return new ArrayList<>();
/*     */       }
/* 140 */       queryFilter.addFilter("a.receive_user_id", leader.getUserId(), QueryOP.EQUAL);
/*     */     } 
/*     */     
/* 143 */     return this.bpmLeaderTaskLogDao.listUserReceive(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveOptionAndUpdateStatusToLeader(String taskId, String option) {
/* 148 */     IUser secretary = ContextUtil.getCurrentUser();
/* 149 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 150 */     IUser leader = leaderService.getUserBySecretaryId(secretary.getUserId());
/* 151 */     if (leader != null) {
/* 152 */       BpmLeaderOptionLog optionLog = new BpmLeaderOptionLog();
/* 153 */       optionLog.setTaskId(taskId);
/* 154 */       BpmTask bpmTask = (BpmTask)this.bpmTaskManager.get(taskId);
/* 155 */       if (bpmTask == null) {
/* 156 */         throw new BusinessMessage("任务不存在或已处理");
/*     */       }
/* 158 */       optionLog.setInstId(bpmTask.getInstId());
/* 159 */       optionLog.setLeaderId(leader.getUserId());
/* 160 */       optionLog.setLeaderName(leader.getFullname());
/* 161 */       optionLog.setSecretaryId(secretary.getUserId());
/* 162 */       optionLog.setSecretaryName(secretary.getFullname());
/* 163 */       optionLog.setOption(option);
/* 164 */       optionLog.setType("1");
/* 165 */       this.bpmLeaderOptionLogManager.create(optionLog);
/* 166 */       updateByTaskId(taskId, "1", "1");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveOptionAndUpdateStatusToSecretary(String taskId, String option) {
/* 172 */     IUser leader = ContextUtil.getCurrentUser();
/* 173 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 174 */     IUser secretary = leaderService.getUserByLeaderId(leader.getUserId());
/* 175 */     if (secretary != null) {
/* 176 */       BpmLeaderOptionLog optionLog = new BpmLeaderOptionLog();
/* 177 */       optionLog.setTaskId(taskId);
/* 178 */       BpmTask bpmTask = (BpmTask)this.bpmTaskManager.get(taskId);
/* 179 */       if (bpmTask == null) {
/* 180 */         throw new BusinessMessage("任务不存在或已处理");
/*     */       }
/* 182 */       optionLog.setInstId(bpmTask.getInstId());
/* 183 */       optionLog.setLeaderId(leader.getUserId());
/* 184 */       optionLog.setLeaderName(leader.getFullname());
/* 185 */       optionLog.setSecretaryId(secretary.getUserId());
/* 186 */       optionLog.setSecretaryName(secretary.getFullname());
/* 187 */       optionLog.setOption(option);
/* 188 */       optionLog.setType("2");
/* 189 */       this.bpmLeaderOptionLogManager.create(optionLog);
/* 190 */       updateByTaskId(taskId, "1", "2");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByInstId(String instId) {
/* 196 */     this.bpmLeaderTaskLogDao.removeByInstId(instId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/impl/BpmLeaderTaskLogManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */