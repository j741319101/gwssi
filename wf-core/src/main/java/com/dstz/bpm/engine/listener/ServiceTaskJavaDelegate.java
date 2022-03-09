/*     */ package com.dstz.bpm.engine.listener;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.engine.plugin.factory.BpmPluginFactory;
/*     */ import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
/*     */ import com.dstz.bpm.engine.plugin.runtime.BpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import javax.annotation.Resource;
/*     */
import org.activiti.engine.delegate.DelegateExecution;
/*     */ import org.activiti.engine.delegate.JavaDelegate;
/*     */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class ServiceTaskJavaDelegate
/*     */   implements JavaDelegate
/*     */ {
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*  39 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(DelegateExecution execution) throws Exception {
/*  44 */     ExecutionEntity entity = (ExecutionEntity)execution;
/*  45 */     String nodeId = entity.getActivityId();
/*     */     
/*  47 */     ActionCmd model = BpmContext.getActionModel();
/*     */     
/*  49 */     DefaultInstanceActionCmd actionCmd = new DefaultInstanceActionCmd();
/*  50 */     actionCmd.setActionName(model.getActionName());
/*  51 */     actionCmd.setBizDataMap(model.getBizDataMap());
/*  52 */     actionCmd.setBpmInstance(model.getBpmInstance());
/*  53 */     actionCmd.setExecutionEntity(entity);
/*     */     
/*  55 */     BpmContext.setActionModel((ActionCmd)actionCmd);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     executeNodePlugins(nodeId, actionCmd, EventType.TASK_COMPLETE_EVENT);
/*     */     
/*  63 */     BpmContext.removeActionModel();
/*     */   }
/*     */ 
/*     */   
/*     */   private void executeNodePlugins(String nodeId, DefaultInstanceActionCmd model, EventType eventType) {
/*  68 */     BpmExecutionPluginSession taskCreatePluginSession = BpmPluginSessionFactory.buildExecutionPluginSession((InstanceActionCmd)model, eventType);
/*  69 */     IBpmInstance instance = model.getBpmInstance();
/*     */     
/*  71 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(instance.getDefId(), nodeId);
/*     */ 
/*     */     
/*  74 */     this.LOG.trace("============【ServiceTask 节点插件】========{}=========", Integer.valueOf(bpmNodeDef.getBpmPluginContexts().size()));
/*  75 */     for (BpmPluginContext bpmPluginContext : bpmNodeDef.getBpmPluginContexts()) {
/*  76 */       BpmPluginDef bpmPluginDef = bpmPluginContext.getBpmPluginDef();
/*  77 */       if (bpmPluginDef instanceof com.dstz.bpm.api.engine.plugin.def.BpmExecutionPluginDef) {
/*     */         
/*  79 */         BpmExecutionPlugin bpmTaskPlugin = BpmPluginFactory.buildExecutionPlugin(bpmPluginContext, eventType);
/*  80 */         if (bpmTaskPlugin == null)
/*     */           continue;  try {
/*  82 */           this.LOG.debug("==>执行服务节点插件【{}】", bpmPluginContext.getTitle());
/*  83 */           bpmTaskPlugin.execute(taskCreatePluginSession, bpmPluginDef);
/*  84 */         } catch (Exception e) {
/*  85 */           this.LOG.error("{}执行任务插件【{}】出现异常:{}", new Object[] { nodeId, bpmPluginContext.getTitle(), e.getMessage(), e });
/*  86 */           throw new BusinessException(bpmPluginContext.getTitle(), BpmStatusCode.PLUGIN_ERROR, e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  93 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/*     */     
/*  95 */     this.LOG.trace("============【全局插件】========{}=========", Integer.valueOf(bpmProcessDef.getBpmPluginContexts().size()));
/*  96 */     for (BpmPluginContext globalCtx : bpmProcessDef.getBpmPluginContexts()) {
/*  97 */       BpmExecutionPlugin bpmExecutionPlugin = BpmPluginFactory.buildExecutionPlugin(globalCtx, eventType);
/*  98 */       if (bpmExecutionPlugin != null) {
/*     */         try {
/* 100 */           this.LOG.debug("==>【{}】节点执行全局插件【{}】", bpmNodeDef.getName(), globalCtx.getTitle());
/* 101 */           bpmExecutionPlugin.execute(taskCreatePluginSession, globalCtx.getBpmPluginDef());
/* 102 */         } catch (BusinessMessage e) {
/* 103 */           throw e;
/* 104 */         } catch (Exception e) {
/* 105 */           this.LOG.error("【{}】节点执行全局插件【{}】出现异常:{}", new Object[] { bpmNodeDef.getName(), globalCtx.getTitle(), e.getMessage(), e });
/* 106 */           throw new BusinessException(globalCtx.getTitle(), BpmStatusCode.PLUGIN_ERROR, e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 111 */     this.LOG.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑【{}】插件执行完毕↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑", eventType.getValue());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/ServiceTaskJavaDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */