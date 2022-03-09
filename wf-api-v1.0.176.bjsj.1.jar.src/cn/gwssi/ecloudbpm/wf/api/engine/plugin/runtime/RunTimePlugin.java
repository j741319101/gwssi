package cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime;

public interface RunTimePlugin<S, M, R> {
  R execute(S paramS, M paramM);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/plugin/runtime/RunTimePlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */