package com.dstz.bpm.plugin.node.dynamictask.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;

public class DynamicTaskPluginDef extends AbstractBpmExecutionPluginDef {
   public static final String dynamicType_user = "user";
   public static final String dynamicType_interface = "interface";
   private Boolean isEnabled = false;
   private String dynamicType = "user";
   private Boolean isParallel = false;
   private String interfaceName;
   private boolean needSupervise = false;
   private boolean canBeRecycledEnd = false;
   private boolean reset = true;

   public Boolean getIsEnabled() {
      return this.isEnabled;
   }

   public void setIsEnabled(Boolean isEnabled) {
      this.isEnabled = isEnabled;
   }

   public String getDynamicType() {
      return this.dynamicType;
   }

   public void setDynamicType(String dynamicType) {
      this.dynamicType = dynamicType;
   }

   public Boolean getIsParallel() {
      return this.isParallel;
   }

   public void setIsParallel(Boolean isParallel) {
      this.isParallel = isParallel;
   }

   public String getInterfaceName() {
      return this.interfaceName;
   }

   public void setInterfaceName(String interfaceName) {
      this.interfaceName = interfaceName;
   }

   public boolean isNeedSupervise() {
      return this.needSupervise;
   }

   public void setNeedSupervise(boolean needSupervise) {
      this.needSupervise = needSupervise;
   }

   public boolean isCanBeRecycledEnd() {
      return this.canBeRecycledEnd;
   }

   public void setCanBeRecycledEnd(boolean canBeRecycledEnd) {
      this.canBeRecycledEnd = canBeRecycledEnd;
   }

   public boolean isReset() {
      return this.reset;
   }

   public void setReset(boolean reset) {
      this.reset = reset;
   }

   public static String getDynamicType_user() {
      return "user";
   }

   public static String getDynamicType_interface() {
      return "interface";
   }

   public Boolean getEnabled() {
      return this.isEnabled;
   }

   public void setEnabled(Boolean enabled) {
      this.isEnabled = enabled;
   }

   public Boolean getParallel() {
      return this.isParallel;
   }

   public void setParallel(Boolean parallel) {
      this.isParallel = parallel;
   }
}
