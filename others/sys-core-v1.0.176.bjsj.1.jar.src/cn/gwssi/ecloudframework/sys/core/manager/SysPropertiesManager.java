package com.dstz.sys.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.sys.core.model.SysProperties;
import java.util.List;
import java.util.Map;

public interface SysPropertiesManager extends Manager<String, SysProperties> {
  List<String> getGroups();
  
  boolean isExist(SysProperties paramSysProperties);
  
  Map<String, Map<String, String>> reloadProperty();
  
  String getPropertyByAlias(String paramString);
  
  void updateByAlias(SysProperties paramSysProperties);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SysPropertiesManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */