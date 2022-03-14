package com.dstz.sys.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.sys.core.model.SysDataSourceDef;
import com.dstz.sys.core.model.def.SysDataSourceDefAttribute;
import java.util.List;

public interface SysDataSourceDefManager extends Manager<String, SysDataSourceDef> {
  List<SysDataSourceDefAttribute> initAttributes(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SysDataSourceDefManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */