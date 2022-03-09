/*     */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.instance;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.form.api.model.FormType;
/*     */ import cn.gwssi.ecloudbpm.wf.act.service.ActTaskService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.ActionDisplayHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.ActionHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.IExtendTaskAction;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.data.result.BpmFlowData;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.data.result.BpmFlowTaskData;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmRightsFormService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.TaskIdentityLinkManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.data.handle.IBpmBusDataHandle;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.JsonUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.model.DefaultJmsDTO;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.model.JmsDTO;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.model.msg.NotifyMessage;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.producer.JmsProducer;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ public class InstanceRecallActionHandler
/*     */   implements ActionHandler<DefualtTaskActionCmd>, ActionDisplayHandler<DefualtTaskActionCmd>
/*     */ {
/*     */   @Autowired
/*     */   private BpmTaskManager taskManager;
/*     */   @Resource
/*     */   private JmsProducer jmsProducer;
/*     */   @Autowired
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Autowired
/*     */   private ActTaskService actTaskService;
/*     */   @Autowired
/*     */   private BpmRightsFormService bpmRightsFormService;
/*     */   @Autowired
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Autowired
/*     */   private IBpmBusDataHandle bpmBusDataHandle;
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Autowired
/*     */   private TaskIdentityLinkManager identityLinkManager;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   
/*     */   public Boolean isDefault() {
/* 100 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 105 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(DefualtTaskActionCmd model) {
/* 110 */     if (model.getBpmInstance() == null) {
/* 111 */       model.setBpmInstance((IBpmInstance)this.bpmInstanceManager.get(model.getInstanceId()));
/*     */     }
/* 113 */     BpmTask bpmTask = new BpmTask();
/* 114 */     bpmTask.setId(model.getTaskId());
/* 115 */     String nodeId = model.getExtendConf().getString("nodeId");
/* 116 */     BpmNodeDef bpmNodeDef = null;
/*     */     
/* 118 */     if (StringUtil.isEmpty(nodeId)) {
/* 119 */       bpmNodeDef = this.bpmProcessDefService.getStartEvent(model.getDefId());
/* 120 */       nodeId = ((BpmNodeDef)bpmNodeDef.getOutcomeTaskNodes().get(0)).getNodeId();
/*     */     } else {
/* 122 */       bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), nodeId);
/*     */     } 
/*     */     
/* 125 */     if (!isDisplay(false, bpmNodeDef, model.getBpmInstance(), (IBpmTask)bpmTask)) {
/* 126 */       throw new BusinessException("操作受限,您没有该操作权限", BpmStatusCode.NO_PERMISSION);
/*     */     }
/* 128 */     List<BpmTask> bpmTasks = this.taskManager.getByInstId(model.getInstanceId());
/* 129 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(model.getTaskId());
/* 130 */     String node = nodeId;
/* 131 */     List<SysIdentity> sysIdentities = model.getBpmIdentity(nodeId);
/* 132 */     DefaultIdentity defaultIdentity = new DefaultIdentity();
/* 133 */     if (CollectionUtils.isNotEmpty(sysIdentities)) {
/* 134 */       SysIdentity sysIdentity = sysIdentities.get(0);
/*     */     } else {
/* 136 */       defaultIdentity = new DefaultIdentity();
/* 137 */       defaultIdentity.setId(bpmTaskOpinion.getApprover());
/* 138 */       defaultIdentity.setName(bpmTaskOpinion.getApproverName());
/* 139 */       defaultIdentity.setOrgId(bpmTaskOpinion.getTaskOrgId());
/* 140 */       defaultIdentity.setType("user");
/*     */     } 
/* 142 */     model.setApproveOrgId(bpmTaskOpinion.getTaskOrgId());
/* 143 */     for (BpmTask task : bpmTasks) {
/* 144 */       if (!StringUtils.equals(task.getTaskType(), TaskType.SIGN.getKey())) {
/* 145 */         List<JmsDTO> notifyMessageList = recallNode(model, (BpmInstance)model.getBpmInstance(), task, node, (SysIdentity)defaultIdentity);
/* 146 */         if (CollectionUtils.isNotEmpty(notifyMessageList)) {
/* 147 */           this.jmsProducer.sendToQueue(notifyMessageList);
/*     */         }
/*     */       } 
/*     */       
/* 151 */       if (StringUtils.equals(task.getTaskType(), TaskType.SIGN.getKey())) {
/* 152 */         removeSignTaskIdentity(task);
/*     */       }
/*     */     } 
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
/*     */ 
/*     */   
/*     */   private List<JmsDTO> recallNode(DefualtTaskActionCmd model, BpmInstance bpmInstance, BpmTask bpmTask, String nodeId, SysIdentity identitys) {
/* 168 */     this.taskManager.recycleTask(bpmTask.getId(), OpinionStatus.RECALL, "用户撤回需要回收该任务");
/* 169 */     DefualtTaskActionCmd defualtTaskActionCmd = new DefualtTaskActionCmd();
/* 170 */     defualtTaskActionCmd.addBpmIdentity(nodeId, identitys);
/* 171 */     defualtTaskActionCmd.setDefId(model.getDefId());
/* 172 */     defualtTaskActionCmd.setBpmInstance((IBpmInstance)bpmInstance);
/* 173 */     defualtTaskActionCmd.setInstanceId(bpmInstance.getId());
/* 174 */     defualtTaskActionCmd.setOpinion(model.getOpinion());
/* 175 */     defualtTaskActionCmd.setDestination(nodeId);
/* 176 */     defualtTaskActionCmd.setDataMode("bo");
/* 177 */     defualtTaskActionCmd.setActionName(ActionType.RECALL.getKey());
/* 178 */     defualtTaskActionCmd.setApproveOrgId(model.getApproveOrgId());
/* 179 */     String[] msgTypes = StringUtils.split(JsonUtil.getString(model.getExtendConf(), "msgType"), ',');
/* 180 */     List<JmsDTO> notifyMessageList = new ArrayList<>();
/* 181 */     if (ArrayUtils.isNotEmpty((Object[])msgTypes)) {
/*     */       
/* 183 */       NotifyMessage message = new NotifyMessage(String.format("[%s]任务撤回提醒", new Object[] { bpmTask.getName() }), model.getOpinion(), ContextUtil.getCurrentUser(), this.taskManager.getAssignUserById(bpmTask));
/* 184 */       for (String type : msgTypes) {
/* 185 */         notifyMessageList.add(new DefaultJmsDTO(type, (Serializable)message));
/*     */       }
/*     */     } 
/* 188 */     BpmFlowTaskData bpmFlowTaskData = new BpmFlowTaskData();
/* 189 */     bpmFlowTaskData.setTask((IBpmTask)bpmTask);
/* 190 */     IBusinessPermission businessPermission = this.bpmRightsFormService.getInstanceFormPermission((BpmFlowData)bpmFlowTaskData, bpmTask.getNodeId(), FormType.PC, false);
/* 191 */     Map<String, IBusinessData> taskBusData = this.bpmBusDataHandle.getInstanceBusData(bpmTask.getInstId(), businessPermission);
/* 192 */     defualtTaskActionCmd.setTaskId(bpmTask.getTaskId());
/* 193 */     defualtTaskActionCmd.setBpmTask((IBpmTask)bpmTask);
/* 194 */     defualtTaskActionCmd.setBizDataMap(taskBusData);
/*     */     try {
/* 196 */       BpmContext.setActionModel((ActionCmd)defualtTaskActionCmd);
/* 197 */       this.actTaskService.completeTask(bpmTask.getTaskId(), new String[] { nodeId });
/*     */     } finally {
/* 199 */       BpmContext.cleanTread();
/*     */     } 
/* 201 */     return notifyMessageList;
/*     */   }
/*     */   
/*     */   private void removeSignTaskIdentity(BpmTask bpmTask) {
/* 205 */     this.identityLinkManager.removeByTaskId(bpmTask.getId());
/* 206 */     this.taskManager.remove(bpmTask.getId());
/* 207 */     this.bpmTaskOpinionManager.removeByTaskId(bpmTask.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 212 */     return ActionType.RECALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 217 */     return 7;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 222 */     return Boolean.valueOf((nodeDef == null || nodeDef.getType() == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 227 */     return "/bpm/task/taskOpinionDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 232 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/* 237 */     if (bpmInstance == null || bpmNodeDef == null) return false;
/*     */ 
/*     */     
/* 240 */     if (bpmInstance.getEndTime() != null || bpmInstance.getIsForbidden().shortValue() == 1 || "forbidden".equals(((DefaultBpmProcessDef)bpmNodeDef.getBpmProcessDef()).getExtProperties().getStatus())) {
/* 241 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 245 */     if (Boolean.FALSE.equals(bpmNodeDef.getNodeProperties().getAllowRecall()))
/* 246 */       return false; 
/* 247 */     if (bpmNodeDef.getNodeProperties().getAllowRecall() == null) {
/*     */       
/* 249 */       DefaultBpmProcessDef defaultBpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(bpmInstance.getDefId());
/* 250 */       if (Boolean.FALSE.equals(Boolean.valueOf(defaultBpmProcessDef.getExtProperties().isAllowRecall()))) {
/* 251 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 255 */     if (NodeType.START.equals(bpmNodeDef.getType())) {
/* 256 */       return false;
/*     */     }
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
/* 268 */     if (bpmTask == null || StringUtil.isEmpty(bpmTask.getId())) {
/* 269 */       return false;
/*     */     }
/* 271 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/*     */     
/* 273 */     if (bpmTaskOpinion == null || StringUtils.contains(bpmTaskOpinion.getStatus(), "sign")) {
/* 274 */       return false;
/*     */     }
/* 276 */     ((BpmTask)bpmTask).setInstId(bpmTaskOpinion.getInstId());
/* 277 */     ((BpmTask)bpmTask).setNodeId(bpmTaskOpinion.getTaskKey());
/*     */     
/* 279 */     List<BpmTask> bpmTasks = this.taskManager.getByInstId(bpmInstance.getId());
/* 280 */     if (CollectionUtils.isEmpty(bpmTasks)) {
/* 281 */       return false;
/*     */     }
/* 283 */     List<IExtendTaskAction> taskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/*     */ 
/*     */     
/* 286 */     for (IExtendTaskAction taskAction : taskActions) {
/* 287 */       if (taskAction.isContainNode(bpmNodeDef.getNodeId(), this.bpmProcessDefService.getBpmProcessDef(bpmInstance.getDefId()), "none")) {
/* 288 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 292 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 293 */     defaultQueryFilter.addParamsFilter("taskId", bpmTask.getId());
/* 294 */     defaultQueryFilter.addFieldSort("level", "asc");
/* 295 */     defaultQueryFilter.addParamsFilter("prior", "FORWARD");
/* 296 */     defaultQueryFilter.addFieldSort("id_", "asc");
/* 297 */     defaultQueryFilter.addFilter("level", Integer.valueOf(1), QueryOP.GREAT);
/* 298 */     defaultQueryFilter.addFilter("level", Integer.valueOf(5), QueryOP.LESS);
/*     */     
/* 300 */     List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/* 301 */     BpmNodeDef parentNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 302 */     SubProcessNodeDef isSubNodeDef = null;
/* 303 */     if (parentNodeDef != null && parentNodeDef instanceof SubProcessNodeDef) {
/* 304 */       isSubNodeDef = (SubProcessNodeDef)parentNodeDef;
/*     */     }
/* 306 */     boolean canCall = false;
/* 307 */     for (BpmTaskStack stack : bpmTaskStacks) {
/* 308 */       for (BpmTask existTask : bpmTasks) {
/* 309 */         if (StringUtils.equals(existTask.getId(), stack.getTaskId())) {
/* 310 */           canCall = true;
/* 311 */           BpmNodeDef eBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), existTask.getNodeId());
/* 312 */           BpmNodeDef eParentNodeDef = eBpmNodeDef.getParentBpmNodeDef();
/* 313 */           if (isSubNodeDef == null && eParentNodeDef != null && eParentNodeDef instanceof SubProcessNodeDef) {
/* 314 */             canCall = false;
/*     */           }
/* 316 */           if (isSubNodeDef != null) {
/* 317 */             if (eParentNodeDef == null || !(eParentNodeDef instanceof SubProcessNodeDef)) {
/* 318 */               canCall = false;
/*     */             }
/* 320 */             if (eParentNodeDef != null && eParentNodeDef instanceof SubProcessNodeDef && 
/* 321 */               !StringUtils.equals(eParentNodeDef.getNodeId(), isSubNodeDef.getNodeId())) {
/* 322 */               canCall = false;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 327 */           if (StringUtils.equals(TaskType.SUPERVISE.getKey(), existTask.getTaskType())) {
/* 328 */             return false;
/*     */           }
/*     */           
/* 331 */           if (StringUtils.equals("SIGN", existTask.getTaskType())) {
/* 332 */             return false;
/*     */           }
/*     */           
/* 335 */           for (IExtendTaskAction taskAction : taskActions) {
/* 336 */             if (!taskAction.canRecall((IBpmTask)existTask)) {
/* 337 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 344 */     return canCall;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceRecallActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */