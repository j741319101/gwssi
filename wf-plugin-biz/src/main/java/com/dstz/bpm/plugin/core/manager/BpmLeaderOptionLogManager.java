package com.dstz.bpm.plugin.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.bpm.plugin.core.model.BpmLeaderOptionLog;

public interface BpmLeaderOptionLogManager extends Manager<String, BpmLeaderOptionLog> {
  void removeByInstId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/BpmLeaderOptionLogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */