package cn.gwssi.ecloudframework.org.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "用户信息")
public interface IUser extends Serializable {
  @ApiModelProperty("用户ID")
  String getUserId();
  
  void setUserId(String paramString);
  
  @ApiModelProperty("用户名")
  String getFullname();
  
  void setFullname(String paramString);
  
  @ApiModelProperty("账户")
  String getAccount();
  
  void setAccount(String paramString);
  
  @ApiModelProperty("密码")
  String getPassword();
  
  @ApiModelProperty("Email")
  String getEmail();
  
  @ApiModelProperty("手机号")
  String getMobile();
  
  @ApiModelProperty("微信号")
  String getWeixin();
  
  @ApiModelProperty("openID")
  String getOpenid();
  
  @ApiModelProperty("座机号")
  String getTelephone();
  
  @ApiModelProperty("是否启用 0/1")
  Integer getStatus();
  
  @ApiModelProperty("照片")
  String getPhoto();
  
  @ApiModelProperty("用户sn")
  Integer getSn();
  
  void setSn(Integer paramInteger);
  
  @ApiModelProperty("角色列表")
  List<? extends IUserRole> getRoles();
  
  @ApiModelProperty("用户类型")
  String getType();
  
  @ApiModelProperty("管理员机构id列表")
  List<String> getManagerGroupIdList();
  
  @ApiModelProperty("主岗位id")
  String getPostId();
  
  @ApiModelProperty("主岗位名称")
  String getPostName();
  
  @ApiModelProperty("主岗位编号")
  String getPostCode();
  
  @ApiModelProperty("主机构id")
  String getOrgId();
  
  @ApiModelProperty("主机构名称")
  String getOrgName();
  
  @ApiModelProperty("主机构code")
  String getOrgCode();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/IUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */