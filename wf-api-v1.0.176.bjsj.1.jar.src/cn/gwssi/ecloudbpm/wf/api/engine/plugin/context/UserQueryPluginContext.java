package com.dstz.bpm.api.engine.plugin.context;

import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;

public interface UserQueryPluginContext {
  Class<? extends RunTimePlugin> getUserQueryPluginClass();
  
  boolean isEmpty();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/plugin/context/UserQueryPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */