/*    */ package com.dstz.bpm.plugin.node.sign.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component("taskSignOpposeActionHandler")
/*    */ public class TaskSignOpposeActionHandler
/*    */   extends TaskSignAgreeActionHandler
/*    */ {
/*    */   public ActionType getActionType() {
/* 15 */     return ActionType.SIGNOPPOSE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 20 */     return 2;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/sign/handler/TaskSignOpposeActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */