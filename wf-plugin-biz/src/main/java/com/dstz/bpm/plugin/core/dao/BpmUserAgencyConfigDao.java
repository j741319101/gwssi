package com.dstz.bpm.plugin.core.dao;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BpmUserAgencyConfigDao extends BaseDao<String, BpmUserAgencyConfig> {
  int insertSelective(BpmUserAgencyConfig paramBpmUserAgencyConfig);
  
  int updateByPrimaryKeySelective(BpmUserAgencyConfig paramBpmUserAgencyConfig);
  
  List<BpmUserAgencyConfig> selectTakeEffectingList(@Param("configUserId") String paramString1, @Param("currentTime") String paramString2, @Param("name") String paramString3);
  
  List<BpmUserAgencyConfig> selectInvalidList(@Param("configUserId") String paramString1, @Param("currentTime") String paramString2, @Param("name") String paramString3);
  
  List<BpmUserAgencyConfig> selectWaitEffectList(@Param("configUserId") String paramString1, @Param("currentTime") String paramString2, @Param("name") String paramString3);
  
  List<BpmUserAgencyConfig> selectAllList(@Param("configUserId") String paramString1, @Param("name") String paramString2);
  
  List<BpmUserAgencyConfig> selectTabListJson(QueryFilter paramQueryFilter);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/dao/BpmUserAgencyConfigDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */