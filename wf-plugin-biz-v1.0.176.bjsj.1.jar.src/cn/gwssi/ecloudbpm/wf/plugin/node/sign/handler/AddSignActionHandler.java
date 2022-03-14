/*    */ package com.dstz.bpm.plugin.node.sign.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.constant.NodeType;
/*    */ import com.dstz.bpm.api.constant.TaskType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*    */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
/*    */ import com.dstz.base.core.id.IdUtil;
/*    */ import com.dstz.sys.api.model.DefaultIdentity;
/*    */ import com.dstz.sys.api.model.SysIdentity;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class AddSignActionHandler
/*    */   extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
/*    */   @Autowired
/*    */   private BpmTaskManager bpmTaskManager;
/*    */   @Autowired
/*    */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*    */   
/*    */   public ActionType getActionType() {
/* 35 */     return ActionType.ADDSIGN;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 40 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 45 */     return "/bpm/task/addSignActionDialog.html";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void doAction(DefualtTaskActionCmd actionModel) {
/* 55 */     JSON bpmTaskJson = (JSON)JSON.toJSON(actionModel.getBpmTask());
/* 56 */     JSONObject jsonObject = actionModel.getExtendConf();
/* 57 */     JSONArray users = jsonObject.getJSONArray("users");
/* 58 */     BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getBpmTask().getId());
/* 59 */     String mainTaskId = taskOpinion.getSignId();
/* 60 */     users.forEach(obj -> {
/*    */           JSONObject json = (JSONObject)obj;
/*    */           BpmTask task = (BpmTask)JSON.toJavaObject(bpmTaskJson, BpmTask.class);
/*    */           task.setId(IdUtil.getSuid());
/*    */           task.setTaskType(TaskType.ADD_SIGN.getKey());
/*    */           task.setParentId(actionModel.getBpmTask().getParentId());
/*    */           task.setAssigneeId(json.getString("id"));
/*    */           task.setAssigneeNames(json.getString("name"));
/*    */           List<SysIdentity> identityList = new ArrayList<>();
/*    */           identityList.add(new DefaultIdentity(json.getString("id"), json.getString("name"), "user", json.getString("orgId")));
/*    */           this.bpmTaskOpinionManager.createOpinion((IBpmTask)task, actionModel.getBpmInstance(), identityList, actionModel.getOpinion(), actionModel.getActionName(), actionModel.getFormId(), mainTaskId, taskOpinion.getTrace(), taskOpinion.getVersion());
/*    */           this.taskIdentityLinkManager.createIdentityLink((IBpmTask)task, identityList);
/*    */           this.bpmTaskManager.create(task);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 87 */     NodeType nodeType = nodeDef.getType();
/* 88 */     if (nodeType == NodeType.USERTASK || nodeType == NodeType.SIGNTASK) {
/* 89 */       return Boolean.valueOf(true);
/*    */     }
/* 91 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/sign/handler/AddSignActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */