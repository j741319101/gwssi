package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
import com.dstz.base.dao.BaseDao;
import java.util.Date;
import java.util.List;

public interface BpmReminderTriggerDao extends BaseDao<String, BpmReminderTrigger> {
   void removeByTaskId(String var1);

   int queryTaskReminderCount(String var1);

   List<BpmReminderTrigger> getDueMinders(Date var1);
}
