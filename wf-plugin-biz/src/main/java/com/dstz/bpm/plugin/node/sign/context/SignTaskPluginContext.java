package com.dstz.bpm.plugin.node.sign.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.executer.SignTaskPluginExecuter;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SignTaskPluginContext extends AbstractBpmPluginContext<SignTaskPluginDef> {
   private static final long serialVersionUID = 8784633971785686365L;

   public List getEventTypes() {
      List<EventType> eventTypes = new ArrayList();
      eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
      eventTypes.add(EventType.TASK_CREATE_EVENT);
      eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
      return eventTypes;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return SignTaskPluginExecuter.class;
   }

   public String getTitle() {
      return "会签任务插件";
   }

   protected SignTaskPluginDef parseFromJson(JSON json) {
      SignTaskPluginDef def = (SignTaskPluginDef)JSON.toJavaObject(json, SignTaskPluginDef.class);
      return def;
   }
}
