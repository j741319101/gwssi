/*    */ package com.dstz.bpm.plugin.node.ruleskip.def;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JumpRule
/*    */   implements Serializable
/*    */ {
/*    */   @NotEmpty(message = "跳转规则名字不能为空")
/* 15 */   private String name = "";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotEmpty(message = "跳转规则目标节点不能为空")
/* 21 */   private String targetNode = "";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotEmpty(message = "跳转规则条件不能为空")
/* 27 */   private String script = "";
/*    */ 
/*    */ 
/*    */   
/*    */   public JumpRule() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public JumpRule(String ruleName, String targetNode, String condition) {
/* 36 */     this.name = ruleName;
/* 37 */     this.targetNode = targetNode;
/* 38 */     this.script = condition;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 42 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String ruleName) {
/* 46 */     this.name = ruleName;
/*    */   }
/*    */   
/*    */   public String getTargetNode() {
/* 50 */     return this.targetNode;
/*    */   }
/*    */   
/*    */   public void setTargetNode(String targetNode) {
/* 54 */     this.targetNode = targetNode;
/*    */   }
/*    */   
/*    */   public String getScript() {
/* 58 */     return this.script;
/*    */   }
/*    */   
/*    */   public void setScript(String script) {
/* 62 */     this.script = script;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/ruleskip/def/JumpRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */