package com.dstz.bpm.plugin.global.script.def;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.HashMap;
import java.util.Map;

public class NodeScriptPluginDef extends AbstractBpmExecutionPluginDef {
   private String nodeId = "";
   private Map<EventType, String> script = new HashMap();

   public String getEvnetnScript(EventType event) {
      return (String)this.script.get(event);
   }

   public void setEvnetnScript(EventType event, String scritp) {
      this.script.put(event, scritp);
   }

   public Map<EventType, String> getScript() {
      return this.script;
   }

   public void setScript(Map<EventType, String> script) {
      this.script = script;
   }

   public String getNodeId() {
      return this.nodeId;
   }

   public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
   }
}
