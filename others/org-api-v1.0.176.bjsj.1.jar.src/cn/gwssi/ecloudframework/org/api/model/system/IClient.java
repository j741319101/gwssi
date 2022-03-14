package com.dstz.org.api.model.system;

import java.util.List;

public interface IClient {
  String getId();
  
  String getName();
  
  String getSecretKey();
  
  List<String> getAuthority();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/system/IClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */