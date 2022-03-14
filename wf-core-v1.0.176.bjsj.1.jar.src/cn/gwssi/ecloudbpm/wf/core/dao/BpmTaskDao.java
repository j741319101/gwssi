package com.dstz.bpm.core.dao;

import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTypeTreeCountVO;
import com.dstz.bpm.core.vo.BpmTaskVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmTaskDao extends BaseDao<String, BpmTask> {
  List<BpmTask> getByInstIdNodeId(@Param("instId") String paramString1, @Param("nodeId") String paramString2);
  
  List<BpmTask> getSignTaskBySignSourceId(@Param("taskId") String paramString);
  
  List<BpmTaskVO> getTodoList(QueryFilter paramQueryFilter);
  
  void removeByInstId(@Param("instId") String paramString);
  
  List<BpmTask> getByParentId(@Param("parentId") String paramString);
  
  Map getMyTaskNum(@Param("userId") String paramString, @Param("userRights") Set<String> paramSet);
  
  List<Map> getUnionOrder();
  
  List<BpmTypeTreeCountVO> getTodoListTypeCount(QueryFilter paramQueryFilter);
  
  Set<String> selectTaskIdByInstId(@Param("instId") String paramString);
  
  List<BpmTask> getByParam(Map<String, Object> paramMap);
  
  List<BpmTaskVO> getTaskLinksInfo(QueryFilter paramQueryFilter);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/dao/BpmTaskDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */