/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.BeanUtils;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTaskApprove;
/*     */ import com.dstz.bpm.core.model.BpmTypeTreeCountVO;
/*     */ import com.dstz.bpm.core.vo.BpmDefinitionVO;
/*     */ import com.dstz.bpm.core.vo.BpmTaskVO;
/*     */ import com.dstz.sys.api.constant.SysTreeTypeConstants;
/*     */ import com.dstz.sys.api.model.ISysTreeNode;
/*     */ import com.dstz.sys.api.service.ISysTreeNodeService;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import com.dstz.sys.util.SysPropertyUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.github.pagehelper.Page;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiImplicitParam;
/*     */ import io.swagger.annotations.ApiImplicitParams;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
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
/*     */ @RequestMapping({"/bpm/my"})
/*     */ @Api(description = "个人办公服务接口")
/*     */ public class BpmMyTaskController
/*     */   extends ControllerTools
/*     */ {
/*  63 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */ 
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
/*     */   
/*     */   @RequestMapping(value = {"todoTaskList"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "我的待办", notes = "根据当前用户获取个人所有待办，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小，默认20条"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "排序，默认升序", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "industry", value = "行业")})
/*     */   public PageResult<BpmTaskVO> todoTaskList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/*  92 */     QueryFilter queryFilter = getQueryFilter(request);
/*  93 */     String userId = ContextUtil.getCurrentUserId();
/*  94 */     return new PageResult(this.bpmTaskManager.getTodoList(userId, queryFilter));
/*     */   }
/*     */ 
/*     */   
/*     */   @ApiOperation("更新代办查看状态")
/*     */   @ApiImplicitParams({@ApiImplicitParam(name = "id_^VIN", value = "批量更新用逗号分隔", dataType = "String")})
/*     */   @RequestMapping(value = {"updateCheckState"}, method = {RequestMethod.POST})
/*     */   public ResultMsg<String> updateCheckState(HttpServletRequest request, HttpServletResponse reponse) {
/* 102 */     QueryFilter queryFilter = getQueryFilter(request);
/* 103 */     if (this.taskIdentityLinkManager.updateCheckState(queryFilter) > 0) {
/* 104 */       return getSuccessResult("更新查看状态成功");
/*     */     }
/* 106 */     return getSuccessResult("更新查看状态失败");
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
/*     */   @RequestMapping(value = {"applyTaskList"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "我的申请", notes = "获取我发起过的流程申请，可以查看自己发起的流程执行情况，可查阅自己发起过的所有流程审批")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
/*     */   public PageResult<BpmInstance> applyTaskList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 124 */     QueryFilter queryFilter = getQueryFilter(request);
/* 125 */     String userId = ContextUtil.getCurrentUserId();
/*     */     
/* 127 */     Page<BpmInstance> bpmTaskList = (Page<BpmInstance>)this.bpmInstanceManager.getApplyList(userId, queryFilter);
/* 128 */     return new PageResult((List)bpmTaskList);
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
/*     */   @ApiOperation(value = "发起申请", notes = "获取自己拥有发起权限的流程列表，用于流程发起。其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @RequestMapping(value = {"definitionList"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmDefinitionVO> definitionList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 147 */     QueryFilter queryFilter = getQueryFilter(request);
/* 148 */     String userId = ContextUtil.getCurrentUserId();
/*     */     
/* 150 */     List<BpmDefinitionVO> list = this.bpmDefinitionManager.getMyDefinitionList(userId, queryFilter);
/* 151 */     return new PageResult(list);
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
/*     */   public PageResult<BpmTaskApprove> approveList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 170 */     QueryFilter queryFilter = getQueryFilter(request);
/* 171 */     String userId = ContextUtil.getCurrentUserId();
/* 172 */     List<BpmTaskApprove> bpmTaskList = this.bpmInstanceManager.getApproveHistoryList(userId, queryFilter);
/* 173 */     return new PageResult(bpmTaskList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "我的草稿", notes = "获取我的草稿，在流程发起页面保存的，且并未提交的流程事项。其他过滤参数：“filter”为数据库过滤字段名，支持subject_,name_，等字段过滤“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @RequestMapping(value = {"draftList"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmInstance> draftList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 184 */     QueryFilter queryFilter = getQueryFilter(request);
/* 185 */     String userId = ContextUtil.getCurrentUserId();
/* 186 */     queryFilter.addFilter("inst.status_", InstanceStatus.STATUS_DRAFT.getKey(), QueryOP.EQUAL);
/* 187 */     List<BpmInstance> instanceList = this.bpmInstanceManager.getApplyList(userId, queryFilter);
/* 188 */     return new PageResult(instanceList);
/*     */   }
/*     */   
/*     */   @ApiOperation(value = "获取数量", notes = "")
/*     */   @RequestMapping({"getMyTaskNum"})
/*     */   public ResultMsg<Map> getMyTaskNum() {
/* 194 */     return getSuccessResult(this.bpmTaskManager.getMyTaskNum());
/*     */   }
/*     */   
/*     */   @ApiOperation(value = "获取业务实体、业务对象、表单、流程定义联合排序", notes = "")
/*     */   @RequestMapping({"getUnionOrder"})
/*     */   public ResultMsg<List<Map>> getUnionOrder() {
/* 200 */     return getSuccessResult(this.bpmTaskManager.getUnionOrder());
/*     */   }
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
/*     */   public ResultMsg<List<BpmTypeTreeCountVO>> todoTaskListTypeTree(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 214 */     QueryFilter queryFilter = getQueryFilter(request);
/* 215 */     String userId = ContextUtil.getCurrentUserId();
/*     */     
/* 217 */     List<BpmTypeTreeCountVO> countVo = this.bpmTaskManager.todoTaskListTypeCount(userId, queryFilter);
/* 218 */     return getSuccessResult(handleBpmTypeTree(countVo));
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
/*     */   @RequestMapping(value = {"approveListTypeTree"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "获取历史审批 分类树", notes = "获取历史审批 分类树，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数")})
/*     */   public ResultMsg<List<BpmTypeTreeCountVO>> approveListTypeTree(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 235 */     QueryFilter queryFilter = getQueryFilter(request);
/* 236 */     String userId = ContextUtil.getCurrentUserId();
/*     */     
/* 238 */     List<BpmTypeTreeCountVO> countVo = this.bpmInstanceManager.getApproveHistoryListTypeCount(userId, queryFilter);
/*     */     
/* 240 */     return getSuccessResult(handleBpmTypeTree(countVo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<BpmTypeTreeCountVO> handleBpmTypeTree(List<BpmTypeTreeCountVO> countVo) {
/* 249 */     List<? extends ISysTreeNode> flowTree = this.sysTreeNodeService.getTreeNodesByType(SysTreeTypeConstants.FLOW.key());
/* 250 */     if (CollectionUtil.isEmpty(flowTree)) return countVo; 
/* 251 */     List<BpmTypeTreeCountVO> typeAndFlowTree = new ArrayList<>();
/*     */     
/* 253 */     BpmTypeTreeCountVO rootTree = new BpmTypeTreeCountVO();
/* 254 */     rootTree.setId("0");
/* 255 */     rootTree.setName("所有数据");
/* 256 */     rootTree.setExpand(true);
/* 257 */     rootTree.setKey("sysTree.rootName");
/* 258 */     typeAndFlowTree.add(rootTree);
/*     */ 
/*     */     
/* 261 */     for (ISysTreeNode treeNode : flowTree) {
/* 262 */       int typeCount = 0;
/* 263 */       for (BpmTypeTreeCountVO flowCount : countVo) {
/* 264 */         if (treeNode.getId().equals(flowCount.getParentId())) {
/* 265 */           typeCount += flowCount.getCount().intValue();
/*     */         }
/*     */       } 
/*     */       
/* 269 */       BpmTypeTreeCountVO tree = new BpmTypeTreeCountVO(treeNode);
/* 270 */       tree.setCount(Integer.valueOf(typeCount));
/* 271 */       typeAndFlowTree.add(tree);
/*     */     } 
/*     */     
/* 274 */     for (BpmTypeTreeCountVO flowCount : countVo) {
/* 275 */       if (StringUtil.isEmpty(flowCount.getParentId())) {
/* 276 */         flowCount.setParentId("0");
/*     */       }
/* 278 */       typeAndFlowTree.add(flowCount);
/*     */     } 
/*     */     
/* 281 */     return BeanUtils.listToTree(typeAndFlowTree);
/*     */   }
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
/*     */   public ResultMsg<List<BpmTypeTreeCountVO>> applyTaskListTypeTree(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/* 295 */     QueryFilter queryFilter = getQueryFilter(request);
/* 296 */     String userId = ContextUtil.getCurrentUserId();
/*     */     
/* 298 */     List<BpmTypeTreeCountVO> countVo = this.bpmInstanceManager.applyTaskListTypeCount(userId, queryFilter);
/* 299 */     return getSuccessResult(handleBpmTypeTree(countVo));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected QueryFilter getQueryFilter(HttpServletRequest request) {
/* 305 */     QueryFilter queryFilter = super.getQueryFilter(request);
/*     */ 
/*     */     
/* 308 */     String industry = request.getParameter("industry");
/* 309 */     if (StringUtil.isNotEmpty(industry)) {
/* 310 */       String value = SysPropertyUtil.getByAlias(industry);
/* 311 */       if (StringUtil.isNotEmpty(value)) {
/* 312 */         String[] split = value.split(":");
/* 313 */         if (split.length == 2) {
/* 314 */           String tableName = split[0];
/* 315 */           String pkColumn = split[1];
/* 316 */           queryFilter.addParamsFilter("industryTable", tableName);
/* 317 */           queryFilter.addParamsFilter("industryPKColumn", pkColumn);
/*     */         }
/*     */         else {
/*     */           
/* 321 */           this.LOG.info("行业标识{}系统属性配置错误", value);
/*     */         } 
/*     */       } else {
/* 324 */         this.LOG.info("行业标识{}系统属性未配置", value);
/*     */       } 
/*     */     } 
/* 327 */     return queryFilter;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/rest/controller/BpmMyTaskController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */