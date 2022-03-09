/*     */ package com.dstz.bpm.plugin.node.userassign.context;
/*     */ 
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.JsonUtil;
/*     */ import com.dstz.base.core.util.ThreadMsgUtil;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.engine.plugin.context.UserCalcPluginContext;
/*     */ import com.dstz.bpm.api.engine.plugin.context.UserQueryPluginContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*     */ import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
/*     */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*     */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*     */ import com.dstz.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
/*     */ import com.dstz.bpm.plugin.node.userassign.def.UserAssignPluginDef;
/*     */ import com.dstz.bpm.plugin.node.userassign.executer.UserAssignPluginExecutor;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */
import org.springframework.context.annotation.Scope;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ @Scope("prototype")
/*     */ public class UserAssignPluginContext
/*     */   extends AbstractBpmPluginContext<UserAssignPluginDef>
/*     */   implements UserQueryPluginContext
/*     */ {
/*     */   public Class getPluginClass() {
/*  38 */     return UserAssignPluginExecutor.class;
/*     */   }
/*     */   
/*     */   public Class<? extends RunTimePlugin> getUserQueryPluginClass() {
/*  42 */     return (Class)UserAssignPluginExecutor.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  47 */     if (getBpmPluginDef() != null && CollectionUtil.isNotEmpty(((UserAssignPluginDef)getBpmPluginDef()).getRuleList())) {
/*  48 */       return false;
/*     */     }
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<EventType> getEventTypes() {
/*  55 */     List<EventType> eventTypes = new ArrayList<>();
/*  56 */     eventTypes.add(EventType.TASK_CREATE_EVENT);
/*  57 */     return eventTypes;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public JSON getJson() {
/*  72 */     if (getBpmPluginDef() == null) return (JSON)JSON.parse("[]");
/*     */     
/*  74 */     List<UserAssignRule> ruleList = ((UserAssignPluginDef)getBpmPluginDef()).getRuleList();
/*  75 */     if (CollectionUtil.isEmpty(ruleList)) return (JSON)JSON.parse("[]");
/*     */     
/*  77 */     return (JSON)JSON.toJSON(ruleList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UserAssignPluginDef parseFromJson(JSON pluginJson) {
/*  86 */     UserAssignPluginDef def = new UserAssignPluginDef();
/*     */     
/*  88 */     JSONArray userRuleList = null;
/*  89 */     if (pluginJson instanceof JSONObject) {
/*  90 */       JSONObject json = (JSONObject)pluginJson;
/*  91 */       if (!json.containsKey("ruleList")) {
/*  92 */         ThreadMsgUtil.addMsg("人员条件不完整！");
/*  93 */         return def;
/*     */       } 
/*     */       
/*  96 */       userRuleList = json.getJSONArray("ruleList");
/*     */     } else {
/*  98 */       userRuleList = (JSONArray)pluginJson;
/*     */     } 
/*     */     
/* 101 */     List<UserAssignRule> ruleList = new ArrayList<>();
/* 102 */     for (int i = 0; i < userRuleList.size(); i++) {
/* 103 */       JSONObject ruleJson = userRuleList.getJSONObject(i);
/* 104 */       UserAssignRule rule = (UserAssignRule)JSON.toJavaObject((JSON)ruleJson, UserAssignRule.class);
/* 105 */       ruleList.add(rule);
/*     */       
/* 107 */       if (!ruleJson.containsKey("calcs")) {
/* 108 */         ThreadMsgUtil.addMsg("人员条件不完整！");
/*     */       }
/*     */       else {
/*     */         
/* 112 */         JSONArray calcAry = ruleJson.getJSONArray("calcs");
/* 113 */         List<UserCalcPluginContext> calcPluginContextList = new ArrayList<>();
/* 114 */         for (Object obj : calcAry) {
/* 115 */           JSONObject calcObj = (JSONObject)obj;
/* 116 */           String pluginContext = JsonUtil.getString(calcObj, "pluginType") + "PluginContext";
/*     */           
/* 118 */           AbstractUserCalcPluginContext ctx = (AbstractUserCalcPluginContext)AppUtil.getBean(pluginContext);
/* 119 */           if (ctx == null) {
/* 120 */             this.LOG.warn("插件{}查找失败！", pluginContext);
/*     */             
/*     */             continue;
/*     */           } 
/* 124 */           ctx.parse((JSON)calcObj);
/*     */           
/* 126 */           calcPluginContextList.add(ctx);
/*     */         } 
/*     */         
/* 129 */         rule.setCalcPluginContextList(calcPluginContextList);
/*     */       } 
/*     */     } 
/* 132 */     def.setRuleList(ruleList);
/* 133 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTitle() {
/* 138 */     return "用户分配插件";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/userassign/context/UserAssignPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */