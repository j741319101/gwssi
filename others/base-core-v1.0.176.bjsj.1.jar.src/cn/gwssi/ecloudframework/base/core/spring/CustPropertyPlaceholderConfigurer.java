/*    */ package com.dstz.base.core.spring;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
/*    */ import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
/*    */ import org.springframework.context.EnvironmentAware;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.core.env.PropertiesPropertySource;
/*    */ import org.springframework.core.env.PropertySource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustPropertyPlaceholderConfigurer
/*    */   extends PropertyPlaceholderConfigurer
/*    */   implements IProperty, EnvironmentAware
/*    */ {
/*    */   private Environment environment;
/*    */   
/*    */   protected void convertProperties(Properties props) {
/* 22 */     ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment)this.environment;
/* 23 */     Properties newProperties = new Properties();
/* 24 */     for (Map.Entry<Object, Object> propEntry : props.entrySet()) {
/* 25 */       if (!configurableEnvironment.containsProperty(String.valueOf(propEntry.getKey()))) {
/* 26 */         newProperties.put(propEntry.getKey(), propEntry.getValue());
/*    */       }
/*    */     } 
/* 29 */     configurableEnvironment.getPropertySources().addLast((PropertySource)new PropertiesPropertySource("ecloudframework", newProperties));
/* 30 */     super.convertProperties(props);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue(String key) {
/* 38 */     return this.environment.getProperty(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEnvironment(Environment environment) {
/* 43 */     this.environment = environment;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/spring/CustPropertyPlaceholderConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */