package cn.gwssi.ecloudframework.org.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "SSO用户信息")
public interface ISSOUser extends Serializable {
  @ApiModelProperty("用户ID")
  String getUserId();
  
  void setUserId(String paramString);
  
  @ApiModelProperty("用户名")
  String getFullname();
  
  void setFullname(String paramString);
  
  @ApiModelProperty("账户")
  String getAccount();
  
  void setAccount(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/ISSOUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */