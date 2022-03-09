/*    */ package cn.gwssi.ecloudbpm.wf.api.model.def;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ScriptType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventScript
/*    */ {
/*    */   private ScriptType scriptType;
/* 12 */   private String content = "";
/*    */ 
/*    */   
/*    */   public EventScript() {}
/*    */   
/*    */   public EventScript(ScriptType scriptType, String content) {
/* 18 */     this.scriptType = scriptType;
/* 19 */     this.content = content;
/*    */   }
/*    */   
/*    */   public ScriptType getScriptType() {
/* 23 */     return this.scriptType;
/*    */   }
/*    */   
/*    */   public void setScriptType(ScriptType scriptType) {
/* 27 */     this.scriptType = scriptType;
/*    */   }
/*    */   
/*    */   public String getContent() {
/* 31 */     return this.content;
/*    */   }
/*    */   
/*    */   public void setContent(String content) {
/* 35 */     this.content = content;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/def/EventScript.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */