package com.dstz.bpm.plugin.core.manager;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import com.dstz.bpm.plugin.core.model.BpmReminderLog;
import java.util.List;

public interface BpmReminderLogManager extends Manager<String, BpmReminderLog> {
  List<BpmReminderLog> getList(QueryFilter paramQueryFilter);
  
  void removeByInstId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/BpmReminderLogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */