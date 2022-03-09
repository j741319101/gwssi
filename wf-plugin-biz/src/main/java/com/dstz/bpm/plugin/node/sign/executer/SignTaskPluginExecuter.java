/*     */ package com.dstz.bpm.plugin.node.sign.executer;
/*     */ 
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.cmd.TaskCommand;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.engine.model.BpmIdentity;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
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
/*     */   
/*     */   public Void execute(DefaultBpmTaskPluginSession pluginSession, SignTaskPluginDef pluginDef) {
/*  49 */     DefaultBpmTaskPluginSession taskSession = pluginSession;
/*  50 */     if (!pluginDef.isSignMultiTask()) {
/*  51 */       return null;
/*     */     }
/*     */     
/*  54 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/*  55 */     this.taskCommand.execute(EventType.TASK_SIGN_CREATE_EVENT, model);
/*     */ 
/*     */     
/*  58 */     BpmTask bpmTask = (BpmTask)taskSession.getBpmTask();
/*  59 */     bpmTask.setTaskType(TaskType.SIGN_SOURCE.getKey());
/*  60 */     bpmTask.setAssigneeId(null);
/*  61 */     bpmTask.setAssigneeNames(null);
/*  62 */     this.bpmTaskManager.update(bpmTask);
/*  63 */     BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getTaskId());
/*  64 */     opinion.setAssignInfo("所有会签用户");
/*     */ 
/*     */     
/*  67 */     this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
/*     */ 
/*     */     
/*  70 */     List<SysIdentity> sysIdentities = extractBpmIdentity();
/*  71 */     JSON bpmTaskJson = (JSON)JSON.toJSON(bpmTask);
/*  72 */     for (SysIdentity sysIdentity : sysIdentities) {
/*  73 */       BpmTask task = (BpmTask)JSON.toJavaObject(bpmTaskJson, BpmTask.class);
/*  74 */       task.setId(IdUtil.getSuid());
/*  75 */       task.setTaskType(TaskType.SIGN.getKey());
/*  76 */       task.setStatus(bpmTask.getStatus());
/*  77 */       task.setParentId(bpmTask.getId());
/*     */       
/*  79 */       task.setAssigneeId(sysIdentity.getId());
/*  80 */       task.setAssigneeNames(sysIdentity.getName());
/*     */       
/*  82 */       List<SysIdentity> identityList = new ArrayList<>();
/*  83 */       identityList.add(sysIdentity);
/*     */ 
/*     */       
/*  86 */       this.bpmTaskOpinionManager.createOpinion((IBpmTask)task, pluginSession.getBpmInstance(), identityList, model.getOpinion(), model.getActionName(), model
/*  87 */           .getFormId(), bpmTask.getId(), opinion.getTrace(), opinion.getVersion());
/*  88 */       this.taskIdentityLinkManager.createIdentityLink((IBpmTask)task, identityList);
/*  89 */       this.bpmTaskManager.create(task);
/*     */     } 
/*     */     
/*  92 */     opinion.setCreateTime(new Date((new Date()).getTime() + 1000L));
/*  93 */     opinion.setTrace("");
/*  94 */     this.bpmTaskOpinionManager.update(opinion);
/*     */ 
/*     */     
/*  97 */     this.taskCommand.execute(EventType.TASK_SIGN_POST_CREATE_EVENT, model);
/*  98 */     return null;
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
/* 109 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/* 110 */     List<SysIdentity> results = new ArrayList<>();
/* 111 */     List<SysIdentity> identities = model.getBpmIdentity(model.getNodeId());
/* 112 */     if (identities != null) {
/* 113 */       for (SysIdentity bpmIdentity : identities) {
/* 114 */         if ("user".equals(bpmIdentity.getType())) {
/* 115 */           results.add(bpmIdentity); continue;
/*     */         } 
/* 117 */         List<IUser> users = this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
/* 118 */         for (IUser user : users) {
/* 119 */           results.add(new BpmIdentity(user));
/*     */         }
/*     */       } 
/*     */     }
/* 123 */     return results;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/sign/executer/SignTaskPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */