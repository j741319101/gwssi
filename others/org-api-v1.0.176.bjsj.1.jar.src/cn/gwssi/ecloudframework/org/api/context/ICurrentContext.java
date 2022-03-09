package cn.gwssi.ecloudframework.org.api.context;

import cn.gwssi.ecloudframework.org.api.model.IGroup;
import cn.gwssi.ecloudframework.org.api.model.IUser;
import cn.gwssi.ecloudframework.org.api.model.IUserRole;
import java.util.List;
import java.util.Locale;

public interface ICurrentContext {
  public static final String CURRENT_ORG = "current_org";
  
  public static final String CURRENT_USER = "current_user";
  
  IUser getCurrentUser();
  
  String getCurrentUserId();
  
  IGroup getCurrentGroup();
  
  void clearCurrentUser();
  
  void setCurrentUser(IUser paramIUser);
  
  void setCurrentUserByAccount(String paramString);
  
  void cacheCurrentGroup(IGroup paramIGroup);
  
  void clearUserRedisCache(String paramString);
  
  Locale getLocale();
  
  void setLocale(Locale paramLocale);
  
  void clearLocale();
  
  boolean isAdmin(IUser paramIUser);
  
  List<? extends IUserRole> getCurrentRoles();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/context/ICurrentContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */