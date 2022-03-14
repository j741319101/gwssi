package com.dstz.bpm.plugin.node.dynamictask.executer;

import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.inst.BpmExecutionStack;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
import com.dstz.bpm.plugin.usercalc.util.UserCalcPreview;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class HandlePrevDynamicTaskCreate {
   @Resource
   BpmProcessDefService bpmProcessDefService;
   @Resource
   DynamicInstTaskAction dynamicInstTaskAction;
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;
   @Resource
   private SuperviseTaskExecuter superviseTaskExecuter;

   public void prevDynamicTaskCreate(DefaultBpmTaskPluginSession pluginSession) {
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
      BpmTask task = (BpmTask)pluginSession.getBpmTask();
      String[] destinationNodes = model.getDestinations();
      Map<String, BpmNodeDef> bpmNodeDefMap = new HashMap();
      List<String> nodeIds = new ArrayList();
      Map<String, Boolean> isInstDy = new HashMap();
      List<String> noNodeIds = new ArrayList();
      int i;
      DynamicInstTaskAction var10000;
      if (destinationNodes != null) {
         String[] var9 = destinationNodes;
         i = destinationNodes.length;

         for(int var11 = 0; var11 < i; ++var11) {
            String destination = var9[var11];
            BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), destination);
            var10000 = this.dynamicInstTaskAction;
            if (DynamicInstTaskAction.isDynamicTask(bpmNodeDef)) {
               nodeIds.add(destination);
               bpmNodeDefMap.put(destination, bpmNodeDef);
            } else if (this.dynamicInstTaskAction.isBeginDynamicInstTask(bpmNodeDef, pluginSession, true)) {
               nodeIds.add(destination);
               isInstDy.put(destination, true);
               bpmNodeDefMap.put(destination, bpmNodeDef);
            } else {
               noNodeIds.add(destination);
            }
         }
      } else {
         List<BpmNodeDef> bpmNodeDefs = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId()).getOutcomeNodes();

         for(i = 0; i < bpmNodeDefs.size(); ++i) {
            BpmNodeDef bpmNodeDef = (BpmNodeDef)bpmNodeDefs.get(i);
            bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), bpmNodeDef.getNodeId());
            var10000 = this.dynamicInstTaskAction;
            if (DynamicInstTaskAction.isDynamicTask(bpmNodeDef)) {
               nodeIds.add(bpmNodeDef.getNodeId());
               bpmNodeDefMap.put(bpmNodeDef.getNodeId(), bpmNodeDef);
            } else if (this.dynamicInstTaskAction.isBeginDynamicInstTask(bpmNodeDef, pluginSession, true)) {
               nodeIds.add(bpmNodeDef.getNodeId());
               isInstDy.put(bpmNodeDef.getNodeId(), true);
               bpmNodeDefMap.put(bpmNodeDef.getNodeId(), bpmNodeDef);
            } else {
               noNodeIds.add(bpmNodeDef.getNodeId());
            }
         }
      }

      BpmExecutionStack bpmExecutionStack = this.bpmTaskStackManager.getByTaskId(task.getId());
      List<BpmExecutionStack> prevBpmExecutionStacks = new ArrayList();
      if (CollectionUtil.isNotEmpty(nodeIds)) {
         this.dynamicInstTaskAction.setTaskThreadLocal(task);
         Map<String, List<SysIdentity>> identitiesMap = new HashMap();
         Map<String, List<List<SysIdentity>>> lidentitieLstsMap = new HashMap();
         boolean isParallel = false;
         List<String> destinations = new ArrayList();
         Iterator var15 = nodeIds.iterator();

         List identities;
         while(var15.hasNext()) {
            String nodeId = (String)var15.next();
            BpmExecutionStack prevBpmExecutionStack = new BpmTaskStack();
            prevBpmExecutionStacks.add(prevBpmExecutionStack);
            identities = null;
            List<List<SysIdentity>> lidentitieLsts = null;
            BpmNodeDef bpmNodeDef = (BpmNodeDef)bpmNodeDefMap.get(nodeId);
            CopyOptions copyOptions = new CopyOptions();
            copyOptions.setIgnoreError(true);
            BeanUtil.copyProperties(bpmExecutionStack, prevBpmExecutionStack, copyOptions);
            prevBpmExecutionStack.setNodeId(nodeId);
            prevBpmExecutionStack.setInstId(task.getInstId());
            if (bpmNodeDef.getParentBpmNodeDef() != null && bpmNodeDef.getParentBpmNodeDef() instanceof SubProcessNodeDef) {
               prevBpmExecutionStack.setNodeId(bpmNodeDef.getParentBpmNodeDef().getNodeId());
               BpmContext.setThreadDynamictaskStack(bpmNodeDef.getParentBpmNodeDef().getNodeId(), prevBpmExecutionStack);
            } else {
               BpmContext.setThreadDynamictaskStack(nodeId, prevBpmExecutionStack);
            }

            if (isInstDy.get(nodeId) != null && (Boolean)isInstDy.get(nodeId)) {
               prevBpmExecutionStack.setNodeName(nodeId);
               if (!(bpmNodeDef instanceof CallActivityNodeDef)) {
                  identities = model.getBpmIdentity(nodeId);
                  lidentitieLsts = model.getDynamicBpmIdentity(nodeId);
                  if (CollectionUtil.isEmpty(identities) && CollectionUtil.isEmpty(lidentitieLsts)) {
                     identities = UserCalcPreview.calcNodeUsers(bpmNodeDef, model);
                  }
               } else {
                  model.setDynamicSubmitTaskName(bpmNodeDef.getName() + "-" + pluginSession.get("submitTaskName"));
                  CallActivityNodeDef callActivityNodeDef = (CallActivityNodeDef)bpmNodeDef;
                  List<BpmNodeDef> bpmNodeDefs = this.bpmProcessDefService.getStartNodes(this.bpmProcessDefService.getDefinitionByKey(callActivityNodeDef.getFlowKey()).getId());
                  if (bpmNodeDefs.size() != 1) {
                     throw new BusinessException("子流程开启动态多实例 启动节点后置任务要唯一");
                  }

                  Iterator var24 = bpmNodeDefs.iterator();

                  while(var24.hasNext()) {
                     BpmNodeDef bpmNodeDef1 = (BpmNodeDef)var24.next();
                     identities = model.getBpmIdentity(bpmNodeDef1.getNodeId());
                     lidentitieLsts = model.getDynamicBpmIdentity(bpmNodeDef1.getNodeId());
                     if (CollectionUtil.isNotEmpty(identities) || CollectionUtil.isNotEmpty(lidentitieLsts)) {
                        break;
                     }
                  }

                  if (CollectionUtil.isEmpty(identities) && CollectionUtil.isEmpty(lidentitieLsts)) {
                     identities = model.getBpmIdentity(((BpmNodeDef)bpmNodeDefs.get(0)).getNodeId());
                     if (CollectionUtil.isEmpty(identities)) {
                        DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd();
                        taskModel.setBizDataMap(model.getBizDataMap());
                        taskModel.setBusData(model.getBusData());
                        taskModel.setActionName("start");
                        identities = UserCalcPreview.calcNodeUsers((BpmNodeDef)bpmNodeDefs.get(0), taskModel);
                     }
                  }
               }
            } else {
               identities = model.getBpmIdentity(nodeId);
               lidentitieLsts = model.getDynamicBpmIdentity(nodeId);
               if (CollectionUtil.isEmpty(identities) && CollectionUtil.isEmpty(lidentitieLsts)) {
                  identities = UserCalcPreview.calcNodeUsers(bpmNodeDef, model);
                  model.setBpmIdentity(nodeId, identities);
               }
            }

            if (CollectionUtil.isNotEmpty(identities)) {
               identitiesMap.put(nodeId, identities);
            } else {
               if (!CollectionUtil.isNotEmpty(lidentitieLsts)) {
                  throw new BusinessException("任务【" + bpmNodeDef.getName() + "】预计算候选人为空，无法生成多实例任务");
               }

               lidentitieLstsMap.put(nodeId, lidentitieLsts);
            }

            if (DynamicTaskPluginExecuter.isParallel(bpmNodeDef)) {
               isParallel = true;
            }
         }

         if (CollectionUtil.isNotEmpty(noNodeIds)) {
            noNodeIds.forEach((noNode) -> {
               BpmExecutionStack prevNoBpmExecutionStack = new BpmTaskStack();
               CopyOptions copyOptions = new CopyOptions();
               copyOptions.setIgnoreError(true);
               BeanUtil.copyProperties(bpmExecutionStack, prevNoBpmExecutionStack, copyOptions);
               prevNoBpmExecutionStack.setNodeId(noNode);
               prevNoBpmExecutionStack.setInstId(task.getInstId());
               BpmContext.setThreadDynamictaskStack(noNode, prevNoBpmExecutionStack);
            });
         }

         if (isParallel) {
            List<String> typeSupervise = this.superviseTaskExecuter.preTaskComplete(pluginSession);
            if (CollectionUtil.isNotEmpty(typeSupervise) && typeSupervise.contains("dynamic")) {
               BpmExecutionStack superviseBpmExecutionStack = new BpmTaskStack();
               CopyOptions copyOptions = new CopyOptions();
               BeanUtil.copyProperties(prevBpmExecutionStacks.get(0), superviseBpmExecutionStack, copyOptions);
               superviseBpmExecutionStack.setNodeId(task.getNodeId());
               BpmContext.setThreadDynamictaskStack(task.getNodeId(), superviseBpmExecutionStack);
               destinations.add(task.getNodeId());
            }

            Iterator var35;
            String nodeId;

            if (CollectionUtil.isNotEmpty(lidentitieLstsMap)) {
               var35 = lidentitieLstsMap.keySet().iterator();

               label137:
               while(true) {
                  do {
                     if (!var35.hasNext()) {
                        break label137;
                     }

                     nodeId = (String)var35.next();
                  } while(!nodeIds.contains(nodeId));

                  identities = (List)lidentitieLstsMap.get(nodeId);

                  for(i = 0; i < identities.size(); ++i) {
                     destinations.add(nodeId);
                     DynamicInstTaskAction.pushDynamictaskIndex(nodeId, i);
                     DynamicInstTaskAction.pushDynamictaskIdentities(nodeId, (List)identities.get(i));
                  }
               }
            }

            if (CollectionUtil.isNotEmpty(identitiesMap)) {
               var35 = identitiesMap.keySet().iterator();

               label120:
               while(true) {
                  do {
                     if (!var35.hasNext()) {
                        break label120;
                     }

                     nodeId = (String)var35.next();
                  } while(!nodeIds.contains(nodeId));

                  identities = (List)identitiesMap.get(nodeId);

                  for(i = 0; i < identities.size(); ++i) {
                     destinations.add(nodeId);
                     DynamicInstTaskAction.pushDynamictaskIndex(nodeId, i);
                     DynamicInstTaskAction.pushDynamictaskIdentities(nodeId, Arrays.asList((SysIdentity)identities.get(i)));
                  }
               }
            }

            destinations.addAll(noNodeIds);
            model.setDestinations((String[])destinations.toArray(new String[destinations.size()]));
         }
      }

   }
}
