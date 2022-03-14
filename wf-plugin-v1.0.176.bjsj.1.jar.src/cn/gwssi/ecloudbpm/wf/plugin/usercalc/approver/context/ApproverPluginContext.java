/*    */ package com.dstz.bpm.plugin.usercalc.approver.context;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
/*    */ import com.dstz.bpm.plugin.usercalc.approver.def.ApproverPluginDef;
/*    */ import com.dstz.bpm.plugin.usercalc.approver.executer.ApproverPluginExecutor;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class ApproverPluginContext
/*    */   extends AbstractUserCalcPluginContext<ApproverPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = 2164348894650802414L;
/*    */   
/*    */   public String getDescription() {
/* 26 */     return "流程历史审批人";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 31 */     return "流程历史审批人";
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 36 */     return (Class)ApproverPluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ApproverPluginDef parseJson(JSONObject pluginJson) {
/* 41 */     ApproverPluginDef def = new ApproverPluginDef();
/* 42 */     return def;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/approver/context/ApproverPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */