package com.dstz.base.api.response;

import java.io.Serializable;

public interface IResult extends Serializable {
  Boolean getIsOk();
  
  String getCode();
  
  String getMsg();
  
  String getCause();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/response/IResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */