package cn.gwssi.ecloudbpm.bus.api.service;

import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

public interface IBusinessObjectService {
  IBusinessObject getByKey(String paramString);
  
  List<JSONObject> boTreeData(String paramString);
  
  IBusinessObject getFilledByKey(String paramString);
  
  String getBoOverallArrangement(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/service/IBusinessObjectService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */