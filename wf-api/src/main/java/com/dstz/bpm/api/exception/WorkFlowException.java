/*    */ package com.dstz.bpm.api.exception;
/*    */ 
/*    */ import com.dstz.base.api.constant.IStatusCode;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ 
/*    */ public class WorkFlowException
/*    */   extends BusinessException {
/*    */   public WorkFlowException(String msg, IStatusCode errorCode) {
/*  9 */     super(msg, errorCode);
/*    */   }
/*    */   
/*    */   public WorkFlowException(IStatusCode errorCode) {
/* 13 */     super(errorCode);
/*    */   }
/*    */   
/*    */   public WorkFlowException(IStatusCode errorCode, Throwable throwable) {
/* 17 */     super(errorCode, throwable);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/exception/WorkFlowException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */