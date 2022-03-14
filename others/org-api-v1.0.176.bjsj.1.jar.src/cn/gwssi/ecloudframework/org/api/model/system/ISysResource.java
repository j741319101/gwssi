package com.dstz.org.api.model.system;

import com.dstz.base.api.model.Tree;
import java.util.List;

public interface ISysResource extends Tree {
  String getId();
  
  String getSystemId();
  
  String getAlias();
  
  String getKey();
  
  String getName();
  
  String getUrl();
  
  Integer getEnable();
  
  Integer getOpened();
  
  String getIcon();
  
  String getType();
  
  Integer getSn();
  
  String getParentId();
  
  List getChildren();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/system/ISysResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */