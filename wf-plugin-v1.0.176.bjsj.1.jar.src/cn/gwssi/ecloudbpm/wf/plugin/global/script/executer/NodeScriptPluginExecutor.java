/*    */ package com.dstz.bpm.plugin.global.script.executer;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import com.dstz.bpm.plugin.global.script.def.NodeScriptPluginDef;
/*    */ import com.dstz.base.api.constant.BaseStatusCode;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.StringUtil;
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
/*    */ @Component
/*    */ public class NodeScriptPluginExecutor
/*    */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, NodeScriptPluginDef>
/*    */ {
/*    */   @Resource
/*    */   IGroovyScriptEngine groovyScriptEngine;
/*    */   
/*    */   public Void execute(BpmExecutionPluginSession pluginSession, NodeScriptPluginDef pluginDef) {
/* 28 */     String script = pluginDef.getEvnetnScript(pluginSession.getEventType());
/* 29 */     if (StringUtil.isEmpty(script)) return null; 
/*    */     try {
/* 31 */       this.groovyScriptEngine.execute(script, (Map)pluginSession);
/* 32 */     } catch (Exception e) {
/* 33 */       throw new BusinessException(script + "脚本执行错误: " + e.getMessage(), BaseStatusCode.SYSTEM_ERROR);
/*    */     } 
/* 35 */     this.LOG.info("节点{}执行了{}事件脚本", pluginDef.getNodeId(), pluginSession.getEventType().getValue());
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/script/executer/NodeScriptPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */