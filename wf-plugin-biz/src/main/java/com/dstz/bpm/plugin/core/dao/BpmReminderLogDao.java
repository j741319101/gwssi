package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.BpmReminderLog;
import com.dstz.base.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

public interface BpmReminderLogDao extends BaseDao<String, BpmReminderLog> {
   Integer feedback(BpmReminderLog var1);

   void removeByInstId(@Param("instId") String var1);
}
