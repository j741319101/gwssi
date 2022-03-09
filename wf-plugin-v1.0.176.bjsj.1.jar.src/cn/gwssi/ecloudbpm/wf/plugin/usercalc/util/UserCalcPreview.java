/*    */ package cn.gwssi.ecloudbpm.wf.plugin.usercalc.util;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.BpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.UserAssignRule;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.factory.BpmPluginSessionFactory;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmUserCalcPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.userassign.def.UserAssignPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.userassign.executer.UserAssignRuleCalc;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserCalcPreview
/*    */ {
/*    */   public static List<SysIdentity> calcNodeUsers(BpmNodeDef userNode, DefualtTaskActionCmd taskModel) {
/* 30 */     for (BpmPluginContext bpmPluginContext : userNode.getBpmPluginContexts()) {
/* 31 */       BpmPluginDef bpmPluginDef = bpmPluginContext.getBpmPluginDef();
/* 32 */       if (!(bpmPluginDef instanceof UserAssignPluginDef)) {
/*    */         continue;
/*    */       }
/* 35 */       UserAssignPluginDef userAssignPluginDef = (UserAssignPluginDef)bpmPluginDef;
/* 36 */       BpmExecutionPluginSession bpmTaskSession = BpmPluginSessionFactory.buildTaskPluginSession((TaskActionCmd)taskModel, EventType.TASK_COMPLETE_EVENT);
/* 37 */       List<UserAssignRule> ruleList = userAssignPluginDef.getRuleList();
/* 38 */       if (CollectionUtil.isEmpty(ruleList)) {
/*    */         continue;
/*    */       }
/* 41 */       DefaultBpmUserCalcPluginSession bpmUserCalcPluginSession = (DefaultBpmUserCalcPluginSession)BpmPluginSessionFactory.buildBpmUserCalcPluginSession((BpmPluginSession)bpmTaskSession);
/* 42 */       bpmUserCalcPluginSession.setBusData(taskModel.getBusData());
/* 43 */       List<SysIdentity> bpmIdentities = UserAssignRuleCalc.calcUserAssign((BpmUserCalcPluginSession)bpmUserCalcPluginSession, ruleList, Boolean.valueOf(false));
/*    */ 
/*    */       
/* 46 */       Collections.sort(bpmIdentities);
/* 47 */       return bpmIdentities;
/*    */     } 
/* 49 */     List<SysIdentity> sysIdentities = new ArrayList<>();
/* 50 */     switch (userNode.getType()) {
/*    */       case END:
/* 52 */         sysIdentities.add(new DefaultIdentity("TRUE", "", "NO_USER_ABLE", "-"));
/*    */         break;
/*    */       case SERVICETASK:
/* 55 */         sysIdentities.add(new DefaultIdentity("TRUE", "", "NO_USER_ABLE", "-"));
/*    */         break;
/*    */     } 
/* 58 */     return sysIdentities;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/util/UserCalcPreview.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */