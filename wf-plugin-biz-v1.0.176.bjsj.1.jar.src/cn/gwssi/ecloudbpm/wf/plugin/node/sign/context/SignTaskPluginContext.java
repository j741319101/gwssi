/*    */ package com.dstz.bpm.plugin.node.sign.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
/*    */ import com.dstz.bpm.plugin.node.sign.executer.SignTaskPluginExecuter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class SignTaskPluginContext
/*    */   extends AbstractBpmPluginContext<SignTaskPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = 8784633971785686365L;
/*    */   
/*    */   public List getEventTypes() {
/* 37 */     List<EventType> eventTypes = new ArrayList<>();
/* 38 */     eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
/* 39 */     eventTypes.add(EventType.TASK_CREATE_EVENT);
/*    */     
/* 41 */     eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
/* 42 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 47 */     return (Class)SignTaskPluginExecuter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 52 */     return "会签任务插件";
/*    */   }
/*    */ 
/*    */   
/*    */   protected SignTaskPluginDef parseFromJson(JSON json) {
/* 57 */     SignTaskPluginDef def = (SignTaskPluginDef)JSON.toJavaObject(json, SignTaskPluginDef.class);
/* 58 */     return def;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/sign/context/SignTaskPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */