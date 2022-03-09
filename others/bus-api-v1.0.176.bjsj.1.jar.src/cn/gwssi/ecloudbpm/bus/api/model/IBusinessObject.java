package cn.gwssi.ecloudbpm.bus.api.model;

import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusObjPermission;
import java.util.Set;

public interface IBusinessObject {
  String getKey();
  
  String getName();
  
  String getDesc();
  
  String getPersistenceType();
  
  IBusTableRel getRelation();
  
  IBusObjPermission getPermission();
  
  void setPermission(IBusObjPermission paramIBusObjPermission);
  
  boolean haveTableDbEditRights(String paramString);
  
  boolean haveTableDbReadRights(String paramString);
  
  boolean haveColumnDbEditRights(String paramString1, String paramString2);
  
  boolean haveColumnDbReadRights(String paramString1, String paramString2);
  
  Set<String> calDataSourceKeys();
  
  String getPerTypeConfig();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/model/IBusinessObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */