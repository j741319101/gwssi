/*    */ package com.dstz.base.api.response.impl;
/*    */ 
/*    */ import com.dstz.base.api.constant.BaseStatusCode;
/*    */ import com.dstz.base.api.response.IResult;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BaseResult
/*    */   implements IResult
/*    */ {
/*    */   private static final long serialVersionUID = 6131673218827464899L;
/*    */   @ApiModelProperty("本次调用是否成功")
/*    */   private Boolean isOk;
/*    */   @ApiModelProperty("操作提示信息")
/*    */   private String msg;
/*    */   @ApiModelProperty("异常堆栈信息")
/*    */   private String cause;
/*    */   @ApiModelProperty("状态码")
/*    */   private String code;
/*    */   
/*    */   public void setOk(Boolean ok) {
/* 42 */     if (ok.booleanValue()) {
/* 43 */       setCode(BaseStatusCode.SUCCESS.getCode());
/*    */     }
/* 45 */     this.isOk = ok;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean getIsOk() {
/* 50 */     return this.isOk;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMsg() {
/* 55 */     return this.msg;
/*    */   }
/*    */   
/*    */   public void setMsg(String message) {
/* 59 */     this.msg = message;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCause() {
/* 65 */     return this.cause;
/*    */   }
/*    */   
/*    */   public void setCause(String cause) {
/* 69 */     this.cause = cause;
/*    */   }
/*    */   
/*    */   public void setCode(String code) {
/* 73 */     this.code = code;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 78 */     return this.code;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/response/impl/BaseResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */