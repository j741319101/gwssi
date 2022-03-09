/*     */ package cn.gwssi.ecloudframework.base.core.spring;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.aop.annotion.ECloudScheduled;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.cron.CronUtil;
/*     */ import cn.hutool.cron.task.Task;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ECloudScheduledBeanPostProcessor
/*     */   implements BeanPostProcessor, DisposableBean, ApplicationListener<ContextRefreshedEvent>
/*     */ {
/*  32 */   private static final Logger logger = LoggerFactory.getLogger(ECloudScheduledBeanPostProcessor.class);
/*     */   
/*     */   @Autowired
/*     */   private ApplicationContext applicationContext;
/*     */   
/*  37 */   private Map<Object, Set<Method>> scheduledMethodMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   private final boolean enableScheduled;
/*     */ 
/*     */ 
/*     */   
/*     */   public ECloudScheduledBeanPostProcessor(boolean enableScheduled) {
/*  45 */     this.enableScheduled = enableScheduled;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
/*  50 */     if (this.enableScheduled) {
/*  51 */       Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
/*  52 */       Set<Method> methods = MethodIntrospector.selectMethods(targetClass, method -> (AnnotationUtils.getAnnotation(method, ECloudScheduled.class) != null));
/*  53 */       if (CollUtil.isNotEmpty(methods)) {
/*  54 */         this.scheduledMethodMap.put(bean, methods);
/*     */       }
/*     */     } 
/*  57 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/*  62 */     CronUtil.stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ContextRefreshedEvent event) {
/*  67 */     if (event.getApplicationContext().getParent() == null || "bootstrap".equals(event.getApplicationContext().getParent().getId())) {
/*  68 */       finishRegistrar();
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void finishRegistrar() {
/*  73 */     if (this.enableScheduled && this.scheduledMethodMap != null && !this.scheduledMethodMap.isEmpty()) {
/*  74 */       CronUtil.setMatchSecond(true);
/*  75 */       CronUtil.start();
/*  76 */       Map<String, ECloudScheduledExceptionHandler> eCloudScheduledExceptionHandlerMap = this.applicationContext.getBeansOfType(ECloudScheduledExceptionHandler.class);
/*  77 */       for (Map.Entry<Object, Set<Method>> entry : this.scheduledMethodMap.entrySet()) {
/*  78 */         for (Method method : entry.getValue()) {
/*  79 */           ECloudScheduled scheduled = method.<ECloudScheduled>getAnnotation(ECloudScheduled.class);
/*  80 */           CronUtil.schedule(scheduled.cron(), new CronTask(entry.getKey(), method, initialMethodParameterZero(method.getParameterTypes()), eCloudScheduledExceptionHandlerMap.values()));
/*     */         } 
/*     */       } 
/*  83 */       this.scheduledMethodMap.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] initialMethodParameterZero(Class[] parameterTypeClass) {
/*  94 */     if (parameterTypeClass == null || parameterTypeClass.length == 0) {
/*  95 */       return null;
/*     */     }
/*  97 */     Object[] parameter = new Object[parameterTypeClass.length];
/*  98 */     for (int i = 0, l = parameterTypeClass.length; i < l; i++) {
/*  99 */       Class parameterType = parameterTypeClass[i];
/* 100 */       Object value = null;
/* 101 */       if (boolean.class.equals(parameterType)) {
/* 102 */         value = Boolean.valueOf(false);
/* 103 */       } else if (parameterType.isPrimitive()) {
/* 104 */         value = Byte.valueOf((byte)0);
/*     */       } 
/* 106 */       parameter[i] = value;
/*     */     } 
/* 108 */     return parameter;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class CronTask
/*     */     implements Task
/*     */   {
/*     */     private Object object;
/*     */     
/*     */     private Method method;
/*     */     
/*     */     private Object[] methodParameter;
/*     */     private Collection<ECloudScheduledExceptionHandler> eCloudScheduledExceptionHandlers;
/*     */     
/*     */     CronTask(Object object, Method method, Object[] methodParameter, Collection<ECloudScheduledExceptionHandler> eCloudScheduledExceptionHandlers) {
/* 123 */       this.object = object;
/* 124 */       this.method = method;
/* 125 */       this.methodParameter = methodParameter;
/* 126 */       this.eCloudScheduledExceptionHandlers = eCloudScheduledExceptionHandlers;
/*     */     }
/*     */ 
/*     */     
/*     */     public void execute() {
/*     */       try {
/* 132 */         this.method.invoke(this.object, this.methodParameter);
/* 133 */       } catch (Throwable t) {
/* 134 */         notifyExceptionHandler(t);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void notifyExceptionHandler(Throwable throwable) {
/* 139 */       if (this.eCloudScheduledExceptionHandlers == null || this.eCloudScheduledExceptionHandlers.isEmpty()) {
/* 140 */         ECloudScheduledBeanPostProcessor.logger.error("schedule {} exception", this.method.toString(), throwable);
/*     */       } else {
/* 142 */         for (ECloudScheduledExceptionHandler ECloudScheduledExceptionHandler : this.eCloudScheduledExceptionHandlers)
/* 143 */           ECloudScheduledExceptionHandler.exception(this.object, this.method, throwable); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/spring/ECloudScheduledBeanPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */