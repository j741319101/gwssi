/*     */ package cn.gwssi.ecloudbpm.wf.act.service.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.act.cmd.CreateNewExecutionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.act.service.ActInstanceService;
/*     */ import cn.gwssi.ecloudbpm.wf.act.util.ActivitiUtil;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.ProcessEngine;
/*     */ import org.activiti.engine.RuntimeService;
/*     */ import org.activiti.engine.impl.ProcessEngineImpl;
/*     */ import org.activiti.engine.impl.identity.Authentication;
/*     */ import org.activiti.engine.impl.interceptor.Command;
/*     */ import org.activiti.engine.impl.interceptor.CommandExecutor;
/*     */ import org.activiti.engine.runtime.ProcessInstance;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class ActInstanceServiceImpl
/*     */   implements ActInstanceService
/*     */ {
/*     */   @Resource
/*     */   RuntimeService runtimeService;
/*     */   @Resource
/*     */   ProcessEngine processEngine;
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public void createNewExecution(String bpmDefId, String actInstance, String targetNode) {
/*  39 */     CreateNewExecutionCmd cmd = new CreateNewExecutionCmd(bpmDefId, actInstance, targetNode);
/*  40 */     getCommandExecutor().execute((Command)cmd);
/*     */   }
/*     */   
/*     */   private CommandExecutor getCommandExecutor() {
/*  44 */     ProcessEngineImpl engine = (ProcessEngineImpl)this.processEngine;
/*  45 */     return engine.getProcessEngineConfiguration().getCommandExecutor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String startProcessInstance(String actDefId, String businessKey, Map<String, Object> variables) {
/*     */     try {
/*  52 */       IUser user = ContextUtil.getCurrentUser();
/*  53 */       Authentication.setAuthenticatedUserId(user.getUserId());
/*  54 */       ProcessInstance instance = this.runtimeService.startProcessInstanceById(actDefId, businessKey, variables);
/*  55 */       return instance.getId();
/*  56 */     } catch (Exception ex) {
/*  57 */       throw new RuntimeException(ex);
/*     */     } finally {
/*  59 */       Authentication.setAuthenticatedUserId(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String startProcessInstance(IBpmInstance instance, Map<String, Object> variables, String[] aryDestination) {
/*  67 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getStartEvent(instance.getDefId());
/*  68 */     String nodeId = bpmNodeDef.getNodeId();
/*     */ 
/*     */     
/*  71 */     Map<String, Object> activityMap = ActivitiUtil.skipPrepare(instance.getActDefId(), nodeId, aryDestination);
/*     */     
/*  73 */     String actInstId = "";
/*     */     try {
/*  75 */       actInstId = startProcessInstance(instance.getActDefId(), instance.getBizKey(), variables);
/*  76 */     } catch (Exception ex) {
/*  77 */       throw new RuntimeException(ex);
/*     */     } finally {
/*     */       
/*  80 */       ActivitiUtil.restoreActivity(activityMap);
/*     */     } 
/*  82 */     return actInstId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getVariables(String processInstanceId) {
/*  88 */     return this.runtimeService.getVariables(processInstanceId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteProcessInstance(String bpmnInstId, String reason) {
/*  93 */     this.runtimeService.deleteProcessInstance(bpmnInstId, reason);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ProcessInstance getProcessInstance(String bpmnInstId) {
/*  99 */     List<ProcessInstance> instances = this.runtimeService.createProcessInstanceQuery().processInstanceId(bpmnInstId).list();
/* 100 */     if (!instances.isEmpty()) {
/* 101 */       return instances.get(0);
/*     */     }
/* 103 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/service/impl/ActInstanceServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */