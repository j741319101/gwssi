/*     */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*     */ import com.dstz.bpm.plugin.core.model.DynamicTask;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class HandelRejectAction
/*     */ {
/*     */   @Resource
/*     */   DynamicTaskManager dynamicTaskManager;
/*     */   
/*     */   public void handelReject(DefualtTaskActionCmd taskActionCmd, DynamicTask dynamicTask) {
/*  24 */     String destination = taskActionCmd.getDestination();
/*     */     
/*  26 */     if (StringUtil.isNotEmpty(destination)) {
/*     */       
/*  28 */       if (destination.indexOf("$$") != -1) {
/*  29 */         String taskName = destination.substring(destination.indexOf("$$") + 2);
/*  30 */         List<DynamicTaskIdentitys> taskIdentitys = dynamicTask.loadAllTaskIdentitys();
/*  31 */         for (int i = 0; i < taskIdentitys.size(); i++) {
/*  32 */           DynamicTaskIdentitys taskIdentity = taskIdentitys.get(i);
/*  33 */           if (taskIdentity.getTaskName().equals(taskName)) {
/*  34 */             dynamicTask.setCurrentIndex(Integer.valueOf(i));
/*     */             break;
/*     */           } 
/*     */         } 
/*  38 */         this.dynamicTaskManager.update(dynamicTask);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  43 */       dynamicTask.setCurrentIndex(Integer.valueOf(0));
/*  44 */       dynamicTask.setStatus("completed");
/*  45 */       this.dynamicTaskManager.update(dynamicTask);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/*  52 */     if (StringUtil.isEmpty(destination)) {
/*  53 */       dynamicTask.setStatus("completed");
/*  54 */       this.dynamicTaskManager.update(dynamicTask);
/*     */       
/*     */       return;
/*     */     } 
/*  58 */     if (dynamicTask.getCurrentIndex().intValue() < 1) {
/*     */       return;
/*     */     }
/*  61 */     dynamicTask.setCurrentIndex(Integer.valueOf(dynamicTask.getCurrentIndex().intValue() - 1));
/*  62 */     this.dynamicTaskManager.update(dynamicTask);
/*  63 */     taskActionCmd.setDestination(dynamicTask.getNodeId());
/*     */   }
/*     */ 
/*     */   
/*     */   @Resource
/*     */   BpmTaskManager bpmTaskManager;
/*     */   
/*     */   public void handelBack2Tasks(BpmTask task, DefualtTaskActionCmd taskActionCmd) {
/*  71 */     DynamicTask dynamicTask = this.dynamicTaskManager.getByStatus(task.getNodeId(), task.getActExecutionId(), "completed");
/*  72 */     if (dynamicTask == null) {
/*  73 */       throw new BusinessException("动态任务配置丢失，请联系管理员，节点：" + task.getName());
/*     */     }
/*     */     
/*  76 */     ActionCmd submitActionCmd = BpmContext.submitActionModel();
/*     */     
/*  78 */     String destination = submitActionCmd.getDestination();
/*     */     
/*  80 */     if (StringUtil.isNotEmpty(destination))
/*     */     {
/*     */       
/*  83 */       if (destination.indexOf("$$") != -1) {
/*  84 */         String taskName = destination.substring(destination.indexOf("$$") + 2);
/*  85 */         List<DynamicTaskIdentitys> list = dynamicTask.loadAllTaskIdentitys();
/*  86 */         for (int i = 0; i < list.size(); i++) {
/*  87 */           DynamicTaskIdentitys taskIdentity = list.get(i);
/*  88 */           if (taskIdentity.getTaskName().equals(taskName)) {
/*  89 */             dynamicTask.setCurrentIndex(Integer.valueOf(i));
/*     */             break;
/*     */           } 
/*     */         } 
/*  93 */         dynamicTask.setStatus("runtime");
/*  94 */         this.dynamicTaskManager.update(dynamicTask);
/*  95 */         taskActionCmd.setBpmIdentity(task.getNodeId(), dynamicTask.loadCurrentTaskIdentitys().getNodeIdentitys());
/*     */         
/*  97 */         task.setName(taskName);
/*  98 */         this.bpmTaskManager.update(task);
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/*     */     
/* 104 */     DynamicTaskIdentitys taskIdentitys = dynamicTask.loadCurrentTaskIdentitys();
/*     */     
/* 106 */     taskActionCmd.setBpmIdentity(task.getNodeId(), taskIdentitys.getNodeIdentitys());
/*     */     
/* 108 */     dynamicTask.setStatus("runtime");
/* 109 */     this.dynamicTaskManager.update(dynamicTask);
/*     */ 
/*     */     
/* 112 */     task.setName(taskIdentitys.getTaskName());
/* 113 */     this.bpmTaskManager.update(task);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/dynamictask/executer/HandelRejectAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */