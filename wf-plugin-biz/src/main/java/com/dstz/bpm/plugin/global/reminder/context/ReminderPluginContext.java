package com.dstz.bpm.plugin.global.reminder.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.global.reminder.def.Reminder;
import com.dstz.bpm.plugin.global.reminder.def.ReminderPluginDef;
import com.dstz.bpm.plugin.global.reminder.executer.ReminderPluginExecuter;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ReminderPluginContext extends AbstractBpmPluginContext<ReminderPluginDef> {
   protected int sn = 80;
   private static final long serialVersionUID = 3192828814663399776L;

   public List<EventType> getEventTypes() {
      List<EventType> eventTypes = new ArrayList();
      eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
      eventTypes.add(EventType.TASK_COMPLETE_EVENT);
      return eventTypes;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return ReminderPluginExecuter.class;
   }

   public String getTitle() {
      return "催办";
   }

   protected ReminderPluginDef parseFromJson(JSON json) {
      List<Reminder> reminderList = JSON.parseArray(json.toJSONString(), Reminder.class);
      ReminderPluginDef def = new ReminderPluginDef();
      def.setReminderList(reminderList);
      return def;
   }

   public Integer getSn() {
      return this.sn;
   }
}
