package com.dstz.sys.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.sys.core.model.SysConnectRecord;
import java.util.List;

public interface SysConnectRecordManager extends Manager<String, SysConnectRecord> {
  List<SysConnectRecord> getByTargetId(String paramString1, String paramString2);
  
  void bulkCreate(List<SysConnectRecord> paramList);
  
  void removeBySourceId(String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SysConnectRecordManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */