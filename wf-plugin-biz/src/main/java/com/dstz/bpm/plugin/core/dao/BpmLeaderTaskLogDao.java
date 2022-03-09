package com.dstz.bpm.plugin.core.dao;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import com.dstz.bpm.core.model.BpmTaskApprove;
import com.dstz.bpm.core.vo.BpmTaskVO;
import com.dstz.bpm.plugin.core.model.BpmLeaderTaskLog;
import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BpmLeaderTaskLogDao extends BaseDao<String, BpmLeaderTaskLog> {
  Integer updateByTaskId(@Param("taskLog") BpmLeaderTaskLog paramBpmLeaderTaskLog);
  
  List<BpmTaskVO> getTodoList(QueryFilter paramQueryFilter);
  
  List<BpmTaskApprove> getApproveHistoryList(QueryFilter paramQueryFilter);
  
  List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter paramQueryFilter);
  
  void removeByInstId(@Param("instId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/dao/BpmLeaderTaskLogDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */