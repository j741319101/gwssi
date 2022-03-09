package com.dstz.bpm.plugin.core.dao;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
import com.dstz.bpm.plugin.vo.BpmUserAgencyLogVO;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

public interface BpmUserAgencyLogDao extends BaseDao<String, BpmUserAgencyLog> {
  int insertSelective(BpmUserAgencyLog paramBpmUserAgencyLog);
  
  int updateByPrimaryKeySelective(BpmUserAgencyLog paramBpmUserAgencyLog);
  
  int removeByConfigIds(@Param("configIds") Set<String> paramSet);
  
  List<BpmUserAgencyLogVO> selectBpmUserAgencyLogList(QueryFilter paramQueryFilter);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/dao/BpmUserAgencyLogDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */