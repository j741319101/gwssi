package cn.gwssi.ecloudbpm.wf.api.engine.plugin.cmd;

import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;

public interface TaskCommand {
  void execute(EventType paramEventType, TaskActionCmd paramTaskActionCmd);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/plugin/cmd/TaskCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */