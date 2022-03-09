/*    */ package com.dstz.bpm.plugin.global.nodemessage.def;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.List;
/*    */ import javax.validation.Valid;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NodeMessagePluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   @Valid
/*    */   @NotEmpty
/*    */   private List<NodeMessage> nodeMessageList;
/*    */   
/*    */   public NodeMessagePluginDef(List<NodeMessage> nodeMessageList) {
/* 20 */     this.nodeMessageList = nodeMessageList;
/*    */   }
/*    */   
/*    */   public List<NodeMessage> getNodeMessageList() {
/* 24 */     return this.nodeMessageList;
/*    */   }
/*    */   
/*    */   public void setNodeMessageList(List<NodeMessage> nodeMessageList) {
/* 28 */     this.nodeMessageList = nodeMessageList;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/nodemessage/def/NodeMessagePluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */