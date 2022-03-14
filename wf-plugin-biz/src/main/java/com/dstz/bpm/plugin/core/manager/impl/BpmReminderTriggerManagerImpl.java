package com.dstz.bpm.plugin.core.manager.impl;

import com.dstz.bpm.plugin.core.dao.BpmReminderTriggerDao;
import com.dstz.bpm.plugin.core.manager.BpmReminderTriggerManager;
import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.manager.impl.BaseManager;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmReminderTriggerManager")
public class BpmReminderTriggerManagerImpl extends BaseManager<String, BpmReminderTrigger> implements BpmReminderTriggerManager {
   @Resource
   BpmReminderTriggerDao bpmReminderTriggerDao;

   public void removeByTaskId(String taskId) {
      if (!StringUtil.isEmpty(taskId)) {
         if (this.bpmReminderTriggerDao.queryTaskReminderCount(taskId) >= 1) {
            this.bpmReminderTriggerDao.removeByTaskId(taskId);
         }
      }
   }

   public List<BpmReminderTrigger> getDueMinders() {
      return this.bpmReminderTriggerDao.getDueMinders(new Date());
   }
}
