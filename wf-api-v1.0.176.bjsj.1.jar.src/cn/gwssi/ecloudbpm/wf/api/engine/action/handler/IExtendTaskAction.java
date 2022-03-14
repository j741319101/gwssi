package com.dstz.bpm.api.engine.action.handler;

import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.base.api.query.QueryFilter;

public interface IExtendTaskAction {
  void turnLeaderTask(IBpmTask paramIBpmTask);
  
  boolean carbonCopyLeaderTask(String paramString1, String paramString2, IBpmTask paramIBpmTask);
  
  boolean carbonInstCopyLeaderTask(String paramString1, String paramString2, IBpmInstance paramIBpmInstance);
  
  String getLeaderUserRights();
  
  boolean isLeaderTask(String paramString);
  
  boolean isContainNode(String paramString1, BpmProcessDef paramBpmProcessDef, String paramString2);
  
  void parseMultInstContainNode(BpmProcessDef paramBpmProcessDef);
  
  void deleteDataByInstId(String paramString);
  
  void revokeInst(String paramString);
  
  void canFreeJump(IBpmTask paramIBpmTask);
  
  boolean isSignTask(BpmNodeDef paramBpmNodeDef);
  
  boolean canRecall(IBpmTask paramIBpmTask);
  
  boolean canReject(IBpmTask paramIBpmTask1, IBpmTask paramIBpmTask2);
  
  void addQueryDodoTaskParams(QueryFilter paramQueryFilter, String paramString);
  
  void addQueryDoReadTaskParams(QueryFilter paramQueryFilter, String paramString);
  
  void doSomethingWhenSaveDef(BpmProcessDef paramBpmProcessDef, String paramString);
  
  void doSomethingWhenDeleteDef(BpmProcessDef paramBpmProcessDef);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/handler/IExtendTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */