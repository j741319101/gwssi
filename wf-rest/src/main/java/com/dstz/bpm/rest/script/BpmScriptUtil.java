/*     */ package com.dstz.bpm.rest.script;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.RestTemplateUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.act.service.ActTaskService;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.constant.LogicType;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.api.model.remote.BpmRemoteBusinessData;
/*     */ import com.dstz.bpm.api.model.remote.FormHandlerResult;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.plugin.usercalc.util.AtomicUserCalcPreview;
/*     */ import com.dstz.org.api.constant.GroupTypeConstant;
/*     */ import com.dstz.org.api.model.IGroup;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.GroupService;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.groovy.IScript;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
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
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class BpmScriptUtil
/*     */   implements IScript
/*     */ {
/*  58 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   protected ActTaskService actTaskService;
/*     */   
/*     */   @Resource
/*     */   UserService userService;
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
/*     */   
/*     */   public Map<String, Object> getVariables(String taskId) {
/*  81 */     return this.actTaskService.getVariables(taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getVariableByTaskId(String taskId, String variableName) {
/*  86 */     return this.actTaskService.getVariable(taskId, variableName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getVariableLocalByTaskId(String taskId, String variableName) {
/*  91 */     return this.actTaskService.getVariableLocal(taskId, variableName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endProcessByInstanceId(String instanceId, String opinion) {
/* 101 */     if (ContextUtil.getCurrentUser() == null) {
/* 102 */       ContextUtil.setCurrentUserByAccount("admin");
/*     */     }
/* 104 */     List<BpmTask> task = this.taskMaanger.getByInstId(instanceId);
/* 105 */     if (CollectionUtil.isEmpty(task)) {
/* 106 */       throw new BusinessException("当前流程实例下没有找到任务，请检查! instanceId:" + instanceId);
/*     */     }
/* 108 */     endProcessByTaskId(((BpmTask)task.get(0)).getId(), opinion);
/*     */   }
/*     */   
/*     */   public void endProcessByTaskId(String taskId, String opinion) {
/* 112 */     if (ContextUtil.getCurrentUser() == null) {
/* 113 */       ContextUtil.setCurrentUserByAccount("admin");
/*     */     }
/*     */     
/* 116 */     DefualtTaskActionCmd taskCmd = new DefualtTaskActionCmd();
/* 117 */     taskCmd.setTaskId(taskId);
/* 118 */     taskCmd.setActionName(ActionType.MANUALEND.getKey());
/* 119 */     taskCmd.setOpinion(opinion);
/* 120 */     taskCmd.executeCmd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void complateTaskById(String taskId, String opinion) {
/* 130 */     if (ContextUtil.getCurrentUser() == null) {
/* 131 */       ContextUtil.setCurrentUserByAccount("admin");
/*     */     }
/*     */     
/* 134 */     DefualtTaskActionCmd taskCmd = new DefualtTaskActionCmd();
/* 135 */     taskCmd.setTaskId(taskId);
/* 136 */     taskCmd.setActionName(ActionType.AGREE.getKey());
/* 137 */     taskCmd.setOpinion(opinion);
/* 138 */     taskCmd.executeCmd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void httpFormHandler(String url) {
/* 146 */     ActionCmd actionCmd = BpmContext.getActionModel();
/*     */     
/* 148 */     String bizKey = actionCmd.getBusinessKey();
/*     */     
/* 150 */     JSONObject object = actionCmd.getBusData();
/* 151 */     BpmRemoteBusinessData<JSONObject> remoteData = new BpmRemoteBusinessData();
/* 152 */     remoteData.setData(object);
/* 153 */     remoteData.setFlowKey(actionCmd.getBpmInstance().getDefKey());
/* 154 */     remoteData.setInstanceId(actionCmd.getBpmInstance().getId());
/*     */     
/* 156 */     this.LOG.debug("远程表单处理器执行持久化动作：{}", JSON.toJSON(remoteData));
/*     */     
/* 158 */     ParameterizedTypeReference<ResultMsg<FormHandlerResult>> typeReference = new ParameterizedTypeReference<ResultMsg<FormHandlerResult>>() {  }
/*     */       ;
/* 160 */     ResultMsg<FormHandlerResult> msg = (ResultMsg<FormHandlerResult>)RestTemplateUtil.post(url, remoteData, typeReference);
/*     */     
/* 162 */     this.LOG.debug("远程表单处理器 执行返回结果：{}", JSON.toJSON(msg));
/*     */     
/* 164 */     FormHandlerResult formHanderResult = (FormHandlerResult)ResultMsg.getSuccessResult(msg);
/*     */     
/* 166 */     String bizId = (formHanderResult != null) ? formHanderResult.getBizId() : "";
/* 167 */     if (StringUtil.isEmpty(bizKey) && StringUtil.isEmpty(bizId)) {
/* 168 */       this.LOG.warn("远程表单处理器执行后未返回业务主键！请确认是不是 没有需要保存的业务数据");
/*     */     }
/* 170 */     if (StringUtil.isNotEmpty(bizId)) {
/* 171 */       actionCmd.setBusinessKey(bizId);
/*     */     }
/*     */     
/* 174 */     if (formHanderResult != null && CollectionUtil.isNotEmpty(formHanderResult.getVariables())) {
/* 175 */       actionCmd.setActionVariables(formHanderResult.getVariables());
/*     */     }
/*     */   }
/*     */   
/*     */   public void httpFormHandler(String url, Map<String, Object> params) {
/* 180 */     this.LOG.debug("远程表单处理器执行持久化动作：{}", JSON.toJSON(params));
/* 181 */     ParameterizedTypeReference<ResultMsg<String>> typeReference = new ParameterizedTypeReference<ResultMsg<String>>() {  }
/*     */       ;
/* 183 */     ResultMsg<String> msg = (ResultMsg<String>)RestTemplateUtil.post(url, params, typeReference);
/* 184 */     this.LOG.debug("远程表单处理器 执行返回结果：{}", JSON.toJSON(msg));
/*     */   }
/*     */   
/*     */   public void asynHttpFormHandler(String url, Map<String, Object> params) {
/* 188 */     ParameterizedTypeReference<String> typeReference = new ParameterizedTypeReference<String>() {  }
/*     */       ;
/* 190 */     RestTemplateUtil.asynPost(url, params, typeReference);
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
/* 202 */     DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 203 */     List<SysIdentity> nodeUserSetting = actionCmd.getBpmIdentity(nodeId);
/* 204 */     if (CollectionUtil.isEmpty(nodeUserSetting)) {
/* 205 */       throw new BusinessMessage("请指定下一环节候选人！");
/*     */     }
/* 207 */     variableScope.setVariable("futureUserSetting_" + nodeId, nodeUserSetting);
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
/* 219 */     if (variableScope == null) return null; 
/* 220 */     List<SysIdentity> nodeUserSetting = (List<SysIdentity>)variableScope.getVariable("futureUserSetting_" + nodeId);
/*     */     
/* 222 */     if (CollectionUtil.isEmpty(nodeUserSetting)) {
/* 223 */       this.LOG.warn("未解析到流程变量人员！ 节点：futureUserSetting_{} ", nodeId);
/* 224 */       return Collections.EMPTY_SET;
/*     */     } 
/* 226 */     return new HashSet<>(nodeUserSetting);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<SysIdentity> getSisByPreNodeGroup() {
/* 235 */     DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 236 */     BpmTask bpmTask = (BpmTask)taskModel.getBpmTask();
/* 237 */     Set<SysIdentity> identities = new HashSet<>();
/* 238 */     if (bpmTask != null) {
/* 239 */       BpmExecutionStack bpmExecutionStack = taskModel.getExecutionStack();
/*     */       
/* 241 */       String nodeId = bpmTask.getNodeId();
/* 242 */       if (bpmExecutionStack != null && StringUtil.isNotEmpty(bpmExecutionStack.getNodeName())) {
/* 243 */         nodeId = bpmExecutionStack.getNodeName().split("-》")[0];
/*     */       }
/* 245 */       List<BpmTaskOpinion> taskOpinions = this.opinionManager.getByInstAndNode(bpmTask.getInstId(), nodeId);
/*     */       
/* 247 */       List<IGroup> existGroup = new ArrayList<>();
/* 248 */       taskOpinions.forEach(option -> {
/*     */             if (StringUtil.isNotEmpty(option.getApprover())) {
/*     */               List<IGroup> groups = this.groupService.getGroupsByGroupTypeUserId(GroupTypeConstant.ORG.key(), option.getApprover());
/*     */ 
/*     */               
/*     */               if (groups.size() > 0) {
/*     */                 if (CollectionUtil.contains(existGroup, groups.get(0))) {
/*     */                   return;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 String groupId = ((IGroup)groups.get(0)).getGroupId();
/*     */                 
/*     */                 List<IUser> users = this.userService.getUserListByGroup("org", groupId);
/*     */                 
/*     */                 if (users != null) {
/*     */                   users.forEach(());
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           });
/*     */     } 
/*     */     
/* 271 */     String groupId = ContextUtil.getCurrentGroup().getGroupId();
/* 272 */     List<IUser> users = this.userService.getUserListByGroup("org", groupId);
/* 273 */     if (users != null) {
/* 274 */       users.forEach(user -> {
/*     */             DefaultIdentity identity = new DefaultIdentity();
/*     */             identity.setId(user.getUserId());
/*     */             identity.setName(user.getFullname());
/*     */             identity.setType("user");
/*     */             identity.setSn(Integer.valueOf((user.getSn() == null) ? 0 : user.getSn().intValue()));
/*     */             identities.add(identity);
/*     */           });
/*     */     }
/* 283 */     return identities;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<SysIdentity> getNodeHistoryUserSetting(String nodeId) {
/* 293 */     DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 294 */     BpmTask bpmTask = (BpmTask)actionCmd.getBpmTask();
/* 295 */     List<BpmTaskOpinion> opinions = this.opinionManager.getByInstAndNodeVersion(bpmTask.getInstId(), nodeId);
/* 296 */     Set<SysIdentity> identities = new HashSet<>();
/* 297 */     opinions.forEach(option -> {
/*     */           String assignInfo = option.getAssignInfo();
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
/* 313 */     return identities;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<SysIdentity> getTaskApproverByTaskStack(String nodeId) {
/* 323 */     DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 324 */     BpmTask bpmTask = (BpmTask)taskModel.getBpmTask();
/* 325 */     if (bpmTask == null) {
/* 326 */       return Collections.emptySet();
/*     */     }
/* 328 */     Set<SysIdentity> identities = new HashSet<>();
/* 329 */     List<BpmTaskStack> stacks = this.bpmTaskStackManager.getTaskStackByIteration(bpmTask.getTaskId(), bpmTask.getNodeId(), nodeId);
/* 330 */     if (CollectionUtil.isNotEmpty(stacks)) {
/* 331 */       BpmTaskStack stack = stacks.get(0);
/* 332 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(stack.getTaskId());
/* 333 */       if (bpmTaskOpinion != null) {
/* 334 */         IUser user = this.userService.getUserById(bpmTaskOpinion.getApprover());
/* 335 */         if (user != null) {
/* 336 */           identities.add(new DefaultIdentity(user));
/*     */         }
/*     */       } 
/*     */     } 
/* 340 */     return identities;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrevTaskId() {
/* 349 */     DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 350 */     BpmTask bpmTask = (BpmTask)taskModel.getBpmTask();
/* 351 */     BpmExecutionStack bpmExecutionStack = taskModel.getExecutionStack();
/* 352 */     String nodeId = bpmTask.getNodeId();
/* 353 */     if (bpmExecutionStack != null && StringUtil.isNotEmpty(bpmExecutionStack.getNodeName())) {
/* 354 */       nodeId = bpmExecutionStack.getNodeName().split("-》")[0];
/*     */     }
/* 356 */     return nodeId;
/*     */   }
/*     */   
/*     */   public void clearSelectSysIdentityAndSetDefault() {
/* 360 */     DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 361 */     BpmTask bpmTask = (BpmTask)actionCmd.getBpmTask();
/* 362 */     actionCmd.setBpmIdentity(bpmTask.getNodeId(), new ArrayList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void joinSysIdentity(String nodeId, String identityId, String type, String logicType, BaseActionCmd actionCmd) {
/* 371 */     List<SysIdentity> sysIdentities = actionCmd.getBpmIdentity(nodeId);
/* 372 */     if (CollectionUtil.isEmpty(sysIdentities)) {
/*     */       return;
/*     */     }
/* 375 */     List<Object[]> sysIdentitieAndLogic = new ArrayList();
/* 376 */     sysIdentities.forEach(identity -> sysIdentitieAndLogic.add(new Object[] { identity, LogicType.OR.getKey() }));
/*     */     
/* 378 */     if (StringUtils.equals(type, "user")) {
/* 379 */       IUser user = this.userService.getUserByAccount(identityId);
/* 380 */       if (user != null) {
/* 381 */         sysIdentitieAndLogic.add(new Object[] { new DefaultIdentity(user), logicType });
/*     */       } else {
/* 383 */         throw new BusinessException(nodeId + "：前置脚本候选人丢失");
/*     */       } 
/*     */     } else {
/* 386 */       IGroup iGroup = this.groupService.getByCode(type, identityId);
/* 387 */       if (iGroup != null) {
/* 388 */         sysIdentitieAndLogic.add(new Object[] { new DefaultIdentity(iGroup.getGroupId(), iGroup.getGroupName(), type), logicType });
/*     */       } else {
/* 390 */         throw new BusinessException(nodeId + "：前置脚本组织丢失");
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 396 */     sysIdentities = AtomicUserCalcPreview.calcUserPreview(sysIdentitieAndLogic);
/* 397 */     if (CollectionUtil.isEmpty(sysIdentities)) {
/* 398 */       throw new BusinessException("候选人为空");
/*     */     }
/* 400 */     actionCmd.setBpmIdentity(nodeId, sysIdentities);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<DynamicTaskIdentitys> testDynamicTask() {
/* 410 */     List<DynamicTaskIdentitys> dynamicTask = new ArrayList<>();
/*     */ 
/*     */     
/* 413 */     List<SysIdentity> identitys = new ArrayList<>();
/* 414 */     identitys.add(new DefaultIdentity("1", "管理员", "user"));
/*     */     
/* 416 */     dynamicTask.add(new DynamicTaskIdentitys("动态任务①", identitys));
/*     */ 
/*     */ 
/*     */     
/* 420 */     List<SysIdentity> identitys2 = new ArrayList<>();
/* 421 */     identitys2.add(new DefaultIdentity("1", "管理员2", "user"));
/*     */     
/* 423 */     dynamicTask.add(new DynamicTaskIdentitys("动态任务②", identitys2));
/*     */ 
/*     */     
/* 426 */     return dynamicTask;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/rest/script/BpmScriptUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */