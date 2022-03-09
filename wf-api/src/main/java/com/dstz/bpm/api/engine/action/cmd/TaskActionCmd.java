package com.dstz.bpm.api.engine.action.cmd;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.TaskSkipType;
import com.dstz.bpm.api.model.task.IBpmTask;

public interface TaskActionCmd extends ActionCmd {
  ActionType getActionType();
  
  String getTaskId();
  
  String getNodeId();
  
  IBpmTask getBpmTask();
  
  void setBpmTask(IBpmTask paramIBpmTask);
  
  String getDestination();
  
  void setDestination(String paramString);
  
  String getOpinion();
  
  TaskSkipType isHasSkipThisTask();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/cmd/TaskActionCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */