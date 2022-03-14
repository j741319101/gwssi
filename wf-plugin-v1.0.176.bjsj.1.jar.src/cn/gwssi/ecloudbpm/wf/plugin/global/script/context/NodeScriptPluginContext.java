/*    */ package com.dstz.bpm.plugin.global.script.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.global.script.def.NodeScriptPluginDef;
/*    */ import com.dstz.bpm.plugin.global.script.executer.NodeScriptPluginExecutor;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class NodeScriptPluginContext
/*    */   extends AbstractBpmPluginContext<NodeScriptPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = -5958682303600423597L;
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 27 */     List<EventType> list = new ArrayList<>();
/* 28 */     list.add(EventType.START_EVENT);
/* 29 */     list.add(EventType.END_EVENT);
/* 30 */     list.add(EventType.TASK_COMPLETE_EVENT);
/* 31 */     list.add(EventType.TASK_CREATE_EVENT);
/* 32 */     list.add(EventType.MANUAL_END);
/* 33 */     return list;
/*    */   }
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 37 */     return (Class)NodeScriptPluginExecutor.class;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected NodeScriptPluginDef parseFromJson(JSON pluginJson) {
/* 43 */     JSONObject jsonObject = (JSONObject)pluginJson;
/* 44 */     NodeScriptPluginDef def = new NodeScriptPluginDef();
/* 45 */     for (String key : jsonObject.keySet()) {
/*    */       try {
/* 47 */         EventType event = EventType.fromKey(key);
/* 48 */         def.setEvnetnScript(event, jsonObject.getString(key));
/* 49 */       } catch (Exception e) {}
/*    */     } 
/*    */ 
/*    */     
/* 53 */     return def;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 58 */     return "脚本";
/*    */   }
/* 60 */   protected int sn = 90;
/*    */ 
/*    */   
/*    */   public Integer getSn() {
/* 64 */     return Integer.valueOf(this.sn);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/script/context/NodeScriptPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */