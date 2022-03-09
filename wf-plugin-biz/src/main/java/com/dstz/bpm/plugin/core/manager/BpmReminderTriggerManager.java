package com.dstz.bpm.plugin.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
import java.util.List;

public interface BpmReminderTriggerManager extends Manager<String, BpmReminderTrigger> {
  void removeByTaskId(String paramString);
  
  List<BpmReminderTrigger> getDueMinders();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/BpmReminderTriggerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */