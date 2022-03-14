package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.plugin.core.model.BpmReminderLog;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import java.util.List;

public interface BpmReminderLogManager extends Manager<String, BpmReminderLog> {
   List<BpmReminderLog> getList(QueryFilter var1);

   void removeByInstId(String var1);
}
