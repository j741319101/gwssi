/*    */ package com.dstz.bpm.plugin.global.leaderTask.handler;
/*    */ 
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.api.constant.TaskStatus;
/*    */ import com.dstz.bpm.api.engine.action.button.ButtonUserChecker;
/*    */ import com.dstz.bpm.api.model.nodedef.Button;
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.bpm.plugin.global.leaderTask.context.LeaderTaskPlaginContext;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component("LeaderButtonChecker")
/*    */ public class LeaderButtonChecker
/*    */   implements ButtonUserChecker
/*    */ {
/* 24 */   private final List<String> supportsLeaderTypes = CollectionUtil.newArrayList((Object[])new String[] { ActionType.TASKOPINION.getKey(), ActionType.FLOWIMAGE.getKey() });
/*    */   
/*    */   @Autowired
/*    */   private TaskLeaderSaveActionHandler leaderSaveActionHandler;
/*    */   
/*    */   @Autowired
/*    */   private TaskSendLeaderActionHandle sendLeaderActionHandle;
/*    */   
/*    */   public boolean isSupport(Button btn, IBpmTask bpmTask) {
/* 33 */     LeaderTaskPlaginContext leaderTaskPlaginContext = (LeaderTaskPlaginContext)AppUtil.getBean("leaderTaskPlaginContext");
/* 34 */     if (leaderTaskPlaginContext.isEffective()) {
/* 35 */       BpmTask task = (BpmTask)bpmTask;
/* 36 */       if (StringUtils.equals(task.getStatus(), TaskStatus.LEADER.getKey())) {
/*    */         
/* 38 */         IUser user = ContextUtil.getCurrentUser();
/* 39 */         if (StringUtils.equals(user.getUserId(), task.getAssigneeId()))
/*    */         {
/* 41 */           return this.supportsLeaderTypes.contains(btn.getAlias());
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void specialBtnByUserHandler(List<Button> btn, IBpmTask bpmTask) {
/* 51 */     LeaderTaskPlaginContext leaderTaskPlaginContext = (LeaderTaskPlaginContext)AppUtil.getBean("leaderTaskPlaginContext");
/* 52 */     if (leaderTaskPlaginContext.isEffective()) {
/*    */       
/* 54 */       BpmTask task = (BpmTask)bpmTask;
/* 55 */       if (StringUtils.equals(task.getStatus(), TaskStatus.LEADER.getKey())) {
/* 56 */         IUser user = ContextUtil.getCurrentUser();
/* 57 */         if (StringUtils.equals(user.getUserId(), task.getAssigneeId())) {
/* 58 */           ActionType actionType = this.leaderSaveActionHandler.getActionType();
/* 59 */           Button leaderAgree = new Button(actionType.getName(), actionType.getKey(), this.leaderSaveActionHandler.getDefaultGroovyScript(), this.leaderSaveActionHandler.getConfigPage());
/* 60 */           btn.add(0, leaderAgree);
/*    */         } else {
/*    */           
/* 63 */           ActionType actionType = this.sendLeaderActionHandle.getActionType();
/* 64 */           Button leaderAgree = new Button(actionType.getName(), actionType.getKey(), this.sendLeaderActionHandle.getDefaultGroovyScript(), this.sendLeaderActionHandle.getConfigPage());
/* 65 */           btn.add(0, leaderAgree);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/leaderTask/handler/LeaderButtonChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */