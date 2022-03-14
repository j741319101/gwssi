package com.dstz.bpm.api.model.task;

import java.io.Serializable;
import java.util.Date;

public interface IBpmTaskApprove extends Serializable {
  public static final long serialVersionUID = -700694295167942753L;
  
  String getId();
  
  String getNodeName();
  
  String getNodeId();
  
  Date getApproveTime();
  
  Long getDurMs();
  
  String getApproveStatus();
  
  String getSubject();
  
  String getDefName();
  
  String getStatus();
  
  Date getEndTime();
  
  Long getDuration();
  
  String getTypeId();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/task/IBpmTaskApprove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */