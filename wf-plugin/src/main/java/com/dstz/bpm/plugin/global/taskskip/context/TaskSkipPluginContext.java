/*    */ package com.dstz.bpm.plugin.global.taskskip.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
/*    */ import com.dstz.bpm.plugin.global.taskskip.executer.TaskSkipPluginExecutor;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */
import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class TaskSkipPluginContext
/*    */   extends AbstractBpmPluginContext<TaskSkipPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = -8171025388788811778L;
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 26 */     List<EventType> list = new ArrayList<>();
/* 27 */     list.add(EventType.TASK_POST_CREATE_EVENT);
/* 28 */     return list;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 33 */     return (Class) TaskSkipPluginExecutor.class;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TaskSkipPluginDef parseFromJson(JSON pluginJson) {
/* 39 */     TaskSkipPluginDef def = (TaskSkipPluginDef)JSON.toJavaObject(pluginJson, TaskSkipPluginDef.class);
/* 40 */     return def;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 46 */     return "任务跳过";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/taskskip/context/TaskSkipPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */