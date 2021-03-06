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
   description = "??????????????????????????????"
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
   @ApiOperation("??????")
   public ResultMsg get(@RequestParam String id) {
      return ResultMsg.SUCCESS(this.bpmTaskManager.get(id));
   }

   @RequestMapping(
      value = {"listJson"},
      method = {RequestMethod.POST}
   )
   @OperateLog
   @ApiOperation(
      value = "??????????????????",
      notes = "???????????????????????????????????????????????????????????????????????????filter??????????????????????????????????????????title_,name_???????????????????????????V?????????????????????????????????EQ/IN/LK/..??????????????????????????????/in/??????/..???"
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
   value = "????????????"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "????????????"
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
   value = "??????id"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "??????????????????"
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
      value = "????????????",
      notes = "????????????????????????"
   )
   public ResultMsg<BpmTask> getBpmTask(@RequestParam @ApiParam(value = "??????ID",required = true) String id) throws Exception {
      BpmTask bpmTask = null;
      if (StringUtil.isNotEmpty(id)) {
         bpmTask = (BpmTask)this.bpmTaskManager.get(id);
      }

      return this.getSuccessResult(bpmTask);
   }

   @RequestMapping({"remove"})
   @CatchErr("????????????????????????")
   public ResultMsg<String> remove(@RequestParam String id) throws Exception {
      String[] aryIds = StringUtil.getStringAryByStr(id);
      this.bpmTaskManager.removeByIds(aryIds);
      return this.getSuccessResult("????????????????????????");
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
      value = "??????????????????????????????",
      notes = "????????????????????????????????????????????????????????????????????????????????????????????????"
   )
   public ResultMsg<FlowData> getTaskData(@RequestParam @ApiParam(value = "??????ID",required = true) String taskId, @RequestParam(required = false) @ApiParam(value = "????????????",defaultValue = "pc") String formType, @RequestParam(required = false) @ApiParam("???????????????,???????????????????????????") String excludeBtnJson, @RequestParam(required = false) @ApiParam("?????????????????????id") String taskLinkId) throws Exception {
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
      value = "????????????????????????",
      notes = "??????????????????????????? ???????????????????????????????????????????????????????????????????????????????????????"
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
      value = "????????????????????????",
      notes = "????????????????????????"
   )
   public ResultMsg<String> doBatchAction(@RequestBody FlowBatchRequestParam flowParam) throws Exception {
      DefaultTaskActionBatchCmd batchCmd = new DefaultTaskActionBatchCmd();
      batchCmd.executeCmd(flowParam);
      return this.getSuccessResult("????????????");
   }

   @RequestMapping(
      value = {"unLock"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr
   @ApiOperation(
      value = "??????????????????",
      notes = "???????????????????????????????????????????????????????????????????????????????????????"
   )
   public ResultMsg<String> unLock(@RequestParam @ApiParam(value = "??????ID",required = true) String taskId) throws Exception {
      this.bpmTaskManager.unLockTask(taskId);
      return this.getSuccessResult("??????????????????");
   }

   @RequestMapping(
      value = {"assignTask"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr
   @ApiOperation(
      value = "????????????",
      notes = "????????????????????????????????????????????????"
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
      return this.getSuccessResult("????????????");
   }

   @RequestMapping(
      value = {"handleNodeFreeSelectUser"},
      method = {RequestMethod.POST, RequestMethod.GET}
   )
   @CatchErr(
      write2response = true
   )
   @ApiOperation(
      value = "???????????? ?????????????????????????????????",
      hidden = true,
      notes = "??????????????????????????????????????????????????????????????????????????????"
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
      value = "????????????????????????",
      hidden = true,
      notes = "????????????????????????????????????????????????"
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
      value = "????????????????????????",
      hidden = true,
      notes = "????????????ID ??????????????????????????????????????????"
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
      value = "??????????????????",
      notes = "??????????????????"
   )
   public ResultMsg<Map> getVariables(@RequestParam @ApiParam(value = "??????ID",required = true) String taskId, @RequestParam @ApiParam(value = "isLocal",defaultValue = "false") Boolean isLocal) throws Exception {
      return !isLocal ? this.getSuccessResult(((ActTaskService)AppUtil.getBean(ActTaskService.class)).getVariables(taskId)) : this.getSuccessResult(((ActTaskService)AppUtil.getBean(ActTaskService.class)).getVariablesLocal(taskId));
   }

   @PostMapping({"doActions"})
   @CatchErr(
      write2errorlog = true
   )
   @ApiOperation(
      value = "??????????????????????????????",
      notes = "????????????????????????????????? ???????????????????????????????????????????????????????????????????????????????????????"
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
                  result.put("msg", actionName + "??????");
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
      return this.getSuccessResult(results, "??????????????????");
   }

   @RequestMapping(
      value = {"todoTaskList"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "??????",
      notes = "????????????????????????subject_,name_???Task??????????????????filter??????????????????????????????,???V???????????????????????????,???EQ/IN/LK/..??????????????????????????????/in/??????/..???"
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
   value = "?????????????????????20???"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "????????????"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "order",
   value = "?????????????????????",
   defaultValue = "ASC"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "????????????"
)})
   public PageResult<BpmTaskVO> getTaskLinksInfo(HttpServletRequest request, HttpServletResponse reponse) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      return new PageResult(this.bpmTaskManager.getTaskLinksInfo(queryFilter));
   }
}
