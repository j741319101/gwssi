/*     */ package cn.gwssi.ecloudbpm.wf.rest.script;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.BpmExecutionStack;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.remote.BpmRemoteBusinessData;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.remote.FormHandlerResult;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmRuntimeService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*     */ import cn.gwssi.ecloudframework.base.core.util.RestTemplateUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.org.api.service.GroupService;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IScript;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.bean.BeanUtil;
/*     */ import cn.hutool.core.bean.copier.CopyOptions;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.delegate.VariableScope;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class BpmScriptUtil implements IScript {
/*  53 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   protected ActTaskService actTaskService;
/*     */   
/*     */   @Resource
/*     */   private UserService userService;
/*     */   
/*     */   @Resource
/*     */   BpmTaskOpinionManager opinionManager;
/*     */   
/*     */   @Resource
/*     */   GroupService groupService;
/*     */   
/*     */   @Resource
/*     */   private BpmTaskManager taskMaanger;
/*     */   
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource(name = "defaultBpmRuntimeServiceImpl")
/*     */   private BpmRuntimeService runtimeService;
/*     */   
/*     */   public Map<String, Object> getVariables(String taskId) {
/*  80 */     return this.actTaskService.getVariables(taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getVariableByTaskId(String taskId, String variableName) {
/*  85 */     return this.actTaskService.getVariable(taskId, variableName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getVariableLocalByTaskId(String taskId, String variableName) {
/*  90 */     return this.actTaskService.getVariableLocal(taskId, variableName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endProcessByInstanceId(String instanceId, String opinion) {
/* 100 */     if (ContextUtil.getCurrentUser() == null) {
/* 101 */       ContextUtil.setCurrentUserByAccount("admin");
/*     */     }
/* 103 */     List<BpmTask> task = this.taskMaanger.getByInstId(instanceId);
/* 104 */     if (CollectionUtil.isEmpty(task)) {
/* 105 */       throw new BusinessException("当前流程实例下没有找到任务，请检查! instanceId:" + instanceId);
/*     */     }
/* 107 */     endProcessByTaskId(((BpmTask)task.get(0)).getId(), opinion);
/*     */   }
/*     */   
/*     */   public void endProcessByTaskId(String taskId, String opinion) {
/* 111 */     if (ContextUtil.getCurrentUser() == null) {
/* 112 */       ContextUtil.setCurrentUserByAccount("admin");
/*     */     }
/*     */     
/* 115 */     DefualtTaskActionCmd taskCmd = new DefualtTaskActionCmd();
/* 116 */     taskCmd.setTaskId(taskId);
/* 117 */     taskCmd.setActionName(ActionType.MANUALEND.getKey());
/* 118 */     taskCmd.setOpinion(opinion);
/* 119 */     taskCmd.executeCmd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void complateTaskById(String taskId, String opinion) {
/* 129 */     if (ContextUtil.getCurrentUser() == null) {
/* 130 */       ContextUtil.setCurrentUserByAccount("admin");
/*     */     }
/*     */     
/* 133 */     DefualtTaskActionCmd taskCmd = new DefualtTaskActionCmd();
/* 134 */     taskCmd.setTaskId(taskId);
/* 135 */     taskCmd.setActionName(ActionType.AGREE.getKey());
/* 136 */     taskCmd.setOpinion(opinion);
/* 137 */     taskCmd.executeCmd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void httpFormHandler(String url) {
/* 145 */     ActionCmd actionCmd = BpmContext.getActionModel();
/*     */     
/* 147 */     String bizKey = actionCmd.getBusinessKey();
/*     */     
/* 149 */     JSONObject object = actionCmd.getBusData();
/* 150 */     BpmRemoteBusinessData<JSONObject> remoteData = new BpmRemoteBusinessData();
/* 151 */     remoteData.setData(object);
/* 152 */     remoteData.setFlowKey(actionCmd.getBpmInstance().getDefKey());
/* 153 */     remoteData.setInstanceId(actionCmd.getBpmInstance().getId());
/*     */     
/* 155 */     this.LOG.debug("远程表单处理器执行持久化动作：{}", JSON.toJSON(remoteData));
/*     */     
/* 157 */     ParameterizedTypeReference<ResultMsg<FormHandlerResult>> typeReference = new ParameterizedTypeReference<ResultMsg<FormHandlerResult>>() {  }
/*     */       ;
/* 159 */     ResultMsg<FormHandlerResult> msg = (ResultMsg<FormHandlerResult>)RestTemplateUtil.post(url, remoteData, typeReference);
/*     */     
/* 161 */     this.LOG.debug("远程表单处理器 执行返回结果：{}", JSON.toJSON(msg));
/*     */     
/* 163 */     FormHandlerResult formHanderResult = (FormHandlerResult)ResultMsg.getSuccessResult(msg);
/*     */     
/* 165 */     String bizId = (formHanderResult != null) ? formHanderResult.getBizId() : "";
/* 166 */     if (StringUtil.isEmpty(bizKey) && StringUtil.isEmpty(bizId)) {
/* 167 */       this.LOG.warn("远程表单处理器执行后未返回业务主键！请确认是不是 没有需要保存的业务数据");
/*     */     }
/* 169 */     if (StringUtil.isNotEmpty(bizId)) {
/* 170 */       actionCmd.setBusinessKey(bizId);
/*     */     }
/*     */     
/* 173 */     if (formHanderResult != null && CollectionUtil.isNotEmpty(formHanderResult.getVariables())) {
/* 174 */       actionCmd.setActionVariables(formHanderResult.getVariables());
/*     */     }
/*     */   }
/*     */   
/*     */   public void httpFormHandler(String url, Map<String, Object> params) {
/* 179 */     this.LOG.debug("远程表单处理器执行持久化动作：{}", JSON.toJSON(params));
/* 180 */     ParameterizedTypeReference<ResultMsg<String>> typeReference = new ParameterizedTypeReference<ResultMsg<String>>() {  }
/*     */       ;
/* 182 */     ResultMsg<String> msg = (ResultMsg<String>)RestTemplateUtil.post(url, params, typeReference);
/* 183 */     this.LOG.debug("远程表单处理器 执行返回结果：{}", JSON.toJSON(msg));
/*     */   }
/*     */   
/*     */   public void asynHttpFormHandler(String url, Map<String, Object> params) {
/* 187 */     ParameterizedTypeReference<String> typeReference = new ParameterizedTypeReference<String>() {  }
/*     */       ;
/* 189 */     RestTemplateUtil.asynPost(url, params, typeReference);
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
/*     */   public void persistenceFutureNodeUserSetting(VariableScope variableScope, String nodeId) {
/* 201 */     DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 202 */     List<SysIdentity> nodeUserSetting = actionCmd.getBpmIdentity(nodeId);
/* 203 */     if (CollectionUtil.isEmpty(nodeUserSetting)) {
/* 204 */       throw new BusinessMessage("请指定下一环节候选人！");
/*     */     }
/* 206 */     variableScope.setVariable("futureUserSetting_" + nodeId, nodeUserSetting);
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
/*     */   public Set<SysIdentity> getVariablesUserSetting(VariableScope variableScope, String nodeId) {
/* 218 */     if (variableScope == null) return null; 
/* 219 */     List<SysIdentity> nodeUserSetting = (List<SysIdentity>)variableScope.getVariable("futureUserSetting_" + nodeId);
/*     */     
/* 221 */     if (CollectionUtil.isEmpty(nodeUserSetting)) {
/* 222 */       this.LOG.warn("未解析到流程变量人员！ 节点：futureUserSetting_{} ", nodeId);
/* 223 */       return Collections.EMPTY_SET;
/*     */     } 
/* 225 */     return new HashSet<>(nodeUserSetting);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<SysIdentity> getNodeHistoryUserSetting(String nodeId) {
/* 236 */     DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 237 */     BpmTask bpmTask = (BpmTask)actionCmd.getBpmTask();
/* 238 */     if (bpmTask == null) {
/* 239 */       return Collections.emptySet();
/*     */     }
/* 241 */     List<BpmTaskOpinion> opinions = this.opinionManager.getByInstAndNodeVersion(bpmTask.getInstId(), nodeId);
/* 242 */     Set<SysIdentity> identities = new HashSet<>();
/* 243 */     opinions.forEach(option -> {
/*     */           String assignInfo = option.getAssignInfo();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           if (StringUtil.isNotEmpty(assignInfo)) {
/*     */             String[] users = assignInfo.split(",");
/*     */ 
/*     */ 
/*     */             
/*     */             Arrays.<String>asList(users).forEach(());
/*     */           } 
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 260 */     return identities;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<SysIdentity> getTaskApproverByTaskStack(String nodeId) {
/* 271 */     DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 272 */     BpmTask bpmTask = (BpmTask)taskModel.getBpmTask();
/* 273 */     if (bpmTask == null) {
/* 274 */       return Collections.emptySet();
/*     */     }
/* 276 */     Set<SysIdentity> identities = new HashSet<>();
/* 277 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 278 */     defaultQueryFilter.addParamsFilter("taskId", bpmTask.getId());
/* 279 */     defaultQueryFilter.addParamsFilter("prior", "BACKWARD");
/* 280 */     defaultQueryFilter.addFilter("node_id_", nodeId, QueryOP.EQUAL);
/* 281 */     List<BpmTaskStack> stacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/* 282 */     if (CollectionUtil.isNotEmpty(stacks)) {
/* 283 */       BpmTaskStack stack = stacks.get(0);
/* 284 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(stack.getTaskId());
/* 285 */       if (bpmTaskOpinion != null) {
/* 286 */         identities.add(new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId()));
/*     */       }
/*     */     } 
/* 289 */     return identities;
/*     */   }
/*     */   
/*     */   public void clearSelectSysIdentityAndSetDefault() {
/* 293 */     DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 294 */     BpmTask bpmTask = (BpmTask)actionCmd.getBpmTask();
/* 295 */     actionCmd.setBpmIdentity(bpmTask.getNodeId(), new ArrayList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOriginDestinationAndSysIdentity(String defaultNode) {
/* 302 */     DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 303 */     if (StringUtils.isNotEmpty(defaultNode)) {
/* 304 */       actionCmd.setDestination(defaultNode);
/*     */       return;
/*     */     } 
/* 307 */     BpmTask bpmTask = (BpmTask)actionCmd.getBpmTask();
/* 308 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 309 */     if (StringUtils.equals(bpmTask.getTaskType(), TaskType.SIGN.getKey())) {
/* 310 */       defaultQueryFilter.addParamsFilter("taskId", bpmTask.getParentId());
/*     */     } else {
/* 312 */       defaultQueryFilter.addParamsFilter("taskId", bpmTask.getId());
/*     */     } 
/* 314 */     defaultQueryFilter.addParamsFilter("prior", "BACKWARD");
/* 315 */     defaultQueryFilter.addFieldSort("level", "asc");
/* 316 */     defaultQueryFilter.addFilter("level", Integer.valueOf(1), QueryOP.GREAT);
/* 317 */     defaultQueryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
/* 318 */     List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/* 319 */     if (CollectionUtil.isNotEmpty(bpmTaskStacks)) {
/* 320 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(((BpmTaskStack)bpmTaskStacks.get(0)).getTaskId());
/* 321 */       if (bpmTaskOpinion != null) {
/* 322 */         actionCmd.setDestination(bpmTaskOpinion.getTaskKey());
/* 323 */         List<SysIdentity> sysIdentities = new ArrayList<>();
/* 324 */         sysIdentities.add(new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId()));
/* 325 */         actionCmd.setBpmIdentity(bpmTaskOpinion.getTaskKey(), sysIdentities);
/*     */       } else {
/* 327 */         actionCmd.setDestination(defaultNode);
/*     */       } 
/*     */     } else {
/* 330 */       actionCmd.setDestination(defaultNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setOriginDestinationAndSysIdentity() {
/* 335 */     setOriginDestinationAndSysIdentity(null);
/*     */   }
/*     */   
/*     */   public void deleteExecution(BpmTask bpmTask, DefualtTaskActionCmd actionCmd) {
/* 339 */     DefualtTaskActionCmd defualtTaskActionCmd = new DefualtTaskActionCmd();
/* 340 */     CopyOptions copyOptions = new CopyOptions();
/* 341 */     copyOptions.setIgnoreError(true);
/* 342 */     BeanUtil.copyProperties(actionCmd, defualtTaskActionCmd, copyOptions);
/* 343 */     List<BpmTask> bpmTasks = this.taskMaanger.getByInstIdNodeId(bpmTask.getInstId(), bpmTask.getNodeId());
/* 344 */     if (CollectionUtil.isNotEmpty(bpmTasks)) {
/* 345 */       bpmTasks.forEach(task -> {
/*     */             defualtTaskActionCmd.setBpmTask((IBpmTask)task);
/*     */             defualtTaskActionCmd.setTaskId(task.getId());
/*     */             defualtTaskActionCmd.setActionName(ActionType.RECOVER.getKey());
/*     */             defualtTaskActionCmd.setOpinion("撤销重复任务");
/*     */             defualtTaskActionCmd.setExecutionStack((BpmExecutionStack)this.bpmTaskStackManager.getByTaskId(task.getId()));
/*     */             defualtTaskActionCmd.setDestinations(new String[0]);
/*     */             BpmContext.setActionModel((ActionCmd)defualtTaskActionCmd);
/*     */             this.actTaskService.completeTask(task.getId(), actionCmd.getActionVariables(), new String[0]);
/*     */             BpmContext.removeActionModel();
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExecution(String instId, String nodeId) {
/* 363 */     ActionCmd actionCmd = BpmContext.getActionModel();
/* 364 */     actionCmd.setActionName("agree");
/* 365 */     this.runtimeService.createNewExecution(instId, nodeId);
/* 366 */     BpmContext.setActionModel(actionCmd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrevTaskId() {
/* 376 */     DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 377 */     BpmTask bpmTask = (BpmTask)taskModel.getBpmTask();
/* 378 */     BpmExecutionStack bpmExecutionStack = taskModel.getExecutionStack();
/* 379 */     String nodeId = bpmTask.getNodeId();
/* 380 */     if (bpmExecutionStack != null && StringUtil.isNotEmpty(bpmExecutionStack.getNodeName())) {
/* 381 */       nodeId = bpmExecutionStack.getNodeName().split("-》")[0];
/*     */     }
/* 383 */     return nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrevNodeId() {
/* 392 */     DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 393 */     BpmExecutionStack bpmExecutionStack = taskModel.getExecutionStack();
/* 394 */     String taskId = bpmExecutionStack.getTaskId();
/* 395 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 396 */     defaultQueryFilter.addParamsFilter("taskId", taskId);
/* 397 */     defaultQueryFilter.addParamsFilter("prior", "BACKWARD");
/* 398 */     defaultQueryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
/* 399 */     defaultQueryFilter.addFilter("inst_id_", bpmExecutionStack.getInstId(), QueryOP.EQUAL);
/* 400 */     defaultQueryFilter.addFieldSort("id_", "desc");
/* 401 */     List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/* 402 */     if (CollectionUtil.isNotEmpty(bpmTaskStacks)) {
/* 403 */       return ((BpmTaskStack)bpmTaskStacks.get(0)).getNodeId();
/*     */     }
/* 405 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/script/BpmScriptUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */