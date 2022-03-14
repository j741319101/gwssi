/*    */ package com.dstz.bpm.plugin.node.sign.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.engine.action.button.ButtonChecker;
/*    */ import com.dstz.bpm.api.model.nodedef.Button;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ @Component("SIGNButtonChecker")
/*    */ public class SignButtonChecker
/*    */   implements ButtonChecker
/*    */ {
/*    */   @Resource(name = "taskSignOpposeActionHandler")
/*    */   TaskSignOpposeActionHandler taskSignOpposeActionHandler;
/*    */   @Resource(name = "taskSignAgreeActionHandler")
/*    */   TaskSignAgreeActionHandler taskSignAgreeActionHandler;
/*    */   @Resource
/*    */   AddSignActionHandler addSignActionHandler;
/* 30 */   private final String supportsType = String.format("%s,%s,%s,%s,%s,%s,%s", new Object[] { ActionType.AGREE.getKey(), ActionType.OPPOSE.getKey(), ActionType.REJECT.getKey(), ActionType.LOCK
/* 31 */         .getKey(), ActionType.UNLOCK.getKey(), ActionType.RECALL.getKey(), ActionType.RECALL.getKey() });
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSupport(Button btn) {
/* 36 */     if (this.supportsType.contains(btn.getAlias())) {
/* 37 */       return false;
/*    */     }
/*    */     
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void specialBtnHandler(List<Button> btn) {
/* 47 */     ActionType actionType = this.taskSignOpposeActionHandler.getActionType();
/* 48 */     Button signOppose = new Button(actionType.getName(), actionType.getKey(), this.taskSignOpposeActionHandler.getDefaultGroovyScript(), this.taskSignOpposeActionHandler.getConfigPage());
/*    */     
/* 50 */     ActionType agreeAction = this.taskSignAgreeActionHandler.getActionType();
/* 51 */     Button signAgree = new Button(agreeAction.getName(), agreeAction.getKey(), this.taskSignAgreeActionHandler.getDefaultGroovyScript(), this.taskSignAgreeActionHandler.getConfigPage());
/* 52 */     ActionType addSignAction = this.addSignActionHandler.getActionType();
/* 53 */     if (!StringUtils.contains("addSign", addSignAction.getKey())) {
/* 54 */       Button addSign = new Button(addSignAction.getName(), addSignAction.getKey(), this.addSignActionHandler.getDefaultGroovyScript(), this.addSignActionHandler.getConfigPage());
/* 55 */       btn.add(addSign);
/*    */     } 
/* 57 */     btn.add(0, signOppose);
/* 58 */     btn.add(0, signAgree);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/sign/handler/SignButtonChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */