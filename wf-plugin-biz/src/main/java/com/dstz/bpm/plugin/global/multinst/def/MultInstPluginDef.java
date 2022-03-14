package com.dstz.bpm.plugin.global.multinst.def;

import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.ArrayList;
import java.util.List;

public class MultInstPluginDef extends AbstractBpmExecutionPluginDef {
   List<MultInst> multInsts = new ArrayList();

   public List<MultInst> getMultInsts() {
      return this.multInsts;
   }

   public void setMultInsts(List<MultInst> multInsts) {
      this.multInsts = multInsts;
   }
}
