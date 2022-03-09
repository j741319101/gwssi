package cn.gwssi.ecloudbpm.bus.api.model.permission;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public interface IBusObjPermission extends IAbstractPermission {
  String getKey();
  
  String getName();
  
  Map<String, ? extends IBusTablePermission> getTableMap();
  
  void handlePermission(JSONObject paramJSONObject1, JSONObject paramJSONObject2, Boolean paramBoolean);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/model/permission/IBusObjPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */