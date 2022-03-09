/*     */ package cn.gwssi.ecloudbpm.wf.engine.listener;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.act.listener.ActEventListener;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.data.handle.IBpmBusDataHandle;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.ThreadMapUtil;
/*     */ import cn.hutool.core.lang.Assert;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.delegate.event.ActivitiEvent;
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
/*     */   BpmTaskManager taskManager;
/*     */   @Resource
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
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
/*  80 */         throw new BusinessException("流程任务数据异常[" + tasks.size() + " ]：instanceID" + taskComplateCmd.getInstanceId() + "节点：" + cancelledEvent.getActivityId());
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
/* 127 */     Assert.notNull(instance, "Event 流程实例丢失! Activiti ProcessInstanceId %s", new Object[] { cancelledEvent.getProcessInstanceId() });
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
/* 139 */     taskComplateCmd.setOpinion("任务取消");
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
/*     */             childOpinion.setOpinion("会签超时 ，自动回收");
/*     */             this.bpmTaskOpinionManager.update(childOpinion);
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/ActivitiCancelledListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */