package cn.gwssi.ecloudbpm.wf.api.engine.action.button;

import cn.gwssi.ecloudbpm.wf.api.model.nodedef.Button;
import java.util.List;

public interface ButtonChecker {
  boolean isSupport(Button paramButton);
  
  void specialBtnHandler(List<Button> paramList);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/button/ButtonChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */