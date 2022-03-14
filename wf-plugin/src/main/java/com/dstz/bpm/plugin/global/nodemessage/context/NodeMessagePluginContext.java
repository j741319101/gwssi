package com.dstz.bpm.plugin.global.nodemessage.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.plugin.global.nodemessage.def.NodeMessage;
import com.dstz.bpm.plugin.global.nodemessage.def.NodeMessagePluginDef;
import com.dstz.bpm.plugin.global.nodemessage.executer.NodeMessagePluginExecutor;
import com.dstz.bpm.plugin.node.userassign.context.UserAssignPluginContext;
import com.dstz.bpm.plugin.node.userassign.def.UserAssignPluginDef;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class NodeMessagePluginContext extends AbstractBpmPluginContext<NodeMessagePluginDef> {
   private static final long serialVersionUID = -8171025388788811778L;

   public List<EventType> getEventTypes() {
      List<EventType> list = new ArrayList();
      list.add(EventType.START_EVENT);
      list.add(EventType.END_EVENT);
      list.add(EventType.TASK_COMPLETE_EVENT);
      list.add(EventType.TASK_POST_CREATE_EVENT);
      return list;
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return NodeMessagePluginExecutor.class;
   }

   protected NodeMessagePluginDef parseFromJson(JSON pluginJson) {
      JSONArray array = (JSONArray)pluginJson;
      List<NodeMessage> messageList = new ArrayList();

      for(int i = 0; i < array.size(); ++i) {
         JSONObject msgJson = array.getJSONObject(i);
         NodeMessage nodeMessage = (NodeMessage)JSON.toJavaObject(msgJson, NodeMessage.class);
         if (StringUtil.isNotEmpty(msgJson.getString("userRules"))) {
            UserAssignPluginContext userPluginContext = (UserAssignPluginContext)AppUtil.getBean(UserAssignPluginContext.class);
            userPluginContext.parse(msgJson.getString("userRules"));
            nodeMessage.setUserRules(((UserAssignPluginDef)userPluginContext.getBpmPluginDef()).getRuleList());
         }

         messageList.add(nodeMessage);
      }

      return new NodeMessagePluginDef(messageList);
   }

   public String getTitle() {
      return "节点消息";
   }
}
