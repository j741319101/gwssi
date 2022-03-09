/*    */ package com.dstz.bpm.plugin.global.datalog.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.global.datalog.def.DataLogPluginDef;
/*    */ import com.dstz.bpm.plugin.global.datalog.executer.DataLogPluginExecutor;
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
/*    */ 
/*    */ @Component("dataLogPluginContext")
/*    */ @Scope("prototype")
/*    */ public class DataLogPluginContext
/*    */   extends AbstractBpmPluginContext<DataLogPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = -1563849340056571771L;
/*    */   
/*    */   public List getEventTypes() {
/* 27 */     List<EventType> eventTypes = new ArrayList<>();
/* 28 */     eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
/* 29 */     eventTypes.add(EventType.START_EVENT);
/* 30 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 35 */     return (Class) DataLogPluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 40 */     return "BO 数据提交日志";
/*    */   }
/*    */ 
/*    */   
/*    */   protected DataLogPluginDef parseFromJson(JSON json) {
/* 45 */     return (DataLogPluginDef)JSON.toJavaObject(json, DataLogPluginDef.class);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/datalog/context/DataLogPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */