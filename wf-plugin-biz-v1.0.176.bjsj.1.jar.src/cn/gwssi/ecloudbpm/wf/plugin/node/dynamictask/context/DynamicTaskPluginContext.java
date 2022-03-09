/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.executer.DynamicTaskPluginExecuter;
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
/* 32 */     eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
/* 33 */     eventTypes.add(EventType.TASK_CREATE_EVENT);
/* 34 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 39 */     return (Class)DynamicTaskPluginExecuter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 44 */     return "动态任务插件";
/*    */   }
/*    */ 
/*    */   
/*    */   protected DynamicTaskPluginDef parseFromJson(JSON json) {
/* 49 */     DynamicTaskPluginDef def = (DynamicTaskPluginDef)JSON.toJavaObject(json, DynamicTaskPluginDef.class);
/* 50 */     return def;
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer getSn() {
/* 55 */     return Integer.valueOf(99);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/dynamictask/context/DynamicTaskPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */