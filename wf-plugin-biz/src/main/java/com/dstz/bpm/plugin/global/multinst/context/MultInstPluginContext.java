/*    */ package com.dstz.bpm.plugin.global.multinst.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.global.multinst.def.MultInst;
/*    */ import com.dstz.bpm.plugin.global.multinst.def.MultInstPluginDef;
/*    */ import com.dstz.bpm.plugin.global.multinst.executer.MultInstPluginExecuter;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class MultInstPluginContext
/*    */   extends AbstractBpmPluginContext<MultInstPluginDef> {
/*    */   private static final long serialVersionUID = -5974567972412827332L;
/* 21 */   private Integer sn = Integer.valueOf(102);
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 24 */     List<EventType> eventTypes = new ArrayList<>();
/* 25 */     eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
/* 26 */     eventTypes.add(EventType.TASK_COMPLETE_EVENT);
/* 27 */     eventTypes.add(EventType.TASK_CREATE_EVENT);
/* 28 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 33 */     return (Class)MultInstPluginExecuter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 38 */     return "多实例";
/*    */   }
/*    */ 
/*    */   
/*    */   protected MultInstPluginDef parseFromJson(JSON json) {
/* 43 */     List<MultInst> multInsts = JSON.parseArray(json.toJSONString(), MultInst.class);
/* 44 */     MultInstPluginDef def = new MultInstPluginDef();
/* 45 */     def.setMultInsts(multInsts);
/* 46 */     return def;
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer getSn() {
/* 51 */     return this.sn;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/multinst/context/MultInstPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */