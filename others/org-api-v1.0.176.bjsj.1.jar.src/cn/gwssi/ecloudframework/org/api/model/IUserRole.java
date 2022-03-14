package com.dstz.org.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "用户角色新 ")
public interface IUserRole extends Serializable {
  @ApiModelProperty("角色标识")
  String getAlias();
  
  @ApiModelProperty("用户名")
  String getFullname();
  
  @ApiModelProperty("角色名")
  String getRoleName();
  
  @ApiModelProperty("角色ID")
  String getRoleId();
  
  @ApiModelProperty("用户ID")
  String getUserId();
  
  @ApiModelProperty("用户账户")
  String getAccount();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/IUserRole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */