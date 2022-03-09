package cn.gwssi.ecloudbpm.wf.api.service;

import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
import java.util.Map;

public interface BpmTaskService {
  IBpmTask getBpmTask(String paramString);
  
  BpmNodeDef getBpmNodeDef(String paramString);
  
  String getTaskReminderStr(String paramString, Map<String, Object> paramMap);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/service/BpmTaskService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */