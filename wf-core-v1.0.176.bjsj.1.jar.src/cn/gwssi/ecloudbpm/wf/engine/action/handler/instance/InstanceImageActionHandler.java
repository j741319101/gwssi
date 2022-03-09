/*    */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.instance;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.ActionHandler;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class InstanceImageActionHandler
/*    */   implements ActionHandler
/*    */ {
/*    */   public void execute(ActionCmd model) {}
/*    */   
/*    */   public ActionType getActionType() {
/* 20 */     return ActionType.FLOWIMAGE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 25 */     return 6;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 30 */     return Boolean.valueOf(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 35 */     return Boolean.valueOf(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 42 */     return "/bpm/instance/instanceImageDialog.html";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDefaultGroovyScript() {
/* 47 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDefaultBeforeScript() {
/* 52 */     return "";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceImageActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */