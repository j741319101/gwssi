package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
import com.dstz.bpm.plugin.vo.BpmUserAgencyLogVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

public interface BpmUserAgencyLogDao extends BaseDao<String, BpmUserAgencyLog> {
   int insertSelective(BpmUserAgencyLog var1);

   int updateByPrimaryKeySelective(BpmUserAgencyLog var1);

   int removeByConfigIds(@Param("configIds") Set<String> var1);

   List<BpmUserAgencyLogVO> selectBpmUserAgencyLogList(QueryFilter var1);
}
