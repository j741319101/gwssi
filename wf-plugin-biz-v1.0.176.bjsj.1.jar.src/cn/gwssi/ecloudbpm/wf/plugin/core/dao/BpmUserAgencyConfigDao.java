package cn.gwssi.ecloudbpm.wf.plugin.core.dao;

import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmUserAgencyConfig;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.dao.BaseDao;
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


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/dao/BpmUserAgencyConfigDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */