/*     */ package com.dstz.bpm.rest.controller;
/*     */ import cn.gwssi.ecloudbpm.form.api.model.FormType;
/*     */ import cn.gwssi.ecloudbpm.service.BpmDecentralizationService;
/*     */ import cn.gwssi.ecloudbpm.service.BpmSomeService;
/*     */ import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
/*     */ import com.dstz.bpm.api.engine.data.result.BpmFlowData;
/*     */ import com.dstz.bpm.api.engine.data.result.BpmFlowInstanceData;
/*     */ import com.dstz.bpm.api.engine.data.result.FlowData;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.Button;
/*     */ import com.dstz.bpm.api.service.BpmImageService;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.vo.BpmInstanceVO;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.aop.annotion.OperateLog;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.ExceptionUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.thread.ThreadUtil;
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
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.BooleanUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ @RestController
/*     */ @RequestMapping({"/bpm/instance/"})
/*     */ @Api(description = "流程实例服务接口")
/*     */ public class BpmInstanceController extends ControllerTools {
/*  78 */   protected Logger logger = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   @Resource
/*     */   BpmFlowDataAccessor bpmFlowDataAccessor;
/*     */   
/*     */   @Resource
/*     */   BpmImageService bpmImageService;
/*     */   
/*     */   @Resource
/*     */   BpmDefinitionManager bpmDefinitionMananger;
/*     */   
/*     */   @Autowired
/*     */   RepositoryService repositoryService;
/*     */   @Autowired
/*     */   BpmTaskStackManager bpmTaskStackManager;
/*     */   @Autowired
/*     */   private BpmSomeService bpmSomeService;
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   @Autowired
/*     */   BpmDecentralizationService decentralizationService;
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   ActInstanceService actInstanceService;
/*     */   
/*     */   @OperateLog
/*     */   @RequestMapping(value = {"listJson"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "流程实例列表", notes = "获取流程实例管理列表，用于超管管理流程实例，可以用来干预任务实例")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "orgId", value = "机构id"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmInstance> listJson(HttpServletRequest request, HttpServletResponse reponse) {
/* 112 */     QueryFilter queryFilter = getQueryFilter(request);
/* 113 */     if (this.decentralizationService.decentralizationEnable("wf")) {
/* 114 */       IUser user = this.iCurrentContext.getCurrentUser();
/* 115 */       List<String> lstOrgId = new ArrayList<>();
/* 116 */       if (null != user) {
/* 117 */         lstOrgId = user.getManagerGroupIdList();
/*     */       }
/* 119 */       if (null == lstOrgId || lstOrgId.size() == 0) {
/* 120 */         lstOrgId = new ArrayList<>();
/* 121 */         lstOrgId.add("");
/*     */       } 
/* 123 */       queryFilter.getParams().put("orgIds", lstOrgId);
/*     */     } 
/* 125 */     String orgId = request.getParameter("orgId");
/* 126 */     if (StringUtils.isNotEmpty(orgId)) {
/* 127 */       queryFilter.getParams().put("orgId", orgId);
/*     */     }
/* 129 */     queryFilter.addFilter("is_test_data_", Integer.valueOf(0), QueryOP.EQUAL);
/* 130 */     List<BpmInstance> bpmInstanceList = this.bpmInstanceManager.query(queryFilter);
/* 131 */     return new PageResult(bpmInstanceList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"listJson_currentOrg"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "流程实例列表-当前组织", notes = "获取流程实例列表-当前组织，用于部门负责人管理本部门下的所有流程实例，可以查看任务情况、干预任务实例")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmInstance> listJson_currentOrg(HttpServletRequest request, HttpServletResponse reponse) {
/* 142 */     QueryFilter queryFilter = getQueryFilter(request);
/* 143 */     String orgId = ContextUtil.getCurrentGroupId();
/* 144 */     if (StringUtil.isEmpty(orgId)) {
/* 145 */       return new PageResult();
/*     */     }
/* 147 */     queryFilter.addFilter("create_org_id_", ContextUtil.getCurrentGroupId(), QueryOP.EQUAL);
/* 148 */     queryFilter.addFilter("is_test_data_", Integer.valueOf(0), QueryOP.EQUAL);
/* 149 */     Page<BpmInstance> bpmInstanceList = (Page<BpmInstance>)this.bpmInstanceManager.query(queryFilter);
/* 150 */     return new PageResult((List)bpmInstanceList);
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
/*     */   public PageResult<BpmInstanceVO> listInstTaskJson(HttpServletRequest request, HttpServletResponse reponse) {
/* 164 */     QueryFilter queryFilter = getQueryFilter(request);
/* 165 */     queryFilter.addFilter("is_test_data_", Integer.valueOf(0), QueryOP.EQUAL);
/* 166 */     return new PageResult(this.bpmInstanceManager.listInstTaskJson(queryFilter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"listInstTaskJsonToExcel"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @ApiOperation(value = "流程实例列表", notes = "获取流程实例管理列表，用于超管管理流程实例，可以用来干预任务实例")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public void listInstTaskJsonToExcel(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 180 */     QueryFilter queryFilter = getQueryFilter(request);
/* 181 */     String name = new String("流程管理.xls".getBytes("UTF-8"), "ISO8859-1");
/* 182 */     reponse.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", new Object[] { name }));
/* 183 */     ServletOutputStream servletOutputStream = reponse.getOutputStream();
/* 184 */     queryFilter.addFilter("is_test_data_", Integer.valueOf(0), QueryOP.EQUAL);
/* 185 */     this.bpmInstanceManager.listInstTaskJsonToExcel(queryFilter, (OutputStream)servletOutputStream);
/* 186 */     servletOutputStream.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getById"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例", notes = "获取流程实例详情信息")
/*     */   public ResultMsg<IBpmInstance> getBpmInstance(@RequestParam @ApiParam("ID") String id) {
/* 196 */     IBpmInstance bpmInstance = null;
/* 197 */     if (StringUtil.isNotEmpty(id)) {
/* 198 */       bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(id);
/*     */     }
/*     */     
/* 201 */     return getSuccessResult(bpmInstance);
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
/*     */   @OperateLog
/*     */   @ApiOperation(value = "流程实例数据", notes = "获取流程实例相关数据，包含实例信息，业务数据，表单权限、表单数据、表单内容等")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "instanceId", value = "流程实例ID"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "readonly", value = "是否只读实例", defaultValue = "false"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "defId", value = "流程定义ID，启动时使用"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "flowKey", value = "流程定义Key，启动时使用,与DefId二选一"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "formType", value = "表单类型", defaultValue = "pc")})
/*     */   public ResultMsg<FlowData> getInstanceData(HttpServletRequest request) {
/* 220 */     String instanceId = request.getParameter("instanceId");
/* 221 */     Boolean readonly = Boolean.valueOf(RequestUtil.getBoolean(request, "readonly", false));
/*     */     
/* 223 */     String defId = request.getParameter("defId");
/* 224 */     String flowKey = RequestUtil.getString(request, "flowKey");
/* 225 */     String nodeId = RequestUtil.getString(request, "nodeId");
/* 226 */     String btnJson = RequestUtil.getString(request, "btnList");
/* 227 */     String excludeBtnJson = RequestUtil.getString(request, "excludeBtnList");
/* 228 */     String taskId = RequestUtil.getString(request, "taskId");
/* 229 */     if (StringUtil.isEmpty(defId) && StringUtil.isNotEmpty(flowKey)) {
/* 230 */       BpmDefinition def = this.bpmDefinitionMananger.getByKey(flowKey);
/* 231 */       if (def == null) {
/* 232 */         throw new BusinessException("流程定义查找失败！ flowKey： " + flowKey, BpmStatusCode.DEF_LOST);
/*     */       }
/* 234 */       defId = def.getId();
/*     */     } 
/* 236 */     if (StringUtils.isNotEmpty(taskId) && StringUtils.isEmpty(nodeId)) {
/* 237 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
/* 238 */       if (bpmTaskOpinion != null) {
/* 239 */         nodeId = bpmTaskOpinion.getTaskKey();
/*     */       }
/*     */     } 
/* 242 */     String formType = RequestUtil.getString(request, "formType", FormType.PC.value());
/* 243 */     if (StringUtil.isNotEmpty(nodeId)) {
/* 244 */       BpmFlowInstanceData instanceData = this.bpmFlowDataAccessor.getInstanceData(instanceId, FormType.fromValue(formType), nodeId, taskId);
/* 245 */       return getSuccessResult(instanceData);
/*     */     } 
/*     */     
/* 248 */     BpmFlowData data = this.bpmFlowDataAccessor.getStartFlowData(defId, instanceId, taskId, FormType.fromValue(formType), readonly);
/* 249 */     if (StringUtil.isNotEmpty(btnJson)) {
/* 250 */       String[] btnList = btnJson.split(",");
/* 251 */       data.setButtonList((List)data.getButtonList().stream().filter(btn -> (ArrayUtils.indexOf((Object[])btnList, btn.getAlias()) != -1)).collect(Collectors.toList()));
/*     */     } 
/* 253 */     if (StringUtil.isNotEmpty(excludeBtnJson)) {
/* 254 */       String[] excludeBtnList = excludeBtnJson.split(",");
/* 255 */       data.setButtonList((List)data.getButtonList().stream().filter(btn -> (ArrayUtils.indexOf((Object[])excludeBtnList, btn.getAlias()) == -1)).collect(Collectors.toList()));
/*     */     } 
/* 257 */     return getSuccessResult(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"doAction"}, method = {RequestMethod.POST})
/*     */   @CatchErr(write2errorlog = true)
/*     */   @ApiOperation(value = "执行流程实例相关动作", notes = "流程启动，流程保存草稿，草稿启动，催办，人工终止等流程实例相关的动作请求入口")
/*     */   public ResultMsg<String> doAction(@RequestBody FlowRequestParam flowParam) {
/* 270 */     DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
/* 271 */     String actionName = instanceCmd.executeCmd();
/*     */     
/* 273 */     return getSuccessResult(instanceCmd.getInstanceId(), actionName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"flowImage"}, method = {RequestMethod.GET})
/*     */   @ApiOperation(value = "获取流程图流文件", notes = "获取流程实例的流程图，以流的形式返回png图片")
/*     */   public void flowImage(@RequestParam(required = false) @ApiParam("流程实例ID") String instId, @RequestParam(required = false) @ApiParam("流程定义ID，流程未启动时使用") String defId, HttpServletResponse response) throws Exception {
/* 282 */     String actDefId, actInstId = null;
/*     */     
/* 284 */     if (StringUtil.isNotEmpty(instId)) {
/* 285 */       BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
/* 286 */       actInstId = inst.getActInstId();
/* 287 */       actDefId = inst.getActDefId();
/*     */     } else {
/* 289 */       BpmDefinition def = (BpmDefinition)this.bpmDefinitionMananger.get(defId);
/* 290 */       actDefId = def.getActDefId();
/*     */     } 
/*     */     
/* 293 */     response.setContentType("image/png");
/* 294 */     IOUtils.copy(this.bpmImageService.draw(actDefId, actInstId), (OutputStream)response.getOutputStream());
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
/*     */   @RequestMapping(value = {"getFlowImageInfo"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "获取流程图相关信息", notes = "获取流程图相关信息")
/*     */   public ResultMsg<JSONObject> getFlowImageInfo(@RequestParam(required = false) @ApiParam("流程实例ID") String instanceId, @RequestParam(required = false) @ApiParam("流程定义ID，流程未启动时使用") String defId, @RequestParam(required = false) @ApiParam("任务id") String taskId) {
/* 311 */     return getSuccessResult(this.bpmSomeService.getFlowImageInfo(instanceId, defId, taskId));
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"toForbidden"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @CatchErr("操作失败")
/*     */   @OperateLog
/*     */   @ApiOperation(value = "流程实例禁用/启用", notes = "流程实例禁用启用接口")
/*     */   public ResultMsg<String> toForbidden(@RequestParam @ApiParam("流程实例ID") String id, @RequestParam @ApiParam("禁用/启用") Boolean forbidden) {
/* 320 */     this.bpmInstanceManager.toForbidden(id, forbidden.booleanValue());
/* 321 */     return getSuccessResult(BooleanUtils.toString(forbidden.booleanValue(), "禁用成功", "取消禁用成功"));
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
/*     */   @RequestMapping({"startTest"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> startTest() {
/* 338 */     this.actInstanceService.startProcessInstance("tset:1:410210125441138689", "test", null);
/*     */     
/* 340 */     return getSuccessResult("成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"delete"})
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   @ApiOperation(value = "流程实例批量删除", notes = "实例id以逗号分隔，删除流程实例会删除相关的所有任务数据，也会级联删除业务数据")
/*     */   public ResultMsg<String> delete(@RequestParam @ApiParam("流程实例ID") String id) {
/* 352 */     this.bpmInstanceManager.delete(id);
/* 353 */     return getSuccessResult("删除实例成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"updateStatus"})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例批量修改状态", notes = "实例id以逗号分隔，删除流程实例会删除相关的所有任务数据，也会级联删除业务数据")
/*     */   public ResultMsg<String> updateStatus(@RequestParam @ApiParam("流程实例ID") String id, @RequestParam String status) {
/* 363 */     this.bpmInstanceManager.updateStatus(id, status);
/* 364 */     return getSuccessResult("修改实例成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getInstanceAndChildren"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例", hidden = true)
/*     */   public ResultMsg<JSONObject> getInstanceAndChildren(@RequestParam @ApiParam("ID") String id) {
/* 375 */     JSONObject json = new JSONObject();
/* 376 */     IBpmInstance bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(id);
/* 377 */     json.put("bpmInstance", bpmInstance);
/* 378 */     List<BpmInstance> instanceList = this.bpmInstanceManager.getByParentId(id);
/* 379 */     json.put("bpmInstanceChildren", instanceList);
/* 380 */     return getSuccessResult(json);
/*     */   }
/*     */   
/*     */   @ApiOperation("获取流程实例数量")
/*     */   @RequestMapping({"getInstNum"})
/*     */   public ResultMsg<List<Map>> getInstNum() {
/* 386 */     return getSuccessResult(this.bpmInstanceManager.getInstNum());
/*     */   }
/*     */   
/*     */   @ApiOperation("根据业务id获取流程实例id")
/*     */   @RequestMapping({"getInstIdByBusId"})
/*     */   public ResultMsg<Map> getInstIdByBusId(@RequestParam String busId) {
/* 392 */     return getSuccessResult(this.bpmInstanceManager.getInstIdByBusId(busId));
/*     */   }
/*     */   
/*     */   @PostMapping({"doActions"})
/*     */   @CatchErr(write2errorlog = true)
/*     */   @ApiOperation(value = "批量执行流程实例相关动作", notes = "批量流程启动，流程保存草稿，草稿启动，催办，人工终止等流程实例相关的动作请求入口")
/*     */   public ResultMsg<Object> doActions(@RequestBody List<FlowRequestParam> flowParams) {
/* 399 */     JSONArray results = new JSONArray();
/* 400 */     final IUser curUser = ContextUtil.getCurrentUser();
/* 401 */     List<Future<JSONObject>> futures = new ArrayList<>();
/* 402 */     ExecutorService executorService = ThreadUtil.newExecutor(flowParams.size());
/* 403 */     flowParams.forEach(flowParam -> {
/*     */           Future<JSONObject> future = executorService.submit(new Callable<JSONObject>()
/*     */               {
/*     */                 
/*     */                 public JSONObject call()
/*     */                 {
/* 409 */                   JSONObject result = new JSONObject();
/* 410 */                   ContextUtil.setCurrentUser(curUser);
/* 411 */                   DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
/* 412 */                   if (StringUtils.isNotEmpty(flowParam.getInstanceId()) && StringUtils.isEmpty(flowParam.getStartOrgId())) {
/* 413 */                     BpmInstance bpmInstance = (BpmInstance)BpmInstanceController.this.bpmInstanceManager.get(flowParam.getInstanceId());
/* 414 */                     if (bpmInstance != null) {
/* 415 */                       instanceCmd.setApproveOrgId(bpmInstance.getCreateOrgId());
/*     */                     }
/*     */                   } 
/* 418 */                   String actionName = instanceCmd.executeCmd();
/* 419 */                   result.put("instId", instanceCmd.getInstanceId());
/* 420 */                   result.put("msg", actionName + "成功");
/* 421 */                   return result;
/*     */                 }
/*     */               });
/*     */           
/*     */           futures.add(future);
/*     */         });
/* 427 */     futures.forEach(future -> {
/*     */           try {
/*     */             results.add(future.get());
/* 430 */           } catch (Exception e) {
/*     */             JSONObject result = new JSONObject();
/*     */             
/*     */             result.put("instId", "0");
/*     */             result.put("msg", ExceptionUtil.getRootErrorMseeage(e));
/*     */             results.add(result);
/*     */           } 
/*     */         });
/* 438 */     executorService.shutdown();
/* 439 */     return getSuccessResult(results, "批量操作成功");
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/controller/BpmInstanceController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */