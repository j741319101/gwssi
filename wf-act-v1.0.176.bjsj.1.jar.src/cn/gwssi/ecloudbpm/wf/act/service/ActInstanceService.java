package cn.gwssi.ecloudbpm.wf.act.service;

import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
import java.util.Map;
import org.activiti.engine.runtime.ProcessInstance;

public interface ActInstanceService {
  String startProcessInstance(String paramString1, String paramString2, Map<String, Object> paramMap);
  
  String startProcessInstance(IBpmInstance paramIBpmInstance, Map<String, Object> paramMap, String... paramVarArgs);
  
  Map<String, Object> getVariables(String paramString);
  
  void deleteProcessInstance(String paramString1, String paramString2);
  
  ProcessInstance getProcessInstance(String paramString);
  
  void createNewExecution(String paramString1, String paramString2, String paramString3);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/service/ActInstanceService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */