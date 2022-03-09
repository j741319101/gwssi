/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.userassign.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.UserAssignRule;
/*    */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.factory.BpmPluginSessionFactory;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.userassign.def.UserAssignPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*    */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class UserAssignPluginExecutor
/*    */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, UserAssignPluginDef>
/*    */ {
/*    */   @Resource
/*    */   IGroovyScriptEngine groovyScriptEngine;
/*    */   @Resource
/*    */   BpmProcessDefService processsDefService;
/*    */   
/*    */   public Void execute(BpmExecutionPluginSession pluginSession, UserAssignPluginDef assignPluginDef) {
/* 38 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/*    */     
/* 40 */     List<SysIdentity> identityList = model.getBpmIdentity(model.getNodeId());
/*    */ 
/*    */     
/* 43 */     if (CollectionUtil.isNotEmpty(identityList)) {
/* 44 */       return null;
/*    */     }
/*    */     
/* 47 */     List<UserAssignRule> ruleList = assignPluginDef.getRuleList();
/*    */     
/* 49 */     BpmUserCalcPluginSession bpmUserCalcPluginSession = BpmPluginSessionFactory.buildBpmUserCalcPluginSession((BpmPluginSession)pluginSession);
/* 50 */     List<SysIdentity> bpmIdentities = UserAssignRuleCalc.calcUserAssign(bpmUserCalcPluginSession, ruleList, Boolean.valueOf(false));
/*    */ 
/*    */     
/* 53 */     if (bpmIdentities.isEmpty()) {
/* 54 */       DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.processsDefService.getBpmProcessDef(model.getDefId());
/* 55 */       if (!processDef.getExtProperties().isAllowExecutorEmpty()) {
/* 56 */         throw new BusinessMessage(String.format("%s节点 任务候选人不能为空!", new Object[] { model.getBpmTask().getName() }), BpmStatusCode.NO_TASK_USER);
/*    */       }
/*    */     } 
/*    */     
/* 60 */     List<SysIdentity> identitieList = new ArrayList<>();
/* 61 */     for (SysIdentity identity : bpmIdentities) {
/* 62 */       if (identity == null)
/* 63 */         continue;  identitieList.add(identity);
/*    */     } 
/*    */     
/* 66 */     this.LOG.debug("用户计算插件执行完毕，解析到【{}】条有效人员信息。节点:{}", Integer.valueOf(identitieList.size()), model.getNodeId());
/* 67 */     this.LOG.trace(JSON.toJSONString(identitieList));
/*    */     
/* 69 */     model.setBpmIdentity(model.getNodeId(), identitieList);
/* 70 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/userassign/executer/UserAssignPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */