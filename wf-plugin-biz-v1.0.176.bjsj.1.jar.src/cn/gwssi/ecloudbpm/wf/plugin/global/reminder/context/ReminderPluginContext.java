/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.reminder.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.reminder.def.Reminder;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.reminder.def.ReminderPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.reminder.executer.ReminderPluginExecuter;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class ReminderPluginContext
/*    */   extends AbstractBpmPluginContext<ReminderPluginDef> {
/* 20 */   protected int sn = 80;
/*    */   
/*    */   private static final long serialVersionUID = 3192828814663399776L;
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 25 */     List<EventType> eventTypes = new ArrayList<>();
/* 26 */     eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
/* 27 */     eventTypes.add(EventType.TASK_COMPLETE_EVENT);
/* 28 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 33 */     return (Class)ReminderPluginExecuter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 38 */     return "催办";
/*    */   }
/*    */ 
/*    */   
/*    */   protected ReminderPluginDef parseFromJson(JSON json) {
/* 43 */     List<Reminder> reminderList = JSON.parseArray(json.toJSONString(), Reminder.class);
/* 44 */     ReminderPluginDef def = new ReminderPluginDef();
/* 45 */     def.setReminderList(reminderList);
/*    */     
/* 47 */     return def;
/*    */   }
/*    */   
/*    */   public Integer getSn() {
/* 51 */     return Integer.valueOf(this.sn);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/reminder/context/ReminderPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */