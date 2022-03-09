/*     */ package cn.gwssi.ecloudbpm.wf.plugin.node.addDo.handler;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.task.AbstractTaskActionHandler;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class AddDoActionHandler
/*     */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*     */ {
/*     */   @Autowired
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   
/*     */   public ActionType getActionType() {
/*  36 */     return ActionType.ADDDO;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/*  41 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/*  46 */     return "/bpm/task/addDoActionDialog.html";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doAction(DefualtTaskActionCmd actionModel) {
/*  59 */     handleCurentTask(actionModel);
/*     */ 
/*     */ 
/*     */     
/*  63 */     createNewTask(actionModel);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/*  73 */     NodeType nodeType = nodeDef.getType();
/*  74 */     return Boolean.valueOf((nodeType == NodeType.USERTASK));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleCurentTask(DefualtTaskActionCmd actionModel) {
/*  86 */     BpmTask bpmTask = (BpmTask)actionModel.getBpmTask();
/*  87 */     if (bpmTask.getStatus().equals(TaskStatus.ADDDOING.getKey())) {
/*  88 */       throw new BusinessMessage("当前任务已有处于加办中");
/*     */     }
/*  90 */     bpmTask.setStatus(TaskStatus.ADDDOING.getKey());
/*  91 */     bpmTask.setAssigneeId("-" + bpmTask.getAssigneeId());
/*  92 */     this.bpmTaskManager.update(bpmTask);
/*     */     
/*  94 */     this.bpmTaskOpinionManager.commonUpdate(bpmTask.getId(), OpinionStatus.ADD_DO, actionModel.getOpinion());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createNewTask(DefualtTaskActionCmd actionModel) {
/* 105 */     JSON bpmTaskJson = (JSON)JSON.toJSON(actionModel.getBpmTask());
/* 106 */     JSONObject jsonObject = actionModel.getExtendConf();
/* 107 */     JSONObject user = jsonObject.getJSONArray("users").getJSONObject(0);
/*     */     
/* 109 */     BpmTask task = (BpmTask)JSON.toJavaObject(bpmTaskJson, BpmTask.class);
/* 110 */     task.setId(IdUtil.getSuid());
/* 111 */     task.setAssigneeId(user.getString("id"));
/* 112 */     task.setAssigneeNames(user.getString("name"));
/* 113 */     task.setParentId(actionModel.getBpmTask().getId());
/* 114 */     task.setStatus(TaskStatus.NORMAL.getKey());
/* 115 */     task.setTaskType(TaskType.ADDDO.getKey());
/* 116 */     this.bpmTaskManager.create(task);
/* 117 */     BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getBpmTask().getId());
/*     */     
/* 119 */     this.bpmTaskOpinionManager.createOpinion((IBpmTask)task, actionModel.getBpmInstance(), null, null, ActionType.ADDDO.getKey(), actionModel
/* 120 */         .getFormId(), taskOpinion.getSignId(), taskOpinion.getTrace(), taskOpinion.getVersion());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/addDo/handler/AddDoActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */