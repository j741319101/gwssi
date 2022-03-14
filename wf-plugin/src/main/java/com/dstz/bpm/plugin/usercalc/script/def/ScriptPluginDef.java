package com.dstz.bpm.plugin.usercalc.script.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractUserCalcPluginDef;
import org.hibernate.validator.constraints.NotEmpty;

public class ScriptPluginDef extends AbstractUserCalcPluginDef {
   @NotEmpty(
      message = "脚本插件，脚本不能为空"
   )
   private String script = "";
   @NotEmpty(
      message = "脚本插件，脚本描述不能为空"
   )
   private String description = "";

   public String getScript() {
      return this.script;
   }

   public void setScript(String script) {
      this.script = script;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }
}
