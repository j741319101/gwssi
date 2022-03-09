/*     */ package com.dstz.bpm.engine.action.handler.task;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.TaskStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.bean.BeanUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */
import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class TaskTurnActionHandler
/*     */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*     */ {
/*     */   @Resource
/*     */ BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   
/*     */   public ActionType getActionType() {
/*  40 */     return ActionType.TURN;
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(DefualtTaskActionCmd model) {
/*  45 */     prepareActionDatas(model);
/*  46 */     checkFlowIsValid((BaseActionCmd)model);
/*     */     
/*  48 */     BpmTask task = (BpmTask)model.getBpmTask();
/*     */     
/*  50 */     List<SysIdentity> userSetting = model.getBpmIdentity(task.getNodeId());
/*     */     
/*  52 */     if (BeanUtil.isEmpty(userSetting)) {
/*  53 */       throw new BusinessException(BpmStatusCode.NO_ASSIGN_USER);
/*     */     }
/*     */     
/*  56 */     IUser user = ContextUtil.getCurrentUser();
/*  57 */     TaskIdentityLink taskIdentityLink = model.getTaskIdentityLink();
/*  58 */     if (StringUtils.equals(task.getAssigneeId(), "0") && 
/*  59 */       StringUtils.indexOf(task.getAssigneeNames(), "user") != -1) {
/*  60 */       TaskIdentityLink identityLink = this.taskIdentityLinkManager.getByTaskIdAndUserId(task.getId(), user.getUserId());
/*     */       
/*  62 */       identityLink.setIdentity(((SysIdentity)userSetting.get(0)).getId());
/*  63 */       identityLink.setIdentityName(((SysIdentity)userSetting.get(0)).getName());
/*  64 */       identityLink.setPermissionCode(String.format("%s-user", new Object[] { ((SysIdentity)userSetting.get(0)).getId() }));
/*  65 */       identityLink.setOrgId(((SysIdentity)userSetting.get(0)).getOrgId());
/*  66 */       this.taskIdentityLinkManager.update(identityLink);
/*  67 */       task.setAssigneeNames(task.getAssigneeNames().replace(
/*  68 */             String.format("user-%s-%s", new Object[] { user.getFullname(), user.getUserId()
/*  69 */               }), String.format("user-%s-%s", new Object[] { ((SysIdentity)userSetting.get(0)).getName(), ((SysIdentity)userSetting.get(0)).getId() })));
/*     */     } else {
/*     */       
/*  72 */       task.setAssigneeId(((SysIdentity)userSetting.get(0)).getId());
/*  73 */       task.setAssigneeNames(((SysIdentity)userSetting.get(0)).getName());
/*     */ 
/*     */       
/*  76 */       this.taskIdentityLinkManager.removeByTaskId(task.getId());
/*  77 */       List<SysIdentity> identitys = new ArrayList<>();
/*  78 */       DefaultIdentity defaultIdentity = new DefaultIdentity();
/*  79 */       defaultIdentity.setId(((SysIdentity)userSetting.get(0)).getId());
/*  80 */       defaultIdentity.setName(((SysIdentity)userSetting.get(0)).getName());
/*  81 */       defaultIdentity.setType("user");
/*  82 */       identitys.add(defaultIdentity);
/*  83 */       this.taskIdentityLinkManager.createIdentityLink((IBpmTask)task, identitys);
/*     */     } 
/*  85 */     task.setStatus(TaskStatus.TURN.getKey());
/*  86 */     this.taskManager.update(task);
/*  87 */     handelTaskOpinion(task, taskIdentityLink, model.getOpinion());
/*     */ 
/*     */     
/*  90 */     Map<String, IExtendTaskAction> leaderTaskAction = AppUtil.getImplInstance(IExtendTaskAction.class);
/*  91 */     leaderTaskAction.forEach((key, action) -> action.turnLeaderTask((IBpmTask)task));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handelTaskOpinion(BpmTask task, TaskIdentityLink taskIdentityLink, String taskOpinion) {
/*  99 */     BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(task.getId());
/* 100 */     String opinionId = opinion.getId();
/* 101 */     String assignInfo = opinion.getAssignInfo();
/* 102 */     opinion.setId(IdUtil.getSuid());
/* 103 */     StringBuilder sb = new StringBuilder();
/* 104 */     sb.append("user").append("-").append(task.getAssigneeNames()).append("-")
/* 105 */       .append(task.getAssigneeId()).append("-").append(taskIdentityLink.getOrgId()).append(",");
/* 106 */     opinion.setAssignInfo(sb.toString());
/* 107 */     opinion.setTaskOrgId(taskIdentityLink.getOrgId());
/* 108 */     this.bpmTaskOpinionManager.create(opinion);
/*     */     
/* 110 */     opinion.setId(opinionId);
/* 111 */     opinion.setAssignInfo(assignInfo);
/*     */     
/* 113 */     opinion.setTaskId("-");
/* 114 */     opinion.setOpinion(taskOpinion);
/* 115 */     opinion.setStatus(ActionType.TURN.getKey());
/* 116 */     opinion.setApproveTime(new Date());
/* 117 */     opinion.setApprover(ContextUtil.getCurrentUserId());
/* 118 */     opinion.setApproverName(ContextUtil.getCurrentUser().getFullname());
/* 119 */     opinion.setDurMs(Long.valueOf(opinion.getApproveTime().getTime() - opinion.getCreateTime().getTime()));
/* 120 */     this.bpmTaskOpinionManager.update(opinion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 136 */     return 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 141 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 146 */     return "/bpm/task/taskTrunActionDialog.html";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/task/TaskTurnActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */