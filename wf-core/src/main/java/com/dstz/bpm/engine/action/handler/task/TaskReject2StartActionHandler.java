/*    */ package com.dstz.bpm.engine.action.handler.task;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.model.def.NodeProperties;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import java.util.List;
/*    */
import org.springframework.stereotype.Component;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component("taskReject2StartActionHandler")
/*    */ public class TaskReject2StartActionHandler
/*    */   extends TaskRejectActionHandler
/*    */ {
/*    */   protected BpmTask getPreDestination(DefualtTaskActionCmd actionModel, NodeProperties nodeProperties) {
/* 33 */     List<BpmNodeDef> nodeDefs = this.bpmProcessDefService.getStartNodes(actionModel.getDefId());
/*    */     
/* 35 */     if (nodeDefs.size() > 1);
/*    */ 
/*    */     
/* 38 */     BpmTask bpmTask = new BpmTask();
/* 39 */     bpmTask.setNodeId(((BpmNodeDef)nodeDefs.get(0)).getNodeId());
/* 40 */     bpmTask.setInstId(actionModel.getInstanceId());
/* 41 */     return bpmTask;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ActionType getActionType() {
/* 48 */     return ActionType.REJECT2START;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 53 */     return 4;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 58 */     return Boolean.valueOf(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 63 */     return "/bpm/task/taskOpinionDialog.html";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/task/TaskReject2StartActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */