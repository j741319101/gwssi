/*    */ package cn.gwssi.ecloudbpm.wf.engine.listener;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.act.listener.ActEventListener;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ScriptType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.cmd.TaskCommand;
/*    */ import javax.annotation.Resource;
/*    */ import org.activiti.engine.delegate.event.ActivitiEntityEvent;
/*    */ import org.activiti.engine.delegate.event.ActivitiEvent;
/*    */ import org.activiti.engine.impl.persistence.entity.TaskEntity;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractTaskListener<T extends TaskActionCmd>
/*    */   implements ActEventListener
/*    */ {
/* 22 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Resource
/*    */   protected TaskCommand taskCommand;
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
/*    */   public abstract void beforePluginExecute(T paramT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract void triggerExecute(T paramT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract void afterPluginExecute(T paramT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void notify(ActivitiEvent event) {
/* 65 */     ActivitiEntityEvent entityEvent = (ActivitiEntityEvent)event;
/* 66 */     TaskEntity taskEntity = (TaskEntity)entityEvent.getEntity();
/*    */     
/* 68 */     T model = getActionModel(taskEntity);
/*    */ 
/*    */     
/* 71 */     beforePluginExecute(model);
/*    */ 
/*    */ 
/*    */     
/* 75 */     if (getBeforeTriggerEventType() != null) {
/* 76 */       this.taskCommand.execute(getBeforeTriggerEventType(), (TaskActionCmd)model);
/*    */     }
/*    */ 
/*    */     
/* 80 */     triggerExecute(model);
/*    */ 
/*    */ 
/*    */     
/* 84 */     if (getAfterTriggerEventType() != null) {
/* 85 */       this.taskCommand.execute(getAfterTriggerEventType(), (TaskActionCmd)model);
/*    */     }
/*    */ 
/*    */     
/* 89 */     afterPluginExecute(model);
/*    */     
/* 91 */     systemMessage((ActionCmd)model);
/*    */   }
/*    */   
/*    */   public void systemMessage(ActionCmd cmd) {}
/*    */   
/*    */   protected abstract ScriptType getScriptType();
/*    */   
/*    */   public abstract T getActionModel(TaskEntity paramTaskEntity);
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/AbstractTaskListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */