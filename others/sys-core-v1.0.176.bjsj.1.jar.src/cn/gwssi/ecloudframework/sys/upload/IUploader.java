package com.dstz.sys.upload;

import com.dstz.sys.core.model.SysFile;
import java.io.InputStream;

public interface IUploader {
  String type();
  
  String upload(InputStream paramInputStream, String paramString);
  
  InputStream take(String paramString);
  
  void remove(String paramString);
  
  String uploadUrl(SysFile paramSysFile);
  
  String downloadUrl(SysFile paramSysFile);
  
  String modify(InputStream paramInputStream, String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/upload/IUploader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */