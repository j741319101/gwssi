package com.dstz.bpm.plugin.global.script.executer;

import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.plugin.global.script.def.GlobalScriptPluginDef;
import com.dstz.base.api.constant.BaseStatusCode;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import cn.hutool.core.collection.CollectionUtil;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class GlobalScriptPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, GlobalScriptPluginDef> {
   @Resource
   IGroovyScriptEngine groovyScriptEngine;
   private static ThreadLocal<Boolean> canExecute = new ThreadLocal();

   public Void execute(BpmExecutionPluginSession pluginSession, GlobalScriptPluginDef pluginDef) {
      if (canExecute.get() != null && !(Boolean)canExecute.get()) {
         return null;
      } else if (CollectionUtil.isEmpty(pluginDef.getGlobalScripts())) {
         return null;
      } else {
         pluginDef.getGlobalScripts().forEach((globalScript) -> {
            if (!StringUtil.isEmpty(globalScript.getScript())) {
               if (!StringUtil.isNotEmpty(globalScript.getEventKeys()) || globalScript.getEventKeys().indexOf(pluginSession.getEventType().getKey()) != -1) {
                  try {
                     this.groovyScriptEngine.execute(globalScript.getScript(), pluginSession);
                  } catch (Exception var4) {
                     throw new BusinessException(globalScript.getScript() + "脚本执行错误: " + var4.getMessage(), BaseStatusCode.SYSTEM_ERROR);
                  }

                  this.LOG.info("执行全局事件脚本，事件为：{}", pluginSession.getEventType().getValue());
               }
            }
         });
         return null;
      }
   }

   public static void setCanExecute(Boolean execute) {
      canExecute.set(execute);
   }

   public static void clearCanExecute() {
      canExecute.remove();
   }
}
