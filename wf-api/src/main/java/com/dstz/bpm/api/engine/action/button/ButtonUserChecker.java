package com.dstz.bpm.api.engine.action.button;

import com.dstz.bpm.api.model.nodedef.Button;
import com.dstz.bpm.api.model.task.IBpmTask;

import java.util.List;

public interface ButtonUserChecker {
  boolean isSupport(Button paramButton, IBpmTask paramIBpmTask);
  
  void specialBtnByUserHandler(List<Button> paramList, IBpmTask paramIBpmTask);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/button/ButtonUserChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */