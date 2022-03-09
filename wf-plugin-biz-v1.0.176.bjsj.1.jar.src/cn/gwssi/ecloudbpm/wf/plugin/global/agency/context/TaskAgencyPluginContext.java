/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.agency.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.agency.def.TaskAgencyPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.agency.executer.TaskAgencyPluginExecutor;
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
/* 27 */     eventTypes.add(EventType.TASK_POST_COMPLETE_EVENT);
/* 28 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 33 */     return (Class)TaskAgencyPluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 38 */     return "任务代理";
/*    */   }
/*    */ 
/*    */   
/*    */   protected TaskAgencyPluginDef parseFromJson(JSON json) {
/* 43 */     return (TaskAgencyPluginDef)JSON.toJavaObject(json, TaskAgencyPluginDef.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer getSn() {
/* 51 */     return Integer.valueOf(110);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/agency/context/TaskAgencyPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */