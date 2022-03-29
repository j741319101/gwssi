package com.dstz.bpm.wf.rest.controller;

import com.dstz.base.api.aop.annotion.OperateLog;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.form.BpmForm;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.model.BpmDefinitionDuplicateDTO;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.aop.annotion.OperateLog;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.cache.ICache;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.BeanCopierUtils;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.rest.BaseController;
import com.dstz.base.rest.util.RequestUtil;
import com.dstz.bpm.service.BpmDecentralizationService;
import com.dstz.bpm.wf.rest.vo.BpmDefinitionVO;
import com.dstz.bpm.wf.rest.vo.UserTaskNodeVO;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm/definition"})
public class BpmDefinitionController extends BaseController<BpmDefinition> {
   private Logger logger = LoggerFactory.getLogger(this.getClass());
   @Resource
   BpmDefinitionManager bpmDefinitionManager;
   @Autowired
   private BpmProcessDefService bpmProcessDefService;
   @Autowired
   private BpmInstanceManager bpmInstanceManager;
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;
   @Resource
   ICurrentContext iCurrentContext;
   @Autowired
   BpmDecentralizationService decentralizationService;
   @Resource
   UserService userService;

   @OperateLog
   @RequestMapping({"listJson"})
   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) {
      String formKey = request.getParameter("formKey");
      QueryFilter queryFilter = this.getQueryFilter(request);
      if (!queryFilter.getParams().containsKey("isVersions")) {
         queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
      }

      if (this.decentralizationService.decentralizationEnable("wf")) {
         IUser user = this.iCurrentContext.getCurrentUser();
         List<String> lstOrgId = new ArrayList();
         if (null != user) {
            lstOrgId = user.getManagerGroupIdList();
         }

         if (null != lstOrgId && ((List)lstOrgId).size() > 0) {
            queryFilter.addFilter("org_id_", lstOrgId, QueryOP.IN);
         } else {
            queryFilter.addFilter("org_id_", "", QueryOP.EQUAL);
         }
      }

      List<BpmDefinition> bpmDefinitionList = this.bpmDefinitionManager.query(queryFilter);
      if (StringUtils.isNotEmpty(formKey)) {
         for(int i = 0; i < bpmDefinitionList.size(); ++i) {
            try {
               if (StringUtils.isNotEmpty(((BpmDefinition)bpmDefinitionList.get(i)).getActDeployId())) {
                  DefaultBpmProcessDef defaultBpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(((BpmDefinition)bpmDefinitionList.get(i)).getId());
                  String defId = ((BpmDefinition)bpmDefinitionList.get(i)).getKey();
                  BpmForm bpmForm = defaultBpmProcessDef.getGlobalForm();
                  if (bpmForm == null || !bpmForm.getFormValue().equals(formKey)) {
                     bpmDefinitionList.remove(i);
                     --i;
                  }
               } else {
                  bpmDefinitionList.remove(i);
                  --i;
               }
            } catch (Exception var10) {
               bpmDefinitionList.remove(i);
               --i;
            }
         }
      }

      List<BpmDefinitionVO> bpmDefinitionVOList = BeanCopierUtils.transformList(bpmDefinitionList, BpmDefinitionVO.class);
      Map<String, String> userMap = this.userService.getUserMapByUserIds(getUserIdSet(bpmDefinitionVOList));
      makeUserInfo(userMap, bpmDefinitionVOList);
      return new PageResult(bpmDefinitionList);
   }

   @RequestMapping({"getNew"})
   @CatchErr
   public ResultMsg<BpmDefinitionVO> getNew(@RequestParam String id) throws Exception {
      BpmDefinition t = null;
      BpmDefinitionVO bpmDefinitionVO = null;
      if (StringUtil.isNotEmpty(id)) {
         t = (BpmDefinition)this.bpmDefinitionManager.get(id);
         bpmDefinitionVO = (BpmDefinitionVO)BeanCopierUtils.transformBean(t, BpmDefinitionVO.class);
         Map<String, String> userMap = this.userService.getUserMapByUserIds(getUserIdSet(bpmDefinitionVO));
         makeUserInfo(userMap, bpmDefinitionVO);
         if (null != bpmDefinitionVO && StringUtils.isNotEmpty(bpmDefinitionVO.getLockBy())) {
            IUser user = this.userService.getUserById(bpmDefinitionVO.getLockBy());
            if (null != user) {
               bpmDefinitionVO.setLockUser(user.getFullname());
            }
         }
      }

      return this.getSuccessResult(bpmDefinitionVO);
   }

   @RequestMapping({"save"})
   @OperateLog
   @CatchErr(
      write2response = true,
      value = "保存流程定义失败"
   )
   public ResultMsg<String> save(@RequestBody BpmDefinition bpmDefinition) {
      this.bpmDefinitionManager.create(bpmDefinition);
      return this.getSuccessResult(bpmDefinition.getActModelId(), "创建成功");
   }

   @RequestMapping({"setMain"})
   @OperateLog
   public ResultMsg<String> setMain(HttpServletRequest request) {
      String definitionId = RequestUtil.getString(request, "id");
      this.bpmDefinitionManager.setDefinition2Main(definitionId);
      return this.getSuccessResult("操作成功！");
   }

   @RequestMapping({"clearSysCache"})
   @OperateLog
   @CatchErr("清除缓存失败")
   public ResultMsg<String> clearCache() {
      ((ICache)Objects.requireNonNull(AppUtil.getBean(ICache.class))).clearAll();
      this.bpmDefinitionManager.clearBpmnModelCache((String)null);
      return this.getSuccessResult("成功清除所有系统缓存");
   }

   @ApiOperation("获取流程定义数量")
   @RequestMapping({"getDefNumByTree"})
   public ResultMsg<List<Map>> getDefNumByTree() {
      return this.getSuccessResult(this.bpmDefinitionManager.getDefNumByTree());
   }

   protected String getModelDesc() {
      return "流程定义";
   }

   @PostMapping(
      value = {"/getAllUserTaskNode"},
      produces = {"application/json;charset=UTF-8"},
      consumes = {"application/json;charset=UTF-8"}
   )
   public ResultMsg<List<UserTaskNodeVO>> getAllUserTaskNode(@RequestBody FlowRequestParam flowRequestParam) {
      BpmContext.cleanTread();
      String defId;
      if (StringUtils.isNotEmpty(flowRequestParam.getDefId())) {
         defId = flowRequestParam.getDefId();
      } else if (StringUtils.isNotEmpty(flowRequestParam.getDefKey())) {
         BpmDefinition bpmDefinition = this.bpmDefinitionManager.getByKey(flowRequestParam.getDefKey());
         if (bpmDefinition == null) {
            return ResultMsg.ERROR(String.format("流程定义(%s)不存在", flowRequestParam.getDefKey()));
         }

         defId = bpmDefinition.getId();
      } else {
         if (!StringUtils.isNotEmpty(flowRequestParam.getInstanceId())) {
            return ResultMsg.ERROR("参数为空, 请至少传递一个参数(defId、defKey、instanceId)");
         }

         BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(flowRequestParam.getInstanceId());
         if (bpmInstance == null) {
            return ResultMsg.ERROR(String.format("流程实例不存在(%s)", flowRequestParam.getInstanceId()));
         }

         defId = bpmInstance.getDefId();
      }

      List<UserTaskNodeVO> userNodeS = new ArrayList();
      this.bpmProcessDefService.getAllNodeDef(defId).stream().filter((bpmNode) -> {
         return bpmNode.getType() == NodeType.USERTASK;
      }).forEach((bpmNode) -> {
         userNodeS.add(new UserTaskNodeVO(bpmNode.getNodeId(), bpmNode.getName()));
      });
      return ResultMsg.SUCCESS(userNodeS);
   }

   @PostMapping(
      value = {"/getAssignUserTaskNode"},
      produces = {"application/json;charset=UTF-8"},
      consumes = {"application/json;charset=UTF-8"}
   )
   public ResultMsg<List<UserTaskNodeVO>> getAssignUserTaskNode(@RequestBody FlowRequestParam flowRequestParam) {
      BpmContext.cleanTread();
      String defId;
      if (StringUtils.isNotEmpty(flowRequestParam.getDefId())) {
         defId = flowRequestParam.getDefId();
      } else if (StringUtils.isNotEmpty(flowRequestParam.getDefKey())) {
         BpmDefinition bpmDefinition = this.bpmDefinitionManager.getByKey(flowRequestParam.getDefKey());
         if (bpmDefinition == null) {
            return ResultMsg.ERROR(String.format("流程定义(%s)不存在", flowRequestParam.getDefKey()));
         }

         defId = bpmDefinition.getId();
      } else {
         if (!StringUtils.isNotEmpty(flowRequestParam.getInstanceId())) {
            return ResultMsg.ERROR("参数为空, 请至少传递一个参数(defId、defKey、instanceId)");
         }

         BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(flowRequestParam.getInstanceId());
         if (bpmInstance == null) {
            return ResultMsg.ERROR(String.format("流程实例不存在(%s)", flowRequestParam.getInstanceId()));
         }

         defId = bpmInstance.getDefId();
      }

      List<UserTaskNodeVO> userNodeS = new ArrayList();
      this.bpmProcessDefService.getAllNodeDef(defId).stream().filter((bpmNode) -> {
         if (bpmNode.getType() == NodeType.USERTASK) {
            SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)bpmNode.getPluginContext(SignTaskPluginContext.class);
            SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef();
            if (signTaskPluginDef.isSignMultiTask() && signTaskPluginDef.isNeedSupervise()) {
               return false;
            } else {
               DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)bpmNode.getPluginContext(DynamicTaskPluginContext.class);
               DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
               return !dynamicTaskPluginDef.getEnabled();
            }
         } else {
            return false;
         }
      }).forEach((bpmNode) -> {
         userNodeS.add(new UserTaskNodeVO(bpmNode.getNodeId(), bpmNode.getName()));
      });
      return ResultMsg.SUCCESS(userNodeS);
   }

   @PostMapping({"/test/doAction"})
   @CatchErr
   @ApiOperation(
      value = "流程定义测试",
      notes = "执行流程定义测试相关动作"
   )
   public ResultMsg doAction(@RequestBody FlowRequestParam flowParam) {
      List<String> nodeIds = new ArrayList(0);
      BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(flowParam.getDefId());
      BpmNodeDef startEvent = bpmProcessDef.getStartEvent();
      nodeIds.add(startEvent.getNodeId());
      List<BpmNodeDef> nodeDefs = startEvent.getOutcomeNodes();
      this.recursionNode(nodeDefs, nodeIds);
      long distinctCount = nodeIds.stream().distinct().count();
      if ((long)nodeIds.size() != distinctCount) {
         return this.getWarnResult("流程中不能存在环路、回路");
      } else {
         DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
         String actionName = instanceCmd.executeCmd();
         String instId = instanceCmd.getInstanceId();
         List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getByInstanceId(instId);
         Iterator stacks = bpmTaskStacks.iterator();

         while(stacks.hasNext()) {
            BpmTaskStack stack = (BpmTaskStack)stacks.next();
            if ("sequenceFlow".equals(stack.getNodeType())) {
               stacks.remove();
            }
         }

         Collections.sort(bpmTaskStacks, new Comparator<BpmTaskStack>() {
            public int compare(BpmTaskStack o1, BpmTaskStack o2) {
               return o1.getStartTime().compareTo(o2.getStartTime());
            }
         });
         this.bpmInstanceManager.delete(instId);
         List<String> traceIds = new ArrayList(0);
         if (!CollectionUtils.isEmpty(bpmTaskStacks)) {
            Iterator var14 = bpmTaskStacks.iterator();

            while(var14.hasNext()) {
               BpmTaskStack taskStack = (BpmTaskStack)var14.next();
               traceIds.add(taskStack.getId());
            }
         }

         if (!CollectionUtils.isEmpty(traceIds)) {
            this.bpmTaskStackManager.removeByIds( traceIds.toArray(new String[traceIds.size()]));
         }

         return this.getSuccessResult(bpmTaskStacks, "测试成功");
      }
   }

   void recursionNode(List<BpmNodeDef> nodeDefs, List<String> nodeIds) {
      Iterator var3 = nodeDefs.iterator();

      while(var3.hasNext()) {
         BpmNodeDef bpmNodeDef = (BpmNodeDef)var3.next();
         nodeIds.add(bpmNodeDef.getNodeId());
         List<BpmNodeDef> nodes = bpmNodeDef.getOutcomeNodes();
         if (!CollectionUtils.isEmpty(nodes)) {
            this.recursionNode(nodes, nodeIds);
         }
      }

   }

   @CatchErr
   @OperateLog
   @ApiOperation("流程定义复制")
   @RequestMapping(
      value = {"/duplicate"},
      method = {RequestMethod.POST},
      produces = {"application/json"},
      consumes = {"application/json"}
   )
   public ResultMsg<String> duplicate(@RequestBody BpmDefinitionDuplicateDTO bpmDefinitionDuplicateDTO) {
      BpmDefinition bpmDefinition = this.bpmDefinitionManager.duplicate(bpmDefinitionDuplicateDTO);
      return ResultMsg.SUCCESS(bpmDefinition.getActModelId());
   }

   public static Set<String> getUserIdSet(List<BpmDefinitionVO> lstModel) {
      Set<String> set = new HashSet();
      if (null != lstModel && lstModel.size() > 0) {
         lstModel.forEach((temp) -> {
            if (null != temp) {
               set.add(temp.getCreateBy());
               set.add(temp.getUpdateBy());
            }

         });
      }

      set.remove((Object)null);
      return set;
   }

   public static void makeUserInfo(Map<String, String> mapUser, List<BpmDefinitionVO> lstModel) {
      if (null != mapUser && mapUser.size() > 0 && null != lstModel && lstModel.size() > 0) {
         lstModel.forEach((temp) -> {
            if (null != temp) {
               temp.setCreateUser((String)mapUser.get(temp.getCreateBy()));
               temp.setUpdateUser((String)mapUser.get(temp.getUpdateBy()));
            }

         });
      }

   }

   public static Set<String> getUserIdSet(BpmDefinitionVO model) {
      Set<String> set = new HashSet();
      if (null != model) {
         set.add(model.getCreateBy());
         set.add(model.getUpdateBy());
      }

      set.remove((Object)null);
      return set;
   }

   public static void makeUserInfo(Map<String, String> mapUser, BpmDefinitionVO model) {
      if (null != model) {
         model.setCreateUser((String)mapUser.get(model.getCreateBy()));
         model.setUpdateUser((String)mapUser.get(model.getUpdateBy()));
      }

   }

   @RequestMapping({"lock"})
   @CatchErr(
      write2response = true,
      value = ""
   )
   public ResultMsg<BpmDefinition> lock(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "id") String id, @RequestParam(name = "isLock") Boolean isLock) {
      if (isLock) {
         this.bpmDefinitionManager.lock(id);
      } else {
         this.bpmDefinitionManager.unlock(id);
      }

      BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get(id);
      if (null != bpmDefinition) {
         BpmDefinitionVO bpmDefinitionVO = (BpmDefinitionVO)BeanCopierUtils.transformBean(bpmDefinition, BpmDefinitionVO.class);
         if (null != bpmDefinitionVO && StringUtils.isNotEmpty(bpmDefinitionVO.getLockBy())) {
            IUser user = this.userService.getUserById(bpmDefinitionVO.getLockBy());
            if (null != user) {
               bpmDefinitionVO.setLockUser(user.getFullname());
            }
         }
      }

      return this.getSuccessResult(bpmDefinition);
   }
}
