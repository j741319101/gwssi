package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

public interface BpmCarbonCopyReceiveDao extends BaseDao<String, BpmCarbonCopyReceive> {
   int createList(@Param("records") List<BpmCarbonCopyReceive> var1);

   int updateRead(@Param("record") BpmCarbonCopyReceive var1, @Param("primaryKeys") Set<String> var2);

   int updateReadByUser(@Param("userId") String var1);

   List<BpmUserReceiveCarbonCopyRecordVO> listUserReceiveList(QueryFilter var1);

   List<BpmCarbonCopyReceive> query2(QueryFilter var1);
}
