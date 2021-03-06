/*     */ package com.dstz.bpm.engine.action.handler.instance;
/*     */ 
/*     */ import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.BuiltinActionHandler;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.cmd.ExecutionCommand;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.BeanCopierUtils;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */
import org.apache.commons.lang3.ObjectUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ @Component("instanceEndActionHandler")
/*     */ public class InstanceEndActionHandler
/*     */   implements BuiltinActionHandler<BaseActionCmd>
/*     */ {
/*  44 */   private static final Logger logger = LoggerFactory.getLogger(InstanceEndActionHandler.class);
/*     */   
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   @Resource
/*     */   private ActInstanceService actInstanceService;
/*     */   
/*     */   @Autowired
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   
/*     */   @Autowired
/*     */   private TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   
/*     */   @Autowired
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   
/*     */   @Autowired
/*     */   private ExecutionCommand executionCommand;
/*     */   
/*     */   @Transactional(rollbackFor = {Exception.class})
/*     */   public void execute(BaseActionCmd model) {
/*     */     DefaultInstanceActionCmd defaultInstanceActionCmd;
/*  70 */     BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
/*  71 */     checkInstance((IBpmInstance)bpmInstance);
/*     */     
/*  73 */     if (model instanceof DefualtTaskActionCmd) {
/*  74 */       defaultInstanceActionCmd = convertDefaultInstanceActionCmd((DefualtTaskActionCmd)model, bpmInstance);
/*  75 */       BpmContext.setActionModel((ActionCmd)model);
/*  76 */     } else if (model instanceof DefaultInstanceActionCmd) {
/*  77 */       BpmContext.setActionModel((ActionCmd)getDefaultTaskActionCmd((DefaultInstanceActionCmd)model, bpmInstance));
/*  78 */       defaultInstanceActionCmd = (DefaultInstanceActionCmd)model;
/*     */     } else {
/*  80 */       logger.warn("model not in (com.dstz.bpm.api.exception.BpmStatusCode.NO_TASK_ACTION, com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd)");
/*  81 */       throw new BusinessException(BpmStatusCode.NO_TASK_ACTION);
/*     */     } 
/*     */     try {
/*  84 */       BpmInstance topInstance = (BpmInstance)ObjectUtils.defaultIfNull(this.bpmInstanceManager.getTopInstance(bpmInstance), bpmInstance);
/*  85 */       endInstance(topInstance, defaultInstanceActionCmd);
/*     */     } finally {
/*  87 */       BpmContext.removeActionModel();
/*     */     } 
/*     */   }
/*     */   
/*     */   private DefaultInstanceActionCmd convertDefaultInstanceActionCmd(DefualtTaskActionCmd defualtTaskActionCmd, BpmInstance bpmInstance) {
/*  92 */     defualtTaskActionCmd.setActionName(ActionType.MANUALEND.getKey());
/*  93 */     defualtTaskActionCmd.setBpmInstance((IBpmInstance)bpmInstance);
/*  94 */     return (DefaultInstanceActionCmd)BeanCopierUtils.transformBean(defualtTaskActionCmd, DefaultInstanceActionCmd.class);
/*     */   }
/*     */   
/*     */   private DefualtTaskActionCmd getDefaultTaskActionCmd(DefaultInstanceActionCmd model, BpmInstance bpmInstance) {
/*  98 */     DefualtTaskActionCmd defualtTaskActionCmd = (DefualtTaskActionCmd)BeanCopierUtils.transformBean(model, DefualtTaskActionCmd.class);
/*  99 */     defualtTaskActionCmd.setCurAccount(ContextUtil.getCurrentUserAccount());
/* 100 */     defualtTaskActionCmd.setActionName(ActionType.MANUALEND.getKey());
/* 101 */     defualtTaskActionCmd.setBpmInstance((IBpmInstance)bpmInstance);
/* 102 */     return defualtTaskActionCmd;
/*     */   }
/*     */   
/*     */   private void checkInstance(IBpmInstance bpmInstance) {
/* 106 */     if (bpmInstance == null) {
/* 107 */       throw new BusinessException(BpmStatusCode.INSTANCE_NOT_EXISTS);
/*     */     }
/*     */     
/* 110 */     if (InstanceStatus.STATUS_END.getKey().equals(bpmInstance.getStatus()) || InstanceStatus.STATUS_MANUAL_END.getKey().equals(bpmInstance.getStatus())) {
/* 111 */       throw new BusinessException(BpmStatusCode.INSTANCE_NOT_RUNNING);
/*     */     }
/* 113 */     if (!ContextUtil.currentUserIsAdmin()) {
/* 114 */       throw new BusinessException(BpmStatusCode.NO_PERMISSION);
/*     */     }
/*     */   }
/*     */   
/*     */   private void endInstance(BpmInstance bpmInstance, DefaultInstanceActionCmd actionCmd) {
/* 119 */     logger.debug("???????????? ????????????: {} ????????????: {}", bpmInstance.getId(), bpmInstance.getSubject());
/*     */     
/* 121 */     actionCmd.setInstanceId(bpmInstance.getId());
/* 122 */     actionCmd.setBpmInstance((IBpmInstance)bpmInstance);
/* 123 */     actionCmd.setDefId(bpmInstance.getDefId());
/*     */     
/* 125 */     if (this.actInstanceService.getProcessInstance(bpmInstance.getActInstId()) != null) {
/* 126 */       this.actInstanceService.deleteProcessInstance(bpmInstance.getActInstId(), actionCmd.getOpinion());
/*     */     }
/* 128 */     endTask(bpmInstance, actionCmd);
/*     */     
/* 130 */     this.bpmTaskManager.removeByInstId(bpmInstance.getId());
/* 131 */     this.taskIdentityLinkManager.removeByInstId(bpmInstance.getId());
/* 132 */     bpmInstance.setStatus(InstanceStatus.STATUS_MANUAL_END.getKey());
/* 133 */     bpmInstance.setEndTime(new Date());
/* 134 */     bpmInstance.setDuration(Long.valueOf(bpmInstance.getEndTime().getTime() - bpmInstance.getCreateTime().getTime()));
/* 135 */     this.bpmInstanceManager.update(bpmInstance);
/* 136 */     this.bpmTaskStackManager.updateStackEndByInstId(actionCmd.getInstanceId());
/* 137 */     this.executionCommand.execute(EventType.MANUAL_END, (InstanceActionCmd)actionCmd);
/* 138 */     List<BpmInstance> subBpmInstanceList = this.bpmInstanceManager.getByPId(bpmInstance.getId());
/* 139 */     if (subBpmInstanceList != null && !subBpmInstanceList.isEmpty()) {
/* 140 */       for (BpmInstance subBpmInstance : subBpmInstanceList) {
/* 141 */         endInstance(subBpmInstance, actionCmd);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void endTask(BpmInstance bpmInstance, DefaultInstanceActionCmd actionCmd) {
/* 148 */     List<BpmTaskOpinion> bpmTaskOpinionList = this.bpmTaskOpinionManager.selectByTaskIds(this.bpmTaskManager.selectTaskIdByInstId(bpmInstance.getId()));
/* 149 */     if (bpmTaskOpinionList != null && !bpmTaskOpinionList.isEmpty()) {
/* 150 */       IUser currentUser = ContextUtil.getCurrentUser();
/* 151 */       for (BpmTaskOpinion bpmTaskOpinion : bpmTaskOpinionList) {
/* 152 */         bpmTaskOpinion.setOpinion(actionCmd.getOpinion());
/* 153 */         bpmTaskOpinion.setStatus(ActionType.MANUALEND.getKey());
/* 154 */         bpmTaskOpinion.setApproveTime(new Date());
/* 155 */         if (currentUser != null) {
/* 156 */           bpmTaskOpinion.setApprover(currentUser.getUserId());
/* 157 */           bpmTaskOpinion.setApproverName(currentUser.getFullname());
/*     */         } 
/* 159 */         bpmTaskOpinion.setDurMs(Long.valueOf(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime()));
/* 160 */         this.bpmTaskOpinionManager.update(bpmTaskOpinion);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 167 */     return ActionType.INSTANCE_END;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 172 */     return 7;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 177 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 182 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 187 */     return "/bpm/task/taskOpinionDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 192 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 197 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/* 202 */     if (bpmInstance == null) {
/* 203 */       return false;
/*     */     }
/* 205 */     return (ContextUtil.currentUserIsAdmin() && InstanceStatus.STATUS_RUNNING
/* 206 */       .getKey().equals(bpmInstance.getStatus()) && bpmInstance
/* 207 */       .getIsForbidden().shortValue() != 1);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceEndActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */