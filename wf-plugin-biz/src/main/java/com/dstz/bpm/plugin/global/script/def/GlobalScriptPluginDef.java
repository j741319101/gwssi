package com.dstz.bpm.plugin.global.script.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.List;

public class GlobalScriptPluginDef extends AbstractBpmExecutionPluginDef {
   private List<GlobalScript> globalScripts;

   public List<GlobalScript> getGlobalScripts() {
      return this.globalScripts;
   }

   public void setGlobalScripts(List<GlobalScript> globalScripts) {
      this.globalScripts = globalScripts;
   }
}
