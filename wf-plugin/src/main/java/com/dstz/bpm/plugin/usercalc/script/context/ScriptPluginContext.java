package com.dstz.bpm.plugin.usercalc.script.context;

import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import com.dstz.bpm.plugin.usercalc.script.def.ScriptPluginDef;
import com.dstz.bpm.plugin.usercalc.script.executer.ScriptPluginExecutor;
import com.dstz.base.core.util.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ScriptPluginContext extends AbstractUserCalcPluginContext<ScriptPluginDef> {
   private static final long serialVersionUID = -2353875054502587417L;

   public String getDescription() {
      ScriptPluginDef def = (ScriptPluginDef)this.getBpmPluginDef();
      return def == null ? "" : def.getDescription();
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return ScriptPluginExecutor.class;
   }

   public String getTitle() {
      return "脚本";
   }

   protected ScriptPluginDef parseJson(JSONObject pluginJson) {
      ScriptPluginDef def = new ScriptPluginDef();
      String script = pluginJson.getString("script");
      String description = JsonUtil.getString(pluginJson, "description", "脚本");
      def.setScript(script);
      def.setDescription(description);
      return def;
   }
}
