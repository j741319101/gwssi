package cn.gwssi.ecloudbpm.bus.api.service;

import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
import com.alibaba.fastjson.JSONObject;

public interface IBusinessDataService {
  void saveFormDefData(JSONObject paramJSONObject, IBusinessPermission paramIBusinessPermission);
  
  void saveNewFormDefData(JSONObject paramJSONObject, IBusinessPermission paramIBusinessPermission) throws BusinessException;
  
  JSONObject getFormDefData(IBusinessObject paramIBusinessObject, Object paramObject);
  
  JSONObject getFormDefData(IBusinessObject paramIBusinessObject);
  
  void removeData(IBusinessObject paramIBusinessObject, Object paramObject);
  
  void saveData(IBusinessData paramIBusinessData);
  
  void saveData(IBusinessData paramIBusinessData, JSONObject paramJSONObject);
  
  IBusinessData loadData(IBusinessObject paramIBusinessObject, Object paramObject);
  
  IBusinessData parseBusinessData(JSONObject paramJSONObject, String paramString);
  
  JSONObject assemblyFormDefData(IBusinessData paramIBusinessData);
  
  IBusinessData loadData(IBusinessObject paramIBusinessObject, Object paramObject, boolean paramBoolean);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/service/IBusinessDataService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */