/*     */ package com.dstz.bpm.plugin.node.userassign.executer;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.api.engine.constant.LogicType;
/*     */ import com.dstz.bpm.api.engine.plugin.context.UserCalcPluginContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*     */ import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.exception.WorkFlowException;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserAssignRuleCalc
/*     */ {
/*  37 */   protected static final Logger LOG = LoggerFactory.getLogger(UserAssignRuleCalc.class);
/*     */ 
/*     */   
/*     */   public static List<SysIdentity> calcUserAssign(BpmUserCalcPluginSession bpmUserCalcPluginSession, List<UserAssignRule> ruleList, Boolean forceExtract) {
/*  41 */     List<SysIdentity> bpmIdentities = new ArrayList<>();
/*     */     
/*  43 */     if (CollectionUtil.isEmpty(ruleList)) return bpmIdentities;
/*     */     
/*  45 */     Collections.sort(ruleList);
/*  46 */     for (UserAssignRule userRule : ruleList) {
/*     */ 
/*     */       
/*  49 */       if (bpmIdentities.size() > 0) {
/*     */         break;
/*     */       }
/*  52 */       boolean isValid = isRuleValid(userRule.getCondition(), bpmUserCalcPluginSession);
/*  53 */       if (!isValid)
/*     */         continue; 
/*  55 */       List<UserCalcPluginContext> calcList = userRule.getCalcPluginContextList();
/*     */       
/*  57 */       for (UserCalcPluginContext context : calcList) {
/*     */         
/*  59 */         AbstractUserCalcPlugin plugin = (AbstractUserCalcPlugin)AppUtil.getBean(context.getPluginClass());
/*  60 */         if (plugin == null) {
/*  61 */           throw new WorkFlowException("请检查该插件是否注入成功：" + context.getPluginClass(), BpmStatusCode.PLUGIN_ERROR);
/*     */         }
/*     */         
/*  64 */         BpmUserCalcPluginDef pluginDef = (BpmUserCalcPluginDef)context.getBpmPluginDef();
/*     */ 
/*     */         
/*  67 */         List<SysIdentity> biList = plugin.execute(bpmUserCalcPluginSession, pluginDef);
/*  68 */         LOG.debug("执行用户计算插件【{}】，解析到【{}】条人员信息，插件计算逻辑：{}", new Object[] { context.getTitle(), Integer.valueOf(biList.size()), pluginDef.getLogicCal() });
/*     */         
/*  70 */         if (CollectionUtil.isEmpty(biList) && 
/*  71 */           StringUtils.equals(pluginDef.getLogicCal().getKey(), LogicType.AND.getKey())) {
/*  72 */           throw new BusinessException(context.getTitle() + "任务候选人不能为空");
/*     */         }
/*     */ 
/*     */         
/*  76 */         if (CollectionUtil.isEmpty(bpmIdentities)) {
/*  77 */           bpmIdentities.addAll(biList);
/*     */           continue;
/*     */         } 
/*  80 */         calc(bpmIdentities, biList, pluginDef.getLogicCal());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  85 */     if (!bpmIdentities.isEmpty() && forceExtract.booleanValue()) {
/*  86 */       return extractBpmIdentity(bpmIdentities);
/*     */     }
/*     */     
/*  89 */     return bpmIdentities;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<SysIdentity> extractBpmIdentity(List<SysIdentity> bpmIdentities) {
/*  94 */     List<SysIdentity> results = new ArrayList<>();
/*  95 */     for (SysIdentity bpmIdentity : bpmIdentities) {
/*     */       
/*  97 */       if ("user".equals(bpmIdentity.getType())) {
/*  98 */         results.add(bpmIdentity);
/*     */         
/*     */         continue;
/*     */       } 
/* 102 */       List<IUser> users = ((UserService)AppUtil.getBean(UserService.class)).getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
/* 103 */       for (IUser user : users) {
/* 104 */         results.add(new DefaultIdentity(user));
/*     */       }
/*     */     } 
/*     */     
/* 108 */     return results;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void calc(List<SysIdentity> existBpmIdentities, List<SysIdentity> newBpmIdentities, LogicType logic) {
/*     */     Set<SysIdentity> set;
/*     */     List<SysIdentity> rtnList;
/* 122 */     switch (logic) {
/*     */       
/*     */       case OR:
/* 125 */         set = new LinkedHashSet<>();
/* 126 */         set.addAll(existBpmIdentities);
/* 127 */         set.addAll(newBpmIdentities);
/* 128 */         existBpmIdentities.clear();
/* 129 */         existBpmIdentities.addAll(set);
/*     */         return;
/*     */       case AND:
/* 132 */         rtnList = new ArrayList<>();
/* 133 */         for (SysIdentity identity : existBpmIdentities) {
/* 134 */           for (SysIdentity tmp : newBpmIdentities) {
/* 135 */             if (identity.equals(tmp)) {
/* 136 */               rtnList.add(identity);
/*     */             }
/*     */           } 
/*     */         } 
/* 140 */         existBpmIdentities.clear();
/* 141 */         existBpmIdentities.addAll(rtnList);
/*     */         return;
/*     */     } 
/*     */     
/* 145 */     for (SysIdentity tmp : newBpmIdentities) {
/* 146 */       existBpmIdentities.remove(tmp);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isRuleValid(String script, BpmUserCalcPluginSession bpmUserCalcPluginSession) {
/* 154 */     if (StringUtil.isEmpty(script)) {
/* 155 */       return true;
/*     */     }
/*     */     
/* 158 */     Map<String, Object> map = new HashMap<>();
/*     */     
/* 160 */     map.putAll(bpmUserCalcPluginSession.getBoDatas());
/* 161 */     map.put("bpmTask", bpmUserCalcPluginSession.getBpmTask());
/* 162 */     map.put("bpmInstance", bpmUserCalcPluginSession.getBpmInstance());
/* 163 */     map.put("variableScope", bpmUserCalcPluginSession.getVariableScope());
/*     */     
/*     */     try {
/* 166 */       return ((IGroovyScriptEngine)AppUtil.getBean(IGroovyScriptEngine.class)).executeBoolean(script, map);
/* 167 */     } catch (Exception e) {
/* 168 */       LOG.error("人员前置脚本解析失败,脚本：{},可能原因为：{}", new Object[] { script, e.getMessage(), e });
/* 169 */       throw new BusinessException(BpmStatusCode.PLUGIN_USERCALC_RULE_CONDITION_ERROR);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/userassign/executer/UserAssignRuleCalc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */