package com.dstz.bpm.wf.rest.script;

import com.dstz.bpm.act.service.ActTaskService;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.inst.BpmExecutionStack;
import com.dstz.bpm.api.model.remote.BpmRemoteBusinessData;
import com.dstz.bpm.api.model.remote.FormHandlerResult;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.api.service.BpmRuntimeService;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.RestTemplateUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.org.api.service.GroupService;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.groovy.IScript;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.activiti.engine.delegate.VariableScope;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public class BpmScriptUtil implements IScript {
   protected Logger LOG = LoggerFactory.getLogger(this.getClass());
   @Resource
   protected ActTaskService actTaskService;
   @Resource
   private UserService userService;
   @Resource
   BpmTaskOpinionManager opinionManager;
   @Resource
   GroupService groupService;
   @Resource
   private BpmTaskManager taskMaanger;
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource
   private BpmProcessDefService bpmProcessDefService;
   @Resource(
      name = "defaultBpmRuntimeServiceImpl"
   )
   private BpmRuntimeService runtimeService;

   public Map<String, Object> getVariables(String taskId) {
      return this.actTaskService.getVariables(taskId);
   }

   public Object getVariableByTaskId(String taskId, String variableName) {
      return this.actTaskService.getVariable(taskId, variableName);
   }

   public Object getVariableLocalByTaskId(String taskId, String variableName) {
      return this.actTaskService.getVariableLocal(taskId, variableName);
   }

   public void endProcessByInstanceId(String instanceId, String opinion) {
      if (ContextUtil.getCurrentUser() == null) {
         ContextUtil.setCurrentUserByAccount("admin");
      }

      List<BpmTask> task = this.taskMaanger.getByInstId(instanceId);
      if (CollectionUtil.isEmpty(task)) {
         throw new BusinessException("当前流程实例下没有找到任务，请检查! instanceId:" + instanceId);
      } else {
         this.endProcessByTaskId(((BpmTask)task.get(0)).getId(), opinion);
      }
   }

   public void endProcessByTaskId(String taskId, String opinion) {
      if (ContextUtil.getCurrentUser() == null) {
         ContextUtil.setCurrentUserByAccount("admin");
      }

      DefualtTaskActionCmd taskCmd = new DefualtTaskActionCmd();
      taskCmd.setTaskId(taskId);
      taskCmd.setActionName(ActionType.MANUALEND.getKey());
      taskCmd.setOpinion(opinion);
      taskCmd.executeCmd();
   }

   public void complateTaskById(String taskId, String opinion) {
      if (ContextUtil.getCurrentUser() == null) {
         ContextUtil.setCurrentUserByAccount("admin");
      }

      DefualtTaskActionCmd taskCmd = new DefualtTaskActionCmd();
      taskCmd.setTaskId(taskId);
      taskCmd.setActionName(ActionType.AGREE.getKey());
      taskCmd.setOpinion(opinion);
      taskCmd.executeCmd();
   }

   public void httpFormHandler(String url) {
      ActionCmd actionCmd = BpmContext.getActionModel();
      String bizKey = actionCmd.getBusinessKey();
      JSONObject object = actionCmd.getBusData();
      BpmRemoteBusinessData<JSONObject> remoteData = new BpmRemoteBusinessData();
      remoteData.setData(object);
      remoteData.setFlowKey(actionCmd.getBpmInstance().getDefKey());
      remoteData.setInstanceId(actionCmd.getBpmInstance().getId());
      this.LOG.debug("远程表单处理器执行持久化动作：{}", JSON.toJSON(remoteData));
      ParameterizedTypeReference<ResultMsg<FormHandlerResult>> typeReference = new ParameterizedTypeReference<ResultMsg<FormHandlerResult>>() {
      };
      ResultMsg<FormHandlerResult> msg = (ResultMsg)RestTemplateUtil.post(url, remoteData, typeReference);
      this.LOG.debug("远程表单处理器 执行返回结果：{}", JSON.toJSON(msg));
      FormHandlerResult formHanderResult = (FormHandlerResult)ResultMsg.getSuccessResult(msg);
      String bizId = formHanderResult != null ? formHanderResult.getBizId() : "";
      if (StringUtil.isEmpty(bizKey) && StringUtil.isEmpty(bizId)) {
         this.LOG.warn("远程表单处理器执行后未返回业务主键！请确认是不是 没有需要保存的业务数据");
      }

      if (StringUtil.isNotEmpty(bizId)) {
         actionCmd.setBusinessKey(bizId);
      }

      if (formHanderResult != null && CollectionUtil.isNotEmpty(formHanderResult.getVariables())) {
         actionCmd.setActionVariables(formHanderResult.getVariables());
      }

   }

   public void httpFormHandler(String url, Map<String, Object> params) {
      this.LOG.debug("远程表单处理器执行持久化动作：{}", JSON.toJSON(params));
      ParameterizedTypeReference<ResultMsg<String>> typeReference = new ParameterizedTypeReference<ResultMsg<String>>() {
      };
      ResultMsg<String> msg = (ResultMsg)RestTemplateUtil.post(url, params, typeReference);
      this.LOG.debug("远程表单处理器 执行返回结果：{}", JSON.toJSON(msg));
   }

   public void asynHttpFormHandler(String url, Map<String, Object> params) {
      ParameterizedTypeReference<String> typeReference = new ParameterizedTypeReference<String>() {
      };
      RestTemplateUtil.asynPost(url, params, typeReference);
   }

   public void persistenceFutureNodeUserSetting(VariableScope variableScope, String nodeId) {
      DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
      List<SysIdentity> nodeUserSetting = actionCmd.getBpmIdentity(nodeId);
      if (CollectionUtil.isEmpty(nodeUserSetting)) {
         throw new BusinessMessage("请指定下一环节候选人！");
      } else {
         variableScope.setVariable("futureUserSetting_" + nodeId, nodeUserSetting);
      }
   }

   public Set<SysIdentity> getVariablesUserSetting(VariableScope variableScope, String nodeId) {
      if (variableScope == null) {
         return null;
      } else {
         List<SysIdentity> nodeUserSetting = (List)variableScope.getVariable("futureUserSetting_" + nodeId);
         if (CollectionUtil.isEmpty(nodeUserSetting)) {
            this.LOG.warn("未解析到流程变量人员！ 节点：futureUserSetting_{} ", nodeId);
            return Collections.EMPTY_SET;
         } else {
            return new HashSet(nodeUserSetting);
         }
      }
   }

   public Set<SysIdentity> getNodeHistoryUserSetting(String nodeId) {
      DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
      BpmTask bpmTask = (BpmTask)actionCmd.getBpmTask();
      if (bpmTask == null) {
         return Collections.emptySet();
      } else {
         List<BpmTaskOpinion> opinions = this.opinionManager.getByInstAndNodeVersion(bpmTask.getInstId(), nodeId);
         Set<SysIdentity> identities = new HashSet();
         opinions.forEach((option) -> {
            String assignInfo = option.getAssignInfo();
            if (StringUtil.isNotEmpty(assignInfo)) {
               String[] users = assignInfo.split(",");
               Arrays.asList(users).forEach((user) -> {
                  if (StringUtils.startsWith(user, "user")) {
                     String[] info = user.split("-");
                     DefaultIdentity identity = new DefaultIdentity();
                     identity.setId(info[2]);
                     identity.setName(info[1]);
                     identity.setType("user");
                     identity.setOrgId(info[3]);
                     identities.add(identity);
                  }

               });
            }

         });
         return identities;
      }
   }

   public Set<SysIdentity> getTaskApproverByTaskStack(String nodeId) {
      DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
      BpmTask bpmTask = (BpmTask)taskModel.getBpmTask();
      if (bpmTask == null) {
         return Collections.emptySet();
      } else {
         Set<SysIdentity> identities = new HashSet();
         QueryFilter queryFilter = new DefaultQueryFilter(true);
         queryFilter.addParamsFilter("taskId", bpmTask.getId());
         queryFilter.addParamsFilter("prior", "BACKWARD");
         queryFilter.addFilter("node_id_", nodeId, QueryOP.EQUAL);
         List<BpmTaskStack> stacks = this.bpmTaskStackManager.getTaskStackByIteration(queryFilter);
         if (CollectionUtil.isNotEmpty(stacks)) {
            BpmTaskStack stack = (BpmTaskStack)stacks.get(0);
            BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(stack.getTaskId());
            if (bpmTaskOpinion != null) {
               identities.add(new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId()));
            }
         }

         return identities;
      }
   }

   public void clearSelectSysIdentityAndSetDefault() {
      DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
      BpmTask bpmTask = (BpmTask)actionCmd.getBpmTask();
      actionCmd.setBpmIdentity(bpmTask.getNodeId(), new ArrayList());
   }

   public void setOriginDestinationAndSysIdentity(String defaultNode) {
      DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
      if (StringUtils.isNotEmpty(defaultNode)) {
         actionCmd.setDestination(defaultNode);
      } else {
         BpmTask bpmTask = (BpmTask)actionCmd.getBpmTask();
         QueryFilter queryFilter = new DefaultQueryFilter(true);
         if (StringUtils.equals(bpmTask.getTaskType(), TaskType.SIGN.getKey())) {
            queryFilter.addParamsFilter("taskId", bpmTask.getParentId());
         } else {
            queryFilter.addParamsFilter("taskId", bpmTask.getId());
         }

         queryFilter.addParamsFilter("prior", "BACKWARD");
         queryFilter.addFieldSort("level", "asc");
         queryFilter.addFilter("level", 1, QueryOP.GREAT);
         queryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
         List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration(queryFilter);
         if (CollectionUtil.isNotEmpty(bpmTaskStacks)) {
            BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(((BpmTaskStack)bpmTaskStacks.get(0)).getTaskId());
            if (bpmTaskOpinion != null) {
               actionCmd.setDestination(bpmTaskOpinion.getTaskKey());
               List<SysIdentity> sysIdentities = new ArrayList();
               sysIdentities.add(new DefaultIdentity(bpmTaskOpinion.getApprover(), bpmTaskOpinion.getApproverName(), "user", bpmTaskOpinion.getTaskOrgId()));
               actionCmd.setBpmIdentity(bpmTaskOpinion.getTaskKey(), sysIdentities);
            } else {
               actionCmd.setDestination(defaultNode);
            }
         } else {
            actionCmd.setDestination(defaultNode);
         }

      }
   }

   public void setOriginDestinationAndSysIdentity() {
      this.setOriginDestinationAndSysIdentity((String)null);
   }

   public void deleteExecution(BpmTask bpmTask, DefualtTaskActionCmd actionCmd) {
      DefualtTaskActionCmd defualtTaskActionCmd = new DefualtTaskActionCmd();
      CopyOptions copyOptions = new CopyOptions();
      copyOptions.setIgnoreError(true);
      BeanUtil.copyProperties(actionCmd, defualtTaskActionCmd, copyOptions);
      List<BpmTask> bpmTasks = this.taskMaanger.getByInstIdNodeId(bpmTask.getInstId(), bpmTask.getNodeId());
      if (CollectionUtil.isNotEmpty(bpmTasks)) {
         bpmTasks.forEach((task) -> {
            defualtTaskActionCmd.setBpmTask(task);
            defualtTaskActionCmd.setTaskId(task.getId());
            defualtTaskActionCmd.setActionName(ActionType.RECOVER.getKey());
            defualtTaskActionCmd.setOpinion("撤销重复任务");
            defualtTaskActionCmd.setExecutionStack(this.bpmTaskStackManager.getByTaskId(task.getId()));
            defualtTaskActionCmd.setDestinations(new String[0]);
            BpmContext.setActionModel(defualtTaskActionCmd);
            this.actTaskService.completeTask(task.getId(), actionCmd.getActionVariables(), new String[0]);
            BpmContext.removeActionModel();
         });
      }

   }

   public void addExecution(String instId, String nodeId) {
      ActionCmd actionCmd = BpmContext.getActionModel();
      actionCmd.setActionName("agree");
      this.runtimeService.createNewExecution(instId, nodeId);
      BpmContext.setActionModel(actionCmd);
   }

   public String getPrevTaskId() {
      DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
      BpmTask bpmTask = (BpmTask)taskModel.getBpmTask();
      BpmExecutionStack bpmExecutionStack = taskModel.getExecutionStack();
      String nodeId = bpmTask.getNodeId();
      if (bpmExecutionStack != null && StringUtil.isNotEmpty(bpmExecutionStack.getNodeName())) {
         nodeId = bpmExecutionStack.getNodeName().split("-》")[0];
      }

      return nodeId;
   }

   public String getPrevNodeId() {
      DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd)BpmContext.getActionModel();
      BpmExecutionStack bpmExecutionStack = taskModel.getExecutionStack();
      String taskId = bpmExecutionStack.getTaskId();
      QueryFilter queryFilter = new DefaultQueryFilter(true);
      queryFilter.addParamsFilter("taskId", taskId);
      queryFilter.addParamsFilter("prior", "BACKWARD");
      queryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
      queryFilter.addFilter("inst_id_", bpmExecutionStack.getInstId(), QueryOP.EQUAL);
      queryFilter.addFieldSort("id_", "desc");
      List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration(queryFilter);
      return CollectionUtil.isNotEmpty(bpmTaskStacks) ? ((BpmTaskStack)bpmTaskStacks.get(0)).getNodeId() : null;
   }
}
