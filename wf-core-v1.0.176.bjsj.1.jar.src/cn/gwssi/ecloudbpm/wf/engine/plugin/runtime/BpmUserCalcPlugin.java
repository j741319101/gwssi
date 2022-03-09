package cn.gwssi.ecloudbpm.wf.engine.plugin.runtime;

import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmUserCalcPluginSession;
import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
import java.util.List;

public interface BpmUserCalcPlugin<M extends cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef> extends RunTimePlugin<BpmUserCalcPluginSession, M, List<SysIdentity>> {}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/runtime/BpmUserCalcPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */