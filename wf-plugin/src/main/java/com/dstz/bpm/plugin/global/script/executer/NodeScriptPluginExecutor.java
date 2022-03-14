package com.dstz.bpm.plugin.global.script.executer;

import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.plugin.global.script.def.NodeScriptPluginDef;
import com.dstz.base.api.constant.BaseStatusCode;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class NodeScriptPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, NodeScriptPluginDef> {
   @Resource
   IGroovyScriptEngine groovyScriptEngine;

   public Void execute(BpmExecutionPluginSession pluginSession, NodeScriptPluginDef pluginDef) {
      String script = pluginDef.getEvnetnScript(pluginSession.getEventType());
      if (StringUtil.isEmpty(script)) {
         return null;
      } else {
         try {
            this.groovyScriptEngine.execute(script, pluginSession);
         } catch (Exception var5) {
            throw new BusinessException(script + "脚本执行错误: " + var5.getMessage(), BaseStatusCode.SYSTEM_ERROR);
         }

         this.LOG.info("节点{}执行了{}事件脚本", pluginDef.getNodeId(), pluginSession.getEventType().getValue());
         return null;
      }
   }
}
