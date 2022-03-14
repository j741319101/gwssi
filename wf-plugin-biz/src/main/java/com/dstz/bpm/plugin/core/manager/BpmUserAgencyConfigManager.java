package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
import com.dstz.bpm.plugin.dto.BpmUserAgencyConfigTabDTO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import com.github.pagehelper.Page;
import java.util.Date;
import java.util.List;

public interface BpmUserAgencyConfigManager extends Manager<String, BpmUserAgencyConfig> {
   List<BpmUserAgencyConfig> selectTakeEffectingList(String var1, Date var2);

   Page<BpmUserAgencyConfig> selectTabList(BpmUserAgencyConfigTabDTO var1);

   List<BpmUserAgencyConfig> selectTabListJson(String var1, String var2, QueryFilter var3);

   List<BpmUserAgencyConfig> selectTargetListJson(String var1, String var2, QueryFilter var3);

   int insertSelective(BpmUserAgencyConfig var1);

   int updateByPrimaryKeySelective(BpmUserAgencyConfig var1);

   List<BpmUserAgencyConfig> checkTabListJson(QueryFilter var1) throws Exception;
}
