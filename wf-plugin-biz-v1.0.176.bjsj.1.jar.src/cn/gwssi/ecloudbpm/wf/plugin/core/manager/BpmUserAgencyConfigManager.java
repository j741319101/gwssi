package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
import com.dstz.bpm.plugin.dto.BpmUserAgencyConfigTabDTO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import com.github.pagehelper.Page;
import java.util.Date;
import java.util.List;

public interface BpmUserAgencyConfigManager extends Manager<String, BpmUserAgencyConfig> {
  List<BpmUserAgencyConfig> selectTakeEffectingList(String paramString, Date paramDate);
  
  Page<BpmUserAgencyConfig> selectTabList(BpmUserAgencyConfigTabDTO paramBpmUserAgencyConfigTabDTO);
  
  List<BpmUserAgencyConfig> selectTabListJson(String paramString1, String paramString2, QueryFilter paramQueryFilter);
  
  List<BpmUserAgencyConfig> selectTargetListJson(String paramString1, String paramString2, QueryFilter paramQueryFilter);
  
  int insertSelective(BpmUserAgencyConfig paramBpmUserAgencyConfig);
  
  int updateByPrimaryKeySelective(BpmUserAgencyConfig paramBpmUserAgencyConfig);
  
  List<BpmUserAgencyConfig> checkTabListJson(QueryFilter paramQueryFilter) throws Exception;
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/BpmUserAgencyConfigManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */