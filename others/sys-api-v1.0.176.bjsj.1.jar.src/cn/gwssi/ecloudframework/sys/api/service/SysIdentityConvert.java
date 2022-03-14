package com.dstz.sys.api.service;

import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.model.SysIdentity;
import java.util.List;

public interface SysIdentityConvert {
  IUser convert2User(SysIdentity paramSysIdentity);
  
  List<? extends IUser> convert2Users(SysIdentity paramSysIdentity);
  
  List<? extends IUser> convert2Users(List<SysIdentity> paramList);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/SysIdentityConvert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */