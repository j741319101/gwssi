/*     */ package com.dstz.bpm.plugin.node.sign.handler;
/*     */ 
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.OpinionStatus;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
/*     */ import com.dstz.bpm.engine.action.handler.task.TaskRejectActionHandler;
/*     */ import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.sign.def.VoteType;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component("taskSignAgreeActionHandler")
/*     */ public class TaskSignAgreeActionHandler
/*     */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*     */ {
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource(name = "taskRejectActionHandler")
/*     */   private TaskRejectActionHandler taskRejectActionHandler;
/*     */   
/*     */   public ActionType getActionType() {
/*  40 */     return ActionType.SIGNAGREE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doAction(DefualtTaskActionCmd actionModel) {
/*  46 */     updateOpinionStatus(actionModel);
/*     */ 
/*     */ 
/*     */     
/*  50 */     SignTaskPluginDef pluginDef = getPluginDef(actionModel);
/*     */     
/*  52 */     BpmTask signTask = (BpmTask)actionModel.getBpmTask();
/*  53 */     List<BpmTask> tasks = this.taskManager.getSignTaskBySignSourceId(signTask.getTaskId());
/*  54 */     BpmTask mainTask = null;
/*  55 */     List<BpmTask> subTasks = new ArrayList<>();
/*  56 */     for (BpmTask task : tasks) {
/*  57 */       if (TaskType.SIGN_SOURCE.equalsWithKey(task.getTaskType())) {
/*  58 */         mainTask = task; continue;
/*     */       } 
/*  60 */       subTasks.add(task);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  65 */     StringBuilder mainApproverName = new StringBuilder();
/*  66 */     boolean hanldeMainTask = isNeedHandleMainTask(actionModel, pluginDef, subTasks, mainTask, mainApproverName);
/*  67 */     if (hanldeMainTask) {
/*     */       
/*  69 */       IBpmTask bpmTask = actionModel.getBpmTask();
/*  70 */       actionModel.setBpmTask((IBpmTask)mainTask);
/*     */       
/*  72 */       if (ActionType.SIGNOPPOSE.getKey().equals(actionModel.getActionName()) && ActionType.REJECT.getKey().equals(pluginDef.getOpposedAction())) {
/*  73 */         this.LOG.trace("会签任务处理主任务=====》走驳回逻辑");
/*  74 */         this.taskRejectActionHandler.doActionBefore(actionModel);
/*  75 */         this.taskRejectActionHandler.doAction(actionModel);
/*  76 */         this.taskRejectActionHandler.doActionAfter(actionModel);
/*     */       } else {
/*  78 */         this.LOG.trace("会签任务处理主任务=====》调用普通任务处理逻辑");
/*  79 */         super.doAction(actionModel);
/*     */       } 
/*  81 */       actionModel.setBpmTask(bpmTask);
/*  82 */       BpmTaskOpinion mainOpinion = this.bpmTaskOpinionManager.getByTaskId(mainTask.getTaskId());
/*  83 */       mainOpinion.setApproverName(mainApproverName.toString());
/*  84 */       mainOpinion.setApprover(null);
/*  85 */       this.bpmTaskOpinionManager.update(mainOpinion);
/*     */     } 
/*     */ 
/*     */     
/*  89 */     recycleTask(actionModel, hanldeMainTask, mainTask, subTasks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {
/*  99 */     taskComplatePrePluginExecute(actionModel);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 104 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 109 */     return "/bpm/task/taskOpinionDialog.html";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateOpinionStatus(DefualtTaskActionCmd actionModel) {
/* 120 */     OpinionStatus opinionStatus = OpinionStatus.getByActionName(actionModel.getActionName());
/* 121 */     this.LOG.trace("会签任务处理当前任务=====》更新任务意见状态");
/*     */     
/* 123 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getTaskId());
/* 124 */     if (bpmTaskOpinion == null) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     bpmTaskOpinion.setStatus(opinionStatus.getKey());
/* 129 */     bpmTaskOpinion.setApproveTime(new Date());
/*     */     
/* 131 */     bpmTaskOpinion.setDurMs(Long.valueOf(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime()));
/* 132 */     bpmTaskOpinion.setOpinion(actionModel.getOpinion());
/*     */     
/* 134 */     IUser user = ContextUtil.getCurrentUser();
/* 135 */     if (user != null) {
/* 136 */       bpmTaskOpinion.setApprover(user.getUserId());
/* 137 */       bpmTaskOpinion.setApproverName(user.getFullname());
/*     */     } 
/*     */     
/* 140 */     this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SignTaskPluginDef getPluginDef(DefualtTaskActionCmd actionModel) {
/* 152 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(actionModel.getDefId(), actionModel.getNodeId());
/* 153 */     SignTaskPluginContext pluginContext = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
/* 154 */     return (SignTaskPluginDef)pluginContext.getBpmPluginDef();
/*     */   }
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isNeedHandleMainTask(DefualtTaskActionCmd actionModel, SignTaskPluginDef pluginDef, List<BpmTask> subTasks, BpmTask mainTask, StringBuilder mainApproverName) {
/* 170 */     int remainAmount = subTasks.size() - 1;
/*     */     
/* 172 */     if (pluginDef.isNeedAllSign() && remainAmount != 0) {
/* 173 */       return false;
/*     */     }
/*     */     
/* 176 */     List<BpmTaskOpinion> opinions = this.bpmTaskOpinionManager.getByInstAndSignId(actionModel.getInstanceId(), mainTask.getId());
/* 177 */     int passedVoteAmount = 0;
/* 178 */     StringBuilder passedMsg = new StringBuilder("");
/* 179 */     StringBuilder notPassedMsg = new StringBuilder("");
/* 180 */     for (BpmTaskOpinion opinion : opinions) {
/* 181 */       if (OpinionStatus.fromKey(opinion.getStatus()) == OpinionStatus.SIGN_NOT_PASSED) {
/* 182 */         if (notPassedMsg.length() != 0) {
/* 183 */           notPassedMsg.append(",");
/*     */         }
/* 185 */         if (mainApproverName.length() != 0) {
/* 186 */           mainApproverName.append(",");
/*     */         }
/* 188 */         notPassedMsg.append(opinion.getApproverName());
/* 189 */         mainApproverName.append(opinion.getApproverName());
/*     */       } 
/* 191 */       if (OpinionStatus.fromKey(opinion.getStatus()) == OpinionStatus.SIGN_PASSED) {
/* 192 */         if (passedMsg.length() != 0) {
/* 193 */           passedMsg.append(",");
/*     */         }
/* 195 */         if (mainApproverName.length() != 0) {
/* 196 */           mainApproverName.append(",");
/*     */         }
/* 198 */         mainApproverName.append(opinion.getApproverName());
/* 199 */         passedMsg.append(opinion.getApproverName());
/* 200 */         passedVoteAmount++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 205 */     String opinionMsg = "";
/* 206 */     if (StringUtil.isNotEmpty(passedMsg.toString())) {
/* 207 */       opinionMsg = opinionMsg + "用户[" + passedMsg + "]会签同意;";
/*     */     }
/* 209 */     if (StringUtil.isNotEmpty(notPassedMsg.toString())) {
/* 210 */       opinionMsg = opinionMsg + "用户[" + notPassedMsg + "]会签反对";
/*     */     }
/*     */ 
/*     */     
/* 214 */     if (pluginDef.getVoteType() == VoteType.AMOUNT) {
/*     */ 
/*     */       
/* 217 */       if (passedVoteAmount >= pluginDef.getVoteAmount()) {
/* 218 */         actionModel.setOpinion("投票通过,票选情况如下:" + opinionMsg);
/* 219 */         actionModel.setActionName(ActionType.SIGNAGREE.getKey());
/* 220 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 224 */       boolean hopeless = (remainAmount + passedVoteAmount < pluginDef.getVoteAmount());
/* 225 */       if (hopeless) {
/* 226 */         actionModel.setActionName(ActionType.SIGNOPPOSE.getKey());
/*     */         
/* 228 */         actionModel.setOpinion("投票不通过,票选情况如下:" + opinionMsg);
/* 229 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 234 */     if (pluginDef.getVoteType() == VoteType.PERCENT) {
/*     */       
/* 236 */       int passedRate = passedVoteAmount * 100 / opinions.size();
/*     */ 
/*     */       
/* 239 */       if (passedRate >= pluginDef.getVoteAmount()) {
/* 240 */         actionModel.setOpinion("投票通过,票选情况如下:" + opinionMsg);
/* 241 */         actionModel.setActionName(ActionType.SIGNAGREE.getKey());
/* 242 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 246 */       boolean hopeless = ((remainAmount + passedVoteAmount) * 100 / opinions.size() < pluginDef.getVoteAmount());
/* 247 */       if (hopeless) {
/* 248 */         actionModel.setActionName(ActionType.SIGNOPPOSE.getKey());
/*     */         
/* 250 */         actionModel.setOpinion("投票不通过,票选情况如下:" + opinionMsg);
/* 251 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 255 */     return false;
/*     */   }
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
/*     */   
/*     */   protected void recycleTask(DefualtTaskActionCmd actionModel, boolean hanldeMainTask, BpmTask mainTask, List<BpmTask> subTasks) {
/* 269 */     if (hanldeMainTask) {
/* 270 */       this.LOG.trace("会签任务回收操作=====》回收当前节点的待办的任务和意见和候选人");
/* 271 */       for (BpmTask task : subTasks) {
/* 272 */         this.taskIdentityLinkManager.removeByTaskId(task.getId());
/* 273 */         this.taskManager.remove(task.getId());
/*     */       } 
/*     */       
/* 276 */       for (BpmTask task : subTasks) {
/* 277 */         BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(task.getId());
/* 278 */         if (opinion.getTaskId().equals(mainTask.getId()) || OpinionStatus.fromKey(opinion.getStatus()) != OpinionStatus.AWAITING_CHECK) {
/*     */           continue;
/*     */         }
/* 281 */         opinion.setOpinion("满足条件自动回收该会签任务");
/* 282 */         opinion.setStatus(OpinionStatus.SIGN_RECYCLE.getKey());
/* 283 */         this.bpmTaskOpinionManager.update(opinion);
/*     */       } 
/*     */     } else {
/* 286 */       this.LOG.trace("会签任务处理当前任务=====》删除任务相关信息 - 任务、任务后续人");
/* 287 */       this.taskIdentityLinkManager.removeByTaskId(actionModel.getTaskId());
/* 288 */       this.taskManager.remove(actionModel.getTaskId());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 294 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 299 */     return Boolean.valueOf(false);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/sign/handler/TaskSignAgreeActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */