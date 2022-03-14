package com.dstz.sys.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.sys.core.model.SerialNo;
import java.util.List;

public interface SerialNoManager extends Manager<String, SerialNo> {
  boolean isAliasExisted(String paramString1, String paramString2);
  
  String getCurIdByAlias(String paramString);
  
  String nextId(String paramString);
  
  List<SerialNo> getPreviewIden(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SerialNoManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */