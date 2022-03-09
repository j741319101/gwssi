package cn.gwssi.ecloudbpm.wf.api.model.inst;

import java.util.Date;

public interface BpmExecutionStack {
  void setId(String paramString);
  
  String getId();
  
  void setTaskId(String paramString);
  
  String getTaskId();
  
  void setInstId(String paramString);
  
  String getInstId();
  
  void setParentId(String paramString);
  
  String getParentId();
  
  void setNodeId(String paramString);
  
  String getNodeId();
  
  void setNodeName(String paramString);
  
  String getNodeName();
  
  void setStartTime(Date paramDate);
  
  Date getStartTime();
  
  void setEndTime(Date paramDate);
  
  Date getEndTime();
  
  void setIsMulitiTask(Short paramShort);
  
  Short getIsMulitiTask();
  
  void setNodeType(String paramString);
  
  String getNodeType();
  
  String getActionName();
  
  void setActionName(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/inst/BpmExecutionStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */