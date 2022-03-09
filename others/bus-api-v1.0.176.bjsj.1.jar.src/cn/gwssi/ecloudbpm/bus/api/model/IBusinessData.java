package cn.gwssi.ecloudbpm.bus.api.model;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IBusinessData extends Serializable {
  Object getPk();
  
  Map<String, Object> getData();
  
  void put(String paramString, Object paramObject);
  
  Object get(String paramString);
  
  IBusinessData getParent();
  
  IBusTableRel getBusTableRel();
  
  String getString(String paramString);
  
  Map<String, List<IBusinessData>> getChilds();
  
  List<IBusinessData> getChild(String paramString);
  
  JSONObject fullBusDataInitData(JSONObject paramJSONObject);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/model/IBusinessData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */