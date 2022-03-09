package cn.gwssi.ecloudbpm.wf.core.manager;

import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
import cn.gwssi.ecloudbpm.wf.core.model.BpmTypeTreeCountVO;
import cn.gwssi.ecloudbpm.wf.core.vo.BpmTaskVO;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BpmTaskManager extends Manager<String, BpmTask> {
  List<BpmTask> getByInstIdNodeId(String paramString1, String paramString2);
  
  List<BpmTask> getSignTaskBySignSourceId(String paramString);
  
  List<BpmTask> getByInstId(String paramString);
  
  List<BpmTaskVO> getTodoList(String paramString, QueryFilter paramQueryFilter);
  
  void assigneeTask(String paramString, List<SysIdentity> paramList);
  
  void unLockTask(String paramString);
  
  void removeByInstId(String paramString);
  
  List getTodoList(QueryFilter paramQueryFilter);
  
  List<SysIdentity> getAssignUserById(BpmTask paramBpmTask);
  
  List<BpmTask> getByParentId(String paramString);
  
  List<BpmTypeTreeCountVO> todoTaskListTypeCount(String paramString, QueryFilter paramQueryFilter);
  
  Set<String> selectTaskIdByInstId(String paramString);
  
  Map getMyTaskNum();
  
  List<Map> getUnionOrder();
  
  List<BpmTask> getByParam(String paramString1, String paramString2);
  
  void recycleTask(String paramString1, OpinionStatus paramOpinionStatus, String paramString2);
  
  List<BpmTaskVO> getTaskLinksInfo(QueryFilter paramQueryFilter);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/BpmTaskManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */