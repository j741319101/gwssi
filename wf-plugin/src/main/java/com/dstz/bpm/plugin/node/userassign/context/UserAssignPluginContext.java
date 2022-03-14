package com.dstz.bpm.plugin.node.userassign.context;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.plugin.context.UserCalcPluginContext;
import com.dstz.bpm.api.engine.plugin.context.UserQueryPluginContext;
import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
import com.dstz.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import com.dstz.bpm.plugin.node.userassign.def.UserAssignPluginDef;
import com.dstz.bpm.plugin.node.userassign.executer.UserAssignPluginExecutor;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.JsonUtil;
import com.dstz.base.core.util.ThreadMsgUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserAssignPluginContext extends AbstractBpmPluginContext<UserAssignPluginDef> implements UserQueryPluginContext {
   public Class getPluginClass() {
      return UserAssignPluginExecutor.class;
   }

   public Class<? extends RunTimePlugin> getUserQueryPluginClass() {
      return UserAssignPluginExecutor.class;
   }

   public boolean isEmpty() {
      return this.getBpmPluginDef() == null || !CollectionUtil.isNotEmpty(((UserAssignPluginDef)this.getBpmPluginDef()).getRuleList());
   }

   public List<EventType> getEventTypes() {
      List<EventType> eventTypes = new ArrayList();
      eventTypes.add(EventType.TASK_CREATE_EVENT);
      return eventTypes;
   }

   public JSON getJson() {
      if (this.getBpmPluginDef() == null) {
         return (JSON)JSON.parse("[]");
      } else {
         List<UserAssignRule> ruleList = ((UserAssignPluginDef)this.getBpmPluginDef()).getRuleList();
         return CollectionUtil.isEmpty(ruleList) ? (JSON)JSON.parse("[]") : (JSON)JSON.toJSON(ruleList);
      }
   }

   protected UserAssignPluginDef parseFromJson(JSON pluginJson) {
      UserAssignPluginDef def = new UserAssignPluginDef();
      JSONArray userRuleList = null;
      if (pluginJson instanceof JSONObject) {
         JSONObject json = (JSONObject)pluginJson;
         if (!json.containsKey("ruleList")) {
            ThreadMsgUtil.addMsg("人员条件不完整！");
            return def;
         }

         userRuleList = json.getJSONArray("ruleList");
      } else {
         userRuleList = (JSONArray)pluginJson;
      }

      List<UserAssignRule> ruleList = new ArrayList();

      for(int i = 0; i < userRuleList.size(); ++i) {
         JSONObject ruleJson = userRuleList.getJSONObject(i);
         UserAssignRule rule = (UserAssignRule)JSON.toJavaObject(ruleJson, UserAssignRule.class);
         ruleList.add(rule);
         if (!ruleJson.containsKey("calcs")) {
            ThreadMsgUtil.addMsg("人员条件不完整！");
         } else {
            JSONArray calcAry = ruleJson.getJSONArray("calcs");
            List<UserCalcPluginContext> calcPluginContextList = new ArrayList();
            Iterator var10 = calcAry.iterator();

            while(var10.hasNext()) {
               Object obj = var10.next();
               JSONObject calcObj = (JSONObject)obj;
               String pluginContext = JsonUtil.getString(calcObj, "pluginType") + "PluginContext";
               AbstractUserCalcPluginContext ctx = (AbstractUserCalcPluginContext)AppUtil.getBean(pluginContext);
               if (ctx == null) {
                  this.LOG.warn("插件{}查找失败！", pluginContext);
               } else {
                  ctx.parse(calcObj);
                  calcPluginContextList.add(ctx);
               }
            }

            rule.setCalcPluginContextList(calcPluginContextList);
         }
      }

      def.setRuleList(ruleList);
      return def;
   }

   public String getTitle() {
      return "用户分配插件";
   }
}
