package cn.gwssi.ecloudframework.sys.api.service;

import cn.gwssi.ecloudframework.sys.api.constant.RightsObjectConstants;
import java.util.Map;
import java.util.Set;

public interface SysAuthorizationService {
  Set<String> getUserRights(String paramString);
  
  Map<String, Object> getUserRightsSql(RightsObjectConstants paramRightsObjectConstants, String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/SysAuthorizationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */