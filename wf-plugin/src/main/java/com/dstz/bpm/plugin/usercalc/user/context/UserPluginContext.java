package com.dstz.bpm.plugin.usercalc.user.context;

import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
import com.dstz.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import com.dstz.bpm.plugin.usercalc.user.def.UserPluginDef;
import com.dstz.bpm.plugin.usercalc.user.executer.UserPluginExecutor;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserPluginContext extends AbstractUserCalcPluginContext<UserPluginDef> {
   private static final long serialVersionUID = 8757352972830358986L;

   public String getDescription() {
      String str = "";
      UserPluginDef def = (UserPluginDef)this.getBpmPluginDef();
      if (def == null) {
         return "";
      } else {
         String source = def.getSource();
         if ("currentUser".equals(source)) {
            str = "当前登录人";
         }

         if ("start".equals(source)) {
            str = "发起人";
         } else if ("prev".equals(source)) {
            str = "上一步执行人";
         } else if ("spec".equals(source)) {
            str = def.getUserName();
         }

         return str;
      }
   }

   public String getTitle() {
      return "用户";
   }

   public Class<? extends RunTimePlugin> getPluginClass() {
      return UserPluginExecutor.class;
   }

   protected UserPluginDef parseJson(JSONObject pluginJson) {
      String source = pluginJson.getString("source");
      UserPluginDef def = new UserPluginDef();
      def.setSource(source);
      if ("spec".equals(source)) {
         String accounts = pluginJson.getString("account");
         String userNames = pluginJson.getString("userName");
         def.setAccount(accounts);
         def.setUserName(userNames);
      }

      return def;
   }
}
