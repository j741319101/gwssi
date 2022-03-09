/*    */ package com.dstz.bpm.plugin.usercalc.samenode.def;
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractUserCalcPluginDef;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ public class SameNodePluginDef extends AbstractUserCalcPluginDef {
/*    */   @NotEmpty(message = "人员插件相同节点执行人，节点ID不能为空")
/*  7 */   private String nodeId = "";
/*    */ 
/*    */   
/*    */   public String getNodeId() {
/* 11 */     return this.nodeId;
/*    */   }
/*    */   
/*    */   public void setNodeId(String nodeId) {
/* 15 */     this.nodeId = nodeId;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/usercalc/samenode/def/SameNodePluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */