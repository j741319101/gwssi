package com.dstz.bpm.plugin.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
import java.util.List;

public interface BpmCarbonCopyRecordManager extends Manager<String, BpmCarbonCopyRecord> {
  int createList(List<BpmCarbonCopyRecord> paramList);
  
  void removeByInstId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/BpmCarbonCopyRecordManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */