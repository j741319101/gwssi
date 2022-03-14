package com.dstz.bpm.core.manager;

import com.dstz.bpm.core.model.BpmExecutionLink;
import com.dstz.base.manager.Manager;
import java.util.List;

public interface BpmExecutionLinkManager extends Manager<String, BpmExecutionLink> {
  List<BpmExecutionLink> getByParam(String paramString1, String paramString2, String paramString3, String paramString4);
  
  void removeByParam(String paramString1, String paramString2, String paramString3, String paramString4);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/BpmExecutionLinkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */