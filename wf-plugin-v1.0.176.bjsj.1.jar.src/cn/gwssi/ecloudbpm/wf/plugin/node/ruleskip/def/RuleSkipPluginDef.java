/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.ruleskip.def;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.validation.Valid;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RuleSkipPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   @Valid
/* 16 */   private List<JumpRule> jumpRules = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public List<JumpRule> getJumpRules() {
/* 20 */     return this.jumpRules;
/*    */   }
/*    */   
/*    */   public void setJumpRules(List<JumpRule> jumpRules) {
/* 24 */     this.jumpRules = jumpRules;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/ruleskip/def/RuleSkipPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */