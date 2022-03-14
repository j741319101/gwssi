package com.dstz.bpm.plugin.node.ruleskip.executer;

import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.node.ruleskip.def.JumpRule;
import com.dstz.bpm.plugin.node.ruleskip.def.RuleSkipPluginDef;
import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import cn.hutool.core.collection.CollectionUtil;
import java.util.Iterator;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class RuleSkipPluginExecutor extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, RuleSkipPluginDef> {
   @Resource
   IGroovyScriptEngine scriptEngine;

   public Void execute(DefaultBpmTaskPluginSession pluginSession, RuleSkipPluginDef pluginDef) {
      if (CollectionUtil.isEmpty(pluginDef.getJumpRules())) {
         return null;
      } else {
         TaskActionCmd taskAction = (TaskActionCmd)BpmContext.getActionModel();
         if (StringUtil.isNotEmpty(taskAction.getDestination())) {
            this.LOG.info("任务【{}】已经指定了跳转节点【{}】，规则跳转将忽略", pluginSession.getBpmTask().getName(), taskAction.getDestination());
            return null;
         } else {
            Iterator var4 = pluginDef.getJumpRules().iterator();

            while(var4.hasNext()) {
               JumpRule jumpRule = (JumpRule)var4.next();
               if (!StringUtil.isEmpty(jumpRule.getScript()) && !StringUtil.isEmpty(jumpRule.getScript())) {
                  boolean isJump = this.scriptEngine.executeBoolean(jumpRule.getScript(), pluginSession);
                  if (isJump) {
                     taskAction.setDestination(jumpRule.getTargetNode());
                     this.LOG.info("节点【{}】规则跳转【{}】条件满足，即将跳转至【{}】", new Object[]{pluginSession.getBpmTask().getName(), jumpRule.getName(), jumpRule.getTargetNode()});
                     this.LOG.debug(jumpRule.getScript());
                     return null;
                  }
               }
            }

            this.LOG.info("节点{}规则跳转，共{}条，均不符合条件，将正常跳转", pluginSession.getBpmTask().getName(), pluginDef.getJumpRules().size());
            return null;
         }
      }
   }
}
