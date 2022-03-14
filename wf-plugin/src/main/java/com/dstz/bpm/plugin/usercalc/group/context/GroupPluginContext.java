package com.dstz.bpm.plugin.usercalc.group.context;

import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import com.dstz.bpm.plugin.usercalc.group.def.GroupPluginDef;
import com.dstz.bpm.plugin.usercalc.group.executer.GroupPluginExecutor;
import com.dstz.base.core.util.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GroupPluginContext extends AbstractUserCalcPluginContext {
   private static final long serialVersionUID = -6084686546165511275L;

   public String getDescription() {
      GroupPluginDef def = (GroupPluginDef)this.getBpmPluginDef();
      return def == null ? "" : String.format("%s[%s]", def.getTypeName(), def.getGroupName());
   }

   public String getTitle() {
      return "组";
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return GroupPluginExecutor.class;
   }

   protected BpmUserCalcPluginDef parseJson(JSONObject pluginJson) {
      GroupPluginDef def = new GroupPluginDef();
      String groupType = JsonUtil.getString(pluginJson, "type");
      String groupTypeName = JsonUtil.getString(pluginJson, "typeName");
      def.setType(groupType);
      def.setTypeName(groupTypeName);
      String groupKey = JsonUtil.getString(pluginJson, "groupKey");
      String groupName = JsonUtil.getString(pluginJson, "groupName");
      def.setGroupKey(groupKey);
      def.setGroupName(groupName);
      return def;
   }
}
