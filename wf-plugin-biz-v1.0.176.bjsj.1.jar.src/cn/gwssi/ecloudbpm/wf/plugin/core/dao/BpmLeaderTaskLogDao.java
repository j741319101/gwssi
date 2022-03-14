package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.core.model.BpmTaskApprove;
import com.dstz.bpm.core.vo.BpmTaskVO;
import com.dstz.bpm.plugin.core.model.BpmLeaderTaskLog;
import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BpmLeaderTaskLogDao extends BaseDao<String, BpmLeaderTaskLog> {
  Integer updateByTaskId(@Param("taskLog") BpmLeaderTaskLog paramBpmLeaderTaskLog);
  
  List<BpmLeaderTaskLog> getByTaskId(@Param("taskId") String paramString);
  
  List<BpmTaskVO> getTodoList(QueryFilter paramQueryFilter);
  
  List<BpmTaskVO> getLeaderTodoList(QueryFilter paramQueryFilter);
  
  List<BpmTaskApprove> getApproveHistoryList(QueryFilter paramQueryFilter);
  
  List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter paramQueryFilter);
  
  void removeByInstId(@Param("instId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/dao/BpmLeaderTaskLogDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */