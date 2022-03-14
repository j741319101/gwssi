/*     */ package com.dstz.bpm.act.service.impl;
/*     */ 
/*     */ import com.dstz.bpm.act.service.ActTaskService;
/*     */ import com.dstz.bpm.act.util.ActivitiUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.TaskService;
/*     */ import org.activiti.engine.delegate.DelegateTask;
/*     */ import org.activiti.engine.impl.persistence.entity.TaskEntity;
/*     */ import org.activiti.engine.task.TaskQuery;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class ActTaskServiceImpl
/*     */   implements ActTaskService
/*     */ {
/*     */   @Resource
/*     */   TaskService taskService;
/*     */   
/*     */   public DelegateTask getByTaskId(String taskId) {
/*  24 */     TaskEntity task = (TaskEntity)((TaskQuery)this.taskService.createTaskQuery().taskId(taskId)).singleResult();
/*  25 */     return (DelegateTask)task;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void completeTask(String taskId, String... destinations) {
/*  31 */     completeTask(taskId, null, destinations);
/*     */   }
/*     */ 
/*     */   
/*     */   public void completeTask(String taskId, Map<String, Object> variables) {
/*  36 */     if (CollectionUtil.isEmpty(variables)) {
/*  37 */       this.taskService.complete(taskId);
/*     */     } else {
/*  39 */       this.taskService.complete(taskId, variables);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void completeTask(String taskId, Map<String, Object> variables, String... destinations) {
/*  47 */     if (destinations.length == 1) {
/*  48 */       String destination = destinations[0];
/*  49 */       if (StringUtil.isNotEmpty(destination) && destination.indexOf("$$") != -1) {
/*  50 */         destinations = new String[] { destination.substring(0, destination.indexOf("$$")) };
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  55 */     TaskEntity task = (TaskEntity)((TaskQuery)this.taskService.createTaskQuery().taskId(taskId)).singleResult();
/*     */     
/*  57 */     String curNodeId = task.getTaskDefinitionKey();
/*  58 */     String actDefId = task.getProcessDefinitionId();
/*     */     
/*  60 */     Map<String, Object> activityMap = ActivitiUtil.skipPrepare(actDefId, curNodeId, destinations);
/*     */     try {
/*  62 */       completeTask(taskId, variables);
/*  63 */     } catch (Exception ex) {
/*  64 */       throw new RuntimeException(ex);
/*     */     } finally {
/*     */       
/*  67 */       ActivitiUtil.restoreActivity(activityMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getVariable(String taskId, String variableName) {
/*  74 */     return this.taskService.getVariable(taskId, variableName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getVariables(String taskId) {
/*  80 */     return this.taskService.getVariables(taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void completeTask(String taskId) {
/*  85 */     this.taskService.complete(taskId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVariable(String taskId, String variableName, Object value) {
/*  91 */     this.taskService.setVariable(taskId, variableName, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVariables(String taskId, Map<String, ? extends Object> variables) {
/*  97 */     this.taskService.setVariables(taskId, variables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getVariableLocal(String taskId, String variableName) {
/* 104 */     return this.taskService.getVariableLocal(taskId, variableName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getVariablesLocal(String taskId) {
/* 110 */     return this.taskService.getVariablesLocal(taskId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/service/impl/ActTaskServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */