/*     */ package com.dstz.bpm.engine.listener;
/*     */ 
/*     */ import com.dstz.bpm.act.listener.ActEventListener;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.constant.OpinionStatus;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
/*     */ import cn.hutool.core.lang.Assert;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */
import org.activiti.engine.delegate.event.ActivitiEvent;
/*     */ import org.activiti.engine.delegate.event.impl.ActivitiActivityCancelledEventImpl;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class ActivitiCancelledListener
/*     */   implements ActEventListener
/*     */ {
/*  44 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Autowired
/*     */ BpmTaskManager taskManager;
/*     */   @Resource
/*     */ BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceMananger;
/*     */   @Autowired
/*     */   private IBpmBusDataHandle bpmBusDataHandle;
/*     */   @Autowired
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   private TaskCompleteListener taskCompleteListener;
/*     */   
/*     */   public void notify(ActivitiEvent event) {
/*  60 */     ActivitiActivityCancelledEventImpl cancelledEvent = (ActivitiActivityCancelledEventImpl)event;
/*     */ 
/*     */     
/*  63 */     if (ThreadMapUtil.get("EcloudBPMDeleteInstance") != null) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     getTaskActionCmd(cancelledEvent);
/*  68 */     DefualtTaskActionCmd taskComplateCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  69 */     if (taskComplateCmd.getActionName().equals(ActionType.MANUALEND.getKey())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  75 */     if (NodeType.USERTASK.getKey().equalsIgnoreCase(cancelledEvent.getActivityType())) {
/*  76 */       String executionId = cancelledEvent.getExecutionId();
/*     */       
/*  78 */       List<BpmTask> tasks = this.taskManager.getByInstIdNodeId(taskComplateCmd.getInstanceId(), cancelledEvent.getActivityId());
/*  79 */       if (tasks.isEmpty()) {
/*  80 */         throw new BusinessException("????????????????????????[" + tasks.size() + " ]???instanceID" + taskComplateCmd.getInstanceId() + "?????????" + cancelledEvent.getActivityId());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  86 */       List<BpmTask> currentExecutionTask = (List<BpmTask>)tasks.stream().filter(task -> (executionId.equals(task.getActExecutionId()) && !TaskType.SIGN.getKey().equals(task.getTaskType()))).collect(Collectors.toList());
/*     */       
/*  88 */       for (BpmTask task : currentExecutionTask) {
/*  89 */         cancelledTask(taskComplateCmd, task);
/*     */       }
/*     */     } 
/*     */     
/*  93 */     systemMessage((ActionCmd)taskComplateCmd);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void systemMessage(ActionCmd cmd) {}
/*     */ 
/*     */   
/*     */   private void cancelledTask(DefualtTaskActionCmd taskComplateCmd, BpmTask task) {
/* 102 */     taskComplateCmd.setBpmTask((IBpmTask)task);
/*     */ 
/*     */     
/* 105 */     this.taskCompleteListener.triggerExecute(taskComplateCmd);
/*     */ 
/*     */     
/* 108 */     removeTaskChildren(task);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void getTaskActionCmd(ActivitiActivityCancelledEventImpl cancelledEvent) {
/* 114 */     BaseActionCmd baseActionCmd = (BaseActionCmd)BpmContext.getActionModel();
/* 115 */     String approveOrgId = baseActionCmd.getApproveOrgId();
/* 116 */     if (baseActionCmd != null) {
/* 117 */       IBpmInstance iBpmInstance = BpmContext.getActionModel().getBpmInstance();
/* 118 */       if (iBpmInstance == null || !iBpmInstance.getActInstId().equals(cancelledEvent.getProcessInstanceId())) {
/* 119 */         BpmContext.cleanTread();
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 126 */     BpmInstance instance = this.bpmInstanceMananger.getByActInstId(cancelledEvent.getProcessInstanceId());
/* 127 */     Assert.notNull(instance, "Event ??????????????????! Activiti ProcessInstanceId %s", new Object[] { cancelledEvent.getProcessInstanceId() });
/*     */     
/* 129 */     DefualtTaskActionCmd taskComplateCmd = new DefualtTaskActionCmd();
/* 130 */     taskComplateCmd.setActionName(ActionType.TASKCANCELLED.getKey());
/*     */     
/* 132 */     taskComplateCmd.setBpmInstance((IBpmInstance)instance);
/* 133 */     taskComplateCmd.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(instance.getDefId()));
/*     */     
/* 135 */     taskComplateCmd.setBizDataMap(this.bpmBusDataHandle.getInstanceBusData(instance.getId(), null));
/*     */ 
/*     */ 
/*     */     
/* 139 */     taskComplateCmd.setOpinion("????????????");
/* 140 */     taskComplateCmd.setApproveOrgId(approveOrgId);
/*     */     
/* 142 */     BpmContext.cleanTread();
/* 143 */     BpmContext.setActionModel((ActionCmd)taskComplateCmd);
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
/*     */   
/*     */   private void removeTaskChildren(BpmTask task) {
/* 156 */     if (!TaskType.SIGN_SOURCE.equalsWithKey(task.getTaskType())) {
/*     */       return;
/*     */     }
/*     */     
/* 160 */     List<BpmTask> taskList = this.taskManager.getByParentId(task.getId());
/* 161 */     taskList.forEach(t -> {
/*     */           this.taskManager.remove(t.getId());
/*     */           BpmTaskOpinion childOpinion = this.bpmTaskOpinionManager.getByTaskId(t.getId());
/*     */           if (childOpinion != null) {
/*     */             childOpinion.setStatus(OpinionStatus.CANCELLED.getKey());
/*     */             childOpinion.setApproveTime(new Date());
/*     */             childOpinion.setDurMs(Long.valueOf(childOpinion.getApproveTime().getTime() - childOpinion.getCreateTime().getTime()));
/*     */             childOpinion.setOpinion("???????????? ???????????????");
/*     */             this.bpmTaskOpinionManager.update(childOpinion);
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/ActivitiCancelledListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */