/*     */ package com.dstz.bpm.engine.action.handler.task;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.OpinionStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.BuiltinActionHandler;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.action.helper.BpmInstanceHelper;
/*     */ import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */
import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component("taskFreeJumpActionHandler")
/*     */ public class TaskFreeJumpActionHandler
/*     */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*     */   implements BuiltinActionHandler<DefualtTaskActionCmd>
/*     */ {
/*  37 */   private static final Logger logger = LoggerFactory.getLogger(TaskFreeJumpActionHandler.class);
/*     */   
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private IBpmBusDataHandle bpmBusDataHandle;
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {
/*  47 */     String destination = actionModel.getDestination();
/*  48 */     if (StringUtils.isEmpty(destination)) {
/*  49 */       throw new BusinessException("?????????????????????");
/*     */     }
/*  51 */     List<IExtendTaskAction> actions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/*  52 */     actions.forEach(action -> action.canFreeJump(actionModel.getBpmTask()));
/*     */ 
/*     */     
/*  55 */     BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(actionModel.getInstanceId());
/*  56 */     if (bpmInstance == null) {
/*  57 */       throw new BusinessException("???????????????????????????instanceId");
/*     */     }
/*  59 */     bpmInstance.setId(actionModel.getInstanceId());
/*  60 */     actionModel.setBizDataMap(this.bpmBusDataHandle.getInstanceData(null, bpmInstance));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {
/*  65 */     this.bpmTaskOpinionManager.commonUpdate(actionModel.getTaskId(), OpinionStatus.TASK_FREE_JUMP, actionModel.getOpinion());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/*  71 */     return ActionType.TASK_FREE_JUMP;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/*  76 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/*  81 */     return "/bpm/task/freeJumpActionDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/*  86 */     if (bpmInstance == null || readOnly) {
/*  87 */       return false;
/*     */     }
/*  89 */     return (ContextUtil.currentUserIsAdmin() && 
/*  90 */       BpmInstanceHelper.isRunning(bpmInstance) && bpmInstance
/*  91 */       .getIsForbidden().shortValue() != 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/*  96 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 101 */     return Boolean.valueOf(false);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/task/TaskFreeJumpActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */