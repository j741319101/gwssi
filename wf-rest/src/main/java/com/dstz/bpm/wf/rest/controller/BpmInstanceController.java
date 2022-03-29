package com.dstz.bpm.wf.rest.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.aop.annotion.OperateLog;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.ExceptionUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.rest.ControllerTools;
import com.dstz.base.rest.util.RequestUtil;
import com.dstz.bpm.act.service.ActInstanceService;
import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
import com.dstz.bpm.api.engine.data.result.BpmFlowData;
import com.dstz.bpm.api.engine.data.result.BpmFlowInstanceData;
import com.dstz.bpm.api.engine.data.result.FlowData;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.service.BpmImageService;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.vo.BpmInstanceVO;
import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import com.dstz.bpm.service.BpmDecentralizationService;
import com.dstz.bpm.service.BpmSomeService;
import com.dstz.form.api.model.FormType;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.util.ContextUtil;
import com.github.pagehelper.Page;
import io.swagger.annotations.*;
import org.activiti.engine.RepositoryService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


@RestController
@RequestMapping({"/bpm/instance/"})
@Api(
   description = "流程实例服务接口"
)
public class BpmInstanceController extends ControllerTools {
   protected Logger logger = LoggerFactory.getLogger(this.getClass());
   @Resource
   BpmInstanceManager bpmInstanceManager;
   @Resource
   BpmFlowDataAccessor bpmFlowDataAccessor;
   @Resource
   BpmImageService bpmImageService;
   @Resource
   BpmDefinitionManager bpmDefinitionMananger;
   @Autowired
   RepositoryService repositoryService;
   @Autowired
   BpmTaskStackManager bpmTaskStackManager;
   @Autowired
   private BpmSomeService bpmSomeService;
   @Resource
   ICurrentContext iCurrentContext;
   @Autowired
   BpmDecentralizationService decentralizationService;
   @Autowired
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource
   ActInstanceService actInstanceService;

   @OperateLog //todo aop待更新
   @RequestMapping(
      value = {"listJson"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "流程实例列表",
      notes = "获取流程实例管理列表，用于超管管理流程实例，可以用来干预任务实例"
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
   name = "orgId",
   value = "机构id"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "其他过滤参数"
)})
   public PageResult<BpmInstance> listJson(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      if (this.decentralizationService.decentralizationEnable("wf")) {
         IUser user = this.iCurrentContext.getCurrentUser();
         List<String> lstOrgId = new ArrayList();
         if (null != user) {
            lstOrgId = user.getManagerGroupIdList();
         }

         if (null == lstOrgId || ((List)lstOrgId).size() == 0) {
            lstOrgId = new ArrayList();
            ((List)lstOrgId).add("");
         }

         queryFilter.getParams().put("orgIds", lstOrgId);
      }

      String orgId = request.getParameter("orgId");
      if (StringUtils.isNotEmpty(orgId)) {
         queryFilter.getParams().put("orgId", orgId);
      }

      queryFilter.addFilter("is_test_data_", 0, QueryOP.EQUAL);
      List<BpmInstance> bpmInstanceList = this.bpmInstanceManager.query(queryFilter);
      return new PageResult(bpmInstanceList);
   }

   @RequestMapping(
      value = {"listJson_currentOrg"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "流程实例列表-当前组织",
      notes = "获取流程实例列表-当前组织，用于部门负责人管理本部门下的所有流程实例，可以查看任务情况、干预任务实例"
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
   public PageResult<BpmInstance> listJson_currentOrg(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String orgId = ContextUtil.getCurrentGroupId();
      if (StringUtil.isEmpty(orgId)) {
         return new PageResult();
      } else {
         queryFilter.addFilter("create_org_id_", ContextUtil.getCurrentGroupId(), QueryOP.EQUAL);
         queryFilter.addFilter("is_test_data_", 0, QueryOP.EQUAL);
         Page<BpmInstance> bpmInstanceList = (Page)this.bpmInstanceManager.query(queryFilter);
         return new PageResult(bpmInstanceList);
      }
   }

   @RequestMapping(
      value = {"listInstTaskJson"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "流程实例列表",
      notes = "获取流程实例管理列表，用于超管管理流程实例，可以用来干预任务实例"
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
   public PageResult<BpmInstanceVO> listInstTaskJson(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      queryFilter.addFilter("is_test_data_", 0, QueryOP.EQUAL);
      return new PageResult(this.bpmInstanceManager.listInstTaskJson(queryFilter));
   }

   @RequestMapping(
      value = {"listInstTaskJsonToExcel"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @ApiOperation(
      value = "流程实例列表",
      notes = "获取流程实例管理列表，用于超管管理流程实例，可以用来干预任务实例"
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
   public void listInstTaskJsonToExcel(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String name = new String("流程管理.xls".getBytes("UTF-8"), "ISO8859-1");
      reponse.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", name));
      OutputStream out = reponse.getOutputStream();
      queryFilter.addFilter("is_test_data_", 0, QueryOP.EQUAL);
      this.bpmInstanceManager.listInstTaskJsonToExcel(queryFilter, out);
      out.flush();
   }

   @RequestMapping(
      value = {"getById"},
      method = {RequestMethod.POST}
   )
   @CatchErr
   @ApiOperation(
      value = "流程实例",
      notes = "获取流程实例详情信息"
   )
   public ResultMsg<IBpmInstance> getBpmInstance(@RequestParam @ApiParam("ID") String id) {
      IBpmInstance bpmInstance = null;
      if (StringUtil.isNotEmpty(id)) {
         bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(id);
      }

      return this.getSuccessResult(bpmInstance);
   }

   @RequestMapping(
      value = {"getInstanceData"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr
   @OperateLog
   @ApiOperation(
      value = "流程实例数据",
      notes = "获取流程实例相关数据，包含实例信息，业务数据，表单权限、表单数据、表单内容等"
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "instanceId",
   value = "流程实例ID"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "readonly",
   value = "是否只读实例",
   defaultValue = "false"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "defId",
   value = "流程定义ID，启动时使用"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "flowKey",
   value = "流程定义Key，启动时使用,与DefId二选一"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "formType",
   value = "表单类型",
   defaultValue = "pc"
)})
   public ResultMsg<FlowData> getInstanceData(HttpServletRequest request) {
      String instanceId = request.getParameter("instanceId");
      Boolean readonly = RequestUtil.getBoolean(request, "readonly", false);
      String defId = request.getParameter("defId");
      String flowKey = RequestUtil.getString(request, "flowKey");
      String nodeId = RequestUtil.getString(request, "nodeId");
      String btnJson = RequestUtil.getString(request, "btnList");
      String excludeBtnJson = RequestUtil.getString(request, "excludeBtnList");
      String taskId = RequestUtil.getString(request, "taskId");
      if (StringUtil.isEmpty(defId) && StringUtil.isNotEmpty(flowKey)) {
         BpmDefinition def = this.bpmDefinitionMananger.getByKey(flowKey);
         if (def == null) {
            throw new BusinessException("流程定义查找失败！ flowKey： " + flowKey, BpmStatusCode.DEF_LOST);
         }

         defId = def.getId();
      }

      if (StringUtils.isNotEmpty(taskId) && StringUtils.isEmpty(nodeId)) {
         BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
         if (bpmTaskOpinion != null) {
            nodeId = bpmTaskOpinion.getTaskKey();
         }
      }

      String formType = RequestUtil.getString(request, "formType", FormType.PC.value());
      if (StringUtil.isNotEmpty(nodeId)) {
         BpmFlowInstanceData instanceData = this.bpmFlowDataAccessor.getInstanceData(instanceId, FormType.fromValue(formType), nodeId, taskId);
         return this.getSuccessResult(instanceData);
      } else {
         BpmFlowData data = this.bpmFlowDataAccessor.getStartFlowData(defId, instanceId, taskId, FormType.fromValue(formType), readonly);
         if (StringUtil.isNotEmpty(btnJson)) {
            String[] btnList = btnJson.split(",");
            data.setButtonList((List)data.getButtonList().stream().filter((btn) -> {
               return ArrayUtils.indexOf(btnList, btn.getAlias()) != -1;
            }).collect(Collectors.toList()));
         }

         if (StringUtil.isNotEmpty(excludeBtnJson)) {
            String[] excludeBtnList = excludeBtnJson.split(",");
            data.setButtonList((List)data.getButtonList().stream().filter((btn) -> {
               return ArrayUtils.indexOf(excludeBtnList, btn.getAlias()) == -1;
            }).collect(Collectors.toList()));
         }

         return this.getSuccessResult(data);
      }
   }

   @RequestMapping(
      value = {"doAction"},
      method = {RequestMethod.POST}
   )
   @CatchErr(
      write2errorlog = true
   )
   @ApiOperation(
      value = "执行流程实例相关动作",
      notes = "流程启动，流程保存草稿，草稿启动，催办，人工终止等流程实例相关的动作请求入口"
   )
   public ResultMsg<String> doAction(@RequestBody FlowRequestParam flowParam) {
      DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
      String actionName = instanceCmd.executeCmd();
      return this.getSuccessResult(instanceCmd.getInstanceId(), actionName);
   }

   @RequestMapping(
      value = {"flowImage"},
      method = {RequestMethod.GET}
   )
   @ApiOperation(
      value = "获取流程图流文件",
      notes = "获取流程实例的流程图，以流的形式返回png图片"
   )
   public void flowImage(@RequestParam(required = false) @ApiParam("流程实例ID") String instId, @RequestParam(required = false) @ApiParam("流程定义ID，流程未启动时使用") String defId, HttpServletResponse response) throws Exception {
      String actInstId = null;
      String actDefId;
      if (StringUtil.isNotEmpty(instId)) {
         BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
         actInstId = inst.getActInstId();
         actDefId = inst.getActDefId();
      } else {
         BpmDefinition def = (BpmDefinition)this.bpmDefinitionMananger.get(defId);
         actDefId = def.getActDefId();
      }

      response.setContentType("image/png");
      IOUtils.copy(this.bpmImageService.draw(actDefId, actInstId), response.getOutputStream());
   }

   @RequestMapping(
      value = {"getFlowImageInfo"},
      method = {RequestMethod.POST}
   )
   @CatchErr
   @ApiOperation(
      value = "获取流程图相关信息",
      notes = "获取流程图相关信息"
   )
   public ResultMsg<JSONObject> getFlowImageInfo(@RequestParam(required = false) @ApiParam("流程实例ID") String instanceId, @RequestParam(required = false) @ApiParam("流程定义ID，流程未启动时使用") String defId, @RequestParam(required = false) @ApiParam("任务id") String taskId) {
      return this.getSuccessResult(this.bpmSomeService.getFlowImageInfo(instanceId, defId, taskId));
   }

   @RequestMapping(
      value = {"toForbidden"},
      method = {RequestMethod.GET, RequestMethod.POST}
   )
   @CatchErr("操作失败")
   @OperateLog
   @ApiOperation(
      value = "流程实例禁用/启用",
      notes = "流程实例禁用启用接口"
   )
   public ResultMsg<String> toForbidden(@RequestParam @ApiParam("流程实例ID") String id, @RequestParam @ApiParam("禁用/启用") Boolean forbidden) {
      this.bpmInstanceManager.toForbidden(id, forbidden);
      return this.getSuccessResult(BooleanUtils.toString(forbidden, "禁用成功", "取消禁用成功"));
   }

   @RequestMapping({"startTest"})
   @CatchErr
   public ResultMsg<String> startTest() {
      this.actInstanceService.startProcessInstance("tset:1:410210125441138689", "test", (Map)null);
      return this.getSuccessResult("成功");
   }

   @PostMapping({"delete"})
   @CatchErr
   @OperateLog
   @ApiOperation(
      value = "流程实例批量删除",
      notes = "实例id以逗号分隔，删除流程实例会删除相关的所有任务数据，也会级联删除业务数据"
   )
   public ResultMsg<String> delete(@RequestParam @ApiParam("流程实例ID") String id) {
      this.bpmInstanceManager.delete(id);
      return this.getSuccessResult("删除实例成功");
   }

   @RequestMapping({"updateStatus"})
   @CatchErr
   @ApiOperation(
      value = "流程实例批量修改状态",
      notes = "实例id以逗号分隔，删除流程实例会删除相关的所有任务数据，也会级联删除业务数据"
   )
   public ResultMsg<String> updateStatus(@RequestParam @ApiParam("流程实例ID") String id, @RequestParam String status) {
      this.bpmInstanceManager.updateStatus(id, status);
      return this.getSuccessResult("修改实例成功");
   }

   @RequestMapping(
      value = {"getInstanceAndChildren"},
      method = {RequestMethod.POST}
   )
   @CatchErr
   @ApiOperation(
      value = "流程实例",
      hidden = true
   )
   public ResultMsg<JSONObject> getInstanceAndChildren(@RequestParam @ApiParam("ID") String id) {
      JSONObject json = new JSONObject();
      IBpmInstance bpmInstance = (IBpmInstance)this.bpmInstanceManager.get(id);
      json.put("bpmInstance", bpmInstance);
      List<BpmInstance> instanceList = this.bpmInstanceManager.getByParentId(id);
      json.put("bpmInstanceChildren", instanceList);
      return this.getSuccessResult(json);
   }

   @ApiOperation("获取流程实例数量")
   @RequestMapping({"getInstNum"})
   public ResultMsg<List<Map>> getInstNum() {
      return this.getSuccessResult(this.bpmInstanceManager.getInstNum());
   }

   @ApiOperation("根据业务id获取流程实例id")
   @RequestMapping({"getInstIdByBusId"})
   public ResultMsg<Map> getInstIdByBusId(@RequestParam String busId) {
      return this.getSuccessResult(this.bpmInstanceManager.getInstIdByBusId(busId));
   }

   @PostMapping({"doActions"})
   @CatchErr(
      write2errorlog = true
   )
   @ApiOperation(
      value = "批量执行流程实例相关动作",
      notes = "批量流程启动，流程保存草稿，草稿启动，催办，人工终止等流程实例相关的动作请求入口"
   )
   public ResultMsg<Object> doActions(@RequestBody List<FlowRequestParam> flowParams) {
      JSONArray results = new JSONArray();
      IUser curUser = ContextUtil.getCurrentUser();
      List<Future<JSONObject>> futures = new ArrayList();
      ExecutorService executorService = ThreadUtil.newExecutor(flowParams.size());
      flowParams.forEach((flowParam) -> {
         Future<JSONObject> future = executorService.submit(new Callable<JSONObject>() {
            public JSONObject call() {
               JSONObject result = new JSONObject();
               ContextUtil.setCurrentUser(curUser);
               DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
               if (StringUtils.isNotEmpty(flowParam.getInstanceId()) && StringUtils.isEmpty(flowParam.getStartOrgId())) {
                  BpmInstance bpmInstance = (BpmInstance)BpmInstanceController.this.bpmInstanceManager.get(flowParam.getInstanceId());
                  if (bpmInstance != null) {
                     instanceCmd.setApproveOrgId(bpmInstance.getCreateOrgId());
                  }
               }

               String actionName = instanceCmd.executeCmd();
               result.put("instId", instanceCmd.getInstanceId());
               result.put("msg", actionName + "成功");
               return result;
            }
         });
         futures.add(future);
      });
      futures.forEach((future) -> {
         try {
            results.add(future.get());
         } catch (Exception var4) {
            JSONObject result = new JSONObject();
            result.put("instId", "0");
            result.put("msg", ExceptionUtil.getRootErrorMseeage(var4));
            results.add(result);
         }

      });
      executorService.shutdown();
      return this.getSuccessResult(results, "批量操作成功");
   }
}
