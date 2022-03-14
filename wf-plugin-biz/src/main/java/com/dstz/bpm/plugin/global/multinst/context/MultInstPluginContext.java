package com.dstz.bpm.plugin.global.multinst.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.global.multinst.def.MultInst;
import com.dstz.bpm.plugin.global.multinst.def.MultInstPluginDef;
import com.dstz.bpm.plugin.global.multinst.executer.MultInstPluginExecuter;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MultInstPluginContext extends AbstractBpmPluginContext<MultInstPluginDef> {
   private static final long serialVersionUID = -5974567972412827332L;
   private Integer sn = 102;

   public List<EventType> getEventTypes() {
      List<EventType> eventTypes = new ArrayList();
      eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
      eventTypes.add(EventType.TASK_COMPLETE_EVENT);
      eventTypes.add(EventType.TASK_CREATE_EVENT);
      eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
      return eventTypes;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return MultInstPluginExecuter.class;
   }

   public String getTitle() {
      return "多实例";
   }

   protected MultInstPluginDef parseFromJson(JSON json) {
      List<MultInst> multInsts = JSON.parseArray(json.toJSONString(), MultInst.class);
      MultInstPluginDef def = new MultInstPluginDef();
      def.setMultInsts(multInsts);
      return def;
   }

   public Integer getSn() {
      return this.sn;
   }
}
