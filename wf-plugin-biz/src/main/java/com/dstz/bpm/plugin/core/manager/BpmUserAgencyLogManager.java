package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
import com.dstz.bpm.plugin.vo.BpmUserAgencyLogVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import java.util.List;

public interface BpmUserAgencyLogManager extends Manager<String, BpmUserAgencyLog> {
   int insertSelective(BpmUserAgencyLog var1);

   int updateByPrimaryKeySelective(BpmUserAgencyLog var1);

   List<BpmUserAgencyLogVO> selectBpmUserAgencyLogList(QueryFilter var1);

   List<BpmUserAgencyLogVO> selectBpmTargetUserAgencyLogList(QueryFilter var1);
}
