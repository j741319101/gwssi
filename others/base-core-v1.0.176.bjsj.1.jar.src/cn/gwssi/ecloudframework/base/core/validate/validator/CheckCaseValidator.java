/*    */ package com.dstz.base.core.validate.validator;
/*    */ 
/*    */ import com.dstz.base.core.validate.annotation.CheckCase;
/*    */ import com.dstz.base.core.validate.constant.CaseMode;
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ 
/*    */ public class CheckCaseValidator
/*    */   implements ConstraintValidator<CheckCase, String> {
/*    */   private CaseMode caseMode;
/*    */   
/*    */   public void initialize(CheckCase constraintAnnotation) {
/* 14 */     this.caseMode = constraintAnnotation.value();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
/* 19 */     if (object == null) {
/* 20 */       return true;
/*    */     }
/* 22 */     if (this.caseMode == CaseMode.UPPER) {
/* 23 */       return object.equals(object.toUpperCase());
/*    */     }
/* 25 */     return object.equals(object.toLowerCase());
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/validate/validator/CheckCaseValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */