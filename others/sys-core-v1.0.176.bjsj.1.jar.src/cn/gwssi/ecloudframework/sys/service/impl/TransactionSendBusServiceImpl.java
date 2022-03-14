/*    */ package com.dstz.sys.service.impl;
/*    */ 
/*    */ import com.dstz.sys.api.model.mq.RocketTransactionMessageDto;
/*    */ import com.dstz.sys.api.service.ITransactionSendBusService;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class TransactionSendBusServiceImpl
/*    */   implements ITransactionSendBusService
/*    */ {
/* 16 */   Logger log = LoggerFactory.getLogger(TransactionSendBusServiceImpl.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleBus(RocketTransactionMessageDto rocketTransactionMessageDto) throws Exception {
/* 21 */     this.log.info("事务消息默认业务处理........，消息message:{}", JSON.toJSONString(rocketTransactionMessageDto));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/TransactionSendBusServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */