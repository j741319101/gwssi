/*     */ package com.dstz.bpm.rest.controller;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*     */ import com.dstz.bpm.api.model.form.BpmForm;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.BpmDefinitionDuplicateDTO;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import com.dstz.bpm.rest.vo.BpmDefinitionVO;
/*     */ import com.dstz.bpm.rest.vo.UserTaskNodeVO;
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.aop.annotion.OperateLog;
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.BeanCopierUtils;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.BaseController;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/bpm/definition"})
/*     */ public class BpmDefinitionController extends BaseController<BpmDefinition> {
/*  65 */   private Logger logger = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */   
/*     */   @Resource
/*     */   BpmDefinitionManager bpmDefinitionManager;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   @Autowired
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   
/*     */   @Autowired
/*     */   BpmDecentralizationService decentralizationService;
/*     */   
/*     */   @Resource
/*     */   UserService userService;
/*     */ 
/*     */   
/*     */   @OperateLog
/*     */   @RequestMapping({"listJson"})
/*     */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) {
/*  94 */     String formKey = request.getParameter("formKey");
/*  95 */     QueryFilter queryFilter = getQueryFilter(request);
/*  96 */     if (!queryFilter.getParams().containsKey("isVersions")) {
/*  97 */       queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
/*     */     }
/*  99 */     if (this.decentralizationService.decentralizationEnable("wf")) {
/* 100 */       IUser user = this.iCurrentContext.getCurrentUser();
/* 101 */       List<String> lstOrgId = new ArrayList<>();
/* 102 */       if (null != user) {
/* 103 */         lstOrgId = user.getManagerGroupIdList();
/*     */       }
/* 105 */       if (null != lstOrgId && lstOrgId.size() > 0) {
/* 106 */         queryFilter.addFilter("org_id_", lstOrgId, QueryOP.IN);
/*     */       } else {
/* 108 */         queryFilter.addFilter("org_id_", "", QueryOP.EQUAL);
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     List<BpmDefinition> bpmDefinitionList = this.bpmDefinitionManager.query(queryFilter);
/* 113 */     if (StringUtils.isNotEmpty(formKey)) {
/* 114 */       for (int i = 0; i < bpmDefinitionList.size(); i++) {
/*     */         try {
/* 116 */           if (StringUtils.isNotEmpty(((BpmDefinition)bpmDefinitionList.get(i)).getActDeployId())) {
/*     */             
/* 118 */             DefaultBpmProcessDef defaultBpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(((BpmDefinition)bpmDefinitionList.get(i)).getId());
/* 119 */             String defId = ((BpmDefinition)bpmDefinitionList.get(i)).getKey();
/* 120 */             BpmForm bpmForm = defaultBpmProcessDef.getGlobalForm();
/* 121 */             if (bpmForm == null || !bpmForm.getFormValue().equals(formKey)) {
/* 122 */               bpmDefinitionList.remove(i);
/* 123 */               i--;
/*     */             } 
/*     */           } else {
/* 126 */             bpmDefinitionList.remove(i);
/* 127 */             i--;
/*     */           } 
/* 129 */         } catch (Exception e) {
/* 130 */           bpmDefinitionList.remove(i);
/* 131 */           i--;
/*     */         } 
/*     */       } 
/*     */     }
/* 135 */     List<BpmDefinitionVO> bpmDefinitionVOList = BeanCopierUtils.transformList(bpmDefinitionList, BpmDefinitionVO.class);
/* 136 */     Map<String, String> userMap = this.userService.getUserMapByUserIds(getUserIdSet(bpmDefinitionVOList));
/* 137 */     makeUserInfo(userMap, bpmDefinitionVOList);
/*     */     
/* 139 */     return new PageResult(bpmDefinitionList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"getNew"})
/*     */   @CatchErr
/*     */   public ResultMsg<BpmDefinitionVO> getNew(@RequestParam String id) throws Exception {
/* 148 */     BpmDefinition t = null;
/* 149 */     BpmDefinitionVO bpmDefinitionVO = null;
/* 150 */     if (StringUtil.isNotEmpty(id)) {
/* 151 */       t = (BpmDefinition)this.bpmDefinitionManager.get(id);
/* 152 */       bpmDefinitionVO = (BpmDefinitionVO)BeanCopierUtils.transformBean(t, BpmDefinitionVO.class);
/* 153 */       Map<String, String> userMap = this.userService.getUserMapByUserIds(getUserIdSet(bpmDefinitionVO));
/* 154 */       makeUserInfo(userMap, bpmDefinitionVO);
/* 155 */       if (null != bpmDefinitionVO && StringUtils.isNotEmpty(bpmDefinitionVO.getLockBy())) {
/* 156 */         IUser user = this.userService.getUserById(bpmDefinitionVO.getLockBy());
/* 157 */         if (null != user) {
/* 158 */           bpmDefinitionVO.setLockUser(user.getFullname());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     return getSuccessResult(bpmDefinitionVO);
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"save"})
/*     */   @OperateLog
/*     */   @CatchErr(write2response = true, value = "保存流程定义失败")
/*     */   public ResultMsg<String> save(@RequestBody BpmDefinition bpmDefinition) {
/* 171 */     this.bpmDefinitionManager.create(bpmDefinition);
/*     */     
/* 173 */     return getSuccessResult(bpmDefinition.getActModelId(), "创建成功");
/*     */   }
/*     */   
/*     */   @RequestMapping({"setMain"})
/*     */   @OperateLog
/*     */   public ResultMsg<String> setMain(HttpServletRequest request) {
/* 179 */     String definitionId = RequestUtil.getString(request, "id");
/*     */     
/* 181 */     this.bpmDefinitionManager.setDefinition2Main(definitionId);
/* 182 */     return getSuccessResult("操作成功！");
/*     */   }
/*     */   
/*     */   @RequestMapping({"clearSysCache"})
/*     */   @OperateLog
/*     */   @CatchErr("清除缓存失败")
/*     */   public ResultMsg<String> clearCache() {
/* 189 */     ((ICache)Objects.<Object>requireNonNull(AppUtil.getBean(ICache.class))).clearAll();
/* 190 */     this.bpmDefinitionManager.clearBpmnModelCache(null);
/* 191 */     return getSuccessResult("成功清除所有系统缓存");
/*     */   }
/*     */   
/*     */   @ApiOperation("获取流程定义数量")
/*     */   @RequestMapping({"getDefNumByTree"})
/*     */   public ResultMsg<List<Map>> getDefNumByTree() {
/* 197 */     return getSuccessResult(this.bpmDefinitionManager.getDefNumByTree());
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getModelDesc() {
/* 202 */     return "流程定义";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/getAllUserTaskNode"}, produces = {"application/json;charset=UTF-8"}, consumes = {"application/json;charset=UTF-8"})
/*     */   public ResultMsg<List<UserTaskNodeVO>> getAllUserTaskNode(@RequestBody FlowRequestParam flowRequestParam) {
/*     */     String defId;
/* 213 */     BpmContext.cleanTread();
/*     */ 
/*     */     
/* 216 */     if (StringUtils.isNotEmpty(flowRequestParam.getDefId())) {
/* 217 */       defId = flowRequestParam.getDefId();
/* 218 */     } else if (StringUtils.isNotEmpty(flowRequestParam.getDefKey())) {
/* 219 */       BpmDefinition bpmDefinition = this.bpmDefinitionManager.getByKey(flowRequestParam.getDefKey());
/* 220 */       if (bpmDefinition == null) {
/* 221 */         return ResultMsg.ERROR(String.format("流程定义(%s)不存在", new Object[] { flowRequestParam.getDefKey() }));
/*     */       }
/* 223 */       defId = bpmDefinition.getId();
/* 224 */     } else if (StringUtils.isNotEmpty(flowRequestParam.getInstanceId())) {
/* 225 */       BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(flowRequestParam.getInstanceId());
/* 226 */       if (bpmInstance == null) {
/* 227 */         return ResultMsg.ERROR(String.format("流程实例不存在(%s)", new Object[] { flowRequestParam.getInstanceId() }));
/*     */       }
/* 229 */       defId = bpmInstance.getDefId();
/*     */     } else {
/* 231 */       return ResultMsg.ERROR("参数为空, 请至少传递一个参数(defId、defKey、instanceId)");
/*     */     } 
/* 233 */     List<UserTaskNodeVO> userNodeS = new ArrayList<>();
/* 234 */     this.bpmProcessDefService.getAllNodeDef(defId).stream().filter(bpmNode -> (bpmNode.getType() == NodeType.USERTASK)).forEach(bpmNode -> userNodeS.add(new UserTaskNodeVO(bpmNode.getNodeId(), bpmNode.getName())));
/*     */ 
/*     */     
/* 237 */     return ResultMsg.SUCCESS(userNodeS);
/*     */   }
/*     */   @PostMapping(value = {"/getAssignUserTaskNode"}, produces = {"application/json;charset=UTF-8"}, consumes = {"application/json;charset=UTF-8"})
/*     */   public ResultMsg<List<UserTaskNodeVO>> getAssignUserTaskNode(@RequestBody FlowRequestParam flowRequestParam) {
/*     */     String defId;
/* 242 */     BpmContext.cleanTread();
/*     */ 
/*     */     
/* 245 */     if (StringUtils.isNotEmpty(flowRequestParam.getDefId())) {
/* 246 */       defId = flowRequestParam.getDefId();
/* 247 */     } else if (StringUtils.isNotEmpty(flowRequestParam.getDefKey())) {
/* 248 */       BpmDefinition bpmDefinition = this.bpmDefinitionManager.getByKey(flowRequestParam.getDefKey());
/* 249 */       if (bpmDefinition == null) {
/* 250 */         return ResultMsg.ERROR(String.format("流程定义(%s)不存在", new Object[] { flowRequestParam.getDefKey() }));
/*     */       }
/* 252 */       defId = bpmDefinition.getId();
/* 253 */     } else if (StringUtils.isNotEmpty(flowRequestParam.getInstanceId())) {
/* 254 */       BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(flowRequestParam.getInstanceId());
/* 255 */       if (bpmInstance == null) {
/* 256 */         return ResultMsg.ERROR(String.format("流程实例不存在(%s)", new Object[] { flowRequestParam.getInstanceId() }));
/*     */       }
/* 258 */       defId = bpmInstance.getDefId();
/*     */     } else {
/* 260 */       return ResultMsg.ERROR("参数为空, 请至少传递一个参数(defId、defKey、instanceId)");
/*     */     } 
/* 262 */     List<UserTaskNodeVO> userNodeS = new ArrayList<>();
/* 263 */     this.bpmProcessDefService.getAllNodeDef(defId).stream().filter(bpmNode -> {
/*     */           if (bpmNode.getType() == NodeType.USERTASK) {
/*     */             SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)bpmNode.getPluginContext(SignTaskPluginContext.class);
/*     */             
/*     */             SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef();
/*     */             
/*     */             if (signTaskPluginDef.isSignMultiTask() && signTaskPluginDef.isNeedSupervise()) {
/*     */               return false;
/*     */             }
/*     */             
/*     */             DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)bpmNode.getPluginContext(DynamicTaskPluginContext.class);
/*     */             DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
/*     */             return !dynamicTaskPluginDef.getEnabled().booleanValue();
/*     */           } 
/*     */           return false;
/* 278 */         }).forEach(bpmNode -> userNodeS.add(new UserTaskNodeVO(bpmNode.getNodeId(), bpmNode.getName())));
/*     */ 
/*     */     
/* 281 */     return ResultMsg.SUCCESS(userNodeS);
/*     */   }
/*     */   
/*     */   @PostMapping({"/test/doAction"})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程定义测试", notes = "执行流程定义测试相关动作")
/*     */   public ResultMsg doAction(@RequestBody FlowRequestParam flowParam) {
/* 288 */     List<String> nodeIds = new ArrayList<>(0);
/* 289 */     BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(flowParam.getDefId());
/* 290 */     BpmNodeDef startEvent = bpmProcessDef.getStartEvent();
/* 291 */     nodeIds.add(startEvent.getNodeId());
/* 292 */     List<BpmNodeDef> nodeDefs = startEvent.getOutcomeNodes();
/* 293 */     recursionNode(nodeDefs, nodeIds);
/* 294 */     long distinctCount = nodeIds.stream().distinct().count();
/* 295 */     if (nodeIds.size() != distinctCount) {
/* 296 */       return getWarnResult("流程中不能存在环路、回路");
/*     */     }
/* 298 */     DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
/* 299 */     String actionName = instanceCmd.executeCmd();
/* 300 */     String instId = instanceCmd.getInstanceId();
/*     */     
/* 302 */     List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getByInstanceId(instId);
/*     */     
/* 304 */     Iterator<BpmTaskStack> stacks = bpmTaskStacks.iterator();
/* 305 */     while (stacks.hasNext()) {
/* 306 */       BpmTaskStack stack = stacks.next();
/* 307 */       if ("sequenceFlow".equals(stack.getNodeType())) {
/* 308 */         stacks.remove();
/*     */       }
/*     */     } 
/* 311 */     Collections.sort(bpmTaskStacks, new Comparator<BpmTaskStack>()
/*     */         {
/*     */           public int compare(BpmTaskStack o1, BpmTaskStack o2) {
/* 314 */             return o1.getStartTime().compareTo(o2.getStartTime());
/*     */           }
/*     */         });
/*     */     
/* 318 */     this.bpmInstanceManager.delete(instId);
/* 319 */     List<String> traceIds = new ArrayList<>(0);
/* 320 */     if (!CollectionUtils.isEmpty(bpmTaskStacks)) {
/* 321 */       for (BpmTaskStack taskStack : bpmTaskStacks) {
/* 322 */         traceIds.add(taskStack.getId());
/*     */       }
/*     */     }
/* 325 */     if (!CollectionUtils.isEmpty(traceIds)) {
/* 326 */       this.bpmTaskStackManager.removeByIds(traceIds.<Serializable>toArray((Serializable[])new String[traceIds.size()]));
/*     */     }
/* 328 */     return getSuccessResult(bpmTaskStacks, "测试成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void recursionNode(List<BpmNodeDef> nodeDefs, List<String> nodeIds) {
/* 338 */     for (BpmNodeDef bpmNodeDef : nodeDefs) {
/* 339 */       nodeIds.add(bpmNodeDef.getNodeId());
/* 340 */       List<BpmNodeDef> nodes = bpmNodeDef.getOutcomeNodes();
/* 341 */       if (!CollectionUtils.isEmpty(nodes)) {
/* 342 */         recursionNode(nodes, nodeIds);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   @ApiOperation("流程定义复制")
/*     */   @RequestMapping(value = {"/duplicate"}, method = {RequestMethod.POST}, produces = {"application/json"}, consumes = {"application/json"})
/*     */   public ResultMsg<String> duplicate(@RequestBody BpmDefinitionDuplicateDTO bpmDefinitionDuplicateDTO) {
/* 358 */     BpmDefinition bpmDefinition = this.bpmDefinitionManager.duplicate(bpmDefinitionDuplicateDTO);
/* 359 */     return ResultMsg.SUCCESS(bpmDefinition.getActModelId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> getUserIdSet(List<BpmDefinitionVO> lstModel) {
/* 369 */     Set<String> set = new HashSet<>();
/* 370 */     if (null != lstModel && lstModel.size() > 0) {
/* 371 */       lstModel.forEach(temp -> {
/*     */             if (null != temp) {
/*     */               set.add(temp.getCreateBy());
/*     */               set.add(temp.getUpdateBy());
/*     */             } 
/*     */           });
/*     */     }
/* 378 */     set.remove(null);
/* 379 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeUserInfo(Map<String, String> mapUser, List<BpmDefinitionVO> lstModel) {
/* 389 */     if (null != mapUser && mapUser.size() > 0 && null != lstModel && lstModel.size() > 0) {
/* 390 */       lstModel.forEach(temp -> {
/*     */             if (null != temp) {
/*     */               temp.setCreateUser((String)mapUser.get(temp.getCreateBy()));
/*     */               temp.setUpdateUser((String)mapUser.get(temp.getUpdateBy()));
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public static Set<String> getUserIdSet(BpmDefinitionVO model) {
/* 400 */     Set<String> set = new HashSet<>();
/* 401 */     if (null != model) {
/* 402 */       set.add(model.getCreateBy());
/* 403 */       set.add(model.getUpdateBy());
/*     */     } 
/* 405 */     set.remove(null);
/* 406 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeUserInfo(Map<String, String> mapUser, BpmDefinitionVO model) {
/* 416 */     if (null != model) {
/* 417 */       model.setCreateUser(mapUser.get(model.getCreateBy()));
/* 418 */       model.setUpdateUser(mapUser.get(model.getUpdateBy()));
/*     */     } 
/*     */   }
/*     */   
/*     */   @RequestMapping({"lock"})
/*     */   @CatchErr(write2response = true, value = "")
/*     */   public ResultMsg<BpmDefinition> lock(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "id") String id, @RequestParam(name = "isLock") Boolean isLock) {
/* 425 */     if (isLock.booleanValue()) {
/* 426 */       this.bpmDefinitionManager.lock(id);
/*     */     } else {
/* 428 */       this.bpmDefinitionManager.unlock(id);
/*     */     } 
/* 430 */     BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get(id);
/* 431 */     if (null != bpmDefinition) {
/* 432 */       BpmDefinitionVO bpmDefinitionVO = (BpmDefinitionVO)BeanCopierUtils.transformBean(bpmDefinition, BpmDefinitionVO.class);
/* 433 */       if (null != bpmDefinitionVO && StringUtils.isNotEmpty(bpmDefinitionVO.getLockBy())) {
/* 434 */         IUser user = this.userService.getUserById(bpmDefinitionVO.getLockBy());
/* 435 */         if (null != user) {
/* 436 */           bpmDefinitionVO.setLockUser(user.getFullname());
/*     */         }
/*     */       } 
/*     */     } 
/* 440 */     return getSuccessResult(bpmDefinition);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/controller/BpmDefinitionController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */