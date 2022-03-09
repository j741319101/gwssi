package cn.gwssi.ecloudframework.sys.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.core.model.SysFile;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface SysFileManager extends Manager<String, SysFile> {
  SysFile upload(InputStream paramInputStream, String paramString);
  
  void upload(InputStream paramInputStream, SysFile paramSysFile);
  
  Map uploadUrl(String paramString1, String paramString2, String paramString3);
  
  String downloadUrl(String paramString);
  
  InputStream download(String paramString);
  
  void delete(String paramString);
  
  void updateInstid(String paramString1, String paramString2);
  
  List<SysFile> getFileByInstId(String paramString);
  
  SysFile get(String paramString);
  
  void modify(InputStream paramInputStream, SysFile paramSysFile, String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SysFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */