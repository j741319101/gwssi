package cn.gwssi.ecloudbpm.wf.plugin.core.dao;

import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmReminderTrigger;
import cn.gwssi.ecloudframework.base.dao.BaseDao;
import java.util.Date;
import java.util.List;

public interface BpmReminderTriggerDao extends BaseDao<String, BpmReminderTrigger> {
  void removeByTaskId(String paramString);
  
  int queryTaskReminderCount(String paramString);
  
  List<BpmReminderTrigger> getDueMinders(Date paramDate);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/dao/BpmReminderTriggerDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */