/*    */ package cn.gwssi.ecloudframework.base.core.util;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.constant.BaseStatusCode;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.validation.ConstraintViolation;
/*    */ import javax.validation.Validation;
/*    */ import javax.validation.Validator;
/*    */ import javax.validation.ValidatorFactory;
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
/*    */ public class ValidateUtil
/*    */ {
/* 32 */   private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
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
/*    */   public static void validate(Object obj) {
/* 48 */     Validator validator = factory.getValidator();
/* 49 */     Set<ConstraintViolation<Object>> violations = validator.validate(obj, new Class[0]);
/*    */     
/* 51 */     if (violations.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     Map<Class<? extends Annotation>, List<ConstraintViolation<Object>>> resultMap = new HashMap<>();
/* 56 */     for (ConstraintViolation<Object> violation : violations) {
/* 57 */       List<ConstraintViolation<Object>> list = resultMap.get(violation.getConstraintDescriptor().getAnnotation().annotationType());
/* 58 */       if (list == null) {
/* 59 */         list = new ArrayList<>();
/* 60 */         resultMap.put(violation.getConstraintDescriptor().getAnnotation().annotationType(), list);
/*    */       } 
/* 62 */       list.add(violation);
/*    */     } 
/* 64 */     StringBuilder sb = new StringBuilder();
/* 65 */     for (Map.Entry<Class<? extends Annotation>, List<ConstraintViolation<Object>>> entry : resultMap.entrySet()) {
/* 66 */       sb.append("[");
/* 67 */       for (ConstraintViolation<Object> violation : entry.getValue()) {
/* 68 */         if (!sb.toString().endsWith("[")) {
/* 69 */           sb.append(",");
/*    */         }
/* 71 */         sb.append(violation.getRootBeanClass().getSimpleName() + "." + violation.getPropertyPath().toString());
/*    */       } 
/* 73 */       sb.append("]" + ((ConstraintViolation)((List<ConstraintViolation>)entry.getValue()).get(0)).getMessage() + ";");
/*    */     } 
/*    */     
/* 76 */     if (StringUtil.isNotEmpty(sb.toString()))
/* 77 */       throw new BusinessException(sb.toString(), BaseStatusCode.PARAM_ILLEGAL); 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/ValidateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */