package cn.gwssi.ecloudframework.sys.api.service;

import cn.gwssi.ecloudframework.sys.api.model.dto.SysFileDTO;
import java.io.InputStream;
import java.util.List;

public interface SysFileService {
  SysFileDTO upload(InputStream paramInputStream, String paramString);
  
  SysFileDTO upload(InputStream paramInputStream, SysFileDTO paramSysFileDTO);
  
  InputStream download(String paramString);
  
  void delete(String... paramVarArgs);
  
  void updateInstid(String paramString1, String paramString2);
  
  List<SysFileDTO> getFileByInstId(String paramString);
  
  SysFileDTO get(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/SysFileService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */