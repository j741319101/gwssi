package com.dstz.bpm.plugin.global.agency.executer;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.TaskSkipType;
import com.dstz.bpm.api.constant.TaskStatus;
import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.plugin.core.manager.BpmUserAgencyConfigManager;
import com.dstz.bpm.plugin.core.manager.BpmUserAgencyLogManager;
import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
import com.dstz.bpm.plugin.global.agency.def.TaskAgencyPluginDef;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskAgencyPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, TaskAgencyPluginDef> {
   @Autowired
   private BpmUserAgencyConfigManager bpmUserAgencyConfigManager;
   @Autowired
   private BpmUserAgencyLogManager bpmUserAgencyLogManager;
   @Autowired
   private BpmTaskManager bpmTaskManager;
   @Autowired
   private TaskIdentityLinkManager taskIdentityLinkManager;
   @Autowired
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Autowired
   private UserService userService;
   @Autowired
   private BpmProcessDefService bpmProcessDefService;

   public Void execute(BpmExecutionPluginSession pluginSession, TaskAgencyPluginDef pluginDef) {
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)Objects.requireNonNull(BpmContext.getActionModel());
      if (Objects.nonNull(model.isHasSkipThisTask()) && !TaskSkipType.NO_SKIP.equals(model.isHasSkipThisTask())) {
         return null;
      } else {
         EventType eventType = pluginSession.getEventType();
         if (eventType == EventType.TASK_POST_CREATE_EVENT) {
            this.postCreate();
         } else if (eventType == EventType.TASK_POST_COMPLETE_EVENT) {
            this.postComplete();
         }

         return null;
      }
   }

   private void postComplete() {
      TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
      IBpmTask bpmTask = model.getBpmTask();
      QueryFilter queryFilter = new DefaultQueryFilter();
      queryFilter.addFilter("task_id_", bpmTask.getId(), QueryOP.EQUAL);
      List<BpmUserAgencyLog> bpmUserAgencyLogs = this.bpmUserAgencyLogManager.query(queryFilter);
      if (CollectionUtil.isNotEmpty(bpmUserAgencyLogs)) {
         BpmUserAgencyLog bpmUserAgencyLog = (BpmUserAgencyLog)bpmUserAgencyLogs.get(0);
         BpmUserAgencyConfig bpmUserAgencyConfig = (BpmUserAgencyConfig)this.bpmUserAgencyConfigManager.get(bpmUserAgencyLog.getConfigId());
         if (bpmUserAgencyConfig == null) {
            return;
         }

         IUser user = this.userService.getUserById(bpmUserAgencyConfig.getConfigUserId());
         BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
         IUser currentUser = ContextUtil.getCurrentUser();
         bpmTaskOpinion.setApproverName(user.getFullname() + "(" + currentUser.getFullname() + " 代)");
         this.bpmTaskOpinionManager.update(bpmTaskOpinion);
      }

   }

   private void postCreate() {
      TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
      IBpmTask bpmTask = model.getBpmTask();
      if (bpmTask != null) {
         BpmTask bpmTaskUpdate = (BpmTask)this.bpmTaskManager.get(bpmTask.getId());
         if (bpmTaskUpdate != null) {
            IBpmInstance bpmInstance = model.getBpmInstance();
            List<SysIdentity> sysIdentities = model.getBpmIdentity(bpmTask.getNodeId());
            if (!CollectionUtil.isEmpty(sysIdentities)) {
               List<SysIdentity> identityList = (List)sysIdentities.stream().filter((item) -> {
                  return StringUtils.equals("user", item.getType());
               }).collect(Collectors.toList());
               if (!CollectionUtil.isEmpty(identityList)) {
                  if (StringUtils.equals(bpmTaskUpdate.getTaskType(), TaskType.SIGN_SOURCE.getKey())) {
                     this.bpmTaskManager.getByParentId(bpmTaskUpdate.getId()).stream().filter((task) -> {
                        return StringUtils.equals(task.getTaskType(), TaskType.SIGN.getKey());
                     }).forEach((task) -> {
                        List<SysIdentity> identity = new ArrayList();
                        TaskIdentityLink taskIdentityLink = (TaskIdentityLink)this.taskIdentityLinkManager.getByTaskId(task.getId()).get(0);
                        identity.add(new DefaultIdentity(task.getAssigneeId(), task.getAssigneeNames(), "user", taskIdentityLink.getOrgId()));
                        this.updateTask(identity, bpmInstance, task);
                     });
                  } else {
                     this.updateTask(identityList, bpmInstance, bpmTaskUpdate);
                  }

               }
            }
         }
      }
   }

   private void updateTask(List<SysIdentity> identityList, IBpmInstance bpmInstance, BpmTask bpmTaskUpdate) {
      Set<SysIdentity> identityUsers = new HashSet();
      Set<SysIdentity> nomalUsers = new HashSet();
      StringBuilder stringBuilder = new StringBuilder();
      identityList.forEach((identity) -> {
         List<BpmUserAgencyConfig> bpmUserAgencyConfigList = this.bpmUserAgencyConfigManager.selectTakeEffectingList(identity.getId(), new Date());
         if (CollectionUtil.isEmpty(bpmUserAgencyConfigList)) {
            nomalUsers.add(identity);
         } else {
            Optional<BpmUserAgencyConfig> agencyConfig = bpmUserAgencyConfigList.stream().filter((bpmUserAgencyConfig) -> {
               return StringUtil.isEmpty(bpmUserAgencyConfig.getAgencyFlowKey()) || ArrayUtils.indexOf(bpmUserAgencyConfig.getAgencyFlowKey().split(","), bpmInstance.getDefKey()) != -1;
            }).findFirst();
            Set<SysIdentity> users = new HashSet();
            agencyConfig.ifPresent((config) -> {
               stringBuilder.append("1");
               String[] userIds = config.getTargetUserId().split(",");
               String[] userNames = config.getTargetUserName().split(",");

               for(int i = 0; i < userIds.length; ++i) {
                  users.add(new DefaultIdentity(userIds[i], identity.getName() + "(" + userNames[i] + " 代)", "user", identity.getOrgId(), 1));
               }

               if (users.size() > 0) {
                  users.stream().filter((user) -> {
                     return !nomalUsers.contains(user);
                  }).forEach((user) -> {
                     identityUsers.add(user);
                  });
                  this.writeUserAgencyLog(config, bpmTaskUpdate);
               }

            });
            if (!agencyConfig.isPresent()) {
               nomalUsers.add(identity);
            }

         }
      });
      if (stringBuilder.length() != 0) {
         DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(bpmInstance.getDefId());
         if (nomalUsers.size() + identityUsers.size() == 0 && !processDef.getExtProperties().isAllowExecutorEmpty()) {
            throw new BusinessMessage(String.format("%s节点 候选人不能为空, 因原候选人工作委托导致处理人为空", bpmTaskUpdate.getName()), BpmStatusCode.NO_TASK_USER);
         } else {
            if (identityUsers.size() + nomalUsers.size() == 1) {
               if (identityUsers.size() == 1) {
                  bpmTaskUpdate.setAssigneeId(((SysIdentity)identityUsers.stream().findFirst().get()).getId());
                  bpmTaskUpdate.setStatus(TaskStatus.AGENCY.getKey());
               } else {
                  bpmTaskUpdate.setAssigneeId(((SysIdentity)nomalUsers.stream().findFirst().get()).getId());
               }
            } else {
               bpmTaskUpdate.setAssigneeId("0");
            }

            this.updateTaskAndOpinion(bpmTaskUpdate, identityUsers, nomalUsers);
            this.writeTaskIdentityLink(bpmTaskUpdate, identityUsers, nomalUsers);
         }
      }
   }

   private void updateTaskAndOpinion(BpmTask bpmTask, Set<SysIdentity> identityUsers, Set<SysIdentity> nomalUsers) {
      BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
      StringBuilder assignInfoAppend = new StringBuilder();
      StringBuilder assigneeNames = new StringBuilder();
      identityUsers.forEach((user) -> {
         assignInfoAppend.append(user.getType()).append("-").append(user.getName()).append("-").append(user.getId()).append("-").append(user.getOrgId()).append(",");
         assigneeNames.append(user.getName()).append(",");
      });
      nomalUsers.forEach((user) -> {
         assignInfoAppend.append(user.getType()).append("-").append(user.getName()).append("-").append(user.getId()).append("-").append(user.getOrgId()).append(",");
         assigneeNames.append(user.getName()).append(",");
      });
      bpmTaskOpinion.setAssignInfo(assignInfoAppend.substring(0, assignInfoAppend.length() - 1));
      this.bpmTaskOpinionManager.update(bpmTaskOpinion);
      bpmTask.setAssigneeNames(assigneeNames.substring(0, assigneeNames.length() - 1));
      this.bpmTaskManager.update(bpmTask);
   }

   private void writeTaskIdentityLink(BpmTask bpmTask, Set<SysIdentity> identityUsers, Set<SysIdentity> nomalUsers) {
      this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
      List<SysIdentity> sysIdentityList = new ArrayList();
      identityUsers.forEach((user) -> {
         sysIdentityList.add(new DefaultIdentity(user.getId(), user.getName(), "user", user.getOrgId()));
      });
      if (sysIdentityList.size() > 0) {
         bpmTask.setTaskType(TaskType.AGENT.getKey());
         this.taskIdentityLinkManager.createIdentityLink(bpmTask, sysIdentityList);
      }

      sysIdentityList.clear();
      nomalUsers.forEach((user) -> {
         sysIdentityList.add(new DefaultIdentity(user.getId(), user.getName(), "user", user.getOrgId()));
      });
      if (sysIdentityList.size() > 0) {
         bpmTask.setTaskType(TaskType.NORMAL.getKey());
         this.taskIdentityLinkManager.createIdentityLink(bpmTask, sysIdentityList);
      }

   }

   private void writeUserAgencyLog(BpmUserAgencyConfig bpmUserAgencyConfig, BpmTask bpmTask) {
      BpmUserAgencyLog bpmUserAgencyLog = new BpmUserAgencyLog();
      bpmUserAgencyLog.setConfigId(bpmUserAgencyConfig.getId());
      bpmUserAgencyLog.setFlowInstanceId(bpmTask.getInstId());
      bpmUserAgencyLog.setTaskId(bpmTask.getId());
      bpmUserAgencyLog.setTaskNodeId(bpmTask.getNodeId());
      bpmUserAgencyLog.setTaskNodeName(bpmTask.getName());
      this.bpmUserAgencyLogManager.insertSelective(bpmUserAgencyLog);
   }
}
