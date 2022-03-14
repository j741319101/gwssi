package com.dstz.bpm.core.manager;

import com.dstz.bpm.api.model.inst.BpmExecutionStack;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import java.util.List;

public interface BpmTaskStackManager extends Manager<String, BpmTaskStack> {
  BpmTaskStack getByTaskId(String paramString);
  
  BpmTaskStack createStackByTask(IBpmTask paramIBpmTask, BpmExecutionStack paramBpmExecutionStack);
  
  void removeByInstanceId(String paramString);
  
  List<BpmTaskStack> getByInstanceId(String paramString);
  
  List<BpmTaskStack> getByInstIdAndTrace(String paramString1, String paramString2);
  
  List<BpmTaskStack> getTaskStackByIteration(QueryFilter paramQueryFilter);
  
  void updateStackEndByInstId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/BpmTaskStackManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */