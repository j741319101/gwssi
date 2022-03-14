/*     */ package com.dstz.bpm.plugin.node.sign.executer;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.TaskSkipType;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.cmd.TaskCommand;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
/*     */ import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class SignTaskPluginExecuter
/*     */   extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, SignTaskPluginDef>
/*     */ {
/*     */   @Autowired
/*     */   private TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   @Autowired
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Autowired
/*     */   private UserService userService;
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Autowired
/*     */   private TaskCommand taskCommand;
/*     */   @Resource
/*     */   private SuperviseTaskExecuter superviseTaskExecuter;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public Void execute(DefaultBpmTaskPluginSession pluginSession, SignTaskPluginDef pluginDef) {
/*  63 */     if (pluginSession.getEventType() == EventType.TASK_PRE_COMPLETE_EVENT) {
/*  64 */       preTaskComplete(pluginSession);
/*  65 */     } else if (pluginSession.getEventType() == EventType.TASK_POST_CREATE_EVENT) {
/*  66 */       postCreateTask(pluginSession, pluginDef);
/*  67 */     } else if (pluginSession.getEventType() == EventType.TASK_CREATE_EVENT) {
/*  68 */       createTask(pluginSession);
/*     */     } 
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void preTaskComplete(DefaultBpmTaskPluginSession pluginSession) {
/*  79 */     this.superviseTaskExecuter.taskComplete(pluginSession);
/*  80 */     List<String> typeSupervise = this.superviseTaskExecuter.preTaskComplete(pluginSession);
/*  81 */     if (CollectionUtil.isNotEmpty(typeSupervise) && typeSupervise.contains("sign")) {
/*  82 */       BaseActionCmd model = (BaseActionCmd)BpmContext.getActionModel();
/*  83 */       BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
/*  84 */       String[] destinations = model.getDestinations();
/*  85 */       if (destinations != null && destinations.length > 0 && 
/*  86 */         !Arrays.<String>asList(destinations).contains(bpmTask.getNodeId())) {
/*  87 */         String[] newDestinations = new String[destinations.length + 1];
/*  88 */         newDestinations[0] = bpmTask.getNodeId();
/*  89 */         for (int i = 0; i < destinations.length; i++) {
/*  90 */           newDestinations[i + 1] = destinations[i];
/*     */         }
/*  92 */         model.setDestinations(newDestinations);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createTask(DefaultBpmTaskPluginSession pluginSession) {
/* 104 */     BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
/* 105 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/* 106 */     SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)((SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class)).getBpmPluginDef();
/* 107 */     if (signTaskPluginDef.isSignMultiTask()) {
/* 108 */       this.superviseTaskExecuter.createTask(pluginSession, "sign");
/*     */     }
/*     */   }
/*     */   
/*     */   private void postCreateTask(DefaultBpmTaskPluginSession pluginSession, SignTaskPluginDef pluginDef) {
/* 113 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/*     */     
/* 115 */     BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
/* 116 */     if (!pluginDef.isSignMultiTask() || StringUtils.equals(bpmTask.getTaskType(), "SIGN")) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 121 */     this.taskCommand.execute(EventType.TASK_SIGN_CREATE_EVENT, model);
/*     */     
/* 123 */     if (model.isHasSkipThisTask() != null && model.isHasSkipThisTask() != TaskSkipType.NO_SKIP) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     bpmTask.setTaskType(TaskType.SIGN_SOURCE.getKey());
/* 128 */     bpmTask.setAssigneeId(null);
/* 129 */     bpmTask.setAssigneeNames(null);
/* 130 */     this.bpmTaskManager.update(bpmTask);
/* 131 */     BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getTaskId());
/* 132 */     opinion.setAssignInfo("所有会签用户");
/*     */ 
/*     */     
/* 135 */     this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
/*     */ 
/*     */     
/* 138 */     List<SysIdentity> sysIdentities = extractBpmIdentity();
/* 139 */     JSON bpmTaskJson = (JSON)JSON.toJSON(bpmTask);
/* 140 */     for (SysIdentity sysIdentity : sysIdentities) {
/* 141 */       BpmTask task = (BpmTask)JSON.toJavaObject(bpmTaskJson, BpmTask.class);
/* 142 */       task.setId(IdUtil.getSuid());
/* 143 */       task.setTaskType(TaskType.SIGN.getKey());
/* 144 */       task.setStatus(bpmTask.getStatus());
/* 145 */       task.setParentId(bpmTask.getId());
/*     */       
/* 147 */       task.setAssigneeId(sysIdentity.getId());
/* 148 */       task.setAssigneeNames(sysIdentity.getName());
/*     */       
/* 150 */       List<SysIdentity> identityList = new ArrayList<>();
/* 151 */       identityList.add(sysIdentity);
/*     */ 
/*     */       
/* 154 */       this.bpmTaskOpinionManager.createOpinion((IBpmTask)task, pluginSession.getBpmInstance(), identityList, model.getOpinion(), model.getActionName(), model
/* 155 */           .getFormId(), bpmTask.getId(), opinion.getTrace(), opinion.getVersion());
/* 156 */       this.taskIdentityLinkManager.createIdentityLink((IBpmTask)task, identityList);
/* 157 */       this.bpmTaskManager.create(task);
/*     */     } 
/*     */     
/* 160 */     opinion.setCreateTime(new Date((new Date()).getTime() + 1000L));
/* 161 */     opinion.setTrace("");
/* 162 */     this.bpmTaskOpinionManager.update(opinion);
/*     */ 
/*     */     
/* 165 */     this.taskCommand.execute(EventType.TASK_SIGN_POST_CREATE_EVENT, model);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<SysIdentity> extractBpmIdentity() {
/* 176 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/* 177 */     List<SysIdentity> results = new ArrayList<>();
/* 178 */     List<SysIdentity> identities = model.getBpmIdentity(model.getNodeId());
/* 179 */     if (identities != null) {
/* 180 */       for (SysIdentity bpmIdentity : identities) {
/* 181 */         if ("user".equals(bpmIdentity.getType())) {
/* 182 */           results.add(bpmIdentity); continue;
/*     */         } 
/* 184 */         List<IUser> users = this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
/* 185 */         for (IUser user : users) {
/* 186 */           results.add(new DefaultIdentity(user));
/*     */         }
/*     */       } 
/*     */     }
/* 190 */     return results;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/sign/executer/SignTaskPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */