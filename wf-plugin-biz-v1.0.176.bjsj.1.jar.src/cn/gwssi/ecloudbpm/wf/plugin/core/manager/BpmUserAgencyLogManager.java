package cn.gwssi.ecloudbpm.wf.plugin.core.manager;

import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmUserAgencyLog;
import cn.gwssi.ecloudbpm.wf.plugin.vo.BpmUserAgencyLogVO;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.manager.Manager;
import java.util.List;

public interface BpmUserAgencyLogManager extends Manager<String, BpmUserAgencyLog> {
  int insertSelective(BpmUserAgencyLog paramBpmUserAgencyLog);
  
  int updateByPrimaryKeySelective(BpmUserAgencyLog paramBpmUserAgencyLog);
  
  List<BpmUserAgencyLogVO> selectBpmUserAgencyLogList(QueryFilter paramQueryFilter);
  
  List<BpmUserAgencyLogVO> selectBpmTargetUserAgencyLogList(QueryFilter paramQueryFilter);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/BpmUserAgencyLogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */