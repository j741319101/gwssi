package com.dstz.bpm.plugin.global.agency.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.global.agency.def.TaskAgencyPluginDef;
import com.dstz.bpm.plugin.global.agency.executer.TaskAgencyPluginExecutor;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TaskAgencyPluginContext extends AbstractBpmPluginContext<TaskAgencyPluginDef> {
   private static final long serialVersionUID = 3192828814663399776L;

   public List<EventType> getEventTypes() {
      List<EventType> eventTypes = new ArrayList();
      eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
      eventTypes.add(EventType.TASK_POST_COMPLETE_EVENT);
      return eventTypes;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return TaskAgencyPluginExecutor.class;
   }

   public String getTitle() {
      return "任务代理";
   }

   protected TaskAgencyPluginDef parseFromJson(JSON json) {
      return (TaskAgencyPluginDef)JSON.toJavaObject(json, TaskAgencyPluginDef.class);
   }

   public Integer getSn() {
      return 110;
   }
}
