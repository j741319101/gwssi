package com.dstz.bpm.api.engine.data;

import com.dstz.bus.api.model.IBusinessData;
import com.dstz.form.api.model.FormType;
import com.dstz.bpm.api.engine.data.result.BpmFlowData;
import com.dstz.bpm.api.engine.data.result.BpmFlowInstanceData;

import java.util.Map;

public interface BpmFlowDataAccessor {
  BpmFlowInstanceData getInstanceData(String paramString1, FormType paramFormType, String paramString2, String paramString3);
  
  Map<String, IBusinessData> getTaskBusData(String paramString);
  
  BpmFlowData getStartFlowData(String paramString1, String paramString2, String paramString3, FormType paramFormType, Boolean paramBoolean);
  
  BpmFlowData getFlowTaskData(String paramString1, String paramString2, FormType paramFormType);
}

