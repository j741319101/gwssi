/*    */ package cn.gwssi.ecloudframework.base.api.exception;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.constant.BaseStatusCode;
/*    */ import cn.gwssi.ecloudframework.base.api.constant.IStatusCode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SystemException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -7289238698048230824L;
/* 14 */   public IStatusCode statusCode = (IStatusCode)BaseStatusCode.SYSTEM_ERROR;
/*    */   
/*    */   public SystemException(String msg) {
/* 17 */     super(msg);
/*    */   }
/*    */   
/*    */   public SystemException(String msg, Throwable throwable) {
/* 21 */     super(msg, throwable);
/*    */   }
/*    */   
/*    */   public SystemException(Throwable throwable) {
/* 25 */     super(throwable);
/*    */   }
/*    */   
/*    */   public SystemException(String msg, IStatusCode errorCode) {
/* 29 */     super(msg);
/* 30 */     this.statusCode = errorCode;
/*    */   }
/*    */   
/*    */   public SystemException(IStatusCode errorCode) {
/* 34 */     super(errorCode.getDesc());
/* 35 */     this.statusCode = errorCode;
/*    */   }
/*    */   
/*    */   public SystemException(IStatusCode errorCode, Throwable throwable) {
/* 39 */     super(errorCode.getDesc(), throwable);
/* 40 */     this.statusCode = errorCode;
/*    */   }
/*    */   
/*    */   public SystemException(String msg, IStatusCode errorCode, Throwable throwable) {
/* 44 */     super(errorCode.getDesc() + ":" + msg, throwable);
/* 45 */     this.statusCode = errorCode;
/*    */   }
/*    */   
/*    */   public String getStatuscode() {
/* 49 */     if (this.statusCode == null) return ""; 
/* 50 */     return this.statusCode.getCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public IStatusCode getStatusCode() {
/* 55 */     return this.statusCode;
/*    */   }
/*    */   
/*    */   public void setStatusCode(IStatusCode statusCode) {
/* 59 */     this.statusCode = statusCode;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/exception/SystemException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */