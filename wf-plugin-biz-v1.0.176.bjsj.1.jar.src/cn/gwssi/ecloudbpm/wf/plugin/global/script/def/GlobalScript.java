/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.script.def;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GlobalScript
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/* 14 */   private String eventKeys = "";
/*    */ 
/*    */   
/*    */   @NotEmpty(message = "脚本")
/*    */   private String script;
/*    */ 
/*    */ 
/*    */   
/*    */   public String getScript() {
/* 23 */     return this.script;
/*    */   }
/*    */   
/*    */   public void setScript(String script) {
/* 27 */     this.script = script;
/*    */   }
/*    */   
/*    */   public String getEventKeys() {
/* 31 */     return this.eventKeys;
/*    */   }
/*    */   
/*    */   public void setEventKeys(String eventKeys) {
/* 35 */     this.eventKeys = eventKeys;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/script/def/GlobalScript.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */