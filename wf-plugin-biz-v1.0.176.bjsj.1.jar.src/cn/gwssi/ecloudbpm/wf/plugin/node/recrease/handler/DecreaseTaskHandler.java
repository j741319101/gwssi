/*     */ package cn.gwssi.ecloudbpm.wf.plugin.node.recrease.handler;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.BuiltinActionHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.context.SignTaskPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.hutool.core.bean.BeanUtil;
/*     */ import cn.hutool.core.bean.copier.CopyOptions;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class DecreaseTaskHandler
/*     */   implements BuiltinActionHandler<DefualtTaskActionCmd>
/*     */ {
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   private RecreaseSignTaskExecuter recreaseSignTaskExecuter;
/*     */   @Resource
/*     */   private RecreaseDynamicTaskExecuter recreaseDynamicTaskExecuter;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private RecreaseTaskAction recreaseTaskAction;
/*     */   
/*     */   @Transactional
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/*  54 */     if (bpmInstance == null || bpmTask == null || StringUtils.isEmpty(bpmInstance.getId()) || StringUtils.isEmpty(bpmTask.getId())) {
/*  55 */       return false;
/*     */     }
/*  57 */     JSONObject result = this.recreaseTaskAction.getExistAliveTask(bpmInstance, bpmTask);
/*  58 */     return result.getBoolean("existAliveTask").booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(DefualtTaskActionCmd model) {
/*  63 */     JSONObject extendConf = model.getExtendConf();
/*  64 */     JSONObject dynamicNodes = extendConf.getJSONObject("dynamicNodes");
/*  65 */     String taskId = model.getTaskId();
/*  66 */     String instId = model.getInstanceId();
/*  67 */     BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(instId);
/*  68 */     model.setBpmInstance((IBpmInstance)bpmInstance);
/*  69 */     if (dynamicNodes != null && !dynamicNodes.isEmpty()) {
/*  70 */       for (String dynamicNodeId : dynamicNodes.keySet()) {
/*  71 */         DefualtTaskActionCmd execCmd = new DefualtTaskActionCmd();
/*  72 */         CopyOptions copyOptions = new CopyOptions();
/*  73 */         copyOptions.setIgnoreError(true);
/*  74 */         BeanUtil.copyProperties(model, execCmd, copyOptions);
/*  75 */         BpmContext.cleanTread();
/*  76 */         String callBackId = dynamicNodes.getString(dynamicNodeId);
/*  77 */         String nodeId = dynamicNodeId;
/*  78 */         if (StringUtils.indexOf(nodeId, "&") > -1) {
/*  79 */           String[] pgIds = dynamicNodeId.split("&");
/*  80 */           nodeId = pgIds[0];
/*     */         } 
/*  82 */         extendConf.put("callBackId", callBackId);
/*  83 */         BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), nodeId);
/*  84 */         BpmNodeDef parentNodeDef = bpmNodeDef.getParentBpmNodeDef();
/*  85 */         SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)((SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class)).getBpmPluginDef();
/*  86 */         DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
/*  87 */         if (parentNodeDef != null && parentNodeDef instanceof cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.SubProcessNodeDef) {
/*  88 */           dynamicTaskPluginDef = (DynamicTaskPluginDef)((DynamicTaskPluginContext)parentNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
/*     */         }
/*  90 */         if (signTaskPluginDef.isSignMultiTask()) {
/*  91 */           DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/*  92 */           defaultQueryFilter.addFilter("level", Integer.valueOf(3), QueryOP.EQUAL);
/*  93 */           defaultQueryFilter.addFilter("node_id_", nodeId, QueryOP.EQUAL);
/*  94 */           defaultQueryFilter.addParamsFilter("taskId", taskId);
/*  95 */           defaultQueryFilter.addParamsFilter("prior", "FORWARD");
/*  96 */           defaultQueryFilter.addFieldSort("level", "asc");
/*  97 */           List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/*  98 */           execCmd.setTaskId(((BpmTaskStack)bpmTaskStacks.get(0)).getTaskId());
/*  99 */           this.recreaseSignTaskExecuter.decrease(execCmd); continue;
/* 100 */         }  if (dynamicTaskPluginDef.getEnabled().booleanValue()) {
/* 101 */           this.recreaseDynamicTaskExecuter.decrease(execCmd);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 110 */     return ActionType.DECREASEDYNAMIC;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 115 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 120 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 125 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 140 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/recrease/handler/DecreaseTaskHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */