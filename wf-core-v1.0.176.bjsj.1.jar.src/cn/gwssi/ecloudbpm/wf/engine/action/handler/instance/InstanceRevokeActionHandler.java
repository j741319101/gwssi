/*     */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.instance;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.act.service.ActInstanceService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.InstanceStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.ActionDisplayHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.TaskIdentityLinkManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.ThreadMapUtil;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ public class InstanceRevokeActionHandler
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
/*     */   
/*     */   public void execute(DefaultInstanceActionCmd model) {
/*     */     BpmNodeDef bpmNodeDef;
/*  65 */     if (model.getBpmInstance() == null) {
/*  66 */       model.setBpmInstance((IBpmInstance)this.bpmInstanceManager.get(model.getInstanceId()));
/*     */     }
/*  68 */     String currentUserId = ContextUtil.getCurrentUserId();
/*  69 */     if (!currentUserId.equals(model.getBpmInstance().getCreateBy())) {
/*  70 */       throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
/*     */     }
/*  72 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(model.getDefId());
/*  73 */     String nodeId = model.getExtendConf().getString("nodeId");
/*     */     
/*  75 */     if (StringUtils.isNotEmpty(nodeId)) {
/*  76 */       bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), nodeId);
/*     */     } else {
/*  78 */       bpmNodeDef = bpmProcessDef.getStartEvent();
/*     */     } 
/*  80 */     if (!isDisplay(false, bpmNodeDef, model.getBpmInstance(), null)) {
/*  81 */       throw new BusinessException("当前节点不支持撤销操作");
/*     */     }
/*     */     
/*  84 */     if (model.getBpmInstance().getIsForbidden().shortValue() == 1) {
/*  85 */       throw new BusinessMessage("流程实例已经被禁止，请联系管理员", BpmStatusCode.DEF_FORBIDDEN);
/*     */     }
/*  87 */     ThreadMapUtil.put("EcloudBPMDeleteInstance", "");
/*  88 */     handleInstanceInfo(model.getOpinion(), (BpmInstance)model.getBpmInstance(), new Date());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleInstanceInfo(String opinion, BpmInstance bpmInstance, Date endTime) {
/*  94 */     List<BpmInstance> subBpmInstanceList = this.bpmInstanceManager.getByParentId(bpmInstance.getId());
/*  95 */     if (CollectionUtils.isNotEmpty(subBpmInstanceList)) {
/*  96 */       for (BpmInstance subBpmInstance : subBpmInstanceList) {
/*  97 */         handleInstanceInfo(opinion, subBpmInstance, endTime);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 102 */     if (this.actInstanceService.getProcessInstance(bpmInstance.getActInstId()) != null) {
/* 103 */       this.actInstanceService.deleteProcessInstance(bpmInstance.getActInstId(), opinion);
/*     */     }
/*     */ 
/*     */     
/* 107 */     List<BpmTask> bpmTaskList = this.bpmTaskManager.getByInstId(bpmInstance.getId());
/* 108 */     for (BpmTask bpmTask : bpmTaskList) {
/* 109 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 110 */       bpmTaskOpinion.setDurMs(Long.valueOf(endTime.getTime() - bpmTaskOpinion.getCreateTime().getTime()));
/* 111 */       bpmTaskOpinion.setOpinion(opinion);
/* 112 */       bpmTaskOpinion.setStatus(InstanceStatus.STATUS_REVOKE.getKey());
/* 113 */       bpmTaskOpinion.setApprover(ContextUtil.getCurrentUserId());
/* 114 */       bpmTaskOpinion.setApproverName(ContextUtil.getCurrentUserName());
/* 115 */       this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/*     */     } 
/*     */ 
/*     */     
/* 119 */     this.bpmTaskManager.removeByInstId(bpmInstance.getId());
/*     */     
/* 121 */     this.taskIdentityLinkManager.removeByInstId(bpmInstance.getId());
/*     */     
/* 123 */     this.bpmTaskStackManager.updateStackEndByInstId(bpmInstance.getId());
/*     */     
/* 125 */     bpmInstance.setStatus(InstanceStatus.STATUS_REVOKE.getKey());
/* 126 */     bpmInstance.setEndTime(endTime);
/* 127 */     bpmInstance.setDuration(Long.valueOf(endTime.getTime() - bpmInstance.getCreateTime().getTime()));
/* 128 */     this.bpmInstanceManager.update(bpmInstance);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 134 */     return ActionType.RECOVER;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 139 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 144 */     return Boolean.valueOf((nodeDef == null || nodeDef.getType() == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 149 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 154 */     return "/bpm/task/taskOpinionDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 159 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/* 167 */     if (bpmInstance == null) {
/* 168 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 172 */     if (bpmInstance.getEndTime() != null || bpmInstance.getIsForbidden().shortValue() == 1 || "forbidden".equals(((DefaultBpmProcessDef)bpmNodeDef.getBpmProcessDef()).getExtProperties().getStatus())) {
/* 173 */       return false;
/*     */     }
/* 175 */     String currentUserId = ContextUtil.getCurrentUserId();
/* 176 */     if (!currentUserId.equals(bpmInstance.getCreateBy())) {
/* 177 */       return false;
/*     */     }
/*     */     
/* 180 */     if (!NodeType.START.equals(bpmNodeDef.getType()) && CollectionUtils.isEmpty(bpmNodeDef.getIncomeNodes())) {
/* 181 */       return false;
/*     */     }
/* 183 */     BpmNodeDef startNodeDef = bpmNodeDef;
/* 184 */     if (!NodeType.START.equals(bpmNodeDef.getType())) {
/* 185 */       startNodeDef = bpmNodeDef.getIncomeNodes().get(0);
/*     */     }
/* 187 */     if (!NodeType.START.equals(startNodeDef.getType()) || Boolean.FALSE.equals(startNodeDef.getNodeProperties().getAllowRevocation())) {
/* 188 */       return false;
/*     */     }
/*     */     
/* 191 */     String allowRevocationPreNodeId = bpmNodeDef.getNodeProperties().getAllowRevocationPreNodeId();
/* 192 */     if (StringUtil.isEmpty(allowRevocationPreNodeId)) {
/* 193 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 197 */     List opinion = this.bpmTaskOpinionManager.getByInstAndNode(bpmInstance.getId(), allowRevocationPreNodeId);
/* 198 */     return CollectionUtil.isEmpty(opinion);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 203 */     return "";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceRevokeActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */