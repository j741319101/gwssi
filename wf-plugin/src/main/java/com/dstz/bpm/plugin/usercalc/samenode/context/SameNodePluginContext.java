package com.dstz.bpm.plugin.usercalc.samenode.context;

import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import com.dstz.bpm.plugin.usercalc.samenode.def.SameNodePluginDef;
import com.dstz.bpm.plugin.usercalc.samenode.executer.SameNodePluginExecutor;
import com.dstz.base.core.util.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SameNodePluginContext extends AbstractUserCalcPluginContext<SameNodePluginDef> {
   private static final long serialVersionUID = 919433269116580830L;

   public String getDescription() {
      SameNodePluginDef def = (SameNodePluginDef)this.getBpmPluginDef();
      return def == null ? "" : "节点：" + def.getNodeId();
   }

   public String getTitle() {
      return "相同节点执行人";
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return SameNodePluginExecutor.class;
   }

   protected SameNodePluginDef parseJson(JSONObject pluginJson) {
      SameNodePluginDef def = new SameNodePluginDef();
      String nodeId = JsonUtil.getString(pluginJson, "nodeId");
      def.setNodeId(nodeId);
      return def;
   }
}
