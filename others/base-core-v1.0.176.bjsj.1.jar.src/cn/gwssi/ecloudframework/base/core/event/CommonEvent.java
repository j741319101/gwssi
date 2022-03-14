/*    */ package com.dstz.base.core.event;
/*    */ 
/*    */ import com.dstz.base.api.constant.EventEnum;
/*    */ import org.springframework.context.ApplicationEvent;
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
/*    */ public class CommonEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   EventEnum eventEnum;
/*    */   
/*    */   public CommonEvent(EventEnum eventEnum) {
/* 21 */     super("");
/* 22 */     setEventEnum(eventEnum);
/*    */   }
/*    */   
/*    */   public CommonEvent(Object source) {
/* 26 */     super(source);
/*    */   }
/*    */   
/*    */   public CommonEvent(Object source, EventEnum eventEnum) {
/* 30 */     super(source);
/* 31 */     this.eventEnum = eventEnum;
/*    */   }
/*    */   
/*    */   public EventEnum getEventEnum() {
/* 35 */     return this.eventEnum;
/*    */   }
/*    */   
/*    */   public void setEventEnum(EventEnum eventEnum) {
/* 39 */     this.eventEnum = eventEnum;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/event/CommonEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */