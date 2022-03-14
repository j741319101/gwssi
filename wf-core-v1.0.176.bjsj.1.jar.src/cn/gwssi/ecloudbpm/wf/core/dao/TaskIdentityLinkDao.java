package com.dstz.bpm.core.dao;

import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface TaskIdentityLinkDao extends BaseDao<String, TaskIdentityLink> {
  void removeByInstId(String paramString);
  
  void removeByTaskId(String paramString);
  
  void bulkCreate(List<TaskIdentityLink> paramList);
  
  int checkUserOperatorPermission(@Param("rights") Set<String> paramSet, @Param("taskId") String paramString1, @Param("instanceId") String paramString2);
  
  List<TaskIdentityLink> getByTaskId(String paramString);
  
  TaskIdentityLink getByTaskIdAndUserId(@Param("taskId") String paramString1, @Param("userId") String paramString2);
  
  int queryTaskIdentityCount(String paramString);
  
  int queryInstanceIdentityCount(String paramString);
  
  int updateCheckState(QueryFilter paramQueryFilter);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/dao/TaskIdentityLinkDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */