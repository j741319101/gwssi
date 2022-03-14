package com.dstz.sys.api.jms;

import com.dstz.sys.api.jms.model.JmsDTO;

public interface JmsHandler<T extends java.io.Serializable> {
  String getType();
  
  boolean handlerMessage(JmsDTO<T> paramJmsDTO, String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/jms/JmsHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */