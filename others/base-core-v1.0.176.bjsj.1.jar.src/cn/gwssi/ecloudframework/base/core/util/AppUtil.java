/*     */ package cn.gwssi.ecloudframework.base.core.util;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.constant.BaseStatusCode;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.hutool.core.util.ArrayUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class AppUtil
/*     */   implements ApplicationContextAware
/*     */ {
/*  27 */   protected static final Logger LOGGER = LoggerFactory.getLogger(AppUtil.class);
/*     */ 
/*     */   
/*     */   private static ApplicationContext context;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext _context) throws BeansException {
/*  35 */     context = _context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ApplicationContext getApplicaitonContext() {
/*  45 */     return context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getBean(String beanId) {
/*     */     try {
/*  58 */       return context.getBean(beanId);
/*  59 */     } catch (Exception ex) {
/*  60 */       LOGGER.debug("getBean:" + beanId + "," + ex.getMessage());
/*     */       
/*  62 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T getBean(Class<T> beanClass) {
/*     */     try {
/*  75 */       return (T)context.getBean(beanClass);
/*  76 */     } catch (Exception ex) {
/*  77 */       LOGGER.debug("getBean:" + beanClass + "," + ex.getMessage());
/*     */       
/*  79 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Map<String, T> getImplInstance(Class<T> clazz) {
/*  90 */     return context.getBeansOfType(clazz);
/*     */   }
/*     */   
/*     */   public static <T> List<T> getImplInstanceArray(Class<T> clazz) {
/*  94 */     List<T> list = new ArrayList<>();
/*     */     
/*  96 */     Map<String, T> map = context.getBeansOfType(clazz);
/*     */     
/*  98 */     for (T t : map.values()) {
/*  99 */       list.add(t);
/*     */     }
/* 101 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void publishEvent(ApplicationEvent event) {
/* 111 */     if (context != null) {
/* 112 */       context.publishEvent(event);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private static String currentProfiles = null;
/*     */   
/*     */   public static String getCtxEnvironment() {
/* 127 */     if (currentProfiles != null) {
/* 128 */       return currentProfiles;
/*     */     }
/*     */     
/* 131 */     Environment environment = context.getEnvironment();
/* 132 */     String[] activeProfiles = environment.getActiveProfiles();
/*     */     
/* 134 */     if (ArrayUtil.isNotEmpty((Object[])activeProfiles)) {
/* 135 */       currentProfiles = activeProfiles[0];
/* 136 */       return currentProfiles;
/*     */     } 
/*     */     
/* 139 */     String[] defaultProfiles = environment.getDefaultProfiles();
/* 140 */     if (ArrayUtil.isNotEmpty((Object[])defaultProfiles)) {
/* 141 */       currentProfiles = defaultProfiles[0];
/* 142 */       return defaultProfiles[0];
/*     */     } 
/*     */     
/* 145 */     throw new BusinessException("查找不到正确的环境属性配置！", BaseStatusCode.SYSTEM_ERROR);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/AppUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */