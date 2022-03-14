/*    */ package com.dstz.bpm.plugin.global.leaderTask.def;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ 
/*    */ public class LeaderTaskPluginDef extends AbstractBpmExecutionPluginDef {
/*    */   private boolean signLeaderTask = false;
/*    */   
/*    */   public boolean isSignLeaderTask() {
/*  9 */     return this.signLeaderTask;
/*    */   }
/*    */   
/*    */   public void setSignLeaderTask(boolean signLeaderTask) {
/* 13 */     this.signLeaderTask = signLeaderTask;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/leaderTask/def/LeaderTaskPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */