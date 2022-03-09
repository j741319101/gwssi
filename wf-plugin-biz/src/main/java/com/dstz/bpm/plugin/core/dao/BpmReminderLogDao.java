package com.dstz.bpm.plugin.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.bpm.plugin.core.model.BpmReminderLog;
import org.apache.ibatis.annotations.Param;

public interface BpmReminderLogDao extends BaseDao<String, BpmReminderLog> {
  Integer feedback(BpmReminderLog paramBpmReminderLog);
  
  void removeByInstId(@Param("instId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/dao/BpmReminderLogDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */