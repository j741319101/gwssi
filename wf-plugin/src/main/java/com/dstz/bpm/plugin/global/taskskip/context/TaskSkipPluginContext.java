package com.dstz.bpm.plugin.global.taskskip.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
import com.dstz.bpm.plugin.global.taskskip.executer.TaskSkipPluginExecutor;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TaskSkipPluginContext extends AbstractBpmPluginContext<TaskSkipPluginDef> {
   private static final long serialVersionUID = -8171025388788811778L;

   public List<EventType> getEventTypes() {
      List<EventType> list = new ArrayList();
      list.add(EventType.TASK_POST_CREATE_EVENT);
      return list;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return TaskSkipPluginExecutor.class;
   }

   protected TaskSkipPluginDef parseFromJson(JSON pluginJson) {
      TaskSkipPluginDef def = (TaskSkipPluginDef)JSON.toJavaObject(pluginJson, TaskSkipPluginDef.class);
      return def;
   }

   public String getTitle() {
      return "任务跳过";
   }

   public Integer getSn() {
      return 95;
   }
}
