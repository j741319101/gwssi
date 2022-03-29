package com.dstz.bpm.plugin.global.script.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.global.script.def.GlobalScript;
import com.dstz.bpm.plugin.global.script.def.GlobalScriptPluginDef;
import com.dstz.bpm.plugin.global.script.executer.GlobalScriptPluginExecutor;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GlobalScriptPluginContext extends AbstractBpmPluginContext<GlobalScriptPluginDef> {
   private static final long serialVersionUID = -5958682303600423597L;

   public List<EventType> getEventTypes() {
      List<EventType> list = new ArrayList();
      list.add(EventType.TASK_COMPLETE_EVENT);
      list.add(EventType.TASK_CREATE_EVENT);
      list.add(EventType.START_EVENT);
      list.add(EventType.END_EVENT);
      list.add(EventType.MANUAL_END);
      list.add(EventType.RECOVER_EVENT);
      list.add(EventType.DELETE_EVENT);
      list.add(EventType.TASK_POST_CREATE_EVENT);
      list.add(EventType.TASK_PRE_COMPLETE_EVENT);
      list.add(EventType.TASK_POST_COMPLETE_EVENT);
      list.add(EventType.TASK_SIGN_CREATE_EVENT);
      list.add(EventType.TASK_SIGN_POST_CREATE_EVENT);
      return list;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return GlobalScriptPluginExecutor.class;
   }

   public String getTitle() {
      return "全局脚本";
   }

   protected GlobalScriptPluginDef parseFromJson(JSON json) {
      List<GlobalScript> globalScripts = JSON.parseArray(json.toJSONString(), GlobalScript.class);
      GlobalScriptPluginDef globalScriptPluginDef = new GlobalScriptPluginDef();
      globalScriptPluginDef.setGlobalScripts(globalScripts);
      return globalScriptPluginDef;
   }
}
