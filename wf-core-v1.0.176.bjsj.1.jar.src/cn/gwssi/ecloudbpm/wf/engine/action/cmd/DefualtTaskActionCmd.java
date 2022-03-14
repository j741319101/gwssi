/*     */ package com.dstz.bpm.engine.action.cmd;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.TaskSkipType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.exception.WorkFlowException;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*     */ import com.dstz.bpm.engine.action.handler.AbsActionHandler;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import java.util.Map;
/*     */ import org.activiti.engine.delegate.DelegateTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefualtTaskActionCmd
/*     */   extends BaseActionCmd
/*     */   implements TaskActionCmd
/*     */ {
/*     */   private String taskId;
/*     */   private IBpmTask bpmTask;
/*     */   private DelegateTask delagateTask;
/*  32 */   private TaskSkipType hasSkipThisTask = TaskSkipType.NO_SKIP;
/*     */   
/*     */   private TaskIdentityLink taskIdentityLink;
/*     */ 
/*     */   
/*     */   public DefualtTaskActionCmd() {}
/*     */ 
/*     */   
/*     */   public DefualtTaskActionCmd(FlowRequestParam flowParam) {
/*  41 */     super(flowParam);
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/*  45 */     if (this.bpmTask != null) {
/*  46 */       return this.bpmTask.getId();
/*     */     }
/*     */     
/*  49 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  53 */     this.taskId = taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initSpecialParam(FlowRequestParam flowParam) {
/*  60 */     String taskId = flowParam.getTaskId();
/*  61 */     if (StringUtil.isEmpty(taskId)) {
/*  62 */       throw new BusinessException("taskId 不能为空", BpmStatusCode.TASK_NOT_FOUND);
/*     */     }
/*  64 */     setTaskLinkId(flowParam.getTaskLinkId());
/*  65 */     setTaskId(taskId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IBpmTask getBpmTask() {
/*  71 */     return this.bpmTask;
/*     */   }
/*     */   
/*     */   public void setBpmTask(IBpmTask task) {
/*  75 */     this.bpmTask = task;
/*     */   }
/*     */   
/*     */   public TaskIdentityLink getTaskIdentityLink() {
/*  79 */     return this.taskIdentityLink;
/*     */   }
/*     */   
/*     */   public void setTaskIdentityLink(TaskIdentityLink taskIdentityLink) {
/*  83 */     this.taskIdentityLink = taskIdentityLink;
/*     */   }
/*     */   
/*     */   public DelegateTask getDelagateTask() {
/*  87 */     if (this.delagateTask == null);
/*     */ 
/*     */     
/*  90 */     return this.delagateTask;
/*     */   }
/*     */   
/*     */   public void setDelagateTask(DelegateTask task) {
/*  94 */     this.delagateTask = task;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/*  99 */     return this.bpmTask.getNodeId();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVariable(String variableName, Object value) {
/* 105 */     if (this.delagateTask == null) {
/* 106 */       throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
/*     */     }
/* 108 */     this.delagateTask.setVariable(variableName, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getVariable(String variableName) {
/* 113 */     if (this.delagateTask == null) {
/* 114 */       throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
/*     */     }
/* 116 */     return this.delagateTask.getVariable(variableName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasVariable(String variableName) {
/* 121 */     if (this.delagateTask == null) {
/* 122 */       throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
/*     */     }
/* 124 */     return this.delagateTask.hasVariable(variableName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeVariable(String variableName) {
/* 129 */     if (this.delagateTask == null) {
/* 130 */       throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getVariables() {
/* 137 */     return this.delagateTask.getVariables();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String executeSkipTask() {
/* 142 */     if (this.hasExecuted) {
/* 143 */       throw new BusinessException("action cmd cannot be invoked twice", BpmStatusCode.PARAM_ILLEGAL);
/*     */     }
/* 145 */     this.hasExecuted = true;
/*     */     
/* 147 */     ActionType actonType = ActionType.fromKey(getActionName());
/*     */     
/* 149 */     AbsActionHandler handler = (AbsActionHandler)AppUtil.getBean(actonType.getBeanId());
/* 150 */     if (handler == null) {
/* 151 */       throw new BusinessException("action beanId cannot be found :" + actonType.getName(), BpmStatusCode.NO_TASK_ACTION);
/*     */     }
/* 153 */     handler.skipTaskExecute(this);
/* 154 */     return handler.getActionType().getName();
/*     */   }
/*     */   
/*     */   public TaskSkipType isHasSkipThisTask() {
/* 158 */     return this.hasSkipThisTask;
/*     */   }
/*     */   
/*     */   public void setHasSkipThisTask(TaskSkipType isSkip) {
/* 162 */     this.hasSkipThisTask = isSkip;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/cmd/DefualtTaskActionCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */