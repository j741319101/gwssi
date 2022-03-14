package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BpmCarbonCopyRecordDao extends BaseDao<String, BpmCarbonCopyRecord> {
   int createList(@Param("records") List<BpmCarbonCopyRecord> var1);

   void removeByInstId(@Param("instId") String var1);
}
