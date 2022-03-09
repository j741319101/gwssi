package cn.gwssi.ecloudframework.sys.api.model;

import java.io.Serializable;

public interface ISysDataSourceDefAttribute extends Serializable {
  String getName();
  
  String getComment();
  
  String getType();
  
  boolean isRequired();
  
  String getDefaultValue();
  
  String getValue();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/ISysDataSourceDefAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */