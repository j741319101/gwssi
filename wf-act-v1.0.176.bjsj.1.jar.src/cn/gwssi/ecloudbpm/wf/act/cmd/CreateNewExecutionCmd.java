/*     */ package com.dstz.bpm.act.cmd;
/*     */ 
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.activiti.engine.impl.db.DbSqlSession;
/*     */ import org.activiti.engine.impl.db.PersistentObject;
/*     */ import org.activiti.engine.impl.interceptor.Command;
/*     */ import org.activiti.engine.impl.interceptor.CommandContext;
/*     */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ import org.activiti.engine.impl.persistence.entity.TaskEntity;
/*     */ import org.activiti.engine.impl.pvm.PvmTransition;
/*     */ import org.activiti.engine.impl.pvm.process.ActivityImpl;
/*     */ import org.activiti.engine.impl.pvm.process.TransitionImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreateNewExecutionCmd
/*     */   implements Command<Object>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected String actInstance;
/*     */   protected String targetNode;
/*     */   protected String processDefId;
/*     */   
/*     */   public CreateNewExecutionCmd(String processDefId, String actInstance, String targetNode) {
/*  36 */     this.targetNode = targetNode;
/*  37 */     this.actInstance = actInstance;
/*  38 */     this.processDefId = processDefId;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object execute(CommandContext commandContext) {
/*  43 */     BpmNodeDef startNode = ((BpmProcessDefService)AppUtil.getBean(BpmProcessDefService.class)).getStartEvent(this.processDefId);
/*     */     
/*  45 */     ExecutionEntity concurrentRoot = getRootExecution(commandContext);
/*     */ 
/*     */     
/*  48 */     ActivityImpl startNodeActiviti = concurrentRoot.getProcessDefinition().findActivity(startNode.getNodeId());
/*  49 */     List<PvmTransition> outgoingTransitions = startNodeActiviti.getOutgoingTransitions();
/*  50 */     outgoingTransitions.clear();
/*     */ 
/*     */     
/*  53 */     ActivityImpl destAct = concurrentRoot.getProcessDefinition().findActivity(this.targetNode);
/*  54 */     if (destAct == null) {
/*  55 */       throw new BusinessException(BpmStatusCode.BPM_SKIP_TARGET_NODE_LOSE);
/*     */     }
/*     */     
/*  58 */     TransitionImpl transitionImpl = startNodeActiviti.createOutgoingTransition();
/*  59 */     transitionImpl.setDestination(destAct);
/*     */ 
/*     */     
/*  62 */     ExecutionEntity outgoingExecution = concurrentRoot.createExecution();
/*     */     
/*  64 */     outgoingExecution.setActive(true);
/*  65 */     outgoingExecution.setScope(false);
/*  66 */     outgoingExecution.setConcurrent(true);
/*     */     
/*  68 */     outgoingExecution.takeAll(outgoingTransitions, Collections.EMPTY_LIST);
/*  69 */     outgoingExecution.remove();
/*     */ 
/*     */ 
/*     */     
/*  73 */     return null;
/*     */   }
/*     */   
/*     */   private ExecutionEntity getRootExecution(CommandContext commandContext) {
/*  77 */     List<ExecutionEntity> executions = commandContext.getExecutionEntityManager().findChildExecutionsByProcessInstanceId(this.actInstance);
/*  78 */     if (CollectionUtil.isEmpty(executions)) {
/*  79 */       throw new BusinessException("流程实例不存在：  processInstanceId :" + this.actInstance);
/*     */     }
/*     */ 
/*     */     
/*  83 */     if (executions.size() > 1) {
/*  84 */       for (ExecutionEntity e : executions) {
/*  85 */         if (e.getId().equals(e.getProcessInstanceId())) {
/*  86 */           return e;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  92 */     ExecutionEntity execution2Parent = executions.get(0);
/*     */     
/*  94 */     ExecutionEntity oldExecution2Sub = execution2Parent.createExecution();
/*  95 */     oldExecution2Sub.setActive(true);
/*  96 */     execution2Parent.setActive(false);
/*     */     
/*  98 */     oldExecution2Sub.setActivity(execution2Parent.getActivity());
/*  99 */     execution2Parent.setActivity(null);
/*     */     
/* 101 */     oldExecution2Sub.setConcurrent(true);
/* 102 */     oldExecution2Sub.setScope(false);
/*     */ 
/*     */     
/* 105 */     DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
/* 106 */     List<TaskEntity> tasks = commandContext.getTaskEntityManager().findTasksByExecutionId(execution2Parent.getId());
/* 107 */     for (TaskEntity task : tasks) {
/* 108 */       task.setExecutionId(oldExecution2Sub.getId());
/* 109 */       dbSqlSession.update((PersistentObject)task);
/*     */     } 
/*     */     
/* 112 */     return execution2Parent;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/cmd/CreateNewExecutionCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */