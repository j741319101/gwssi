/*    */ package com.dstz.bpm.plugin.node.userassign.executer;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*    */ import com.dstz.bpm.api.engine.context.BpmContext;
/*    */ import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
/*    */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*    */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*    */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*    */ import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmPluginSession;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import com.dstz.bpm.plugin.node.userassign.def.UserAssignPluginDef;
/*    */ import com.dstz.base.api.exception.BusinessMessage;
/*    */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*    */ import com.dstz.sys.api.model.SysIdentity;
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