/*    */ package cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.BpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GateWayBpmNodeDef
/*    */   extends BaseBpmNodeDef
/*    */ {
/*    */   public List<BpmPluginContext> getBpmPluginContexts() {
/* 21 */     return Collections.emptyList();
/*    */   }
/*    */   
/* 24 */   private Map<String, String> outGoingConditions = new HashMap<>();
/*    */   
/*    */   public BpmProcessDef getChildBpmProcessDef() {
/* 27 */     throw new RuntimeException("GateWayBpmNodeDef not support getChildBpmProcessDef method");
/*    */   }
/*    */   
/*    */   public Map<String, String> getOutGoingConditions() {
/* 31 */     return this.outGoingConditions;
/*    */   }
/*    */   
/*    */   public void setOutGoingConditions(Map<String, String> outGoingConditions) {
/* 35 */     this.outGoingConditions = outGoingConditions;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/nodedef/impl/GateWayBpmNodeDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */