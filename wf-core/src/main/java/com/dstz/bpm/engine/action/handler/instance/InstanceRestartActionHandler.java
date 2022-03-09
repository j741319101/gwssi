/*     */ package com.dstz.bpm.engine.action.handler.instance;
/*     */ 
/*     */ import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.BuiltinActionHandler;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.def.IBpmDefinition;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */
import org.apache.commons.lang3.StringUtils;
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
/*     */ @Component("instanceRestartActionHandler")
/*     */ public class InstanceRestartActionHandler
/*     */   implements BuiltinActionHandler<DefaultInstanceActionCmd>
/*     */ {
/*     */   @Autowired
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Autowired
/*     */   private ActInstanceService actInstanceService;
/*     */   @Autowired
/*     */   private BpmDefinitionManager bpmDefinitionManager;
/*     */   @Resource
/*     */   private IBpmBusDataHandle bpmBusDataHandle;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/*  61 */     if (bpmInstance == null || !ContextUtil.currentUserIsAdmin()) {
/*  62 */       return false;
/*     */     }
/*  64 */     if (bpmInstance != null) {
/*  65 */       return false;
/*     */     }
/*  67 */     return canRestart(bpmInstance);
/*     */   }
/*     */   
/*     */   private boolean canRestart(IBpmInstance bpmInstance) {
/*  71 */     return StringUtils.equalsAny(bpmInstance.getStatus(), new CharSequence[] { InstanceStatus.STATUS_END
/*  72 */           .getKey(), InstanceStatus.STATUS_MANUAL_END
/*  73 */           .getKey() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(DefaultInstanceActionCmd model) {
/*  78 */     if (!ContextUtil.currentUserIsAdmin()) {
/*  79 */       throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
/*     */     }
/*  81 */     BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
/*  82 */     if (bpmInstance == null)
/*  83 */       throw new BusinessMessage("操作流程实例不存在"); 
/*  84 */     if (!canRestart((IBpmInstance)bpmInstance)) {
/*  85 */       throw new BusinessMessage("操作流程实例状态已在运行中");
/*     */     }
/*     */     
/*  88 */     String destination = model.getDestination();
/*  89 */     bpmInstance.setActInstId(null);
/*  90 */     model.setBizDataMap(this.bpmBusDataHandle.getInstanceData(null, bpmInstance));
/*  91 */     model.setBpmInstance((IBpmInstance)bpmInstance);
/*  92 */     model.setBpmDefinition((IBpmDefinition)this.bpmDefinitionManager.get(bpmInstance.getDefId()));
/*     */     
/*  94 */     if (StringUtils.isNotEmpty(bpmInstance.getParentInstId())) {
/*     */       
/*  96 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/*  97 */       defaultQueryFilter.addFilter("inst_id_", bpmInstance.getId(), QueryOP.EQUAL);
/*  98 */       defaultQueryFilter.addFilter("node_type_", "endNoneEvent", QueryOP.EQUAL);
/*  99 */       defaultQueryFilter.addFieldSort("id_", "desc");
/* 100 */       List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.query((QueryFilter)defaultQueryFilter);
/* 101 */       if (CollectionUtil.isEmpty(bpmTaskStacks)) {
/* 102 */         throw new BusinessException("重启失败，查询执行栈：instId:" + bpmInstance.getId());
/*     */       }
/* 104 */       BpmTaskStack bpmTaskStack = bpmTaskStacks.get(0);
/* 105 */       bpmTaskStack.setNodeId(bpmInstance.getSuperNodeId());
/* 106 */       model.setExecutionStack((BpmExecutionStack)bpmTaskStack);
/* 107 */       BpmContext.setThreadDynamictaskStack(bpmTaskStack.getNodeId(), (BpmExecutionStack)bpmTaskStack);
/*     */     }  try {
/*     */       String actInstId;
/* 110 */       BpmContext.setActionModel((ActionCmd)model);
/*     */       
/* 112 */       if (StringUtils.isEmpty(destination)) {
/* 113 */         actInstId = this.actInstanceService.startProcessInstance(bpmInstance.getActDefId(), bpmInstance.getBizKey(), model.getActionVariables());
/*     */       } else {
/* 115 */         actInstId = this.actInstanceService.startProcessInstance((IBpmInstance)bpmInstance, model.getActionVariables(), new String[] { destination });
/*     */       } 
/* 117 */       bpmInstance.setActInstId(actInstId);
/* 118 */       bpmInstance.setStatus(InstanceStatus.STATUS_RUNNING.getKey());
/* 119 */       bpmInstance.setDuration(null);
/* 120 */       bpmInstance.setEndTime(null);
/* 121 */       this.bpmInstanceManager.update(bpmInstance);
/*     */     } finally {
/* 123 */       BpmContext.cleanTread();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 129 */     return ActionType.INSTANCE_RESTART;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 134 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 139 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 144 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 149 */     return "/bpm/instance/instanceRestartOpinionDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 159 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceRestartActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */