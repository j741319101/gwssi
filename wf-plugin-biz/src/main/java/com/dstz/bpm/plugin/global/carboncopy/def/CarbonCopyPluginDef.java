package com.dstz.bpm.plugin.global.carboncopy.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.List;
import java.util.Map;

public class CarbonCopyPluginDef extends AbstractBpmExecutionPluginDef {
   private static final long serialVersionUID = 4987507273918000937L;
   private Map<String, List<BpmCarbonCopy>> nodeEventCarbonCopyMap;

   public Map<String, List<BpmCarbonCopy>> getNodeEventCarbonCopyMap() {
      return this.nodeEventCarbonCopyMap;
   }

   public void setNodeEventCarbonCopyMap(Map<String, List<BpmCarbonCopy>> nodeEventCarbonCopyMap) {
      this.nodeEventCarbonCopyMap = nodeEventCarbonCopyMap;
   }

   public static String getMapKey(String nodeId, String event) {
      return nodeId + "_" + event;
   }
}
