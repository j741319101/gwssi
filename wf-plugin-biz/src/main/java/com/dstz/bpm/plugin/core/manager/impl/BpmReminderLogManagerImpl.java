package com.dstz.bpm.plugin.core.manager.impl;

import com.dstz.bpm.plugin.core.dao.BpmReminderLogDao;
import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
import com.dstz.bpm.plugin.core.model.BpmReminderLog;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.impl.BaseManager;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmReminderLogManager")
public class BpmReminderLogManagerImpl extends BaseManager<String, BpmReminderLog> implements BpmReminderLogManager {
   @Resource
   BpmReminderLogDao bpmReminderLogDao;

   public List<BpmReminderLog> getList(QueryFilter queryFilter) {
      return null;
   }

   public void removeByInstId(String instId) {
      this.bpmReminderLogDao.removeByInstId(instId);
   }
}
