/*    */ package com.dstz.bpm.plugin.global.reminder.executer;
/*    */ 
/*    */ import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class RemindTaskAction
/*    */   extends DefaultExtendTaskAction {
/*    */   @Resource
/*    */   private BpmReminderLogManager reminderLogManager;
/*    */   
/*    */   public void deleteDataByInstId(String instId) {
/* 15 */     this.reminderLogManager.removeByInstId(instId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/reminder/executer/RemindTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */