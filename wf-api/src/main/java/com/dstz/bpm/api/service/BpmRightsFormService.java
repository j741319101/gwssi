package com.dstz.bpm.api.service;

import com.dstz.bus.api.model.IBusinessPermission;
import com.dstz.form.api.model.FormType;
import com.dstz.bpm.api.engine.data.result.BpmFlowData;

import java.util.Set;

public interface BpmRightsFormService {
  IBusinessPermission getInstanceFormPermission(BpmFlowData paramBpmFlowData, String paramString, FormType paramFormType, boolean paramBoolean);
  
  IBusinessPermission getNodeSavePermission(String paramString1, String paramString2, String paramString3, Set<String> paramSet);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/service/BpmRightsFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */