package cn.gwssi.ecloudbpm.wf.api.engine.data;

import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
import cn.gwssi.ecloudbpm.form.api.model.FormType;
import cn.gwssi.ecloudbpm.wf.api.engine.data.result.BpmFlowData;
import cn.gwssi.ecloudbpm.wf.api.engine.data.result.BpmFlowInstanceData;
import java.util.Map;

public interface BpmFlowDataAccessor {
  BpmFlowInstanceData getInstanceData(String paramString1, FormType paramFormType, String paramString2, String paramString3);
  
  Map<String, IBusinessData> getTaskBusData(String paramString);
  
  BpmFlowData getStartFlowData(String paramString1, String paramString2, String paramString3, FormType paramFormType, Boolean paramBoolean);
  
  BpmFlowData getFlowTaskData(String paramString1, String paramString2, FormType paramFormType);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/data/BpmFlowDataAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */