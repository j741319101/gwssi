/*    */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*    */ import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*    */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class DynamicTaskPluginExecuter
/*    */   extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, DynamicTaskPluginDef>
/*    */ {
/*    */   @Resource
/*    */   HandleTaskCreateEvent handleTaskCreateEvent;
/*    */   @Resource
/*    */   HandlePrevDynamicTaskCreate handlePrevDynamicTaskCreate;
/*    */   @Resource
/*    */   HandleTaskComplateEvent handleTaskComplateEvent;
/*    */   
/*    */   public Void execute(DefaultBpmTaskPluginSession pluginSession, DynamicTaskPluginDef pluginDef) {
/* 39 */     EventType eventType = pluginSession.getEventType();
/* 40 */     if (eventType == EventType.TASK_CREATE_EVENT) {
/* 41 */       this.handleTaskCreateEvent.taskCreateEvent(pluginDef, pluginSession);
/* 42 */     } else if (eventType == EventType.TASK_PRE_COMPLETE_EVENT) {
/* 43 */       this.handlePrevDynamicTaskCreate.prevDynamicTaskCreate(pluginSession);
/* 44 */       this.handleTaskComplateEvent.taskComplateEvent(pluginDef, pluginSession);
/* 45 */     } else if (eventType == EventType.TASK_COMPLETE_EVENT) {
/* 46 */       this.handleTaskComplateEvent.checkCompleteEvent(pluginDef, pluginSession);
/*    */     } 
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   static boolean isParallel(BpmNodeDef bpmNodeDef) {
/* 53 */     AtomicReference<Boolean> isParallel = new AtomicReference<>(Boolean.valueOf(false));
/* 54 */     if (bpmNodeDef == null) {
/* 55 */       return false;
/*    */     }
/*    */     
/* 58 */     bpmNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*    */           if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*    */             isParallel.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsParallel());
/*    */           }
/*    */         });
/*    */     
/* 64 */     if (!((Boolean)isParallel.get()).booleanValue()) {
/* 65 */       BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 66 */       if (subProcessNodeDef != null && subProcessNodeDef instanceof com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef) {
/* 67 */         subProcessNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*    */               if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*    */                 isParallel.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsParallel());
/*    */               }
/*    */             });
/*    */       }
/*    */     } 
/* 74 */     return ((Boolean)isParallel.get()).booleanValue();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynamictask/executer/DynamicTaskPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */