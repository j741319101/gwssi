package com.dstz.sys.api.groovy;

import java.util.Map;

public interface IGroovyScriptEngine {
  void execute(String paramString);
  
  void execute(String paramString, Map<String, Object> paramMap);
  
  boolean executeBoolean(String paramString, Map<String, Object> paramMap);
  
  String executeString(String paramString, Map<String, Object> paramMap);
  
  int executeInt(String paramString, Map<String, Object> paramMap);
  
  float executeFloat(String paramString, Map<String, Object> paramMap);
  
  Object executeObject(String paramString, Map<String, Object> paramMap);
  
  String executeFloat(String paramString1, Map<String, Object> paramMap, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/groovy/IGroovyScriptEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */