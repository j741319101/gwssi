/*    */ package com.dstz.bpm.plugin.global.leaderTask.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.engine.action.button.ButtonUserChecker;
/*    */ import com.dstz.bpm.api.model.nodedef.Button;
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component("LeaderButtonChecker")
/*    */ public class LeaderButtonChecker
/*    */   implements ButtonUserChecker
/*    */ {
/* 20 */   private final List<String> supportsLeaderTypes = CollectionUtil.newArrayList((Object[])new String[] { ActionType.TASKOPINION.getKey(), ActionType.FLOWIMAGE.getKey() });
/*    */   
/*    */   @Autowired
/*    */   private TaskLeaderSaveActionHandler leaderSaveActionHandler;
/*    */   
/*    */   @Autowired
/*    */   private TaskSendLeaderActionHandle sendLeaderActionHandle;
/*    */   
/*    */   public boolean isSupport(Button btn, IBpmTask bpmTask) {
/* 29 */     BpmTask task = (BpmTask)bpmTask;
/*    */     
/* 31 */     IUser user = ContextUtil.getCurrentUser();
/* 32 */     if (StringUtils.equals(user.getUserId(), task.getAssigneeId()))
/*    */     {
/* 34 */       return this.supportsLeaderTypes.contains(btn.getAlias());
/*    */     }
/*    */     
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void specialBtnByUserHandler(List<Button> btn, IBpmTask bpmTask) {
/* 43 */     BpmTask task = (BpmTask)bpmTask;
/*    */     
/* 45 */     IUser user = ContextUtil.getCurrentUser();
/* 46 */     if (StringUtils.equals(user.getUserId(), task.getAssigneeId())) {
/* 47 */       ActionType actionType = this.leaderSaveActionHandler.getActionType();
/* 48 */       Button leaderAgree = new Button(actionType.getName(), actionType.getKey(), this.leaderSaveActionHandler.getDefaultGroovyScript(), this.leaderSaveActionHandler.getConfigPage());
/* 49 */       btn.add(0, leaderAgree);
/*    */     } else {
/*    */       
/* 52 */       ActionType actionType = this.sendLeaderActionHandle.getActionType();
/* 53 */       Button leaderAgree = new Button(actionType.getName(), actionType.getKey(), this.sendLeaderActionHandle.getDefaultGroovyScript(), this.sendLeaderActionHandle.getConfigPage());
/* 54 */       btn.add(0, leaderAgree);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/leaderTask/handler/LeaderButtonChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */