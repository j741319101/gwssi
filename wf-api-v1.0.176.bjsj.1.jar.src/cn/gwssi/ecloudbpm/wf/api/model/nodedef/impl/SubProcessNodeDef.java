/*    */ package com.dstz.bpm.api.model.nodedef.impl;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.NodeType;
/*    */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*    */ 
/*    */ 
/*    */ public class SubProcessNodeDef
/*    */   extends BaseBpmNodeDef
/*    */ {
/*    */   private static final long serialVersionUID = -1165886168391484970L;
/*    */   private BpmProcessDef bpmChildProcessDef;
/*    */   
/*    */   public SubProcessNodeDef() {
/* 14 */     setType(NodeType.SUBPROCESS);
/*    */   }
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
/*    */   public BpmProcessDef getChildBpmProcessDef() {
/* 27 */     return this.bpmChildProcessDef;
/*    */   }
/*    */   
/*    */   public void setChildBpmProcessDef(BpmProcessDef bpmChildProcessDef) {
/* 31 */     this.bpmChildProcessDef = bpmChildProcessDef;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/nodedef/impl/SubProcessNodeDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */