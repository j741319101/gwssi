package com.dstz.bpm.plugin.usercalc.util;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmUserCalcPluginSession;
import com.dstz.bpm.plugin.node.userassign.def.UserAssignPluginDef;
import com.dstz.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UserCalcPreview {
   public static List<SysIdentity> calcNodeUsers(BpmNodeDef userNode, DefualtTaskActionCmd taskModel) {
      Iterator var2 = userNode.getBpmPluginContexts().iterator();

      while(var2.hasNext()) {
         BpmPluginContext bpmPluginContext = (BpmPluginContext)var2.next();
         BpmPluginDef bpmPluginDef = bpmPluginContext.getBpmPluginDef();
         if (bpmPluginDef instanceof UserAssignPluginDef) {
            UserAssignPluginDef userAssignPluginDef = (UserAssignPluginDef)bpmPluginDef;
            BpmExecutionPluginSession bpmTaskSession = BpmPluginSessionFactory.buildTaskPluginSession(taskModel, EventType.TASK_COMPLETE_EVENT);
            List<UserAssignRule> ruleList = userAssignPluginDef.getRuleList();
            if (!CollectionUtil.isEmpty(ruleList)) {
               DefaultBpmUserCalcPluginSession bpmUserCalcPluginSession = (DefaultBpmUserCalcPluginSession)BpmPluginSessionFactory.buildBpmUserCalcPluginSession(bpmTaskSession);
               bpmUserCalcPluginSession.setBusData(taskModel.getBusData());
               List<SysIdentity> bpmIdentities = UserAssignRuleCalc.calcUserAssign(bpmUserCalcPluginSession, ruleList, false);
               Collections.sort(bpmIdentities);
               return bpmIdentities;
            }
         }
      }

      List<SysIdentity> sysIdentities = new ArrayList();
      switch(userNode.getType()) {
      case END:
         sysIdentities.add(new DefaultIdentity("TRUE", "", "NO_USER_ABLE", "-"));
         break;
      case SERVICETASK:
         sysIdentities.add(new DefaultIdentity("TRUE", "", "NO_USER_ABLE", "-"));
      }

      return sysIdentities;
   }
}
