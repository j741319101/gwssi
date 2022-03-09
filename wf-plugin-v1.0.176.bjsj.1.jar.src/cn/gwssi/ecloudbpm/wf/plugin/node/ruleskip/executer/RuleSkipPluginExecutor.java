/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.ruleskip.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.ruleskip.def.JumpRule;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.ruleskip.def.RuleSkipPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
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
/*    */ public class RuleSkipPluginExecutor
/*    */   extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, RuleSkipPluginDef>
/*    */ {
/*    */   @Resource
/*    */   IGroovyScriptEngine scriptEngine;
/*    */   
/*    */   public Void execute(DefaultBpmTaskPluginSession pluginSession, RuleSkipPluginDef pluginDef) {
/* 28 */     if (CollectionUtil.isEmpty(pluginDef.getJumpRules())) return null;
/*    */ 
/*    */     
/* 31 */     TaskActionCmd taskAction = (TaskActionCmd)BpmContext.getActionModel();
/* 32 */     if (StringUtil.isNotEmpty(taskAction.getDestination())) {
/* 33 */       this.LOG.info("任务【{}】已经指定了跳转节点【{}】，规则跳转将忽略", pluginSession.getBpmTask().getName(), taskAction.getDestination());
/* 34 */       return null;
/*    */     } 
/*    */     
/* 37 */     for (JumpRule jumpRule : pluginDef.getJumpRules()) {
/* 38 */       if (StringUtil.isEmpty(jumpRule.getScript()) || StringUtil.isEmpty(jumpRule.getScript())) {
/*    */         continue;
/*    */       }
/*    */       
/* 42 */       boolean isJump = this.scriptEngine.executeBoolean(jumpRule.getScript(), (Map)pluginSession);
/*    */       
/* 44 */       if (isJump) {
/* 45 */         taskAction.setDestination(jumpRule.getTargetNode());
/*    */         
/* 47 */         this.LOG.info("节点【{}】规则跳转【{}】条件满足，即将跳转至【{}】", new Object[] { pluginSession.getBpmTask().getName(), jumpRule.getName(), jumpRule.getTargetNode() });
/* 48 */         this.LOG.debug(jumpRule.getScript());
/* 49 */         return null;
/*    */       } 
/*    */     } 
/*    */     
/* 53 */     this.LOG.info("节点{}规则跳转，共{}条，均不符合条件，将正常跳转", pluginSession.getBpmTask().getName(), Integer.valueOf(pluginDef.getJumpRules().size()));
/* 54 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/ruleskip/executer/RuleSkipPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */