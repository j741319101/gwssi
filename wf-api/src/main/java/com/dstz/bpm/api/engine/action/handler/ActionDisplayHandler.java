package com.dstz.bpm.api.engine.action.handler;

import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;

public interface ActionDisplayHandler<T extends ActionCmd> extends ActionHandler<T> {
  boolean isDisplay(boolean paramBoolean, BpmNodeDef paramBpmNodeDef, IBpmInstance paramIBpmInstance, IBpmTask paramIBpmTask);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/handler/ActionDisplayHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */