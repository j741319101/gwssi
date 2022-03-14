package com.dstz.bpm.plugin.usercalc.samenode.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractUserCalcPluginDef;
import org.hibernate.validator.constraints.NotEmpty;

public class SameNodePluginDef extends AbstractUserCalcPluginDef {
   @NotEmpty(
      message = "人员插件相同节点执行人，节点ID不能为空"
   )
   private String nodeId = "";

   public String getNodeId() {
      return this.nodeId;
   }

   public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
   }
}
