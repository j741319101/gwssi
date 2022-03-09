/*     */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.task;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.act.service.ActTaskService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.cmd.TaskCommand;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.TaskIdentityLinkManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.TaskIdentityLink;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.AbsActionHandler;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public abstract class AbstractTaskActionHandler<T extends DefualtTaskActionCmd>
/*     */   extends AbsActionHandler<T>
/*     */ {
/*     */   @Resource
/*     */   protected ActTaskService actTaskService;
/*     */   @Resource
/*     */   protected BpmTaskManager taskManager;
/*     */   
/*     */   public void doAction(T actionModel) {
/*  34 */     BpmTask bpmTask = (BpmTask)actionModel.getBpmTask();
/*     */     
/*  36 */     String taskId = bpmTask.getTaskId();
/*  37 */     String destinationNode = bpmTask.getBackNode();
/*     */     
/*  39 */     String[] destinationNodes = null;
/*  40 */     if (StringUtil.isEmpty(destinationNode)) {
/*  41 */       destinationNodes = actionModel.getDestinations();
/*     */     } else {
/*  43 */       destinationNodes = new String[] { destinationNode };
/*     */     } 
/*     */     
/*  46 */     if (StringUtil.isEmpty(destinationNode)) {
/*  47 */       destinationNode = actionModel.getDestination();
/*     */     }
/*  49 */     if (destinationNodes == null) {
/*  50 */       this.actTaskService.completeTask(taskId, actionModel.getActionVariables());
/*     */     } else {
/*     */       
/*  53 */       this.actTaskService.completeTask(taskId, actionModel.getActionVariables(), destinationNodes);
/*     */     } 
/*     */   }
/*     */   @Resource
/*     */   protected TaskCommand taskCommand; @Resource
/*     */   protected TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   protected boolean prepareActionDatas(T data) {
/*  60 */     if (data.getBpmTask() != null) return false;
/*     */     
/*  62 */     BpmTask task = (BpmTask)this.taskManager.get(data.getTaskId());
/*  63 */     if (task == null) {
/*  64 */       throw new BusinessException(BpmStatusCode.TASK_NOT_FOUND);
/*     */     }
/*  66 */     if (StringUtils.isNotEmpty(data.getTaskLinkId())) {
/*  67 */       TaskIdentityLink taskIdentityLink = (TaskIdentityLink)this.taskIdentityLinkManager.get(data.getTaskLinkId());
/*  68 */       if (taskIdentityLink == null) {
/*  69 */         throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
/*     */       }
/*  71 */       data.setTaskIdentityLink(taskIdentityLink);
/*     */     } 
/*  73 */     data.setBpmTask((IBpmTask)task);
/*  74 */     data.setDefId(task.getDefId());
/*  75 */     data.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(task.getDefId()));
/*  76 */     data.setBpmInstance((IBpmInstance)this.bpmInstanceManager.get(task.getInstId()));
/*     */     
/*  78 */     parserBusinessData((BaseActionCmd)data);
/*     */     
/*  80 */     boolean isStopExecute = handelFormInit((BaseActionCmd)data, this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId()));
/*  81 */     return isStopExecute;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/*  86 */     NodeType nodeType = nodeDef.getType();
/*     */     
/*  88 */     if (nodeType == NodeType.USERTASK || nodeType == NodeType.SIGNTASK) {
/*  89 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/*  92 */     return Boolean.valueOf(false);
/*     */   }
/*     */   
/*     */   protected void taskComplatePrePluginExecute(DefualtTaskActionCmd actionModel) {
/*  96 */     this.taskCommand.execute(EventType.TASK_PRE_COMPLETE_EVENT, (TaskActionCmd)actionModel);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void toDoActionAfter(T actionModel) {
/* 102 */     doActionAfter((BaseActionCmd)actionModel);
/*     */ 
/*     */     
/* 105 */     if (actionModel.isSource()) {
/* 106 */       BpmInstance instance = (BpmInstance)actionModel.getBpmInstance();
/* 107 */       if (instance.isHasUpdate()) {
/* 108 */         this.bpmInstanceManager.update(instance);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 118 */     return "";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/task/AbstractTaskActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */