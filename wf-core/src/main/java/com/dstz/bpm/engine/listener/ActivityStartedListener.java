/*    */ package com.dstz.bpm.engine.listener;
/*    */ 
/*    */ import com.dstz.bpm.act.listener.ActEventListener;
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*    */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*    */ import com.dstz.bpm.api.engine.context.BpmContext;
/*    */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*    */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*    */ import com.dstz.bpm.core.model.BpmTaskStack;
/*    */ import com.dstz.base.core.id.IdUtil;
/*    */ import java.util.Date;
/*    */ import javax.annotation.Resource;
/*    */
import org.activiti.engine.delegate.event.ActivitiEvent;
/*    */ import org.activiti.engine.delegate.event.impl.ActivitiActivityEventImpl;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ActivityStartedListener
/*    */   implements ActEventListener
/*    */ {
/*    */   @Resource
/*    */ BpmTaskStackManager bpmTaskStackMananger;
/*    */   
/*    */   public void notify(ActivitiEvent event) {
/* 38 */     if (!(event instanceof ActivitiActivityEventImpl))
/*    */       return; 
/* 40 */     ActivitiActivityEventImpl activitEvent = (ActivitiActivityEventImpl)event;
/*    */     
/* 42 */     if (activitEvent.getActivityType().equals("callActivity")) {
/* 43 */       createCallActvitiviStack(activitEvent);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void createCallActvitiviStack(ActivitiActivityEventImpl activitEvent) {
/* 49 */     BaseActionCmd action = BpmContext.getActionModel(activitEvent.getProcessInstanceId());
/* 50 */     BpmExecutionStack parentStack = action.getExecutionStack();
/* 51 */     BpmTaskStack stack = new BpmTaskStack();
/* 52 */     action.setActionName(ActionType.CREATE.getKey());
/* 53 */     stack.setInstId(action.getInstanceId());
/* 54 */     if (parentStack == null) {
/* 55 */       stack.setParentId("0");
/*    */     } else {
/* 57 */       stack.setParentId(parentStack.getId());
/*    */     } 
/*    */     
/* 60 */     BpmExecutionStack bpmExecutionStack = BpmContext.getThreadDynamictaskStack(activitEvent.getActivityId());
/* 61 */     if (bpmExecutionStack != null && 
/* 62 */       StringUtils.equals(activitEvent.getActivityId(), bpmExecutionStack.getNodeName())) {
/* 63 */       stack.setInstId(bpmExecutionStack.getInstId());
/*    */     }
/*    */     
/* 66 */     String id = IdUtil.getSuid();
/* 67 */     stack.setId(id);
/* 68 */     stack.setNodeId(activitEvent.getActivityId());
/* 69 */     stack.setNodeName(activitEvent.getActivityName());
/* 70 */     stack.setTaskId(activitEvent.getExecutionId());
/*    */     
/* 72 */     stack.setStartTime(new Date());
/*    */     
/* 74 */     stack.setNodeType("callActivity");
/* 75 */     stack.setActionName(BpmContext.getActionModel());
/*    */     
/* 77 */     stack.setTrace(BpmContext.popMulInstOpTrace());
/* 78 */     action.setExecutionStack((BpmExecutionStack)stack);
/* 79 */     this.bpmTaskStackMananger.create(stack);
/*    */   }
/*    */   
/*    */   public void systemMessage(ActionCmd cmd) {}
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/ActivityStartedListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */