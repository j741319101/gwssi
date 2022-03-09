/*    */ package cn.gwssi.ecloudframework.base.core.validate.aop;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.aop.annotion.ParamValidate;
/*    */ import cn.gwssi.ecloudframework.base.api.response.impl.BaseResult;
/*    */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.base.core.validate.ValidateUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import org.aspectj.lang.ProceedingJoinPoint;
/*    */ import org.aspectj.lang.Signature;
/*    */ import org.aspectj.lang.annotation.Around;
/*    */ import org.aspectj.lang.reflect.MethodSignature;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidateParamAspect
/*    */ {
/* 26 */   private final Logger logger = LoggerFactory.getLogger(getClass());
/*    */ 
/*    */   
/*    */   @Around("@annotation(paramValidate)")
/*    */   public Object doAround(ProceedingJoinPoint pjp, ParamValidate paramValidate) throws Throwable {
/* 31 */     Object[] objects = pjp.getArgs();
/* 32 */     this.logger.debug("参数拦截开始=====" + JSON.toJSONString(objects));
/* 33 */     for (Object o : objects) {
/* 34 */       String msg = ValidateUtil.getValidateMsg(o);
/* 35 */       if (StringUtil.isNotEmpty(msg)) {
/* 36 */         this.logger.error("参数拦截信息" + msg);
/* 37 */         return getResult(pjp, msg);
/*    */       } 
/*    */     } 
/* 40 */     this.logger.debug("======参数拦截结束=====");
/* 41 */     Object result = pjp.proceed();
/* 42 */     return result;
/*    */   }
/*    */   
/*    */   private BaseResult getResult(ProceedingJoinPoint point, String error) {
/*    */     ResultMsg resultMsg;
/* 47 */     Signature signature = point.getSignature();
/* 48 */     Class returnType = ((MethodSignature)signature).getReturnType();
/*    */ 
/*    */     
/* 51 */     BaseResult res = null;
/* 52 */     if (ResultMsg.class.equals(returnType)) {
/* 53 */       resultMsg = new ResultMsg();
/*    */     }
/*    */ 
/*    */     
/* 57 */     resultMsg.setOk(Boolean.FALSE);
/* 58 */     resultMsg.setMsg(error);
/* 59 */     return (BaseResult)resultMsg;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/validate/aop/ValidateParamAspect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */