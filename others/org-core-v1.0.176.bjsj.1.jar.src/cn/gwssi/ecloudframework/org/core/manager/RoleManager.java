package cn.gwssi.ecloudframework.org.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.org.core.model.Role;
import java.util.List;

public interface RoleManager extends Manager<String, Role> {
  Role getByAlias(String paramString);
  
  boolean isRoleExist(Role paramRole);
  
  List<Role> getByUserId(String paramString);
  
  void setStatus(String paramString, Integer paramInteger);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/RoleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */