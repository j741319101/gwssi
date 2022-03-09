/*     */ package com.dstz.bpm.plugin.global.agency.executer;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.core.util.StringUtil;
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
/*     */ import com.dstz.bpm.engine.model.BpmIdentity;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyConfigManager;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyLogManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
/*     */ import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
/*     */ import com.dstz.bpm.plugin.global.agency.def.TaskAgencyPluginDef;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  73 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/*     */     
/*  75 */     IBpmTask bpmTask = model.getBpmTask();
/*  76 */     BpmTask bpmTaskUpdate = (BpmTask)this.bpmTaskManager.get(bpmTask.getId());
/*  77 */     IBpmInstance bpmInstance = model.getBpmInstance();
/*  78 */     List<SysIdentity> sysIdentities = model.getBpmIdentity(bpmTask.getNodeId());
/*  79 */     if (CollectionUtil.isEmpty(sysIdentities)) {
/*  80 */       return null;
/*     */     }
/*     */     
/*  83 */     List<SysIdentity> identityList = (List<SysIdentity>)sysIdentities.stream().filter(item -> StringUtils.equals("user", item.getType())).collect(Collectors.toList());
/*  84 */     if (CollectionUtil.isEmpty(identityList)) {
/*  85 */       return null;
/*     */     }
/*     */     
/*  88 */     identityList = (List<SysIdentity>)identityList.stream().filter(item -> StringUtils.equals("user", item.getType())).collect(Collectors.toList());
/*  89 */     if (CollectionUtil.isEmpty(identityList)) {
/*  90 */       return null;
/*     */     }
/*  92 */     if (StringUtils.equals(bpmTaskUpdate.getTaskType(), TaskType.SIGN_SOURCE.getKey())) {
/*  93 */       this.bpmTaskManager.getByInstId(bpmTaskUpdate.getInstId()).stream().filter(task -> StringUtils.equals(task.getTaskType(), TaskType.SIGN.getKey()))
/*     */         
/*  95 */         .forEach(task -> {
/*     */             List<SysIdentity> identity = new ArrayList<>();
/*     */             identity.add(new BpmIdentity(task.getAssigneeId(), task.getAssigneeNames(), "user"));
/*     */             updateTask(identity, bpmInstance, task);
/*     */           });
/*     */     } else {
/* 101 */       updateTask(identityList, bpmInstance, bpmTaskUpdate);
/*     */     } 
/* 103 */     return null;
/*     */   }
/*     */   
/*     */   private void updateTask(List<SysIdentity> identityList, IBpmInstance bpmInstance, BpmTask bpmTaskUpdate) {
/* 107 */     Set<SysIdentity> identityUsers = new HashSet<>();
/* 108 */     Set<SysIdentity> nomalUsers = new HashSet<>();
/* 109 */     StringBuilder stringBuilder = new StringBuilder();
/* 110 */     identityList.forEach(identity -> {
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
/* 140 */     if (stringBuilder.length() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 144 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(bpmInstance.getDefId());
/* 145 */     if (nomalUsers.size() + identityUsers.size() == 0 && !processDef.getExtProperties().isAllowExecutorEmpty()) {
/* 146 */       throw new BusinessMessage(String.format("%s节点 候选人不能为空, 因原候选人工作委托导致处理人为空", new Object[] { bpmTaskUpdate.getName() }), BpmStatusCode.NO_TASK_USER);
/*     */     }
/*     */     
/* 149 */     if (identityUsers.size() + nomalUsers.size() == 1) {
/* 150 */       if (identityUsers.size() == 1) {
/* 151 */         bpmTaskUpdate.setAssigneeId(((SysIdentity)identityUsers.stream().findFirst().get()).getId());
/* 152 */         if (StringUtils.equals(TaskType.SIGN.getKey(), bpmTaskUpdate.getTaskType())) {
/* 153 */           bpmTaskUpdate.setStatus(TaskStatus.AGENCY.getKey());
/*     */         } else {
/* 155 */           bpmTaskUpdate.setTaskType(TaskType.AGENT.getKey());
/*     */         } 
/*     */       } else {
/*     */         
/* 159 */         bpmTaskUpdate.setAssigneeId(((SysIdentity)nomalUsers.stream().findFirst().get()).getId());
/*     */       } 
/*     */     } else {
/* 162 */       bpmTaskUpdate.setAssigneeId("0");
/*     */     } 
/* 164 */     updateTaskAndOpinion(bpmTaskUpdate, identityUsers, nomalUsers);
/* 165 */     writeTaskIdentityLink(bpmTaskUpdate, identityUsers, nomalUsers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateTaskAndOpinion(BpmTask bpmTask, Set<SysIdentity> identityUsers, Set<SysIdentity> nomalUsers) {
/* 174 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 175 */     StringBuilder assignInfoAppend = new StringBuilder();
/* 176 */     identityUsers.forEach(user -> assignInfoAppend.append(user.getName()).append(";"));
/*     */ 
/*     */     
/* 179 */     nomalUsers.forEach(user -> assignInfoAppend.append(user.getName()).append(";"));
/*     */ 
/*     */ 
/*     */     
/* 183 */     bpmTaskOpinion.setAssignInfo(assignInfoAppend.substring(0, assignInfoAppend.length() - 1));
/* 184 */     this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/* 185 */     bpmTask.setAssigneeNames(assignInfoAppend.substring(0, assignInfoAppend.length() - 1));
/* 186 */     this.bpmTaskManager.update(bpmTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeTaskIdentityLink(BpmTask bpmTask, Set<SysIdentity> identityUsers, Set<SysIdentity> nomalUsers) {
/* 195 */     this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
/* 196 */     List<SysIdentity> sysIdentityList = new ArrayList<>();
/* 197 */     identityUsers.forEach(user -> sysIdentityList.add(new BpmIdentity(user.getId(), user.getName(), "user")));
/* 198 */     if (sysIdentityList.size() > 0) {
/* 199 */       bpmTask.setTaskType(TaskType.AGENT.getKey());
/* 200 */       this.taskIdentityLinkManager.createIdentityLink((IBpmTask)bpmTask, sysIdentityList);
/*     */     } 
/* 202 */     sysIdentityList.clear();
/* 203 */     nomalUsers.forEach(user -> sysIdentityList.add(new BpmIdentity(user.getId(), user.getName(), "user")));
/* 204 */     if (sysIdentityList.size() > 0) {
/* 205 */       bpmTask.setTaskType(TaskType.NORMAL.getKey());
/* 206 */       this.taskIdentityLinkManager.createIdentityLink((IBpmTask)bpmTask, sysIdentityList);
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
/* 218 */     BpmUserAgencyLog bpmUserAgencyLog = new BpmUserAgencyLog();
/* 219 */     bpmUserAgencyLog.setConfigId(bpmUserAgencyConfig.getId());
/* 220 */     bpmUserAgencyLog.setFlowInstanceId(bpmTask.getInstId());
/* 221 */     bpmUserAgencyLog.setTaskId(bpmTask.getId());
/* 222 */     bpmUserAgencyLog.setTaskNodeId(bpmTask.getNodeId());
/* 223 */     bpmUserAgencyLog.setTaskNodeName(bpmTask.getName());
/* 224 */     this.bpmUserAgencyLogManager.insertSelective(bpmUserAgencyLog);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/agency/executer/TaskAgencyPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */