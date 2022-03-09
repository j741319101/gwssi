package cn.gwssi.ecloudbpm.bus.api.model;

import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusObjPermission;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public interface IBusinessPermission {
  String getObjType();
  
  String getObjVal();
  
  Map<String, ? extends IBusObjPermission> getBusObjMap();
  
  IBusObjPermission getBusObj(String paramString);
  
  JSONObject getTablePermission(boolean paramBoolean);
  
  JSONObject getPermission(boolean paramBoolean);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/model/IBusinessPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */