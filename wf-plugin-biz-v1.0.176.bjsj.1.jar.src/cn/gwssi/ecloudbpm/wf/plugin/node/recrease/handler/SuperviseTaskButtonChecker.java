/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.recrease.handler;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.button.ButtonChecker;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.Button;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component("SUPERVISEButtonChecker")
/*    */ public class SuperviseTaskButtonChecker
/*    */   implements ButtonChecker {
/*    */   @Resource
/*    */   private IncreaseTaskHandler increaseTaskHandler;
/* 15 */   private final String supportsType = String.format("%s,%s,%s,%s,%s,%s", new Object[] { ActionType.ADDDO.getKey(), ActionType.ADDSIGN.getKey(), ActionType.REJECT.getKey(), ActionType.LOCK
/* 16 */         .getKey(), ActionType.UNLOCK.getKey(), ActionType.TURN.getKey() });
/*    */ 
/*    */   
/*    */   public boolean isSupport(Button btn) {
/* 20 */     if (this.supportsType.contains(btn.getAlias())) {
/* 21 */       return false;
/*    */     }
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void specialBtnHandler(List<Button> btn) {
/* 28 */     ActionType actionType = this.increaseTaskHandler.getActionType();
/* 29 */     Button increase = new Button(actionType.getName(), actionType.getKey(), this.increaseTaskHandler.getDefaultGroovyScript(), this.increaseTaskHandler.getConfigPage());
/* 30 */     if (btn.contains(increase)) {
/* 31 */       Button agree = new Button("同意", "agree", "", "");
/* 32 */       btn.remove(agree);
/* 33 */       agree.setAlias("oppose");
/* 34 */       btn.remove(agree);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/recrease/handler/SuperviseTaskButtonChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */