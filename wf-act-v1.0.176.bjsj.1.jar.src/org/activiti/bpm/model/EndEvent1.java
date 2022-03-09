/*    */ package org.activiti.bpm.model;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.activiti.bpmn.model.BaseElement;
/*    */ import org.activiti.bpmn.model.Event;
/*    */ import org.activiti.bpmn.model.FlowElement;
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
/*    */ public class EndEvent1
/*    */   extends Event
/*    */   implements Serializable
/*    */ {
/*    */   public EndEvent1 clone() {
/* 26 */     EndEvent1 clone = new EndEvent1();
/* 27 */     clone.setValues(this);
/* 28 */     return clone;
/*    */   }
/*    */   
/*    */   public void setValues(EndEvent1 otherEvent) {
/* 32 */     setValues(otherEvent);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/org/activiti/bpm/model/EndEvent1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */