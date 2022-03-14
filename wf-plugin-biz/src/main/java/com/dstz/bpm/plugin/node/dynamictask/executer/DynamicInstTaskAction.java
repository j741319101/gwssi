package com.dstz.bpm.plugin.node.dynamictask.executer;

import com.dstz.bpm.act.util.ActivitiUtil;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.GateWayBpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DynamicInstTaskAction extends DefaultExtendTaskAction {
   @Resource
   DynamicTaskManager dynamicTaskManager;
   @Resource
   BpmProcessDefService bpmProcessDefService;
   @Resource
   BpmInstanceManager bpmInstanceManager;
   private final ThreadLocal<BpmTask> taskThreadLocal = new ThreadLocal();
   private static ThreadLocal<Map<String, ArrayDeque<Integer>>> threadDynamictaskIndex = new ThreadLocal();
   private static ThreadLocal<Map<String, ArrayDeque<List<SysIdentity>>>> threadDynamictaskIdentities = new ThreadLocal();

   public void dynamicInstTaskComplate(DefaultBpmTaskPluginSession pluginSession, BpmNodeDef instBpmNodeDef) {
      BpmInstance childBpmInstance = (BpmInstance)pluginSession.getBpmInstance();
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
      String parentInstId = "";
      String superNodeId = "";
      if (instBpmNodeDef.getType() == NodeType.CALLACTIVITY) {
         parentInstId = childBpmInstance.getParentInstId();
         superNodeId = childBpmInstance.getSuperNodeId();
      } else {
         parentInstId = childBpmInstance.getId();
         superNodeId = instBpmNodeDef.getParentBpmNodeDef().getNodeId();
      }

      IBpmTask childTask = pluginSession.getBpmTask();
      AtomicBoolean isEndNode = new AtomicBoolean(false);
      String[] destinationNodes = model.getDestinations();
      List dynamicTasks;
      if (destinationNodes != null && destinationNodes.length > 0) {
         Arrays.asList(destinationNodes).forEach((destinationNodeId) -> {
            BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(childBpmInstance.getDefId(), destinationNodeId);
            if (bpmNodeDef.getType() == NodeType.END) {
               isEndNode.set(true);
            }

         });
      } else {
         BpmNodeDef bpmNodeDefEnd = this.bpmProcessDefService.getBpmNodeDef(childBpmInstance.getDefId(), childTask.getNodeId());
         dynamicTasks = bpmNodeDefEnd.getOutcomeNodes();
         ((List<BpmNodeDef>)dynamicTasks).forEach((outNode) -> {
            if (outNode.getType() == NodeType.END) {
               isEndNode.set(true);
            }

         });
      }

      if (isEndNode.get()) {
         QueryFilter queryFilter = new DefaultQueryFilter();
         queryFilter.addFilter("inst_id_", parentInstId, QueryOP.EQUAL);
         queryFilter.addFilter("node_id_", superNodeId, QueryOP.EQUAL);
         queryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
         dynamicTasks = this.dynamicTaskManager.query(queryFilter);
         if (dynamicTasks == null || dynamicTasks.size() == 0) {
            return;
         }

         DynamicTask dynamicTask = (DynamicTask)dynamicTasks.get(0);
         dynamicTask.setCurrentIndex(dynamicTask.getCurrentIndex() + 1);
         if (dynamicTask.getCurrentIndex() >= dynamicTask.getAmmount()) {
            dynamicTask.setStatus("completed");
            this.dynamicTaskManager.update(dynamicTask);
            return;
         }

         BpmInstance parentInstance = (BpmInstance)this.bpmInstanceManager.get(parentInstId);
         BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(parentInstance.getDefId(), superNodeId);
         String[] destinations = new String[0];
         if (!DynamicTaskPluginExecuter.isParallel(bpmNodeDef)) {
            destinations = new String[]{superNodeId};
         }

         ActivitiUtil.skipPrepare(parentInstance.getActDefId(), superNodeId, destinations);
         this.dynamicTaskManager.update(dynamicTask);
      }

   }

   BpmNodeDef getDynamicInstNodeDef(DefaultBpmTaskPluginSession pluginSession) {
      BpmInstance bpmInstance = (BpmInstance)pluginSession.getBpmInstance();
      IBpmTask bpmTask = pluginSession.getBpmTask();
      BpmNodeDef instNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), bpmTask.getNodeId());
      BpmNodeDef subInstNodeDef = instNodeDef.getParentBpmNodeDef();
      if (subInstNodeDef != null && subInstNodeDef instanceof SubProcessNodeDef) {
         return instNodeDef;
      } else {
         String parentInstId = bpmInstance.getParentInstId();
         String superNodeId = bpmInstance.getSuperNodeId();
         BpmNodeDef bpmNodeDef = null;
         if (StringUtil.isNotEmpty(parentInstId)) {
            IBpmInstance parentInst = (IBpmInstance)this.bpmInstanceManager.get(parentInstId);
            if (parentInst != null) {
               bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(parentInst.getDefId(), superNodeId);
            }
         }

         return bpmNodeDef;
      }
   }

   public boolean isBeginDynamicInstTask(BpmNodeDef bpmNodeDef, DefaultBpmTaskPluginSession pluginSession, boolean isPreDy) {
      AtomicReference<Boolean> isEnabled = new AtomicReference(false);
      if (bpmNodeDef == null) {
         return false;
      } else {
         BaseActionCmd baseActionCmd = (BaseActionCmd)BpmContext.getActionModel();
         BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
         if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
            subProcessNodeDef.getBpmPluginContexts().forEach((bpmPluginContext) -> {
               if (bpmPluginContext instanceof DynamicTaskPluginContext) {
                  isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
               }

            });
            if ((Boolean)isEnabled.get()) {
               BpmProcessDef bpmProcessDef = ((SubProcessNodeDef)subProcessNodeDef).getChildBpmProcessDef();
               String fromTaskName = (String)pluginSession.get("submitTaskName");
               if (StringUtils.isEmpty(fromTaskName)) {
                  fromTaskName = baseActionCmd.getDynamicSubmitTaskName();
               }

               if (StringUtils.isNotEmpty(fromTaskName)) {
                 /* bpmProcessDef.getBpmnNodeDefs().forEach((innerNodeDef) -> {
                     if (StringUtils.equals(fromTaskName, innerNodeDef.getName())) {
                        isEnabled.set(false);
                     }

                  });*/
                  for (BpmNodeDef innerNodeDef : bpmProcessDef.getBpmnNodeDefs()) {
                     if (StringUtils.equals(fromTaskName, innerNodeDef.getName())) {
                        isEnabled.set(false);
                     }
                  }
               } else {
                  isEnabled.set(false);
               }
            }
         }

         if (StringUtils.equals(bpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
            DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class);
            if (dynamicTaskPluginContext != null && ((DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled()) {
               if (isPreDy) {
                  isEnabled.set(true);
               } else {
                  IBpmTask bpmTask = pluginSession.getBpmTask();
                  BpmNodeDef currentNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
                  List<BpmNodeDef> bpmNodeDefs = this.bpmProcessDefService.getStartNodes(bpmTask.getDefId());
                  bpmNodeDefs.forEach((bpmNodeDef1x) -> {
                     if (StringUtils.equals(currentNodeDef.getNodeId(), bpmNodeDef1x.getNodeId())) {
                        isEnabled.set(true);
                     }

                  });
                  String compareName = baseActionCmd.getDynamicSubmitTaskName();
                  if (StringUtil.isEmpty(compareName)) {
                     return false;
                  }

                  Iterator var12 = this.bpmProcessDefService.getBpmProcessDef(bpmTask.getDefId()).getBpmnNodeDefs().iterator();

                  while(var12.hasNext()) {
                     BpmNodeDef bpmNodeDef1 = (BpmNodeDef)var12.next();
                     if (StringUtils.equals(compareName, bpmNodeDef1.getName())) {
                        isEnabled.set(false);
                     }
                  }
               }
            }
         }

         return (Boolean)isEnabled.get();
      }
   }

   public boolean isEndDynamicInstTask(BpmNodeDef bpmNodeDef, DefaultBpmTaskPluginSession pluginSession) {
      AtomicReference<Boolean> isEnabled = new AtomicReference(false);
      if (bpmNodeDef == null) {
         return false;
      } else {
         BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
         if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
            subProcessNodeDef.getBpmPluginContexts().forEach((bpmPluginContext) -> {
               if (bpmPluginContext instanceof DynamicTaskPluginContext) {
                  isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
               }

            });
            if ((Boolean)isEnabled.get()) {
               BpmProcessDef bpmProcessDef = ((SubProcessNodeDef)subProcessNodeDef).getChildBpmProcessDef();
               isEnabled.set(false);
               if (!(Boolean)isEnabled.get()) {
                  DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
                  String[] destinations = model.getDestinations();
                  if (destinations != null) {
                     if (destinations.length == 1) {
                        BpmNodeDef endBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), destinations[0]);
                        if (endBpmNodeDef.getType() == NodeType.END) {
                           isEnabled.set(true);
                        }
                     }
                  } else {
                     List<BpmNodeDef> bpmNodeDefs = bpmNodeDef.getOutcomeNodes();
                     if (bpmNodeDefs != null && bpmNodeDefs.size() == 1) {
                        bpmNodeDefs.forEach((endNode) -> {
                           if (endNode.getType() == NodeType.END) {
                              isEnabled.set(true);
                           }

                        });
                     }
                  }
               }
            }
         }

         if (StringUtils.equals(bpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
            bpmNodeDef.getBpmPluginContexts().forEach((bpmPluginContext) -> {
               if (bpmPluginContext instanceof DynamicTaskPluginContext) {
                  isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
               }

            });
         }

         return (Boolean)isEnabled.get();
      }
   }

   public static boolean isDynamicTask(BpmNodeDef bpmNodeDef) {
      AtomicReference<Boolean> isEnabled = new AtomicReference(false);
      if (bpmNodeDef != null && !(bpmNodeDef instanceof GateWayBpmNodeDef) && !(bpmNodeDef instanceof CallActivityNodeDef)) {
         bpmNodeDef.getBpmPluginContexts().forEach((bpmPluginContext) -> {
            if (bpmPluginContext instanceof DynamicTaskPluginContext) {
               isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
            }

         });
         return (Boolean)isEnabled.get();
      } else {
         return false;
      }
   }

   public static boolean isDynamicInstTask(BpmNodeDef bpmNodeDef) {
      AtomicReference<Boolean> isEnabled = new AtomicReference(false);
      if (bpmNodeDef == null) {
         return false;
      } else {
         BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
         if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
            subProcessNodeDef.getBpmPluginContexts().forEach((bpmPluginContext) -> {
               if (bpmPluginContext instanceof DynamicTaskPluginContext) {
                  isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
               }

            });
         }

         if (StringUtils.equals(bpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
            bpmNodeDef.getBpmPluginContexts().forEach((bpmPluginContext) -> {
               if (bpmPluginContext instanceof DynamicTaskPluginContext) {
                  isEnabled.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsEnabled());
               }

            });
         }

         return (Boolean)isEnabled.get();
      }
   }

   public void canFreeJump(IBpmTask bpmTask) {
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
      if (isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getNodeId())) && !isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getDestination()))) {
         throw new BusinessException("动态子流程暂不适用跳转到子流程外部节点");
      } else if (!isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getNodeId())) && isDynamicInstTask(this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), model.getDestination()))) {
         throw new BusinessException("动态子流程暂不适用外部节点跳转到子流程");
      }
   }

   public void checkEvent(DefaultBpmTaskPluginSession pluginSession) {
      BpmNodeDef instBpmNodeDef = this.getDynamicInstNodeDef(pluginSession);
      List<String> keys = Arrays.asList(ActionType.AGREE.getKey(), ActionType.OPPOSE.getKey(), ActionType.SIGNOPPOSE.getKey(), ActionType.SIGNAGREE.getKey(), ActionType.TASK_FREE_JUMP.getKey(), ActionType.CREATE.getKey(), ActionType.DECREASEDYNAMIC.getKey(), ActionType.INCREASEDYNAMIC.getKey(), ActionType.START.getKey(), ActionType.RECALL.getKey(), ActionType.REJECT.getKey(), ActionType.INSTANCE_RESTART.getKey());
      if (isDynamicInstTask(instBpmNodeDef) && !CollectionUtil.contains(keys, pluginSession.get("submitActionName").toString())) {
         throw new BusinessException(String.format("动态多实例任务暂不适用%s操作", pluginSession.get("submitActionDesc")));
      }
   }

   public void deleteDataByInstId(String instId) {
      this.dynamicTaskManager.removeByInstId(instId);
   }

   public BpmTask getTaskThreadLocal() {
      return (BpmTask)this.taskThreadLocal.get();
   }

   public void setTaskThreadLocal(BpmTask bpmTask) {
      this.taskThreadLocal.set(bpmTask);
   }

   public void clearTaskThreadLocal() {
      this.taskThreadLocal.remove();
   }

   public void clearIdentities() {
      threadDynamictaskIdentities.remove();
      threadDynamictaskIndex.remove();
   }

   public static void pushDynamictaskIndex(String nodeId, Integer index) {
      Map<String, ArrayDeque<Integer>> arrayDequeMap = (Map)threadDynamictaskIndex.get();
      if (arrayDequeMap == null) {
         arrayDequeMap = new HashMap();
         threadDynamictaskIndex.set(arrayDequeMap);
      }

      ArrayDeque<Integer> arrayDeque = (ArrayDeque)((Map)arrayDequeMap).get(nodeId);
      if (arrayDeque == null) {
         arrayDeque = new ArrayDeque();
         ((Map)arrayDequeMap).put(nodeId, arrayDeque);
      }

      arrayDeque.add(index);
   }

   public static Integer popDynamictaskIndex(String nodeId) {
      Map<String, ArrayDeque<Integer>> arrayDequeMap = (Map)threadDynamictaskIndex.get();
      if (arrayDequeMap == null) {
         return null;
      } else {
         ArrayDeque<Integer> arrayDeque = (ArrayDeque)arrayDequeMap.get(nodeId);
         return arrayDeque.isEmpty() ? null : (Integer)arrayDeque.poll();
      }
   }

   public static void pushDynamictaskIdentities(String nodeId, List<SysIdentity> sysIdentity) {
      Map<String, ArrayDeque<List<SysIdentity>>> arrayDequeMap = (Map)threadDynamictaskIdentities.get();
      if (CollectionUtil.isEmpty((Map)arrayDequeMap)) {
         arrayDequeMap = new HashMap();
         threadDynamictaskIdentities.set(arrayDequeMap);
      }

      ArrayDeque<List<SysIdentity>> arrayDeque = (ArrayDeque)((Map)arrayDequeMap).get(nodeId);
      if (arrayDeque == null) {
         arrayDeque = new ArrayDeque();
         ((Map)arrayDequeMap).put(nodeId, arrayDeque);
      }

      arrayDeque.add(sysIdentity);
   }

   public static List<SysIdentity> popDynamictaskIdentities(String nodeId) {
      Map<String, ArrayDeque<List<SysIdentity>>> arrayDequeMap = (Map)threadDynamictaskIdentities.get();
      if (CollectionUtil.isEmpty(arrayDequeMap)) {
         return null;
      } else {
         ArrayDeque<List<SysIdentity>> arrayDeque = (ArrayDeque)arrayDequeMap.get(nodeId);
         return arrayDeque.isEmpty() ? null : (List)arrayDeque.poll();
      }
   }

   public boolean canRecall(IBpmTask callTask) {
      BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(callTask.getInstId());
      BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), callTask.getNodeId());
      DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class);
      DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
      if (dynamicTaskPluginDef.getIsEnabled()) {
         return false;
      } else {
         BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
         if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
            DynamicTaskPluginContext supDynamicTaskPluginContext = (DynamicTaskPluginContext)subProcessNodeDef.getPluginContext(DynamicTaskPluginContext.class);
            DynamicTaskPluginDef supDynamicTaskPluginDef = (DynamicTaskPluginDef)supDynamicTaskPluginContext.getBpmPluginDef();
            if (supDynamicTaskPluginDef.getIsEnabled()) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean canReject(IBpmTask curTask, IBpmTask rejectTask) {
      if (!StringUtils.equals(rejectTask.getInstId(), curTask.getInstId())) {
         return false;
      } else {
         BpmInstance curBpmInstance = (BpmInstance)this.bpmInstanceManager.get(curTask.getInstId());
         BpmNodeDef curBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(curBpmInstance.getDefId(), curTask.getNodeId());
         DynamicTaskPluginContext curDynamicTaskPluginContext = (DynamicTaskPluginContext)curBpmNodeDef.getPluginContext(DynamicTaskPluginContext.class);
         if (((DynamicTaskPluginDef)curDynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled()) {
            return false;
         } else {
            BpmInstance callBpmInstance = (BpmInstance)this.bpmInstanceManager.get(rejectTask.getInstId());
            BpmNodeDef callBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(callBpmInstance.getDefId(), rejectTask.getNodeId());
            DynamicTaskPluginContext callDynamicTaskPluginContext = (DynamicTaskPluginContext)callBpmNodeDef.getPluginContext(DynamicTaskPluginContext.class);
            if (((DynamicTaskPluginDef)callDynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled()) {
               return false;
            } else {
               BpmNodeDef curSubProcessNodeDef = curBpmNodeDef.getParentBpmNodeDef();
               BpmNodeDef callSubProcessNodeDef = callBpmNodeDef.getParentBpmNodeDef();
               DynamicTaskPluginContext dynamicTaskPluginContext;
               if (curSubProcessNodeDef != null && curSubProcessNodeDef instanceof SubProcessNodeDef) {
                  dynamicTaskPluginContext = (DynamicTaskPluginContext)curSubProcessNodeDef.getPluginContext(DynamicTaskPluginContext.class);
                  if (((DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled() && (callSubProcessNodeDef == null || callSubProcessNodeDef.getNodeId() != curSubProcessNodeDef.getNodeId())) {
                     return false;
                  }
               }

               if (callSubProcessNodeDef != null && callSubProcessNodeDef instanceof SubProcessNodeDef) {
                  dynamicTaskPluginContext = (DynamicTaskPluginContext)callSubProcessNodeDef.getPluginContext(DynamicTaskPluginContext.class);
                  if (((DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef()).getIsEnabled() && (curSubProcessNodeDef == null || callSubProcessNodeDef.getNodeId() != curSubProcessNodeDef.getNodeId())) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }
}
