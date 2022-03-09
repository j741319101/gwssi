/*    */ package com.dstz.bpm.plugin.node.dynaMultTaskReset.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.plugin.global.multinst.executer.MultInstTaskAction;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class MultInstTaskResetAction
/*    */ {
/*    */   @Resource
/*    */   private MultInstTaskAction multInstTaskAction;
/*    */   
/*    */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 17 */     BpmProcessDef bpmProcessDef = nodeDef.getBpmProcessDef();
/* 18 */     return Boolean.valueOf(this.multInstTaskAction.isContainNode(nodeDef.getNodeId(), bpmProcessDef, "none"));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynaMultTaskReset/handler/MultInstTaskResetAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */