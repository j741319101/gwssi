package com.dstz.org.core.manager;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.org.api.model.dto.BpmOrgDTO;
import com.dstz.org.api.model.dto.BpmUserDTO;
import com.dstz.org.core.model.User;
import java.util.List;

public interface BpmUserManager {
  List<User> getUserListByOrgId(String paramString);
  
  List<User> getUserListByRelation(String paramString1, String paramString2);
  
  List<User> query(QueryFilter paramQueryFilter);
  
  List<BpmUserDTO> getUserOrgInfos(String paramString);
  
  List<BpmOrgDTO> getOrgInfos(String paramString);
  
  List<User> getUserListByPostId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/BpmUserManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */