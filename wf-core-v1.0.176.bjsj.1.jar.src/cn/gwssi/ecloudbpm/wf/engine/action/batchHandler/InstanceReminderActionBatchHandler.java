/*    */ package com.dstz.bpm.engine.action.batchHandler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.BatchActionType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.FlowBatchRequestParam;
/*    */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*    */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*    */ import java.util.Map;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class InstanceReminderActionBatchHandler
/*    */   extends AbsActionBatchHandler
/*    */ {
/*    */   public void execute(FlowBatchRequestParam flowParam) {
/* 22 */     FlowRequestParam flowRequestParam = new FlowRequestParam();
/* 23 */     flowRequestParam.setAction(BatchActionType.REMINDER.getKey());
/* 24 */     flowRequestParam.setOpinion(flowParam.getOption());
/* 25 */     flowRequestParam.setExtendConf(flowParam.getExtendConf());
/* 26 */     flowParam.getParam().forEach(param -> {
/*    */           flowRequestParam.setInstanceId(param.get("instanceId").toString());
/*    */           DefaultInstanceActionCmd taskModel = new DefaultInstanceActionCmd(flowRequestParam);
/*    */           taskModel.executeCmd();
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/batchHandler/InstanceReminderActionBatchHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */