package cn.gwssi.ecloudframework.sys.api.model;

import java.util.List;

public interface ISysDataSource {
  String getKey();
  
  String getName();
  
  String getDesc();
  
  String getDbType();
  
  String getClassPath();
  
  List<? extends ISysDataSourceDefAttribute> getAttributes();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/ISysDataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */