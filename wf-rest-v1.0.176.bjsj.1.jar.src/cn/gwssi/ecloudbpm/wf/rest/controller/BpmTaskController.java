/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import cn.gwssi.ecloudbpm.form.api.model.FormType;
/*     */ import cn.gwssi.ecloudbpm.service.BpmDecentralizationService;
/*     */ import cn.gwssi.ecloudbpm.service.BpmSomeService;
/*     */ import com.dstz.bpm.act.service.ActTaskService;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowBatchRequestParam;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
/*     */ import com.dstz.bpm.api.engine.data.result.BpmFlowTaskData;
/*     */ import com.dstz.bpm.api.engine.data.result.FlowData;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.Button;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.vo.BpmTaskVO;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultTaskActionBatchCmd;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.engine.plugin.service.BpmPluginService;
/*     */ import com.dstz.bpm.engine.util.BpmTaskShowUtil;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.usercalc.util.UserCalcPreview;
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.aop.annotion.OperateLog;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.ExceptionUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.thread.ThreadUtil;
/*     */ import com.alibaba.fastjson.JSON;
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
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
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
/*     */   
/*     */   @GetMapping({"get"})
/*     */   @ApiOperation("获取")
/*     */   public ResultMsg get(@RequestParam String id) {
/* 107 */     return ResultMsg.SUCCESS(this.bpmTaskManager.get(id));
/*     */   }
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   BpmPluginService bpmPluginService;
/*     */   @Autowired
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Autowired
/*     */   IBusinessDataService iBusinessDataService;
/*     */   
/*     */   @RequestMapping(value = {"listJson"}, method = {RequestMethod.POST})
/*     */   @OperateLog
/*     */   @ApiOperation(value = "流程任务列表", notes = "获取流程任务的列表数据，用于管理员管理任务，入参“filter”为数据库过滤字段名，支持对title_,name_等任务字段过滤，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "orgId", value = "机构id"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmTask> listJson(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 123 */     QueryFilter queryFilter = getQueryFilter(request);
/* 124 */     if (this.decentralizationService.decentralizationEnable("wf")) {
/* 125 */       IUser user = this.iCurrentContext.getCurrentUser();
/* 126 */       List<String> lstOrgId = new ArrayList<>();
/* 127 */       if (null != user) {
/* 128 */         lstOrgId = user.getManagerGroupIdList();
/*     */       }
/* 130 */       if (null == lstOrgId || lstOrgId.size() == 0) {
/* 131 */         lstOrgId = new ArrayList<>();
/* 132 */         lstOrgId.add("");
/*     */       } 
/* 134 */       queryFilter.getParams().put("orgIds", lstOrgId);
/*     */     } 
/* 136 */     String orgId = request.getParameter("orgId");
/* 137 */     if (StringUtils.isNotEmpty(orgId)) {
/* 138 */       queryFilter.getParams().put("orgId", orgId);
/*     */     }
/* 140 */     List<BpmTask> bpmTaskList = this.bpmTaskManager.query(queryFilter);
/* 141 */     return new PageResult(bpmTaskList);
/*     */   }
/*     */   
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   
/*     */   @RequestMapping(value = {"getBpmTask"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程任务", notes = "获取流程任务信息")
/*     */   public ResultMsg<BpmTask> getBpmTask(@RequestParam @ApiParam(value = "任务ID", required = true) String id) throws Exception {
/* 151 */     BpmTask bpmTask = null;
/* 152 */     if (StringUtil.isNotEmpty(id)) {
/* 153 */       bpmTask = (BpmTask)this.bpmTaskManager.get(id);
/*     */     }
/*     */     
/* 156 */     return getSuccessResult(bpmTask);
/*     */   }
/*     */   
/*     */   @Autowired
/*     */   BpmDecentralizationService decentralizationService;
/*     */   
/*     */   @RequestMapping({"remove"})
/*     */   @CatchErr("删除流程任务失败")
/*     */   public ResultMsg<String> remove(@RequestParam String id) throws Exception {
/* 165 */     String[] aryIds = StringUtil.getStringAryByStr(id);
/*     */     
/* 167 */     this.bpmTaskManager.removeByIds((Serializable[])aryIds);
/*     */     
/* 169 */     return getSuccessResult("删除流程任务成功");
/*     */   }
/*     */ 
/*     */   
/*     */   @Resource
/*     */   private BpmSomeService bpmSomeService;
/*     */   
/*     */   @RequestMapping(value = {"getTaskData"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr(write2errorlog = true)
/*     */   @OperateLog
/*     */   @ApiOperation(value = "获取流程任务相关数据", notes = "获取任务的业务数据、表单、按钮、权限等信息，为了渲染展示任务页面")
/*     */   public ResultMsg<FlowData> getTaskData(@RequestParam @ApiParam(value = "任务ID", required = true) String taskId, @RequestParam(required = false) @ApiParam(value = "表单类型", defaultValue = "pc") String formType, @RequestParam(required = false) @ApiParam("排除的按钮,多个按钮用逗号拼接") String excludeBtnJson, @RequestParam(required = false) @ApiParam("任务候选人关系id") String taskLinkId) throws Exception {
/* 181 */     if (StringUtil.isEmpty(formType)) {
/* 182 */       formType = FormType.PC.value();
/*     */     }
/* 184 */     BpmFlowTaskData data = (BpmFlowTaskData)this.bpmFlowDataAccessor.getFlowTaskData(taskId, taskLinkId, FormType.fromValue(formType));
/* 185 */     if (StringUtil.isNotEmpty(excludeBtnJson)) {
/* 186 */       String[] excludeBtnList = excludeBtnJson.split(",");
/* 187 */       data.setButtonList((List)data.getButtonList().stream().filter(btn -> (ArrayUtils.indexOf((Object[])excludeBtnList, btn.getAlias()) == -1)).collect(Collectors.toList()));
/*     */     } 
/* 189 */     return getSuccessResult(data);
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
/*     */   @CatchErr(write2errorlog = true)
/*     */   @ApiOperation(value = "执行任务相关动作", notes = "执行任务相关的动作 如：同意，驳回，反对，锁定，解锁，转办，会签任务等相关操作")
/*     */   public ResultMsg<String> doAction(@RequestBody FlowRequestParam flowParam) throws Exception {
/* 204 */     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
/* 205 */     String result = taskModel.executeCmd();
/* 206 */     return getSuccessResult(result);
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"doBatchAction"}, method = {RequestMethod.POST})
/*     */   @CatchErr(write2errorlog = true)
/*     */   @ApiOperation(value = "批量执行任务动作", notes = "批量执行任务操作")
/*     */   public ResultMsg<String> doBatchAction(@RequestBody FlowBatchRequestParam flowParam) throws Exception {
/* 213 */     DefaultTaskActionBatchCmd batchCmd = new DefaultTaskActionBatchCmd();
/* 214 */     batchCmd.executeCmd(flowParam);
/* 215 */     return getSuccessResult("执行成功");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"unLock"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "任务取消指派", notes = "管理员将任务取消指派，若任务原先无候选人，则无法取消指派。")
/*     */   public ResultMsg<String> unLock(@RequestParam @ApiParam(value = "任务ID", required = true) String taskId) throws Exception {
/* 222 */     this.bpmTaskManager.unLockTask(taskId);
/* 223 */     return getSuccessResult("取消指派成功");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"assignTask"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "任务指派", notes = "管理员将任务指派给某一个用户处理")
/*     */   public ResultMsg<String> assignTask(@RequestBody JSONObject params) throws Exception {
/* 230 */     String taskId = params.getString("taskId");
/* 231 */     JSONArray jsonArray = params.getJSONArray("sysIdentities");
/* 232 */     List<SysIdentity> sysIdentities = new ArrayList<>();
/* 233 */     if (jsonArray != null) {
/* 234 */       jsonArray.forEach(obj -> {
/*     */             JSONObject user = (JSONObject)obj;
/*     */             String clazzStr = user.getString("clazz");
/*     */             Class<SysIdentity> clazz = SysIdentity.class;
/*     */             if (StringUtils.isNotEmpty(clazzStr)) {
/*     */               ServiceLoader<SysIdentity> loader = ServiceLoader.load(SysIdentity.class);
/*     */               for (SysIdentity sysIdentity : loader) {
/*     */                 System.out.println(sysIdentity.getClass());
/*     */                 if (StringUtils.equals(clazzStr, sysIdentity.getClass().getSimpleName())) {
/*     */                   clazz = (Class)sysIdentity.getClass();
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             SysIdentity bpmInentity = (SysIdentity)JSON.toJavaObject((JSON)user, clazz);
/*     */             sysIdentities.add(bpmInentity);
/*     */           });
/*     */     }
/* 252 */     this.bpmTaskManager.assigneeTask(taskId, sysIdentities);
/* 253 */     return getSuccessResult("指派成功");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"handleNodeFreeSelectUser"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr(write2response = true)
/*     */   @ApiOperation(value = "处理节点 【自由选择候选人】功能", hidden = true, notes = "根据配置，处理节点可自由选择下一个节点的执行人的逻辑")
/*     */   public ResultMsg<Map<String, Object>> handleNodeFreeSelectUser(@RequestBody FlowRequestParam flowParam) throws Exception {
/* 261 */     Map<String, Object> result = this.bpmSomeService.handleNodeFreeSelectUser(flowParam);
/* 262 */     String taskId = flowParam.getTaskId();
/* 263 */     if (StringUtil.isNotEmpty(taskId)) {
/* 264 */       BpmTask bpmTask = (BpmTask)this.bpmTaskManager.get(taskId);
/* 265 */       if (bpmTask != null) {
/* 266 */         BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/*     */         
/* 268 */         SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
/* 269 */         SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef();
/* 270 */         boolean isSupervise = false;
/* 271 */         if (signTaskPluginDef.isSignMultiTask() && signTaskPluginDef.isNeedSupervise()) {
/* 272 */           isSupervise = true;
/*     */         }
/* 274 */         DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)nodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 275 */         DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
/* 276 */         if (dynamicTaskPluginDef.getIsEnabled().booleanValue() && dynamicTaskPluginDef.isNeedSupervise()) {
/* 277 */           isSupervise = true;
/*     */         }
/* 279 */         if (isSupervise) {
/*     */           
/* 281 */           List<BpmNodeDef> inComeNodeDefs = nodeDef.getIncomeTaskNodes();
/* 282 */           List<BpmTask> bpmTasks = this.bpmTaskManager.getByInstId(bpmTask.getInstId());
/* 283 */           if (CollectionUtil.isNotEmpty(bpmTasks)) {
/* 284 */             for (BpmTask existTask : bpmTasks) {
/* 285 */               for (BpmNodeDef bpmNodeDef : inComeNodeDefs) {
/* 286 */                 if (StringUtils.equals(bpmNodeDef.getNodeId(), existTask.getNodeId())) {
/* 287 */                   JSONObject supervise = new JSONObject();
/* 288 */                   BpmTask superviseTask = bpmTasks.get(0);
/* 289 */                   supervise.put("taskName", superviseTask.getName());
/* 290 */                   supervise.put("assigneeName", superviseTask.getAssigneeNames());
/* 291 */                   result.put("superviseInfo", supervise);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 299 */     return getSuccessResult(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getNodeDefaultUser"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr(write2response = true)
/*     */   @ApiOperation(value = "获取节默认候选人", hidden = true, notes = "根据配置，获取节点的执行人的逻辑")
/*     */   public ResultMsg<List<SysIdentity>> getNodeDefaultUser(@RequestParam String defId, @RequestParam String nodeId, @RequestParam(required = false) String taskId, @RequestParam(required = false) String startOrgId) {
/* 309 */     BpmContext.cleanTread();
/* 310 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 311 */     if (bpmProcessDef == null) {
/* 312 */       return null;
/*     */     }
/* 314 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, nodeId);
/* 315 */     if (bpmNodeDef == null) {
/* 316 */       return null;
/*     */     }
/* 318 */     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd();
/* 319 */     taskModel.setActionName("start");
/* 320 */     taskModel.setApproveOrgId(startOrgId);
/* 321 */     if (StringUtils.isNotEmpty(taskId)) {
/* 322 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
/* 323 */       BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceMananger.get(bpmTaskOpinion.getInstId());
/* 324 */       taskModel.setBpmInstance((IBpmInstance)bpmInstance);
/* 325 */       taskModel.setBizDataMap(this.bpmBusDataHandle.getInstanceData(null, bpmInstance));
/* 326 */       BpmNodeDef bpmNodeDef1 = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), bpmTaskOpinion.getTaskKey());
/* 327 */       BpmTask bpmTask = new BpmTask();
/* 328 */       bpmTask.setName(bpmNodeDef1.getName());
/* 329 */       bpmTask.setId(taskId);
/* 330 */       taskModel.setBpmTask((IBpmTask)bpmTask);
/*     */     } 
/*     */     
/* 333 */     BpmContext.setActionModel((ActionCmd)taskModel);
/* 334 */     return getSuccessResult(BpmTaskShowUtil.appendOrgUser(UserCalcPreview.calcNodeUsers(bpmNodeDef, taskModel), bpmNodeDef));
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getCanBackHistoryNodes"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr(write2errorlog = true)
/*     */   @ApiOperation(value = "获取可驳回的节点", hidden = true, notes = "根据任务ID 获取下个节点有哪些可驳回节点")
/*     */   public ResultMsg<Map<String, String>> getCanBackHistoryNodes(@RequestParam String taskId) throws Exception {
/* 342 */     BpmTask task = (BpmTask)this.bpmTaskManager.get(taskId);
/*     */     
/* 344 */     if (task == null) {
/* 345 */       throw new BusinessException(BpmStatusCode.TASK_NOT_FOUND);
/*     */     }
/*     */     
/* 348 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
/*     */     
/* 350 */     Boolean isFreeBack = Boolean.valueOf(nodeDef.getNodeProperties().isFreeBack());
/* 351 */     if (!isFreeBack.booleanValue()) {
/* 352 */       return new ResultMsg();
/*     */     }
/* 354 */     Map<String, String> nodeList = new HashMap<>();
/* 355 */     List<BpmTaskOpinion> opinionList = this.bpmTaskOpinionManager.getByInstId(task.getInstId());
/* 356 */     BpmNodeDef subBpmNodeDef = nodeDef.getParentBpmNodeDef();
/* 357 */     boolean isSubProcess = false;
/* 358 */     if (subBpmNodeDef != null && subBpmNodeDef instanceof SubProcessNodeDef) {
/* 359 */       isSubProcess = true;
/*     */     }
/* 361 */     for (BpmTaskOpinion opinion : opinionList) {
/* 362 */       if (opinion.getDurMs().longValue() <= 1L || 
/* 363 */         isDynamicTask(task, opinion.getTaskKey()) || isSignTask(task, opinion.getTaskKey())) {
/*     */         continue;
/*     */       }
/*     */       
/* 367 */       if (isSubProcess) {
/* 368 */         BpmNodeDef bpmNodeDef = ((SubProcessNodeDef)subBpmNodeDef).getChildBpmProcessDef().getBpmnNodeDef(opinion.getTaskKey());
/* 369 */         if (bpmNodeDef == null) {
/*     */           continue;
/*     */         }
/*     */       } 
/* 373 */       nodeList.put(opinion.getTaskKey(), opinion.getTaskName());
/*     */     } 
/*     */ 
/*     */     
/* 377 */     return getSuccessResult(nodeList);
/*     */   }
/*     */   
/*     */   private boolean isDynamicTask(BpmTask task, String nodeId) {
/* 381 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), nodeId);
/*     */     
/* 383 */     DynamicTaskPluginContext dynamicTaskContext = (DynamicTaskPluginContext)nodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 384 */     if (dynamicTaskContext != null && ((DynamicTaskPluginDef)dynamicTaskContext.getBpmPluginDef()).getIsEnabled().booleanValue()) {
/* 385 */       return true;
/*     */     }
/* 387 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isSignTask(BpmTask task, String nodeId) {
/* 391 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), nodeId);
/*     */     
/* 393 */     SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
/* 394 */     if (signTaskPluginContext != null && ((SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef()).isSignMultiTask()) {
/* 395 */       return true;
/*     */     }
/* 397 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getVariables"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程任务变量", notes = "获取流程变量")
/*     */   public ResultMsg<Map> getVariables(@RequestParam @ApiParam(value = "任务ID", required = true) String taskId, @RequestParam @ApiParam(value = "isLocal", defaultValue = "false") Boolean isLocal) throws Exception {
/* 407 */     if (!isLocal.booleanValue()) return getSuccessResult(((ActTaskService)AppUtil.getBean(ActTaskService.class)).getVariables(taskId));
/*     */     
/* 409 */     return getSuccessResult(((ActTaskService)AppUtil.getBean(ActTaskService.class)).getVariablesLocal(taskId));
/*     */   }
/*     */   
/*     */   @PostMapping({"doActions"})
/*     */   @CatchErr(write2errorlog = true)
/*     */   @ApiOperation(value = "批量执行任务相关动作", notes = "批量执行任务相关的动作 如：同意，驳回，反对，锁定，解锁，转办，会签任务等相关操作")
/*     */   public ResultMsg<Object> doActions(@RequestBody List<FlowRequestParam> flowParams) throws Exception {
/* 416 */     JSONArray results = new JSONArray();
/* 417 */     final IUser curUser = ContextUtil.getCurrentUser();
/* 418 */     List<Future<JSONObject>> futures = new ArrayList<>();
/* 419 */     ExecutorService executorService = ThreadUtil.newExecutor(flowParams.size());
/* 420 */     flowParams.forEach(flowParam -> {
/*     */           Future<JSONObject> future = executorService.submit(new Callable<JSONObject>()
/*     */               {
/*     */                 
/*     */                 public JSONObject call() throws Exception
/*     */                 {
/*     */                   try {
/* 427 */                     JSONObject result = new JSONObject();
/* 428 */                     ContextUtil.setCurrentUser(curUser);
/* 429 */                     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
/* 430 */                     String actionName = taskModel.executeCmd();
/* 431 */                     result.put("flag", "1");
/* 432 */                     result.put("msg", actionName + "成功");
/* 433 */                     return result;
/*     */                   } finally {
/* 435 */                     ContextUtil.clearAll();
/*     */                   } 
/*     */                 }
/*     */               });
/*     */           
/*     */           futures.add(future);
/*     */         });
/* 442 */     futures.forEach(future -> {
/*     */           try {
/*     */             results.add(future.get());
/* 445 */           } catch (Exception e) {
/*     */             JSONObject result = new JSONObject();
/*     */             
/*     */             result.put("flag", "0");
/*     */             result.put("msg", ExceptionUtil.getRootErrorMseeage(e));
/*     */             results.add(result);
/*     */           } 
/*     */         });
/* 453 */     executorService.shutdown();
/* 454 */     return getSuccessResult(results, "批量操作成功");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"todoTaskList"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "待办", notes = "所有待办，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小，默认20条"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "排序，默认升序", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数")})
/*     */   public PageResult<BpmTaskVO> getTaskLinksInfo(HttpServletRequest request, HttpServletResponse reponse) {
/* 473 */     QueryFilter queryFilter = getQueryFilter(request);
/* 474 */     return new PageResult(this.bpmTaskManager.getTaskLinksInfo(queryFilter));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/controller/BpmTaskController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */