package cn.gwssi.ecloudbpm.wf.api.model.task;

import java.util.Date;

public interface IBpmTaskOpinion {
  String getId();
  
  String getInstId();
  
  String getSupInstId();
  
  String getTaskId();
  
  String getTaskKey();
  
  String getTaskName();
  
  String getAssignInfo();
  
  String getApprover();
  
  String getApproverName();
  
  String getOpinion();
  
  String getStatus();
  
  String getFormId();
  
  String getCreateBy();
  
  Date getCreateTime();
  
  Date getApproveTime();
  
  String getTaskOrgId();
  
  void setTaskOrgId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/task/IBpmTaskOpinion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */