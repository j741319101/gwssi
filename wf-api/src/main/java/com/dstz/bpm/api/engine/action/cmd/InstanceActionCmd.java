package com.dstz.bpm.api.engine.action.cmd;

import com.dstz.bpm.api.model.inst.IBpmInstance;

public interface InstanceActionCmd extends ActionCmd {
  String getSubject();
  
  String getBusinessKey();
  
  String getInstanceId();
  
  IBpmInstance getBpmInstance();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/cmd/InstanceActionCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */