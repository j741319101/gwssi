/*    */ package com.dstz.bpm.plugin.usercalc.script.context;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
/*    */ import com.dstz.bpm.plugin.usercalc.script.def.ScriptPluginDef;
/*    */ import com.dstz.bpm.plugin.usercalc.script.executer.ScriptPluginExecutor;
/*    */ import com.dstz.base.core.util.JsonUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class ScriptPluginContext
/*    */   extends AbstractUserCalcPluginContext<ScriptPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = -2353875054502587417L;
/*    */   
/*    */   public String getDescription() {
/* 22 */     ScriptPluginDef def = (ScriptPluginDef)getBpmPluginDef();
/* 23 */     if (def == null) return ""; 
/* 24 */     return def.getDescription();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 29 */     return (Class)ScriptPluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 34 */     return "脚本";
/*    */   }
/*    */ 
/*    */   
/*    */   protected ScriptPluginDef parseJson(JSONObject pluginJson) {
/* 39 */     ScriptPluginDef def = new ScriptPluginDef();
/* 40 */     String script = pluginJson.getString("script");
/* 41 */     String description = JsonUtil.getString(pluginJson, "description", "脚本");
/* 42 */     def.setScript(script);
/* 43 */     def.setDescription(description);
/* 44 */     return def;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/script/context/ScriptPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */