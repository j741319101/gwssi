/*    */ package com.dstz.bpm.plugin.global.leaderTask.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.context.DefaultGlobalPluginContext;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.global.leaderTask.def.LeaderTaskPluginDef;
/*    */ import com.dstz.bpm.plugin.global.leaderTask.executer.LeaderPluginExecuter;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class LeaderTaskPlaginContext
/*    */   extends AbstractBpmPluginContext<LeaderTaskPluginDef>
/*    */   implements DefaultGlobalPluginContext
/*    */ {
/*    */   @Value("${bpm.plugin.leader.flag}")
/* 25 */   private String LEADER_FLAG = "N";
/*    */   
/* 27 */   private Integer sn = Integer.valueOf(102);
/*    */   
/*    */   protected LeaderTaskPluginDef parseFromJson(JSON json) {
/* 30 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 35 */     List<EventType> eventTypes = new ArrayList<>();
/* 36 */     eventTypes.add(EventType.TASK_POST_COMPLETE_EVENT);
/* 37 */     eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
/* 38 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 43 */     return (Class)LeaderPluginExecuter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 48 */     return "领导任务";
/*    */   }
/*    */   
/*    */   public Integer getSn() {
/* 52 */     return this.sn;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEffective() {
/* 57 */     return StringUtils.equals("Y", this.LEADER_FLAG);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/leaderTask/context/LeaderTaskPlaginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */