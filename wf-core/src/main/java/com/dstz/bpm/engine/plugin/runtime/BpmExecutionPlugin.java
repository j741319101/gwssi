package com.dstz.bpm.engine.plugin.runtime;

import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;

public interface BpmExecutionPlugin<S extends BpmExecutionPluginSession, M extends com.dstz.bpm.api.engine.plugin.def.BpmExecutionPluginDef> extends RunTimePlugin<S, M, Void> {}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/runtime/BpmExecutionPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */