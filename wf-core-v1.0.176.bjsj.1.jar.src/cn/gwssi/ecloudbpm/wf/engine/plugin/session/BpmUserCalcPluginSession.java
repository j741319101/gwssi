package com.dstz.bpm.engine.plugin.session;

import com.dstz.bpm.api.model.task.IBpmTask;

public interface BpmUserCalcPluginSession extends BpmPluginSession {
  Boolean isPreViewModel();
  
  IBpmTask getBpmTask();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/session/BpmUserCalcPluginSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */