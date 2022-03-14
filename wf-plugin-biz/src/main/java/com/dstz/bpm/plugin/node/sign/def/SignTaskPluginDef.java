package com.dstz.bpm.plugin.node.sign.def;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class SignTaskPluginDef extends AbstractBpmExecutionPluginDef {
   private boolean signMultiTask = false;
   private VoteType voteType;
   private boolean needAllSign;
   private boolean needSupervise;
   @Range(
      message = "投票数/百分比必须位于 1~100 之间",
      max = 100L,
      min = 1L
   )
   @NotNull(
      message = "投票数/百分比 不能为空"
   )
   private Integer voteAmount;
   @NotBlank
   private String opposedAction;
   private boolean reset;

   public SignTaskPluginDef() {
      this.voteType = VoteType.PERCENT;
      this.needAllSign = false;
      this.needSupervise = false;
      this.voteAmount = 51;
      this.opposedAction = ActionType.OPPOSE.getKey();
      this.reset = true;
   }

   public boolean isSignMultiTask() {
      return this.signMultiTask;
   }

   public void setSignMultiTask(boolean signMultiTask) {
      this.signMultiTask = signMultiTask;
   }

   public VoteType getVoteType() {
      return this.voteType;
   }

   public void setVoteType(VoteType voteType) {
      this.voteType = voteType;
   }

   public boolean isNeedAllSign() {
      return this.needAllSign;
   }

   public Integer getVoteAmount() {
      return this.voteAmount;
   }

   public void setVoteAmount(Integer voteAmount) {
      this.voteAmount = voteAmount;
   }

   public void setNeedAllSign(boolean needAllSign) {
      this.needAllSign = needAllSign;
   }

   public String getOpposedAction() {
      return this.opposedAction;
   }

   public void setOpposedAction(String opposedAction) {
      this.opposedAction = opposedAction;
   }

   public boolean isNeedSupervise() {
      return this.needSupervise;
   }

   public void setNeedSupervise(boolean needSupervise) {
      this.needSupervise = needSupervise;
   }

   public boolean isReset() {
      return this.reset;
   }

   public void setReset(boolean reset) {
      this.reset = reset;
   }
}
