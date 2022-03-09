package cn.gwssi.ecloudbpm.wf.engine.plugin.session;

import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
import java.util.Map;
import org.activiti.engine.delegate.VariableScope;

public interface BpmPluginSession extends Map<String, Object> {
  IBpmInstance getBpmInstance();
  
  Map<String, IBusinessData> getBoDatas();
  
  VariableScope getVariableScope();
  
  EventType getEventType();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/session/BpmPluginSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */