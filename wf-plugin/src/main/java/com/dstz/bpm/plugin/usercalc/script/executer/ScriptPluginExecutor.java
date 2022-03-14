package com.dstz.bpm.plugin.usercalc.script.executer;

import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import com.dstz.bpm.plugin.usercalc.script.def.ScriptPluginDef;
import com.dstz.base.api.constant.BaseStatusCode;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ScriptPluginExecutor extends AbstractUserCalcPlugin<ScriptPluginDef> {
   @Resource
   IGroovyScriptEngine groovyScriptEngine;

   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, ScriptPluginDef def) {
      String script = def.getScript();
      if (StringUtil.isEmpty(script)) {
         return Collections.EMPTY_LIST;
      } else {
         ArrayList list = new ArrayList();

         try {
            Set<SysIdentity> set = (Set)this.groovyScriptEngine.executeObject(script, pluginSession);
            if (CollectionUtil.isEmpty(set)) {
               return list;
            } else {
               list.addAll(set);
               return list;
            }
         } catch (Exception var6) {
            throw new BusinessException(script + " 脚本执行错误: " + var6.getMessage(), BaseStatusCode.SYSTEM_ERROR);
         }
      }
   }

   public boolean supportPreView() {
      return false;
   }
}
