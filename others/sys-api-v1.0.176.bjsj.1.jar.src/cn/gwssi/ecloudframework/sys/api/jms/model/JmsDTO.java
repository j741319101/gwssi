package com.dstz.sys.api.jms.model;

import java.io.Serializable;

public interface JmsDTO<T extends Serializable> extends Serializable {
  String getType();
  
  T getData();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/jms/model/JmsDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */