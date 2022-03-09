package cn.gwssi.ecloudframework.base.core.validate.annotation;

import cn.gwssi.ecloudframework.base.core.validate.constant.CaseMode;
import cn.gwssi.ecloudframework.base.core.validate.validator.CheckCaseValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CheckCaseValidator.class})
@Documented
public @interface CheckCase {
  String message() default "";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  CaseMode value();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/validate/annotation/CheckCase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */