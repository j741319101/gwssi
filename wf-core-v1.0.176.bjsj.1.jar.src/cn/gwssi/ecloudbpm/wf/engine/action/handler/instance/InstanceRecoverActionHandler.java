/*     */ package com.dstz.bpm.engine.action.handler.instance;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.ActionDisplayHandler;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.cmd.ExecutionCommand;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.collections.CollectionUtils;
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
/*     */ @Component
/*     */ public class InstanceRecoverActionHandler
/*     */   implements ActionDisplayHandler<DefaultInstanceActionCmd>
/*     */ {
/*     */   @Autowired
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Autowired
/*     */   private ActInstanceService actInstanceService;
/*     */   @Autowired
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Autowired
/*     */   private TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   @Autowired
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Autowired
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private ExecutionCommand executionCommand;
/*     */   @Resource
/*     */   IBpmBusDataHandle bpmBusDataHandle;
/*     */   
/*     */   public void execute(DefaultInstanceActionCmd model) {
/*     */     BpmNodeDef bpmNodeDef;
/*  79 */     if (model.getBpmInstance() == null) {
/*  80 */       model.setBpmInstance((IBpmInstance)this.bpmInstanceManager.get(model.getInstanceId()));
/*     */     }
/*  82 */     String currentUserId = ContextUtil.getCurrentUserId();
/*  83 */     if (!currentUserId.equals(model.getBpmInstance().getCreateBy()) && !ContextUtil.currentUserIsAdmin()) {
/*  84 */       throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
/*     */     }
/*  86 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(model.getDefId());
/*  87 */     String nodeId = model.getExtendConf().getString("nodeId");
/*     */     
/*  89 */     if (StringUtils.isNotEmpty(nodeId)) {
/*  90 */       bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), nodeId);
/*     */     } else {
/*  92 */       bpmNodeDef = bpmProcessDef.getStartEvent();
/*     */     } 
/*  94 */     if (!isDisplay(false, bpmNodeDef, model.getBpmInstance(), null)) {
/*  95 */       throw new BusinessException("当前节点不支持撤销操作");
/*     */     }
/*     */     
/*  98 */     if (model.getBpmInstance().getIsForbidden().shortValue() == 1) {
/*  99 */       throw new BusinessMessage("流程实例已经被禁止，请联系管理员", BpmStatusCode.DEF_FORBIDDEN);
/*     */     }
/* 101 */     ThreadMapUtil.put("EcloudBPMDeleteInstance", "");
/* 102 */     handleInstanceInfo(model.getOpinion(), (BpmInstance)model.getBpmInstance(), new Date(), model);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleInstanceInfo(String opinion, BpmInstance bpmInstance, Date endTime, DefaultInstanceActionCmd model) {
/* 108 */     List<BpmInstance> subBpmInstanceList = this.bpmInstanceManager.getByParentId(bpmInstance.getId());
/* 109 */     if (CollectionUtils.isNotEmpty(subBpmInstanceList)) {
/* 110 */       for (BpmInstance subBpmInstance : subBpmInstanceList) {
/* 111 */         handleInstanceInfo(opinion, subBpmInstance, endTime, model);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 116 */     if (this.actInstanceService.getProcessInstance(bpmInstance.getActInstId()) != null) {
/* 117 */       this.actInstanceService.deleteProcessInstance(bpmInstance.getActInstId(), opinion);
/*     */     }
/*     */ 
/*     */     
/* 121 */     List<BpmTask> bpmTaskList = this.bpmTaskManager.getByInstId(bpmInstance.getId());
/* 122 */     for (BpmTask bpmTask : bpmTaskList) {
/* 123 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 124 */       if (bpmTaskOpinion.getApproveTime() == null) {
/* 125 */         bpmTaskOpinion.setDurMs(Long.valueOf(endTime.getTime() - bpmTaskOpinion.getCreateTime().getTime()));
/* 126 */         bpmTaskOpinion.setOpinion(opinion);
/* 127 */         bpmTaskOpinion.setStatus(InstanceStatus.STATUS_REVOKE.getKey());
/* 128 */         bpmTaskOpinion.setTaskOrgId(model.getApproveOrgId());
/* 129 */         this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/*     */       } 
/*     */     } 
/*     */     
/* 133 */     InstanceActionCmd instanceActionCmd = getInstCmd(model, bpmInstance);
/* 134 */     BpmContext.setActionModel((ActionCmd)instanceActionCmd);
/* 135 */     this.executionCommand.execute(EventType.RECOVER_EVENT, instanceActionCmd);
/*     */ 
/*     */     
/* 138 */     this.bpmTaskManager.removeByInstId(bpmInstance.getId());
/*     */     
/* 140 */     this.taskIdentityLinkManager.removeByInstId(bpmInstance.getId());
/*     */     
/* 142 */     this.bpmTaskStackManager.updateStackEndByInstId(model.getInstanceId());
/*     */     
/* 144 */     bpmInstance.setStatus(InstanceStatus.STATUS_REVOKE.getKey());
/* 145 */     bpmInstance.setEndTime(endTime);
/* 146 */     bpmInstance.setDuration(Long.valueOf(endTime.getTime() - bpmInstance.getCreateTime().getTime()));
/* 147 */     this.bpmInstanceManager.update(bpmInstance);
/* 148 */     List<IExtendTaskAction> actions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/*     */     
/* 150 */     actions.forEach(action -> action.revokeInst(bpmInstance.getId()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private InstanceActionCmd getInstCmd(DefaultInstanceActionCmd model, BpmInstance bpmInstance) {
/* 156 */     DefaultInstanceActionCmd instanceActionCmd = new DefaultInstanceActionCmd();
/* 157 */     instanceActionCmd.setActionName(model.getActionName());
/* 158 */     instanceActionCmd.setActionVariables(model.getActionVariables());
/* 159 */     if (CollectionUtil.isEmpty(model.getBizDataMap())) {
/* 160 */       BpmInstance topInstance = this.bpmInstanceManager.getTopInstance(bpmInstance);
/* 161 */       if (topInstance != null) {
/* 162 */         bpmInstance = topInstance;
/*     */       }
/* 164 */       Map<String, IBusinessData> data = this.bpmBusDataHandle.getInstanceBusData(bpmInstance.getId(), null);
/* 165 */       if (CollectionUtil.isNotEmpty(data)) {
/* 166 */         model.setBizDataMap(data);
/*     */       }
/*     */     } 
/* 169 */     instanceActionCmd.setBizDataMap(model.getBizDataMap());
/* 170 */     instanceActionCmd.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(bpmInstance.getDefId()));
/* 171 */     instanceActionCmd.setBpmIdentities(model.getBpmIdentities());
/* 172 */     instanceActionCmd.setDynamicBpmIdentity(model.getDynamicBpmIdentity());
/* 173 */     instanceActionCmd.setBpmInstance((IBpmInstance)bpmInstance);
/* 174 */     instanceActionCmd.setBusData(model.getBusData());
/* 175 */     instanceActionCmd.setBusinessKey(model.getBusinessKey());
/* 176 */     instanceActionCmd.setDataMode(model.getDataMode());
/* 177 */     instanceActionCmd.setDefId(model.getDefId());
/* 178 */     instanceActionCmd.setDestination(model.getDestination());
/* 179 */     instanceActionCmd.setFormId(model.getFormId());
/* 180 */     instanceActionCmd.setApproveOrgId(model.getApproveOrgId());
/* 181 */     return (InstanceActionCmd)instanceActionCmd;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 186 */     return ActionType.RECOVER;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 191 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 196 */     return Boolean.valueOf((nodeDef == null || nodeDef.getType() == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 201 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 206 */     return "/bpm/task/taskOpinionDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 211 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/* 219 */     if (bpmInstance == null) {
/* 220 */       return false;
/*     */     }
/*     */     
/* 223 */     if (StringUtil.isNotZeroEmpty(bpmInstance.getParentInstId())) {
/* 224 */       return false;
/*     */     }
/*     */     
/* 227 */     if (bpmInstance.getEndTime() != null || bpmInstance.getIsForbidden().shortValue() == 1 || "forbidden".equals(((DefaultBpmProcessDef)bpmNodeDef.getBpmProcessDef()).getExtProperties().getStatus())) {
/* 228 */       return false;
/*     */     }
/* 230 */     String currentUserId = ContextUtil.getCurrentUserId();
/* 231 */     if (!currentUserId.equals(bpmInstance.getCreateBy()) && !ContextUtil.currentUserIsAdmin()) {
/* 232 */       return false;
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
/* 254 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 259 */     return "";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceRecoverActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */