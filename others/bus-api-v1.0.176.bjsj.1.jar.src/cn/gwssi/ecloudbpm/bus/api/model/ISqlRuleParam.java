package cn.gwssi.ecloudbpm.bus.api.model;

import java.io.Serializable;
import java.util.Map;

public interface ISqlRuleParam extends Serializable {
  String getDbType();
  
  Map<String, Object> getParams();
  
  Map<String, String> getTypes();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/model/ISqlRuleParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */