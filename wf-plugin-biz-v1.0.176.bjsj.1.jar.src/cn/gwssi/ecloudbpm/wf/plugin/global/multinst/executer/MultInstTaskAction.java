/*     */ package cn.gwssi.ecloudbpm.wf.plugin.global.multinst.executer;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.DefaultExtendTaskAction;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.global.multinst.context.MultInstPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.global.multinst.def.MultInst;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.global.multinst.def.MultInstPluginDef;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class MultInstTaskAction
/*     */   extends DefaultExtendTaskAction
/*     */ {
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public boolean isContainNode(String nodeId, BpmProcessDef bpmProcessDef, String appendType) {
/*  31 */     MultInstPluginContext pluginContext = (MultInstPluginContext)((DefaultBpmProcessDef)bpmProcessDef).getBpmPluginContext(MultInstPluginContext.class);
/*  32 */     if (pluginContext != null) {
/*  33 */       MultInstPluginDef pluginDef = (MultInstPluginDef)pluginContext.getBpmPluginDef();
/*  34 */       for (MultInst multInst : pluginDef.getMultInsts()) {
/*  35 */         if (StringUtils.equals(appendType, "start")) {
/*  36 */           if (StringUtils.equals(nodeId, multInst.getStartNodeKey())) {
/*  37 */             return true;
/*     */           }
/*  39 */         } else if (StringUtils.equals(appendType, "end")) {
/*  40 */           if (StringUtils.equals(nodeId, multInst.getEndNodeKey())) {
/*  41 */             return true;
/*     */           }
/*  43 */         } else if (StringUtils.equals(appendType, "both") && (
/*  44 */           StringUtils.equals(nodeId, multInst.getEndNodeKey()) || 
/*  45 */           StringUtils.equals(nodeId, multInst.getStartNodeKey()))) {
/*  46 */           return true;
/*     */         } 
/*     */         
/*  49 */         if (multInst.getContainNode().contains(nodeId)) {
/*  50 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseMultInstContainNode(BpmProcessDef bpmProcessDef) {
/*  60 */     MultInstPluginContext pluginContext = (MultInstPluginContext)((DefaultBpmProcessDef)bpmProcessDef).getBpmPluginContext(MultInstPluginContext.class);
/*  61 */     if (pluginContext != null) {
/*  62 */       MultInstPluginDef pluginDef = (MultInstPluginDef)pluginContext.getBpmPluginDef();
/*  63 */       pluginDef.getMultInsts().forEach(multInst -> multInst.parseMultInstContainNode((DefaultBpmProcessDef)bpmProcessDef));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void canFreeJump(IBpmTask bpmTask) {
/*  71 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  72 */     String taskId = model.getTaskId();
/*  73 */     BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
/*  74 */     if (StringUtils.isNotEmpty(opinion.getTrace()) && 
/*  75 */       !isContainNode(model.getDestination(), this.bpmProcessDefService.getBpmProcessDef(model.getBpmDefinition().getId()), "end")) {
/*  76 */       throw new BusinessException("多实例任务暂不适用跳转到多实例外的任务");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultInst getMultInstConfig(String nodeId, BpmProcessDef bpmProcessDef, String appendType) {
/*  84 */     MultInstPluginContext pluginContext = (MultInstPluginContext)((DefaultBpmProcessDef)bpmProcessDef).getBpmPluginContext(MultInstPluginContext.class);
/*  85 */     if (pluginContext != null) {
/*  86 */       MultInstPluginDef pluginDef = (MultInstPluginDef)pluginContext.getBpmPluginDef();
/*  87 */       for (MultInst multInst : pluginDef.getMultInsts()) {
/*  88 */         if (StringUtils.equals(appendType, "start")) {
/*  89 */           if (StringUtils.equals(nodeId, multInst.getStartNodeKey())) {
/*  90 */             return multInst;
/*     */           }
/*  92 */         } else if (StringUtils.equals(appendType, "end")) {
/*  93 */           if (StringUtils.equals(nodeId, multInst.getEndNodeKey())) {
/*  94 */             return multInst;
/*     */           }
/*  96 */         } else if (StringUtils.equals(appendType, "both") && (
/*  97 */           StringUtils.equals(nodeId, multInst.getEndNodeKey()) || 
/*  98 */           StringUtils.equals(nodeId, multInst.getStartNodeKey()))) {
/*  99 */           return multInst;
/*     */         } 
/*     */         
/* 102 */         if (multInst.getContainNode().contains(nodeId)) {
/* 103 */           return multInst;
/*     */         }
/*     */       } 
/*     */     } 
/* 107 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/multinst/executer/MultInstTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */