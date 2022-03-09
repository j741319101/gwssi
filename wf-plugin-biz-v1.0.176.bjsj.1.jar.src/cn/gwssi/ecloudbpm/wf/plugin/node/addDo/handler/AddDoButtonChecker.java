/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.addDo.handler;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.button.ButtonChecker;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.Button;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
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
/*    */ @Component("ADD_DOButtonChecker")
/*    */ public class AddDoButtonChecker
/*    */   implements ButtonChecker
/*    */ {
/* 25 */   private final List<String> supportsTypes = CollectionUtil.newArrayList((Object[])new String[] { ActionType.TASKOPINION.getKey(), ActionType.FLOWIMAGE.getKey(), ActionType.SAVE.getKey() });
/*    */   
/*    */   @Autowired
/*    */   private AddDoAgreeActionHandler addDoAgreeActionHandler;
/*    */   
/*    */   public boolean isSupport(Button btn) {
/* 31 */     return this.supportsTypes.contains(btn.getAlias());
/*    */   }
/*    */ 
/*    */   
/*    */   public void specialBtnHandler(List<Button> btn) {
/* 36 */     ActionType actionType = this.addDoAgreeActionHandler.getActionType();
/* 37 */     if (!StringUtils.contains("addSign", actionType.getKey())) {
/* 38 */       Button addDoAgree = new Button(actionType.getName(), actionType.getKey(), this.addDoAgreeActionHandler.getDefaultGroovyScript(), this.addDoAgreeActionHandler.getConfigPage());
/* 39 */       btn.add(0, addDoAgree);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/addDo/handler/AddDoButtonChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */