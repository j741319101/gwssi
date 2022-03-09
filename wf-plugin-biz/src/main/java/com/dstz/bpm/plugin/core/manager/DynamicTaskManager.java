package com.dstz.bpm.plugin.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.bpm.plugin.core.model.DynamicTask;

public interface DynamicTaskManager extends Manager<String, DynamicTask> {
  DynamicTask getByStatus(String paramString1, String paramString2, String paramString3);
  
  DynamicTask getDynamicTaskSettingByInstanceId(String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/DynamicTaskManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */