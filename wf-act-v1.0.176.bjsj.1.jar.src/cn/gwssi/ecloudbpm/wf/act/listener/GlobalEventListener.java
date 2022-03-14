/*    */ package com.dstz.bpm.act.listener;
/*    */ 
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.activiti.engine.delegate.event.ActivitiEvent;
/*    */ import org.activiti.engine.delegate.event.ActivitiEventListener;
/*    */ import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GlobalEventListener
/*    */   implements ActivitiEventListener
/*    */ {
/* 20 */   private final Logger logger = LoggerFactory.getLogger(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   private Map<String, String> handlers = new HashMap<>();
/*    */ 
/*    */   
/*    */   public void onEvent(ActivitiEvent event) {
/* 29 */     String eventType = event.getType().name();
/*    */     
/* 31 */     String eventHandlerBeanId = this.handlers.get(eventType);
/* 32 */     if (eventHandlerBeanId != null) {
/* 33 */       ActEventListener handler = (ActEventListener)AppUtil.getBean(eventHandlerBeanId);
/* 34 */       ActivitiEventImpl e = (ActivitiEventImpl)event;
/* 35 */       handler.notify((ActivitiEvent)e);
/*    */     } else {
/* 37 */       this.logger.debug("eventListener:{} skiped" + eventType);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFailOnException() {
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   public Map<String, String> getHandlers() {
/* 47 */     return this.handlers;
/*    */   }
/*    */   
/*    */   public void setHandlers(Map<String, String> handlers) {
/* 51 */     this.handlers = handlers;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/listener/GlobalEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */