/*     */ package com.dstz.bpm.plugin.node.sign.def;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import org.hibernate.validator.constraints.NotBlank;
/*     */ import org.hibernate.validator.constraints.Range;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignTaskPluginDef
/*     */   extends AbstractBpmExecutionPluginDef
/*     */ {
/*     */   private boolean signMultiTask = false;
/*  26 */   private VoteType voteType = VoteType.PERCENT;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean needAllSign = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean needSupervise = false;
/*     */ 
/*     */ 
/*     */   
/*     */   @Range(message = "投票数/百分比必须位于 1~100 之间", max = 100L, min = 1L)
/*     */   @NotNull(message = "投票数/百分比 不能为空")
/*  41 */   private Integer voteAmount = Integer.valueOf(51);
/*     */   @NotBlank
/*  43 */   private String opposedAction = ActionType.OPPOSE
/*  44 */     .getKey();
/*     */ 
/*     */   
/*     */   private boolean reset = true;
/*     */ 
/*     */   
/*     */   public boolean isSignMultiTask() {
/*  51 */     return this.signMultiTask;
/*     */   }
/*     */   
/*     */   public void setSignMultiTask(boolean signMultiTask) {
/*  55 */     this.signMultiTask = signMultiTask;
/*     */   }
/*     */   
/*     */   public VoteType getVoteType() {
/*  59 */     return this.voteType;
/*     */   }
/*     */   
/*     */   public void setVoteType(VoteType voteType) {
/*  63 */     this.voteType = voteType;
/*     */   }
/*     */   
/*     */   public boolean isNeedAllSign() {
/*  67 */     return this.needAllSign;
/*     */   }
/*     */   
/*     */   public Integer getVoteAmount() {
/*  71 */     return this.voteAmount;
/*     */   }
/*     */   
/*     */   public void setVoteAmount(Integer voteAmount) {
/*  75 */     this.voteAmount = voteAmount;
/*     */   }
/*     */   
/*     */   public void setNeedAllSign(boolean needAllSign) {
/*  79 */     this.needAllSign = needAllSign;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOpposedAction() {
/*  84 */     return this.opposedAction;
/*     */   }
/*     */   
/*     */   public void setOpposedAction(String opposedAction) {
/*  88 */     this.opposedAction = opposedAction;
/*     */   }
/*     */   
/*     */   public boolean isNeedSupervise() {
/*  92 */     return this.needSupervise;
/*     */   }
/*     */   
/*     */   public void setNeedSupervise(boolean needSupervise) {
/*  96 */     this.needSupervise = needSupervise;
/*     */   }
/*     */   
/*     */   public boolean isReset() {
/* 100 */     return this.reset;
/*     */   }
/*     */   
/*     */   public void setReset(boolean reset) {
/* 104 */     this.reset = reset;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/sign/def/SignTaskPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */