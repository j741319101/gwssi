package cn.gwssi.ecloudframework.org.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.org.core.model.User;
import java.util.List;

public interface UserManager extends Manager<String, User> {
  User getByAccount(String paramString);
  
  List<User> getUserListByRelation(String paramString1, String paramString2);
  
  boolean isUserExist(User paramUser);
  
  void saveUserInfo(User paramUser);
  
  List<User> getUserListByGroupPath(String paramString);
  
  void removeOutSystemUser();
  
  User getByOpneid(String paramString);
  
  int updateByPrimaryKeySelective(User paramUser);
  
  String getIdByAccount(String paramString);
  
  Integer getAllEnableUserNum();
  
  List<User> getUsersByOrgPath(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/UserManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */