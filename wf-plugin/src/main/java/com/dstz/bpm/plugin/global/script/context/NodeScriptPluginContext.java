package com.dstz.bpm.plugin.global.script.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.global.script.def.NodeScriptPluginDef;
import com.dstz.bpm.plugin.global.script.executer.NodeScriptPluginExecutor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class NodeScriptPluginContext extends AbstractBpmPluginContext<NodeScriptPluginDef> {
   private static final long serialVersionUID = -5958682303600423597L;
   protected int sn = 90;

   public List<EventType> getEventTypes() {
      List<EventType> list = new ArrayList();
      list.add(EventType.START_EVENT);
      list.add(EventType.END_EVENT);
      list.add(EventType.TASK_COMPLETE_EVENT);
      list.add(EventType.TASK_CREATE_EVENT);
      list.add(EventType.MANUAL_END);
      return list;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return NodeScriptPluginExecutor.class;
   }

   protected NodeScriptPluginDef parseFromJson(JSON pluginJson) {
      JSONObject jsonObject = (JSONObject)pluginJson;
      NodeScriptPluginDef def = new NodeScriptPluginDef();
      Iterator var4 = jsonObject.keySet().iterator();

      while(var4.hasNext()) {
         String key = (String)var4.next();

         try {
            EventType event = EventType.fromKey(key);
            def.setEvnetnScript(event, jsonObject.getString(key));
         } catch (Exception var7) {
            ;
         }
      }

      return def;
   }

   public String getTitle() {
      return "脚本";
   }

   public Integer getSn() {
      return this.sn;
   }
}
