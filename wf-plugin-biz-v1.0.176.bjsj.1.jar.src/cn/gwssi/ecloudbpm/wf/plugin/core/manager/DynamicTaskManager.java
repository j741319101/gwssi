package cn.gwssi.ecloudbpm.wf.plugin.core.manager;

import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
import cn.gwssi.ecloudbpm.wf.plugin.core.model.DynamicTask;
import cn.gwssi.ecloudframework.base.manager.Manager;
import java.util.List;
import java.util.Map;

public interface DynamicTaskManager extends Manager<String, DynamicTask> {
  DynamicTask getByStatus(String paramString1, String paramString2, String paramString3);
  
  DynamicTask getDynamicTaskSettingByInstanceId(String paramString1, String paramString2);
  
  List<BpmTaskStack> getTaskStackByInstIdAndNodeId(String paramString1, String paramString2);
  
  List<Map> getDynamicTaskByInstIdAndNodeId(String paramString1, String paramString2);
  
  void removeByInstId(String paramString);
  
  List<DynamicTask> getByTaskId(String paramString);
  
  void updateEndByInstId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/DynamicTaskManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */