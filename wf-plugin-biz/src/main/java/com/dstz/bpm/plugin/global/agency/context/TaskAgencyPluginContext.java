/*    */ package com.dstz.bpm.plugin.global.agency.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.global.agency.def.TaskAgencyPluginDef;
/*    */ import com.dstz.bpm.plugin.global.agency.executer.TaskAgencyPluginExecutor;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class TaskAgencyPluginContext
/*    */   extends AbstractBpmPluginContext<TaskAgencyPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = 3192828814663399776L;
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 25 */     List<EventType> eventTypes = new ArrayList<>();
/* 26 */     eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
/* 27 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 32 */     return (Class)TaskAgencyPluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 37 */     return "任务代理";
/*    */   }
/*    */ 
/*    */   
/*    */   protected TaskAgencyPluginDef parseFromJson(JSON json) {
/* 42 */     return (TaskAgencyPluginDef)JSON.toJavaObject(json, TaskAgencyPluginDef.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer getSn() {
/* 50 */     return Integer.valueOf(110);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/agency/context/TaskAgencyPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */