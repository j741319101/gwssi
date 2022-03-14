package com.dstz.base.api.model;

import java.util.Date;

public interface CreateInfoModel {
  Date getCreateTime();
  
  void setCreateTime(Date paramDate);
  
  String getCreateBy();
  
  void setCreateBy(String paramString);
  
  Date getUpdateTime();
  
  void setUpdateTime(Date paramDate);
  
  String getUpdateBy();
  
  void setUpdateBy(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/model/CreateInfoModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */