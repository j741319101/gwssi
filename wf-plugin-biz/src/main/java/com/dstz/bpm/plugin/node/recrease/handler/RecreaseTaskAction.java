package com.dstz.bpm.plugin.node.recrease.handler;

import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.engine.util.BpmTaskShowUtil;
import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Resource;

import com.dstz.sys.api.model.SysIdentity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RecreaseTaskAction {
   @Resource
   private DynamicTaskManager dynamicTaskManager;
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;
   @Resource
   private BpmInstanceManager bpmInstanceManager;
   @Resource
   private BpmProcessDefService bpmProcessDefService;
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;

   public JSONObject getExistAliveTask(IBpmInstance bpmInstance, IBpmTask bpmTask) {
      if (bpmInstance != null && !StringUtils.isEmpty(bpmInstance.getId())) {
         if (bpmTask != null && !StringUtils.isEmpty(bpmTask.getId())) {
            List<DynamicTask> dynamicTasks = this.getDynamicTasks(bpmTask.getId());
            QueryFilter queryFilter = new DefaultQueryFilter(true);
            Map<String, BpmInstance> instances = new ConcurrentHashMap();
            instances.put(bpmInstance.getId(), (BpmInstance)bpmInstance);
            AtomicBoolean isDynamic = new AtomicBoolean(false);
            List taskStacks;
            if (CollectionUtil.isNotEmpty(dynamicTasks)) {
               taskStacks = this.bpmInstanceManager.getByParentId(bpmInstance.getId());
               List<String> childrenInstIds = new ArrayList();
               ((List<BpmInstance>)taskStacks).forEach((child) -> {
                  childrenInstIds.add(child.getId());
                  instances.put(child.getId(), child);
               });
               isDynamic.set(true);
               childrenInstIds.add(bpmInstance.getId());
               queryFilter.addFilter("inst_id_", childrenInstIds, QueryOP.IN);
            } else {
               queryFilter.addFilter("level", 5, QueryOP.LESS);
            }

            queryFilter.addFilter("level", 2, QueryOP.GREAT);
            queryFilter.addParamsFilter("taskId", bpmTask.getId());
            queryFilter.addParamsFilter("prior", "FORWARD");
            queryFilter.addFieldSort("level", "asc");
            queryFilter.addFieldSort("id_", "asc");
            taskStacks = this.bpmTaskStackManager.getTaskStackByIteration(queryFilter);
            Map<String, BpmNodeDef> executionNodeDefs = new ConcurrentHashMap();
            Map<String, List<BpmTaskStack>> executionStacks = new ConcurrentHashMap();
            Map<String, List<String>> stackParentIds = new ConcurrentHashMap();
            ((List<BpmTaskStack>)taskStacks).forEach((bpmTaskStack) -> {
               String subInstId = bpmTaskStack.getInstId();
               String executionNodeId = bpmTaskStack.getNodeId();
               BpmInstance subInst = (BpmInstance)instances.get(subInstId);
               if (subInst == null) {
                  subInst = (BpmInstance)this.bpmInstanceManager.get(subInstId);
                  if (subInst == null) {
                     throw new BusinessException("流程实例缺失，检查流程实例" + subInstId);
                  }

                  instances.put(subInstId, subInst);
               }

               BpmNodeDef executionNodeDef = (BpmNodeDef)executionNodeDefs.get(subInst.getDefId() + "-" + executionNodeId);
               if (executionNodeDef == null) {
                  executionNodeDef = this.bpmProcessDefService.getBpmNodeDef(subInst.getDefId(), executionNodeId);
                  if (executionNodeDef != null) {
                     executionNodeDefs.put(subInst.getDefId() + "-" + executionNodeId, executionNodeDef);
                  }
               }

               if (bpmTaskStack.getLevel() == 3) {
                  List<String> parentIds = new ArrayList();
                  parentIds.add(bpmTaskStack.getId());
                  stackParentIds.put(bpmTaskStack.getId(), parentIds);
                  List<BpmTaskStack> execution = new ArrayList();
                  execution.add(bpmTaskStack);
                  executionStacks.put(bpmTaskStack.getId(), execution);
               } else {
                  Iterator var14 = stackParentIds.keySet().iterator();

                  while(var14.hasNext()) {
                     String key = (String)var14.next();
                     List<String> value = (List)stackParentIds.get(key);
                     List<BpmTaskStack> stacks = (List)executionStacks.get(key);
                     if (value.contains(bpmTaskStack.getParentId())) {
                        value.add(bpmTaskStack.getId());
                        stacks.add(bpmTaskStack);
                        break;
                     }
                  }
               }

            });
            JSONObject stackTask = new JSONObject();
            AtomicBoolean existAliveTask = new AtomicBoolean(false);
            Map<String, JSONObject> executionTasks = new ConcurrentHashMap();
            Map<String, Object[]> sysIdentityMap = new ConcurrentHashMap();
            Map<String, List<SysIdentity>> opinionSysIdentity = new ConcurrentHashMap();
            ((List<BpmTaskStack>)taskStacks).stream().filter((bpmTaskStack) -> {
               return bpmTaskStack.getLevel() < 5;
            }).filter((bpmTaskStack) -> {
               return StringUtils.equals(bpmTaskStack.getNodeType(), "userTask") || StringUtils.equals(bpmTaskStack.getNodeType(), "callActivity");
            }).forEach((bpmTaskStack) -> {
               JSONObject executionTask = (JSONObject)executionTasks.get(bpmTaskStack.getNodeId());
               if (executionTask == null) {
                  executionTask = new JSONObject();
                  executionTask.put("tasks", new JSONArray());
                  executionTask.put("existAliveTask", false);
                  executionTasks.put(bpmTaskStack.getNodeId(), executionTask);
               }

               AtomicBoolean executionExistAliveTask = new AtomicBoolean(false);
               AtomicBoolean taskAlive = new AtomicBoolean(false);
               BpmNodeDef bpmNodeDef = (BpmNodeDef)executionNodeDefs.get(bpmInstance.getDefId() + "-" + bpmTaskStack.getNodeId());
               if (bpmNodeDef != null) {
                  BpmTaskOpinion bpmTaskOpinion = null;
                  String taskType = "none";
                  if (StringUtils.equals(bpmTaskStack.getNodeType(), "callActivity")) {
                     if (!isDynamic.get()) {
                        return;
                     }

                     DynamicTaskPluginDef dynamicTaskPluginDefx = (DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
                     if (!dynamicTaskPluginDefx.getEnabled()) {
                        return;
                     }

                     taskType = "dynamic";
                     List subStacks = (List)executionStacks.get(bpmTaskStack.getId());
                     if (CollectionUtil.isEmpty(subStacks) || subStacks.size() < 2) {
                        return;
                     }

                     BpmTaskStack subFirstNodeStack = null;

                     for(int i = 1; i < subStacks.size(); ++i) {
                        if (StringUtils.equals(((BpmTaskStack)subStacks.get(i)).getNodeType(), "userTask") && !StringUtils.equals(((BpmTaskStack)subStacks.get(i)).getInstId(), bpmTaskStack.getInstId())) {
                           subFirstNodeStack = (BpmTaskStack)subStacks.get(i);
                           break;
                        }
                     }

                     BpmInstance subInst = (BpmInstance)instances.get(subFirstNodeStack.getInstId());
                     BpmNodeDef subBpmNodeDef = (BpmNodeDef)executionNodeDefs.get(subInst.getDefId() + "-" + subFirstNodeStack.getNodeId());
                     bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(subFirstNodeStack.getTaskId());
                     if (bpmTaskStack.getEndTime() == null && dynamicTaskPluginDefx.isReset()) {
                        if (bpmTaskOpinion.getApproveTime() == null) {
                           existAliveTask.set(true);
                           executionExistAliveTask.set(true);
                           taskAlive.set(true);
                        } else if (dynamicTaskPluginDefx.isCanBeRecycledEnd() && bpmTaskStack.getEndTime() == null) {
                           existAliveTask.set(true);
                           executionExistAliveTask.set(true);
                           taskAlive.set(true);
                        }
                     }

                     executionTask.put("existAliveTask", executionExistAliveTask.get());
                     executionTask.put("defId", subInst.getDefId());
                     executionTask.put("nodeId", bpmTaskOpinion.getTaskKey());
                  }

                  SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)((SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class)).getBpmPluginDef();
                  if (signTaskPluginDef.isSignMultiTask()) {
                     taskType = "sign";
                     if (bpmTaskStack.getEndTime() == null && signTaskPluginDef.isReset()) {
                        existAliveTask.set(true);
                        executionTask.put("existAliveTask", true);
                     }

                     executionTask.put("defId", bpmInstance.getDefId());
                     executionTask.put("nodeId", bpmTaskStack.getNodeId());
                  }

                  executionTask.put("taskName", bpmNodeDef.getName());
                  List executionStack;
                  if (StringUtils.equals(taskType, "none")) {
                     BpmNodeDef supNodeDef = bpmNodeDef.getParentBpmNodeDef();
                     bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTaskStack.getTaskId());
                     DynamicTaskPluginDef dynamicTaskPluginDef;
                     if (supNodeDef != null && supNodeDef instanceof SubProcessNodeDef) {
                        dynamicTaskPluginDef = (DynamicTaskPluginDef)((DynamicTaskPluginContext)supNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
                        if (!dynamicTaskPluginDef.getEnabled()) {
                           return;
                        }

                        taskType = "dynamic";
                        executionTask.put("taskName", supNodeDef.getName());
                        if (bpmTaskStack.getEndTime() == null) {
                           taskAlive.set(true);
                           existAliveTask.set(true);
                           executionExistAliveTask.set(true);
                        } else if (dynamicTaskPluginDef.isCanBeRecycledEnd()) {
                           executionExistAliveTask.set(true);
                           List<BpmNodeDef> endNodeDefs = ((SubProcessNodeDef)supNodeDef).getChildBpmProcessDef().getEndEvents();
                           executionStack = (List)executionStacks.get(bpmTaskStack.getId());
                           ((List<BpmTaskStack>)executionStack).parallelStream().forEach((stack) -> {
                              endNodeDefs.forEach((def) -> {
                                 if (StringUtils.equals(def.getNodeId(), stack.getNodeId()) || StringUtils.indexOf(stack.getNodeName(), def.getNodeId()) > -1) {
                                    executionExistAliveTask.set(false);
                                 }

                              });
                           });
                           if (executionExistAliveTask.get()) {
                              existAliveTask.set(true);
                              taskAlive.set(true);
                           }
                        }
                     } else {
                        dynamicTaskPluginDef = (DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
                        if (!dynamicTaskPluginDef.getEnabled()) {
                           return;
                        }

                        taskType = "dynamic";
                        if (bpmTaskStack.getEndTime() == null) {
                           existAliveTask.set(true);
                           executionExistAliveTask.set(true);
                           taskAlive.set(true);
                        }
                     }

                     executionTask.put("existAliveTask", executionExistAliveTask.get());
                     executionTask.put("defId", bpmInstance.getDefId());
                     executionTask.put("nodeId", bpmTaskStack.getNodeId());
                  }

                  executionTask.put("taskType", taskType);
                  JSONArray tasks = executionTask.getJSONArray("tasks");
                  ArrayList sysIdentities;
                  List bpmTaskOpinions;
                  List sysIdentitiesxx;
                  if (StringUtils.equals(taskType, "sign")) {
                     String key = bpmNodeDef.getBpmProcessDef().getProcessDefinitionId() + bpmNodeDef.getNodeId();
                     Object[] nodeSysIdentity = (Object[])sysIdentityMap.get(key);
                     if (nodeSysIdentity == null) {
                        List<SysIdentity> sysIdentitiesx = new ArrayList();
                        sysIdentities = new ArrayList();
                        nodeSysIdentity = new Object[]{bpmNodeDef, sysIdentitiesx, sysIdentities};
                        sysIdentityMap.put(key, nodeSysIdentity);
                     }

                     executionStack = (List)nodeSysIdentity[1];
                     sysIdentitiesxx = (List)nodeSysIdentity[2];
                     bpmTaskOpinions = this.bpmTaskOpinionManager.getByInstAndSignId(bpmInstance.getId(), bpmTaskStack.getTaskId());
                     if (!CollectionUtils.isEmpty(bpmTaskOpinions)) {
                        for (BpmTaskOpinion signSpmTaskOpinion : ((List<BpmTaskOpinion>) bpmTaskOpinions)) {
                           JSONObject approve = new JSONObject();
                           approve.put("id", signSpmTaskOpinion.getId());
                           approve.put("taskId", signSpmTaskOpinion.getTaskId());
                           approve.put("instId", signSpmTaskOpinion.getInstId());
                           approve.put("nodeId", signSpmTaskOpinion.getTaskKey());
                           approve.put("existAliveTask", StringUtils.equals(signSpmTaskOpinion.getStatus(), "awaiting_check"));
                           approve.put("taskName", signSpmTaskOpinion.getTaskName());
                           approve.put("approver", signSpmTaskOpinion.getApprover());
                           approve.put("approverName", signSpmTaskOpinion.getApproverName());
                           approve.put("approveTime", signSpmTaskOpinion.getApproveTime());
                           List<SysIdentity> users = new ArrayList();
                           opinionSysIdentity.put(signSpmTaskOpinion.getId(), users);
                           Iterator var7 = Arrays.asList(signSpmTaskOpinion.getAssignInfo().split(",")).iterator();

                           while(var7.hasNext()) {
                              String info = (String)var7.next();
                              if (StringUtils.isNotEmpty(info)) {
                                 String[] user;
                                 DefaultIdentity sysIdentity;
                                 if (StringUtils.startsWith(info, "user")) {
                                    user = info.split("-");
                                    sysIdentity = new DefaultIdentity(user[2], user[1], "user");
                                    if (user.length>3){
                                       sysIdentity.setOrgId(user[3]);
                                    }
                                    executionStack.add(sysIdentity);
                                    users.add(sysIdentity);
                                 } else {
                                    user = info.split("-");
                                    sysIdentity = new DefaultIdentity(user[2], user[1], user[0]);
                                    if (user.length>3){
                                       sysIdentity.setOrgId(user[3]);
                                    }
                                    executionStack.add(sysIdentity);
                                    users.add(sysIdentity);
                                 }
                              }
                           }

                           tasks.add(approve);
                           sysIdentitiesxx.add(approve);
                        }
                     }
                  } else {
                     JSONObject approve = new JSONObject();
                     approve.put("id", bpmTaskOpinion.getId());
                     approve.put("taskId", bpmTaskOpinion.getTaskId());
                     approve.put("instId", bpmTaskOpinion.getInstId());
                     approve.put("nodeId", bpmTaskOpinion.getTaskKey());
                     approve.put("taskName", bpmTaskOpinion.getTaskName());
                     approve.put("approver", bpmTaskOpinion.getApprover());
                     approve.put("approverName", bpmTaskOpinion.getApproverName());
                     approve.put("approveTime", bpmTaskOpinion.getApproveTime());
                     approve.put("existAliveTask", taskAlive.get());
                     if (StringUtils.isNotEmpty(bpmTaskOpinion.getAssignInfo())) {
                        String keyx = bpmNodeDef.getBpmProcessDef().getProcessDefinitionId() + bpmNodeDef.getNodeId();
                        Object[] nodeSysIdentityx = (Object[])sysIdentityMap.get(keyx);
                        if (nodeSysIdentityx == null) {
                           sysIdentities = new ArrayList();
                           List<JSONObject> opinions = new ArrayList();
                           BpmNodeDef subBpmNodeDef = (BpmNodeDef)executionNodeDefs.get(executionTask.getString("defId") + "-" + bpmTaskOpinion.getTaskKey());
                           nodeSysIdentityx = new Object[]{subBpmNodeDef, sysIdentities, opinions};
                           sysIdentityMap.put(keyx, nodeSysIdentityx);
                        }

                        sysIdentitiesxx = (List)nodeSysIdentityx[1];
                        bpmTaskOpinions = (List)nodeSysIdentityx[2];
                        List<SysIdentity> users = new ArrayList();
                        bpmTaskOpinions.add(approve);
                        opinionSysIdentity.put(bpmTaskOpinion.getId(), users);
                        Iterator var25 = Arrays.asList(bpmTaskOpinion.getAssignInfo().split(",")).iterator();

                        while(var25.hasNext()) {
                           String info = (String)var25.next();
                           if (StringUtils.isNotEmpty(info)) {
                              String[] user;
                              DefaultIdentity sysIdentity;
                              if (StringUtils.startsWith(info, "user")) {
                                 user = info.split("-");
                                 sysIdentity = new DefaultIdentity(user[2], user[1], "user", user[3]);
                                 sysIdentitiesxx.add(sysIdentity);
                                 users.add(sysIdentity);
                              } else {
                                 user = info.split("-");
                                 sysIdentity = new DefaultIdentity(user[2], user[1], user[0], user[3]);
                                 sysIdentitiesxx.add(sysIdentity);
                                 users.add(sysIdentity);
                              }
                           }
                        }
                     }

                     tasks.add(approve);
                  }

               }
            });
            stackTask.put("existAliveTask", existAliveTask.get());
            if (existAliveTask.get() && sysIdentityMap.size() > 0) {
               sysIdentityMap.forEach((key, value) -> {
                  List<SysIdentity> lists = (List)value[1];
                  BpmNodeDef bpmNodeDef = (BpmNodeDef)value[0];
                  List<JSONObject> opinions = (List)value[2];
                  BpmTaskShowUtil.appendOrgUser(lists, bpmNodeDef, "--");
                  opinions.forEach((opinion) -> {
                     List<SysIdentity> sysIdentities = (List)opinionSysIdentity.get(opinion.getString("id"));
                     JSONArray jsonArray = new JSONArray();
                     if (!CollectionUtils.isEmpty(sysIdentities)) {
                        sysIdentities.forEach((sysIdentity) -> {
                           JSONObject userInfo = new JSONObject();
                           userInfo.put("type", sysIdentity.getType());
                           userInfo.put("id", sysIdentity.getId());
                           userInfo.put("name", sysIdentity.getName());
                           userInfo.put("orgId", sysIdentity.getOrgId());
                           userInfo.put("sn", ((DefaultIdentity)sysIdentity).getSn());
                           userInfo.put("compareOrgId", ((DefaultIdentity)sysIdentity).getCompareOrgId());
                           jsonArray.add(userInfo);
                        });
                     }

                     opinion.put("assignInfo", jsonArray);
                  });
               });
            }

            stackTask.put("tasks", executionTasks);
            return stackTask;
         } else {
            return new JSONObject();
         }
      } else {
         return new JSONObject();
      }
   }

   private List<DynamicTask> getDynamicTasks(String taskId) {
      List<DynamicTask> dynamicTasks = this.dynamicTaskManager.getByTaskId(taskId);
      return dynamicTasks;
   }
}
