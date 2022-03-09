/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.script.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.script.def.NodeScriptPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.api.constant.BaseStatusCode;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
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