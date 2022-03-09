package cn.gwssi.ecloudbpm.bus.service;

import cn.gwssi.ecloudbpm.bus.model.BusinessData;
import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
import com.alibaba.fastjson.JSONObject;

public interface BusinessDataPersistenceService {
  String type();
  
  void saveData(BusinessData paramBusinessData);
  
  void saveData(BusinessData paramBusinessData, JSONObject paramJSONObject);
  
  BusinessData loadData(BusinessObject paramBusinessObject, Object paramObject);
  
  void removeData(BusinessObject paramBusinessObject, Object paramObject);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/BusinessDataPersistenceService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */