/*    */ package com.dstz.bpm.plugin.global.script.def;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NodeScriptPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   @NotEmpty(message = "事件脚本节点ID不能为空")
/* 16 */   private String nodeId = "";
/*    */   
/* 18 */   private Map<EventType, String> script = new HashMap<>();
/*    */   
/*    */   public String getEvnetnScript(EventType event) {
/* 21 */     return this.script.get(event);
/*    */   }
/*    */   
/*    */   public void setEvnetnScript(EventType event, String scritp) {
/* 25 */     this.script.put(event, scritp);
/*    */   }
/*    */   
/*    */   public Map<EventType, String> getScript() {
/* 29 */     return this.script;
/*    */   }
/*    */   
/*    */   public void setScript(Map<EventType, String> script) {
/* 33 */     this.script = script;
/*    */   }
/*    */   
/*    */   public String getNodeId() {
/* 37 */     return this.nodeId;
/*    */   }
/*    */   
/*    */   public void setNodeId(String nodeId) {
/* 41 */     this.nodeId = nodeId;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/script/def/NodeScriptPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */