/*    */ package com.dstz.bpm.plugin.node.dynaMultTaskReset.handler;
/*    */ 
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.plugin.node.dynamictask.executer.DynamicInstTaskAction;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class DynamicTaskRessetActoin
/*    */ {
/*    */   @Resource
/*    */   private DynamicInstTaskAction dynamicInstTaskAction;
/*    */   
/*    */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 17 */     return Boolean.valueOf((this.dynamicInstTaskAction.isDynamicTask(nodeDef) || this.dynamicInstTaskAction.isDynamicInstTask(nodeDef)));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynaMultTaskReset/handler/DynamicTaskRessetActoin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */