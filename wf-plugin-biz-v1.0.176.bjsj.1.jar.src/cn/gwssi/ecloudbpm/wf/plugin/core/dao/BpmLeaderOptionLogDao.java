package cn.gwssi.ecloudbpm.wf.plugin.core.dao;

import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmLeaderOptionLog;
import cn.gwssi.ecloudframework.base.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

public interface BpmLeaderOptionLogDao extends BaseDao<String, BpmLeaderOptionLog> {
  void removeByInstId(@Param("instId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/dao/BpmLeaderOptionLogDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */