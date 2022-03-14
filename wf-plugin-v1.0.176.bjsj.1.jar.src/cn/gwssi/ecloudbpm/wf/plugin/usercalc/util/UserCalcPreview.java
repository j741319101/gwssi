/*    */ package com.dstz.bpm.plugin.usercalc.util;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.constant.NodeType;
/*    */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*    */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmPluginSession;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmUserCalcPluginSession;
/*    */ import com.dstz.bpm.plugin.node.userassign.def.UserAssignPluginDef;
/*    */ import com.dstz.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
/*    */ import com.dstz.sys.api.model.DefaultIdentity;
/*    */ import com.dstz.sys.api.model.SysIdentity;
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