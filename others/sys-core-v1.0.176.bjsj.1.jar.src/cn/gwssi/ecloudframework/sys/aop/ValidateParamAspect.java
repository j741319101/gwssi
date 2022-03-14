/*    */ package com.dstz.sys.aop;
/*    */ 
/*    */ import com.dstz.base.api.aop.annotion.ParamValidate;
/*    */ import com.dstz.base.api.response.impl.BaseResult;
/*    */ import com.dstz.base.api.response.impl.ResultMsg;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.base.core.validate.ValidateUtil;
/*    */ import com.dstz.base.db.model.page.PageResult;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import org.aspectj.lang.ProceedingJoinPoint;
/*    */ import org.aspectj.lang.Signature;
/*    */ import org.aspectj.lang.annotation.Around;
/*    */ import org.aspectj.lang.annotation.Aspect;
/*    */ import org.aspectj.lang.reflect.MethodSignature;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Aspect
/*    */ @Component
/*    */ public class ValidateParamAspect
/*    */ {
/* 29 */   private final Logger logger = LoggerFactory.getLogger(getClass());
/*    */ 
/*    */   
/*    */   @Around("@annotation(paramValidate)")
/*    */   public Object doAround(ProceedingJoinPoint pjp, ParamValidate paramValidate) throws Throwable {
/* 34 */     Object[] objects = pjp.getArgs();
/* 35 */     this.logger.debug("参数拦截开始====={}", JSON.toJSONString(objects));
/* 36 */     for (Object o : objects) {
/* 37 */       String msg = ValidateUtil.getValidateMsg(o);
/* 38 */       if (StringUtil.isNotEmpty(msg)) {
/* 39 */         this.logger.error("参数拦截信息{}", msg);
/* 40 */         return getResult(pjp, msg);
/*    */       } 
/*    */     } 
/* 43 */     this.logger.debug("======参数拦截结束=====");
/* 44 */     Object result = pjp.proceed();
/* 45 */     return result;
/*    */   }
/*    */   
/*    */   private BaseResult getResult(ProceedingJoinPoint point, String error) {
/*    */     ResultMsg resultMsg;
/* 50 */     Signature signature = point.getSignature();
/* 51 */     Class returnType = ((MethodSignature)signature).getReturnType();
/*    */ 
/*    */ 
/*    */     
/* 55 */     if (PageResult.class.equals(returnType)) {
/* 56 */       PageResult pageResult = new PageResult();
/*    */     } else {
/* 58 */       resultMsg = new ResultMsg();
/*    */     } 
/* 60 */     resultMsg.setOk(Boolean.valueOf(false));
/* 61 */     resultMsg.setMsg(error);
/* 62 */     return (BaseResult)resultMsg;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/aop/ValidateParamAspect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */