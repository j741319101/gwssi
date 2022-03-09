/*    */ package com.dstz.bpm.plugin.global.reminder.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.global.reminder.def.Reminder;
/*    */ import com.dstz.bpm.plugin.global.reminder.def.ReminderPluginDef;
/*    */ import com.dstz.bpm.plugin.global.reminder.executer.ReminderPluginExecuter;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */
import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class ReminderPluginContext
/*    */   extends AbstractBpmPluginContext<ReminderPluginDef>
/*    */ {
/* 21 */   protected int sn = 80;
/*    */   
/*    */   private static final long serialVersionUID = 3192828814663399776L;
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 26 */     List<EventType> eventTypes = new ArrayList<>();
/* 27 */     eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
/* 28 */     eventTypes.add(EventType.TASK_COMPLETE_EVENT);
/* 29 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 34 */     return (Class) ReminderPluginExecuter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 39 */     return "催办";
/*    */   }
/*    */ 
/*    */   
/*    */   protected ReminderPluginDef parseFromJson(JSON json) {
/* 44 */     List<Reminder> reminderList = JSON.parseArray(json.toJSONString(), Reminder.class);
/* 45 */     ReminderPluginDef def = new ReminderPluginDef();
/* 46 */     def.setReminderList(reminderList);
/*    */     
/* 48 */     return def;
/*    */   }
/*    */   
/*    */   public Integer getSn() {
/* 52 */     return Integer.valueOf(this.sn);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/reminder/context/ReminderPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */