package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BpmUserAgencyConfigDao extends BaseDao<String, BpmUserAgencyConfig> {
   int insertSelective(BpmUserAgencyConfig var1);

   int updateByPrimaryKeySelective(BpmUserAgencyConfig var1);

   List<BpmUserAgencyConfig> selectTakeEffectingList(@Param("configUserId") String var1, @Param("currentTime") String var2, @Param("name") String var3);

   List<BpmUserAgencyConfig> selectInvalidList(@Param("configUserId") String var1, @Param("currentTime") String var2, @Param("name") String var3);

   List<BpmUserAgencyConfig> selectWaitEffectList(@Param("configUserId") String var1, @Param("currentTime") String var2, @Param("name") String var3);

   List<BpmUserAgencyConfig> selectAllList(@Param("configUserId") String var1, @Param("name") String var2);

   List<BpmUserAgencyConfig> selectTabListJson(QueryFilter var1);
}
