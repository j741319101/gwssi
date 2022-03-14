package com.dstz.bpm.plugin.global.taskskip.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;

public class TaskSkipPluginDef extends AbstractBpmExecutionPluginDef {
   private String skipTypeArr;
   private String script = "";

   public String getScript() {
      return this.script;
   }

   public void setScript(String script) {
      this.script = script;
   }

   public String getSkipTypeArr() {
      return this.skipTypeArr;
   }

   public void setSkipTypeArr(String skipTypeArr) {
      this.skipTypeArr = skipTypeArr;
   }
}
