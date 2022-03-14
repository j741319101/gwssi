/*    */ package com.dstz.bpm.plugin.global.script.executer;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import com.dstz.bpm.plugin.global.script.def.GlobalScript;
/*    */ import com.dstz.bpm.plugin.global.script.def.GlobalScriptPluginDef;
/*    */ import com.dstz.base.api.constant.BaseStatusCode;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class GlobalScriptPluginExecutor
/*    */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, GlobalScriptPluginDef>
/*    */ {
/*    */   @Resource
/*    */   IGroovyScriptEngine groovyScriptEngine;
/* 26 */   private static ThreadLocal<Boolean> canExecute = new ThreadLocal<>();
/*    */   
/*    */   public Void execute(BpmExecutionPluginSession pluginSession, GlobalScriptPluginDef pluginDef) {
/* 29 */     if (canExecute.get() != null && !((Boolean)canExecute.get()).booleanValue()) {
/* 30 */       return null;
/*    */     }
/* 32 */     if (CollectionUtil.isEmpty(pluginDef.getGlobalScripts())) {
/* 33 */       return null;
/*    */     }
/* 35 */     pluginDef.getGlobalScripts().forEach(globalScript -> {
/*    */           if (StringUtil.isEmpty(globalScript.getScript())) {
/*    */             return;
/*    */           }
/*    */ 
/*    */           
/*    */           if (StringUtil.isNotEmpty(globalScript.getEventKeys()) && globalScript.getEventKeys().indexOf(pluginSession.getEventType().getKey()) == -1) {
/*    */             return;
/*    */           }
/*    */           
/*    */           try {
/*    */             this.groovyScriptEngine.execute(globalScript.getScript(), (Map)pluginSession);
/* 47 */           } catch (Exception e) {
/*    */             throw new BusinessException(globalScript.getScript() + "脚本执行错误: " + e.getMessage(), BaseStatusCode.SYSTEM_ERROR);
/*    */           } 
/*    */           
/*    */           this.LOG.info("执行全局事件脚本，事件为：{}", pluginSession.getEventType().getValue());
/*    */         });
/* 53 */     return null;
/*    */   }
/*    */   
/*    */   public static void setCanExecute(Boolean execute) {
/* 57 */     canExecute.set(execute);
/*    */   }
/*    */   public static void clearCanExecute() {
/* 60 */     canExecute.remove();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/script/executer/GlobalScriptPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */