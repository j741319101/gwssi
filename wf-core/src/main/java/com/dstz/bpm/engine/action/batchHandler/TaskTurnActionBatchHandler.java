/*    */ package com.dstz.bpm.engine.action.batchHandler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.BatchActionType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.FlowBatchRequestParam;
/*    */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */
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
/*    */ @Component
/*    */ public class TaskTurnActionBatchHandler
/*    */   extends AbsActionBatchHandler
/*    */ {
/*    */   public void execute(FlowBatchRequestParam flowParam) {
/* 28 */     FlowRequestParam flowRequestParam = new FlowRequestParam();
/* 29 */     flowRequestParam.setOpinion(flowParam.getOption());
/* 30 */     flowRequestParam.setAction(BatchActionType.AGREE.getKey());
/* 31 */     flowParam.getParam().forEach(param -> {
/*    */           flowRequestParam.setInstanceId(param.get("instanceId").toString());
/*    */           flowRequestParam.setTaskId(param.get("taskId").toString());
/*    */           DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowRequestParam);
/*    */           taskModel.executeCmd();
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/batchHandler/TaskTurnActionBatchHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */