/*     */ package cn.gwssi.ecloudbpm.wf.engine.listener;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.InstanceStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ScriptType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.BpmExecutionStack;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.TaskIdentityLinkManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.TaskIdentityLink;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.producer.JmsProducer;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.delegate.DelegateTask;
/*     */ import org.activiti.engine.impl.persistence.entity.TaskEntity;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class TaskCompleteListener
/*     */   extends AbstractTaskListener<DefualtTaskActionCmd>
/*     */ {
/*     */   private static final long serialVersionUID = 6844821899585103714L;
/*     */   @Resource
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Resource
/*     */   private TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   private IGroovyScriptEngine groovyScriptEngine;
/*     */   @Resource
/*     */   JmsProducer jmsProducer;
/*     */   
/*     */   public EventType getBeforeTriggerEventType() {
/*  69 */     return EventType.TASK_COMPLETE_EVENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EventType getAfterTriggerEventType() {
/*  75 */     return EventType.TASK_POST_COMPLETE_EVENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforePluginExecute(DefualtTaskActionCmd taskActionModel) {
/*  80 */     this.LOG.debug("任务【{}】执行完成事件 - TaskID: {}", taskActionModel.getBpmTask().getName(), taskActionModel.getBpmTask().getId());
/*     */     
/*  82 */     Map<String, Object> actionVariables = taskActionModel.getActionVariables();
/*  83 */     if (CollectionUtil.isEmpty(actionVariables)) {
/*     */       return;
/*     */     }
/*     */     
/*  87 */     for (String key : actionVariables.keySet()) {
/*  88 */       taskActionModel.addVariable(key, actionVariables.get(key));
/*     */     }
/*  90 */     this.LOG.debug("设置流程变量【{}】", actionVariables.keySet().toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void triggerExecute(DefualtTaskActionCmd taskActionModel) {
/*  96 */     DefualtTaskActionCmd complateModel = taskActionModel;
/*     */     
/*  98 */     this.LOG.trace("执行任务完成动作=====》更新任务意见状态");
/*  99 */     updateOpinionStatus(complateModel);
/*     */     
/* 101 */     this.LOG.trace("执行任务完成动作=====》更新任务堆栈记录");
/* 102 */     updateExcutionStack(complateModel);
/*     */     
/* 104 */     this.LOG.trace("执行任务完成动作=====》删除任务相关信息 - 任务、任务后续人");
/* 105 */     delTaskRelated(complateModel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPluginExecute(DefualtTaskActionCmd taskActionModel) {
/* 110 */     BpmContext.setOptionVersion(String.valueOf(System.currentTimeMillis()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ScriptType getScriptType() {
/* 115 */     return ScriptType.COMPLETE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefualtTaskActionCmd getActionModel(TaskEntity taskEntity) {
/* 120 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 121 */     model.setDelagateTask((DelegateTask)taskEntity);
/* 122 */     return model;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateOpinionStatus(DefualtTaskActionCmd taskActionModel) {
/* 127 */     InstanceStatus flowStatus = InstanceStatus.getByActionName(taskActionModel.getActionName());
/*     */ 
/*     */     
/* 130 */     BpmInstance instance = (BpmInstance)taskActionModel.getBpmInstance();
/* 131 */     if (!flowStatus.getKey().equals(instance.getStatus())) {
/* 132 */       instance.setStatus(flowStatus.getKey());
/* 133 */       instance.setHasUpdate(true);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 138 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskActionModel.getTaskId());
/* 139 */     if (bpmTaskOpinion == null)
/*     */       return; 
/* 141 */     OpinionStatus opnionStatus = OpinionStatus.getByActionName(taskActionModel.getActionName());
/* 142 */     bpmTaskOpinion.setStatus(opnionStatus.getKey());
/* 143 */     bpmTaskOpinion.setApproveTime(new Date());
/*     */ 
/*     */     
/* 146 */     Long durMs = Long.valueOf(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime());
/* 147 */     bpmTaskOpinion.setDurMs(Long.valueOf((durMs.longValue() > 0L) ? durMs.longValue() : 10L));
/*     */     
/* 149 */     bpmTaskOpinion.setOpinion(taskActionModel.getOpinion());
/*     */     
/* 151 */     IUser user = ContextUtil.getCurrentUser();
/* 152 */     if (user != null) {
/* 153 */       bpmTaskOpinion.setApprover(user.getUserId());
/* 154 */       bpmTaskOpinion.setApproverName(user.getFullname());
/*     */     } 
/* 156 */     String orgId = taskActionModel.getApproveOrgId();
/* 157 */     TaskIdentityLink taskIdentityLink = taskActionModel.getTaskIdentityLink();
/* 158 */     if (taskIdentityLink != null) {
/* 159 */       orgId = taskIdentityLink.getOrgId();
/*     */     }
/* 161 */     if (StringUtils.isEmpty(orgId)) {
/* 162 */       throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
/*     */     }
/* 164 */     bpmTaskOpinion.setTaskOrgId(orgId);
/*     */     
/* 166 */     taskActionModel.setApproveOrgId(orgId);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     if (BpmContext.isMulInstOpTraceEmpty() && StringUtil.isNotEmpty(bpmTaskOpinion.getTrace())) {
/* 174 */       BpmContext.pushMulInstOpTrace(bpmTaskOpinion.getTrace());
/*     */     }
/* 176 */     this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateExcutionStack(DefualtTaskActionCmd taskActionModel) {
/* 181 */     BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(taskActionModel.getTaskId());
/*     */     
/* 183 */     bpmTaskStack.setEndTime(new Date());
/* 184 */     bpmTaskStack.setActionName(BpmContext.getActionModel());
/* 185 */     this.bpmTaskStackManager.update(bpmTaskStack);
/*     */     
/* 187 */     taskActionModel.setExecutionStack((BpmExecutionStack)bpmTaskStack);
/*     */   }
/*     */   
/*     */   private void delTaskRelated(DefualtTaskActionCmd taskActionModel) {
/* 191 */     this.taskIdentityLinkManager.removeByTaskId(taskActionModel.getTaskId());
/* 192 */     this.bpmTaskManager.remove(taskActionModel.getTaskId());
/*     */   }
/*     */   
/*     */   public void systemMessage(ActionCmd cmd) {}
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/TaskCompleteListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */