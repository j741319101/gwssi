package cn.gwssi.ecloudframework.sys.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.core.model.MetaColumn;

public interface SysMetaColumnManager extends Manager<String, MetaColumn> {
  void updateStatus(String paramString, int paramInt);
  
  void updateGroupId(String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SysMetaColumnManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */