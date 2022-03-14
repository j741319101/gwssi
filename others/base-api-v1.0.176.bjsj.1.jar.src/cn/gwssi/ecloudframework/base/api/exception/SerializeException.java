/*    */ package com.dstz.base.api.exception;
/*    */ 
/*    */ public class SerializeException extends RuntimeException {
/*    */   private static final long serialVersionUID = 8847845622247770262L;
/*    */   
/*    */   public SerializeException(String msg) {
/*  7 */     super(msg);
/*    */   }
/*    */   
/*    */   public SerializeException(String msg, Throwable throwable) {
/* 11 */     super(msg, throwable);
/*    */   }
/*    */   
/*    */   public SerializeException(Throwable throwable) {
/* 15 */     super(throwable);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/exception/SerializeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */