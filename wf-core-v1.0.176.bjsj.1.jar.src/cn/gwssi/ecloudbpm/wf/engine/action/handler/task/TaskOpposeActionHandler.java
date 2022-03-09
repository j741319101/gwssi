/*    */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.task;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class TaskOpposeActionHandler
/*    */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*    */ {
/*    */   public ActionType getActionType() {
/* 19 */     return ActionType.OPPOSE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {
/* 30 */     taskComplatePrePluginExecute(actionModel);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 35 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 40 */     NodeType nodeType = nodeDef.getType();
/*    */     
/* 42 */     if (nodeType == NodeType.USERTASK || nodeType == NodeType.SIGNTASK) {
/* 43 */       return Boolean.valueOf(true);
/*    */     }
/*    */     
/* 46 */     return Boolean.valueOf(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 51 */     return "/bpm/task/taskOpinionDialog.html";
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 56 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/task/TaskOpposeActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */