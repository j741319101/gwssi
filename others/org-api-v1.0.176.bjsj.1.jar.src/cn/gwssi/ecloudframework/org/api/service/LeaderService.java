package cn.gwssi.ecloudframework.org.api.service;

import cn.gwssi.ecloudframework.org.api.model.IUser;

public interface LeaderService {
  IUser getUserBySecretaryId(String paramString);
  
  boolean isLeaderByLeaderId(String paramString);
  
  IUser getUserByLeaderId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/service/LeaderService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */