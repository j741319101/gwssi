package com.dstz.bpm.plugin.node.recrease.executer;

import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.inst.BpmExecutionStack;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
//import com.dstz.bpm.plugin.global.leaderTask.context.LeaderTaskPluginContext;
//import com.dstz.bpm.plugin.global.leaderTask.def.LeaderTaskPluginDef;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.dto.UserDTO;
//import com.dstz.org.api.service.LeaderService;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SuperviseTaskExecuter {
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;
   @Resource
   private BpmProcessDefService bpmProcessDefService;
   @Resource
   private BpmTaskManager bpmTaskManager;
   private ThreadLocal<Boolean> execForce = new ThreadLocal();
   private final ThreadLocal<Boolean> isNeedSupervise = new ThreadLocal();

   public void createTask(DefaultBpmTaskPluginSession pluginSession, String type) {
      BpmTask task = (BpmTask)pluginSession.getBpmTask();
      BpmExecutionStack prevBpmExecutionStack = BpmContext.getThreadDynamictaskStack(task.getNodeId());
      if (!StringUtils.equals(task.getTaskType(), TaskType.SUPERVISE.getKey()) && this.getIsNeedSupervise()) {
         task.setTaskType(TaskType.SUPERVISE.getKey());
         this.bpmTaskManager.update(task);
         this.setIsNeedSupervise(false);
         if (prevBpmExecutionStack != null && StringUtils.equals("dynamic", type)) {
            BpmExecutionStack bpmExecutionStack = this.bpmTaskStackManager.getByTaskId(task.getId());
            Map<String, BpmExecutionStack> map = BpmContext.getAllThreadDynamictaskStack();
            if (CollectionUtil.isNotEmpty(map)) {
               map.forEach((key, stack) -> {
                  stack.setId(bpmExecutionStack.getId());
               });
            }
         }
      }

   }

   public List<String> preTaskComplete(DefaultBpmTaskPluginSession pluginSession) {
      BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
      if (StringUtils.equals(bpmTask.getTaskType(), TaskType.SUPERVISE.getKey())) {
         QueryFilter queryFilter = new DefaultQueryFilter();
         queryFilter.addParamsFilter("taskId", bpmTask.getId());
         queryFilter.addParamsFilter("prior", "FORWARD");
         queryFilter.addFilter("end_time", (Object)null, QueryOP.IS_NULL);
         queryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
         queryFilter.addFilter("level", 1, QueryOP.GREAT);
         queryFilter.addFilter("level", 5, QueryOP.LESS);
         List aliveStack = this.bpmTaskStackManager.getTaskStackByIteration(queryFilter);
         if (CollectionUtil.isNotEmpty(aliveStack)) {
            Boolean exec = (Boolean)this.execForce.get();
            if (exec == null || !exec) {
               throw new BusinessException("需监管的任务还未完成");
            }
         }
      }

      BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
      BaseActionCmd model = (BaseActionCmd)BpmContext.getActionModel();
      String[] destinations = model.getDestinations();
      List<String> typeSupervise = new ArrayList();
      if (destinations == null) {
         return typeSupervise;
      } else {
         String[] var7 = destinations;
         int var8 = destinations.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String destination = var7[var9];
            BpmNodeDef superviseNode = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), destination);
            BpmNodeDef subProcessNodeDef = superviseNode.getParentBpmNodeDef();
            SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)superviseNode.getPluginContext(SignTaskPluginContext.class);
            SignTaskPluginDef signTaskPluginDef = null;
            if (signTaskPluginContext != null) {
               signTaskPluginDef = (SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef();
            }

            DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)superviseNode.getPluginContext(DynamicTaskPluginContext.class);
            DynamicTaskPluginDef dynamicTaskPluginDef = null;
            if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
               dynamicTaskPluginContext = (DynamicTaskPluginContext)subProcessNodeDef.getPluginContext(DynamicTaskPluginContext.class);
            }

            if (dynamicTaskPluginContext != null) {
               dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
            }

            if (signTaskPluginDef != null && signTaskPluginDef.isSignMultiTask() && signTaskPluginDef.isNeedSupervise() || dynamicTaskPluginDef != null && dynamicTaskPluginDef.getEnabled() && dynamicTaskPluginDef.isNeedSupervise()) {
               if (subProcessNodeDef == null) {
                  if (CollectionUtil.isNotEmpty(superviseNode.getOutcomeNodes())) {
                     throw new BusinessException("受监管的任务不能有后续任务");
                  }
               } else if (dynamicTaskPluginDef.getEnabled()) {
                  if (CollectionUtil.isNotEmpty(subProcessNodeDef.getOutcomeNodes())) {
                     throw new BusinessException("受监管的任务不能有后续任务");
                  }
               } else if (CollectionUtil.isNotEmpty(superviseNode.getOutcomeNodes())) {
                  throw new BusinessException("受监管的任务不能有后续任务");
               }

               if (signTaskPluginDef.isSignMultiTask() && signTaskPluginDef.isNeedSupervise()) {
                  typeSupervise.add("sign");
               }

               if (dynamicTaskPluginDef.getEnabled() && dynamicTaskPluginDef.isNeedSupervise()) {
                  typeSupervise.add("dynamic");
               }

               IUser user = ContextUtil.getCurrentUser();
//               if (!StringUtils.equals(bpmTask.getAssigneeId(), "0") && !StringUtils.equals(((IUser)user).getUserId(), bpmTask.getAssigneeId())) {
//                  LeaderService leaderService = (LeaderService)AppUtil.getImplInstanceArray(LeaderService.class).get(0);
//                  LeaderTaskPluginContext leaderTaskPluginContext = (LeaderTaskPluginContext)bpmNodeDef.getPluginContext(LeaderTaskPluginContext.class);
//                  if (((LeaderTaskPluginDef)leaderTaskPluginContext.getBpmPluginDef()).isSignLeaderTask()) {
//                     IUser secretary = leaderService.getUserByLeaderId(bpmTask.getAssigneeId());
//                     if (secretary != null && StringUtils.equals(secretary.getUserId(), ((IUser)user).getUserId())) {
//                        user = new UserDTO();
//                        ((UserDTO)user).setId(bpmTask.getAssigneeId());
//                        ((UserDTO)user).setFullname(bpmTask.getAssigneeNames());
//                        ((UserDTO)user).setSn(0);
//                     }
//                  }
//               } todo 注释掉领导任务

               TaskIdentityLink taskIdentityLink = pluginSession.getTaskIdentityLink();
               String orgId = model.getApproveOrgId();
               if (taskIdentityLink != null) {
                  orgId = taskIdentityLink.getOrgId();
               }

               model.setBpmIdentity(bpmTask.getNodeId(), Arrays.asList(new DefaultIdentity(((IUser)user).getUserId(), ((IUser)user).getFullname(), "user", orgId)));
               this.setIsNeedSupervise(true);
            }
         }

         return typeSupervise;
      }
   }

   public boolean taskComplete(DefaultBpmTaskPluginSession pluginSession) {
      BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
      if (StringUtils.equals(bpmTask.getTaskType(), TaskType.SUPERVISE.getKey())) {
         QueryFilter queryFilter = new DefaultQueryFilter();
         queryFilter.addParamsFilter("taskId", bpmTask.getId());
         queryFilter.addParamsFilter("prior", "FORWARD");
         queryFilter.addFilter("end_time", (Object)null, QueryOP.IS_NULL);
         queryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
         queryFilter.addFilter("level", 1, QueryOP.GREAT);
         queryFilter.addFilter("level", 5, QueryOP.LESS);
         List aliveStack = this.bpmTaskStackManager.getTaskStackByIteration(queryFilter);
         if (CollectionUtil.isNotEmpty(aliveStack)) {
            throw new BusinessException("需监管的任务还未完成");
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public void setIsNeedSupervise(Boolean need) {
      this.isNeedSupervise.set(need);
   }

   public boolean getIsNeedSupervise() {
      Boolean b = (Boolean)this.isNeedSupervise.get();
      return b == null ? false : b;
   }

   public void clearSupervise() {
      this.isNeedSupervise.remove();
   }

   public Boolean getExecForce() {
      return (Boolean)this.execForce.get();
   }

   public void setExecForce(Boolean exec) {
      this.execForce.set(exec);
   }

   public void clearExecForce() {
      this.execForce.remove();
   }
}
