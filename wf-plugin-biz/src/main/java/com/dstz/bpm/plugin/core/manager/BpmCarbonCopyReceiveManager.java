package com.dstz.bpm.plugin.core.manager;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import java.util.List;
import java.util.Set;

public interface BpmCarbonCopyReceiveManager extends Manager<String, BpmCarbonCopyReceive> {
  int createList(List<BpmCarbonCopyReceive> paramList);
  
  int updateRead(BpmCarbonCopyReceive paramBpmCarbonCopyReceive, Set<String> paramSet);
  
  int updateReadByUser();
  
  List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter paramQueryFilter);
  
  void removeByInstId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/BpmCarbonCopyReceiveManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */