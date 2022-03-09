package cn.gwssi.ecloudbpm.wf.core.dao;

import cn.gwssi.ecloudbpm.wf.core.model.BpmExecutionLink;
import cn.gwssi.ecloudframework.base.dao.BaseDao;
import java.util.List;
import java.util.Map;

public interface BpmExecutionLinkDao extends BaseDao<String, BpmExecutionLink> {
  List<BpmExecutionLink> getByParam(Map<String, Object> paramMap);
  
  void removeByParam(Map<String, Object> paramMap);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/dao/BpmExecutionLinkDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */