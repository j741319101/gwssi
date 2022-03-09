package cn.gwssi.ecloudbpm.bus.api.model;

import java.util.List;
import java.util.Map;

public interface IBusinessTable {
  public static final String ID_COLUMN_NAME = "id";
  
  String getKey();
  
  String getName();
  
  String getComment();
  
  String getDsKey();
  
  List<? extends IBusinessColumn> getColumns();
  
  String getDsName();
  
  List<? extends IBusinessColumn> getColumnsWithoutPk();
  
  Map<String, Object> initDbData();
  
  Map<String, Object> initData();
  
  IBusinessColumn getColumnByKey(String paramString);
  
  IBusinessColumn getColumn(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/model/IBusinessTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */