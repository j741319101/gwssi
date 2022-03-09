package cn.gwssi.ecloudframework.base.api.model;

public interface IGBaseModel extends CreateInfoModel, IDModel {
  void setCreateUser(String paramString);
  
  String getCreateUser();
  
  void setUpdateUser(String paramString);
  
  String getUpdateUser();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/model/IGBaseModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */