/*    */ package com.dstz.bpm.engine.action.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*    */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*    */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultExtendTaskAction
/*    */   implements IExtendTaskAction
/*    */ {
/*    */   public void turnLeaderTask(IBpmTask bpmTask) {}
/*    */   
/*    */   public boolean carbonCopyLeaderTask(String receiverId, String receId, IBpmTask bpmTask) {
/* 19 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean carbonInstCopyLeaderTask(String receiverId, String receId, IBpmInstance bpmInstance) {
/* 24 */     return false;
/*    */   }
/*    */   
/*    */   public String getLeaderUserRights() {
/* 28 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isLeaderTask(String taskId) {
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isContainNode(String nodeId, BpmProcessDef bpmProcessDef, String appendType) {
/* 38 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parseMultInstContainNode(BpmProcessDef bpmProcessDef) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void deleteDataByInstId(String instId) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void revokeInst(String instId) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void canFreeJump(IBpmTask bpmTask) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSignTask(BpmNodeDef bpmNodeDef) {
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canRecall(IBpmTask callTask) {
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canReject(IBpmTask curTask, IBpmTask rejectTask) {
/* 71 */     return true;
/*    */   }
/*    */   
/*    */   public void addQueryDodoTaskParams(QueryFilter queryFilter, String taskTableAlias) {}
/*    */   
/*    */   public void addQueryDoReadTaskParams(QueryFilter queryFilter, String carbonTableAlias) {}
/*    */   
/*    */   public void doSomethingWhenSaveDef(BpmProcessDef bpmProcessDef, String oldDefId) {}
/*    */   
/*    */   public void doSomethingWhenDeleteDef(BpmProcessDef bpmProcessDef) {}
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/DefaultExtendTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */