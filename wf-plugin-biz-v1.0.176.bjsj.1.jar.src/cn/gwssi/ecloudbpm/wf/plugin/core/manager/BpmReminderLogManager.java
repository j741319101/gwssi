package cn.gwssi.ecloudbpm.wf.plugin.core.manager;

import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmReminderLog;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.manager.Manager;
import java.util.List;

public interface BpmReminderLogManager extends Manager<String, BpmReminderLog> {
  List<BpmReminderLog> getList(QueryFilter paramQueryFilter);
  
  void removeByInstId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/BpmReminderLogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */