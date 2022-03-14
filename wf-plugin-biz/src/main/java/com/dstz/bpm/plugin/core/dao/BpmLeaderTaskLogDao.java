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
   Integer updateByTaskId(@Param("taskLog") BpmLeaderTaskLog var1);

   List<BpmLeaderTaskLog> getByTaskId(@Param("taskId") String var1);

   List<BpmTaskVO> getTodoList(QueryFilter var1);

   List<BpmTaskVO> getLeaderTodoList(QueryFilter var1);

   List<BpmTaskApprove> getApproveHistoryList(QueryFilter var1);

   List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter var1);

   void removeByInstId(@Param("instId") String var1);
}
