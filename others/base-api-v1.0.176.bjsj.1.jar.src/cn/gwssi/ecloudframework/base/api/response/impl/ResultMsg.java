/*     */ package cn.gwssi.ecloudframework.base.api.response.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.constant.BaseStatusCode;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.IStatusCode;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.StatusCode;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ApiModel(description = "标准的返回结果包装类")
/*     */ public class ResultMsg<E>
/*     */   extends BaseResult
/*     */ {
/*     */   private static final long serialVersionUID = 7420095794453471L;
/*     */   @Deprecated
/*     */   public static final int SUCCESS = 1;
/*     */   @Deprecated
/*     */   public static final int FAIL = 0;
/*     */   @Deprecated
/*     */   public static final int ERROR = -1;
/*     */   @Deprecated
/*     */   public static final int TIMEOUT = 2;
/*     */   @ApiModelProperty("结果数据")
/*     */   private E data;
/*     */   
/*     */   public ResultMsg() {}
/*     */   
/*     */   public ResultMsg(E result) {
/*  50 */     setOk(Boolean.TRUE);
/*  51 */     setCode(BaseStatusCode.SUCCESS.getCode());
/*  52 */     setData(result);
/*     */   }
/*     */   
/*     */   public ResultMsg(IStatusCode code, String msg) {
/*  56 */     setOk(Boolean.valueOf(BaseStatusCode.SUCCESS.getCode().equals(code.getCode())));
/*  57 */     setCode(code.getCode());
/*  58 */     setMsg(msg);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public ResultMsg(int code, String msg) {
/*  63 */     setOk(Boolean.valueOf((code == 1)));
/*  64 */     setMsg(msg);
/*     */   }
/*     */   
/*     */   public E getData() {
/*  68 */     return this.data;
/*     */   }
/*     */   
/*     */   public void setData(E data) {
/*  72 */     this.data = data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T getSuccessResult(ResultMsg<T> result) {
/*  80 */     if (result != null && result.getIsOk().booleanValue()) {
/*  81 */       return result.getData();
/*     */     }
/*  83 */     throw new BusinessException(new StatusCode(result.getCode(), result.getMsg()));
/*     */   }
/*     */   
/*     */   public ResultMsg<E> addMapParam(String key, Object val) {
/*  87 */     if (this.data == null) {
/*  88 */       Map<Object, Object> map1 = new HashMap<>();
/*  89 */       this.data = (E)map1;
/*     */     } 
/*  91 */     if (!(this.data instanceof Map)) {
/*  92 */       throw new RuntimeException("设置参数异常！当前返回结果非map对象，无法使用 addMapParam方法获取数据");
/*     */     }
/*     */     
/*  95 */     Map<String, Object> map = (Map)this.data;
/*  96 */     map.put(key, val);
/*     */     
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public Object getMapParam(String key) {
/* 102 */     if (!(this.data instanceof Map)) {
/* 103 */       throw new RuntimeException("获取参数异常！当前返回结果非map对象，无法使用 addMapParam方法获取数据");
/*     */     }
/*     */     
/* 106 */     Map map = (Map)this.data;
/* 107 */     return map.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> ResultMsg<E> ERROR(String msg) {
/* 112 */     ResultMsg<E> result = new ResultMsg<>();
/* 113 */     result.setOk(Boolean.FALSE);
/* 114 */     result.setMsg(msg);
/* 115 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> ResultMsg<E> SUCCESS() {
/* 120 */     ResultMsg<E> result = new ResultMsg<>();
/* 121 */     result.setOk(Boolean.TRUE);
/* 122 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> ResultMsg<E> SUCCESS(E data) {
/* 127 */     ResultMsg<E> result = new ResultMsg<>();
/* 128 */     result.setOk(Boolean.TRUE);
/* 129 */     result.setData(data);
/* 130 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/response/impl/ResultMsg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */