package cn.gwssi.ecloudframework.sys.api.jms.producer;

import cn.gwssi.ecloudframework.sys.api.jms.MessageQueueSendException;
import cn.gwssi.ecloudframework.sys.api.jms.model.JmsDTO;
import java.util.List;

public interface JmsProducer {
  void sendToQueue(JmsDTO paramJmsDTO) throws MessageQueueSendException;
  
  void sendToQueue(List<JmsDTO> paramList) throws MessageQueueSendException;
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/jms/producer/JmsProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */