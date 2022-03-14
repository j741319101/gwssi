package com.dstz.bpm.plugin.global.carboncopy.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.global.carboncopy.def.BpmCarbonCopy;
import com.dstz.bpm.plugin.global.carboncopy.def.CarbonCopyPluginDef;
import com.dstz.bpm.plugin.global.carboncopy.executor.CarbonCopyPluginExecutor;
import com.dstz.bpm.plugin.node.userassign.context.UserAssignPluginContext;
import com.dstz.bpm.plugin.node.userassign.def.UserAssignPluginDef;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarbonCopyPluginContext extends AbstractBpmPluginContext<CarbonCopyPluginDef> {
   private static final long serialVersionUID = 4733258050731331492L;

   protected CarbonCopyPluginDef parseFromJson(JSON pluginJson) {
      JSONArray array = (JSONArray)pluginJson;
      Map<String, List<BpmCarbonCopy>> nodeEventBpmCarbonCopyMap = Collections.emptyMap();
      if (array != null && array.size() > 0) {
         nodeEventBpmCarbonCopyMap = new HashMap(array.size());
         int i = 0;

         for(int l = array.size(); i < l; ++i) {
            JSONObject msgJson = array.getJSONObject(i);
            BpmCarbonCopy bpmCarbonCopy = (BpmCarbonCopy)JSON.toJavaObject(msgJson, BpmCarbonCopy.class);
            if (StringUtil.isNotEmpty(msgJson.getString("userRules"))) {
               UserAssignPluginContext userPluginContext = (UserAssignPluginContext)AppUtil.getBean(UserAssignPluginContext.class);
               userPluginContext.parse(msgJson.getString("userRules"));
               bpmCarbonCopy.setUserRules(((UserAssignPluginDef)userPluginContext.getBpmPluginDef()).getRuleList());
            }

            String key = CarbonCopyPluginDef.getMapKey(bpmCarbonCopy.getNodeId(), bpmCarbonCopy.getEvent());
            List<BpmCarbonCopy> bpmCarbonCopyList = (List)((Map)nodeEventBpmCarbonCopyMap).computeIfAbsent(key, (k) -> {
               return new ArrayList();
            });
            bpmCarbonCopyList.add(bpmCarbonCopy);
         }
      }

      CarbonCopyPluginDef carbonCopyPluginDef = new CarbonCopyPluginDef();
      carbonCopyPluginDef.setNodeEventCarbonCopyMap((Map)nodeEventBpmCarbonCopyMap);
      return carbonCopyPluginDef;
   }

   public List<EventType> getEventTypes() {
      List<EventType> eventTypeList = new ArrayList();
      eventTypeList.add(EventType.TASK_POST_CREATE_EVENT);
      eventTypeList.add(EventType.START_EVENT);
      eventTypeList.add(EventType.END_EVENT);
      eventTypeList.add(EventType.TASK_COMPLETE_EVENT);
      return eventTypeList;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return CarbonCopyPluginExecutor.class;
   }

   public String getTitle() {
      return "节点消息抄送";
   }
}
