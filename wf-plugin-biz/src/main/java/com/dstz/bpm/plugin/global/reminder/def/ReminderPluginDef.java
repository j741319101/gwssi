package com.dstz.bpm.plugin.global.reminder.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.List;

public class ReminderPluginDef extends AbstractBpmExecutionPluginDef {
   private List<Reminder> reminderList;

   public List<Reminder> getReminderList() {
      return this.reminderList;
   }

   public void setReminderList(List<Reminder> reminderList) {
      this.reminderList = reminderList;
   }
}
