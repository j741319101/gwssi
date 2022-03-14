package com.dstz.base.api.aop.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SysThreeMemberSecurityAnno {
  String account() default "";
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/aop/annotion/SysThreeMemberSecurityAnno.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */