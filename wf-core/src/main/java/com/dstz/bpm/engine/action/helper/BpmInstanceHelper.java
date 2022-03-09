/*    */ package com.dstz.bpm.engine.action.helper;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.InstanceStatus;
/*    */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*    */ import java.util.Objects;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BpmInstanceHelper
/*    */ {
/*    */   public static boolean isRunning(IBpmInstance bpmInstance) throws NullPointerException {
/* 28 */     Objects.requireNonNull(bpmInstance);
/* 29 */     if (bpmInstance.getIsForbidden().shortValue() != 0) {
/* 30 */       return false;
/*    */     }
/* 32 */     return (InstanceStatus.STATUS_RUNNING.getKey().equalsIgnoreCase(bpmInstance.getStatus()) || InstanceStatus.STATUS_BACK
/* 33 */       .getKey().equalsIgnoreCase(bpmInstance.getStatus()));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/helper/BpmInstanceHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */