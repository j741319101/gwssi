/*    */ package com.dstz.bpm.plugin.global.script.def;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GlobalScriptPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   private List<GlobalScript> globalScripts;
/*    */   
/*    */   public List<GlobalScript> getGlobalScripts() {
/* 18 */     return this.globalScripts;
/*    */   }
/*    */   
/*    */   public void setGlobalScripts(List<GlobalScript> globalScripts) {
/* 22 */     this.globalScripts = globalScripts;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/script/def/GlobalScriptPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */