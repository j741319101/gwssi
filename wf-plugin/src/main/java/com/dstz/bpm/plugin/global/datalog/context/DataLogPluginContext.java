package com.dstz.bpm.plugin.global.datalog.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.context.IExtendGlobalPluginContext;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.global.datalog.def.DataLogPluginDef;
import com.dstz.bpm.plugin.global.datalog.executer.DataLogPluginExecutor;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("dataLogPluginContext")
@Scope("prototype")
public class DataLogPluginContext extends AbstractBpmPluginContext<DataLogPluginDef> implements IExtendGlobalPluginContext {
   private static final long serialVersionUID = -1563849340056571771L;

   public List getEventTypes() {
      List<EventType> eventTypes = new ArrayList();
      eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
      eventTypes.add(EventType.START_EVENT);
      return eventTypes;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return DataLogPluginExecutor.class;
   }

   public String getTitle() {
      return "BO 数据提交日志";
   }

   protected DataLogPluginDef parseFromJson(JSON json) {
      return (DataLogPluginDef)JSON.toJavaObject(json, DataLogPluginDef.class);
   }

   public String getKey() {
      return "dataLog";
   }
}
