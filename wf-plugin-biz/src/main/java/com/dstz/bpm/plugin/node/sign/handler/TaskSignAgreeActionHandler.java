package com.dstz.bpm.plugin.node.sign.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
import com.dstz.bpm.engine.action.handler.task.TaskRejectActionHandler;
//import com.dstz.bpm.plugin.global.leaderTask.context.LeaderTaskPluginContext;
//import com.dstz.bpm.plugin.global.leaderTask.def.LeaderTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.def.VoteType;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.dto.UserDTO;
//import com.dstz.org.api.service.LeaderService;
import com.dstz.sys.util.ContextUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("taskSignAgreeActionHandler")
public class TaskSignAgreeActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
   @Autowired
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource(
      name = "taskRejectActionHandler"
   )
   private TaskRejectActionHandler taskRejectActionHandler;
   @Resource
   private BpmProcessDefService bpmProcessDefService;

   public ActionType getActionType() {
      return ActionType.SIGNAGREE;
   }

   public void doAction(DefualtTaskActionCmd actionModel) {
      this.updateOpinionStatus(actionModel);
      SignTaskPluginDef pluginDef = this.getPluginDef(actionModel);
      BpmTask mainTask = (BpmTask)this.taskManager.get(actionModel.getBpmTask().getParentId());
      List<BpmTask> subTasks = this.taskManager.getByParentId(actionModel.getBpmTask().getParentId());
      StringBuilder mainApproverName = new StringBuilder();
      boolean hanldeMainTask = this.isNeedHandleMainTask(actionModel, pluginDef, subTasks, mainTask, mainApproverName);
      this.LOG.trace("会签任务处理当前任务=====》删除任务相关信息 - 任务、任务后续人");
      this.taskIdentityLinkManager.removeByTaskId(actionModel.getTaskId());
      this.taskManager.remove(actionModel.getTaskId());
      this.taskCommand.execute(EventType.TASK_COMPLETE_EVENT, actionModel);
      this.taskCommand.execute(EventType.TASK_POST_COMPLETE_EVENT, actionModel);
      if (hanldeMainTask) {
         this.taskManager.recycleTask(mainTask.getId(), OpinionStatus.SIGN_RECYCLE, "满足条件自动回收该会签任务");
         IBpmTask bpmTask = actionModel.getBpmTask();
         actionModel.setBpmTask(mainTask);
         if (ActionType.SIGNOPPOSE.getKey().equals(actionModel.getActionName()) && ActionType.REJECT.getKey().equals(pluginDef.getOpposedAction())) {
            this.LOG.trace("会签任务处理主任务=====》走驳回逻辑");
            this.taskRejectActionHandler.doActionBefore(actionModel);
            this.taskRejectActionHandler.doAction(actionModel);
            this.taskRejectActionHandler.doActionAfter(actionModel);
         } else {
            this.LOG.trace("会签任务处理主任务=====》调用普通任务处理逻辑");
            super.doAction(actionModel);
         }

         actionModel.setBpmTask(bpmTask);
         BpmTaskOpinion mainOpinion = this.bpmTaskOpinionManager.getByTaskId(mainTask.getTaskId());
         mainOpinion.setApproverName(mainApproverName.toString());
         mainOpinion.setApprover((String)null);
         mainOpinion.setApproveTime(new Date((new Date()).getTime() + 1000L));
         this.bpmTaskOpinionManager.update(mainOpinion);
      }

   }

   protected void doActionAfter(DefualtTaskActionCmd actionModel) {
   }

   protected void doActionBefore(DefualtTaskActionCmd actionModel) {
      this.taskComplatePrePluginExecute(actionModel);
   }

   public int getSn() {
      return 1;
   }

   public String getConfigPage() {
      return "/bpm/task/taskOpinionDialog.html";
   }

   protected void updateOpinionStatus(DefualtTaskActionCmd actionModel) {
      OpinionStatus opinionStatus = OpinionStatus.getByActionName(actionModel.getActionName());
      this.LOG.trace("会签任务处理当前任务=====》更新任务意见状态");
      BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getTaskId());
      if (bpmTaskOpinion != null) {
         BpmTask task = (BpmTask)actionModel.getBpmTask();
         bpmTaskOpinion.setStatus(opinionStatus.getKey());
         bpmTaskOpinion.setApproveTime(new Date());
         bpmTaskOpinion.setDurMs(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime());
         bpmTaskOpinion.setOpinion(actionModel.getOpinion());
         IUser user = ContextUtil.getCurrentUser();
//         if (!StringUtils.equals(task.getAssigneeId(), "0") && !StringUtils.equals(((IUser)user).getUserId(), task.getAssigneeId())) {
//            LeaderService leaderService = (LeaderService)AppUtil.getImplInstanceArray(LeaderService.class).get(0);
//            BpmNodeDef dBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
//            LeaderTaskPluginContext leaderTaskPluginContext = (LeaderTaskPluginContext)dBpmNodeDef.getPluginContext(LeaderTaskPluginContext.class);
//            if (((LeaderTaskPluginDef)leaderTaskPluginContext.getBpmPluginDef()).isSignLeaderTask()) {
//               IUser secretary = leaderService.getUserByLeaderId(task.getAssigneeId());
//               if (secretary != null && StringUtils.equals(secretary.getUserId(), ((IUser)user).getUserId())) {
//                  user = new UserDTO();
//                  ((UserDTO)user).setId(task.getAssigneeId());
//                  ((UserDTO)user).setFullname(task.getAssigneeNames());
//                  ((UserDTO)user).setSn(0);
//               }
//            }
//         } todo 去掉领导任务

         bpmTaskOpinion.setApprover(((IUser)user).getUserId());
         bpmTaskOpinion.setApproverName(((IUser)user).getFullname());
         TaskIdentityLink taskIdentityLink = actionModel.getTaskIdentityLink();
         bpmTaskOpinion.setTaskOrgId(taskIdentityLink.getOrgId());
         this.bpmTaskOpinionManager.update(bpmTaskOpinion);
      }
   }

   protected SignTaskPluginDef getPluginDef(DefualtTaskActionCmd actionModel) {
      BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(actionModel.getBpmInstance().getDefId(), actionModel.getNodeId());
      SignTaskPluginContext pluginContext = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
      return (SignTaskPluginDef)pluginContext.getBpmPluginDef();
   }

   public boolean isNeedHandleMainTask(DefualtTaskActionCmd actionModel, SignTaskPluginDef pluginDef, List<BpmTask> subTasks, BpmTask mainTask, StringBuilder mainApproverName) {
      return this.isNeedHandleMainTask(actionModel, pluginDef, subTasks, mainTask, mainApproverName, 1);
   }

   public boolean isNeedHandleMainTask(DefualtTaskActionCmd actionModel, SignTaskPluginDef pluginDef, List<BpmTask> subTasks, BpmTask mainTask, StringBuilder mainApproverName, int decreate) {
      int remainAmount = subTasks.size() - decreate;
      if (pluginDef.isNeedAllSign() && remainAmount != 0) {
         return false;
      } else {
         List<BpmTaskOpinion> allOpinions = this.bpmTaskOpinionManager.getByInstAndSignId(actionModel.getInstanceId(), mainTask.getId());
         List<BpmTaskOpinion> opinions = (List)allOpinions.stream().filter((opinionx) -> {
            return !OpinionStatus.TURN.getKey().equals(opinionx.getStatus());
         }).collect(Collectors.toList());
         int passedVoteAmount = 0;
         StringBuilder passedMsg = new StringBuilder("");
         StringBuilder notPassedMsg = new StringBuilder("");
         Iterator var13 = opinions.iterator();

         while(var13.hasNext()) {
            BpmTaskOpinion opinion = (BpmTaskOpinion)var13.next();
            if (OpinionStatus.fromKey(opinion.getStatus()) == OpinionStatus.SIGN_NOT_PASSED || OpinionStatus.fromKey(opinion.getStatus()) == OpinionStatus.DECREASEDYNAMIC) {
               if (notPassedMsg.length() != 0) {
                  notPassedMsg.append(",");
               }

               if (mainApproverName.length() != 0) {
                  mainApproverName.append(",");
               }

               notPassedMsg.append(opinion.getApproverName());
               mainApproverName.append(opinion.getApproverName());
            }

            if (OpinionStatus.fromKey(opinion.getStatus()) == OpinionStatus.SIGN_PASSED) {
               if (passedMsg.length() != 0) {
                  passedMsg.append(",");
               }

               if (mainApproverName.length() != 0) {
                  mainApproverName.append(",");
               }

               mainApproverName.append(opinion.getApproverName());
               passedMsg.append(opinion.getApproverName());
               ++passedVoteAmount;
            }
         }

         String opinionMsg = "";
         if (StringUtil.isNotEmpty(passedMsg.toString())) {
            opinionMsg = opinionMsg + "用户[" + passedMsg + "]会签同意;";
         }

         if (StringUtil.isNotEmpty(notPassedMsg.toString())) {
            opinionMsg = opinionMsg + "用户[" + notPassedMsg + "]会签反对";
         }

         if (pluginDef.getVoteType() == VoteType.AMOUNT) {
            if (passedVoteAmount >= pluginDef.getVoteAmount()) {
               if (StringUtil.isNotEmpty(opinionMsg)) {
                  actionModel.setOpinion("投票通过,票选情况如下:" + opinionMsg);
               }

               actionModel.setActionName(ActionType.SIGNAGREE.getKey());
               return true;
            }

            boolean hopeless = remainAmount + passedVoteAmount < pluginDef.getVoteAmount();
            if (hopeless) {
               actionModel.setActionName(ActionType.SIGNOPPOSE.getKey());
               if (StringUtil.isNotEmpty(opinionMsg)) {
                  actionModel.setOpinion("投票不通过,票选情况如下:" + opinionMsg);
               }

               return true;
            }
         }

         if (pluginDef.getVoteType() == VoteType.PERCENT) {
            if (opinions.size() == 0) {
               actionModel.setActionName(ActionType.SIGNOPPOSE.getKey());
               actionModel.setOpinion("投票不通过,票选情况如下:" + opinionMsg);
               return true;
            }

            int passedRate = passedVoteAmount * 100 / opinions.size();
            if (passedRate >= pluginDef.getVoteAmount()) {
               actionModel.setOpinion("投票通过,票选情况如下:" + opinionMsg);
               actionModel.setActionName(ActionType.SIGNAGREE.getKey());
               return true;
            }

            boolean hopeless = (remainAmount + passedVoteAmount) * 100 / opinions.size() < pluginDef.getVoteAmount();
            if (hopeless) {
               actionModel.setActionName(ActionType.SIGNOPPOSE.getKey());
               actionModel.setOpinion("投票不通过,票选情况如下:" + opinionMsg);
               return true;
            }
         }

         return false;
      }
   }

   public Boolean isDefault() {
      return false;
   }

   public Boolean isSupport(BpmNodeDef nodeDef) {
      return false;
   }
}
