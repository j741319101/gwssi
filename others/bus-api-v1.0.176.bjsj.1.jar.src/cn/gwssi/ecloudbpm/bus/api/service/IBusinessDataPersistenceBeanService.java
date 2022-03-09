package cn.gwssi.ecloudbpm.bus.api.service;

import cn.gwssi.ecloudbpm.bus.api.remote.RemoteBusinessData;
import com.alibaba.fastjson.JSONObject;

public interface IBusinessDataPersistenceBeanService {
  String getName();
  
  Object saveData(RemoteBusinessData<JSONObject> paramRemoteBusinessData);
  
  RemoteBusinessData<JSONObject> loadData(RemoteBusinessData paramRemoteBusinessData);
  
  void removeData(RemoteBusinessData paramRemoteBusinessData);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/service/IBusinessDataPersistenceBeanService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */