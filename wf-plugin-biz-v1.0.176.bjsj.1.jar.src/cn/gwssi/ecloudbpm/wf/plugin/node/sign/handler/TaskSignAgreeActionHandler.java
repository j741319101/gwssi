/*     */ package com.dstz.bpm.plugin.node.sign.handler;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.OpinionStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
/*     */ import com.dstz.bpm.engine.action.handler.task.TaskRejectActionHandler;
/*     */ import com.dstz.bpm.plugin.global.leaderTask.context.LeaderTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.global.leaderTask.def.LeaderTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.sign.def.VoteType;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.model.dto.UserDTO;
/*     */ import com.dstz.org.api.service.LeaderService;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
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
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public ActionType getActionType() {
/*  50 */     return ActionType.SIGNAGREE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doAction(DefualtTaskActionCmd actionModel) {
/*  56 */     updateOpinionStatus(actionModel);
/*     */ 
/*     */ 
/*     */     
/*  60 */     SignTaskPluginDef pluginDef = getPluginDef(actionModel);
/*     */     
/*  62 */     BpmTask mainTask = (BpmTask)this.taskManager.get(actionModel.getBpmTask().getParentId());
/*  63 */     List<BpmTask> subTasks = this.taskManager.getByParentId(actionModel.getBpmTask().getParentId());
/*     */ 
/*     */     
/*  66 */     StringBuilder mainApproverName = new StringBuilder();
/*  67 */     boolean hanldeMainTask = isNeedHandleMainTask(actionModel, pluginDef, subTasks, mainTask, mainApproverName);
/*     */     
/*  69 */     this.LOG.trace("会签任务处理当前任务=====》删除任务相关信息 - 任务、任务后续人");
/*  70 */     this.taskIdentityLinkManager.removeByTaskId(actionModel.getTaskId());
/*  71 */     this.taskManager.remove(actionModel.getTaskId());
/*  72 */     this.taskCommand.execute(EventType.TASK_COMPLETE_EVENT, (TaskActionCmd)actionModel);
/*  73 */     this.taskCommand.execute(EventType.TASK_POST_COMPLETE_EVENT, (TaskActionCmd)actionModel);
/*     */     
/*  75 */     if (hanldeMainTask) {
/*     */       
/*  77 */       this.taskManager.recycleTask(mainTask.getId(), OpinionStatus.SIGN_RECYCLE, "满足条件自动回收该会签任务");
/*     */       
/*  79 */       IBpmTask bpmTask = actionModel.getBpmTask();
/*  80 */       actionModel.setBpmTask((IBpmTask)mainTask);
/*     */       
/*  82 */       if (ActionType.SIGNOPPOSE.getKey().equals(actionModel.getActionName()) && ActionType.REJECT.getKey().equals(pluginDef.getOpposedAction())) {
/*  83 */         this.LOG.trace("会签任务处理主任务=====》走驳回逻辑");
/*  84 */         this.taskRejectActionHandler.doActionBefore(actionModel);
/*  85 */         this.taskRejectActionHandler.doAction(actionModel);
/*  86 */         this.taskRejectActionHandler.doActionAfter(actionModel);
/*     */       } else {
/*  88 */         this.LOG.trace("会签任务处理主任务=====》调用普通任务处理逻辑");
/*  89 */         super.doAction(actionModel);
/*     */       } 
/*  91 */       actionModel.setBpmTask(bpmTask);
/*  92 */       BpmTaskOpinion mainOpinion = this.bpmTaskOpinionManager.getByTaskId(mainTask.getTaskId());
/*  93 */       mainOpinion.setApproverName(mainApproverName.toString());
/*  94 */       mainOpinion.setApprover(null);
/*  95 */       mainOpinion.setApproveTime(new Date((new Date()).getTime() + 1000L));
/*  96 */       this.bpmTaskOpinionManager.update(mainOpinion);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {
/* 107 */     taskComplatePrePluginExecute(actionModel);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 112 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 117 */     return "/bpm/task/taskOpinionDialog.html";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateOpinionStatus(DefualtTaskActionCmd actionModel) {
/*     */     UserDTO userDTO;
/* 128 */     OpinionStatus opinionStatus = OpinionStatus.getByActionName(actionModel.getActionName());
/* 129 */     this.LOG.trace("会签任务处理当前任务=====》更新任务意见状态");
/*     */     
/* 131 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getTaskId());
/* 132 */     if (bpmTaskOpinion == null) {
/*     */       return;
/*     */     }
/* 135 */     BpmTask task = (BpmTask)actionModel.getBpmTask();
/* 136 */     bpmTaskOpinion.setStatus(opinionStatus.getKey());
/* 137 */     bpmTaskOpinion.setApproveTime(new Date());
/*     */     
/* 139 */     bpmTaskOpinion.setDurMs(Long.valueOf(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime()));
/* 140 */     bpmTaskOpinion.setOpinion(actionModel.getOpinion());
/*     */     
/* 142 */     IUser user = ContextUtil.getCurrentUser();
/*     */     
/* 144 */     if (!StringUtils.equals(task.getAssigneeId(), "0") && 
/* 145 */       !StringUtils.equals(user.getUserId(), task.getAssigneeId())) {
/* 146 */       LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 147 */       BpmNodeDef dBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
/* 148 */       LeaderTaskPluginContext leaderTaskPluginContext = (LeaderTaskPluginContext)dBpmNodeDef.getPluginContext(LeaderTaskPluginContext.class);
/* 149 */       if (((LeaderTaskPluginDef)leaderTaskPluginContext.getBpmPluginDef()).isSignLeaderTask()) {
/* 150 */         IUser secretary = leaderService.getUserByLeaderId(task.getAssigneeId());
/*     */         
/* 152 */         if (secretary != null && StringUtils.equals(secretary.getUserId(), user.getUserId())) {
/* 153 */           userDTO = new UserDTO();
/* 154 */           userDTO.setId(task.getAssigneeId());
/* 155 */           userDTO.setFullname(task.getAssigneeNames());
/* 156 */           userDTO.setSn(Integer.valueOf(0));
/*     */         } 
/*     */       } 
/*     */     } 
/* 160 */     bpmTaskOpinion.setApprover(userDTO.getUserId());
/* 161 */     bpmTaskOpinion.setApproverName(userDTO.getFullname());
/* 162 */     TaskIdentityLink taskIdentityLink = actionModel.getTaskIdentityLink();
/* 163 */     bpmTaskOpinion.setTaskOrgId(taskIdentityLink.getOrgId());
/* 164 */     this.bpmTaskOpinionManager.update(bpmTaskOpinion);
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
/* 176 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(actionModel.getBpmInstance().getDefId(), actionModel.getNodeId());
/* 177 */     SignTaskPluginContext pluginContext = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
/* 178 */     return (SignTaskPluginDef)pluginContext.getBpmPluginDef();
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
/*     */   public boolean isNeedHandleMainTask(DefualtTaskActionCmd actionModel, SignTaskPluginDef pluginDef, List<BpmTask> subTasks, BpmTask mainTask, StringBuilder mainApproverName) {
/* 193 */     return isNeedHandleMainTask(actionModel, pluginDef, subTasks, mainTask, mainApproverName, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNeedHandleMainTask(DefualtTaskActionCmd actionModel, SignTaskPluginDef pluginDef, List<BpmTask> subTasks, BpmTask mainTask, StringBuilder mainApproverName, int decreate) {
/* 198 */     int remainAmount = subTasks.size() - decreate;
/*     */     
/* 200 */     if (pluginDef.isNeedAllSign() && remainAmount != 0) {
/* 201 */       return false;
/*     */     }
/*     */     
/* 204 */     List<BpmTaskOpinion> allOpinions = this.bpmTaskOpinionManager.getByInstAndSignId(actionModel.getInstanceId(), mainTask.getId());
/*     */     
/* 206 */     List<BpmTaskOpinion> opinions = (List<BpmTaskOpinion>)allOpinions.stream().filter(opinion -> !OpinionStatus.TURN.getKey().equals(opinion.getStatus())).collect(Collectors.toList());
/*     */     
/* 208 */     int passedVoteAmount = 0;
/* 209 */     StringBuilder passedMsg = new StringBuilder("");
/* 210 */     StringBuilder notPassedMsg = new StringBuilder("");
/* 211 */     for (BpmTaskOpinion opinion : opinions) {
/* 212 */       if (OpinionStatus.fromKey(opinion.getStatus()) == OpinionStatus.SIGN_NOT_PASSED || OpinionStatus.fromKey(opinion.getStatus()) == OpinionStatus.DECREASEDYNAMIC) {
/* 213 */         if (notPassedMsg.length() != 0) {
/* 214 */           notPassedMsg.append(",");
/*     */         }
/* 216 */         if (mainApproverName.length() != 0) {
/* 217 */           mainApproverName.append(",");
/*     */         }
/* 219 */         notPassedMsg.append(opinion.getApproverName());
/* 220 */         mainApproverName.append(opinion.getApproverName());
/*     */       } 
/* 222 */       if (OpinionStatus.fromKey(opinion.getStatus()) == OpinionStatus.SIGN_PASSED) {
/* 223 */         if (passedMsg.length() != 0) {
/* 224 */           passedMsg.append(",");
/*     */         }
/* 226 */         if (mainApproverName.length() != 0) {
/* 227 */           mainApproverName.append(",");
/*     */         }
/* 229 */         mainApproverName.append(opinion.getApproverName());
/* 230 */         passedMsg.append(opinion.getApproverName());
/* 231 */         passedVoteAmount++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 236 */     String opinionMsg = "";
/* 237 */     if (StringUtil.isNotEmpty(passedMsg.toString())) {
/* 238 */       opinionMsg = opinionMsg + "用户[" + passedMsg + "]会签同意;";
/*     */     }
/* 240 */     if (StringUtil.isNotEmpty(notPassedMsg.toString())) {
/* 241 */       opinionMsg = opinionMsg + "用户[" + notPassedMsg + "]会签反对";
/*     */     }
/*     */ 
/*     */     
/* 245 */     if (pluginDef.getVoteType() == VoteType.AMOUNT) {
/*     */ 
/*     */       
/* 248 */       if (passedVoteAmount >= pluginDef.getVoteAmount().intValue()) {
/* 249 */         if (StringUtil.isNotEmpty(opinionMsg)) {
/* 250 */           actionModel.setOpinion("投票通过,票选情况如下:" + opinionMsg);
/*     */         }
/* 252 */         actionModel.setActionName(ActionType.SIGNAGREE.getKey());
/* 253 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 257 */       boolean hopeless = (remainAmount + passedVoteAmount < pluginDef.getVoteAmount().intValue());
/* 258 */       if (hopeless) {
/* 259 */         actionModel.setActionName(ActionType.SIGNOPPOSE.getKey());
/*     */         
/* 261 */         if (StringUtil.isNotEmpty(opinionMsg)) {
/* 262 */           actionModel.setOpinion("投票不通过,票选情况如下:" + opinionMsg);
/*     */         }
/* 264 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 269 */     if (pluginDef.getVoteType() == VoteType.PERCENT) {
/* 270 */       if (opinions.size() == 0) {
/* 271 */         actionModel.setActionName(ActionType.SIGNOPPOSE.getKey());
/* 272 */         actionModel.setOpinion("投票不通过,票选情况如下:" + opinionMsg);
/* 273 */         return true;
/*     */       } 
/*     */       
/* 276 */       int passedRate = passedVoteAmount * 100 / opinions.size();
/*     */ 
/*     */       
/* 279 */       if (passedRate >= pluginDef.getVoteAmount().intValue()) {
/* 280 */         actionModel.setOpinion("投票通过,票选情况如下:" + opinionMsg);
/* 281 */         actionModel.setActionName(ActionType.SIGNAGREE.getKey());
/* 282 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 286 */       boolean hopeless = ((remainAmount + passedVoteAmount) * 100 / opinions.size() < pluginDef.getVoteAmount().intValue());
/* 287 */       if (hopeless) {
/* 288 */         actionModel.setActionName(ActionType.SIGNOPPOSE.getKey());
/*     */         
/* 290 */         actionModel.setOpinion("投票不通过,票选情况如下:" + opinionMsg);
/* 291 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 295 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 300 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 305 */     return Boolean.valueOf(false);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/sign/handler/TaskSignAgreeActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */