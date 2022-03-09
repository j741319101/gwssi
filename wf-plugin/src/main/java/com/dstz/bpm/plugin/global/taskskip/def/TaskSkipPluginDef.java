/*    */ package com.dstz.bpm.plugin.global.taskskip.def;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TaskSkipPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   private String skipTypeArr;
/* 12 */   private String script = "";
/*    */ 
/*    */   
/*    */   public String getScript() {
/* 16 */     return this.script;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setScript(String script) {
/* 21 */     this.script = script;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSkipTypeArr() {
/* 26 */     return this.skipTypeArr;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSkipTypeArr(String skipTypeArr) {
/* 31 */     this.skipTypeArr = skipTypeArr;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/taskskip/def/TaskSkipPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */