package cn.gwssi.ecloudbpm.wf.api.platform;

import cn.gwssi.ecloudbpm.wf.api.engine.data.result.BpmFlowData;

public interface IBpmFlowDataAccessorPlatFormSerice {
  BpmFlowData getStartFlowData(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, Boolean paramBoolean);
  
  BpmFlowData getFlowTaskData(String paramString1, String paramString2, String paramString3);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/platform/IBpmFlowDataAccessorPlatFormSerice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */