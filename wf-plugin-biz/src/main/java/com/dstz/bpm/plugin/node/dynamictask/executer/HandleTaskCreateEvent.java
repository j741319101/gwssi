package com.dstz.bpm.plugin.node.dynamictask.executer;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
import com.dstz.bpm.api.model.inst.BpmExecutionStack;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class HandleTaskCreateEvent {
   @Resource
   private DynamicTaskManager dynamicTaskManager;
   @Resource
   private BpmTaskManager bpmTaskManager;
   @Resource
   private IGroovyScriptEngine scriptEngine;
   @Resource
   private HandelRejectAction handelRejectAction;
   @Resource
   private DynamicInstTaskAction dynamicInstTaskActions;
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;
   @Resource
   private SuperviseTaskExecuter superviseTaskExecuter;

   public void postTaskCreateEvent(DefaultBpmTaskPluginSession pluginSession) {
      this.superviseTaskExecuter.createTask(pluginSession, "dynamic");
   }

   public void taskCreateEvent(DynamicTaskPluginDef pluginDef, DefaultBpmTaskPluginSession pluginSession) {
      BpmTask task = (BpmTask)pluginSession.getBpmTask();
      BpmExecutionStack prevBpmExecutionStack = BpmContext.getThreadDynamictaskStack(task.getNodeId());
      if (this.superviseTaskExecuter.getIsNeedSupervise() && prevBpmExecutionStack != null) {
         this.dynamicInstTaskActions.setTaskThreadLocal(task);
      } else {
         DefualtTaskActionCmd taskActionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
         BpmTask prepareTask = this.dynamicInstTaskActions.getTaskThreadLocal();
         boolean isEnabled = pluginDef.getIsEnabled();
         if (StringUtils.equals("追加", taskActionCmd.getDoActionName())) {
            isEnabled = false;
         }

         BpmNodeDef instBpmNodeDef = this.dynamicInstTaskActions.getDynamicInstNodeDef(pluginSession);
         boolean isInstDy = false;
         if (!isEnabled) {
            isInstDy = this.dynamicInstTaskActions.isBeginDynamicInstTask(instBpmNodeDef, pluginSession, false);
            isEnabled = isInstDy;
         }

         if (isEnabled) {
            BpmInstance bpmInstance = (BpmInstance)pluginSession.getBpmInstance();
            String parentInstId = bpmInstance.getParentInstId();
            String superNodeId = bpmInstance.getSuperNodeId();
            if (isInstDy && instBpmNodeDef.getType() != NodeType.CALLACTIVITY) {
               BpmNodeDef subProcessNodeDef = instBpmNodeDef.getParentBpmNodeDef();
               parentInstId = bpmInstance.getId();
               superNodeId = subProcessNodeDef.getNodeId();
            }

            ActionCmd cmd = BpmContext.submitActionModel();
            if (BpmContext.submitActionModel().getActionName().equals(ActionType.REJECT.getKey())) {
               if (isInstDy) {
                  return;
               }

               DefualtTaskActionCmd submitActionCmd = (DefualtTaskActionCmd)cmd;
               if (!submitActionCmd.getBpmTask().getNodeId().equals(task.getNodeId())) {
                  this.handelRejectAction.handelBack2Tasks(task, taskActionCmd);
                  return;
               }
            }

            QueryFilter queryFilter = new DefaultQueryFilter();
            if (isInstDy) {
               queryFilter.addFilter("inst_id_", parentInstId, QueryOP.EQUAL);
               queryFilter.addFilter("node_id_", superNodeId, QueryOP.EQUAL);
            } else {
               queryFilter.addFilter("inst_id_", task.getInstId(), QueryOP.EQUAL);
               queryFilter.addFilter("node_id_", task.getNodeId(), QueryOP.EQUAL);
            }

            queryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
            if (prepareTask != null) {
               queryFilter.addFilter("task_id_", prepareTask.getId(), QueryOP.EQUAL);
            }

            List<DynamicTask> dynamicTasks = this.dynamicTaskManager.query(queryFilter);
            DynamicTask dynamicTask = dynamicTasks.size() == 0 ? null : (DynamicTask)dynamicTasks.get(0);
            boolean isParallel = pluginDef.getIsParallel();
            if (dynamicTask == null) {
               dynamicTask = this.genDynamicTask(pluginDef, taskActionCmd, task, pluginSession);
            }

            if (isInstDy) {
               isParallel = DynamicTaskPluginExecuter.isParallel(instBpmNodeDef);
               dynamicTask.setNodeId(superNodeId);
               dynamicTask.setInstId(parentInstId);
            }

            dynamicTask.setIsParallel(isParallel);
            if (prepareTask != null) {
               dynamicTask.setTaskId(prepareTask.getId());
            }

            if (isParallel) {
               String nodeId = task.getNodeId();
               if (isInstDy && instBpmNodeDef instanceof CallActivityNodeDef) {
                  nodeId = superNodeId;
               }

               Integer currentIndex = DynamicInstTaskAction.popDynamictaskIndex(nodeId);
               if (currentIndex == null) {
                  throw new BusinessMessage("动态任务生成错误");
               }

               if (currentIndex == 0 && StringUtils.isEmpty(dynamicTask.getId())) {
                  this.dynamicTaskManager.create(dynamicTask);
               }

               List<SysIdentity> sysIdentities = new ArrayList();
               sysIdentities.addAll(DynamicInstTaskAction.popDynamictaskIdentities(nodeId));
               taskActionCmd.setBpmIdentity(task.getNodeId(), sysIdentities);
            } else {
               if (StringUtil.isEmpty(dynamicTask.getId())) {
                  this.dynamicTaskManager.create(dynamicTask);
               }

               DynamicTaskIdentitys taskIdentitys = dynamicTask.loadCurrentTaskIdentitys();
               taskActionCmd.setBpmIdentity(task.getNodeId(), taskIdentitys.getNodeIdentitys());
            }

         }
      }
   }

   private DynamicTask genDynamicTask(DynamicTaskPluginDef pluginDef, DefualtTaskActionCmd taskActionCmd, BpmTask task, DefaultBpmTaskPluginSession pluginSession) {
      if ("interface".equals(pluginDef.getDynamicType()) && StringUtil.isNotEmpty(pluginDef.getInterfaceName())) {
         try {
            List<DynamicTaskIdentitys> dynamicTaskIdentitys = (List)this.scriptEngine.executeObject(pluginDef.getInterfaceName(), pluginSession);
            if (CollectionUtil.isEmpty(dynamicTaskIdentitys)) {
               throw new BusinessException("动态任务生成失败，节点候选人不能为空！节点：" + task.getName());
            } else {
               DynamicTask dt = new DynamicTask(task);
               dt.setAmmount(dynamicTaskIdentitys.size());
               dt.setIsParallel(pluginDef.getIsParallel());
               dt.setIdentityNode(JSON.toJSONString(dynamicTaskIdentitys));
               return dt;
            }
         } catch (Exception var11) {
            throw new BusinessException("动态任务生成失败，自定义动态任务接口执行失败：" + var11.getMessage(), var11);
         }
      } else {
         List<SysIdentity> identitys = new ArrayList();
         List<List<SysIdentity>> identityLis = taskActionCmd.getDynamicBpmIdentity(task.getNodeId());
         DynamicTask dynamicTask = new DynamicTask(task);
         dynamicTask.setIsParallel(pluginDef.getIsParallel());
         if (!CollectionUtils.isNotEmpty(identityLis)) {
            List<SysIdentity> sysIdentities = taskActionCmd.getBpmIdentity(task.getNodeId());
            if (CollectionUtil.isNotEmpty(sysIdentities)) {
               identitys.addAll(sysIdentities);
            }

            if (CollectionUtil.isEmpty(identitys)) {
               throw new BusinessException("动态任务生成失败，节点候选人不能为空！节点：" + task.getName());
            } else {
               return new DynamicTask(task, identitys, pluginDef.getIsParallel());
            }
         } else {
            identityLis.forEach((identityLi) -> {
               identitys.addAll(identityLi);
            });
            dynamicTask.setAmmount(identityLis.size());
            List<DynamicTaskIdentitys> identityNodes = new ArrayList();

            for(int i = 0; i < identityLis.size(); ++i) {
               List<SysIdentity> list = new ArrayList(1);
               list.addAll((Collection)identityLis.get(i));
               identityNodes.add(new DynamicTaskIdentitys(String.format("%s-%d", task.getName(), i + 1), list));
            }

            dynamicTask.setIdentityNode(JSON.toJSONString(identityNodes));
            return dynamicTask;
         }
      }
   }
}
