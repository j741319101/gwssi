package com.dstz.bpm.plugin.node.sign.executer;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.TaskSkipType;
import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.plugin.cmd.TaskCommand;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.base.core.id.IdUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignTaskPluginExecuter extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, SignTaskPluginDef> {
   @Autowired
   private TaskIdentityLinkManager taskIdentityLinkManager;
   @Autowired
   private BpmTaskManager bpmTaskManager;
   @Autowired
   private UserService userService;
   @Autowired
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Autowired
   private TaskCommand taskCommand;
   @Resource
   private SuperviseTaskExecuter superviseTaskExecuter;
   @Resource
   private BpmProcessDefService bpmProcessDefService;

   public Void execute(DefaultBpmTaskPluginSession pluginSession, SignTaskPluginDef pluginDef) {
      if (pluginSession.getEventType() == EventType.TASK_PRE_COMPLETE_EVENT) {
         this.preTaskComplete(pluginSession);
      } else if (pluginSession.getEventType() == EventType.TASK_POST_CREATE_EVENT) {
         this.postCreateTask(pluginSession, pluginDef);
      } else if (pluginSession.getEventType() == EventType.TASK_CREATE_EVENT) {
         this.createTask(pluginSession);
      }

      return null;
   }

   private void preTaskComplete(DefaultBpmTaskPluginSession pluginSession) {
      this.superviseTaskExecuter.taskComplete(pluginSession);
      List<String> typeSupervise = this.superviseTaskExecuter.preTaskComplete(pluginSession);
      if (CollectionUtil.isNotEmpty(typeSupervise) && typeSupervise.contains("sign")) {
         BaseActionCmd model = (BaseActionCmd)BpmContext.getActionModel();
         BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
         String[] destinations = model.getDestinations();
         if (destinations != null && destinations.length > 0 && !Arrays.asList(destinations).contains(bpmTask.getNodeId())) {
            String[] newDestinations = new String[destinations.length + 1];
            newDestinations[0] = bpmTask.getNodeId();

            for(int i = 0; i < destinations.length; ++i) {
               newDestinations[i + 1] = destinations[i];
            }

            model.setDestinations(newDestinations);
         }
      }

   }

   private void createTask(DefaultBpmTaskPluginSession pluginSession) {
      BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
      BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
      SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)((SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class)).getBpmPluginDef();
      if (signTaskPluginDef.isSignMultiTask()) {
         this.superviseTaskExecuter.createTask(pluginSession, "sign");
      }

   }

   private void postCreateTask(DefaultBpmTaskPluginSession pluginSession, SignTaskPluginDef pluginDef) {
      TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
      BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
      if (pluginDef.isSignMultiTask() && !StringUtils.equals(bpmTask.getTaskType(), "SIGN")) {
         this.taskCommand.execute(EventType.TASK_SIGN_CREATE_EVENT, model);
         if (model.isHasSkipThisTask() == null || model.isHasSkipThisTask() == TaskSkipType.NO_SKIP) {
            bpmTask.setTaskType(TaskType.SIGN_SOURCE.getKey());
            bpmTask.setAssigneeId((String)null);
            bpmTask.setAssigneeNames((String)null);
            this.bpmTaskManager.update(bpmTask);
            BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getTaskId());
            opinion.setAssignInfo("所有会签用户");
            this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
            List<SysIdentity> sysIdentities = this.extractBpmIdentity();
            JSON bpmTaskJson = (JSON)JSON.toJSON(bpmTask);
            Iterator var8 = sysIdentities.iterator();

            while(var8.hasNext()) {
               SysIdentity sysIdentity = (SysIdentity)var8.next();
               BpmTask task = (BpmTask)JSON.toJavaObject(bpmTaskJson, BpmTask.class);
               task.setId(IdUtil.getSuid());
               task.setTaskType(TaskType.SIGN.getKey());
               task.setStatus(bpmTask.getStatus());
               task.setParentId(bpmTask.getId());
               task.setAssigneeId(sysIdentity.getId());
               task.setAssigneeNames(sysIdentity.getName());
               List<SysIdentity> identityList = new ArrayList();
               identityList.add(sysIdentity);
               this.bpmTaskOpinionManager.createOpinion(task, pluginSession.getBpmInstance(), identityList, model.getOpinion(), model.getActionName(), model.getFormId(), bpmTask.getId(), opinion.getTrace(), opinion.getVersion());
               this.taskIdentityLinkManager.createIdentityLink(task, identityList);
               this.bpmTaskManager.create(task);
            }

            opinion.setCreateTime(new Date((new Date()).getTime() + 1000L));
            opinion.setTrace("");
            this.bpmTaskOpinionManager.update(opinion);
            this.taskCommand.execute(EventType.TASK_SIGN_POST_CREATE_EVENT, model);
         }
      }
   }

   private List<SysIdentity> extractBpmIdentity() {
      TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
      List<SysIdentity> results = new ArrayList();
      List<SysIdentity> identities = model.getBpmIdentity(model.getNodeId());
      if (identities != null) {
         Iterator var4 = identities.iterator();

         while(true) {
            while(var4.hasNext()) {
               SysIdentity bpmIdentity = (SysIdentity)var4.next();
               if ("user".equals(bpmIdentity.getType())) {
                  results.add(bpmIdentity);
               } else {
                  List<IUser> users = (List<IUser>) this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
                  Iterator var7 = users.iterator();

                  while(var7.hasNext()) {
                     IUser user = (IUser)var7.next();
                     results.add(new DefaultIdentity(user));
                  }
               }
            }

            return results;
         }
      } else {
         return results;
      }
   }
}
