package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
import com.dstz.base.manager.Manager;
import java.util.List;

public interface BpmReminderTriggerManager extends Manager<String, BpmReminderTrigger> {
   void removeByTaskId(String var1);

   List<BpmReminderTrigger> getDueMinders();
}
