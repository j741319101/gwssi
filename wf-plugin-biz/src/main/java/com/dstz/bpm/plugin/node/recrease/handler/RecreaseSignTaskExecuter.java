package com.dstz.bpm.plugin.node.recrease.handler;

import com.dstz.bpm.act.service.ActTaskService;
import com.dstz.bpm.act.util.ActivitiUtil;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.plugin.cmd.TaskCommand;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.handler.task.TaskRejectActionHandler;
//import com.dstz.bpm.plugin.global.leaderTask.context.LeaderTaskPluginContext;
//import com.dstz.bpm.plugin.global.leaderTask.def.LeaderTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.handler.TaskSignAgreeActionHandler;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.id.IdUtil;
import com.dstz.base.core.util.AppUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.dto.UserDTO;
//import com.dstz.org.api.service.LeaderService;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RecreaseSignTaskExecuter {
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource
   private BpmTaskManager bpmTaskManager;
   @Resource
   private UserService userService;
   @Resource
   private BpmInstanceManager bpmInstanceManager;
   @Resource
   private TaskIdentityLinkManager taskIdentityLinkManager;
   @Resource
   private BpmProcessDefService bpmProcessDefService;
   @Resource
   private TaskCommand taskCommand;
   @Resource
   private TaskSignAgreeActionHandler taskSignAgreeActionHandler;
   @Resource(
      name = "taskRejectActionHandler"
   )
   private TaskRejectActionHandler taskRejectActionHandler;
   @Resource
   protected ActTaskService actTaskService;

   public void increase(DefualtTaskActionCmd model) {
      BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(model.getTaskId());
      JSONObject userInfo = model.getExtendConf();
      JSONArray users = userInfo.getJSONArray("users");
      String signId = model.getTaskId();

      for(int i = 0; i < users.size(); ++i) {
         JSONObject user = users.getJSONObject(i);
         BpmTask task = (BpmTask)this.bpmTaskManager.get(model.getTaskId());
         task.setId(IdUtil.getSuid());
         task.setTaskType(TaskType.SIGN.getKey());
         task.setStatus("NORMAL");
         task.setParentId(signId);
         String clazzStr = user.getString("clazz");
         Class clazz = SysIdentity.class;
         if (StringUtils.isNotEmpty(clazzStr)) {
            ServiceLoader<SysIdentity> loader = ServiceLoader.load(SysIdentity.class);
            Iterator var12 = loader.iterator();

            while(var12.hasNext()) {
               SysIdentity sysIdentity = (SysIdentity)var12.next();
               if (StringUtils.equals(clazzStr, sysIdentity.getClass().getSimpleName())) {
                  clazz = sysIdentity.getClass();
                  break;
               }
            }
         }

         SysIdentity bpmInentity = (SysIdentity)JSON.toJavaObject(user, clazz);
         task.setAssigneeId(bpmInentity.getId());
         IUser addUser = this.userService.getUserById(bpmInentity.getId());
         if (addUser == null) {
            throw new BusinessException(user.getString("name") + "用户丢失");
         }

         bpmInentity.setName(addUser.getFullname());
         task.setAssigneeNames(addUser.getFullname());
         List<SysIdentity> identityList = new ArrayList();
         identityList.add(bpmInentity);
         BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
         this.bpmTaskOpinionManager.createOpinion(task, bpmInstance, identityList, model.getOpinion(), model.getActionName(), model.getFormId(), signId, (String)null, (String)null);
         this.taskIdentityLinkManager.createIdentityLink(task, identityList);
         this.bpmTaskManager.create(task);
         BpmContext.setActionModel(model);
         model.setBpmTask(task);
         this.taskCommand.execute(EventType.TASK_POST_CREATE_EVENT, model);
         BpmContext.removeActionModel();
      }

      bpmTaskOpinion.setCreateTime(new Date((new Date()).getTime() + 1000L));
      this.bpmTaskOpinionManager.update(bpmTaskOpinion);
   }

   public void decrease(DefualtTaskActionCmd model) {
      JSONObject extendConf = model.getExtendConf();
      String callBackId = extendConf == null ? "" : extendConf.getString("callBackId");
      BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(model.getTaskId());
      String orgId = "";
      if (bpmTaskOpinion.getApproveTime() == null) {
         TaskIdentityLink taskIdentityLink = (TaskIdentityLink)this.taskIdentityLinkManager.get(model.getTaskLinkId());
         if (taskIdentityLink != null) {
            orgId = taskIdentityLink.getOrgId();
         }
      } else {
         orgId = bpmTaskOpinion.getTaskOrgId();
      }

      if (StringUtils.isEmpty(orgId)) {
         throw new BusinessException("当前机构为空，请检查TaskLinkId");
      } else {
         model.setApproveOrgId(orgId);
         BpmTask mainTask = null;
         if (!StringUtils.isNotEmpty(callBackId)) {
            throw new BusinessException("减签操作 任务id不能为空");
         } else {
            String[] var7 = callBackId.split(",");
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String opinionId = var7[var9];
               BpmTaskOpinion decreaseTaskOpinion = (BpmTaskOpinion)this.bpmTaskOpinionManager.get(opinionId);
               if (decreaseTaskOpinion == null || decreaseTaskOpinion.getApproveTime() != null) {
                  throw new BusinessException("任务已处理 : " + opinionId);
               }

               IUser user = ContextUtil.getCurrentUser();
               BpmTask signTask = (BpmTask)this.bpmTaskManager.get(decreaseTaskOpinion.getTaskId());
               BpmNodeDef dBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(signTask.getDefId(), signTask.getNodeId());
               decreaseTaskOpinion.setStatus(OpinionStatus.DECREASEDYNAMIC.getKey());
               decreaseTaskOpinion.setApproveTime(new Date());
               decreaseTaskOpinion.setDurMs(decreaseTaskOpinion.getApproveTime().getTime() - decreaseTaskOpinion.getCreateTime().getTime());
//               LeaderTaskPluginContext leaderTaskPluginContext = (LeaderTaskPluginContext)dBpmNodeDef.getPluginContext(LeaderTaskPluginContext.class);
//               if (((LeaderTaskPluginDef)leaderTaskPluginContext.getBpmPluginDef()).isSignLeaderTask()) {
//                  LeaderService leaderService = (LeaderService)AppUtil.getImplInstanceArray(LeaderService.class).get(0);
//                  IUser secretary = leaderService.getUserByLeaderId(signTask.getAssigneeId());
//                  if (secretary != null && StringUtils.equals(secretary.getUserId(), ((IUser)user).getUserId())) {
//                     user = new UserDTO();
//                     ((UserDTO)user).setId(signTask.getAssigneeId());
//                     ((IUser)user).setFullname(signTask.getAssigneeNames());
//                     ((IUser)user).setSn(0);
//                  }
//               } todo 去掉领导任务

               decreaseTaskOpinion.setApprover(((IUser)user).getUserId());
               decreaseTaskOpinion.setOpinion(model.getOpinion());
               decreaseTaskOpinion.setApproverName(((IUser)user).getFullname());
               decreaseTaskOpinion.setTaskOrgId(model.getApproveOrgId());
               this.bpmTaskOpinionManager.update(decreaseTaskOpinion);
               if (mainTask == null) {
                  mainTask = (BpmTask)this.bpmTaskManager.get(decreaseTaskOpinion.getSignId());
               }

               this.bpmTaskManager.remove(decreaseTaskOpinion.getTaskId());
               this.taskIdentityLinkManager.removeByTaskId(decreaseTaskOpinion.getTaskId());
               model.setBpmTask(signTask);
               model.setTaskId(signTask.getId());
               BpmContext.setActionModel(model);
               this.taskCommand.execute(EventType.TASK_COMPLETE_EVENT, model);
               this.taskCommand.execute(EventType.TASK_POST_COMPLETE_EVENT, model);
               BpmContext.removeActionModel();
            }

            BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(mainTask.getDefId(), mainTask.getNodeId());
            List<BpmTask> subTasks = this.bpmTaskManager.getByParentId(mainTask.getId());
            StringBuilder mainApproverName = new StringBuilder();
            SignTaskPluginDef pluginDef = (SignTaskPluginDef)((SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class)).getBpmPluginDef();
            boolean hanldeMainTask = this.taskSignAgreeActionHandler.isNeedHandleMainTask(model, pluginDef, subTasks, mainTask, mainApproverName, 0);
            if (hanldeMainTask) {
               this.bpmTaskManager.recycleTask(mainTask.getId(), OpinionStatus.SIGN_RECYCLE, "满足条件自动回收该会签任务");
               model.setBpmTask(mainTask);
               BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(mainTask.getInstId());
               model.setBpmInstance(bpmInstance);
               BpmContext.setActionModel(model);
               String[] nodeIds = new String[0];
               if (bpmTaskOpinion.getApproveTime() != null) {
                  nodeIds = new String[]{bpmTaskOpinion.getTaskKey()};
               }

               Map activityMap = ActivitiUtil.skipPrepare(bpmInstance.getActDefId(), bpmNodeDef.getNodeId(), nodeIds);

               try {
                  if (ActionType.SIGNOPPOSE.getKey().equals(model.getActionName()) && ActionType.REJECT.getKey().equals(pluginDef.getOpposedAction())) {
                     this.taskRejectActionHandler.doActionBefore(model);
                     this.taskRejectActionHandler.doAction(model);
                     this.taskRejectActionHandler.doActionAfter(model);
                  } else {
                     this.actTaskService.completeTask(mainTask.getTaskId(), model.getActionVariables());
                  }
               } catch (Exception var21) {
                  throw new BusinessException(var21);
               } finally {
                  ActivitiUtil.restoreActivity(activityMap);
               }
            }

         }
      }
   }
}
