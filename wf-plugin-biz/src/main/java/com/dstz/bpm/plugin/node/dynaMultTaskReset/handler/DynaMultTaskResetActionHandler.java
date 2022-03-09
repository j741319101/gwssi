/*     */ package com.dstz.bpm.plugin.node.dynaMultTaskReset.handler;
/*     */ 
/*     */ import com.dstz.base.api.query.Direction;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultFieldSort;
/*     */ import com.dstz.base.db.model.query.DefaultPage;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.ActionDisplayHandler;
/*     */ import com.dstz.bpm.api.engine.action.handler.ActionHandler;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class DynaMultTaskResetActionHandler
/*     */   implements ActionHandler<DefaultInstanceActionCmd>, ActionDisplayHandler<DefaultInstanceActionCmd>
/*     */ {
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private DynamicTaskRessetActoin dynamicTaskRessetActoin;
/*     */   @Resource
/*     */   private MultInstTaskResetAction multInstTaskResetAction;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   
/*     */   public ActionType getActionType() {
/*  54 */     return ActionType.DYNAMULTRESET;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance) {
/*  59 */     if (bpmInstance == null || bpmNodeDef == null) return false;
/*     */ 
/*     */     
/*  62 */     if (bpmInstance.getEndTime() != null || bpmInstance.getIsForbidden().shortValue() == 1 || "forbidden"
/*  63 */       .equals(((DefaultBpmProcessDef)bpmNodeDef.getBpmProcessDef()).getExtProperties().getStatus())) {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  68 */     defaultQueryFilter.addFilter("inst_id_", bpmInstance.getId(), QueryOP.EQUAL);
/*  69 */     defaultQueryFilter.addFilter("node_id_", bpmNodeDef.getNodeId(), QueryOP.EQUAL);
/*  70 */     DefaultPage page = (DefaultPage)defaultQueryFilter.getPage();
/*  71 */     page.getOrders().add(new DefaultFieldSort("approver_name_", Direction.DESC));
/*  72 */     List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByInstIdAndNodeId((QueryFilter)defaultQueryFilter);
/*     */     
/*  74 */     if (CollectionUtils.isNotEmpty(bpmTaskStacks)) {
/*  75 */       String nextNodeId = "";
/*  76 */       for (BpmTaskStack bpmTaskStack : bpmTaskStacks) {
/*  77 */         String nodeId = bpmTaskStack.getNodeId();
/*  78 */         if (StringUtil.isNotEmpty(nextNodeId) && !StringUtils.equals(nextNodeId, nodeId)) {
/*  79 */           return false;
/*     */         }
/*  81 */         nextNodeId = nodeId;
/*     */       } 
/*     */       
/*  84 */       if (StringUtil.isNotEmpty(nextNodeId)) {
/*  85 */         BpmNodeDef nextNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), nextNodeId);
/*  86 */         if (!this.dynamicTaskRessetActoin.isSupport(nextNodeDef).booleanValue() && !this.multInstTaskResetAction.isSupport(nextNodeDef).booleanValue()) {
/*  87 */           return false;
/*     */         }
/*  89 */         StringBuffer sb = new StringBuffer();
/*  90 */         bpmTaskStacks.forEach(bpmTaskStack -> sb.append(bpmTaskStack.getTaskId()).append(","));
/*     */ 
/*     */         
/*  93 */         defaultQueryFilter = new DefaultQueryFilter();
/*  94 */         defaultQueryFilter.addFilter("id_", sb.substring(0, sb.length() - 1), QueryOP.IN);
/*  95 */         List list = this.bpmTaskManager.query((QueryFilter)defaultQueryFilter);
/*  96 */         if (list.size() == bpmTaskStacks.size()) {
/*  97 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(DefaultInstanceActionCmd model) {
/* 106 */     System.out.println();
/* 107 */     model.getExtendConf();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 112 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 117 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 122 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 137 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynaMultTaskReset/handler/DynaMultTaskResetActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */