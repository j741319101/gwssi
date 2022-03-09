/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.def.LeaderTaskPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.executer.LeaderTaskPluginExecuter;
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
/*    */ public class LeaderTaskPluginContext
/*    */   extends AbstractBpmPluginContext<LeaderTaskPluginDef>
/*    */ {
/* 22 */   private Integer sn = Integer.valueOf(102);
/*    */   
/*    */   protected LeaderTaskPluginDef parseFromJson(JSON json) {
/* 25 */     return (LeaderTaskPluginDef)JSON.toJavaObject(json, LeaderTaskPluginDef.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 30 */     List<EventType> eventTypes = new ArrayList<>();
/* 31 */     eventTypes.add(EventType.TASK_POST_COMPLETE_EVENT);
/* 32 */     eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
/* 33 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 38 */     return (Class)LeaderTaskPluginExecuter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 43 */     return "领导任务";
/*    */   }
/*    */   
/*    */   public Integer getSn() {
/* 47 */     return this.sn;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/leaderTask/context/LeaderTaskPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */