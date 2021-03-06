/*    */ package com.dstz.bpm.engine.action.cmd;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.BatchActionType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.FlowBatchRequestParam;
/*    */ import com.dstz.bpm.engine.action.batchHandler.AbsActionBatchHandler;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ 
/*    */ public class DefaultTaskActionBatchCmd {
/*    */   public String executeCmd(FlowBatchRequestParam flowParam) {
/* 10 */     BatchActionType batchActionType = BatchActionType.fromKey(flowParam.getAction());
/* 11 */     AbsActionBatchHandler handler = (AbsActionBatchHandler)AppUtil.getBean(batchActionType.getBeanId());
/* 12 */     handler.execute(flowParam);
/* 13 */     return batchActionType.getName();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/cmd/DefaultTaskActionBatchCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */