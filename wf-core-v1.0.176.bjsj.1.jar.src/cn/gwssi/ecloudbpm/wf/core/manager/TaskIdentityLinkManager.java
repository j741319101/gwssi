package cn.gwssi.ecloudbpm.wf.core.manager;

import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
import cn.gwssi.ecloudbpm.wf.core.model.TaskIdentityLink;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
import java.util.List;
import java.util.Set;

public interface TaskIdentityLinkManager extends Manager<String, TaskIdentityLink> {
  void removeByTaskId(String paramString);
  
  void bulkCreate(List<TaskIdentityLink> paramList);
  
  void removeByInstId(String paramString);
  
  Boolean checkUserOperatorPermission(String paramString1, String paramString2, String paramString3);
  
  void createIdentityLink(IBpmTask paramIBpmTask, List<SysIdentity> paramList);
  
  Set<String> getUserRights(String paramString);
  
  List<TaskIdentityLink> getByTaskId(String paramString);
  
  TaskIdentityLink getByTaskIdAndUserId(String paramString1, String paramString2);
  
  int updateCheckState(QueryFilter paramQueryFilter);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/TaskIdentityLinkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */