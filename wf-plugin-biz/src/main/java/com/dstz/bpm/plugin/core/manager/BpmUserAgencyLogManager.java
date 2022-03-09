package com.dstz.bpm.plugin.core.manager;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
import com.dstz.bpm.plugin.vo.BpmUserAgencyLogVO;
import java.util.List;

public interface BpmUserAgencyLogManager extends Manager<String, BpmUserAgencyLog> {
  int insertSelective(BpmUserAgencyLog paramBpmUserAgencyLog);
  
  int updateByPrimaryKeySelective(BpmUserAgencyLog paramBpmUserAgencyLog);
  
  List<BpmUserAgencyLogVO> selectBpmUserAgencyLogList(QueryFilter paramQueryFilter);
  
  List<BpmUserAgencyLogVO> selectBpmTargetUserAgencyLogList(QueryFilter paramQueryFilter);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/BpmUserAgencyLogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */