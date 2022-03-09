/*    */ package com.dstz.bpm.plugin.node.addDo.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.engine.action.button.ButtonChecker;
/*    */ import com.dstz.bpm.api.model.nodedef.Button;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
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
/*    */ @Component("ADD_DOButtonChecker")
/*    */ public class AddDoButtonChecker
/*    */   implements ButtonChecker
/*    */ {
/* 25 */   private final List<String> supportsTypes = CollectionUtil.newArrayList((Object[])new String[] { ActionType.TASKOPINION.getKey(), ActionType.FLOWIMAGE.getKey(), ActionType.SAVE.getKey() });
/*    */   
/*    */   @Value("${bpm.plugin.exclude.buttons}")
/*    */   private String excludeButtons;
/*    */   @Autowired
/*    */   private AddDoAgreeActionHandler addDoAgreeActionHandler;
/*    */   
/*    */   public boolean isSupport(Button btn) {
/* 33 */     return this.supportsTypes.contains(btn.getAlias());
/*    */   }
/*    */ 
/*    */   
/*    */   public void specialBtnHandler(List<Button> btn) {
/* 38 */     ActionType actionType = this.addDoAgreeActionHandler.getActionType();
/* 39 */     if (!StringUtils.contains(this.excludeButtons, actionType.getKey())) {
/* 40 */       Button addDoAgree = new Button(actionType.getName(), actionType.getKey(), this.addDoAgreeActionHandler.getDefaultGroovyScript(), this.addDoAgreeActionHandler.getConfigPage());
/* 41 */       btn.add(0, addDoAgree);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/addDo/handler/AddDoButtonChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */