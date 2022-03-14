package com.dstz.bpm.engine.plugin.session;

import com.dstz.bus.api.model.IBusinessData;
import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import java.util.Map;
import org.activiti.engine.delegate.VariableScope;

public interface BpmPluginSession extends Map<String, Object> {
  IBpmInstance getBpmInstance();
  
  Map<String, IBusinessData> getBoDatas();
  
  VariableScope getVariableScope();
  
  EventType getEventType();
}
