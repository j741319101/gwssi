/*     */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*     */ import com.dstz.bpm.plugin.core.model.DynamicTask;
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
/*  44 */       dynamicTask.setStatus("complated");
/*  45 */       this.dynamicTaskManager.update(dynamicTask);
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  54 */     if (dynamicTask.getCurrentIndex().intValue() < 1) {
/*     */       return;
/*     */     }
/*  57 */     dynamicTask.setCurrentIndex(Integer.valueOf(dynamicTask.getCurrentIndex().intValue() - 1));
/*  58 */     this.dynamicTaskManager.update(dynamicTask);
/*  59 */     taskActionCmd.setDestination(dynamicTask.getNodeId());
/*     */   }
/*     */ 
/*     */   
/*     */   @Resource
/*     */   BpmTaskManager bpmTaskManager;
/*     */   
/*     */   public void handelBack2Tasks(BpmTask task, DefualtTaskActionCmd taskActionCmd) {
/*  67 */     DynamicTask dynamicTask = this.dynamicTaskManager.getByStatus(task.getNodeId(), task.getActExecutionId(), "complated");
/*  68 */     if (dynamicTask == null) {
/*  69 */       throw new BusinessException("动态任务配置丢失，请联系管理员，节点：" + task.getName());
/*     */     }
/*     */     
/*  72 */     ActionCmd submitActionCmd = BpmContext.submitActionModel();
/*     */     
/*  74 */     String destination = submitActionCmd.getDestination();
/*     */     
/*  76 */     if (StringUtil.isNotEmpty(destination))
/*     */     {
/*     */       
/*  79 */       if (destination.indexOf("$$") != -1) {
/*  80 */         String taskName = destination.substring(destination.indexOf("$$") + 2);
/*  81 */         List<DynamicTaskIdentitys> list = dynamicTask.loadAllTaskIdentitys();
/*  82 */         for (int i = 0; i < list.size(); i++) {
/*  83 */           DynamicTaskIdentitys taskIdentity = list.get(i);
/*  84 */           if (taskIdentity.getTaskName().equals(taskName)) {
/*  85 */             dynamicTask.setCurrentIndex(Integer.valueOf(i));
/*     */             break;
/*     */           } 
/*     */         } 
/*  89 */         dynamicTask.setStatus("runtime");
/*  90 */         this.dynamicTaskManager.update(dynamicTask);
/*  91 */         taskActionCmd.setBpmIdentity(task.getNodeId(), dynamicTask.loadCurrentTaskIdentitys().getNodeIdentitys());
/*     */         
/*  93 */         task.setName(taskName);
/*  94 */         this.bpmTaskManager.update(task);
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/*     */     
/* 100 */     DynamicTaskIdentitys taskIdentitys = dynamicTask.loadCurrentTaskIdentitys();
/*     */     
/* 102 */     taskActionCmd.setBpmIdentity(task.getNodeId(), taskIdentitys.getNodeIdentitys());
/*     */     
/* 104 */     dynamicTask.setStatus("runtime");
/* 105 */     this.dynamicTaskManager.update(dynamicTask);
/*     */ 
/*     */     
/* 108 */     task.setName(taskIdentitys.getTaskName());
/* 109 */     this.bpmTaskManager.update(task);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynamictask/executer/HandelRejectAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */