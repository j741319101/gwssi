package cn.gwssi.ecloudframework.base.api.aop.annotion;

import cn.gwssi.ecloudframework.base.api.handler.OperateLogHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {
  boolean writeResponse() default true;
  
  boolean writeRequest() default true;
  
  Class<? extends OperateLogHandler> handler() default OperateLogHandler.class;
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/aop/annotion/OperateLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */