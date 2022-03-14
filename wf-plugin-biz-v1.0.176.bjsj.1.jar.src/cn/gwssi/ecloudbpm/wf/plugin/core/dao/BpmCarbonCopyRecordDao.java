package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BpmCarbonCopyRecordDao extends BaseDao<String, BpmCarbonCopyRecord> {
  int createList(@Param("records") List<BpmCarbonCopyRecord> paramList);
  
  void removeByInstId(@Param("instId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/dao/BpmCarbonCopyRecordDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */