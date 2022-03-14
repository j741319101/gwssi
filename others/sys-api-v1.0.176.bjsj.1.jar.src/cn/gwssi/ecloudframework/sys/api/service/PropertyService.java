package com.dstz.sys.api.service;

public interface PropertyService {
  String getByAlias(String paramString);
  
  String getByAlias(String paramString1, String paramString2);
  
  Integer getIntByAlias(String paramString);
  
  Integer getIntByAlias(String paramString, Integer paramInteger);
  
  Long getLongByAlias(String paramString);
  
  boolean getBooleanByAlias(String paramString);
  
  boolean getBooleanByAlias(String paramString, boolean paramBoolean);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/PropertyService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */