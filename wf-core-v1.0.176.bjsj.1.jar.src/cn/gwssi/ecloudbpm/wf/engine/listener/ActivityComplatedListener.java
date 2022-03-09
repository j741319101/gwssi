/*     */ package cn.gwssi.ecloudbpm.wf.engine.listener;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.act.listener.ActEventListener;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.IBpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.BpmExecutionStack;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmDefinitionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.data.handle.IBpmBusDataHandle;
/*     */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.hutool.core.lang.Assert;
/*     */ import java.util.Date;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.delegate.event.ActivitiEvent;
/*     */ import org.activiti.engine.delegate.event.impl.ActivitiActivityEventImpl;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class ActivityComplatedListener
/*     */   implements ActEventListener
/*     */ {
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceMananger;
/*     */   @Resource
/*     */   private BpmDefinitionManager bpmDefinitionManager;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Autowired
/*     */   IBpmBusDataHandle bpmBusDataHandle;
/*     */   @Autowired
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public void notify(ActivitiEvent event) {
/*  51 */     if (!(event instanceof ActivitiActivityEventImpl)) {
/*     */       return;
/*     */     }
/*  54 */     ActivitiActivityEventImpl activitEvent = (ActivitiActivityEventImpl)event;
/*  55 */     if (activitEvent.getActivityType() == null)
/*     */       return; 
/*  57 */     if (activitEvent.getActivityType().equals("callActivity"))
/*     */     {
/*  59 */       prepareSuperInstanceActionCmd(activitEvent);
/*     */     }
/*     */ 
/*     */     
/*  63 */     if ("intermediateSignalCatch,".indexOf(activitEvent.getActivityType()) != -1) {
/*  64 */       initActionModelByActivitiEvent(activitEvent);
/*     */     }
/*     */ 
/*     */     
/*  68 */     if ("startEvent,exclusiveGateway,inclusiveGateway,parallelGateway,serviceTask,intermediateSignalCatch,intermediateTimer,throwSignal,boundarySignal,boundaryTimer,boundaryError,endError".indexOf(activitEvent.getActivityType()) != -1) {
/*  69 */       createCommonExecutionStack(activitEvent);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void systemMessage(ActionCmd cmd) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createCommonExecutionStack(ActivitiActivityEventImpl event) {
/*  85 */     BaseActionCmd actionCmd = (BaseActionCmd)BpmContext.getActionModel();
/*  86 */     BpmExecutionStack taskStack = actionCmd.getExecutionStack();
/*     */     
/*  88 */     BpmTaskStack exectionStack = new BpmTaskStack();
/*  89 */     String id = IdUtil.getSuid();
/*  90 */     exectionStack.setId(id);
/*  91 */     exectionStack.setNodeId(event.getActivityId());
/*  92 */     exectionStack.setNodeName(event.getActivityName());
/*  93 */     exectionStack.setTaskId(event.getExecutionId());
/*     */     
/*  95 */     exectionStack.setStartTime(new Date());
/*  96 */     exectionStack.setEndTime(new Date());
/*  97 */     exectionStack.setInstId(actionCmd.getInstanceId());
/*  98 */     exectionStack.setNodeType(event.getActivityType());
/*  99 */     exectionStack.setActionName(BpmContext.getActionModel());
/*     */     
/* 101 */     if (taskStack == null) {
/* 102 */       exectionStack.setParentId("0");
/*     */     } else {
/* 104 */       exectionStack.setParentId(taskStack.getId());
/*     */     } 
/* 106 */     exectionStack.setTrace(BpmContext.peekMulInstOpTrace());
/* 107 */     this.bpmTaskStackManager.create(exectionStack);
/*     */ 
/*     */     
/* 110 */     actionCmd.setExecutionStack((BpmExecutionStack)exectionStack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initActionModelByActivitiEvent(ActivitiActivityEventImpl activitEvent) {
/* 116 */     if (BpmContext.getActionModel() != null) {
/* 117 */       IBpmInstance iBpmInstance = BpmContext.getActionModel().getBpmInstance();
/* 118 */       if (iBpmInstance == null || !iBpmInstance.getActInstId().equals(activitEvent.getProcessInstanceId())) {
/* 119 */         BpmContext.cleanTread();
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 126 */     BpmInstance instance = this.bpmInstanceMananger.getByActInstId(activitEvent.getProcessInstanceId());
/* 127 */     Assert.notNull(instance, "TIMER_FIRED Event 流程实例丢失! Activiti ProcessInstanceId %s", new Object[] { activitEvent.getProcessInstanceId() });
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
/* 139 */     taskComplateCmd.setOpinion("任务进入定时事件");
/*     */ 
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
/*     */   private void prepareSuperInstanceActionCmd(ActivitiActivityEventImpl activitEvent) {
/* 153 */     BaseActionCmd actionCmd = (BaseActionCmd)BpmContext.getActionModel();
/* 154 */     IBpmInstance childInstance = actionCmd.getBpmInstance();
/*     */ 
/*     */     
/* 157 */     if (StringUtil.isZeroEmpty(childInstance.getParentInstId())) {
/*     */       return;
/*     */     }
/*     */     
/* 161 */     BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceMananger.get(childInstance.getParentInstId());
/* 162 */     if (!bpmInstance.getActInstId().equals(activitEvent.getProcessInstanceId())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 168 */     BpmTaskStack bpmTaskStack = updateExcutionStack(activitEvent.getExecutionId());
/*     */     
/* 170 */     BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get(bpmInstance.getDefId());
/*     */     
/* 172 */     DefualtTaskActionCmd callActiviti = new DefualtTaskActionCmd();
/* 173 */     callActiviti.setBpmDefinition((IBpmDefinition)bpmDefinition);
/* 174 */     callActiviti.setBpmInstance((IBpmInstance)bpmInstance);
/* 175 */     callActiviti.setExecutionStack((BpmExecutionStack)bpmTaskStack);
/* 176 */     callActiviti.setActionName(ActionType.CREATE.getKey());
/* 177 */     callActiviti.setBpmIdentities(actionCmd.getBpmIdentities());
/* 178 */     callActiviti.setBizDataMap(this.bpmBusDataHandle.getInstanceBusData(bpmInstance.getId(), null));
/* 179 */     callActiviti.setApproveOrgId(actionCmd.getApproveOrgId());
/*     */     
/* 181 */     BpmContext.setActionModel((ActionCmd)callActiviti);
/*     */   }
/*     */ 
/*     */   
/*     */   private BpmTaskStack updateExcutionStack(String executionId) {
/* 186 */     BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(executionId);
/*     */     
/* 188 */     bpmTaskStack.setEndTime(new Date());
/* 189 */     bpmTaskStack.setActionName(BpmContext.getActionModel());
/* 190 */     this.bpmTaskStackManager.update(bpmTaskStack);
/*     */     
/* 192 */     return bpmTaskStack;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/ActivityComplatedListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */