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
