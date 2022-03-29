package com.dstz.bpm.wf.rest.controller;

import com.dstz.base.api.aop.annotion.OperateLog;
import com.dstz.bpm.act.service.ActTaskService;
import com.dstz.bpm.api.engine.action.cmd.FlowBatchRequestParam;
import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
import com.dstz.bpm.api.engine.data.result.BpmFlowTaskData;
import com.dstz.bpm.api.engine.data.result.FlowData;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.vo.BpmTaskVO;
import com.dstz.bpm.engine.action.cmd.DefaultTaskActionBatchCmd;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.engine.plugin.service.BpmPluginService;
import com.dstz.bpm.engine.util.BpmTaskShowUtil;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.bpm.plugin.usercalc.util.UserCalcPreview;
import com.dstz.base.api.aop.annotion.CatchErr;
//import com.dstz.base.api.aop.annotion.OperateLog;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.ExceptionUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.rest.ControllerTools;
import com.dstz.bpm.service.BpmDecentralizationService;
import com.dstz.bpm.service.BpmSomeService;
import com.dstz.bus.api.model.IBusinessPermission;
import com.dstz.bus.api.service.IBusinessDataService;
import com.dstz.form.api.model.FormType;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm/task"})
@Api(
   description = "流程任务相关服务接口"
)
public class BpmTaskController extends ControllerTools {
   @Resource
   BpmTaskManager bpmTaskManager;
   @Resource
   BpmFlowDataAccessor bpmFlowDataAccessor;
   @Autowired
   BpmProcessDefService bpmProcessDefService;
   @Autowired
   BpmInstanceManager bpmInstanceMananger;
   @Autowired
   IBpmBusDataHandle bpmBusDataHandle;
   @Autowired
   BpmPluginService bpmPluginService;
   @Autowired
   BpmTaskOpinionManager bpmTaskOpinionManager;
   @Autowired
   IBusinessDataService iBusinessDataService;
   @Resource
   ICurrentContext iCurrentContext;
   @Autowired
   BpmDecentralizationService decentralizationService;
   @Resource
   private BpmSomeService bpmSomeService;

   @GetMapping({"get"})
   @ApiOperation("获取")
   public ResultMsg get(@RequestParam String id) {
      return ResultMsg.SUCCESS(this.bpmTaskManager.get(id));
   }

   @RequestMapping(
      value = {"listJson"},
      method = {RequestMethod.POST}
   )
   @OperateLog
   @ApiOperation(
      value = "流程任务列表",
      notes = "获取流程任务的列表数据，用于管理员管理任务，入参“filter”为数据库过滤字段名，支持对title_,name_等任务字段过滤，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
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
   public PageResult<BpmTask> listJson(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
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

      List<BpmTask> bpmTaskList = this.bpmTaskManager.query(queryFilter);
      return new PageResult(bpmTaskList);
   }

   @RequestMapping(
      value = {"getBpmTask"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr
   @ApiOperation(
      value = "流程任务",
      notes = "获取流程任务信息"
   )
   public ResultMsg<BpmTask> getBpmTask(@RequestParam @ApiParam(value = "任务ID",required = true) String id) throws Exception {
      BpmTask bpmTask = null;
      if (StringUtil.isNotEmpty(id)) {
         bpmTask = (BpmTask)this.bpmTaskManager.get(id);
      }

      return this.getSuccessResult(bpmTask);
   }

   @RequestMapping({"remove"})
   @CatchErr("删除流程任务失败")
   public ResultMsg<String> remove(@RequestParam String id) throws Exception {
      String[] aryIds = StringUtil.getStringAryByStr(id);
      this.bpmTaskManager.removeByIds(aryIds);
      return this.getSuccessResult("删除流程任务成功");
   }

   @RequestMapping(
      value = {"getTaskData"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr(
      write2errorlog = true
   )
   @OperateLog
   @ApiOperation(
      value = "获取流程任务相关数据",
      notes = "获取任务的业务数据、表单、按钮、权限等信息，为了渲染展示任务页面"
   )
   public ResultMsg<FlowData> getTaskData(@RequestParam @ApiParam(value = "任务ID",required = true) String taskId, @RequestParam(required = false) @ApiParam(value = "表单类型",defaultValue = "pc") String formType, @RequestParam(required = false) @ApiParam("排除的按钮,多个按钮用逗号拼接") String excludeBtnJson, @RequestParam(required = false) @ApiParam("任务候选人关系id") String taskLinkId) throws Exception {
      if (StringUtil.isEmpty(formType)) {
         formType = FormType.PC.value();
      }

      BpmFlowTaskData data = (BpmFlowTaskData)this.bpmFlowDataAccessor.getFlowTaskData(taskId, taskLinkId, FormType.fromValue(formType));
      if (StringUtil.isNotEmpty(excludeBtnJson)) {
         String[] excludeBtnList = excludeBtnJson.split(",");
         data.setButtonList((List)data.getButtonList().stream().filter((btn) -> {
            return ArrayUtils.indexOf(excludeBtnList, btn.getAlias()) == -1;
         }).collect(Collectors.toList()));
      }

      return this.getSuccessResult(data);
   }

   @RequestMapping(
      value = {"doAction"},
      method = {RequestMethod.POST}
   )
   @CatchErr(
      write2errorlog = true
   )
   @ApiOperation(
      value = "执行任务相关动作",
      notes = "执行任务相关的动作 如：同意，驳回，反对，锁定，解锁，转办，会签任务等相关操作"
   )
   public ResultMsg<String> doAction(@RequestBody FlowRequestParam flowParam) throws Exception {
      DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
      String result = taskModel.executeCmd();
      return this.getSuccessResult(result);
   }

   @RequestMapping(
      value = {"doBatchAction"},
      method = {RequestMethod.POST}
   )
   @CatchErr(
      write2errorlog = true
   )
   @ApiOperation(
      value = "批量执行任务动作",
      notes = "批量执行任务操作"
   )
   public ResultMsg<String> doBatchAction(@RequestBody FlowBatchRequestParam flowParam) throws Exception {
      DefaultTaskActionBatchCmd batchCmd = new DefaultTaskActionBatchCmd();
      batchCmd.executeCmd(flowParam);
      return this.getSuccessResult("执行成功");
   }

   @RequestMapping(
      value = {"unLock"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr
   @ApiOperation(
      value = "任务取消指派",
      notes = "管理员将任务取消指派，若任务原先无候选人，则无法取消指派。"
   )
   public ResultMsg<String> unLock(@RequestParam @ApiParam(value = "任务ID",required = true) String taskId) throws Exception {
      this.bpmTaskManager.unLockTask(taskId);
      return this.getSuccessResult("取消指派成功");
   }

   @RequestMapping(
      value = {"assignTask"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr
   @ApiOperation(
      value = "任务指派",
      notes = "管理员将任务指派给某一个用户处理"
   )
   public ResultMsg<String> assignTask(@RequestBody JSONObject params) throws Exception {
      String taskId = params.getString("taskId");
      JSONArray jsonArray = params.getJSONArray("sysIdentities");
      List<SysIdentity> sysIdentities = new ArrayList();
      if (jsonArray != null) {
         jsonArray.forEach((obj) -> {
            JSONObject user = (JSONObject)obj;
            String clazzStr = user.getString("clazz");
            Class clazz = SysIdentity.class;
            if (StringUtils.isNotEmpty(clazzStr)) {
               ServiceLoader<SysIdentity> loader = ServiceLoader.load(SysIdentity.class);
               Iterator var6 = loader.iterator();

               while(var6.hasNext()) {
                  SysIdentity sysIdentity = (SysIdentity)var6.next();
                  System.out.println(sysIdentity.getClass());
                  if (StringUtils.equals(clazzStr, sysIdentity.getClass().getSimpleName())) {
                     clazz = sysIdentity.getClass();
                     break;
                  }
               }
            }

            SysIdentity bpmInentity = (SysIdentity)JSON.toJavaObject(user, clazz);
            sysIdentities.add(bpmInentity);
         });
      }

      this.bpmTaskManager.assigneeTask(taskId, sysIdentities);
      return this.getSuccessResult("指派成功");
   }

   @RequestMapping(
      value = {"handleNodeFreeSelectUser"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr(
      write2response = true
   )
   @ApiOperation(
      value = "处理节点 【自由选择候选人】功能",
      hidden = true,
      notes = "根据配置，处理节点可自由选择下一个节点的执行人的逻辑"
   )
   public ResultMsg<Map<String, Object>> handleNodeFreeSelectUser(@RequestBody FlowRequestParam flowParam) throws Exception {
      Map<String, Object> result = this.bpmSomeService.handleNodeFreeSelectUser(flowParam);
      String taskId = flowParam.getTaskId();
      if (StringUtil.isNotEmpty(taskId)) {
         BpmTask bpmTask = (BpmTask)this.bpmTaskManager.get(taskId);
         if (bpmTask != null) {
            BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
            SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
            SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef();
            boolean isSupervise = false;
            if (signTaskPluginDef.isSignMultiTask() && signTaskPluginDef.isNeedSupervise()) {
               isSupervise = true;
            }

            DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)nodeDef.getPluginContext(DynamicTaskPluginContext.class);
            DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
            if (dynamicTaskPluginDef.getIsEnabled() && dynamicTaskPluginDef.isNeedSupervise()) {
               isSupervise = true;
            }

            if (isSupervise) {
               List<BpmNodeDef> inComeNodeDefs = nodeDef.getIncomeTaskNodes();
               List<BpmTask> bpmTasks = this.bpmTaskManager.getByInstId(bpmTask.getInstId());
               if (CollectionUtil.isNotEmpty(bpmTasks)) {
                  Iterator var13 = bpmTasks.iterator();

                  while(var13.hasNext()) {
                     BpmTask existTask = (BpmTask)var13.next();
                     Iterator var15 = inComeNodeDefs.iterator();

                     while(var15.hasNext()) {
                        BpmNodeDef bpmNodeDef = (BpmNodeDef)var15.next();
                        if (StringUtils.equals(bpmNodeDef.getNodeId(), existTask.getNodeId())) {
                           JSONObject supervise = new JSONObject();
                           BpmTask superviseTask = (BpmTask)bpmTasks.get(0);
                           supervise.put("taskName", superviseTask.getName());
                           supervise.put("assigneeName", superviseTask.getAssigneeNames());
                           result.put("superviseInfo", supervise);
                        }
                     }
                  }
               }
            }
         }
      }

      return this.getSuccessResult(result);
   }

   @RequestMapping(
      value = {"getNodeDefaultUser"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr(
      write2response = true
   )
   @ApiOperation(
      value = "获取节默认候选人",
      hidden = true,
      notes = "根据配置，获取节点的执行人的逻辑"
   )
   public ResultMsg<List<SysIdentity>> getNodeDefaultUser(@RequestParam String defId, @RequestParam String nodeId, @RequestParam(required = false) String taskId, @RequestParam(required = false) String startOrgId) {
      BpmContext.cleanTread();
      DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
      if (bpmProcessDef == null) {
         return null;
      } else {
         BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, nodeId);
         if (bpmNodeDef == null) {
            return null;
         } else {
            DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd();
            taskModel.setActionName("start");
            taskModel.setApproveOrgId(startOrgId);
            if (StringUtils.isNotEmpty(taskId)) {
               BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
               BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceMananger.get(bpmTaskOpinion.getInstId());
               taskModel.setBpmInstance(bpmInstance);
               taskModel.setBizDataMap(this.bpmBusDataHandle.getInstanceData((IBusinessPermission)null, bpmInstance));
               BpmNodeDef bpmNodeDef1 = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), bpmTaskOpinion.getTaskKey());
               BpmTask bpmTask = new BpmTask();
               bpmTask.setName(bpmNodeDef1.getName());
               bpmTask.setId(taskId);
               taskModel.setBpmTask(bpmTask);
            }

            BpmContext.setActionModel(taskModel);
            return this.getSuccessResult(BpmTaskShowUtil.appendOrgUser(UserCalcPreview.calcNodeUsers(bpmNodeDef, taskModel), bpmNodeDef));
         }
      }
   }

   @RequestMapping(
      value = {"getCanBackHistoryNodes"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr(
      write2errorlog = true
   )
   @ApiOperation(
      value = "获取可驳回的节点",
      hidden = true,
      notes = "根据任务ID 获取下个节点有哪些可驳回节点"
   )
   public ResultMsg<Map<String, String>> getCanBackHistoryNodes(@RequestParam String taskId) throws Exception {
      BpmTask task = (BpmTask)this.bpmTaskManager.get(taskId);
      if (task == null) {
         throw new BusinessException(BpmStatusCode.TASK_NOT_FOUND);
      } else {
         BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
         Boolean isFreeBack = nodeDef.getNodeProperties().isFreeBack();
         if (!isFreeBack) {
            return new ResultMsg();
         } else {
            Map<String, String> nodeList = new HashMap();
            List<BpmTaskOpinion> opinionList = this.bpmTaskOpinionManager.getByInstId(task.getInstId());
            BpmNodeDef subBpmNodeDef = nodeDef.getParentBpmNodeDef();
            boolean isSubProcess = false;
            if (subBpmNodeDef != null && subBpmNodeDef instanceof SubProcessNodeDef) {
               isSubProcess = true;
            }

            Iterator var9 = opinionList.iterator();

            while(true) {
               BpmTaskOpinion opinion;
               BpmNodeDef bpmNodeDef;
               do {
                  do {
                     do {
                        do {
                           if (!var9.hasNext()) {
                              return this.getSuccessResult(nodeList);
                           }

                           opinion = (BpmTaskOpinion)var9.next();
                        } while(opinion.getDurMs() <= 1L);
                     } while(this.isDynamicTask(task, opinion.getTaskKey()));
                  } while(this.isSignTask(task, opinion.getTaskKey()));

                  if (!isSubProcess) {
                     break;
                  }

                  bpmNodeDef = ((SubProcessNodeDef)subBpmNodeDef).getChildBpmProcessDef().getBpmnNodeDef(opinion.getTaskKey());
               } while(bpmNodeDef == null);

               nodeList.put(opinion.getTaskKey(), opinion.getTaskName());
            }
         }
      }
   }

   private boolean isDynamicTask(BpmTask task, String nodeId) {
      BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), nodeId);
      DynamicTaskPluginContext dynamicTaskContext = (DynamicTaskPluginContext)nodeDef.getPluginContext(DynamicTaskPluginContext.class);
      return dynamicTaskContext != null && ((DynamicTaskPluginDef)dynamicTaskContext.getBpmPluginDef()).getIsEnabled();
   }

   private boolean isSignTask(BpmTask task, String nodeId) {
      BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), nodeId);
      SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
      return signTaskPluginContext != null && ((SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef()).isSignMultiTask();
   }

   @RequestMapping(
      value = {"getVariables"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr
   @ApiOperation(
      value = "流程任务变量",
      notes = "获取流程变量"
   )
   public ResultMsg<Map> getVariables(@RequestParam @ApiParam(value = "任务ID",required = true) String taskId, @RequestParam @ApiParam(value = "isLocal",defaultValue = "false") Boolean isLocal) throws Exception {
      return !isLocal ? this.getSuccessResult(((ActTaskService)AppUtil.getBean(ActTaskService.class)).getVariables(taskId)) : this.getSuccessResult(((ActTaskService)AppUtil.getBean(ActTaskService.class)).getVariablesLocal(taskId));
   }

   @PostMapping({"doActions"})
   @CatchErr(
      write2errorlog = true
   )
   @ApiOperation(
      value = "批量执行任务相关动作",
      notes = "批量执行任务相关的动作 如：同意，驳回，反对，锁定，解锁，转办，会签任务等相关操作"
   )
   public ResultMsg<Object> doActions(@RequestBody List<FlowRequestParam> flowParams) throws Exception {
      JSONArray results = new JSONArray();
      IUser curUser = ContextUtil.getCurrentUser();
      List<Future<JSONObject>> futures = new ArrayList();
      ExecutorService executorService = ThreadUtil.newExecutor(flowParams.size());
      flowParams.forEach((flowParam) -> {
         Future<JSONObject> future = executorService.submit(new Callable<JSONObject>() {
            public JSONObject call() throws Exception {
               JSONObject var4;
               try {
                  JSONObject result = new JSONObject();
                  ContextUtil.setCurrentUser(curUser);
                  DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
                  String actionName = taskModel.executeCmd();
                  result.put("flag", "1");
                  result.put("msg", actionName + "成功");
                  var4 = result;
               } finally {
                  ContextUtil.clearAll();
               }

               return var4;
            }
         });
         futures.add(future);
      });
      futures.forEach((future) -> {
         try {
            results.add(future.get());
         } catch (Exception var4) {
            JSONObject result = new JSONObject();
            result.put("flag", "0");
            result.put("msg", ExceptionUtil.getRootErrorMseeage(var4));
            results.add(result);
         }

      });
      executorService.shutdown();
      return this.getSuccessResult(results, "批量操作成功");
   }

   @RequestMapping(
      value = {"todoTaskList"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "待办",
      notes = "所有待办，支持对subject_,name_等Task字段过滤，“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”"
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
)})
   public PageResult<BpmTaskVO> getTaskLinksInfo(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      return new PageResult(this.bpmTaskManager.getTaskLinksInfo(queryFilter));
   }
}
