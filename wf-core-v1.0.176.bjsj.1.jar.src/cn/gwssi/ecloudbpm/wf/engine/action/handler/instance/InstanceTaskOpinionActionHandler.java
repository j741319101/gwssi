/*    */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.instance;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.ActionHandler;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class InstanceTaskOpinionActionHandler
/*    */   implements ActionHandler
/*    */ {
/*    */   public void execute(ActionCmd model) {}
/*    */   
/*    */   public ActionType getActionType() {
/* 20 */     return ActionType.TASKOPINION;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 25 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 30 */     if (nodeDef.getType() == NodeType.START) return Boolean.valueOf(false);
/*    */     
/* 32 */     return Boolean.valueOf(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 37 */     return Boolean.valueOf(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 44 */     return "/bpm/instance/taskOpinionHistoryDialog.html";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDefaultGroovyScript() {
/* 49 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDefaultBeforeScript() {
/* 54 */     return "";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceTaskOpinionActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */