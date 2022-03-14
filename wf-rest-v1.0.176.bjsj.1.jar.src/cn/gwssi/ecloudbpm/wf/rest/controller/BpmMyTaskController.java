/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.service.BpmDecentralizationService;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTaskApprove;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.model.BpmTypeTreeCountVO;
/*     */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*     */ import com.dstz.bpm.core.vo.BpmDefinitionVO;
/*     */ import com.dstz.bpm.core.vo.BpmTaskApproveVO;
/*     */ import com.dstz.bpm.core.vo.BpmTaskVO;
/*     */ import com.dstz.base.api.aop.annotion.OperateLog;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.BeanUtils;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.api.constant.SysTreeTypeConstants;
/*     */ import com.dstz.sys.api.model.ISysTreeNode;
/*     */ import com.dstz.sys.api.service.ISysTreeNodeService;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import com.dstz.sys.util.SysPropertyUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiImplicitParam;
/*     */ import io.swagger.annotations.ApiImplicitParams;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/bpm/my"})
/*     */ @Api(description = "个人办公服务接口")
/*     */ public class BpmMyTaskController
/*     */   extends ControllerTools
/*     */ {
/*  59 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */   
/*     */   @Resource
/*     */   BpmTaskManager bpmTaskManager;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   BpmDefinitionManager bpmDefinitionManager;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   TaskIdentityLinkManager taskIdentityLinkManager;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   ISysTreeNodeService sysTreeNodeService;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   
/*     */   @Autowired
/*     */   BpmDecentralizationService decentralizationService;
/*     */   
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"todoTaskList"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "我的待办", notes = "根据当前用户获取个人所有待办，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小，默认20条"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "排序，默认升序", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "industry", value = "行业")})
/*     */   public PageResult<BpmTaskVO> todoTaskList(HttpServletRequest request, HttpServletResponse reponse) {
/*  96 */     QueryFilter queryFilter = getQueryFilter(request);
/*  97 */     String userId = ContextUtil.getCurrentUserId();
/*  98 */     return new PageResult(this.bpmTaskManager.getTodoList(userId, queryFilter));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation("更新代办查看状态")
/*     */   @ApiImplicitParams({@ApiImplicitParam(name = "id_^VIN", value = "批量更新用逗号分隔", dataType = "String")})
/*     */   @RequestMapping(value = {"updateCheckState"}, method = {RequestMethod.POST})
/*     */   public ResultMsg<String> updateCheckState(HttpServletRequest request, HttpServletResponse reponse) {
/* 107 */     QueryFilter queryFilter = getQueryFilter(request);
/* 108 */     if (this.taskIdentityLinkManager.updateCheckState(queryFilter) > 0) {
/* 109 */       return getSuccessResult("更新查看状态成功");
/*     */     }
/* 111 */     return getSuccessResult("更新查看状态失败");
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
/*     */   @RequestMapping(value = {"applyTaskList"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "我的申请", notes = "获取我发起过的流程申请，可以查看自己发起的流程执行情况，可查阅自己发起过的所有流程审批")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
/*     */   public PageResult<BpmInstance> applyTaskList(HttpServletRequest request, HttpServletResponse reponse) {
/* 130 */     QueryFilter queryFilter = getQueryFilter(request);
/* 131 */     String userId = ContextUtil.getCurrentUserId();
/*     */     
/* 133 */     List<BpmInstance> bpmTaskList = this.bpmInstanceManager.getApplyList(userId, queryFilter);
/* 134 */     return new PageResult(bpmTaskList);
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
/*     */   @OperateLog
/*     */   @ApiOperation(value = "发起申请", notes = "获取自己拥有发起权限的流程列表，用于流程发起。其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @RequestMapping(value = {"definitionList"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmDefinitionVO> definitionList(HttpServletRequest request, HttpServletResponse reponse) {
/* 154 */     QueryFilter queryFilter = getQueryFilter(request);
/* 155 */     String userId = ContextUtil.getCurrentUserId();
/* 156 */     if (this.decentralizationService.decentralizationEnable("wf")) {
/* 157 */       IUser user = this.iCurrentContext.getCurrentUser();
/* 158 */       List<String> lstOrgId = new ArrayList<>();
/* 159 */       if (null != user) {
/* 160 */         lstOrgId = user.getManagerGroupIdList();
/*     */       }
/* 162 */       if (null != lstOrgId && lstOrgId.size() > 0) {
/* 163 */         queryFilter.addFilter("bpm_definition.org_id_", lstOrgId, QueryOP.IN);
/*     */       } else {
/* 165 */         queryFilter.addFilter("bpm_definition.org_id_", "", QueryOP.EQUAL);
/*     */       } 
/*     */     } 
/* 168 */     List<BpmDefinitionVO> list = this.bpmDefinitionManager.getMyDefinitionList(userId, queryFilter);
/* 169 */     return new PageResult(list);
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
/*     */   @ApiOperation(value = "我的审批", notes = "获取历史审批过的流程任务 。会展示所有自己办理过的流程、若是候选人未办理则不会展示。 其他过滤参数：“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @RequestMapping(value = {"approveList"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmTaskApprove> approveList(HttpServletRequest request, HttpServletResponse reponse) {
/* 188 */     QueryFilter queryFilter = getQueryFilter(request);
/* 189 */     String userId = ContextUtil.getCurrentUserId();
/* 190 */     List<BpmTaskApprove> bpmTaskList = this.bpmInstanceManager.getApproveHistoryList(userId, queryFilter);
/* 191 */     return new PageResult(bpmTaskList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "我的审批(按流程分组)", notes = "获取历史审批过的流程任务 。会展示所有自己办理过的流程、若是候选人未办理则不会展示。 其他过滤参数：“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @RequestMapping(value = {"approveInstList"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmTaskApproveVO> approveInstList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 202 */     QueryFilter queryFilter = getQueryFilter(request);
/* 203 */     String approverTime = request.getParameter("approverTime");
/* 204 */     String approverTimeEnd = request.getParameter("approverTimeEnd");
/* 205 */     if (StringUtil.isNotEmpty(approverTime)) {
/* 206 */       queryFilter.addParamsFilter("approverTime", approverTime);
/*     */     }
/* 208 */     if (StringUtil.isNotEmpty(approverTimeEnd)) {
/* 209 */       queryFilter.addParamsFilter("approverTimeEnd", approverTimeEnd);
/*     */     }
/* 211 */     List<BpmTaskApproveVO> bpmTaskList = this.bpmInstanceManager.getApproveInstHistoryList(queryFilter);
/* 212 */     return new PageResult(bpmTaskList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "我的草稿", notes = "获取我的草稿，在流程发起页面保存的，且并未提交的流程事项。其他过滤参数：“filter”为数据库过滤字段名，支持subject_,name_，等字段过滤“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @RequestMapping(value = {"draftList"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmInstance> draftList(HttpServletRequest request, HttpServletResponse reponse) {
/* 224 */     QueryFilter queryFilter = getQueryFilter(request);
/* 225 */     String userId = ContextUtil.getCurrentUserId();
/* 226 */     queryFilter.addFilter("inst.status_", InstanceStatus.STATUS_DRAFT.getKey(), QueryOP.EQUAL);
/* 227 */     List<BpmInstance> instanceList = this.bpmInstanceManager.getApplyList(userId, queryFilter);
/* 228 */     return new PageResult(instanceList);
/*     */   }
/*     */   
/*     */   @ApiOperation("获取数量")
/*     */   @RequestMapping({"getMyTaskNum"})
/*     */   public ResultMsg<Map> getMyTaskNum() {
/* 234 */     return getSuccessResult(this.bpmTaskManager.getMyTaskNum());
/*     */   }
/*     */   
/*     */   @ApiOperation("获取业务实体、业务对象、表单、流程定义联合排序")
/*     */   @RequestMapping({"getUnionOrder"})
/*     */   public ResultMsg<List<Map>> getUnionOrder() {
/* 240 */     return getSuccessResult(this.bpmTaskManager.getUnionOrder());
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
/*     */   @RequestMapping(value = {"todoTaskListTypeTree"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "我的待办事项-分类树", notes = "我的待办事项-分类树，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数")})
/*     */   public ResultMsg<List<BpmTypeTreeCountVO>> todoTaskListTypeTree(HttpServletRequest request, HttpServletResponse reponse) {
/* 255 */     QueryFilter queryFilter = getQueryFilter(request);
/* 256 */     String userId = ContextUtil.getCurrentUserId();
/*     */     
/* 258 */     List<BpmTypeTreeCountVO> countVo = this.bpmTaskManager.todoTaskListTypeCount(userId, queryFilter);
/* 259 */     return getSuccessResult(handleBpmTypeTree(countVo));
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
/*     */   @RequestMapping(value = {"approveListTypeTree"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "获取历史审批 分类树", notes = "获取历史审批 分类树，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数")})
/*     */   public ResultMsg<List<BpmTypeTreeCountVO>> approveListTypeTree(HttpServletRequest request, HttpServletResponse reponse) {
/* 275 */     QueryFilter queryFilter = getQueryFilter(request);
/* 276 */     String userId = ContextUtil.getCurrentUserId();
/*     */     
/* 278 */     List<BpmTypeTreeCountVO> countVo = this.bpmInstanceManager.getApproveHistoryListTypeCount(userId, queryFilter);
/*     */     
/* 280 */     return getSuccessResult(handleBpmTypeTree(countVo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<BpmTypeTreeCountVO> handleBpmTypeTree(List<BpmTypeTreeCountVO> countVo) {
/* 290 */     List<? extends ISysTreeNode> flowTree = this.sysTreeNodeService.getTreeNodesByType(SysTreeTypeConstants.FLOW.key());
/* 291 */     if (CollectionUtil.isEmpty(flowTree)) {
/* 292 */       return countVo;
/*     */     }
/* 294 */     List<BpmTypeTreeCountVO> typeAndFlowTree = new ArrayList<>();
/*     */     
/* 296 */     BpmTypeTreeCountVO rootTree = new BpmTypeTreeCountVO();
/* 297 */     rootTree.setId("0");
/* 298 */     rootTree.setName("所有数据");
/* 299 */     rootTree.setExpand(true);
/* 300 */     rootTree.setKey("sysTree.rootName");
/* 301 */     typeAndFlowTree.add(rootTree);
/*     */ 
/*     */     
/* 304 */     for (ISysTreeNode treeNode : flowTree) {
/* 305 */       int typeCount = 0;
/* 306 */       for (BpmTypeTreeCountVO flowCount : countVo) {
/* 307 */         if (treeNode.getId().equals(flowCount.getParentId())) {
/* 308 */           typeCount += flowCount.getCount().intValue();
/*     */         }
/*     */       } 
/*     */       
/* 312 */       BpmTypeTreeCountVO tree = new BpmTypeTreeCountVO(treeNode);
/* 313 */       tree.setCount(Integer.valueOf(typeCount));
/* 314 */       typeAndFlowTree.add(tree);
/*     */     } 
/*     */     
/* 317 */     for (BpmTypeTreeCountVO flowCount : countVo) {
/* 318 */       if (StringUtil.isEmpty(flowCount.getParentId())) {
/* 319 */         flowCount.setParentId("0");
/*     */       }
/* 321 */       typeAndFlowTree.add(flowCount);
/*     */     } 
/*     */     
/* 324 */     return BeanUtils.listToTree(typeAndFlowTree);
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
/*     */   @RequestMapping(value = {"applyTaskListTypeTree"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "获取我的申请-分类树", notes = "获取我的申请-分类树，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数")})
/*     */   public ResultMsg<List<BpmTypeTreeCountVO>> applyTaskListTypeTree(HttpServletRequest request, HttpServletResponse reponse) {
/* 339 */     QueryFilter queryFilter = getQueryFilter(request);
/* 340 */     String userId = ContextUtil.getCurrentUserId();
/*     */     
/* 342 */     List<BpmTypeTreeCountVO> countVo = this.bpmInstanceManager.applyTaskListTypeCount(userId, queryFilter);
/* 343 */     return getSuccessResult(handleBpmTypeTree(countVo));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected QueryFilter getQueryFilter(HttpServletRequest request) {
/* 349 */     QueryFilter queryFilter = super.getQueryFilter(request);
/*     */ 
/*     */     
/* 352 */     String industry = request.getParameter("industry");
/* 353 */     if (StringUtil.isNotEmpty(industry)) {
/* 354 */       String value = SysPropertyUtil.getByAlias(industry);
/* 355 */       if (StringUtil.isNotEmpty(value)) {
/* 356 */         String[] split = value.split(":");
/* 357 */         if (split.length == 2) {
/* 358 */           String tableName = split[0];
/* 359 */           String pkColumn = split[1];
/* 360 */           queryFilter.addParamsFilter("industryTable", tableName);
/* 361 */           queryFilter.addParamsFilter("industryPKColumn", pkColumn);
/*     */         }
/*     */         else {
/*     */           
/* 365 */           this.LOG.info("行业标识{}系统属性配置错误", value);
/*     */         } 
/*     */       } else {
/* 368 */         this.LOG.info("行业标识{}系统属性未配置", value);
/*     */       } 
/*     */     } 
/* 371 */     return queryFilter;
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
/*     */   
/*     */   @RequestMapping(value = {"taskHandleStatus"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "查询任务办理情况", notes = "根据当前用户获取个人所有待办，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小，默认20条"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "排序，默认升序", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "industry", value = "行业")})
/*     */   public ResultMsg taskHandleStatus(HttpServletRequest request, HttpServletResponse reponse) {
/* 391 */     Map<String, Object> map = new HashMap<>(0);
/* 392 */     String taskId = RequestUtil.getStringValues(request, "task.id_^VEQ");
/* 393 */     PageResult<BpmTaskVO> pageResult = todoTaskList(request, reponse);
/* 394 */     if (CollectionUtil.isNotEmpty(pageResult.getRows()) && pageResult.getRows().size() > 0) {
/* 395 */       map.put("status", "未办理");
/* 396 */       TaskIdentityLink link = this.taskIdentityLinkManager.getByTaskIdAndUserId(taskId, this.iCurrentContext.getCurrentUserId());
/* 397 */       if (null != link) {
/* 398 */         map.put("linkId", link.getId());
/*     */       }
/*     */     } else {
/* 401 */       map.put("status", "已办理");
/* 402 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
/* 403 */       if (null != bpmTaskOpinion) {
/* 404 */         map.put("instId", bpmTaskOpinion.getInstId());
/*     */         
/* 406 */         String approver = bpmTaskOpinion.getApprover();
/* 407 */         if (!this.iCurrentContext.getCurrentUserId().equals(approver)) {
/* 408 */           map.put("status", "该工作已被他人办理");
/*     */         }
/*     */       } else {
/* 411 */         map.put("instId", "该任务实例已被删除");
/*     */       } 
/*     */     } 
/*     */     
/* 415 */     return ResultMsg.SUCCESS(map);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/controller/BpmMyTaskController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */