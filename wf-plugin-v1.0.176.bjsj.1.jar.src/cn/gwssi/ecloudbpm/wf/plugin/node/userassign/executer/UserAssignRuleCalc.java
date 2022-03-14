/*     */ package com.dstz.bpm.plugin.node.userassign.executer;
/*     */ import com.dstz.bpm.api.engine.constant.LogicType;
/*     */ import com.dstz.bpm.api.engine.plugin.context.UserCalcPluginContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*     */ import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.exception.WorkFlowException;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class UserAssignRuleCalc {
/*  27 */   protected static final Logger LOG = LoggerFactory.getLogger(UserAssignRuleCalc.class);
/*     */ 
/*     */   
/*     */   public static List<SysIdentity> calcUserAssign(BpmUserCalcPluginSession bpmUserCalcPluginSession, List<UserAssignRule> ruleList, Boolean forceExtract) {
/*  31 */     List<SysIdentity> bpmIdentities = new ArrayList<>();
/*     */     
/*  33 */     if (CollectionUtil.isEmpty(ruleList)) return bpmIdentities;
/*     */     
/*  35 */     synchronized (UserAssignRuleCalc.class) {
/*  36 */       Collections.sort(ruleList);
/*     */     } 
/*  38 */     for (UserAssignRule userRule : ruleList) {
/*     */ 
/*     */       
/*  41 */       if (bpmIdentities.size() > 0) {
/*     */         break;
/*     */       }
/*  44 */       boolean isValid = isRuleValid(userRule.getCondition(), bpmUserCalcPluginSession);
/*  45 */       if (!isValid)
/*     */         continue; 
/*  47 */       List<UserCalcPluginContext> calcList = userRule.getCalcPluginContextList();
/*  48 */       int index = 0;
/*  49 */       for (UserCalcPluginContext context : calcList) {
/*     */         
/*  51 */         AbstractUserCalcPlugin plugin = (AbstractUserCalcPlugin)AppUtil.getBean(context.getPluginClass());
/*  52 */         if (plugin == null) {
/*  53 */           throw new WorkFlowException("请检查该插件是否注入成功：" + context.getPluginClass(), BpmStatusCode.PLUGIN_ERROR);
/*     */         }
/*     */         
/*  56 */         BpmUserCalcPluginDef pluginDef = (BpmUserCalcPluginDef)context.getBpmPluginDef();
/*     */ 
/*     */         
/*  59 */         List<SysIdentity> biList = plugin.execute(bpmUserCalcPluginSession, pluginDef);
/*  60 */         LOG.debug("执行用户计算插件【{}】，解析到【{}】条人员信息，插件计算逻辑：{}", new Object[] { context.getTitle(), Integer.valueOf(biList.size()), pluginDef.getLogicCal() });
/*     */ 
/*     */         
/*  63 */         calc(bpmIdentities, biList, pluginDef.getLogicCal());
/*  64 */         index++;
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     if (!bpmIdentities.isEmpty() && forceExtract.booleanValue()) {
/*  69 */       return extractBpmIdentity(bpmIdentities);
/*     */     }
/*     */     
/*  72 */     return bpmIdentities;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<SysIdentity> extractBpmIdentity(List<SysIdentity> bpmIdentities) {
/*  77 */     List<SysIdentity> results = new ArrayList<>();
/*  78 */     for (SysIdentity bpmIdentity : bpmIdentities) {
/*     */       
/*  80 */       if ("user".equals(bpmIdentity.getType())) {
/*  81 */         results.add(bpmIdentity);
/*     */         
/*     */         continue;
/*     */       } 
/*  85 */       List<IUser> users = ((UserService)AppUtil.getBean(UserService.class)).getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
/*  86 */       for (IUser user : users) {
/*  87 */         results.add(new DefaultIdentity(user));
/*     */       }
/*     */     } 
/*     */     
/*  91 */     return results;
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
/* 105 */     switch (logic) {
/*     */       
/*     */       case OR:
/* 108 */         set = new LinkedHashSet<>();
/* 109 */         set.addAll(existBpmIdentities);
/* 110 */         set.addAll(newBpmIdentities);
/* 111 */         existBpmIdentities.clear();
/* 112 */         existBpmIdentities.addAll(set);
/*     */         return;
/*     */       case AND:
/* 115 */         rtnList = new ArrayList<>();
/* 116 */         if (CollectionUtils.isEmpty(existBpmIdentities)) {
/* 117 */           existBpmIdentities.clear();
/*     */         } else {
/*     */           
/* 120 */           for (SysIdentity identity : existBpmIdentities) {
/* 121 */             for (SysIdentity tmp : newBpmIdentities) {
/* 122 */               if (identity.equals(tmp)) {
/* 123 */                 rtnList.add(identity);
/*     */               }
/*     */             } 
/*     */           } 
/* 127 */           existBpmIdentities.clear();
/* 128 */           existBpmIdentities.addAll(rtnList);
/*     */         }  return;
/*     */     } 
/* 131 */     for (SysIdentity tmp : newBpmIdentities) {
/* 132 */       existBpmIdentities.remove(tmp);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isRuleValid(String script, BpmUserCalcPluginSession bpmUserCalcPluginSession) {
/* 140 */     if (StringUtil.isEmpty(script)) {
/* 141 */       return true;
/*     */     }
/*     */     
/* 144 */     Map<String, Object> map = new HashMap<>();
/*     */     
/* 146 */     map.putAll(bpmUserCalcPluginSession.getBoDatas());
/* 147 */     map.put("bpmTask", bpmUserCalcPluginSession.getBpmTask());
/* 148 */     map.put("bpmInstance", bpmUserCalcPluginSession.getBpmInstance());
/* 149 */     map.put("variableScope", bpmUserCalcPluginSession.getVariableScope());
/* 150 */     map.put("submitTaskName", bpmUserCalcPluginSession.get("submitTaskName"));
/*     */     try {
/* 152 */       return ((IGroovyScriptEngine)AppUtil.getBean(IGroovyScriptEngine.class)).executeBoolean(script, map);
/* 153 */     } catch (Exception e) {
/* 154 */       LOG.error("人员前置脚本解析失败,脚本：{},可能原因为：{}", new Object[] { script, e.getMessage(), e });
/* 155 */       throw new BusinessException(BpmStatusCode.PLUGIN_USERCALC_RULE_CONDITION_ERROR);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/userassign/executer/UserAssignRuleCalc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */