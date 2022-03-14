/*    */ package com.dstz.bpm.plugin.node.userassign.def;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.validation.Valid;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserAssignPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   @Valid
/* 18 */   List<UserAssignRule> ruleList = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public List<UserAssignRule> getRuleList() {
/* 22 */     return this.ruleList;
/*    */   }
/*    */   
/*    */   public void setRuleList(List<UserAssignRule> ruleList) {
/* 26 */     this.ruleList = ruleList;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/userassign/def/UserAssignPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */