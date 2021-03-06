/*     */ package com.dstz.bpm.service;
/*     */ 
/*     */ import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.api.service.BpmRuntimeService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */
import org.activiti.engine.RuntimeService;
/*     */ import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
/*     */ import org.activiti.engine.impl.cmd.CompleteTaskCmd;
/*     */ import org.activiti.engine.impl.cmd.SignalEventReceivedCmd;
/*     */ import org.activiti.engine.impl.context.Context;
/*     */ import org.activiti.engine.impl.interceptor.Command;
/*     */ import org.activiti.engine.impl.interceptor.CommandContext;
/*     */ import org.activiti.engine.impl.interceptor.CommandContextFactory;
/*     */ import org.activiti.engine.impl.persistence.entity.SignalEventSubscriptionEntity;
/*     */ import org.activiti.engine.runtime.Execution;
/*     */ import org.activiti.spring.SpringProcessEngineConfiguration;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class DefaultBpmRuntimeServiceImpl
/*     */   implements BpmRuntimeService
/*     */ {
/*     */   @Resource
/*     */   RuntimeService runtimeService;
/*     */   @Resource
/*     */   SpringProcessEngineConfiguration springProcessEngineConfiguration;
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */ BpmInstanceManager bpmInstanceMananger;
/*     */   @Autowired
/*     */ IBpmBusDataHandle bpmBusDataHandle;
/*     */   @Resource
/*     */   ActInstanceService actInstanceService;
/*     */   @Resource
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   
/*     */   public String signalEventReceived(String signalName, String taskId, String submitActionName) {
/*  61 */     BpmTask bpmTask = (BpmTask)this.bpmTaskManager.get(taskId);
/*  62 */     if (bpmTask == null) {
/*  63 */       throw new BusinessException("????????????????????????  taskId???" + taskId);
/*     */     }
/*  65 */     BpmInstance instance = (BpmInstance)this.bpmInstanceMananger.get(bpmTask.getInstId());
/*  66 */     prepareActionCmd(instance, submitActionName);
/*     */     
/*  68 */     Execution execution = (Execution)this.runtimeService.createExecutionQuery().executionId(bpmTask.getActExecutionId()).singleResult();
/*  69 */     if (execution == null) {
/*  70 */       throw new BusinessException("ACT??????????????????????????? task???" + bpmTask.getName());
/*     */     }
/*     */     
/*  73 */     this.runtimeService.signalEventReceived(signalName, bpmTask.getActExecutionId());
/*  74 */     BpmContext.removeActionModel();
/*     */     
/*  76 */     return "????????????????????????";
/*     */   }
/*     */ 
/*     */   
/*     */   public String signalEventReceived(String signalKey, String submitActionName) {
/*  81 */     this.runtimeService.signalEventReceived(signalKey);
/*  82 */     return "????????????????????????";
/*     */   }
/*     */ 
/*     */   
/*     */   @Transactional
/*     */   public String instanceScopeSignalEventReceived(String signalName, String instId, String submitActionName) {
/*  88 */     BpmInstance instance = (BpmInstance)this.bpmInstanceMananger.get(instId);
/*  89 */     if (instance == null) {
/*  90 */       throw new BusinessException("????????????????????????  instanceID???" + instId);
/*     */     }
/*  92 */     prepareActionCmd(instance, submitActionName);
/*  93 */     CommandContextFactory commandContextFactory = new CommandContextFactory();
/*  94 */     commandContextFactory.setProcessEngineConfiguration((ProcessEngineConfigurationImpl)this.springProcessEngineConfiguration);
/*  95 */     List<SignalEventSubscriptionEntity> subscriptionEntities = null;
/*     */     
/*  97 */     CommandContext commandContext = commandContextFactory.createCommandContext((Command)new SignalEventReceivedCmd(signalName, instance.getActInstId(), null, null));
/*  98 */     Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl)this.springProcessEngineConfiguration);
/*  99 */     Context.setCommandContext(commandContext);
/*     */ 
/*     */     
/* 102 */     subscriptionEntities = commandContext.getEventSubscriptionEntityManager().findSignalEventSubscriptionsByProcessInstanceAndEventName(instance.getActInstId(), signalName);
/* 103 */     for (SignalEventSubscriptionEntity signalEventSubscriptionEntity : subscriptionEntities) {
/* 104 */       signalEventSubscriptionEntity.eventReceived(null, false);
/*     */     }
/* 106 */     Context.removeCommandContext();
/* 107 */     Context.removeProcessEngineConfiguration();
/* 108 */     BpmContext.removeActionModel();
/* 109 */     return "????????????????????????";
/*     */   }
/*     */ 
/*     */   
/*     */   @Transactional
/*     */   public String instanceScopeSignalEventExecutionReceived(String signalName, String taskId, String submitActionName) {
/* 115 */     BpmTask bpmTask = (BpmTask)this.bpmTaskManager.get(taskId);
/* 116 */     if (bpmTask == null) {
/* 117 */       throw new BusinessException("????????????????????????  instanceID???" + taskId);
/*     */     }
/* 119 */     BpmInstance instance = (BpmInstance)this.bpmInstanceMananger.get(bpmTask.getInstId());
/* 120 */     prepareActionCmd(instance, submitActionName);
/* 121 */     CommandContextFactory commandContextFactory = new CommandContextFactory();
/* 122 */     commandContextFactory.setProcessEngineConfiguration((ProcessEngineConfigurationImpl)this.springProcessEngineConfiguration);
/* 123 */     List<SignalEventSubscriptionEntity> subscriptionEntities = null;
/* 124 */     CommandContext commandContext = commandContextFactory.createCommandContext((Command)new CompleteTaskCmd(bpmTask.getActExecutionId(), null));
/* 125 */     Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl)this.springProcessEngineConfiguration);
/* 126 */     Context.setCommandContext(commandContext);
/*     */ 
/*     */     
/* 129 */     subscriptionEntities = commandContext.getEventSubscriptionEntityManager().findSignalEventSubscriptionsByNameAndExecution(signalName, bpmTask.getActExecutionId());
/* 130 */     for (SignalEventSubscriptionEntity signalEventSubscriptionEntity : subscriptionEntities) {
/* 131 */       signalEventSubscriptionEntity.eventReceived(null, false);
/*     */     }
/* 133 */     Context.removeCommandContext();
/* 134 */     Context.removeProcessEngineConfiguration();
/* 135 */     BpmContext.removeActionModel();
/* 136 */     return "????????????????????????";
/*     */   }
/*     */ 
/*     */   
/*     */   public String instanceScopeStopAfterSignalEventReceived(String signalName, String instId, String submitActionName) {
/* 141 */     instanceScopeSignalEventReceived(signalName, instId, submitActionName);
/* 142 */     return "????????????????????????,??????execute";
/*     */   }
/*     */ 
/*     */   
/*     */   public String instanceScopeStopAfterSignalEventExecutionReceived(String signalName, String taskId, String submitActionName) {
/* 147 */     instanceScopeSignalEventExecutionReceived(signalName, taskId, submitActionName);
/* 148 */     return "????????????????????????,??????execute";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String createNewExecution(String processInstanceId, String targetNode) {
/* 154 */     BpmInstance instance = (BpmInstance)this.bpmInstanceMananger.get(processInstanceId);
/* 155 */     if (instance == null) {
/* 156 */       throw new BusinessException("????????????????????????  instanceID???" + processInstanceId);
/*     */     }
/* 158 */     prepareActionCmd(instance, null);
/*     */ 
/*     */     
/* 161 */     this.actInstanceService.createNewExecution(instance.getDefId(), instance.getActInstId(), targetNode);
/*     */     
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void prepareActionCmd(BpmInstance instance, String submitActionName) {
/* 173 */     DefualtTaskActionCmd taskComplateCmd = new DefualtTaskActionCmd();
/* 174 */     BaseActionCmd baseActionCmd = (BaseActionCmd)BpmContext.getActionModel();
/* 175 */     if (StringUtil.isEmpty(submitActionName) && StringUtil.isEmpty(baseActionCmd.getActionName())) {
/* 176 */       taskComplateCmd.setActionName(ActionType.TASKCANCELLED.getKey());
/* 177 */     } else if (StringUtil.isNotEmpty(submitActionName)) {
/* 178 */       taskComplateCmd.setActionName(ActionType.fromKey(submitActionName).getKey());
/*     */     } else {
/* 180 */       taskComplateCmd.setActionName(baseActionCmd.getActionName());
/*     */     } 
/*     */     
/* 183 */     taskComplateCmd.setBpmInstance((IBpmInstance)instance);
/* 184 */     taskComplateCmd.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(instance.getDefId()));
/* 185 */     taskComplateCmd.setBpmIdentities(baseActionCmd.getBpmIdentities());
/* 186 */     taskComplateCmd.setDynamicBpmIdentity(baseActionCmd.getDynamicBpmIdentity());
/* 187 */     taskComplateCmd.setDynamicSubmitTaskName(baseActionCmd.getDynamicSubmitTaskName());
/* 188 */     taskComplateCmd.setStartAppointDestinations(baseActionCmd.getStartAppointDestinations());
/* 189 */     taskComplateCmd.setDoActionName(baseActionCmd.getDoActionName());
/* 190 */     taskComplateCmd.setApproveOrgId(baseActionCmd.getApproveOrgId());
/* 191 */     if (CollectionUtil.isNotEmpty(baseActionCmd.getBizDataMap())) {
/* 192 */       taskComplateCmd.setBizDataMap(baseActionCmd.getBizDataMap());
/*     */     } else {
/* 194 */       taskComplateCmd.setBizDataMap(this.bpmBusDataHandle.getInstanceBusData(instance.getId(), null));
/*     */     } 
/*     */ 
/*     */     
/* 198 */     if (StringUtils.isEmpty(baseActionCmd.getOpinion())) {
/* 199 */       taskComplateCmd.setOpinion("????????????");
/*     */     } else {
/* 201 */       taskComplateCmd.setOpinion(baseActionCmd.getOpinion());
/*     */     } 
/*     */ 
/*     */     
/* 205 */     BpmContext.setActionModel((ActionCmd)taskComplateCmd);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/service/DefaultBpmRuntimeServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */