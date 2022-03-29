package com.dstz.bpm.wf.rest.controller;

import com.dstz.bpm.api.constant.InstanceStatus;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTaskApprove;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTypeTreeCountVO;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.core.vo.BpmDefinitionVO;
import com.dstz.bpm.core.vo.BpmTaskApproveVO;
import com.dstz.bpm.core.vo.BpmTaskVO;
import com.dstz.base.api.aop.annotion.OperateLog;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.BeanUtils;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.rest.ControllerTools;
import com.dstz.base.rest.util.RequestUtil;
import com.dstz.bpm.service.BpmDecentralizationService;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.constant.SysTreeTypeConstants;
import com.dstz.sys.api.model.ISysTreeNode;
import com.dstz.sys.api.service.ISysTreeNodeService;
import com.dstz.sys.util.ContextUtil;
import com.dstz.sys.util.SysPropertyUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm/my"})
@Api(
   description = "个人办公服务接口"
)
public class BpmMyTaskController extends ControllerTools {
   protected Logger LOG = LoggerFactory.getLogger(this.getClass());
   @Resource
   BpmTaskManager bpmTaskManager;
   @Resource
   BpmInstanceManager bpmInstanceManager;
   @Resource
   BpmDefinitionManager bpmDefinitionManager;
   @Resource
   TaskIdentityLinkManager taskIdentityLinkManager;
   @Resource
   ISysTreeNodeService sysTreeNodeService;
   @Resource
   ICurrentContext iCurrentContext;
   @Autowired
   BpmDecentralizationService decentralizationService;
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;

   @RequestMapping(
      value = {"todoTaskList"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "我的待办",
      notes = "根据当前用户获取个人所有待办，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "offset",
   value = "offset"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "limit",
   value = "分页大小，默认20条"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "排序字段"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "order",
   value = "排序，默认升序",
   defaultValue = "ASC"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "过滤参数"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "industry",
   value = "行业"
)})
   public PageResult<BpmTaskVO> todoTaskList(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String userId = ContextUtil.getCurrentUserId();
      return new PageResult(this.bpmTaskManager.getTodoList(userId, queryFilter));
   }

   @ApiOperation("更新代办查看状态")
   @ApiImplicitParams({@ApiImplicitParam(
   name = "id_^VIN",
   value = "批量更新用逗号分隔",
   dataType = "String"
)})
   @RequestMapping(
      value = {"updateCheckState"},
      method = {RequestMethod.POST}
   )
   public ResultMsg<String> updateCheckState(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      return this.taskIdentityLinkManager.updateCheckState(queryFilter) > 0 ? this.getSuccessResult("更新查看状态成功") : this.getSuccessResult("更新查看状态失败");
   }

   @RequestMapping(
      value = {"applyTaskList"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "我的申请",
      notes = "获取我发起过的流程申请，可以查看自己发起的流程执行情况，可查阅自己发起过的所有流程审批"
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "offset",
   value = "offset"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "limit",
   value = "分页大小"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "排序字段"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "order",
   value = "order",
   defaultValue = "ASC"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
)})
   public PageResult<BpmInstance> applyTaskList(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String userId = ContextUtil.getCurrentUserId();
      List<BpmInstance> bpmTaskList = this.bpmInstanceManager.getApplyList(userId, queryFilter);
      return new PageResult(bpmTaskList);
   }

   @OperateLog
   @ApiOperation(
      value = "发起申请",
      notes = "获取自己拥有发起权限的流程列表，用于流程发起。其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
   )
   @RequestMapping(
      value = {"definitionList"},
      method = {RequestMethod.POST}
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "offset",
   value = "offset"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "limit",
   value = "分页大小"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "排序字段"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "order",
   value = "order",
   defaultValue = "ASC"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "其他过滤参数"
)})
   public PageResult<BpmDefinitionVO> definitionList(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String userId = ContextUtil.getCurrentUserId();
      if (this.decentralizationService.decentralizationEnable("wf")) {
         IUser user = this.iCurrentContext.getCurrentUser();
         List<String> lstOrgId = new ArrayList();
         if (null != user) {
            lstOrgId = user.getManagerGroupIdList();
         }

         if (null != lstOrgId && ((List)lstOrgId).size() > 0) {
            queryFilter.addFilter("bpm_definition.org_id_", lstOrgId, QueryOP.IN);
         } else {
            queryFilter.addFilter("bpm_definition.org_id_", "", QueryOP.EQUAL);
         }
      }

      List<BpmDefinitionVO> list = this.bpmDefinitionManager.getMyDefinitionList(userId, queryFilter);
      return new PageResult(list);
   }

   @ApiOperation(
      value = "我的审批",
      notes = "获取历史审批过的流程任务 。会展示所有自己办理过的流程、若是候选人未办理则不会展示。 其他过滤参数：“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
   )
   @RequestMapping(
      value = {"approveList"},
      method = {RequestMethod.POST}
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "offset",
   value = "offset"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "limit",
   value = "分页大小"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "排序字段"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "order",
   value = "order",
   defaultValue = "ASC"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "其他过滤参数"
)})
   public PageResult<BpmTaskApprove> approveList(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String userId = ContextUtil.getCurrentUserId();
      List<BpmTaskApprove> bpmTaskList = this.bpmInstanceManager.getApproveHistoryList(userId, queryFilter);
      return new PageResult(bpmTaskList);
   }

   @ApiOperation(
      value = "我的审批(按流程分组)",
      notes = "获取历史审批过的流程任务 。会展示所有自己办理过的流程、若是候选人未办理则不会展示。 其他过滤参数：“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
   )
   @RequestMapping(
      value = {"approveInstList"},
      method = {RequestMethod.POST}
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "offset",
   value = "offset"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "limit",
   value = "分页大小"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "排序字段"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "order",
   value = "order",
   defaultValue = "ASC"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "其他过滤参数"
)})
   public PageResult<BpmTaskApproveVO> approveInstList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String approverTime = request.getParameter("approverTime");
      String approverTimeEnd = request.getParameter("approverTimeEnd");
      if (StringUtil.isNotEmpty(approverTime)) {
         queryFilter.addParamsFilter("approverTime", approverTime);
      }

      if (StringUtil.isNotEmpty(approverTimeEnd)) {
         queryFilter.addParamsFilter("approverTimeEnd", approverTimeEnd);
      }

      List<BpmTaskApproveVO> bpmTaskList = this.bpmInstanceManager.getApproveInstHistoryList(queryFilter);
      return new PageResult(bpmTaskList);
   }

   @ApiOperation(
      value = "我的草稿",
      notes = "获取我的草稿，在流程发起页面保存的，且并未提交的流程事项。其他过滤参数：“filter”为数据库过滤字段名，支持subject_,name_，等字段过滤“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
   )
   @RequestMapping(
      value = {"draftList"},
      method = {RequestMethod.POST}
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "offset",
   value = "offset"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "limit",
   value = "分页大小"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "排序字段"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "order",
   value = "order",
   defaultValue = "ASC"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "其他过滤参数"
)})
   public PageResult<BpmInstance> draftList(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String userId = ContextUtil.getCurrentUserId();
      queryFilter.addFilter("inst.status_", InstanceStatus.STATUS_DRAFT.getKey(), QueryOP.EQUAL);
      List<BpmInstance> instanceList = this.bpmInstanceManager.getApplyList(userId, queryFilter);
      return new PageResult(instanceList);
   }

   @ApiOperation("获取数量")
   @RequestMapping({"getMyTaskNum"})
   public ResultMsg<Map> getMyTaskNum() {
      return this.getSuccessResult(this.bpmTaskManager.getMyTaskNum());
   }

   @ApiOperation("获取业务实体、业务对象、表单、流程定义联合排序")
   @RequestMapping({"getUnionOrder"})
   public ResultMsg<List<Map>> getUnionOrder() {
      return this.getSuccessResult(this.bpmTaskManager.getUnionOrder());
   }

   @RequestMapping(
      value = {"todoTaskListTypeTree"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "我的待办事项-分类树",
      notes = "我的待办事项-分类树，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "过滤参数"
)})
   public ResultMsg<List<BpmTypeTreeCountVO>> todoTaskListTypeTree(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String userId = ContextUtil.getCurrentUserId();
      List<BpmTypeTreeCountVO> countVo = this.bpmTaskManager.todoTaskListTypeCount(userId, queryFilter);
      return this.getSuccessResult(this.handleBpmTypeTree(countVo));
   }

   @RequestMapping(
      value = {"approveListTypeTree"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "获取历史审批 分类树",
      notes = "获取历史审批 分类树，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "过滤参数"
)})
   public ResultMsg<List<BpmTypeTreeCountVO>> approveListTypeTree(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String userId = ContextUtil.getCurrentUserId();
      List<BpmTypeTreeCountVO> countVo = this.bpmInstanceManager.getApproveHistoryListTypeCount(userId, queryFilter);
      return this.getSuccessResult(this.handleBpmTypeTree(countVo));
   }

   private List<BpmTypeTreeCountVO> handleBpmTypeTree(List<BpmTypeTreeCountVO> countVo) {
      List<? extends ISysTreeNode> flowTree = this.sysTreeNodeService.getTreeNodesByType(SysTreeTypeConstants.FLOW.key());
      if (CollectionUtil.isEmpty(flowTree)) {
         return countVo;
      } else {
         List<BpmTypeTreeCountVO> typeAndFlowTree = new ArrayList();
         BpmTypeTreeCountVO rootTree = new BpmTypeTreeCountVO();
         rootTree.setId("0");
         rootTree.setName("所有数据");
         rootTree.setExpand(true);
         rootTree.setKey("sysTree.rootName");
         typeAndFlowTree.add(rootTree);
         Iterator var5 = flowTree.iterator();

         while(var5.hasNext()) {
            ISysTreeNode treeNode = (ISysTreeNode)var5.next();
            int typeCount = 0;
            Iterator var8 = countVo.iterator();

            while(var8.hasNext()) {
               BpmTypeTreeCountVO flowCount = (BpmTypeTreeCountVO)var8.next();
               if (treeNode.getId().equals(flowCount.getParentId())) {
                  typeCount += flowCount.getCount();
               }
            }

            BpmTypeTreeCountVO tree = new BpmTypeTreeCountVO(treeNode);
            tree.setCount(typeCount);
            typeAndFlowTree.add(tree);
         }

         BpmTypeTreeCountVO flowCount;
         for(var5 = countVo.iterator(); var5.hasNext(); typeAndFlowTree.add(flowCount)) {
            flowCount = (BpmTypeTreeCountVO)var5.next();
            if (StringUtil.isEmpty(flowCount.getParentId())) {
               flowCount.setParentId("0");
            }
         }

         return BeanUtils.listToTree(typeAndFlowTree);
      }
   }

   @RequestMapping(
      value = {"applyTaskListTypeTree"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "获取我的申请-分类树",
      notes = "获取我的申请-分类树，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "过滤参数"
)})
   public ResultMsg<List<BpmTypeTreeCountVO>> applyTaskListTypeTree(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String userId = ContextUtil.getCurrentUserId();
      List<BpmTypeTreeCountVO> countVo = this.bpmInstanceManager.applyTaskListTypeCount(userId, queryFilter);
      return this.getSuccessResult(this.handleBpmTypeTree(countVo));
   }

   protected QueryFilter getQueryFilter(HttpServletRequest request) {
      QueryFilter queryFilter = super.getQueryFilter(request);
      String industry = request.getParameter("industry");
      if (StringUtil.isNotEmpty(industry)) {
         String value = SysPropertyUtil.getByAlias(industry);
         if (StringUtil.isNotEmpty(value)) {
            String[] split = value.split(":");
            if (split.length == 2) {
               String tableName = split[0];
               String pkColumn = split[1];
               queryFilter.addParamsFilter("industryTable", tableName);
               queryFilter.addParamsFilter("industryPKColumn", pkColumn);
            } else {
               this.LOG.info("行业标识{}系统属性配置错误", value);
            }
         } else {
            this.LOG.info("行业标识{}系统属性未配置", value);
         }
      }

      return queryFilter;
   }

   @RequestMapping(
      value = {"taskHandleStatus"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "查询任务办理情况",
      notes = "根据当前用户获取个人所有待办，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "offset",
   value = "offset"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "limit",
   value = "分页大小，默认20条"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "排序字段"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "order",
   value = "排序，默认升序",
   defaultValue = "ASC"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "过滤参数"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "industry",
   value = "行业"
)})
   public ResultMsg taskHandleStatus(HttpServletRequest request, HttpServletResponse reponse) {
      Map<String, Object> map = new HashMap(0);
      String taskId = RequestUtil.getStringValues(request, "task.id_^VEQ");
      PageResult<BpmTaskVO> pageResult = this.todoTaskList(request, reponse);
      if (CollectionUtil.isNotEmpty(pageResult.getRows()) && pageResult.getRows().size() > 0) {
         map.put("status", "未办理");
         TaskIdentityLink link = this.taskIdentityLinkManager.getByTaskIdAndUserId(taskId, this.iCurrentContext.getCurrentUserId());
         if (null != link) {
            map.put("linkId", link.getId());
         }
      } else {
         map.put("status", "已办理");
         BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
         if (null != bpmTaskOpinion) {
            map.put("instId", bpmTaskOpinion.getInstId());
            String approver = bpmTaskOpinion.getApprover();
            if (!this.iCurrentContext.getCurrentUserId().equals(approver)) {
               map.put("status", "该工作已被他人办理");
            }
         } else {
            map.put("instId", "该任务实例已被删除");
         }
      }

      return ResultMsg.SUCCESS(map);
   }
}
