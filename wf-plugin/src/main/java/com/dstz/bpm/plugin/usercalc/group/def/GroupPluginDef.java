package com.dstz.bpm.plugin.usercalc.group.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractUserCalcPluginDef;
import org.hibernate.validator.constraints.NotEmpty;

public class GroupPluginDef extends AbstractUserCalcPluginDef {
   @NotEmpty(
      message = "人员插件组类型不能为空"
   )
   private String type = "";
   private String typeName = "";
   @NotEmpty(
      message = "人员插件组KEY不能为空"
   )
   private String groupKey = "";
   private String groupName = "";

   public String getGroupKey() {
      return this.groupKey;
   }

   public void setGroupKey(String groupKey) {
      this.groupKey = groupKey;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getTypeName() {
      return this.typeName;
   }

   public void setTypeName(String typeName) {
      this.typeName = typeName;
   }
}
