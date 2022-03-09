/*    */ package cn.gwssi.ecloudframework.base.api.exception;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.constant.BaseStatusCode;
/*    */ import cn.gwssi.ecloudframework.base.api.constant.IStatusCode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BusinessMessage
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -7289238698048230824L;
/* 13 */   public IStatusCode statusCode = (IStatusCode)BaseStatusCode.SYSTEM_ERROR;
/*    */   
/*    */   public BusinessMessage(String msg) {
/* 16 */     super(msg);
/*    */   }
/*    */   
/*    */   public BusinessMessage(String msg, Throwable throwable) {
/* 20 */     super(msg, throwable);
/*    */   }
/*    */   
/*    */   public BusinessMessage(Throwable throwable) {
/* 24 */     super(throwable);
/*    */   }
/*    */   
/*    */   public BusinessMessage(String msg, IStatusCode errorCode) {
/* 28 */     super(msg);
/* 29 */     this.statusCode = errorCode;
/*    */   }
/*    */   
/*    */   public BusinessMessage(IStatusCode errorCode) {
/* 33 */     super(errorCode.getDesc());
/* 34 */     this.statusCode = errorCode;
/*    */   }
/*    */   
/*    */   public BusinessMessage(IStatusCode errorCode, Throwable throwable) {
/* 38 */     super(errorCode.getDesc(), throwable);
/* 39 */     this.statusCode = errorCode;
/*    */   }
/*    */   
/*    */   public BusinessMessage(String msg, IStatusCode errorCode, Throwable throwable) {
/* 43 */     super(errorCode.getDesc() + ":" + msg, throwable);
/* 44 */     this.statusCode = errorCode;
/*    */   }
/*    */   
/*    */   public String getStatuscode() {
/* 48 */     if (this.statusCode == null) return ""; 
/* 49 */     return this.statusCode.getCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public IStatusCode getStatusCode() {
/* 54 */     return this.statusCode;
/*    */   }
/*    */   
/*    */   public void setStatusCode(IStatusCode statusCode) {
/* 58 */     this.statusCode = statusCode;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/exception/BusinessMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */