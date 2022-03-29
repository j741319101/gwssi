package com.dstz.bpm.plugin.global.script.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import org.hibernate.validator.constraints.NotEmpty;

public class GlobalScript extends AbstractBpmExecutionPluginDef {
   private String eventKeys = "";
   @NotEmpty(
      message = "脚本"
   )
   private String script;

   public String getScript() {
      return this.script;
   }

   public void setScript(String script) {
      this.script = script;
   }

   public String getEventKeys() {
      return this.eventKeys;
   }

   public void setEventKeys(String eventKeys) {
      this.eventKeys = eventKeys;
   }
}
