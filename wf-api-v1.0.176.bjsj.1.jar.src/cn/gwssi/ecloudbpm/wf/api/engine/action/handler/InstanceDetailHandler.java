package com.dstz.bpm.api.engine.action.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;

public interface InstanceDetailHandler {
  boolean isDisplay(BpmNodeDef paramBpmNodeDef, IBpmInstance paramIBpmInstance);
  
  ActionType getActionType();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/handler/InstanceDetailHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */