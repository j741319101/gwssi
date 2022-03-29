package com.dstz.bpm.plugin.node.recrease.handler;

import com.dstz.bpm.act.service.ActInstanceService;
import com.dstz.bpm.act.service.ActTaskService;
import com.dstz.bpm.act.util.ActivitiUtil;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.InstanceStatus;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.def.NodeInit;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.api.service.BpmRuntimeService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.data.DefaultBpmFlowDataAccessor;
import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.bpm.plugin.global.script.executer.GlobalScriptPluginExecutor;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.bus.api.model.IBusinessData;
import com.dstz.bus.api.model.IBusinessPermission;
import com.dstz.bus.api.service.IBusinessDataService;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.GroupService;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RecreaseDynamicTaskExecuter {
   @Resource
   private DynamicTaskManager dynamicTaskManager;
   @Resource
   private BpmInstanceManager bpmInstanceManager;
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource
   private TaskIdentityLinkManager taskIdentityLinkManager;
   @Resource
   private RecreaseTaskAction recreaseTaskAction;
   @Resource
   private BpmProcessDefService bpmProcessDefService;
   @Resource
   private IBpmBusDataHandle bpmBusDataHandle;
   @Resource
   private IBusinessDataService iBusinessDataService;
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;
   @Resource
   private BpmTaskManager bpmTaskManager;
   @Resource
   protected ActTaskService actTaskService;
   @Resource
   private ActInstanceService actInstanceService;
   @Resource
   protected IGroovyScriptEngine iGroovyScriptEngine;
   @Resource
   private UserService userService;
   @Resource
   private GroupService groupService;
   @Resource(
      name = "defaultBpmRuntimeServiceImpl"
   )
   private BpmRuntimeService runtimeService;
   @Resource
   private DefaultBpmFlowDataAccessor defaultBpmFlowDataAccessor;
   @Resource
   private SuperviseTaskExecuter superviseTaskExecuter;

   public void increase(DefualtTaskActionCmd model) {
      JSONObject extendConf = model.getExtendConf();
      BpmTask bpmTask = new BpmTask();
      bpmTask.setId(model.getTaskId());
      List<DynamicTask> dynamicTasks = this.dynamicTaskManager.getByTaskId(bpmTask.getId());
      boolean isDynamic = false;
      Boolean forceIncrease = extendConf.getBoolean("forceIncrease");
      Iterator var7 = dynamicTasks.iterator();

      while(var7.hasNext()) {
         DynamicTask dynamicTask = (DynamicTask)var7.next();
         if (StringUtils.equals("runtime", dynamicTask.getStatus())) {
            isDynamic = true;
         }
      }

      if (!isDynamic && forceIncrease != null && !forceIncrease) {
         throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
      } else {
         IBpmInstance bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
         JSONObject existAliveTask = this.recreaseTaskAction.getExistAliveTask(bpmInstance, bpmTask);
         Boolean b = existAliveTask.getBoolean("existAliveTask");
         if (!b && (forceIncrease == null || !forceIncrease)) {
            throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
         } else {
            JSONObject dynamicNodes = extendConf.getJSONObject("dynamicNodes");
            bpmTask.setDefId(bpmInstance.getDefId());
            BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
            bpmTask.setNodeId(bpmTaskOpinion.getTaskKey());
            bpmTask.setInstId(bpmTaskOpinion.getInstId());
            model.setBpmTask(bpmTask);
            model.setBpmInstance(bpmInstance);
            String startOrgId = bpmTaskOpinion.getTaskOrgId();
            if (bpmTaskOpinion.getApproveTime() == null) {
               TaskIdentityLink taskIdentityLink = (TaskIdentityLink)this.taskIdentityLinkManager.getByTaskId(bpmTask.getId()).get(0);
               startOrgId = taskIdentityLink.getOrgId();
            }

            model.setApproveOrgId(startOrgId);
            BpmNodeDef disBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), bpmTask.getNodeId());
            if (dynamicNodes != null || !dynamicNodes.isEmpty()) {
               Iterator var14 = dynamicNodes.keySet().iterator();

               while(var14.hasNext()) {
                  String dynamicNodeId = (String)var14.next();
                  String dynamicDestination = null;
                  JSONArray inUsers = dynamicNodes.getJSONArray(dynamicNodeId);
                  if (StringUtils.indexOf(dynamicNodeId, "&") > -1) {
                     String[] pgIds = dynamicNodeId.split("&");
                     dynamicNodeId = pgIds[0];
                     dynamicDestination = pgIds[1];
                  }

                  DynamicTask dynamicTask = this.getDynamicTask(dynamicTasks, dynamicNodeId);
                  if (dynamicTask == null) {
                     dynamicTask = new DynamicTask(bpmTask);
                     dynamicTask.setNodeId(dynamicNodeId);
                     this.dynamicTaskManager.create(dynamicTask);
                  }

                  for(Iterator var19 = inUsers.iterator(); var19.hasNext(); this.dynamicTaskManager.update(dynamicTask)) {
                     Object user = var19.next();
                     BpmNodeDef dynamicBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), dynamicNodeId);
                     String clazzStr = ((JSONObject)user).getString("clazz");
                     Class clazz = SysIdentity.class;
                     if (StringUtils.isNotEmpty(clazzStr)) {
                        ServiceLoader<SysIdentity> loader = ServiceLoader.load(SysIdentity.class);
                        Iterator var25 = loader.iterator();

                        while(var25.hasNext()) {
                           SysIdentity sysIdentity = (SysIdentity)var25.next();
                           if (StringUtils.equals(clazzStr, sysIdentity.getClass().getSimpleName())) {
                              clazz = sysIdentity.getClass();
                              break;
                           }
                        }
                     }

                     SysIdentity bpmInentity = (SysIdentity)JSON.toJavaObject((JSONObject)user, clazz);
                     if (StringUtils.equals("user", bpmInentity.getType())) {
                        IUser addUser = this.userService.getUserById(bpmInentity.getId());
                        if (addUser == null) {
                           throw new BusinessMessage(bpmInentity.getName() + " 用户丢失");
                        }

                        bpmInentity.setName(addUser.getFullname());
                     } else {
                        IGroup group = this.groupService.getById(bpmInentity.getType(), bpmInentity.getId());
                        if (group == null) {
                           throw new BusinessMessage(bpmInentity.getName() + " 组丢失");
                        }

                        bpmInentity.setName(group.getGroupName());
                     }

                     dynamicTask.setAmmount(dynamicTask.getAmmount() + 1);
                     if (dynamicTask.getCurrentIndex() < dynamicTask.getAmmount()) {
                        dynamicTask.setStatus("runtime");
                     }

                     List<JSONObject> identitys = JSONObject.parseArray(dynamicTask.getIdentityNode(), JSONObject.class);
                     if (identitys == null) {
                        identitys = new ArrayList();
                     }

                     JSONObject identity = new JSONObject();
                     JSONArray users = new JSONArray();
                     JSONObject addUser = new JSONObject();
                     addUser.put("id", bpmInentity.getId());
                     addUser.put("name", bpmInentity.getName());
                     addUser.put("type", bpmInentity.getType());
                     addUser.put("orgId", bpmInentity.getOrgId());
                     users.add(addUser);
                     identity.put("nodeIdentitys", users);
                     identity.put("taskName", extendConf.getString("taskName"));
                     ((List)identitys).add(identity);
                     dynamicTask.setIdentityNode(JSONObject.toJSONString(identitys));
                     if (dynamicTask.getIsParallel()) {
                        List<SysIdentity> sysIdentities = new ArrayList();
                        sysIdentities.add(bpmInentity);
                        BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(bpmTask.getId());
                        bpmTaskStack.setNodeId(dynamicBpmNodeDef.getNodeId());
                        if (dynamicBpmNodeDef.getParentBpmNodeDef() != null && dynamicBpmNodeDef.getParentBpmNodeDef() instanceof SubProcessNodeDef) {
                           bpmTaskStack.setNodeId(dynamicBpmNodeDef.getParentBpmNodeDef().getNodeId());
                        }

                        model.setExecutionStack(bpmTaskStack);
                        BpmContext.setThreadDynamictaskStack(bpmTaskStack.getNodeId(), bpmTaskStack);
                        if (StringUtils.equalsIgnoreCase(dynamicBpmNodeDef.getType().getKey(), NodeType.CALLACTIVITY.getKey())) {
                           String callFlowKey = ((CallActivityNodeDef)dynamicBpmNodeDef).getFlowKey();
                           BpmNodeDef startNode = (BpmNodeDef)this.bpmProcessDefService.getBpmProcessDef(this.bpmProcessDefService.getDefinitionByKey(callFlowKey).getId()).getStartNodes().get(0);
                           String callTargetNodeId = startNode.getNodeId();
                           model.setDynamicSubmitTaskName(startNode.getName());
                           model.setBpmIdentity(callTargetNodeId, sysIdentities);
                           BpmInstance topInstance = this.bpmInstanceManager.getTopInstance((BpmInstance)bpmInstance);
                           if (topInstance == null) {
                              topInstance = (BpmInstance)bpmInstance;
                           }

                           Map<String, IBusinessData> data = this.bpmBusDataHandle.getInstanceBusData(topInstance.getId(), (IBusinessPermission)null);
                           if (CollectionUtil.isNotEmpty(data)) {
                              model.setBizDataMap(data);
                           }
                        } else {
                           model.setDynamicSubmitTaskName(dynamicBpmNodeDef.getName());
                           model.setBpmIdentity(dynamicBpmNodeDef.getNodeId(), sysIdentities);
                        }

                        model.setDoActionName("追加");
                        BpmContext.setActionModel(model);
                        if (StringUtils.isNotEmpty(dynamicDestination)) {
                           model.setStartAppointDestinations(Arrays.asList(dynamicDestination));
                        }

                        model.setDestination(dynamicBpmNodeDef.getNodeId());
                        this.handelFormInit(model, disBpmNodeDef);
                        Map<String, List<List<SysIdentity>>> map = model.getDynamicBpmIdentity();
                        if (CollectionUtil.isNotEmpty(map)) {
                           map.forEach((nodeId, sysIdentityList) -> {
                              if (CollectionUtil.isNotEmpty(sysIdentityList)) {
                                 sysIdentityList.forEach((list) -> {
                                    model.addBpmIdentity(nodeId, list);
                                 });
                              }

                           });
                           map.clear();
                        }

                        this.runtimeService.createNewExecution(model.getInstanceId(), dynamicBpmNodeDef.getNodeId());
                        BpmContext.removeActionModel();
                     }
                  }
               }
            }

         }
      }
   }

   public void decrease(DefualtTaskActionCmd model) {
      JSONObject extendConf = model.getExtendConf();
      BpmTask bpmTask = new BpmTask();
      bpmTask.setId(model.getTaskId());
      List<DynamicTask> dynamicTasks = this.dynamicTaskManager.getByTaskId(bpmTask.getId());
      boolean isDynamic = false;
      Iterator var6 = dynamicTasks.iterator();

      while(var6.hasNext()) {
         DynamicTask dynamicTask = (DynamicTask)var6.next();
         if (StringUtils.equals("runtime", dynamicTask.getStatus())) {
            isDynamic = true;
         }
      }

      if (!isDynamic) {
         throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
      } else {
         IBpmInstance bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
         BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
         String orgId = null;
         if (bpmTaskOpinion.getApproveTime() == null) {
            bpmTaskOpinion = null;
            String taskLinkId = model.getTaskLinkId();
            if (StringUtils.isEmpty(taskLinkId)) {
               throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
            }

            TaskIdentityLink taskIdentityLink = (TaskIdentityLink)this.taskIdentityLinkManager.get(taskLinkId);
            if (taskIdentityLink == null) {
               throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
            }

            orgId = taskIdentityLink.getOrgId();
         } else {
            orgId = bpmTaskOpinion.getTaskOrgId();
         }

         model.setApproveOrgId(orgId);
         JSONObject existAliveTask = this.recreaseTaskAction.getExistAliveTask(bpmInstance, bpmTask);
         Boolean b = existAliveTask.getBoolean("existAliveTask");
         if (!b) {
            throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
         } else {
            String callBackId = (String)extendConf.get("callBackId");
            Map<String, JSONObject> tasks = (Map)existAliveTask.get("tasks");
            Iterator var13 = tasks.keySet().iterator();

            label455:
            while(var13.hasNext()) {
               String dynamicNodeId = (String)var13.next();
               JSONObject executionTask = (JSONObject)tasks.get(dynamicNodeId);
               JSONArray jsonObject = (JSONArray)executionTask.get("tasks");
               BpmNodeDef dynamicNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), dynamicNodeId);
               JSONObject data = model.getBusData();
               if (data != null && !data.isEmpty()) {
                  Map<String, IBusinessData> businessDataMap = new HashMap();
                  Iterator var20 = data.keySet().iterator();

                  while(var20.hasNext()) {
                     String key = (String)var20.next();
                     IBusinessData businessData = this.iBusinessDataService.parseBusinessData(data.getJSONObject(key), key);
                     businessDataMap.put(key, businessData);
                  }

                  model.setBizDataMap(businessDataMap);
               }

               if (data == null) {
                  Map<String, IBusinessData> businessDataMap = this.bpmBusDataHandle.getInstanceBusData(bpmInstance.getId(), (IBusinessPermission)null);
                  model.setBizDataMap(businessDataMap);
               }

               Iterator var55 = jsonObject.iterator();

               while(true) {
                  String instId;
                  String taskId;
                  String nodeId;
                  JSONObject task;
                  String id;
                  do {
                     if (!var55.hasNext()) {
                        continue label455;
                     }

                     Object jsonTask = var55.next();
                     task = (JSONObject)jsonTask;
                     id = (String)task.get("id");
                     instId = task.getString("instId");
                     taskId = task.getString("taskId");
                     nodeId = task.getString("nodeId");
                  } while(StringUtils.indexOf(callBackId, id) <= -1);

                  if (!task.getBoolean("existAliveTask")) {
                     throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
                  }

                  BpmNodeDef parentBpmNodeDef = dynamicNodeDef.getParentBpmNodeDef();
                  if (parentBpmNodeDef != null && parentBpmNodeDef instanceof SubProcessNodeDef && ((DynamicTaskPluginDef)((DynamicTaskPluginContext)parentBpmNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef()).getEnabled()) {
                     dynamicNodeId = parentBpmNodeDef.getNodeId();
                  }

                  DynamicTask dynamicTask = this.getDynamicTask(dynamicTasks, dynamicNodeId);
                  dynamicTask.setCurrentIndex(dynamicTask.getCurrentIndex() + 1);
                  if (dynamicTask.getCurrentIndex() == dynamicTask.getAmmount()) {
                     dynamicTask.setStatus("completed");
                  }

                  BpmInstance subInst;
                  if (StringUtils.equalsIgnoreCase(NodeType.CALLACTIVITY.getKey(), dynamicNodeDef.getType().getKey())) {
                     subInst = (BpmInstance)this.bpmInstanceManager.get(instId);
                     model.setInstanceId(instId);
                     model.setBpmInstance(subInst);
                     this.handleInstanceInfo(model, StringUtils.equals("completed", dynamicTask.getStatus()), bpmTaskOpinion);
                  } else {
                     model.setBpmTask((IBpmTask)this.bpmTaskManager.get(taskId));
                     subInst = (BpmInstance)this.bpmInstanceManager.get(instId);
                     model.setTaskId(taskId);
                     model.setInstanceId(instId);
                     model.setBpmInstance(subInst);
                     BpmContext.setActionModel(model);
                     BpmNodeDef bpmNodeDef1 = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), nodeId);
                     BpmNodeDef subProcessNodeDef = bpmNodeDef1.getParentBpmNodeDef();
                     if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
                        String endNodeId = ((BpmNodeDef)((SubProcessNodeDef)subProcessNodeDef).getChildBpmProcessDef().getEndEvents().get(0)).getNodeId();
                        BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(taskId);
                        if (!StringUtils.equals("create", bpmTaskStack.getActionName())) {
                           QueryFilter queryFilter = new DefaultQueryFilter(true);
                           queryFilter.addParamsFilter("taskId", taskId);
                           queryFilter.addParamsFilter("prior", "FORWARD");
                           queryFilter.addFilter("action_name_", "create", QueryOP.EQUAL);
                           queryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
                           List<BpmTaskStack> bpmTaskStackList = this.bpmTaskStackManager.getTaskStackByIteration(queryFilter);
                           if (CollectionUtil.isEmpty(bpmTaskStackList)) {
                              throw new BusinessException("取回子流程任务失败，查看taskId:" + taskId + "的后置任务");
                           }

                           taskId = ((BpmTaskStack)bpmTaskStackList.get(0)).getTaskId();
                           model.setBpmTask((IBpmTask)this.bpmTaskManager.get(taskId));
                        }

                        String[] nodeIds = new String[0];
                        if (StringUtils.equals(dynamicTask.getStatus(), "completed") && bpmTaskOpinion != null) {
                           nodeIds = new String[]{bpmTaskOpinion.getTaskKey()};
                           model.setBpmIdentity(bpmTaskOpinion.getTaskKey(), Arrays.asList(new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId())));
                        }

                        Map activityMap = ActivitiUtil.skipPrepare(subInst.getActDefId(), subProcessNodeDef.getNodeId(), nodeIds);

                        try {
                           this.actTaskService.completeTask(taskId, model.getActionVariables(), new String[]{endNodeId});
                        } catch (Exception var48) {
                           throw new BusinessException(var48);
                        } finally {
                           ActivitiUtil.restoreActivity(activityMap);
                        }
                     } else {
                        String[] nodeIds = new String[0];
                        if (StringUtils.equals(dynamicTask.getStatus(), "completed") && bpmTaskOpinion != null) {
                           nodeIds = new String[]{bpmTaskOpinion.getTaskKey()};
                           model.setBpmIdentity(bpmTaskOpinion.getTaskKey(), Arrays.asList(new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId())));
                        }

                        Map activityMap = ActivitiUtil.skipPrepare(subInst.getActDefId(), bpmNodeDef1.getNodeId(), nodeIds);

                        try {
                           this.actTaskService.completeTask(taskId, model.getActionVariables());
                        } catch (Exception var46) {
                           throw new BusinessException(var46);
                        } finally {
                           ActivitiUtil.restoreActivity(activityMap);
                        }
                     }

                     BpmContext.removeActionModel();
                  }

                  this.dynamicTaskManager.update(dynamicTask);
               }
            }

         }
      }
   }

   private DynamicTask getDynamicTask(List<DynamicTask> dynamicTasks, String dynamicNodeId) {
      Iterator var3 = dynamicTasks.iterator();

      DynamicTask dynamicTask;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         dynamicTask = (DynamicTask)var3.next();
      } while(!StringUtils.equals(dynamicNodeId, dynamicTask.getNodeId()));

      return dynamicTask;
   }

   private void handleInstanceInfo(DefualtTaskActionCmd model, Boolean isEnd, BpmTaskOpinion bpmTaskOpinion) {
      BpmInstance instance = (BpmInstance)model.getBpmInstance();

      try {
         List<BpmInstance> subs = this.bpmInstanceManager.getByPId(instance.getId());
         Map<String, Integer> subNodeMax = new HashMap();

         for (BpmInstance inst : subs) {
            Integer index = subNodeMax.get(inst.getSuperNodeId());
            if (index == null) {
               index = Integer.valueOf(0);
            }
            subNodeMax.put(inst.getSuperNodeId(), index = Integer.valueOf(index.intValue() + 1));
         }

         Map<String, Integer> subNodeIndex = new HashMap();
         Iterator var44 = subs.iterator();

         while(var44.hasNext()) {
            BpmInstance inst = (BpmInstance)var44.next();
            Integer max = (Integer)subNodeMax.get(inst.getSuperNodeId());
            Integer index = (Integer)subNodeIndex.get(inst.getSuperNodeId());
            if (index == null) {
               index = 0;
            }

            index = index + 1;
            if (index < max) {
               model.setBpmInstance(inst);
               model.setDefId(inst.getDefId());
               this.handleInstanceInfo(model, false, (BpmTaskOpinion)null);
            } else if (index == max) {
               model.setBpmInstance(inst);
               model.setDefId(inst.getDefId());
               this.handleInstanceInfo(model, true, (BpmTaskOpinion)null);
            }
         }

         model.setBpmInstance(instance);
         model.setDefId(instance.getDefId());
         if (isEnd) {
            BpmInstance supInst = (BpmInstance)this.bpmInstanceManager.get(instance.getParentInstId());
            String[] nodeIds = new String[0];
            if (bpmTaskOpinion != null) {
               nodeIds = new String[]{bpmTaskOpinion.getTaskKey()};
               model.setBpmIdentity(bpmTaskOpinion.getTaskKey(), Arrays.asList(new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId())));
            }

            Map activityMap = ActivitiUtil.skipPrepare(supInst.getActDefId(), instance.getSuperNodeId(), nodeIds);

            try {
               BpmNodeDef bpmNodeDef = (BpmNodeDef)this.bpmProcessDefService.getEndEvents(instance.getDefId()).get(0);
               List<BpmTask> bpmTasks = this.bpmTaskManager.getByInstId(instance.getId());
               if (CollectionUtil.isNotEmpty(bpmTasks)) {
                  Iterator var13 = bpmTasks.iterator();

                  while(var13.hasNext()) {
                     BpmTask bpmTask = (BpmTask)var13.next();
                     if (StringUtils.equals("SIGN", bpmTask.getTaskType())) {
                        this.bpmTaskManager.remove(bpmTask.getId());
                        this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
                        BpmTaskOpinion signBpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
                        signBpmTaskOpinion.setApproveTime(new Date());
                        signBpmTaskOpinion.setOpinion(model.getOpinion());
                        signBpmTaskOpinion.setApprover(ContextUtil.getCurrentUserId());
                        signBpmTaskOpinion.setApproverName(ContextUtil.getCurrentUserName());
                        signBpmTaskOpinion.setTaskOrgId(model.getApproveOrgId());
                        this.bpmTaskOpinionManager.update(signBpmTaskOpinion);
                     } else {
                        model.setBpmTask(bpmTask);
                        BpmContext.setActionModel(model);
                        this.actTaskService.completeTask(bpmTask.getTaskId(), new String[]{bpmNodeDef.getNodeId()});
                        BpmContext.removeActionModel();
                     }
                  }
               }
            } catch (Exception var37) {
               throw new BusinessException(var37);
            } finally {
               ActivitiUtil.restoreActivity(activityMap);
            }

            this.dynamicTaskManager.updateEndByInstId(instance.getId());
         } else {
            BpmInstance supInst = (BpmInstance)this.bpmInstanceManager.get(instance.getParentInstId());
            Map activityMap = ActivitiUtil.skipPrepare(supInst.getActDefId(), instance.getSuperNodeId(), new String[0]);

            try {
               BpmNodeDef bpmNodeDef = (BpmNodeDef)this.bpmProcessDefService.getEndEvents(instance.getDefId()).get(0);
               List<BpmTask> bpmTasks = this.bpmTaskManager.getByInstId(instance.getId());
               if (CollectionUtil.isNotEmpty(bpmTasks)) {
                  Iterator var50 = bpmTasks.iterator();

                  while(var50.hasNext()) {
                     BpmTask bpmTask = (BpmTask)var50.next();
                     if (StringUtils.equals("SIGN", bpmTask.getTaskType())) {
                        this.bpmTaskManager.remove(bpmTask.getId());
                        this.taskIdentityLinkManager.removeByTaskId(bpmTask.getId());
                        BpmTaskOpinion signBpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
                        signBpmTaskOpinion.setApproveTime(new Date());
                        signBpmTaskOpinion.setOpinion(model.getOpinion());
                        signBpmTaskOpinion.setApprover(ContextUtil.getCurrentUserId());
                        signBpmTaskOpinion.setApproverName(ContextUtil.getCurrentUserName());
                        signBpmTaskOpinion.setTaskOrgId(model.getApproveOrgId());
                        signBpmTaskOpinion.setStatus(OpinionStatus.DECREASEDYNAMIC.getKey());
                        this.bpmTaskOpinionManager.update(signBpmTaskOpinion);
                     } else {
                        model.setBpmTask(bpmTask);
                        BpmContext.setActionModel(model);
                        this.actTaskService.completeTask(bpmTask.getTaskId(), new String[]{bpmNodeDef.getNodeId()});
                        BpmContext.removeActionModel();
                     }
                  }
               }
            } catch (Exception var39) {
               throw new BusinessException(var39);
            } finally {
               ActivitiUtil.restoreActivity(activityMap);
            }
         }

         instance.setStatus(InstanceStatus.STATUS_RECOVER.getKey());
         instance.setEndTime(new Date());
         instance.setDuration(instance.getEndTime().getTime() - instance.getCreateTime().getTime());
         this.bpmInstanceManager.update(instance);
      } catch (Exception var41) {
         throw new BusinessException(var41);
      } finally {
         GlobalScriptPluginExecutor.clearCanExecute();
         this.superviseTaskExecuter.clearExecForce();
      }
   }

   private void handelFormInit(BaseActionCmd cmd, BpmNodeDef nodeDef) {
      String nodeId = nodeDef.getNodeId();
      DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(cmd.getBpmInstance().getDefId());
      List<NodeInit> nodeInitList = def.getNodeInitList(nodeId);
      Map<String, IBusinessData> bos = cmd.getBizDataMap();
      if (MapUtil.isEmpty(bos)) {
         bos = this.defaultBpmFlowDataAccessor.getTaskBusData(cmd.getTaskId());
      }

      if (!CollectionUtil.isEmpty(nodeInitList)) {
         Map<String, Object> param = new HashMap();
         if (MapUtil.isNotEmpty(bos)) {
            param.putAll(bos);
         }

         param.put("bpmInstance", cmd.getBpmInstance());
         param.put("actionCmd", cmd);
         ActionType actionType = cmd.getActionType();
         param.put("submitActionDesc", actionType.getName());
         param.put("submitActionName", actionType.getKey());
         param.put("submitOpinion", cmd.getOpinion());
         param.put("isTask", false);
         if (cmd instanceof DefualtTaskActionCmd) {
            param.put("isTask", true);
            param.put("bpmTask", ((DefualtTaskActionCmd)cmd).getBpmTask());
         }

         Iterator var9 = nodeInitList.iterator();

         while(var9.hasNext()) {
            NodeInit init = (NodeInit)var9.next();
            if (StringUtil.isNotEmpty(init.getWhenSave())) {
               try {
                  this.iGroovyScriptEngine.executeObject(init.getWhenSave(), param);
               } catch (Exception var12) {
                  throw new BusinessMessage(var12.getMessage(), BpmStatusCode.FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR, var12);
               }
            }
         }

      }
   }
}
