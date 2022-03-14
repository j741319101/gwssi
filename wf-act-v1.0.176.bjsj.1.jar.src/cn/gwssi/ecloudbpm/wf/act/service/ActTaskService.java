package com.dstz.bpm.act.service;

import java.util.Map;
import org.activiti.engine.delegate.DelegateTask;

public interface ActTaskService {
  DelegateTask getByTaskId(String paramString);
  
  void completeTask(String paramString, Map<String, Object> paramMap);
  
  void completeTask(String paramString);
  
  void completeTask(String paramString, Map<String, Object> paramMap, String... paramVarArgs);
  
  void completeTask(String paramString, String... paramVarArgs);
  
  Object getVariable(String paramString1, String paramString2);
  
  Object getVariableLocal(String paramString1, String paramString2);
  
  Map<String, Object> getVariables(String paramString);
  
  void setVariable(String paramString1, String paramString2, Object paramObject);
  
  void setVariables(String paramString, Map<String, ? extends Object> paramMap);
  
  Map<String, Object> getVariablesLocal(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/service/ActTaskService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */