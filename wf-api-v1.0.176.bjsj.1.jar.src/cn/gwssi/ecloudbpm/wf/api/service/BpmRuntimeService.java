package cn.gwssi.ecloudbpm.wf.api.service;

public interface BpmRuntimeService {
  String signalEventReceived(String paramString1, String paramString2, String paramString3);
  
  String signalEventReceived(String paramString1, String paramString2);
  
  String instanceScopeSignalEventReceived(String paramString1, String paramString2, String paramString3);
  
  String instanceScopeSignalEventExecutionReceived(String paramString1, String paramString2, String paramString3);
  
  String instanceScopeStopAfterSignalEventReceived(String paramString1, String paramString2, String paramString3);
  
  String instanceScopeStopAfterSignalEventExecutionReceived(String paramString1, String paramString2, String paramString3);
  
  String createNewExecution(String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/service/BpmRuntimeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */