/*    */ package com.dstz.bpm.engine.action.batchHandler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.BatchActionType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.FlowBatchRequestParam;
/*    */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import com.dstz.base.core.util.JsonUtil;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */
/*    */
import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class TaskAgreeActionBatchHandler
/*    */   extends AbsActionBatchHandler
/*    */ {
/*    */   public void execute(FlowBatchRequestParam flowParam) {
/* 18 */     FlowRequestParam flowRequestParam = new FlowRequestParam();
/* 19 */     flowRequestParam.setOpinion(flowParam.getOption());
/* 20 */     flowRequestParam.setAction(BatchActionType.TURN.getKey());
/* 21 */     JSONObject user = new JSONObject();
/* 22 */     JsonUtil.getString(flowParam.getExtendConf(), "userId");
/* 23 */     user.put("id", JsonUtil.getString(flowParam.getExtendConf(), "userId"));
/* 24 */     user.put("name", JsonUtil.getString(flowParam.getExtendConf(), "userName"));
/* 25 */     user.put("type", JsonUtil.getString(flowParam.getExtendConf(), "userType"));
/* 26 */     JSONArray array = new JSONArray();
/* 27 */     array.add(user);
/* 28 */     JSONObject users = new JSONObject();
/* 29 */     flowParam.getParam().forEach(param -> {
/*    */           flowRequestParam.setInstanceId(param.get("instanceId").toString());
/*    */           flowRequestParam.setTaskId(param.get("taskId").toString());
/*    */           users.put(param.get("nodeId").toString(), array);
/*    */           flowRequestParam.setNodeUsers(users);
/*    */           DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowRequestParam);
/*    */           taskModel.executeCmd();
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/batchHandler/TaskAgreeActionBatchHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */