/*    */ package com.dstz.bpm.plugin.global.multinst.def;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class MultInstPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/* 21 */   List<MultInst> multInsts = new ArrayList<>();
/*    */   
/*    */   public List<MultInst> getMultInsts() {
/* 24 */     return this.multInsts;
/*    */   }
/*    */   
/*    */   public void setMultInsts(List<MultInst> multInsts) {
/* 28 */     this.multInsts = multInsts;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/multinst/def/MultInstPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */