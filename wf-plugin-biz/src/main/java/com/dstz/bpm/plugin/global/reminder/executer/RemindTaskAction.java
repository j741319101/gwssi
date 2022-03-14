package com.dstz.bpm.plugin.global.reminder.executer;

import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class RemindTaskAction extends DefaultExtendTaskAction {
   @Resource
   private BpmReminderLogManager reminderLogManager;

   public void deleteDataByInstId(String instId) {
      this.reminderLogManager.removeByInstId(instId);
   }
}
