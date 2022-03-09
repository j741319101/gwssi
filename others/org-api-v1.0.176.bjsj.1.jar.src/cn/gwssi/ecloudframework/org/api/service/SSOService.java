package cn.gwssi.ecloudframework.org.api.service;

import cn.gwssi.ecloudframework.org.api.model.ISSOUser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SSOService {
  ISSOUser getSSOUser(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
  
  void invalidSSOToken(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
  
  String getType();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/service/SSOService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */