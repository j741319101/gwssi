/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.BpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.recrease.executer.SuperviseTaskExecuter;
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
/*    */ @Component
/*    */ public class DynamicTaskPluginExecuter
/*    */   extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, DynamicTaskPluginDef>
/*    */ {
/*    */   @Resource
/*    */   private HandleTaskCreateEvent handleTaskCreateEvent;
/*    */   @Resource
/*    */   private HandlePrevDynamicTaskCreate handlePrevDynamicTaskCreate;
/*    */   @Resource
/*    */   private HandleTaskComplateEvent handleTaskComplateEvent;
/*    */   @Resource
/*    */   private DynamicInstTaskAction dynamicInstTaskAction;
/*    */   @Resource
/*    */   private SuperviseTaskExecuter superviseTaskExecuter;
/*    */   
/*    */   public Void execute(DefaultBpmTaskPluginSession pluginSession, DynamicTaskPluginDef pluginDef) {
/* 41 */     EventType eventType = pluginSession.getEventType();
/* 42 */     this.dynamicInstTaskAction.checkEvent(pluginSession);
/* 43 */     if (eventType == EventType.TASK_CREATE_EVENT) {
/* 44 */       this.handleTaskCreateEvent.taskCreateEvent(pluginDef, pluginSession);
/* 45 */     } else if (eventType == EventType.TASK_POST_CREATE_EVENT) {
/* 46 */       this.handleTaskCreateEvent.postTaskCreateEvent(pluginSession);
/*    */     }
/* 48 */     else if (eventType == EventType.TASK_PRE_COMPLETE_EVENT) {
/* 49 */       this.superviseTaskExecuter.clearSupervise();
/* 50 */       this.dynamicInstTaskAction.clearTaskThreadLocal();
/* 51 */       this.dynamicInstTaskAction.clearIdentities();
/* 52 */       this.superviseTaskExecuter.setIsNeedSupervise(Boolean.valueOf(false));
/* 53 */       this.handlePrevDynamicTaskCreate.prevDynamicTaskCreate(pluginSession);
/* 54 */       this.handleTaskComplateEvent.taskComplateEvent(pluginDef, pluginSession);
/*    */     } 
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isParallel(BpmNodeDef bpmNodeDef) {
/* 61 */     AtomicReference<Boolean> isParallel = new AtomicReference<>(Boolean.valueOf(false));
/* 62 */     if (bpmNodeDef == null) {
/* 63 */       return false;
/*    */     }
/*    */     
/* 66 */     bpmNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*    */           if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*    */             isParallel.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsParallel());
/*    */           }
/*    */         });
/*    */     
/* 72 */     if (!((Boolean)isParallel.get()).booleanValue()) {
/* 73 */       BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
/* 74 */       if (subProcessNodeDef != null && subProcessNodeDef instanceof cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.SubProcessNodeDef) {
/* 75 */         subProcessNodeDef.getBpmPluginContexts().forEach(bpmPluginContext -> {
/*    */               if (bpmPluginContext instanceof DynamicTaskPluginContext) {
/*    */                 isParallel.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsParallel());
/*    */               }
/*    */             });
/*    */       }
/*    */     } 
/* 82 */     return ((Boolean)isParallel.get()).booleanValue();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/dynamictask/executer/DynamicTaskPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */