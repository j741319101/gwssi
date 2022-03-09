/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.ExceptionUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
/*     */ import com.dstz.bpm.api.engine.data.result.BpmFlowData;
/*     */ import com.dstz.bpm.api.engine.data.result.BpmFlowInstanceData;
/*     */ import com.dstz.bpm.api.engine.data.result.FlowData;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.Button;
/*     */ import com.dstz.bpm.api.service.BpmImageService;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.core.vo.BpmInstanceVO;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.plugin.util.FlowImageInfoPLuginsUtil;
/*     */ import com.dstz.form.api.model.FormType;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.thread.ThreadUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.github.pagehelper.Page;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiImplicitParam;
/*     */ import io.swagger.annotations.ApiImplicitParams;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.activiti.bpmn.model.BpmnModel;
/*     */ import org.activiti.bpmn.model.FlowElement;
/*     */ import org.activiti.bpmn.model.GraphicInfo;
/*     */ import org.activiti.bpmn.model.Process;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.BooleanUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
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
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/bpm/instance/"})
/*     */ @Api(description = "流程实例服务接口")
/*     */ public class BpmInstanceController
/*     */   extends ControllerTools
/*     */ {
/*     */   @Resource
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   BpmFlowDataAccessor bpmFlowDataAccessor;
/*     */   @Resource
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   BpmImageService bpmImageService;
/*     */   @Resource
/*     */   BpmDefinitionManager bpmDefinitionMananger;
/*     */   @Autowired
/*     */   RepositoryService repositoryService;
/*     */   @Autowired
/*     */   BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/* 116 */   protected Logger logger = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   @Resource
/*     */   ActInstanceService actInstanceService;
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"listJson"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "流程实例列表", notes = "获取流程实例管理列表，用于超管管理流程实例，可以用来干预任务实例")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmInstance> listJson(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 129 */     QueryFilter queryFilter = getQueryFilter(request);
/* 130 */     Page<BpmInstance> bpmInstanceList = (Page<BpmInstance>)this.bpmInstanceManager.query(queryFilter);
/* 131 */     return new PageResult((List)bpmInstanceList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"listJson_currentOrg"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "流程实例列表-当前组织", notes = "获取流程实例列表-当前组织，用于部门负责人管理本部门下的所有流程实例，可以查看任务情况、干预任务实例")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmInstance> listJson_currentOrg(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 142 */     QueryFilter queryFilter = getQueryFilter(request);
/* 143 */     String orgId = ContextUtil.getCurrentGroupId();
/* 144 */     if (StringUtil.isEmpty(orgId)) {
/* 145 */       return new PageResult();
/*     */     }
/* 147 */     queryFilter.addFilter("create_org_id_", ContextUtil.getCurrentGroupId(), QueryOP.EQUAL);
/* 148 */     Page<BpmInstance> bpmInstanceList = (Page<BpmInstance>)this.bpmInstanceManager.query(queryFilter);
/* 149 */     return new PageResult((List)bpmInstanceList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"listInstTaskJson"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "流程实例列表", notes = "获取流程实例管理列表，用于超管管理流程实例，可以用来干预任务实例")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmInstanceVO> listInstTaskJson(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 163 */     QueryFilter queryFilter = getQueryFilter(request);
/* 164 */     return new PageResult(this.bpmInstanceManager.listInstTaskJson(queryFilter));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getById"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例", notes = "获取流程实例详情信息")
/*     */   public ResultMsg<IBpmInstance> getBpmInstance(@RequestParam @ApiParam("ID") String id) throws Exception {
/* 173 */     IBpmInstance bpmInstance = null;
/* 174 */     if (StringUtil.isNotEmpty(id)) {
/* 175 */       bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(id);
/*     */     }
/*     */     
/* 178 */     return getSuccessResult(bpmInstance);
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
/*     */   @RequestMapping(value = {"getInstanceData"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例数据", notes = "获取流程实例相关数据，包含实例信息，业务数据，表单权限、表单数据、表单内容等")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "instanceId", value = "流程实例ID"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "readonly", value = "是否只读实例", defaultValue = "false"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "defId", value = "流程定义ID，启动时使用"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "flowKey", value = "流程定义Key，启动时使用,与DefId二选一"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "formType", value = "表单类型", defaultValue = "pc")})
/*     */   public ResultMsg<FlowData> getInstanceData(HttpServletRequest request) throws Exception {
/* 196 */     String instanceId = request.getParameter("instanceId");
/* 197 */     Boolean readonly = Boolean.valueOf(RequestUtil.getBoolean(request, "readonly", false));
/*     */     
/* 199 */     String defId = request.getParameter("defId");
/* 200 */     String flowKey = RequestUtil.getString(request, "flowKey");
/* 201 */     String nodeId = RequestUtil.getString(request, "nodeId");
/* 202 */     String btnJson = RequestUtil.getString(request, "btnList");
/* 203 */     String excludeBtnJson = RequestUtil.getString(request, "excludeBtnList");
/* 204 */     if (StringUtil.isEmpty(defId) && StringUtil.isNotEmpty(flowKey)) {
/* 205 */       BpmDefinition def = this.bpmDefinitionMananger.getByKey(flowKey);
/* 206 */       if (def == null) {
/* 207 */         throw new BusinessException("流程定义查找失败！ flowKey： " + flowKey, BpmStatusCode.DEF_LOST);
/*     */       }
/* 209 */       defId = def.getId();
/*     */     } 
/*     */     
/* 212 */     String formType = RequestUtil.getString(request, "formType", FormType.PC.value());
/* 213 */     if (StringUtil.isNotEmpty(nodeId)) {
/* 214 */       BpmFlowInstanceData instanceData = this.bpmFlowDataAccessor.getInstanceData(instanceId, FormType.fromValue(formType), nodeId);
/* 215 */       return getSuccessResult(instanceData);
/*     */     } 
/*     */     
/* 218 */     BpmFlowData data = this.bpmFlowDataAccessor.getStartFlowData(defId, instanceId, FormType.fromValue(formType), readonly);
/* 219 */     if (StringUtil.isNotEmpty(btnJson)) {
/* 220 */       String[] btnList = btnJson.split(",");
/* 221 */       data.setButtonList((List)data.getButtonList().stream().filter(btn -> (ArrayUtils.indexOf((Object[])btnList, btn.getAlias()) != -1)).collect(Collectors.toList()));
/*     */     } 
/* 223 */     if (StringUtil.isNotEmpty(excludeBtnJson)) {
/* 224 */       String[] excludeBtnList = excludeBtnJson.split(",");
/* 225 */       data.setButtonList((List)data.getButtonList().stream().filter(btn -> (ArrayUtils.indexOf((Object[])excludeBtnList, btn.getAlias()) == -1)).collect(Collectors.toList()));
/*     */     } 
/* 227 */     return getSuccessResult(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"doAction"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "执行流程实例相关动作", notes = "流程启动，流程保存草稿，草稿启动，催办，人工终止等流程实例相关的动作请求入口")
/*     */   public ResultMsg<String> doAction(@RequestBody FlowRequestParam flowParam) throws Exception {
/* 240 */     DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
/* 241 */     String actionName = instanceCmd.executeCmd();
/*     */     
/* 243 */     return getSuccessResult(instanceCmd.getInstanceId(), actionName);
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getInstanceOpinion"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "获取流程意见", notes = "通过流程实例ID 获取该流程实例下所有的审批意见、并按处理时间排序")
/*     */   public ResultMsg<List<BpmTaskOpinion>> getInstanceOpinion(@RequestParam @ApiParam("流程实例ID") String instId, @RequestParam(required = false) @ApiParam("任务ID") String taskId) throws Exception {
/* 251 */     String trace = null;
/* 252 */     if (StringUtil.isNotEmpty(taskId)) {
/* 253 */       BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
/* 254 */       if (StringUtil.isNotEmpty(opinion.getTrace())) {
/* 255 */         trace = opinion.getTrace();
/*     */       }
/*     */     } 
/* 258 */     List<BpmTaskOpinion> taskOpinion = this.bpmTaskOpinionManager.getByInst(instId, null, null, trace);
/* 259 */     return getSuccessResult(taskOpinion, "获取流程意见成功");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"getOpinion"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "获取流程意见", notes = "通过流程实例ID/任务ID，获取该流程实例下所有的审批意见，并按处理时间排序")
/*     */   public ResultMsg<List<BpmTaskOpinion>> getOpinion(@RequestParam @ApiParam("流程实例ID") String instId, @RequestParam(required = false) @ApiParam("任务ID") String taskId) throws Exception {
/* 266 */     String trace = null;
/* 267 */     if (StringUtil.isNotEmpty(taskId)) {
/* 268 */       BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
/* 269 */       if (StringUtil.isNotEmpty(opinion.getTrace())) {
/* 270 */         trace = opinion.getTrace();
/*     */       }
/*     */     } 
/* 273 */     List<BpmTaskOpinion> taskOpinion = this.bpmTaskOpinionManager.getByInst(instId, null, null, trace);
/* 274 */     return getSuccessResult(taskOpinion, "获取流程意见成功");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"getInstanceOpinionStruct"}, method = {RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例审批意见信息结构", notes = "通过流程实例ID 获取该流程实例下所有的审批意见的展示结构")
/*     */   public ResultMsg getInstanceOpinionStruct(@RequestParam @ApiParam("流程实例ID") String instId) throws Exception {
/* 281 */     BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
/* 282 */     if (null != inst) {
/* 283 */       String defId = inst.getDefId();
/* 284 */       BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionMananger.get(defId);
/* 285 */       if (null != bpmDefinition) {
/* 286 */         String defSetting = bpmDefinition.getDefSetting();
/* 287 */         JSONObject jsonObject = JSON.parseObject(defSetting);
/* 288 */         JSONObject flowObject = (JSONObject)jsonObject.get("flow");
/* 289 */         JSONObject propertiesObject = (JSONObject)flowObject.get("properties");
/* 290 */         JSONObject opinionObject = (JSONObject)propertiesObject.get("opinion");
/* 291 */         return getSuccessResult(opinionObject, "获取审批意见结构成功");
/*     */       } 
/*     */     } 
/* 294 */     return getSuccessResult("获取审批意见结构成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"flowImage"}, method = {RequestMethod.GET})
/*     */   @ApiOperation(value = "获取流程图流文件", notes = "获取流程实例的流程图，以流的形式返回png图片")
/*     */   public void flowImage(@RequestParam(required = false) @ApiParam("流程实例ID") String instId, @RequestParam(required = false) @ApiParam("流程定义ID，流程未启动时使用") String defId, HttpServletResponse response) throws Exception {
/* 303 */     String actDefId, actInstId = null;
/*     */     
/* 305 */     if (StringUtil.isNotEmpty(instId)) {
/* 306 */       BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
/* 307 */       actInstId = inst.getActInstId();
/* 308 */       actDefId = inst.getActDefId();
/*     */     } else {
/* 310 */       BpmDefinition def = (BpmDefinition)this.bpmDefinitionMananger.get(defId);
/* 311 */       actDefId = def.getActDefId();
/*     */     } 
/*     */     
/* 314 */     response.setContentType("image/png");
/* 315 */     IOUtils.copy(this.bpmImageService.draw(actDefId, actInstId), (OutputStream)response.getOutputStream());
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
/*     */   @RequestMapping(value = {"getFlowImageInfo"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "获取流程图相关信息", notes = "获取流程图相关信息")
/*     */   public ResultMsg<JSONObject> getFlowImageInfo(@RequestParam(required = false) @ApiParam("流程实例ID") String instanceId, @RequestParam(required = false) @ApiParam("流程定义ID，流程未启动时使用") String defId, @RequestParam(required = false) @ApiParam("任务id") String taskId) {
/*     */     String actDefId, bpmDefId;
/* 332 */     JSONObject data = new JSONObject();
/* 333 */     if (StringUtil.isNotEmpty(instanceId)) {
/* 334 */       BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instanceId);
/* 335 */       actDefId = inst.getActDefId();
/* 336 */       bpmDefId = inst.getDefId();
/*     */       
/* 338 */       String trace = null;
/* 339 */       if (StringUtil.isNotEmpty(taskId)) {
/* 340 */         BpmTaskOpinion bto = this.bpmTaskOpinionManager.getByTaskId(taskId);
/* 341 */         trace = bto.getTrace();
/*     */       } 
/*     */       
/* 344 */       Map<String, List<BpmTaskOpinion>> opinionMap = new HashMap<>();
/* 345 */       List<BpmTaskOpinion> ops = this.bpmTaskOpinionManager.getByInst(instanceId, null, null, trace);
/* 346 */       ops.forEach(op -> {
/*     */             String taskKey = op.getTaskKey();
/*     */             if (!StringUtil.isEmpty(taskKey)) {
/*     */               List<BpmTaskOpinion> opinions = opinionMap.computeIfAbsent(taskKey, ());
/*     */               opinions.add(op);
/*     */             } 
/*     */           });
/* 353 */       data.put("opinionMap", opinionMap);
/*     */ 
/*     */       
/* 356 */       List<BpmTaskStack> stacks = this.bpmTaskStackManager.getByInstIdAndTrace(instanceId, trace);
/* 357 */       data.put("stacks", stacks);
/*     */     } else {
/* 359 */       BpmDefinition def = (BpmDefinition)this.bpmDefinitionMananger.get(defId);
/* 360 */       actDefId = def.getActDefId();
/* 361 */       bpmDefId = def.getId();
/*     */     } 
/*     */     
/* 364 */     BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(bpmDefId);
/* 365 */     BpmnModel bpmnModel = this.repositoryService.getBpmnModel(actDefId);
/* 366 */     JSONArray flowElements = new JSONArray();
/* 367 */     Process process = bpmnModel.getProcesses().get(0);
/*     */ 
/*     */     
/* 370 */     Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
/* 371 */     Map<String, FlowElement> flowElementMap = new HashMap<>();
/* 372 */     for (FlowElement fe : process.getFlowElements()) {
/* 373 */       JSONObject json = JSON.parseObject(JSON.toJSONString(fe));
/* 374 */       json.put("type", fe.getClass().getSimpleName());
/* 375 */       flowElements.add(json);
/* 376 */       flowElementMap.put(fe.getId(), fe);
/*     */     } 
/*     */ 
/*     */     
/* 380 */     JSONObject nodeMap = new JSONObject();
/* 381 */     Set<String> locationKeySet = locationMap.keySet();
/* 382 */     for (String location : locationKeySet) {
/* 383 */       GraphicInfo graphicInfo = locationMap.get(location);
/* 384 */       JSONObject nodeInfo = JSON.parseObject(JSON.toJSONString(graphicInfo));
/* 385 */       FlowElement flowElement = flowElementMap.get(location);
/* 386 */       if (flowElement != null) {
/* 387 */         nodeInfo.put("type", flowElement.getClass().getSimpleName());
/*     */       }
/*     */       
/* 390 */       FlowImageInfoPLuginsUtil.handlePlugins(location, nodeInfo, bpmProcessDef, instanceId);
/*     */       
/* 392 */       nodeMap.put(location, nodeInfo);
/*     */     } 
/*     */     
/* 395 */     data.put("nodeMap", nodeMap);
/* 396 */     data.put("flowElements", flowElements);
/* 397 */     data.put("flowLocation", bpmnModel.getFlowLocationMap());
/* 398 */     data.put("artifacts", process.getArtifacts());
/*     */     
/* 400 */     data.put("pools", bpmnModel.getPools());
/* 401 */     data.put("lanes", process.getLanes());
/*     */ 
/*     */     
/* 404 */     return getSuccessResult(data);
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"toForbidden"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @CatchErr("操作失败")
/*     */   @ApiOperation(value = "流程实例禁用/启用", notes = "流程实例禁用启用接口")
/*     */   public ResultMsg<String> toForbidden(@RequestParam @ApiParam("流程实例ID") String id, @RequestParam @ApiParam("禁用/启用") Boolean forbidden) throws Exception {
/* 412 */     this.bpmInstanceManager.toForbidden(id, forbidden.booleanValue());
/* 413 */     return getSuccessResult(BooleanUtils.toString(forbidden.booleanValue(), "禁用成功", "取消禁用成功"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"startTest"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> startTest() throws Exception {
/* 426 */     this.actInstanceService.startProcessInstance("tset:1:410210125441138689", "test", null);
/*     */     
/* 428 */     return getSuccessResult("成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"delete"})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例批量删除", notes = "实例id以逗号分隔，删除流程实例会删除相关的所有任务数据，也会级联删除业务数据")
/*     */   public ResultMsg<String> delete(@RequestParam @ApiParam("流程实例ID") String id) throws Exception {
/* 439 */     this.bpmInstanceManager.delete(id);
/* 440 */     return getSuccessResult("删除实例成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"updateStatus"})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例批量修改状态", notes = "实例id以逗号分隔，删除流程实例会删除相关的所有任务数据，也会级联删除业务数据")
/*     */   public ResultMsg<String> updateStatus(@RequestParam @ApiParam("流程实例ID") String id, @RequestParam String status) throws Exception {
/* 450 */     this.bpmInstanceManager.updateStatus(id, status);
/* 451 */     return getSuccessResult("修改实例成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getInstanceAndChildren"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例", hidden = true)
/*     */   public ResultMsg<JSONObject> getInstanceAndChildren(@RequestParam @ApiParam("ID") String id) throws Exception {
/* 462 */     JSONObject json = new JSONObject();
/* 463 */     IBpmInstance bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(id);
/* 464 */     json.put("bpmInstance", bpmInstance);
/* 465 */     List<BpmInstance> instanceList = this.bpmInstanceManager.getByParentId(id);
/* 466 */     json.put("bpmInstanceChildren", instanceList);
/* 467 */     return getSuccessResult(json);
/*     */   }
/*     */   
/*     */   @ApiOperation(value = "获取流程实例数量", notes = "")
/*     */   @RequestMapping({"getInstNum"})
/*     */   public ResultMsg<List<Map>> getInstNum() {
/* 473 */     return getSuccessResult(this.bpmInstanceManager.getInstNum());
/*     */   }
/*     */   
/*     */   @ApiOperation(value = "根据业务id获取流程实例id", notes = "")
/*     */   @RequestMapping({"getInstIdByBusId"})
/*     */   public ResultMsg<Map> getInstIdByBusId(@RequestParam String busId) {
/* 479 */     return getSuccessResult(this.bpmInstanceManager.getInstIdByBusId(busId));
/*     */   }
/*     */   
/*     */   @PostMapping({"doActions"})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "批量执行流程实例相关动作", notes = "批量流程启动，流程保存草稿，草稿启动，催办，人工终止等流程实例相关的动作请求入口")
/*     */   public ResultMsg<Object> doActions(@RequestBody List<FlowRequestParam> flowParams) throws Exception {
/* 486 */     JSONArray results = new JSONArray();
/* 487 */     final IUser curUser = ContextUtil.getCurrentUser();
/* 488 */     List<Future<JSONObject>> futures = new ArrayList<>();
/* 489 */     ExecutorService executorService = ThreadUtil.newExecutor(flowParams.size());
/* 490 */     flowParams.forEach(flowParam -> {
/*     */           Future<JSONObject> future = executorService.submit(new Callable<JSONObject>()
/*     */               {
/*     */                 
/*     */                 public JSONObject call() throws Exception
/*     */                 {
/* 496 */                   JSONObject result = new JSONObject();
/* 497 */                   ContextUtil.setCurrentUser(curUser);
/* 498 */                   DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
/* 499 */                   String actionName = instanceCmd.executeCmd();
/* 500 */                   result.put("instId", instanceCmd.getInstanceId());
/* 501 */                   result.put("msg", actionName + "成功");
/* 502 */                   return result;
/*     */                 }
/*     */               });
/*     */           
/*     */           futures.add(future);
/*     */         });
/* 508 */     futures.forEach(future -> {
/*     */           try {
/*     */             results.add(future.get());
/* 511 */           } catch (Exception e) {
/*     */             JSONObject result = new JSONObject();
/*     */             
/*     */             result.put("instId", "0");
/*     */             result.put("msg", ExceptionUtil.getRootErrorMseeage(e));
/*     */             results.add(result);
/*     */           } 
/*     */         });
/* 519 */     executorService.shutdown();
/* 520 */     return getSuccessResult(results, "批量操作成功");
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/rest/controller/BpmInstanceController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */