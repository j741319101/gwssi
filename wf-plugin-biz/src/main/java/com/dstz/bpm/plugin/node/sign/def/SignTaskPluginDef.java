/*    */ package com.dstz.bpm.plugin.node.sign.def;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ActionType;
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import javax.validation.constraints.Min;
/*    */ import org.hibernate.validator.constraints.NotBlank;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SignTaskPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   private boolean signMultiTask = false;
/* 26 */   private VoteType voteType = VoteType.PERCENT;
/*    */ 
/*    */   
/*    */   private boolean needAllSign = false;
/*    */ 
/*    */   
/*    */   @NotEmpty
/*    */   @Min(1L)
/* 34 */   private int voteAmount = 51;
/*    */ 
/*    */   
/*    */   @NotBlank
/* 38 */   private String opposedAction = ActionType.OPPOSE
/* 39 */     .getKey();
/*    */   
/*    */   public boolean isSignMultiTask() {
/* 42 */     return this.signMultiTask;
/*    */   }
/*    */   
/*    */   public void setSignMultiTask(boolean signMultiTask) {
/* 46 */     this.signMultiTask = signMultiTask;
/*    */   }
/*    */   
/*    */   public VoteType getVoteType() {
/* 50 */     return this.voteType;
/*    */   }
/*    */   
/*    */   public void setVoteType(VoteType voteType) {
/* 54 */     this.voteType = voteType;
/*    */   }
/*    */   
/*    */   public boolean isNeedAllSign() {
/* 58 */     return this.needAllSign;
/*    */   }
/*    */   
/*    */   public void setNeedAllSign(boolean needAllSign) {
/* 62 */     this.needAllSign = needAllSign;
/*    */   }
/*    */   
/*    */   public int getVoteAmount() {
/* 66 */     return this.voteAmount;
/*    */   }
/*    */   
/*    */   public void setVoteAmount(int voteAmount) {
/* 70 */     this.voteAmount = voteAmount;
/*    */   }
/*    */   
/*    */   public String getOpposedAction() {
/* 74 */     return this.opposedAction;
/*    */   }
/*    */   
/*    */   public void setOpposedAction(String opposedAction) {
/* 78 */     this.opposedAction = opposedAction;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/sign/def/SignTaskPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */