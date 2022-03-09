/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.sign.handler;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
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


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/sign/handler/TaskSignOpposeActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */