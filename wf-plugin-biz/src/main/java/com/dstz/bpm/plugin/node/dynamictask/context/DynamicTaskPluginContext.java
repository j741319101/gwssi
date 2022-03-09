/*    */ package com.dstz.bpm.plugin.node.dynamictask.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*    */ import com.dstz.bpm.plugin.node.dynamictask.executer.DynamicTaskPluginExecuter;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class DynamicTaskPluginContext
/*    */   extends AbstractBpmPluginContext<DynamicTaskPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = 8784633971785686365L;
/*    */   
/*    */   public List getEventTypes() {
/* 28 */     List<EventType> eventTypes = new ArrayList<>();
/*    */     
/* 30 */     eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
/*    */     
/* 32 */     eventTypes.add(EventType.TASK_CREATE_EVENT);
/*    */     
/* 34 */     eventTypes.add(EventType.TASK_COMPLETE_EVENT);
/* 35 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 40 */     return (Class)DynamicTaskPluginExecuter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 45 */     return "动态任务插件";
/*    */   }
/*    */ 
/*    */   
/*    */   protected DynamicTaskPluginDef parseFromJson(JSON json) {
/* 50 */     DynamicTaskPluginDef def = (DynamicTaskPluginDef)JSON.toJavaObject(json, DynamicTaskPluginDef.class);
/* 51 */     return def;
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer getSn() {
/* 56 */     return Integer.valueOf(101);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynamictask/context/DynamicTaskPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */