/*    */ package com.dstz.bpm.engine.plugin.cmd;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
/*    */ import com.dstz.bpm.api.engine.plugin.cmd.ExecutionCommand;
/*    */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmExecutionPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*    */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*    */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*    */ import com.dstz.bpm.engine.plugin.factory.BpmPluginFactory;
/*    */ import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
/*    */ import com.dstz.bpm.engine.plugin.runtime.BpmExecutionPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.alibaba.druid.util.StringUtils;
/*    */ import javax.annotation.Resource;
/*    */
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ExecutionPluginCommand
/*    */   implements ExecutionCommand
/*    */ {
/* 31 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*    */   
/*    */   @Resource
/*    */   BpmProcessDefService bpmProcessDefService;
/*    */ 
/*    */   
/*    */   public void execute(EventType eventType, InstanceActionCmd actionModel) {
/* 38 */     this.LOG.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓【{}】插件执行开始↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓", eventType.getValue());
/*    */     
/* 40 */     String defId = actionModel.getDefId();
/* 41 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/*    */     
/* 43 */     this.LOG.trace("============【全局插件】========{}=========", Integer.valueOf(bpmProcessDef.getBpmPluginContexts().size()));
/* 44 */     for (BpmPluginContext bpmPluginContext : bpmProcessDef.getBpmPluginContexts()) {
/* 45 */       executeContext(bpmPluginContext, actionModel, eventType);
/*    */     }
/*    */ 
/*    */     
/* 49 */     BpmNodeDef nodeDef = null;
/* 50 */     if (eventType == EventType.START_POST_EVENT || eventType == EventType.START_EVENT) {
/* 51 */       nodeDef = this.bpmProcessDefService.getStartEvent(defId);
/* 52 */     } else if (eventType == EventType.END_EVENT || eventType == EventType.END_POST_EVENT || eventType == EventType.MANUAL_END) {
/*    */       
/* 54 */       ExecutionEntity executionEntity = ((DefaultInstanceActionCmd)actionModel).getExecutionEntity();
/* 55 */       if (executionEntity == null) {
/* 56 */         nodeDef = this.bpmProcessDefService.getEndEvents(defId).get(0);
/*    */       } else {
/* 58 */         for (BpmNodeDef endEvent : this.bpmProcessDefService.getEndEvents(defId)) {
/* 59 */           if (StringUtils.equals(executionEntity.getActivityId(), endEvent.getNodeId())) {
/* 60 */             nodeDef = endEvent;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 67 */     if (nodeDef != null && nodeDef.getBpmPluginContexts() != null) {
/* 68 */       this.LOG.trace("============【{}插件】========{}=========", eventType.getValue(), Integer.valueOf(bpmProcessDef.getBpmPluginContexts().size()));
/* 69 */       for (BpmPluginContext bpmPluginContext : nodeDef.getBpmPluginContexts()) {
/* 70 */         executeContext(bpmPluginContext, actionModel, eventType);
/*    */       }
/*    */     } 
/*    */     
/* 74 */     this.LOG.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑【{}】插件执行完毕↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑", eventType.getValue());
/*    */   }
/*    */   
/*    */   private void executeContext(BpmPluginContext bpmPluginContext, InstanceActionCmd actionModel, EventType eventType) {
/* 78 */     BpmPluginDef bpmPluginDef = bpmPluginContext.getBpmPluginDef();
/*    */     
/* 80 */     if (bpmPluginDef instanceof BpmExecutionPluginDef) {
/* 81 */       BpmExecutionPluginDef bpmExecutionPluginDef = (BpmExecutionPluginDef)bpmPluginDef;
/* 82 */       BpmExecutionPlugin<BpmExecutionPluginSession, BpmExecutionPluginDef> bpmExecutionPlugin = BpmPluginFactory.buildExecutionPlugin(bpmPluginContext, eventType);
/* 83 */       BpmExecutionPluginSession session = BpmPluginSessionFactory.buildExecutionPluginSession(actionModel, eventType);
/*    */       
/* 85 */       if (bpmExecutionPlugin != null)
/*    */         try {
/* 87 */           bpmExecutionPlugin.execute(session, bpmExecutionPluginDef);
/* 88 */           this.LOG.debug("==>执行插件【{}】", bpmPluginContext.getTitle());
/* 89 */         } catch (Exception e) {
/* 90 */           this.LOG.error("执行插件【{}】出现异常:{}", new Object[] { bpmPluginContext.getTitle(), e.getMessage(), e });
/* 91 */           throw new BusinessException(bpmPluginContext.getTitle(), BpmStatusCode.PLUGIN_ERROR, e);
/*    */         }  
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/cmd/ExecutionPluginCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */