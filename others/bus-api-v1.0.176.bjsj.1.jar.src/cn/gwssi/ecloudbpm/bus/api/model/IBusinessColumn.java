package cn.gwssi.ecloudbpm.bus.api.model;

public interface IBusinessColumn {
  String getKey();
  
  String getName();
  
  String getType();
  
  int getLength();
  
  int getDecimal();
  
  boolean isRequired();
  
  boolean isPrimary();
  
  String getDefaultValue();
  
  String getComment();
  
  IBusColumnCtrl getCtrl();
  
  String getTableId();
  
  IBusinessTable getTable();
  
  Object initValue();
  
  Object value(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/model/IBusinessColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */