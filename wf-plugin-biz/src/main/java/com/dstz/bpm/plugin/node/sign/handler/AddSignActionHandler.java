/*    */ package com.dstz.bpm.plugin.node.sign.handler;
/*    */ 
/*    */ import com.dstz.base.core.id.IdUtil;
/*    */ import com.dstz.bpm.api.constant.ActionType;
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
/* 34 */     return ActionType.ADDSIGN;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 39 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 44 */     return "/bpm/task/addSignActionDialog.html";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void doAction(DefualtTaskActionCmd actionModel) {
/* 54 */     JSON bpmTaskJson = (JSON)JSON.toJSON(actionModel.getBpmTask());
/* 55 */     JSONObject jsonObject = actionModel.getExtendConf();
/* 56 */     JSONArray users = jsonObject.getJSONArray("users");
/* 57 */     BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getBpmTask().getId());
/* 58 */     String mainTaskId = taskOpinion.getSignId();
/* 59 */     users.forEach(obj -> {
/*    */           JSONObject json = (JSONObject)obj;
/*    */           BpmTask task = (BpmTask)JSON.toJavaObject(bpmTaskJson, BpmTask.class);
/*    */           task.setId(IdUtil.getSuid());
/*    */           task.setTaskType(TaskType.ADD_SIGN.getKey());
/*    */           task.setParentId(actionModel.getBpmTask().getId());
/*    */           task.setAssigneeId(json.getString("id"));
/*    */           task.setAssigneeNames(json.getString("name"));
/*    */           List<SysIdentity> identityList = new ArrayList<>();
/*    */           identityList.add(new DefaultIdentity(json.getString("id"), json.getString("name"), "user"));
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
/* 86 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/sign/handler/AddSignActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */