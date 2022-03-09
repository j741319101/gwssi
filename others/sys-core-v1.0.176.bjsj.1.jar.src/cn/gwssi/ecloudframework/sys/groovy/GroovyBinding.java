/*    */ package cn.gwssi.ecloudframework.sys.groovy;
/*    */ 
/*    */ import groovy.lang.Binding;
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GroovyBinding
/*    */   extends Binding
/*    */ {
/* 18 */   private final Logger logger = LoggerFactory.getLogger(getClass());
/*    */   
/*    */   private Map<?, ?> variables;
/*    */   
/* 22 */   private static ThreadLocal<Map<String, Object>> localVars = new ThreadLocal<>();
/*    */   
/* 24 */   private static Map<String, Object> propertyMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public GroovyBinding() {}
/*    */   
/*    */   public void setThreadVariables(Map<String, Object> variables) {
/* 30 */     localVars.remove();
/* 31 */     localVars.set(variables);
/*    */   }
/*    */   
/*    */   public GroovyBinding(String[] args) {
/* 35 */     this();
/* 36 */     setVariable("args", args);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getVariable(String name) {
/* 41 */     Map<String, Object> map = localVars.get();
/* 42 */     Object result = null;
/* 43 */     if (map != null) {
/* 44 */       result = map.get(name);
/*    */     }
/* 46 */     if (result == null) {
/* 47 */       result = propertyMap.get(name);
/*    */     }
/*    */     
/* 50 */     if (result == null) {
/* 51 */       this.logger.warn("执行Groovy 语句时,Context 缺少 Variable ：{}", name);
/*    */     }
/*    */     
/* 54 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setVariable(String name, Object value) {
/* 59 */     if (localVars.get() == null) {
/* 60 */       Map<String, Object> vars = new LinkedHashMap<>();
/* 61 */       vars.put(name, value);
/* 62 */       localVars.set(vars);
/*    */     } else {
/* 64 */       ((Map<String, Object>)localVars.get()).put(name, value);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<?, ?> getVariables() {
/* 70 */     if (localVars.get() == null) {
/* 71 */       return new LinkedHashMap<>();
/*    */     }
/* 73 */     return localVars.get();
/*    */   }
/*    */   
/*    */   public void clearVariables() {
/* 77 */     localVars.remove();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getProperty(String property) {
/* 82 */     return propertyMap.get(property);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setProperty(String property, Object newValue) {
/* 87 */     propertyMap.put(property, newValue);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/groovy/GroovyBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */