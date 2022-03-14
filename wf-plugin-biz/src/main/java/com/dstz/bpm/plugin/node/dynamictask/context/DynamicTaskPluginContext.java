package com.dstz.bpm.plugin.node.dynamictask.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.dynamictask.executer.DynamicTaskPluginExecuter;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DynamicTaskPluginContext extends AbstractBpmPluginContext<DynamicTaskPluginDef> {
   private static final long serialVersionUID = 8784633971785686365L;

   public List getEventTypes() {
      List<EventType> eventTypes = new ArrayList();
      eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
      eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
      eventTypes.add(EventType.TASK_CREATE_EVENT);
      return eventTypes;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return DynamicTaskPluginExecuter.class;
   }

   public String getTitle() {
      return "动态任务插件";
   }

   protected DynamicTaskPluginDef parseFromJson(JSON json) {
      DynamicTaskPluginDef def = (DynamicTaskPluginDef)JSON.toJavaObject(json, DynamicTaskPluginDef.class);
      return def;
   }

   public Integer getSn() {
      return 99;
   }
}
