/*    */ package com.dstz.bpm.engine.listener;
/*    */ 
/*    */ import com.dstz.bpm.act.listener.ActEventListener;
/*    */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*    */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*    */ import com.dstz.bpm.api.engine.context.BpmContext;
/*    */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*    */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*    */ import com.dstz.bpm.core.model.BpmTaskStack;
/*    */ import com.dstz.base.core.id.IdUtil;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import java.util.Date;
/*    */
import org.activiti.engine.delegate.event.ActivitiEvent;
/*    */ import org.activiti.engine.delegate.event.impl.ActivitiSequenceFlowTakenEventImpl;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class SequenceFlowTakenListener
/*    */   implements ActEventListener
/*    */ {
/*    */   @Autowired
/*    */   private BpmTaskStackManager bpmTaskStackManager;
/*    */   
/*    */   public void notify(ActivitiEvent event) {
/* 33 */     ActivitiSequenceFlowTakenEventImpl sequenceFlowEvent = (ActivitiSequenceFlowTakenEventImpl)event;
/*    */     
/* 35 */     BaseActionCmd actionCmd = BpmContext.getActionModel(sequenceFlowEvent.getProcessInstanceId());
/* 36 */     BpmExecutionStack oldTaskStack = actionCmd.getExecutionStack();
/*    */     
/* 38 */     BpmTaskStack sequenceFlowStack = new BpmTaskStack();
/* 39 */     String id = IdUtil.getSuid();
/* 40 */     sequenceFlowStack.setId(id);
/* 41 */     sequenceFlowStack.setNodeId(sequenceFlowEvent.getId());
/*    */     
/* 43 */     if (StringUtil.isEmpty(sequenceFlowStack.getNodeId())) {
/*    */       
/* 45 */       String nodeId = BpmContext.getThreadMulSequenceLine();
/* 46 */       if (StringUtil.isEmpty(nodeId)) {
/* 47 */         nodeId = "back";
/*    */       }
/* 49 */       sequenceFlowStack.setNodeId(nodeId);
/*    */     } 
/* 51 */     sequenceFlowStack.setNodeName(String.format("%s-》%s", new Object[] { sequenceFlowEvent.getSourceActivityId(), sequenceFlowEvent.getTargetActivityId() }));
/* 52 */     sequenceFlowStack.setTaskId(event.getExecutionId());
/*    */     
/* 54 */     sequenceFlowStack.setStartTime(new Date());
/* 55 */     sequenceFlowStack.setEndTime(new Date());
/* 56 */     sequenceFlowStack.setInstId(actionCmd.getInstanceId());
/* 57 */     sequenceFlowStack.setNodeType("sequenceFlow");
/* 58 */     sequenceFlowStack.setActionName(BpmContext.getActionModel());
/*    */     
/* 60 */     if (oldTaskStack == null) {
/* 61 */       sequenceFlowStack.setParentId("0");
/*    */     } else {
/* 63 */       sequenceFlowStack.setParentId(oldTaskStack.getId());
/*    */     } 
/*    */     
/* 66 */     BpmExecutionStack bpmExecutionStack = BpmContext.getThreadDynamictaskStack(sequenceFlowEvent.getTargetActivityId());
/* 67 */     if (bpmExecutionStack != null && 
/* 68 */       StringUtils.equals(sequenceFlowEvent.getTargetActivityId(), bpmExecutionStack.getNodeId())) {
/* 69 */       sequenceFlowStack.setInstId(bpmExecutionStack.getInstId());
/* 70 */       sequenceFlowStack.setParentId(bpmExecutionStack.getId());
/*    */     } 
/*    */     
/* 73 */     sequenceFlowStack.setTrace(BpmContext.peekMulInstOpTrace());
/* 74 */     this.bpmTaskStackManager.create(sequenceFlowStack);
/*    */     
/* 76 */     actionCmd.setExecutionStack((BpmExecutionStack)sequenceFlowStack);
/*    */   }
/*    */   
/*    */   public void systemMessage(ActionCmd cmd) {}
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/SequenceFlowTakenListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */