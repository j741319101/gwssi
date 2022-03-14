/*    */ package com.dstz.bpm.api.engine.data.result;
/*    */ 
/*    */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ 
/*    */ public class BpmFlowInstanceData
/*    */   extends BpmFlowData
/*    */ {
/*    */   @ApiModelProperty("流程实例信息")
/*    */   private IBpmInstance instance;
/*    */   
/*    */   public IBpmInstance getInstance() {
/* 13 */     return this.instance;
/*    */   }
/*    */   
/*    */   public void setInstance(IBpmInstance instance) {
/* 17 */     this.instance = instance;
/* 18 */     this.defId = instance.getDefId();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/data/result/BpmFlowInstanceData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */