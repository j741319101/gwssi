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
/* 29 */     for (BpmPluginContext bpmPluginContext : userNode.getBpmPluginContexts()) {
/* 30 */       BpmPluginDef bpmPluginDef = bpmPluginContext.getBpmPluginDef();
/* 31 */       if (!(bpmPluginDef instanceof UserAssignPluginDef)) {
/*    */         continue;
/*    */       }
/* 34 */       UserAssignPluginDef userAssignPluginDef = (UserAssignPluginDef)bpmPluginDef;
/* 35 */       BpmExecutionPluginSession bpmTaskSession = BpmPluginSessionFactory.buildTaskPluginSession((TaskActionCmd)taskModel, EventType.TASK_COMPLETE_EVENT);
/* 36 */       List<UserAssignRule> ruleList = userAssignPluginDef.getRuleList();
/* 37 */       if (CollectionUtil.isEmpty(ruleList)) {
/*    */         continue;
/*    */       }
/* 40 */       BpmUserCalcPluginSession bpmUserCalcPluginSession = BpmPluginSessionFactory.buildBpmUserCalcPluginSession((BpmPluginSession)bpmTaskSession);
/* 41 */       List<SysIdentity> bpmIdentities = UserAssignRuleCalc.calcUserAssign(bpmUserCalcPluginSession, ruleList, Boolean.valueOf(false));
/*    */ 
/*    */       
/* 44 */       Collections.sort(bpmIdentities);
/* 45 */       return bpmIdentities;
/*    */     } 
/* 47 */     List<SysIdentity> sysIdentities = new ArrayList<>();
/* 48 */     switch (userNode.getType()) {
/*    */       case END:
/* 50 */         sysIdentities.add(new DefaultIdentity("TRUE", "", "NO_USER_ABLE"));
/*    */         break;
/*    */       case SERVICETASK:
/* 53 */         sysIdentities.add(new DefaultIdentity("TRUE", "", "NO_USER_ABLE"));
/*    */         break;
/*    */     } 
/* 56 */     return sysIdentities;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/usercalc/util/UserCalcPreview.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */