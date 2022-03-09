/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.script.def;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NodeScriptPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/* 16 */   private String nodeId = "";
/* 17 */   private Map<EventType, String> script = new HashMap<>();
/*    */   
/*    */   public String getEvnetnScript(EventType event) {
/* 20 */     return this.script.get(event);
/*    */   }
/*    */   
/*    */   public void setEvnetnScript(EventType event, String scritp) {
/* 24 */     this.script.put(event, scritp);
/*    */   }
/*    */   
/*    */   public Map<EventType, String> getScript() {
/* 28 */     return this.script;
/*    */   }
/*    */   
/*    */   public void setScript(Map<EventType, String> script) {
/* 32 */     this.script = script;
/*    */   }
/*    */   
/*    */   public String getNodeId() {
/* 36 */     return this.nodeId;
/*    */   }
/*    */   
/*    */   public void setNodeId(String nodeId) {
/* 40 */     this.nodeId = nodeId;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/script/def/NodeScriptPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */