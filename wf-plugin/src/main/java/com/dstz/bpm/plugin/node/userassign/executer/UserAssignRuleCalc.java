package com.dstz.bpm.plugin.node.userassign.executer;

import com.dstz.bpm.api.engine.constant.LogicType;
import com.dstz.bpm.api.engine.plugin.context.UserCalcPluginContext;
import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.exception.WorkFlowException;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class UserAssignRuleCalc {
   protected static final Logger LOG = LoggerFactory.getLogger(UserAssignRuleCalc.class);

   public static List<SysIdentity> calcUserAssign(BpmUserCalcPluginSession bpmUserCalcPluginSession, List<UserAssignRule> ruleList, Boolean forceExtract) {
      List<SysIdentity> bpmIdentities = new ArrayList();
      if (CollectionUtil.isEmpty(ruleList)) {
         return bpmIdentities;
      } else {
         Class var4 = UserAssignRuleCalc.class;
         synchronized(UserAssignRuleCalc.class) {
            Collections.sort(ruleList);
         }

         Iterator var15 = ruleList.iterator();

         while(var15.hasNext()) {
            UserAssignRule userRule = (UserAssignRule)var15.next();
            if (bpmIdentities.size() > 0) {
               break;
            }

            boolean isValid = isRuleValid(userRule.getCondition(), bpmUserCalcPluginSession);
            if (isValid) {
               List<UserCalcPluginContext> calcList = userRule.getCalcPluginContextList();
               int index = 0;

               for(Iterator var9 = calcList.iterator(); var9.hasNext(); ++index) {
                  UserCalcPluginContext context = (UserCalcPluginContext)var9.next();
                  AbstractUserCalcPlugin plugin = (AbstractUserCalcPlugin)AppUtil.getBean(context.getPluginClass());
                  if (plugin == null) {
                     throw new WorkFlowException("请检查该插件是否注入成功：" + context.getPluginClass(), BpmStatusCode.PLUGIN_ERROR);
                  }

                  BpmUserCalcPluginDef pluginDef = (BpmUserCalcPluginDef)context.getBpmPluginDef();
                  List<SysIdentity> biList = plugin.execute(bpmUserCalcPluginSession, pluginDef);
                  LOG.debug("执行用户计算插件【{}】，解析到【{}】条人员信息，插件计算逻辑：{}", new Object[]{context.getTitle(), biList.size(), pluginDef.getLogicCal()});
                  calc(bpmIdentities, biList, pluginDef.getLogicCal());
               }
            }
         }

         return (List)(!bpmIdentities.isEmpty() && forceExtract ? extractBpmIdentity(bpmIdentities) : bpmIdentities);
      }
   }

   public static List<SysIdentity> extractBpmIdentity(List<SysIdentity> bpmIdentities) {
      List<SysIdentity> results = new ArrayList();
      Iterator var2 = bpmIdentities.iterator();

      while(true) {
         while(var2.hasNext()) {
            SysIdentity bpmIdentity = (SysIdentity)var2.next();
            if ("user".equals(bpmIdentity.getType())) {
               results.add(bpmIdentity);
            } else {
               List<IUser> users =(List<IUser>) ((UserService)AppUtil.getBean(UserService.class)).getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
               Iterator var5 = users.iterator();

               while(var5.hasNext()) {
                  IUser user = (IUser)var5.next();
                  results.add(new DefaultIdentity(user));
               }
            }
         }

         return results;
      }
   }

   public static void calc(List<SysIdentity> existBpmIdentities, List<SysIdentity> newBpmIdentities, LogicType logic) {
      Iterator var5;
      SysIdentity identity;
      switch(logic) {
      case OR:
         Set<SysIdentity> set = new LinkedHashSet();
         set.addAll(existBpmIdentities);
         set.addAll(newBpmIdentities);
         existBpmIdentities.clear();
         existBpmIdentities.addAll(set);
         break;
      case AND:
         List<SysIdentity> rtnList = new ArrayList();
         if (CollectionUtils.isEmpty(existBpmIdentities)) {
            existBpmIdentities.clear();
         } else {
            var5 = existBpmIdentities.iterator();

            while(var5.hasNext()) {
               identity = (SysIdentity)var5.next();
               Iterator var7 = newBpmIdentities.iterator();

               while(var7.hasNext()) {
                  SysIdentity tmp = (SysIdentity)var7.next();
                  if (identity.equals(tmp)) {
                     rtnList.add(identity);
                  }
               }
            }

            existBpmIdentities.clear();
            existBpmIdentities.addAll(rtnList);
         }
         break;
      default:
         var5 = newBpmIdentities.iterator();

         while(var5.hasNext()) {
            identity = (SysIdentity)var5.next();
            existBpmIdentities.remove(identity);
         }
      }

   }

   private static boolean isRuleValid(String script, BpmUserCalcPluginSession bpmUserCalcPluginSession) {
      if (StringUtil.isEmpty(script)) {
         return true;
      } else {
         Map<String, Object> map = new HashMap();
         map.putAll(bpmUserCalcPluginSession.getBoDatas());
         map.put("bpmTask", bpmUserCalcPluginSession.getBpmTask());
         map.put("bpmInstance", bpmUserCalcPluginSession.getBpmInstance());
         map.put("variableScope", bpmUserCalcPluginSession.getVariableScope()); //todo 报错 找不到org.activiti.engine.delegate.VariableScope的类文件
         map.put("submitTaskName", bpmUserCalcPluginSession.get("submitTaskName"));

         try {
            return ((IGroovyScriptEngine)AppUtil.getBean(IGroovyScriptEngine.class)).executeBoolean(script, map);
         } catch (Exception var4) {
            LOG.error("人员前置脚本解析失败,脚本：{},可能原因为：{}", new Object[]{script, var4.getMessage(), var4});
            throw new BusinessException(BpmStatusCode.PLUGIN_USERCALC_RULE_CONDITION_ERROR);
         }
      }
   }
}
