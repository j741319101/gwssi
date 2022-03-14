package com.dstz.bpm.plugin.usercalc.approver.context;

import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import com.dstz.bpm.plugin.usercalc.approver.def.ApproverPluginDef;
import com.dstz.bpm.plugin.usercalc.approver.executer.ApproverPluginExecutor;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ApproverPluginContext extends AbstractUserCalcPluginContext<ApproverPluginDef> {
   private static final long serialVersionUID = 2164348894650802414L;

   public String getDescription() {
      return "流程历史审批人";
   }

   public String getTitle() {
      return "流程历史审批人";
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return ApproverPluginExecutor.class;
   }

   protected ApproverPluginDef parseJson(JSONObject pluginJson) {
      ApproverPluginDef def = new ApproverPluginDef();
      return def;
   }
}
