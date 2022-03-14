/*     */ package com.dstz.bpm.plugin.global.agency.executer;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.TaskSkipType;
/*     */ import com.dstz.bpm.api.constant.TaskStatus;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyConfigManager;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyLogManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
/*     */ import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
/*     */ import com.dstz.bpm.plugin.global.agency.def.TaskAgencyPluginDef;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class TaskAgencyPluginExecutor
/*     */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, TaskAgencyPluginDef>
/*     */ {
/*     */   @Autowired
/*     */   private BpmUserAgencyConfigManager bpmUserAgencyConfigManager;
/*     */   @Autowired
/*     */   private BpmUserAgencyLogManager bpmUserAgencyLogManager;
/*     */   @Autowired
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Autowired
/*     */   private TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Autowired
/*     */   private UserService userService;
/*     */   @Autowired
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public Void execute(BpmExecutionPluginSession pluginSession, TaskAgencyPluginDef pluginDef) {
/*  73 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)Objects.requireNonNull(BpmContext.getActionModel());
/*  74 */     if (Objects.nonNull(model.isHasSkipThisTask()) && !TaskSkipType.NO_SKIP.equals(model.isHasSkipThisTask())) {
/*  75 */       return null;
/*     */     }
/*  77 */     EventType eventType = pluginSession.getEventType();
/*  78 */     if (eventType == EventType.TASK_POST_CREATE_EVENT) {
/*  79 */       postCreate();
/*  80 */     } else if (eventType == EventType.TASK_POST_COMPLETE_EVENT) {
/*  81 */       postComplete();
/*     */     } 
/*     */     
/*  84 */     return null;
/*     */   }
/*     */   
/*     */   private void postComplete() {
/*  88 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/*     */     
/*  90 */     IBpmTask bpmTask = model.getBpmTask();
/*  91 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  92 */     defaultQueryFilter.addFilter("task_id_", bpmTask.getId(), QueryOP.EQUAL);
/*  93 */     List<BpmUserAgencyLog> bpmUserAgencyLogs = this.bpmUserAgencyLogManager.query((QueryFilter)defaultQueryFilter);
/*  94 */     if (CollectionUtil.isNotEmpty(bpmUserAgencyLogs)) {
/*  95 */       BpmUserAgencyLog bpmUserAgencyLog = bpmUserAgencyLogs.get(0);
/*  96 */       BpmUserAgencyConfig bpmUserAgencyConfig = (BpmUserAgencyConfig)this.bpmUserAgencyConfigManager.get(bpmUserAgencyLog.getConfigId());
/*  97 */       if (bpmUserAgencyConfig == null) {
/*     */         return;
/*     */       }
/* 100 */       IUser user = this.userService.getUserById(bpmUserAgencyConfig.getConfigUserId());
/* 101 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 102 */       IUser currentUser = ContextUtil.getCurrentUser();
/* 103 */       bpmTaskOpinion.setApproverName(user.getFullname() + "(" + currentUser.getFullname() + " 代)");
/* 104 */       this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void postCreate() {
/* 109 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/*     */     
/* 111 */     IBpmTask bpmTask = model.getBpmTask();
/* 112 */     if (bpmTask == null) {
/*     */       return;
/*     */     }
/* 115 */     BpmTask bpmTaskUpdate = (BpmTask)this.bpmTaskManager.get(bpmTask.getId());
/* 116 */     if (bpmTaskUpdate == null) {
/*     */       return;
/*     */     }
/* 119 */     IBpmInstance bpmInstance = model.getBpmInstance();
/*     */     
/* 121 */     List<SysIdentity> sysIdentities = model.getBpmIdentity(bpmTask.getNodeId());
/* 122 */     if (CollectionUtil.isEmpty(sysIdentities)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 127 */     List<SysIdentity> identityList = (List<SysIdentity>)sysIdentities.stream().filter(item -> StringUtils.equals("user", item.getType())).collect(Collectors.toList());
/* 128 */     if (CollectionUtil.isEmpty(identityList)) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     if (StringUtils.equals(bpmTaskUpdate.getTaskType(), TaskType.SIGN_SOURCE.getKey())) {
/*     */       
/* 134 */       this.bpmTaskManager.getByParentId(bpmTaskUpdate.getId()).stream().filter(task -> StringUtils.equals(task.getTaskType(), TaskType.SIGN.getKey()))
/*     */         
/* 136 */         .forEach(task -> {
/*     */             List<SysIdentity> identity = new ArrayList<>();
/*     */             TaskIdentityLink taskIdentityLink = this.taskIdentityLinkManager.getByTaskId(task.getId()).get(0);
/*     */             identity.add(new DefaultIdentity(task.getAssigneeId(), task.getAssigneeNames(), "user", taskIdentityLink.getOrgId()));
/*     */             updateTask(identity, bpmInstance, task);
/*     */           });
/*     */     } else {
/* 143 */       updateTask(identityList, bpmInstance, bpmTaskUpdate);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateTask(List<SysIdentity> identityList, IBpmInstance bpmInstance, BpmTask bpmTaskUpdate) {
/* 148 */     Set<SysIdentity> identityUsers = new HashSet<>();
/* 149 */     Set<SysIdentity> nomalUsers = new HashSet<>();
/* 150 */     StringBuilder stringBuilder = new StringBuilder();
/* 151 */     identityList.forEach(identity -> {
/*     */           List<BpmUserAgencyConfig> bpmUserAgencyConfigList = this.bpmUserAgencyConfigManager.selectTakeEffectingList(identity.getId(), new Date());
/*     */ 
/*     */ 
/*     */           
/*     */           if (CollectionUtil.isEmpty(bpmUserAgencyConfigList)) {
/*     */             nomalUsers.add(identity);
/*     */ 
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */ 
/*     */           
/*     */           Optional<BpmUserAgencyConfig> agencyConfig = bpmUserAgencyConfigList.stream().filter(()).findFirst();
/*     */ 
/*     */           
/*     */           Set<SysIdentity> users = new HashSet<>();
/*     */ 
/*     */           
/*     */           agencyConfig.ifPresent(());
/*     */ 
/*     */           
/*     */           if (!agencyConfig.isPresent()) {
/*     */             nomalUsers.add(identity);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 180 */     if (stringBuilder.length() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 184 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(bpmInstance.getDefId());
/* 185 */     if (nomalUsers.size() + identityUsers.size() == 0 && !processDef.getExtProperties().isAllowExecutorEmpty()) {
/* 186 */       throw new BusinessMessage(String.format("%s节点 候选人不能为空, 因原候选人工作委托导致处理人为空", new Object[] { bpmTaskUpdate.getName() }), BpmStatusCode.NO_TASK_USER);
/*     */     }
/*     */     
/* 189 */     if (identityUsers.size() + nomalUsers.size() == 1) {
/* 190 */       if (identityUsers.size() == 1) {
/* 191 */         bpmTaskUpdate.setAssigneeId(((SysIdentity)identityUsers.stream().findFirst().get()).getId());
/*     */         
/* 193 */         bpmTaskUpdate.setStatus(TaskStatus.AGENCY.getKey());
/*     */       } else {
/*     */         
/* 196 */         bpmTaskUpdate.setAssigneeId(((SysIdentity)nomalUsers.stream().findFirst().get()).getId());
/*     */       } 
/*     */     } else {
/* 199 */       bpmTaskUpdate.setAssigneeId("0");
/*     */     } 
/* 201 */     updateTaskAndOpinion(bpmTaskUpdate, identityUsers, nomalUsers);
/* 202 */     writeTaskIdentityLink(bpmTaskUpdate, identityUsers, nomalUsers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateTaskAndOpinion(BpmTask bpmTask, Set<SysIdentity> identityUsers, Set<SysIdentity> nomalUsers) {
/* 211 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 212 */     StringBuilder assignInfoAppend = new StringBuilder();
/* 213 */     StringBuilder assigneeNames = new StringBuilder();
/* 214 */     identityUsers.forEach(user -> {
/*     */           assignInfoAppend.append(user.getType()).append("-").append(user.getName()).append("-").append(user.getId()).append("-").append(user.getOrgId()).append(",");
/*     */           assigneeNames.append(user.getName()).append(",");
/*     */         });
/* 218 */     nomalUsers.forEach(user -> {
/*     */           assignInfoAppend.append(user.getType()).append("-").append(user.getName()).append("-").append(user.getId()).append("-").append(user.getOrgId()).append(",");
/*     */           
/*     */           assigneeNames.append(user.getName()).append(",");
/*     */         });
/* 223 */     bpmTaskOpinion.setAssignInfo(assignInfoAppend.substring(0, assignInfoAppend.length() - 1));
/* 224 */     this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/* 225 */     bpmTask.setAssigneeNames(assigneeNames.substring(0, assigneeNames.length() - 1));
/* 226 */     this.bpmTaskManager.update(bpmTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeTaskIdentityLink(BpmTask bpmTask, Set<SysIdentity> identityUsers, Set<SysIdentity> nomalUsers) {
/* 235 */     this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
/* 236 */     List<SysIdentity> sysIdentityList = new ArrayList<>();
/* 237 */     identityUsers.forEach(user -> sysIdentityList.add(new DefaultIdentity(user.getId(), user.getName(), "user", user.getOrgId())));
/* 238 */     if (sysIdentityList.size() > 0) {
/* 239 */       bpmTask.setTaskType(TaskType.AGENT.getKey());
/* 240 */       this.taskIdentityLinkManager.createIdentityLink((IBpmTask)bpmTask, sysIdentityList);
/*     */     } 
/* 242 */     sysIdentityList.clear();
/* 243 */     nomalUsers.forEach(user -> sysIdentityList.add(new DefaultIdentity(user.getId(), user.getName(), "user", user.getOrgId())));
/* 244 */     if (sysIdentityList.size() > 0) {
/* 245 */       bpmTask.setTaskType(TaskType.NORMAL.getKey());
/* 246 */       this.taskIdentityLinkManager.createIdentityLink((IBpmTask)bpmTask, sysIdentityList);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeUserAgencyLog(BpmUserAgencyConfig bpmUserAgencyConfig, BpmTask bpmTask) {
/* 258 */     BpmUserAgencyLog bpmUserAgencyLog = new BpmUserAgencyLog();
/* 259 */     bpmUserAgencyLog.setConfigId(bpmUserAgencyConfig.getId());
/* 260 */     bpmUserAgencyLog.setFlowInstanceId(bpmTask.getInstId());
/* 261 */     bpmUserAgencyLog.setTaskId(bpmTask.getId());
/* 262 */     bpmUserAgencyLog.setTaskNodeId(bpmTask.getNodeId());
/* 263 */     bpmUserAgencyLog.setTaskNodeName(bpmTask.getName());
/* 264 */     this.bpmUserAgencyLogManager.insertSelective(bpmUserAgencyLog);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/agency/executer/TaskAgencyPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */