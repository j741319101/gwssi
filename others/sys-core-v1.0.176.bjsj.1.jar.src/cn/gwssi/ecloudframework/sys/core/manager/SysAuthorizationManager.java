package cn.gwssi.ecloudframework.sys.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.api.constant.RightsObjectConstants;
import cn.gwssi.ecloudframework.sys.core.model.SysAuthorization;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysAuthorizationManager extends Manager<String, SysAuthorization> {
  Set<String> getUserRights(String paramString);
  
  Map<String, Object> getUserRightsSql(RightsObjectConstants paramRightsObjectConstants, String paramString1, String paramString2);
  
  List<SysAuthorization> getByTarget(RightsObjectConstants paramRightsObjectConstants, String paramString);
  
  void createAll(List<SysAuthorization> paramList, String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SysAuthorizationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */