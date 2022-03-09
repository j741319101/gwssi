/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.script.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.script.def.GlobalScript;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.script.def.GlobalScriptPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.script.executer.GlobalScriptPluginExecutor;
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
/*    */ public class GlobalScriptPluginContext
/*    */   extends AbstractBpmPluginContext<GlobalScriptPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = -5958682303600423597L;
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 26 */     List<EventType> list = new ArrayList<>();
/*    */     
/* 28 */     list.add(EventType.TASK_COMPLETE_EVENT);
/* 29 */     list.add(EventType.TASK_CREATE_EVENT);
/*    */     
/* 31 */     list.add(EventType.START_EVENT);
/* 32 */     list.add(EventType.END_EVENT);
/* 33 */     list.add(EventType.MANUAL_END);
/* 34 */     list.add(EventType.RECOVER_EVENT);
/* 35 */     list.add(EventType.DELETE_EVENT);
/*    */     
/* 37 */     list.add(EventType.TASK_POST_CREATE_EVENT);
/* 38 */     list.add(EventType.TASK_PRE_COMPLETE_EVENT);
/* 39 */     list.add(EventType.TASK_POST_COMPLETE_EVENT);
/*    */     
/* 41 */     list.add(EventType.TASK_SIGN_CREATE_EVENT);
/* 42 */     list.add(EventType.TASK_SIGN_POST_CREATE_EVENT);
/* 43 */     return list;
/*    */   }
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 47 */     return (Class)GlobalScriptPluginExecutor.class;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 53 */     return "全局脚本";
/*    */   }
/*    */ 
/*    */   
/*    */   protected GlobalScriptPluginDef parseFromJson(JSON json) {
/* 58 */     List<GlobalScript> globalScripts = JSON.parseArray(json.toJSONString(), GlobalScript.class);
/* 59 */     GlobalScriptPluginDef globalScriptPluginDef = new GlobalScriptPluginDef();
/* 60 */     globalScriptPluginDef.setGlobalScripts(globalScripts);
/* 61 */     return globalScriptPluginDef;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/script/context/GlobalScriptPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */