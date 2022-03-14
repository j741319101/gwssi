package com.dstz.bpm.plugin.node.ruleskip.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.node.ruleskip.def.JumpRule;
import com.dstz.bpm.plugin.node.ruleskip.def.RuleSkipPluginDef;
import com.dstz.bpm.plugin.node.ruleskip.executer.RuleSkipPluginExecutor;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RuleSkipPluginContext extends AbstractBpmPluginContext<RuleSkipPluginDef> {
   private static final long serialVersionUID = 8784633971785686365L;

   public List getEventTypes() {
      List<EventType> eventTypes = new ArrayList();
      eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
      return eventTypes;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return RuleSkipPluginExecutor.class;
   }

   public String getTitle() {
      return "规则跳转";
   }

   protected RuleSkipPluginDef parseFromJson(JSON json) {
      RuleSkipPluginDef def = new RuleSkipPluginDef();
      List<JumpRule> list = JSON.parseArray(json.toJSONString(), JumpRule.class);
      def.setJumpRules(list);
      return def;
   }
}
