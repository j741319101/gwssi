/*    */ package com.dstz.bpm.plugin.usercalc.script.executer;
/*    */ 
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import com.dstz.bpm.plugin.usercalc.script.def.ScriptPluginDef;
/*    */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*    */ import com.dstz.sys.api.model.SysIdentity;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ScriptPluginExecutor
/*    */   extends AbstractUserCalcPlugin<ScriptPluginDef>
/*    */ {
/*    */   @Resource
/*    */   IGroovyScriptEngine groovyScriptEngine;
/*    */   
/*    */   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, ScriptPluginDef def) {
/* 33 */     String script = def.getScript();
/* 34 */     if (StringUtil.isEmpty(script)) {
/* 35 */       return Collections.EMPTY_LIST;
/*    */     }
/*    */     
/* 38 */     Set<SysIdentity> set = (Set<SysIdentity>)this.groovyScriptEngine.executeObject(script, (Map)pluginSession);
/*    */     
/* 40 */     List<SysIdentity> list = new ArrayList<>();
/* 41 */     if (CollectionUtil.isEmpty(set)) return list;
/*    */     
/* 43 */     list.addAll(set);
/* 44 */     return list;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportPreView() {
/* 49 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/usercalc/script/executer/ScriptPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */