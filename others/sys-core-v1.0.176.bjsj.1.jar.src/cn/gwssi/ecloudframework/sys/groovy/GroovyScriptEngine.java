/*     */ package cn.gwssi.ecloudframework.sys.groovy;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IScript;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.ScriptLog;
/*     */ import groovy.lang.GroovyShell;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class GroovyScriptEngine
/*     */   implements IGroovyScriptEngine, ApplicationListener<ContextRefreshedEvent>
/*     */ {
/*  30 */   private final Logger logger = LoggerFactory.getLogger(getClass());
/*  31 */   private GroovyBinding groovyBinding = new GroovyBinding();
/*     */   
/*     */   @Resource
/*     */   private ScriptLog log;
/*     */   
/*     */   @Value("${ecloud.groovy.blackKeywords:System,Runtime,BeanUtil,AppUtil,JdbcTemplate,FileUtil,InputStream,IoUtil,FileWriter,ReflectUtil,ClassUtil}")
/*     */   private String filters;
/*     */   private List<String> blackKeywords;
/*     */   
/*     */   private List<String> getBackKeywordsList() {
/*  41 */     if (this.blackKeywords != null) return this.blackKeywords;
/*     */     
/*  43 */     this.blackKeywords = Arrays.asList(this.filters.split(","));
/*  44 */     return this.blackKeywords;
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(String script) {
/*  49 */     executeObject(script, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(String script, Map<String, Object> vars) {
/*  54 */     executeObject(script, vars);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean executeBoolean(String script, Map<String, Object> vars) {
/*  59 */     return ((Boolean)executeObject(script, vars)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String executeString(String script, Map<String, Object> vars) {
/*  64 */     return (String)executeObject(script, vars);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeInt(String script, Map<String, Object> vars) {
/*  70 */     return ((Integer)executeObject(script, vars)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public float executeFloat(String script, Map<String, Object> vars) {
/*  75 */     return ((Float)executeObject(script, vars)).floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object executeObject(String script, Map<String, Object> vars) {
/*  80 */     if (script == null) {
/*  81 */       return null;
/*     */     }
/*     */     
/*  84 */     for (String keyWords : getBackKeywordsList()) {
/*  85 */       if (StringUtils.isEmpty(keyWords))
/*     */         continue; 
/*  87 */       if (script.indexOf(keyWords) != -1) {
/*  88 */         throw new BusinessException(String.format("GroovyScriptEngine 执行失败，使用了黑名单中的关键词：[%s]，请修改脚本：%s", new Object[] { keyWords, script }));
/*     */       }
/*     */     } 
/*     */     
/*  92 */     if (vars != null) {
/*  93 */       vars.put("log", this.log);
/*     */     }
/*  95 */     this.groovyBinding.setThreadVariables(vars);
/*     */     
/*  97 */     if (this.logger.isDebugEnabled()) {
/*  98 */       this.logger.debug("执行:{}", script);
/*  99 */       this.logger.debug("variables:{}", vars + "");
/*     */     } 
/*     */     
/* 102 */     GroovyShell shell = new GroovyShell(this.groovyBinding);
/*     */ 
/*     */ 
/*     */     
/* 106 */     script = script.replace("&apos;", "'").replace("&quot;", "\"").replace("&gt;", ">").replace("&lt;", "<").replace("&nuot;", "\n").replace("&amp;", "&");
/*     */     
/* 108 */     Object rtn = shell.evaluate(script);
/* 109 */     return rtn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ContextRefreshedEvent event) {
/* 114 */     if (event.getApplicationContext().getParent() == null || "bootstrap".equals(event.getApplicationContext().getParent().getId())) {
/* 115 */       Map<String, IScript> scirptImpls = AppUtil.getImplInstance(IScript.class);
/* 116 */       for (Map.Entry<String, IScript> scriptMap : scirptImpls.entrySet()) {
/* 117 */         this.groovyBinding.setProperty(scriptMap.getKey(), scriptMap.getValue());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String executeFloat(String script, Map<String, Object> vars, String invalid) {
/* 125 */     this.groovyBinding.setThreadVariables(vars);
/* 126 */     GroovyShell shell = new GroovyShell(this.groovyBinding);
/*     */ 
/*     */ 
/*     */     
/* 130 */     script = script.replace("&apos;", "'").replace("&quot;", "\"").replace("&gt;", ">").replace("&lt;", "<").replace("&nuot;", "\n").replace("&amp;", "&");
/*     */     
/* 132 */     Object rtn = shell.evaluate(script);
/* 133 */     return (String)rtn;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/groovy/GroovyScriptEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */