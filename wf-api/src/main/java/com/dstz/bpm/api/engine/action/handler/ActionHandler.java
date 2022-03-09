package com.dstz.bpm.api.engine.action.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;

public interface ActionHandler<T extends ActionCmd> {
  void execute(T paramT);
  
  ActionType getActionType();
  
  int getSn();
  
  Boolean isSupport(BpmNodeDef paramBpmNodeDef);
  
  Boolean isDefault();
  
  String getConfigPage();
  
  String getDefaultGroovyScript();
  
  String getDefaultBeforeScript();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/handler/ActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */