/*     */ package com.dstz.bpm.engine.listener;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.constant.ScriptType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import java.util.Date;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class InstanceEndEventListener
/*     */   extends AbstractInstanceListener
/*     */ {
/*     */   @Resource
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   BpmTaskStackManager bpmTaskStackManager;
/*     */   
/*     */   public EventType getBeforeTriggerEventType() {
/*  42 */     return EventType.END_EVENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public EventType getAfterTriggerEventType() {
/*  47 */     return EventType.END_POST_EVENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforePluginExecute(InstanceActionCmd instanceActionModel) {
/*  52 */     this.LOG.debug("流程实例【{}】,ID【{}】开始触发终止事件", instanceActionModel.getBpmInstance().getSubject(), instanceActionModel.getBpmInstance().getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void triggerExecute(InstanceActionCmd instanceActionModel) {
/*  57 */     this.bpmTaskOpinionManager.createOpinionByInstance(instanceActionModel, false);
/*     */ 
/*     */     
/*  60 */     BpmInstance instance = (BpmInstance)instanceActionModel.getBpmInstance();
/*  61 */     instance.setStatus(InstanceStatus.STATUS_END.getKey());
/*  62 */     instance.setEndTime(new Date());
/*  63 */     instance.setDuration(Long.valueOf(instance.getEndTime().getTime() - instance.getCreateTime().getTime()));
/*  64 */     this.bpmInstanceManager.update(instance);
/*     */     
/*  66 */     createExecutionStack(instanceActionModel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPluginExecute(InstanceActionCmd instanceActionModel) {
/*  74 */     this.LOG.debug("流程实例【{}】,ID【{}】已经终止", instanceActionModel.getBpmInstance().getSubject(), instanceActionModel.getBpmInstance().getId());
/*     */   }
/*     */ 
/*     */   
/*     */   protected ScriptType getScriptType() {
/*  79 */     return ScriptType.END;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InstanceActionCmd getInstanceActionModel(ExecutionEntity executionEntity) {
/*  84 */     BaseActionCmd actionModle = (BaseActionCmd)BpmContext.getActionModel();
/*     */     
/*  86 */     DefaultInstanceActionCmd instanceModel = new DefaultInstanceActionCmd();
/*  87 */     instanceModel.setBpmInstance(actionModle.getBpmInstance());
/*  88 */     instanceModel.setBpmDefinition(actionModle.getBpmDefinition());
/*  89 */     instanceModel.setBizDataMap(actionModle.getBizDataMap());
/*  90 */     instanceModel.setBusinessKey(actionModle.getBusinessKey());
/*  91 */     instanceModel.setExecutionStack(actionModle.getExecutionStack());
/*  92 */     instanceModel.setActionName(ActionType.END.getKey());
/*  93 */     instanceModel.setExecutionEntity(executionEntity);
/*  94 */     instanceModel.setApproveOrgId(actionModle.getApproveOrgId());
/*  95 */     return (InstanceActionCmd)instanceModel;
/*     */   }
/*     */   
/*     */   private void createExecutionStack(InstanceActionCmd instanceActionModel) {
/*  99 */     DefaultInstanceActionCmd actionCmd = (DefaultInstanceActionCmd)instanceActionModel;
/* 100 */     ExecutionEntity entity = actionCmd.getExecutionEntity();
/*     */     
/* 102 */     BpmTaskStack endFlowStack = new BpmTaskStack();
/* 103 */     String id = IdUtil.getSuid();
/* 104 */     endFlowStack.setId(id);
/* 105 */     endFlowStack.setNodeId(entity.getActivityId());
/*     */     
/* 107 */     endFlowStack.setNodeName(entity.getCurrentActivityName());
/* 108 */     endFlowStack.setTaskId("0");
/*     */     
/* 110 */     endFlowStack.setStartTime(new Date());
/* 111 */     endFlowStack.setEndTime(new Date());
/* 112 */     endFlowStack.setInstId(actionCmd.getInstanceId());
/* 113 */     endFlowStack.setNodeType("endNoneEvent");
/* 114 */     endFlowStack.setActionName("end");
/*     */     
/* 116 */     BpmExecutionStack parentStack = actionCmd.getExecutionStack();
/* 117 */     if (parentStack != null) {
/* 118 */       endFlowStack.setParentId(parentStack.getId());
/*     */     }
/*     */     
/* 121 */     this.bpmTaskStackManager.create(endFlowStack);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/InstanceEndEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */