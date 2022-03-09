package cn.gwssi.ecloudbpm.wf.act.listener;

import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
import org.activiti.engine.delegate.event.ActivitiEvent;

public interface ActEventListener {
  void notify(ActivitiEvent paramActivitiEvent);
  
  void systemMessage(ActionCmd paramActionCmd);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/listener/ActEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */