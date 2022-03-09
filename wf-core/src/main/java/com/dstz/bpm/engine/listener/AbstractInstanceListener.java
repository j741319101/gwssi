/*    */ package com.dstz.bpm.engine.listener;
/*    */ 
/*    */ import com.dstz.bpm.act.listener.ActEventListener;
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.constant.ScriptType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*    */ import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
/*    */ import com.dstz.bpm.api.engine.plugin.cmd.ExecutionCommand;
/*    */ import javax.annotation.Resource;
/*    */ import org.activiti.engine.delegate.event.ActivitiEvent;
/*    */ import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
/*    */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractInstanceListener
/*    */   implements ActEventListener
/*    */ {
/* 21 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Resource
/*    */   private ExecutionCommand executionCommand;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract EventType getBeforeTriggerEventType();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract EventType getAfterTriggerEventType();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract void beforePluginExecute(InstanceActionCmd paramInstanceActionCmd);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract void triggerExecute(InstanceActionCmd paramInstanceActionCmd);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract void afterPluginExecute(InstanceActionCmd paramInstanceActionCmd);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void notify(ActivitiEvent event) {
/* 62 */     ActivitiEntityEventImpl processEvent = (ActivitiEntityEventImpl)event;
/* 63 */     ExecutionEntity excutionEntity = (ExecutionEntity)processEvent.getEntity();
/* 64 */     InstanceActionCmd actionModel = getInstanceActionModel(excutionEntity);
/*    */ 
/*    */ 
/*    */     
/* 68 */     beforePluginExecute(actionModel);
/*    */ 
/*    */     
/* 71 */     if (getBeforeTriggerEventType() != null) {
/* 72 */       this.executionCommand.execute(getBeforeTriggerEventType(), actionModel);
/*    */     }
/*    */ 
/*    */     
/* 76 */     triggerExecute(actionModel);
/*    */ 
/*    */     
/* 79 */     if (getAfterTriggerEventType() != null) {
/* 80 */       this.executionCommand.execute(getAfterTriggerEventType(), actionModel);
/*    */     }
/*    */ 
/*    */     
/* 84 */     afterPluginExecute(actionModel);
/*    */ 
/*    */     
/* 87 */     systemMessage((ActionCmd)actionModel);
/*    */   }
/*    */   
/*    */   public void systemMessage(ActionCmd cmd) {}
/*    */   
/*    */   protected abstract InstanceActionCmd getInstanceActionModel(ExecutionEntity paramExecutionEntity);
/*    */   
/*    */   protected abstract ScriptType getScriptType();
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/AbstractInstanceListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */