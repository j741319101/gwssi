/*    */ package cn.gwssi.ecloudbpm.wf.engine.action.batchHandler;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.BatchActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.FlowBatchRequestParam;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.FlowRequestParam;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import java.util.Map;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class CarbonCopyActionBatchHandler
/*    */   extends AbsActionBatchHandler {
/*    */   public void execute(FlowBatchRequestParam flowParam) {
/* 14 */     FlowRequestParam flowRequestParam = new FlowRequestParam();
/* 15 */     flowRequestParam.setAction(BatchActionType.CARBONCOPY.getKey());
/* 16 */     flowRequestParam.setOpinion(flowParam.getOption());
/* 17 */     flowRequestParam.setExtendConf(flowParam.getExtendConf());
/* 18 */     flowParam.getParam().forEach(param -> {
/*    */           flowRequestParam.setInstanceId(param.get("instanceId").toString());
/*    */           flowRequestParam.setTaskId(param.get("id").toString());
/*    */           DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowRequestParam);
/*    */           taskModel.executeCmd();
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/batchHandler/CarbonCopyActionBatchHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */