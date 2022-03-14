/*    */ package com.dstz.base.core.validate;
/*    */ 
/*    */ import com.dstz.base.api.constant.BaseStatusCode;
/*    */ import com.dstz.base.api.exception.BusinessMessage;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import java.util.Set;
/*    */ import javax.validation.ConstraintViolation;
/*    */ import javax.validation.Validation;
/*    */ import javax.validation.Validator;
/*    */ import javax.validation.ValidatorFactory;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidateUtil
/*    */ {
/* 18 */   private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
/*    */   
/* 20 */   private static final Logger logger = LoggerFactory.getLogger(ValidateUtil.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void validate(Object o) {
/* 26 */     String msg = getValidateMsg(o);
/* 27 */     if (StringUtil.isNotEmpty(msg)) {
/* 28 */       logger.info("参数拦截信息{}", msg);
/* 29 */       throw new BusinessMessage(msg, BaseStatusCode.PARAM_ILLEGAL);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static Set<ConstraintViolation<Object>> getValidation(Object o) {
/* 34 */     Validator validator = factory.getValidator();
/* 35 */     Set<ConstraintViolation<Object>> violations = validator.validate(o, new Class[0]);
/*    */     
/* 37 */     return violations;
/*    */   }
/*    */   
/*    */   public static String getValidateMsg(Object o) {
/* 41 */     Validator validator = factory.getValidator();
/* 42 */     Set<ConstraintViolation<Object>> violations = validator.validate(o, new Class[0]);
/*    */     
/* 44 */     if (violations.isEmpty()) {
/* 45 */       return "";
/*    */     }
/*    */     
/* 48 */     StringBuilder sb = new StringBuilder();
/* 49 */     for (ConstraintViolation<Object> violation : violations) {
/* 50 */       if (sb.length() != 0) {
/* 51 */         sb.append("; ");
/*    */       }
/* 53 */       sb.append(violation.getMessage()).append("[").append(violation.getPropertyPath()).append("]");
/*    */     } 
/* 55 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/validate/ValidateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */