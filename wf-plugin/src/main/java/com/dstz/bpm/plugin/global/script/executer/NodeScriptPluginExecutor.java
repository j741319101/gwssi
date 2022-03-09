/*    */ package com.dstz.bpm.plugin.global.script.executer;
/*    */ 
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import com.dstz.bpm.plugin.global.script.def.NodeScriptPluginDef;
/*    */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class NodeScriptPluginExecutor
/*    */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, NodeScriptPluginDef>
/*    */ {
/*    */   @Resource
/*    */   IGroovyScriptEngine groovyScriptEngine;
/*    */   
/*    */   public Void execute(BpmExecutionPluginSession pluginSession, NodeScriptPluginDef pluginDef) {
/* 27 */     String script = pluginDef.getEvnetnScript(pluginSession.getEventType());
/* 28 */     if (StringUtil.isEmpty(script)) return null;
/*    */     
/* 30 */     this.groovyScriptEngine.execute(script, (Map)pluginSession);
/*    */     
/* 32 */     this.LOG.info("节点{}执行了{}事件脚本", pluginDef.getNodeId(), pluginSession.getEventType().getValue());
/* 33 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/script/executer/NodeScriptPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */