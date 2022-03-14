/*    */ package com.dstz.bpm.api.engine.event;
/*    */ 
/*    */ import com.dstz.bpm.api.model.def.IBpmDefinition;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ 
/*    */ public class BpmDefinitionUpdateEvent extends ApplicationEvent {
/*    */   private static final long serialVersionUID = 550560932524738231L;
/*    */   
/*    */   public BpmDefinitionUpdateEvent(IBpmDefinition source) {
/* 10 */     super(source);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/event/BpmDefinitionUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */