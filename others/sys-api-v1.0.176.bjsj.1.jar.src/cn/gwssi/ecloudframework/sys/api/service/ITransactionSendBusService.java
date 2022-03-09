package cn.gwssi.ecloudframework.sys.api.service;

import cn.gwssi.ecloudframework.sys.api.model.mq.RocketTransactionMessageDto;

public interface ITransactionSendBusService {
  void handleBus(RocketTransactionMessageDto paramRocketTransactionMessageDto) throws Exception;
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/ITransactionSendBusService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */