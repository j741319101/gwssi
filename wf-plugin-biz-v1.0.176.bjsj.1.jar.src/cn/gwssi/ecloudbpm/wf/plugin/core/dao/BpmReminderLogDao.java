package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.BpmReminderLog;
import com.dstz.base.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

public interface BpmReminderLogDao extends BaseDao<String, BpmReminderLog> {
  Integer feedback(BpmReminderLog paramBpmReminderLog);
  
  void removeByInstId(@Param("instId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/dao/BpmReminderLogDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */