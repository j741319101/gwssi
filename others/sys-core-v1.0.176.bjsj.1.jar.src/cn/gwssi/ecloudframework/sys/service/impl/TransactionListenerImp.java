/*    */ package com.dstz.sys.service.impl;
/*    */ 
/*    */ import com.dstz.sys.api.model.mq.RocketTransactionMessageDto;
/*    */ import com.dstz.sys.api.service.ITransactionSendBusService;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.rocketmq.client.producer.LocalTransactionState;
/*    */ import org.apache.rocketmq.client.producer.TransactionListener;
/*    */ import org.apache.rocketmq.common.message.Message;
/*    */ import org.apache.rocketmq.common.message.MessageExt;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class TransactionListenerImp
/*    */   implements TransactionListener
/*    */ {
/* 23 */   Logger log = LoggerFactory.getLogger(TransactionListenerImp.class);
/*    */   
/*    */   @Resource
/*    */   ITransactionSendBusService transactionSendBusService;
/*    */   @Resource
/*    */   private Environment environment;
/* 29 */   private ConcurrentHashMap<String, Integer> locaTrans = new ConcurrentHashMap<>(0);
/*    */ 
/*    */   
/*    */   public LocalTransactionState executeLocalTransaction(Message message, Object o) {
/* 33 */     String transactionId = message.getTransactionId();
/* 34 */     this.locaTrans.put(transactionId, Integer.valueOf(0));
/*    */ 
/*    */     
/* 37 */     this.log.info("开始执行本地事务..........");
/*    */     try {
/* 39 */       RocketTransactionMessageDto rocketTransactionMessageDto = new RocketTransactionMessageDto();
/* 40 */       rocketTransactionMessageDto.setKey(message.getKeys());
/* 41 */       rocketTransactionMessageDto.setTopic(message.getTopic());
/* 42 */       rocketTransactionMessageDto.setTag(message.getTags());
/* 43 */       rocketTransactionMessageDto.setMsg(new String(message.getBody(), "UTF-8"));
/* 44 */       this.transactionSendBusService.handleBus(rocketTransactionMessageDto);
/* 45 */       this.locaTrans.put(transactionId, Integer.valueOf(1));
/* 46 */       this.log.info("本地事务执行成功...........");
/* 47 */     } catch (Exception e) {
/* 48 */       e.printStackTrace();
/* 49 */       this.log.error("本地事务执行失败-----------");
/* 50 */       this.locaTrans.put(transactionId, Integer.valueOf(2));
/* 51 */       return LocalTransactionState.ROLLBACK_MESSAGE;
/*    */     } 
/*    */     
/* 54 */     return LocalTransactionState.COMMIT_MESSAGE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
/* 60 */     String transactionId = messageExt.getTransactionId();
/* 61 */     Integer status = this.locaTrans.get(transactionId);
/* 62 */     this.log.info("消息回查------------status:{},----------transactionId:{}", status, transactionId);
/* 63 */     switch (status.intValue()) {
/*    */       case 0:
/* 65 */         return LocalTransactionState.UNKNOW;
/*    */       case 1:
/* 67 */         return LocalTransactionState.COMMIT_MESSAGE;
/*    */       case 2:
/* 69 */         return LocalTransactionState.ROLLBACK_MESSAGE;
/*    */     } 
/*    */     
/* 72 */     return LocalTransactionState.UNKNOW;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/TransactionListenerImp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */