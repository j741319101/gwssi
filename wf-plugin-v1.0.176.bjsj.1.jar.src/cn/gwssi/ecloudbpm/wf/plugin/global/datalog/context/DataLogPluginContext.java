/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.datalog.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.IExtendGlobalPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.datalog.def.DataLogPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.datalog.executer.DataLogPluginExecutor;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component("dataLogPluginContext")
/*    */ @Scope("prototype")
/*    */ public class DataLogPluginContext
/*    */   extends AbstractBpmPluginContext<DataLogPluginDef>
/*    */   implements IExtendGlobalPluginContext
/*    */ {
/*    */   private static final long serialVersionUID = -1563849340056571771L;
/*    */   
/*    */   public List getEventTypes() {
/* 28 */     List<EventType> eventTypes = new ArrayList<>();
/* 29 */     eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
/* 30 */     eventTypes.add(EventType.START_EVENT);
/* 31 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 36 */     return (Class)DataLogPluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 41 */     return "BO 数据提交日志";
/*    */   }
/*    */ 
/*    */   
/*    */   protected DataLogPluginDef parseFromJson(JSON json) {
/* 46 */     return (DataLogPluginDef)JSON.toJavaObject(json, DataLogPluginDef.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 51 */     return "dataLog";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/datalog/context/DataLogPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */