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
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAssignRuleCalc {
    protected static final Logger LOG = LoggerFactory.getLogger(UserAssignRuleCalc.class);

    public static List<SysIdentity> calcUserAssign(BpmUserCalcPluginSession bpmUserCalcPluginSession, List<UserAssignRule> ruleList, Boolean forceExtract) {
        List<SysIdentity> bpmIdentities = new ArrayList<>();
        if (CollectionUtil.isEmpty(ruleList)) return bpmIdentities;
        synchronized (UserAssignRuleCalc.class) {
            Collections.sort(ruleList);
        }
        for (UserAssignRule userRule : ruleList) {
            if (bpmIdentities.size() > 0) {
                break;
            }
            boolean isValid = isRuleValid(userRule.getCondition(), bpmUserCalcPluginSession);
            if (!isValid)
                continue;
            List<UserCalcPluginContext> calcList = userRule.getCalcPluginContextList();
            int index = 0;
            for (UserCalcPluginContext context : calcList) {
                AbstractUserCalcPlugin plugin = (AbstractUserCalcPlugin) AppUtil.getBean(context.getPluginClass());
                if (plugin == null) {
                    throw new WorkFlowException("请检查该插件是否注入成功：" + context.getPluginClass(), BpmStatusCode.PLUGIN_ERROR);
                }
                BpmUserCalcPluginDef pluginDef = (BpmUserCalcPluginDef) context.getBpmPluginDef();
                List<SysIdentity> biList = plugin.execute(bpmUserCalcPluginSession, pluginDef);
                LOG.debug("执行用户计算插件【{}】，解析到【{}】条人员信息，插件计算逻辑：{}", new Object[]{context.getTitle(), Integer.valueOf(biList.size()), pluginDef.getLogicCal()});
                calc(bpmIdentities, biList, pluginDef.getLogicCal());
                index++;
            }
        }
        if (!bpmIdentities.isEmpty() && forceExtract.booleanValue()) {
            return extractBpmIdentity(bpmIdentities);
        }
        return bpmIdentities;
    }

    public static List<SysIdentity> extractBpmIdentity(List<SysIdentity> bpmIdentities) {
        List<SysIdentity> results = new ArrayList<>();
        for (SysIdentity bpmIdentity : bpmIdentities) {
            if ("user".equals(bpmIdentity.getType())) {
                results.add(bpmIdentity);
                continue;
            }
            List<IUser> users = ((UserService) AppUtil.getBean(UserService.class)).getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
            for (IUser user : users) {
                results.add(new DefaultIdentity(user));
            }
        }
        return results;
    }

    public static void calc(List<SysIdentity> existBpmIdentities, List<SysIdentity> newBpmIdentities, LogicType logic) {
        Set<SysIdentity> set;
        List<SysIdentity> rtnList;
        switch (logic) {
            case OR:
                set = new LinkedHashSet<>();
                set.addAll(existBpmIdentities);
                set.addAll(newBpmIdentities);
                existBpmIdentities.clear();
                existBpmIdentities.addAll(set);
                return;
            case AND:
                rtnList = new ArrayList<>();
                if (CollectionUtils.isEmpty(existBpmIdentities)) {
                    existBpmIdentities.clear();
                } else {
                    for (SysIdentity identity : existBpmIdentities) {
                        for (SysIdentity tmp : newBpmIdentities) {
                            if (identity.equals(tmp)) {
                                rtnList.add(identity);
                            }
                        }
                    }
                    existBpmIdentities.clear();
                    existBpmIdentities.addAll(rtnList);
                }
                return;
        }
        for (SysIdentity tmp : newBpmIdentities) {
            existBpmIdentities.remove(tmp);
        }
    }

    private static boolean isRuleValid(String script, BpmUserCalcPluginSession bpmUserCalcPluginSession) {
        if (StringUtil.isEmpty(script)) {
            return true;
        }
        Map<String, Object> map = new HashMap<>();
        map.putAll(bpmUserCalcPluginSession.getBoDatas());
        map.put("bpmTask", bpmUserCalcPluginSession.getBpmTask());
        map.put("bpmInstance", bpmUserCalcPluginSession.getBpmInstance());
        map.put("variableScope", bpmUserCalcPluginSession.getVariableScope());
        map.put("submitTaskName", bpmUserCalcPluginSession.get("submitTaskName"));
        try {
            return ((IGroovyScriptEngine) AppUtil.getBean(IGroovyScriptEngine.class)).executeBoolean(script, map);
        } catch (Exception e) {
            LOG.error("人员前置脚本解析失败,脚本：{},可能原因为：{}", new Object[]{script, e.getMessage(), e});
            throw new BusinessException(BpmStatusCode.PLUGIN_USERCALC_RULE_CONDITION_ERROR);
        }
    }
}
