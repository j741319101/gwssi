package com.dstz.bpm.plugin.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
import java.util.Date;
import java.util.List;

public interface BpmReminderTriggerDao extends BaseDao<String, BpmReminderTrigger> {
  void removeByTaskId(String paramString);
  
  int queryTaskReminderCount(String paramString);
  
  List<BpmReminderTrigger> getDueMinders(Date paramDate);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/dao/BpmReminderTriggerDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */