/*     */ package com.dstz.bpm.plugin.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.BaseController;
/*     */ import com.dstz.bpm.core.model.BpmTaskApprove;
/*     */ import com.dstz.bpm.core.vo.BpmTaskVO;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmLeaderTaskLogManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmLeaderTaskLog;
/*     */ import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
/*     */ import io.swagger.annotations.ApiImplicitParam;
/*     */ import io.swagger.annotations.ApiImplicitParams;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ @RequestMapping({"/bpm/leader"})
/*     */ @RestController
/*     */ public class BpmLeaderTaskLogController
/*     */   extends BaseController<BpmLeaderTaskLog>
/*     */ {
/*     */   @Autowired
/*     */   private BpmLeaderTaskLogManager leaderTaskLogManager;
/*     */   
/*     */   @ApiOperation(value = "领导待办（秘书）", notes = "根据当前用户获取个人所有待办，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小，默认20条"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "排序，默认升序", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "industry", value = "行业")})
/*     */   @RequestMapping(value = {"todoTaskList"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   public PageResult<BpmTaskVO> todoTaskList(HttpServletRequest request) throws Exception {
/*  46 */     QueryFilter queryFilter = getQueryFilter(request);
/*  47 */     return new PageResult(this.leaderTaskLogManager.getTodoList(queryFilter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "领导审批历史（秘书）", notes = "获取历史审批过的流程任务 。会展示所有自己办理过的流程、若是候选人未办理则不会展示。 其他过滤参数：“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @RequestMapping(value = {"approveList"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmTaskApprove> approveList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
/*  58 */     QueryFilter queryFilter = getQueryFilter(request);
/*  59 */     List<BpmTaskApprove> bpmTaskList = this.leaderTaskLogManager.getApproveHistoryList(queryFilter);
/*  60 */     return new PageResult(bpmTaskList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "领导待阅（秘书）", notes = "根据当前用户获取个人所有待阅，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小，默认20条"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "排序，默认升序", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "过滤参数"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "industry", value = "行业")})
/*     */   @RequestMapping(value = {"receiveList"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   public PageResult<BpmUserReceiveCarbonCopyRecordVO> receiveList(HttpServletRequest request) throws Exception {
/*  72 */     QueryFilter queryFilter = getQueryFilter(request);
/*  73 */     return new PageResult(this.leaderTaskLogManager.listUserReceive(queryFilter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "任务呈送给领导", notes = "任务呈送给领导")
/*     */   @RequestMapping(value = {"sendTaskToLeader"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "taskId", value = "任务ID"), @ApiImplicitParam(dataType = "String", name = "optoin", value = "意见")})
/*     */   public ResultMsg<String> sendTaskToLeader(@RequestParam String taskId, @RequestParam String option) throws Exception {
/*  83 */     this.leaderTaskLogManager.saveOptionAndUpdateStatusToLeader(taskId, option);
/*  84 */     return getSuccessResult("呈送成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "任务返回给秘书", notes = "任务返回给秘书")
/*     */   @RequestMapping(value = {"sendTaskTosecretary"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "taskId", value = "任务ID"), @ApiImplicitParam(dataType = "String", name = "optoin", value = "意见")})
/*     */   public ResultMsg<String> sendTaskTosecretary(@RequestParam String taskId, @RequestParam String option) throws Exception {
/*  94 */     this.leaderTaskLogManager.saveOptionAndUpdateStatusToSecretary(taskId, option);
/*  95 */     return getSuccessResult("返回成功");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "抄送呈送给领导", notes = "抄送呈送给领导")
/*     */   @RequestMapping(value = {"sendCarbonCopyToLeader"}, method = {RequestMethod.POST})
/*     */   @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "taskId", value = "taskID")})
/*     */   public ResultMsg<String> sendCarbonCopyToLeader(@RequestParam String taskId) throws Exception {
/* 104 */     this.leaderTaskLogManager.updateByTaskId(taskId, "1", "2");
/* 105 */     return getSuccessResult("呈送成功");
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getModelDesc() {
/* 110 */     return "领导任务";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/rest/controller/BpmLeaderTaskLogController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */