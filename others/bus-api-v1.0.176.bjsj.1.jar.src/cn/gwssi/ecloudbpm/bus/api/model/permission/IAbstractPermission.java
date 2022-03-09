package cn.gwssi.ecloudbpm.bus.api.model.permission;

import com.alibaba.fastjson.JSONArray;
import java.io.Serializable;
import java.util.Map;

public interface IAbstractPermission extends Serializable {
  String getResult();
  
  Map<String, JSONArray> getRights();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/model/permission/IAbstractPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */