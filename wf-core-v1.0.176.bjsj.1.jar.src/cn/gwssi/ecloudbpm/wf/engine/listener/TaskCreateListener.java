/*     */ package com.dstz.bpm.engine.listener;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.constant.ScriptType;
/*     */ import com.dstz.bpm.api.constant.TaskSkipType;
/*     */ import com.dstz.bpm.api.constant.TaskStatus;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ @Component
/*     */ public class TaskCreateListener
/*     */   extends AbstractTaskListener<DefualtTaskActionCmd>
/*     */ {
/*     */   private static final long serialVersionUID = -7836822392037648008L;
/*     */   @Resource
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public EventType getBeforeTriggerEventType() {
/*  59 */     return EventType.TASK_CREATE_EVENT; } @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager; @Resource
/*     */   private BpmTaskStackManager bpmExecutionStackManager; @Resource
/*     */   private TaskIdentityLinkManager taskIdentityLinkManager; @Resource
/*     */   private UserService userService; public EventType getAfterTriggerEventType() {
/*  64 */     return EventType.TASK_POST_CREATE_EVENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforePluginExecute(DefualtTaskActionCmd taskActionModel) {
/*  69 */     this.LOG.debug("任务【{}】执行创建过程 - taskID: {}", taskActionModel.getBpmTask().getName(), taskActionModel.getBpmTask().getId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void triggerExecute(DefualtTaskActionCmd taskActionModel) {
/*  75 */     BpmTask task = (BpmTask)taskActionModel.getBpmTask();
/*  76 */     IBpmInstance instance = taskActionModel.getBpmInstance();
/*  77 */     assignUser((TaskActionCmd)taskActionModel);
/*  78 */     if (StringUtils.equals(instance.getStatus(), InstanceStatus.STATUS_BACK.getKey())) {
/*  79 */       task.setStatus(TaskStatus.BACK.getKey());
/*  80 */       task.setPriority(Integer.valueOf(task.getPriority().intValue() + TaskStatus.BACK.getPriority()));
/*     */     } 
/*  82 */     this.bpmTaskManager.create(task);
/*  83 */     this.bpmTaskOpinionManager.createOpinionByTask((TaskActionCmd)taskActionModel);
/*     */     
/*  85 */     BpmTaskStack stack = this.bpmExecutionStackManager.createStackByTask((IBpmTask)task, taskActionModel.getExecutionStack());
/*     */     
/*  87 */     taskActionModel.setExecutionStack((BpmExecutionStack)stack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPluginExecute(DefualtTaskActionCmd taskActionModel) {
/*  94 */     remindNextUserActionType(taskActionModel);
/*     */   }
/*     */ 
/*     */   
/*     */   private void remindNextUserActionType(DefualtTaskActionCmd taskActionModel) {
/*  99 */     if (taskActionModel.isHasSkipThisTask() != TaskSkipType.NO_SKIP) {
/*     */       return;
/*     */     }
/* 102 */     if (((BaseActionCmd)BpmContext.getTopActionModel()).isIfremindNextUser()) {
/* 103 */       BpmTask bpmtask = (BpmTask)taskActionModel.getBpmTask();
/* 104 */       List<SysIdentity> identities = taskActionModel.getBpmIdentity(bpmtask.getNodeId());
/* 105 */       if (identities != null) {
/* 106 */         extractBpmIdentity(identities);
/* 107 */         BpmContext.setThreadNextIdentitys(identities);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ScriptType getScriptType() {
/* 114 */     return ScriptType.CREATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void assignUser(TaskActionCmd taskActionModel) {
/* 123 */     IBpmTask bpmTask = taskActionModel.getBpmTask();
/*     */     
/* 125 */     List<SysIdentity> identityList = taskActionModel.getBpmIdentity(bpmTask.getNodeId());
/*     */     
/* 127 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(bpmTask.getDefId());
/*     */ 
/*     */     
/* 130 */     if (CollectionUtil.isEmpty(identityList)) {
/* 131 */       if (!processDef.getExtProperties().isAllowExecutorEmpty()) {
/* 132 */         throw new BusinessMessage(String.format("%s节点 任务候选人不能为空!", new Object[] { bpmTask.getName() }), BpmStatusCode.NO_TASK_USER);
/*     */       }
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 140 */     SysIdentity firstIdentity = identityList.get(0);
/*     */     
/* 142 */     if (identityList.size() == 1 && firstIdentity.getType().equals("user")) {
/* 143 */       bpmTask.setAssigneeId(firstIdentity.getId());
/* 144 */       bpmTask.setAssigneeNames(firstIdentity.getName());
/*     */     } else {
/* 146 */       bpmTask.setAssigneeId("0");
/*     */       
/* 148 */       StringBuilder nameSb = new StringBuilder();
/* 149 */       for (SysIdentity identity : identityList) {
/* 150 */         if (nameSb.length() > 0) {
/* 151 */           nameSb.append(";");
/*     */         }
/* 153 */         nameSb.append(identity.getName());
/*     */       } 
/*     */       
/* 156 */       bpmTask.setAssigneeNames(nameSb.toString());
/*     */     } 
/*     */     
/* 159 */     this.taskIdentityLinkManager.createIdentityLink(bpmTask, identityList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefualtTaskActionCmd getActionModel(TaskEntity taskEntity) {
/* 168 */     BaseActionCmd model = BpmContext.getActionModel(taskEntity.getProcessInstanceId());
/* 169 */     if (!taskEntity.getProcessInstanceId().equals(model.getBpmInstance().getActInstId())) {
/* 170 */       throw new BusinessException("数据异常，actioncdm 与  TaskEntity 数据不一致，请检查！");
/*     */     }
/*     */ 
/*     */     
/* 174 */     BpmTask task = genByActTask(taskEntity, model.getBpmInstance());
/*     */     
/* 176 */     DefualtTaskActionCmd createModel = new DefualtTaskActionCmd();
/* 177 */     createModel.setBpmInstance(model.getBpmInstance());
/* 178 */     createModel.setBpmDefinition(model.getBpmDefinition());
/* 179 */     createModel.setBizDataMap(model.getBizDataMap());
/* 180 */     createModel.setBpmIdentities(model.getBpmIdentities());
/* 181 */     createModel.setDynamicBpmIdentity(model.getDynamicBpmIdentity());
/* 182 */     createModel.setDynamicSubmitTaskName(model.getDynamicSubmitTaskName());
/* 183 */     createModel.setDoActionName(model.getDoActionName());
/* 184 */     createModel.setBusinessKey(model.getBusinessKey());
/* 185 */     createModel.setActionName(ActionType.CREATE.getKey());
/* 186 */     createModel.setIfremindNextUser(model.isIfremindNextUser());
/* 187 */     createModel.setBpmTask((IBpmTask)task);
/* 188 */     createModel.setExtendConf(model.getExtendConf());
/* 189 */     createModel.setDelagateTask((DelegateTask)taskEntity);
/* 190 */     createModel.setExecutionStack(model.getExecutionStack());
/* 191 */     createModel.setApproveOrgId(model.getApproveOrgId());
/* 192 */     BpmContext.setActionModel((ActionCmd)createModel);
/*     */     
/* 194 */     return createModel;
/*     */   }
/*     */ 
/*     */   
/*     */   private BpmTask genByActTask(TaskEntity taskEntity, IBpmInstance iBpmInstance) {
/* 199 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(iBpmInstance.getDefId(), taskEntity.getTaskDefinitionKey());
/* 200 */     int isSupportMobile = 1;
/* 201 */     if (nodeDef.getMobileForm() != null && nodeDef.getMobileForm().isFormEmpty()) {
/* 202 */       isSupportMobile = 0;
/*     */     }
/*     */     
/* 205 */     BpmTask task = new BpmTask();
/* 206 */     task.setActExecutionId(taskEntity.getExecutionId());
/* 207 */     task.setActInstId(taskEntity.getProcessInstanceId());
/* 208 */     task.setDefId(iBpmInstance.getDefId());
/* 209 */     task.setId(taskEntity.getId());
/* 210 */     task.setInstId(iBpmInstance.getId());
/* 211 */     task.setName(taskEntity.getName());
/* 212 */     task.setNodeId(taskEntity.getTaskDefinitionKey());
/* 213 */     task.setParentId("0");
/* 214 */     task.setPriority(Integer.valueOf(taskEntity.getPriority()));
/* 215 */     task.setStatus(TaskType.NORMAL.getKey());
/* 216 */     task.setTaskType(getTaskTypeByNodeType(nodeDef.getType()));
/* 217 */     task.setSubject(iBpmInstance.getSubject());
/* 218 */     task.setSupportMobile(Integer.valueOf(isSupportMobile));
/* 219 */     task.setStatus(TaskStatus.NORMAL.getKey());
/* 220 */     task.setTaskId(taskEntity.getId());
/* 221 */     task.setTypeId(iBpmInstance.getTypeId());
/*     */     
/* 223 */     return task;
/*     */   }
/*     */   
/*     */   private String getTaskTypeByNodeType(NodeType type) {
/* 227 */     switch (type) {
/*     */       case SIGNTASK:
/* 229 */         return TaskType.SIGN.getKey();
/*     */       
/*     */       case CALLACTIVITY:
/* 232 */         return TaskType.SUBFLOW.getKey();
/*     */       
/*     */       case USERTASK:
/* 235 */         return TaskType.NORMAL.getKey();
/*     */     } 
/*     */     
/* 238 */     return TaskType.NORMAL.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   private void extractBpmIdentity(List<SysIdentity> identities) {
/* 243 */     List<SysIdentity> results = new ArrayList<>();
/* 244 */     for (SysIdentity bpmIdentity : identities) {
/* 245 */       if ("user".equals(bpmIdentity.getType())) {
/* 246 */         results.add(bpmIdentity); continue;
/*     */       } 
/* 248 */       List<IUser> users = this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
/* 249 */       for (IUser user : users) {
/* 250 */         results.add(new DefaultIdentity(user));
/*     */       }
/*     */     } 
/*     */     
/* 254 */     identities.clear();
/* 255 */     identities.addAll(results);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/TaskCreateListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */