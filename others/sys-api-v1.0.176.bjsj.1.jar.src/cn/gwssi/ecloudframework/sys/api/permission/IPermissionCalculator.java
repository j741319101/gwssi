package com.dstz.sys.api.permission;

import com.alibaba.fastjson.JSONObject;

public interface IPermissionCalculator {
  String getType();
  
  String getTitle();
  
  boolean haveRights(JSONObject paramJSONObject);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/permission/IPermissionCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */