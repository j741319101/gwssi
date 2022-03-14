package com.dstz.sys.api.model;

import java.io.Serializable;

public interface SysIdentity<T extends SysIdentity> extends Serializable, Comparable<T> {
  public static final String TYPE_USER = "user";
  
  public static final String TYPE_ROLE = "role";
  
  public static final String TYPE_GROUP = "group";
  
  public static final String TYPE_ORG = "org";
  
  public static final String TYPE_POST = "post";
  
  public static final String TYPE_JOB = "job";
  
  public static final String USER_NAME_FORMAT = "$userName";
  
  String getId();
  
  void setId(String paramString);
  
  String getName();
  
  void setName(String paramString);
  
  String getType();
  
  void setType(String paramString);
  
  void setOrgId(String paramString);
  
  String getOrgId();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/SysIdentity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */