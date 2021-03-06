package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import java.util.List;
import java.util.Set;

public interface BpmCarbonCopyReceiveManager extends Manager<String, BpmCarbonCopyReceive> {
  int createList(List<BpmCarbonCopyReceive> paramList);
  
  int updateRead(BpmCarbonCopyReceive paramBpmCarbonCopyReceive, Set<String> paramSet);
  
  int updateReadByUser(String paramString);
  
  List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter paramQueryFilter);
  
  void removeByInstId(String paramString);
  
  List<BpmCarbonCopyReceive> query2(QueryFilter paramQueryFilter);
  
  List<BpmCarbonCopyReceive> getByParam(String paramString1, String paramString2, String paramString3);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/BpmCarbonCopyReceiveManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */