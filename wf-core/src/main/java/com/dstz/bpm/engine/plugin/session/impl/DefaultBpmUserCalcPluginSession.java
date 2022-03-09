/*    */ package com.dstz.bpm.engine.plugin.session.impl;
/*    */ 
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ 
/*    */ 
/*    */ public class DefaultBpmUserCalcPluginSession
/*    */   extends DefaultBpmExecutionPluginSession
/*    */   implements BpmUserCalcPluginSession
/*    */ {
/*    */   private static final long serialVersionUID = 1132300282829841447L;
/*    */   private IBpmTask bpmTask;
/* 13 */   private Boolean isPreViewModel = Boolean.valueOf(false);
/*    */   
/*    */   public IBpmTask getBpmTask() {
/* 16 */     return this.bpmTask;
/*    */   }
/*    */   
/*    */   public void setBpmTask(IBpmTask bpmTask) {
/* 20 */     put("bpmTask", bpmTask);
/* 21 */     this.bpmTask = bpmTask;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isPreViewModel() {
/* 26 */     return this.isPreViewModel;
/*    */   }
/*    */   
/*    */   public void setIsPreVrewModel(Boolean isPreViewModel) {
/* 30 */     this.isPreViewModel = isPreViewModel;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/session/impl/DefaultBpmUserCalcPluginSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */