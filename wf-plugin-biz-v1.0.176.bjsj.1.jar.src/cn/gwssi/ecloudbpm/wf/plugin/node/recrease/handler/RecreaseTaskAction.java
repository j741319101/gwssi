/*     */ package cn.gwssi.ecloudbpm.wf.plugin.node.recrease.handler;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.util.BpmTaskShowUtil;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.DynamicTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.DynamicTask;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.context.SignTaskPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class RecreaseTaskAction
/*     */ {
/*     */   @Resource
/*     */   private DynamicTaskManager dynamicTaskManager;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   
/*     */   public JSONObject getExistAliveTask(IBpmInstance bpmInstance, IBpmTask bpmTask) {
/*  53 */     if (bpmInstance == null || StringUtils.isEmpty(bpmInstance.getId())) {
/*  54 */       return new JSONObject();
/*     */     }
/*  56 */     if (bpmTask == null || StringUtils.isEmpty(bpmTask.getId())) {
/*  57 */       return new JSONObject();
/*     */     }
/*  59 */     List<DynamicTask> dynamicTasks = getDynamicTasks(bpmTask.getId());
/*  60 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/*     */     
/*  62 */     Map<String, BpmInstance> instances = new ConcurrentHashMap<>();
/*  63 */     instances.put(bpmInstance.getId(), (BpmInstance)bpmInstance);
/*  64 */     AtomicBoolean isDynamic = new AtomicBoolean(false);
/*  65 */     if (CollectionUtil.isNotEmpty(dynamicTasks)) {
/*  66 */       List<BpmInstance> bpmChildInstances = this.bpmInstanceManager.getByParentId(bpmInstance.getId());
/*  67 */       List<String> childrenInstIds = new ArrayList<>();
/*  68 */       bpmChildInstances.forEach(child -> {
/*     */             childrenInstIds.add(child.getId());
/*     */             instances.put(child.getId(), child);
/*     */           });
/*  72 */       isDynamic.set(true);
/*  73 */       childrenInstIds.add(bpmInstance.getId());
/*  74 */       defaultQueryFilter.addFilter("inst_id_", childrenInstIds, QueryOP.IN);
/*     */     } else {
/*  76 */       defaultQueryFilter.addFilter("level", Integer.valueOf(5), QueryOP.LESS);
/*     */     } 
/*  78 */     defaultQueryFilter.addFilter("level", Integer.valueOf(2), QueryOP.GREAT);
/*  79 */     defaultQueryFilter.addParamsFilter("taskId", bpmTask.getId());
/*  80 */     defaultQueryFilter.addParamsFilter("prior", "FORWARD");
/*  81 */     defaultQueryFilter.addFieldSort("level", "asc");
/*  82 */     defaultQueryFilter.addFieldSort("id_", "asc");
/*  83 */     List<BpmTaskStack> taskStacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/*     */     
/*  85 */     Map<String, BpmNodeDef> executionNodeDefs = new ConcurrentHashMap<>();
/*     */     
/*  87 */     Map<String, List<BpmTaskStack>> executionStacks = new ConcurrentHashMap<>();
/*     */     
/*  89 */     Map<String, List<String>> stackParentIds = new ConcurrentHashMap<>();
/*     */     
/*  91 */     taskStacks.forEach(bpmTaskStack -> {
/*     */           String subInstId = bpmTaskStack.getInstId();
/*     */           
/*     */           String executionNodeId = bpmTaskStack.getNodeId();
/*     */           BpmInstance subInst = (BpmInstance)instances.get(subInstId);
/*     */           if (subInst == null) {
/*     */             subInst = (BpmInstance)this.bpmInstanceManager.get(subInstId);
/*     */             if (subInst == null) {
/*     */               throw new BusinessException("流程实例缺失，检查流程实例" + subInstId);
/*     */             }
/*     */             instances.put(subInstId, subInst);
/*     */           } 
/*     */           BpmNodeDef executionNodeDef = (BpmNodeDef)executionNodeDefs.get(subInst.getDefId() + "-" + executionNodeId);
/*     */           if (executionNodeDef == null) {
/*     */             executionNodeDef = this.bpmProcessDefService.getBpmNodeDef(subInst.getDefId(), executionNodeId);
/*     */             if (executionNodeDef != null) {
/*     */               executionNodeDefs.put(subInst.getDefId() + "-" + executionNodeId, executionNodeDef);
/*     */             }
/*     */           } 
/*     */           if (bpmTaskStack.getLevel().intValue() == 3) {
/*     */             List<String> parentIds = new ArrayList<>();
/*     */             parentIds.add(bpmTaskStack.getId());
/*     */             stackParentIds.put(bpmTaskStack.getId(), parentIds);
/*     */             List<BpmTaskStack> execution = new ArrayList<>();
/*     */             execution.add(bpmTaskStack);
/*     */             executionStacks.put(bpmTaskStack.getId(), execution);
/*     */           } else {
/*     */             for (String key : stackParentIds.keySet()) {
/*     */               List<String> value = stackParentIds.get(key);
/*     */               List<BpmTaskStack> stacks = executionStacks.get(key);
/*     */               if (value.contains(bpmTaskStack.getParentId())) {
/*     */                 value.add(bpmTaskStack.getId());
/*     */                 stacks.add(bpmTaskStack);
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/* 129 */     JSONObject stackTask = new JSONObject();
/*     */     
/* 131 */     AtomicBoolean existAliveTask = new AtomicBoolean(false);
/*     */     
/* 133 */     Map<String, JSONObject> executionTasks = new ConcurrentHashMap<>();
/*     */     
/* 135 */     Map<String, Object[]> sysIdentityMap = (Map)new ConcurrentHashMap<>();
/* 136 */     Map<String, List<SysIdentity>> opinionSysIdentity = new ConcurrentHashMap<>();
/* 137 */     taskStacks.stream().filter(bpmTaskStack -> (bpmTaskStack.getLevel().intValue() < 5))
/* 138 */       .filter(bpmTaskStack -> (StringUtils.equals(bpmTaskStack.getNodeType(), "userTask") || StringUtils.equals(bpmTaskStack.getNodeType(), "callActivity")))
/*     */       
/* 140 */       .forEach(bpmTaskStack -> {
/*     */           JSONObject executionTask = (JSONObject)executionTasks.get(bpmTaskStack.getNodeId());
/*     */           
/*     */           if (executionTask == null) {
/*     */             executionTask = new JSONObject();
/*     */             
/*     */             executionTask.put("tasks", new JSONArray());
/*     */             
/*     */             executionTask.put("existAliveTask", Boolean.valueOf(false));
/*     */             
/*     */             executionTasks.put(bpmTaskStack.getNodeId(), executionTask);
/*     */           } 
/*     */           
/*     */           AtomicBoolean executionExistAliveTask = new AtomicBoolean(false);
/*     */           
/*     */           AtomicBoolean taskAlive = new AtomicBoolean(false);
/*     */           
/*     */           BpmNodeDef bpmNodeDef = (BpmNodeDef)executionNodeDefs.get(bpmInstance.getDefId() + "-" + bpmTaskStack.getNodeId());
/*     */           
/*     */           if (bpmNodeDef == null) {
/*     */             return;
/*     */           }
/*     */           
/*     */           BpmTaskOpinion bpmTaskOpinion = null;
/*     */           
/*     */           String taskType = "none";
/*     */           
/*     */           if (StringUtils.equals(bpmTaskStack.getNodeType(), "callActivity")) {
/*     */             if (!isDynamic.get()) {
/*     */               return;
/*     */             }
/*     */             
/*     */             DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
/*     */             
/*     */             if (dynamicTaskPluginDef.getEnabled().booleanValue()) {
/*     */               taskType = "dynamic";
/*     */             } else {
/*     */               return;
/*     */             } 
/*     */             
/*     */             List<BpmTaskStack> subStacks = (List<BpmTaskStack>)executionStacks.get(bpmTaskStack.getId());
/*     */             
/*     */             if (CollectionUtil.isEmpty(subStacks) || subStacks.size() < 2) {
/*     */               return;
/*     */             }
/*     */             
/*     */             BpmTaskStack subFirstNodeStack = null;
/*     */             
/*     */             for (int i = 1; i < subStacks.size(); i++) {
/*     */               if (StringUtils.equals(((BpmTaskStack)subStacks.get(i)).getNodeType(), "userTask") && !StringUtils.equals(((BpmTaskStack)subStacks.get(i)).getInstId(), bpmTaskStack.getInstId())) {
/*     */                 subFirstNodeStack = subStacks.get(i);
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */             
/*     */             BpmInstance subInst = (BpmInstance)instances.get(subFirstNodeStack.getInstId());
/*     */             
/*     */             BpmNodeDef subBpmNodeDef = (BpmNodeDef)executionNodeDefs.get(subInst.getDefId() + "-" + subFirstNodeStack.getNodeId());
/*     */             
/*     */             bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(subFirstNodeStack.getTaskId());
/*     */             
/*     */             if (bpmTaskStack.getEndTime() == null && dynamicTaskPluginDef.isReset()) {
/*     */               if (bpmTaskOpinion.getApproveTime() == null) {
/*     */                 existAliveTask.set(true);
/*     */                 
/*     */                 executionExistAliveTask.set(true);
/*     */                 
/*     */                 taskAlive.set(true);
/*     */               } else if (dynamicTaskPluginDef.isCanBeRecycledEnd() && bpmTaskStack.getEndTime() == null) {
/*     */                 existAliveTask.set(true);
/*     */                 
/*     */                 executionExistAliveTask.set(true);
/*     */                 
/*     */                 taskAlive.set(true);
/*     */               } 
/*     */             }
/*     */             
/*     */             executionTask.put("existAliveTask", Boolean.valueOf(executionExistAliveTask.get()));
/*     */             
/*     */             executionTask.put("defId", subInst.getDefId());
/*     */             
/*     */             executionTask.put("nodeId", bpmTaskOpinion.getTaskKey());
/*     */           } 
/*     */           
/*     */           SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)((SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class)).getBpmPluginDef();
/*     */           
/*     */           if (signTaskPluginDef.isSignMultiTask()) {
/*     */             taskType = "sign";
/*     */             
/*     */             if (bpmTaskStack.getEndTime() == null && signTaskPluginDef.isReset()) {
/*     */               existAliveTask.set(true);
/*     */               
/*     */               executionTask.put("existAliveTask", Boolean.valueOf(true));
/*     */             } 
/*     */             
/*     */             executionTask.put("defId", bpmInstance.getDefId());
/*     */             
/*     */             executionTask.put("nodeId", bpmTaskStack.getNodeId());
/*     */           } 
/*     */           
/*     */           executionTask.put("taskName", bpmNodeDef.getName());
/*     */           
/*     */           if (StringUtils.equals(taskType, "none")) {
/*     */             BpmNodeDef supNodeDef = bpmNodeDef.getParentBpmNodeDef();
/*     */             
/*     */             bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTaskStack.getTaskId());
/*     */             
/*     */             if (supNodeDef != null && supNodeDef instanceof SubProcessNodeDef) {
/*     */               DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)((DynamicTaskPluginContext)supNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
/*     */               
/*     */               if (!dynamicTaskPluginDef.getEnabled().booleanValue()) {
/*     */                 return;
/*     */               }
/*     */               
/*     */               taskType = "dynamic";
/*     */               
/*     */               executionTask.put("taskName", supNodeDef.getName());
/*     */               
/*     */               if (bpmTaskStack.getEndTime() == null) {
/*     */                 taskAlive.set(true);
/*     */                 
/*     */                 existAliveTask.set(true);
/*     */                 
/*     */                 executionExistAliveTask.set(true);
/*     */               } else if (dynamicTaskPluginDef.isCanBeRecycledEnd()) {
/*     */                 executionExistAliveTask.set(true);
/*     */                 
/*     */                 List<BpmNodeDef> endNodeDefs = ((SubProcessNodeDef)supNodeDef).getChildBpmProcessDef().getEndEvents();
/*     */                 
/*     */                 List<BpmTaskStack> executionStack = (List<BpmTaskStack>)executionStacks.get(bpmTaskStack.getId());
/*     */                 executionStack.parallelStream().forEach(());
/*     */                 if (executionExistAliveTask.get()) {
/*     */                   existAliveTask.set(true);
/*     */                   taskAlive.set(true);
/*     */                 } 
/*     */               } 
/*     */             } else {
/*     */               DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
/*     */               if (dynamicTaskPluginDef.getEnabled().booleanValue()) {
/*     */                 taskType = "dynamic";
/*     */               } else {
/*     */                 return;
/*     */               } 
/*     */               if (bpmTaskStack.getEndTime() == null) {
/*     */                 existAliveTask.set(true);
/*     */                 executionExistAliveTask.set(true);
/*     */                 taskAlive.set(true);
/*     */               } 
/*     */             } 
/*     */             executionTask.put("existAliveTask", Boolean.valueOf(executionExistAliveTask.get()));
/*     */             executionTask.put("defId", bpmInstance.getDefId());
/*     */             executionTask.put("nodeId", bpmTaskStack.getNodeId());
/*     */           } 
/*     */           executionTask.put("taskType", taskType);
/*     */           JSONArray tasks = executionTask.getJSONArray("tasks");
/*     */           if (StringUtils.equals(taskType, "sign")) {
/*     */             String key = bpmNodeDef.getBpmProcessDef().getProcessDefinitionId() + bpmNodeDef.getNodeId();
/*     */             Object[] nodeSysIdentity = (Object[])sysIdentityMap.get(key);
/*     */             if (nodeSysIdentity == null) {
/*     */               List<SysIdentity> list = new ArrayList<>();
/*     */               List<JSONObject> list1 = new ArrayList<>();
/*     */               nodeSysIdentity = new Object[] { bpmNodeDef, list, list1 };
/*     */               sysIdentityMap.put(key, nodeSysIdentity);
/*     */             } 
/*     */             List<SysIdentity> sysIdentities = (List<SysIdentity>)nodeSysIdentity[1];
/*     */             List<JSONObject> opinions = (List<JSONObject>)nodeSysIdentity[2];
/*     */             List<BpmTaskOpinion> bpmTaskOpinions = this.bpmTaskOpinionManager.getByInstAndSignId(bpmInstance.getId(), bpmTaskStack.getTaskId());
/*     */             if (!CollectionUtils.isEmpty(bpmTaskOpinions)) {
/*     */               bpmTaskOpinions.forEach(());
/*     */             }
/*     */           } else {
/*     */             JSONObject approve = new JSONObject();
/*     */             approve.put("id", bpmTaskOpinion.getId());
/*     */             approve.put("taskId", bpmTaskOpinion.getTaskId());
/*     */             approve.put("instId", bpmTaskOpinion.getInstId());
/*     */             approve.put("nodeId", bpmTaskOpinion.getTaskKey());
/*     */             approve.put("taskName", bpmTaskOpinion.getTaskName());
/*     */             approve.put("approver", bpmTaskOpinion.getApprover());
/*     */             approve.put("approverName", bpmTaskOpinion.getApproverName());
/*     */             approve.put("approveTime", bpmTaskOpinion.getApproveTime());
/*     */             approve.put("existAliveTask", Boolean.valueOf(taskAlive.get()));
/*     */             if (StringUtils.isNotEmpty(bpmTaskOpinion.getAssignInfo())) {
/*     */               String key = bpmNodeDef.getBpmProcessDef().getProcessDefinitionId() + bpmNodeDef.getNodeId();
/*     */               Object[] nodeSysIdentity = sysIdentityMap.get(key);
/*     */               if (nodeSysIdentity == null) {
/*     */                 List<SysIdentity> list = new ArrayList<>();
/*     */                 List<JSONObject> list1 = new ArrayList<>();
/*     */                 BpmNodeDef subBpmNodeDef = (BpmNodeDef)executionNodeDefs.get(executionTask.getString("defId") + "-" + bpmTaskOpinion.getTaskKey());
/*     */                 nodeSysIdentity = new Object[] { subBpmNodeDef, list, list1 };
/*     */                 sysIdentityMap.put(key, nodeSysIdentity);
/*     */               } 
/*     */               List<SysIdentity> sysIdentities = (List<SysIdentity>)nodeSysIdentity[1];
/*     */               List<JSONObject> opinions = (List<JSONObject>)nodeSysIdentity[2];
/*     */               List<SysIdentity> users = new ArrayList<>();
/*     */               opinions.add(approve);
/*     */               opinionSysIdentity.put(bpmTaskOpinion.getId(), users);
/*     */               for (String info : Arrays.<String>asList(bpmTaskOpinion.getAssignInfo().split(","))) {
/*     */                 if (StringUtils.isNotEmpty(info)) {
/*     */                   if (StringUtils.startsWith(info, "user")) {
/*     */                     String[] user = info.split("-");
/*     */                     DefaultIdentity defaultIdentity1 = new DefaultIdentity(user[2], user[1], "user", user[3]);
/*     */                     sysIdentities.add(defaultIdentity1);
/*     */                     users.add(defaultIdentity1);
/*     */                     continue;
/*     */                   } 
/*     */                   String[] group = info.split("-");
/*     */                   DefaultIdentity defaultIdentity = new DefaultIdentity(group[2], group[1], group[0], group[3]);
/*     */                   sysIdentities.add(defaultIdentity);
/*     */                   users.add(defaultIdentity);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             tasks.add(approve);
/*     */           } 
/*     */         });
/* 356 */     stackTask.put("existAliveTask", Boolean.valueOf(existAliveTask.get()));
/* 357 */     if (existAliveTask.get() && sysIdentityMap.size() > 0) {
/* 358 */       sysIdentityMap.forEach((key, value) -> {
/*     */             List<SysIdentity> lists = (List<SysIdentity>)value[1];
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             BpmNodeDef bpmNodeDef = (BpmNodeDef)value[0];
/*     */ 
/*     */ 
/*     */             
/*     */             List<JSONObject> opinions = (List<JSONObject>)value[2];
/*     */ 
/*     */ 
/*     */             
/*     */             lists = BpmTaskShowUtil.appendOrgUser(lists, bpmNodeDef, "--");
/*     */ 
/*     */ 
/*     */             
/*     */             opinions.forEach(());
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 382 */     stackTask.put("tasks", executionTasks);
/* 383 */     return stackTask; } @Resource
/*     */   private BpmInstanceManager bpmInstanceManager; @Resource
/*     */   private BpmProcessDefService bpmProcessDefService; @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager; private List<DynamicTask> getDynamicTasks(String taskId) {
/* 387 */     List<DynamicTask> dynamicTasks = this.dynamicTaskManager.getByTaskId(taskId);
/* 388 */     return dynamicTasks;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/recrease/handler/RecreaseTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */