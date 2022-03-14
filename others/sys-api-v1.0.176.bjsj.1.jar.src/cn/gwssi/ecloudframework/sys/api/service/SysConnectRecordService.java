package com.dstz.sys.api.service;

import com.dstz.sys.api.model.SysConnectRecordDTO;
import java.util.List;

public interface SysConnectRecordService {
  List<SysConnectRecordDTO> getByTargetId(String paramString1, String paramString2);
  
  void save(List<SysConnectRecordDTO> paramList);
  
  void save(SysConnectRecordDTO paramSysConnectRecordDTO);
  
  void removeBySourceId(String paramString1, String paramString2);
  
  void checkIsRelatedWithBusinessMessage(String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/SysConnectRecordService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */