/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.ExceptionUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.dstz.bpm.act.service.ActTaskService;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowBatchRequestParam;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
/*     */ import com.dstz.bpm.api.engine.data.result.BpmFlowData;
/*     */ import com.dstz.bpm.api.engine.data.result.FlowData;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.def.BpmDataModel;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultTaskActionBatchCmd;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.engine.plugin.service.BpmPluginService;
/*     */ import com.dstz.bpm.plugin.global.taskskip.context.TaskSkipPluginContext;
/*     */ import com.dstz.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.usercalc.util.UserCalcPreview;
/*     */ import com.dstz.bus.api.model.IBusinessData;
/*     */ import com.dstz.bus.api.service.IBusinessDataService;
/*     */ import com.dstz.form.api.model.FormType;
/*     */ import com.dstz.org.api.model.IGroup;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.GroupService;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.thread.ThreadUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiImplicitParam;
/*     */ import io.swagger.annotations.ApiImplicitParams;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.collections.map.HashedMap;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/bpm/task"})
/*     */ @Api(description = "流程任务相关服务接口")
/*     */ public class BpmTaskController
/*     */   extends ControllerTools {
/*     */   @Resource
/*     */   BpmTaskManager bpmTaskManager;
/*     */   @Resource
/*     */   BpmFlowDataAccessor bpmFlowDataAccessor;
/*     */   @Autowired
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   @Autowired
/*     */   BpmInstanceManager bpmInstanceMananger;
/*     */   @Autowired
/*     */   IBpmBusDataHandle bpmBusDataHandle;
/*     */   @Autowired
/*     */   BpmPluginService bpmPluginService;
/*     */   @Autowired
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Autowired
/*     */   IBusinessDataService iBusinessDataService;
/*     */   @Resource
/*     */   private GroupService groupService;
/*     */   
/*     */   @RequestMapping(value = {"listJson"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "流程任务列表", notes = "获取流程任务的列表数据，用于管理员管理任务，入参“filter”为数据库过滤字段名，支持对title_,name_等任务字段过滤，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmTask> listJson(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 106 */     QueryFilter queryFilter = getQueryFilter(request);
/* 107 */     List<BpmTask> bpmTaskList = this.bpmTaskManager.query(queryFilter);
/* 108 */     return new PageResult(bpmTaskList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getBpmTask"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程任务", notes = "获取流程任务信息")
/*     */   public ResultMsg<BpmTask> getBpmTask(@RequestParam @ApiParam(value = "任务ID", required = true) String id) throws Exception {
/* 118 */     BpmTask bpmTask = null;
/* 119 */     if (StringUtil.isNotEmpty(id)) {
/* 120 */       bpmTask = (BpmTask)this.bpmTaskManager.get(id);
/*     */     }
/*     */     
/* 123 */     return getSuccessResult(bpmTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"remove"})
/*     */   @CatchErr("删除流程任务失败")
/*     */   public ResultMsg<String> remove(@RequestParam String id) throws Exception {
/* 132 */     String[] aryIds = StringUtil.getStringAryByStr(id);
/*     */     
/* 134 */     this.bpmTaskManager.removeByIds((Serializable[])aryIds);
/*     */     
/* 136 */     return getSuccessResult("删除流程任务成功");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getTaskData"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "获取流程任务相关数据", notes = "获取任务的业务数据、表单、按钮、权限等信息，为了渲染展示任务页面")
/*     */   public ResultMsg<FlowData> getTaskData(@RequestParam @ApiParam(value = "任务ID", required = true) String taskId, @RequestParam(required = false) @ApiParam(value = "表单类型", defaultValue = "pc") String formType) throws Exception {
/* 145 */     if (StringUtil.isEmpty(formType)) {
/* 146 */       formType = FormType.PC.value();
/*     */     }
/*     */     
/* 149 */     BpmFlowData bpmFlowData = this.bpmFlowDataAccessor.getFlowTaskData(taskId, FormType.fromValue(formType));
/* 150 */     return getSuccessResult(bpmFlowData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"doAction"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "执行任务相关动作", notes = "执行任务相关的动作 如：同意，驳回，反对，锁定，解锁，转办，会签任务等相关操作")
/*     */   public ResultMsg<String> doAction(@RequestBody FlowRequestParam flowParam) throws Exception {
/* 165 */     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
/* 166 */     String result = taskModel.executeCmd();
/* 167 */     return getSuccessResult(result);
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"doBatchAction"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "批量执行任务动作", notes = "批量执行任务操作")
/*     */   public ResultMsg<String> doBatchAction(@RequestBody FlowBatchRequestParam flowParam) throws Exception {
/* 174 */     DefaultTaskActionBatchCmd batchCmd = new DefaultTaskActionBatchCmd();
/* 175 */     batchCmd.executeCmd(flowParam);
/* 176 */     return getSuccessResult("执行成功");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"unLock"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "任务取消指派", notes = "管理员将任务取消指派，若任务原先无候选人，则无法取消指派。")
/*     */   public ResultMsg<String> unLock(@RequestParam @ApiParam(value = "任务ID", required = true) String taskId) throws Exception {
/* 183 */     this.bpmTaskManager.unLockTask(taskId);
/* 184 */     return getSuccessResult("取消指派成功");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"assignTask"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "任务指派", notes = "管理员将任务指派给某一个用户处理")
/*     */   public ResultMsg<String> assignTask(@RequestParam String taskId, @RequestParam String userName, @RequestParam String userId) throws Exception {
/* 192 */     this.bpmTaskManager.assigneeTask(taskId, userId, userName);
/* 193 */     return getSuccessResult("指派成功");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"handleNodeFreeSelectUser"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "处理节点 【自由选择候选人】功能", hidden = true, notes = "根据配置，处理节点可自由选择下一个节点的执行人的逻辑")
/*     */   public ResultMsg<Map<String, Object>> handleNodeFreeSelectUser(@RequestBody FlowRequestParam flowParam) throws Exception {
/* 201 */     BpmContext.cleanTread();
/* 202 */     HashedMap<String, String> hashedMap = new HashedMap();
/*     */     
/* 204 */     if (StringUtil.isEmpty(flowParam.getTaskId()) && StringUtil.isNotEmpty(flowParam.getDefId())) {
/* 205 */       getStartNodeSetting(flowParam, (Map)hashedMap);
/* 206 */       return getSuccessResult(hashedMap);
/*     */     } 
/*     */ 
/*     */     
/* 210 */     BpmTask task = (BpmTask)this.bpmTaskManager.get(flowParam.getTaskId());
/* 211 */     if (task == null) {
/* 212 */       throw new BusinessException(BpmStatusCode.TASK_NOT_FOUND);
/*     */     }
/*     */     
/* 215 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
/* 216 */     String freeSelectUser = nodeDef.getNodeProperties().getFreeSelectUser();
/* 217 */     hashedMap.put("type", freeSelectUser);
/* 218 */     boolean freeSelectNode = nodeDef.getNodeProperties().isFreeSelectNode();
/* 219 */     hashedMap.put("freeSelectNode", Boolean.valueOf(freeSelectNode));
/*     */     
/* 221 */     if (!"no".equals(freeSelectUser) || freeSelectNode) {
/* 222 */       handleNodeInfo(flowParam, task, nodeDef, (Map)hashedMap, freeSelectUser);
/*     */     }
/*     */     
/* 225 */     return getSuccessResult(hashedMap);
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"getNodeDefaultUser"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "获取节默认候选人", hidden = true, notes = "根据配置，获取节点的执行人的逻辑")
/*     */   public ResultMsg<List<SysIdentity>> getNodeDefaultUser(@RequestParam String defId, @RequestParam String nodeId) {
/* 232 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 233 */     if (bpmProcessDef == null) {
/* 234 */       return null;
/*     */     }
/* 236 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, nodeId);
/* 237 */     if (bpmNodeDef == null) {
/* 238 */       return null;
/*     */     }
/* 240 */     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd();
/* 241 */     taskModel.setActionName("start");
/*     */     
/* 243 */     BpmContext.setActionModel((ActionCmd)taskModel);
/* 244 */     return getSuccessResult(appendOrgUser(UserCalcPreview.calcNodeUsers(bpmNodeDef, taskModel)));
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"getCanBackHistoryNodes"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "获取可驳回的节点", hidden = true, notes = "根据任务ID 获取下个节点有哪些可驳回节点")
/*     */   public ResultMsg<Map<String, String>> getCanBackHistoryNodes(@RequestParam String taskId) throws Exception {
/* 251 */     BpmTask task = (BpmTask)this.bpmTaskManager.get(taskId);
/*     */     
/* 253 */     if (task == null) {
/* 254 */       throw new BusinessException(BpmStatusCode.TASK_NOT_FOUND);
/*     */     }
/*     */     
/* 257 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
/*     */     
/* 259 */     Boolean isFreeBack = Boolean.valueOf(nodeDef.getNodeProperties().isFreeBack());
/* 260 */     if (!isFreeBack.booleanValue()) {
/* 261 */       return new ResultMsg();
/*     */     }
/* 263 */     Map<String, String> nodeList = new HashMap<>();
/* 264 */     List<BpmTaskOpinion> opinionList = this.bpmTaskOpinionManager.getByInstId(task.getInstId());
/* 265 */     opinionList.forEach(opinion -> {
/*     */           if (opinion.getDurMs().longValue() > 1L) {
/*     */             if (isDynamicTask(task, opinion.getTaskKey())) {
/*     */               nodeList.put(opinion.getTaskKey().concat("$$").concat(opinion.getTaskName()), opinion.getTaskName());
/*     */             } else {
/*     */               nodeList.put(opinion.getTaskKey(), opinion.getTaskName());
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 276 */     return getSuccessResult(nodeList);
/*     */   }
/*     */   
/*     */   private boolean isDynamicTask(BpmTask task, String nodeId) {
/* 280 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), nodeId);
/*     */     
/* 282 */     DynamicTaskPluginContext dynamicTaskContext = (DynamicTaskPluginContext)nodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 283 */     if (dynamicTaskContext != null && ((DynamicTaskPluginDef)dynamicTaskContext.getBpmPluginDef()).getIsEnabled().booleanValue()) {
/* 284 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 288 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleNodeInfo(FlowRequestParam flowParam, BpmTask task, BpmNodeDef nodeDef, Map<String, Object> result, String freeSelectUser) {
/* 299 */     IBpmInstance instance = (IBpmInstance)this.bpmInstanceMananger.get(task.getInstId());
/* 300 */     Map<String, IBusinessData> map = this.bpmBusDataHandle.getInstanceBusData(task.getInstId(), null);
/*     */     
/* 302 */     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
/* 303 */     taskModel.setBpmTask((IBpmTask)task);
/* 304 */     taskModel.setBpmInstance(instance);
/* 305 */     taskModel.setBizDataMap(map);
/*     */ 
/*     */     
/* 308 */     BpmContext.setActionModel((ActionCmd)taskModel);
/* 309 */     HashedMap<String, String> hashedMap = new HashedMap();
/* 310 */     HashedMap<String, List<SysIdentity>> hashedMap1 = new HashedMap();
/*     */     
/* 312 */     for (BpmNodeDef node : nodeDef.getOutcomeTaskNodes()) {
/* 313 */       hashedMap.put(node.getNodeId(), node.getName());
/* 314 */       hashedMap1.put(node.getNodeId(), appendOrgUser(UserCalcPreview.calcNodeUsers(node, taskModel)));
/*     */     } 
/*     */     
/* 317 */     result.put("nodeIdentitysMap", hashedMap1);
/* 318 */     result.put("nodeNameMap", hashedMap);
/* 319 */     BpmContext.cleanTread();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getStartNodeSetting(FlowRequestParam flowParam, Map<String, Object> result) {
/* 329 */     String defId = flowParam.getDefId();
/* 330 */     List<BpmNodeDef> firstNodes = this.bpmProcessDefService.getStartNodes(defId);
/* 331 */     if (CollectionUtil.isEmpty(firstNodes)) {
/*     */       return;
/*     */     }
/*     */     
/* 335 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 336 */     TaskSkipPluginDef pluginDef = (TaskSkipPluginDef)def.getBpmPluginContext(TaskSkipPluginContext.class).getBpmPluginDef();
/*     */     
/* 338 */     BpmNodeDef startNode = this.bpmProcessDefService.getStartEvent(defId);
/* 339 */     BpmNodeDef firstNode = startNode;
/* 340 */     Boolean isFirestNode = this.bpmPluginService.isSkipFirstNode(defId);
/*     */     
/* 342 */     if (isFirestNode.booleanValue() && startNode.getOutcomeNodes().size() == 1) {
/* 343 */       firstNode = startNode.getOutcomeNodes().get(0);
/*     */     }
/*     */     
/* 346 */     String freeSelectUser = startNode.getNodeProperties().getFreeSelectUser();
/* 347 */     result.put("type", freeSelectUser);
/* 348 */     boolean freeSelectNode = startNode.getNodeProperties().isFreeSelectNode();
/* 349 */     result.put("freeSelectNode", Boolean.valueOf(freeSelectNode));
/*     */     
/* 351 */     if ("no".equals(freeSelectUser) && !freeSelectNode) {
/*     */       return;
/*     */     }
/* 354 */     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd();
/* 355 */     JSONObject busData = flowParam.getData();
/* 356 */     taskModel.setActionName("start");
/* 357 */     taskModel.setBusData(busData);
/* 358 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/*     */     
/* 360 */     for (BpmDataModel dataModel : bpmProcessDef.getDataModelList()) {
/* 361 */       String modelCode = dataModel.getCode();
/* 362 */       if (busData.containsKey(modelCode)) {
/* 363 */         IBusinessData businessData = this.iBusinessDataService.parseBusinessData(busData.getJSONObject(modelCode), modelCode);
/* 364 */         taskModel.getBizDataMap().put(modelCode, businessData);
/*     */       } 
/*     */     } 
/*     */     
/* 368 */     BpmContext.setActionModel((ActionCmd)taskModel);
/* 369 */     Map<String, String> nodeNameMap = new HashMap<>(startNode.getOutcomeTaskNodes().size());
/* 370 */     Map<String, List<SysIdentity>> nodeIdentitysMap = new HashMap<>();
/* 371 */     for (BpmNodeDef node : firstNode.getOutcomeTaskNodes()) {
/* 372 */       nodeNameMap.put(node.getNodeId(), node.getName());
/* 373 */       nodeIdentitysMap.put(node.getNodeId(), appendOrgUser(UserCalcPreview.calcNodeUsers(node, taskModel)));
/*     */     } 
/*     */     
/* 376 */     result.put("nodeNameMap", nodeNameMap);
/* 377 */     result.put("nodeIdentitysMap", nodeIdentitysMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getVariables"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程任务变量", notes = "获取流程变量")
/*     */   public ResultMsg<Map> getVariables(@RequestParam @ApiParam(value = "任务ID", required = true) String taskId, @RequestParam @ApiParam(value = "isLocal", defaultValue = "false") Boolean isLocal) throws Exception {
/* 387 */     if (!isLocal.booleanValue()) return getSuccessResult(((ActTaskService)AppUtil.getBean(ActTaskService.class)).getVariables(taskId));
/*     */     
/* 389 */     return getSuccessResult(((ActTaskService)AppUtil.getBean(ActTaskService.class)).getVariablesLocal(taskId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<SysIdentity> appendOrgUser(List<SysIdentity> sysIdentities) {
/* 396 */     sysIdentities.forEach(user -> {
/*     */           IGroup group = this.groupService.getMainGroup(user.getId());
/*     */           if (group != null) {
/*     */             user.setName(user.getName() + "(" + group.getGroupName() + ")");
/*     */           }
/*     */         });
/* 402 */     return sysIdentities;
/*     */   }
/*     */   
/*     */   @PostMapping({"doActions"})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "批量执行任务相关动作", notes = "批量执行任务相关的动作 如：同意，驳回，反对，锁定，解锁，转办，会签任务等相关操作")
/*     */   public ResultMsg<Object> doActions(@RequestBody List<FlowRequestParam> flowParams) throws Exception {
/* 409 */     JSONArray results = new JSONArray();
/* 410 */     final IUser curUser = ContextUtil.getCurrentUser();
/* 411 */     List<Future<JSONObject>> futures = new ArrayList<>();
/* 412 */     ExecutorService executorService = ThreadUtil.newExecutor(flowParams.size());
/* 413 */     flowParams.forEach(flowParam -> {
/*     */           Future<JSONObject> future = executorService.submit(new Callable<JSONObject>()
/*     */               {
/*     */                 
/*     */                 public JSONObject call() throws Exception
/*     */                 {
/*     */                   try {
/* 420 */                     JSONObject result = new JSONObject();
/* 421 */                     ContextUtil.setCurrentUser(curUser);
/* 422 */                     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
/* 423 */                     String actionName = taskModel.executeCmd();
/* 424 */                     result.put("flag", "1");
/* 425 */                     result.put("msg", actionName + "成功");
/* 426 */                     return result;
/*     */                   } finally {
/* 428 */                     ContextUtil.clearAll();
/*     */                   } 
/*     */                 }
/*     */               });
/*     */           
/*     */           futures.add(future);
/*     */         });
/* 435 */     futures.forEach(future -> {
/*     */           try {
/*     */             results.add(future.get());
/* 438 */           } catch (Exception e) {
/*     */             JSONObject result = new JSONObject();
/*     */             
/*     */             result.put("flag", "0");
/*     */             result.put("msg", ExceptionUtil.getRootErrorMseeage(e));
/*     */             results.add(result);
/*     */           } 
/*     */         });
/* 446 */     executorService.shutdown();
/* 447 */     return getSuccessResult(results, "批量操作成功");
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/rest/controller/BpmTaskController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */