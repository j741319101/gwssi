/*     */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.instance;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.wf.act.service.ActInstanceService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.InstanceStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.InstanceActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.cmd.ExecutionCommand;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.task.AbstractTaskActionHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.data.handle.IBpmBusDataHandle;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.transaction.annotation.Transactional;
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
/*     */ @Component
/*     */ public class InstanceManualEndActionHandler
/*     */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*     */ {
/*     */   @Autowired
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Autowired
/*     */   ActInstanceService actInstanceService;
/*     */   @Autowired
/*     */   BpmTaskManager bpmTaskManager;
/*     */   @Autowired
/*     */   ExecutionCommand executionCommand;
/*     */   @Autowired
/*     */   BpmTaskStackManager bpmTaskStackManager;
/*     */   @Autowired
/*     */   IBpmBusDataHandle bpmBusDataHandle;
/*     */   
/*     */   @Transactional
/*     */   public void execute(DefualtTaskActionCmd model) {
/*  69 */     prepareActionDatas(model);
/*     */ 
/*     */     
/*  72 */     checkFlowIsValid((BaseActionCmd)model);
/*     */ 
/*     */     
/*  75 */     BpmContext.setActionModel((ActionCmd)model);
/*     */     
/*  77 */     handelBusData((BaseActionCmd)model);
/*     */     
/*  79 */     updateOpinionStatus(model);
/*     */ 
/*     */     
/*  82 */     BpmInstance topInstance = this.bpmInstanceManager.getTopInstance((BpmInstance)model.getBpmInstance());
/*  83 */     if (topInstance != null) {
/*  84 */       model.setBpmInstance((IBpmInstance)topInstance);
/*  85 */       model.setDefId(topInstance.getDefId());
/*     */     } 
/*  87 */     handleInstanceInfo(model);
/*     */ 
/*     */     
/*  90 */     BpmContext.removeActionModel();
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/*  95 */     return ActionType.MANUALEND;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 100 */     return 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 105 */     NodeType nodeType = nodeDef.getType();
/*     */     
/* 107 */     if (nodeType == NodeType.USERTASK || nodeType == NodeType.SIGNTASK) {
/* 108 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/* 111 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 116 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 124 */     return "/bpm/task/taskOpinionDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 129 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 134 */     return "";
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
/*     */   private void updateOpinionStatus(DefualtTaskActionCmd actionModel) {
/* 146 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getTaskId());
/* 147 */     if (bpmTaskOpinion == null) {
/*     */       return;
/*     */     }
/*     */     
/* 151 */     bpmTaskOpinion.setStatus(OpinionStatus.getByActionName(actionModel.getActionName()).getKey());
/* 152 */     bpmTaskOpinion.setApproveTime(new Date());
/*     */     
/* 154 */     bpmTaskOpinion.setDurMs(Long.valueOf(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime()));
/* 155 */     bpmTaskOpinion.setOpinion(actionModel.getOpinion());
/*     */     
/* 157 */     IUser user = ContextUtil.getCurrentUser();
/* 158 */     if (user != null) {
/* 159 */       bpmTaskOpinion.setApprover(user.getUserId());
/* 160 */       bpmTaskOpinion.setApproverName(user.getFullname());
/*     */     } 
/*     */     
/* 163 */     this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleInstanceInfo(DefualtTaskActionCmd model) {
/* 173 */     BpmInstance instance = (BpmInstance)model.getBpmInstance();
/*     */     
/* 175 */     if (instance == null) {
/* 176 */       instance = (BpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
/* 177 */       model.setBpmInstance((IBpmInstance)instance);
/*     */     } 
/*     */ 
/*     */     
/* 181 */     if (this.actInstanceService.getProcessInstance(instance.getActInstId()) != null) {
/* 182 */       this.actInstanceService.deleteProcessInstance(instance.getActInstId(), model.getOpinion());
/*     */     }
/*     */ 
/*     */     
/* 186 */     this.bpmTaskManager.removeByInstId(instance.getId());
/*     */     
/* 188 */     this.taskIdentityLinkManager.removeByInstId(instance.getId());
/*     */     
/* 190 */     Date endTime = new Date();
/* 191 */     this.bpmTaskOpinionManager.getByInstId(instance.getId()).stream()
/* 192 */       .filter(bpmTaskOpinion -> (bpmTaskOpinion.getApproveTime() == null))
/* 193 */       .forEach(bpmTaskOpinion -> {
/*     */           bpmTaskOpinion.setStatus(OpinionStatus.MANUAL_END.getKey());
/*     */           
/*     */           bpmTaskOpinion.setDurMs(Long.valueOf(endTime.getTime() - bpmTaskOpinion.getCreateTime().getTime()));
/*     */           bpmTaskOpinion.setOpinion(model.getOpinion());
/*     */           bpmTaskOpinion.setTaskOrgId(model.getApproveOrgId());
/*     */           this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/*     */         });
/* 201 */     instance.setStatus(InstanceStatus.STATUS_MANUAL_END.getKey());
/* 202 */     instance.setEndTime(new Date());
/* 203 */     instance.setDuration(Long.valueOf(instance.getEndTime().getTime() - instance.getCreateTime().getTime()));
/* 204 */     this.bpmInstanceManager.update(instance);
/*     */     
/* 206 */     this.bpmTaskStackManager.updateStackEndByInstId(model.getInstanceId());
/*     */ 
/*     */     
/* 209 */     this.executionCommand.execute(EventType.MANUAL_END, getInstCmd(model));
/*     */ 
/*     */     
/* 212 */     List<BpmInstance> subs = this.bpmInstanceManager.getByPId(instance.getId());
/* 213 */     for (BpmInstance inst : subs) {
/* 214 */       model.setBpmInstance((IBpmInstance)inst);
/* 215 */       model.setDefId(inst.getDefId());
/* 216 */       handleInstanceInfo(model);
/*     */     } 
/*     */   }
/*     */   
/*     */   private InstanceActionCmd getInstCmd(DefualtTaskActionCmd model) {
/* 221 */     DefaultInstanceActionCmd instanceActionCmd = new DefaultInstanceActionCmd();
/* 222 */     instanceActionCmd.setActionName(model.getActionName());
/* 223 */     instanceActionCmd.setActionVariables(model.getActionVariables());
/* 224 */     if (CollectionUtil.isNotEmpty(model.getBizDataMap())) {
/* 225 */       BpmInstance bpmInstance = (BpmInstance)model.getBpmInstance();
/* 226 */       BpmInstance topInstance = this.bpmInstanceManager.getTopInstance(bpmInstance);
/* 227 */       if (topInstance != null) {
/* 228 */         bpmInstance = topInstance;
/*     */       }
/* 230 */       Map<String, IBusinessData> data = this.bpmBusDataHandle.getInstanceBusData(bpmInstance.getId(), null);
/* 231 */       if (CollectionUtil.isNotEmpty(data)) {
/* 232 */         model.setBizDataMap(data);
/*     */       }
/*     */     } 
/* 235 */     instanceActionCmd.setBizDataMap(model.getBizDataMap());
/* 236 */     instanceActionCmd.setBpmDefinition(model.getBpmDefinition());
/* 237 */     instanceActionCmd.setBpmIdentities(model.getBpmIdentities());
/* 238 */     instanceActionCmd.setDynamicBpmIdentity(model.getDynamicBpmIdentity());
/* 239 */     instanceActionCmd.setBpmInstance(model.getBpmInstance());
/* 240 */     instanceActionCmd.setBusData(model.getBusData());
/* 241 */     instanceActionCmd.setBusinessKey(model.getBusinessKey());
/* 242 */     instanceActionCmd.setDataMode(model.getDataMode());
/* 243 */     instanceActionCmd.setDefId(model.getDefId());
/* 244 */     instanceActionCmd.setDestination(model.getDestination());
/* 245 */     instanceActionCmd.setFormId(model.getFormId());
/* 246 */     instanceActionCmd.setApproveOrgId(model.getApproveOrgId());
/* 247 */     return (InstanceActionCmd)instanceActionCmd;
/*     */   }
/*     */   
/*     */   public void doAction(DefualtTaskActionCmd model) {}
/*     */   
/*     */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {}
/*     */   
/*     */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceManualEndActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */