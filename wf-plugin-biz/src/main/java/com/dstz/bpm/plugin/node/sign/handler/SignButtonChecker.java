/*    */ package com.dstz.bpm.plugin.node.sign.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.engine.action.button.ButtonChecker;
/*    */ import com.dstz.bpm.api.model.nodedef.Button;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Value;
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
/*    */   @Value("${bpm.plugin.exclude.buttons}")
/*    */   private String excludeButtons;
/* 33 */   private final String supportsType = String.format("%s,%s,%s,%s,%s,", new Object[] { ActionType.AGREE.getKey(), ActionType.OPPOSE.getKey(), ActionType.REJECT.getKey(), ActionType.LOCK.getKey(), ActionType.UNLOCK.getKey() });
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSupport(Button btn) {
/* 38 */     if (this.supportsType.contains(btn.getAlias())) {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void specialBtnHandler(List<Button> btn) {
/* 50 */     ActionType actionType = this.taskSignOpposeActionHandler.getActionType();
/* 51 */     Button signOppose = new Button(actionType.getName(), actionType.getKey(), this.taskSignOpposeActionHandler.getDefaultGroovyScript(), this.taskSignOpposeActionHandler.getConfigPage());
/*    */     
/* 53 */     ActionType agreeAction = this.taskSignAgreeActionHandler.getActionType();
/* 54 */     Button signAgree = new Button(agreeAction.getName(), agreeAction.getKey(), this.taskSignAgreeActionHandler.getDefaultGroovyScript(), this.taskSignAgreeActionHandler.getConfigPage());
/* 55 */     ActionType addSignAction = this.addSignActionHandler.getActionType();
/* 56 */     if (!StringUtils.contains(this.excludeButtons, addSignAction.getKey())) {
/* 57 */       Button addSign = new Button(addSignAction.getName(), addSignAction.getKey(), this.addSignActionHandler.getDefaultGroovyScript(), this.addSignActionHandler.getConfigPage());
/* 58 */       btn.add(addSign);
/*    */     } 
/* 60 */     btn.add(0, signOppose);
/* 61 */     btn.add(0, signAgree);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/sign/handler/SignButtonChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */