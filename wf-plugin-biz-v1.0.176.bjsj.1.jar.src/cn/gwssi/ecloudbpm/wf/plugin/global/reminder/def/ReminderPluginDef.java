/*    */ package com.dstz.bpm.plugin.global.reminder.def;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReminderPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   private List<Reminder> reminderList;
/*    */   
/*    */   public List<Reminder> getReminderList() {
/* 17 */     return this.reminderList;
/*    */   }
/*    */   
/*    */   public void setReminderList(List<Reminder> reminderList) {
/* 21 */     this.reminderList = reminderList;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/reminder/def/ReminderPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */