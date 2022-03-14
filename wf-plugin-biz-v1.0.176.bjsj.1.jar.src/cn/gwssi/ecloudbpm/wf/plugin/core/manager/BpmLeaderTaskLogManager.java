package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.core.model.BpmTaskApprove;
import com.dstz.bpm.core.vo.BpmTaskVO;
import com.dstz.bpm.plugin.core.model.BpmLeaderTaskLog;
import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import java.util.List;

public interface BpmLeaderTaskLogManager extends Manager<String, BpmLeaderTaskLog> {
  Integer updateByTaskId(String paramString1, String paramString2, String paramString3);
  
  BpmLeaderTaskLog getByTaskId(String paramString);
  
  List<BpmTaskVO> getTodoList(QueryFilter paramQueryFilter);
  
  List<BpmTaskApprove> getApproveHistoryList(QueryFilter paramQueryFilter);
  
  List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter paramQueryFilter);
  
  void saveOptionAndUpdateStatusToLeader(String paramString1, String paramString2);
  
  void saveOptionAndUpdateStatusToSecretary(String paramString1, String paramString2);
  
  void removeByInstId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/BpmLeaderTaskLogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */