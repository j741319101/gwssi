package com.dstz.bpm.plugin.node.userassign.executer;

import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import com.dstz.bpm.plugin.node.userassign.def.UserAssignPluginDef;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UserAssignPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, UserAssignPluginDef> {
   @Resource
   IGroovyScriptEngine groovyScriptEngine;
   @Resource
   BpmProcessDefService processsDefService;

   public Void execute(BpmExecutionPluginSession pluginSession, UserAssignPluginDef assignPluginDef) {
      TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
      List<SysIdentity> identityList = model.getBpmIdentity(model.getNodeId());
      if (CollectionUtil.isNotEmpty(identityList)) {
         return null;
      } else {
         List<UserAssignRule> ruleList = assignPluginDef.getRuleList();
         BpmUserCalcPluginSession bpmUserCalcPluginSession = BpmPluginSessionFactory.buildBpmUserCalcPluginSession(pluginSession);
         List<SysIdentity> bpmIdentities = UserAssignRuleCalc.calcUserAssign(bpmUserCalcPluginSession, ruleList, false);
         if (bpmIdentities.isEmpty()) {
            DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.processsDefService.getBpmProcessDef(model.getDefId());
            if (!processDef.getExtProperties().isAllowExecutorEmpty()) {
               throw new BusinessMessage(String.format("%s节点 任务候选人不能为空!", model.getBpmTask().getName()), BpmStatusCode.NO_TASK_USER);
            }
         }

         List<SysIdentity> identitieList = new ArrayList();
         Iterator var9 = bpmIdentities.iterator();

         while(var9.hasNext()) {
            SysIdentity identity = (SysIdentity)var9.next();
            if (identity != null) {
               identitieList.add(identity);
            }
         }

         this.LOG.debug("用户计算插件执行完毕，解析到【{}】条有效人员信息。节点:{}", identitieList.size(), model.getNodeId());
         this.LOG.trace(JSON.toJSONString(identitieList));
         model.setBpmIdentity(model.getNodeId(), identitieList);
         return null;
      }
   }
}
