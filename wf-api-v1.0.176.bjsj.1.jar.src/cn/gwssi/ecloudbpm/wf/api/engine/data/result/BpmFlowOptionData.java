/*    */ package cn.gwssi.ecloudbpm.wf.api.engine.data.result;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTaskOpinion;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ 
/*    */ public class BpmFlowOptionData extends BpmFlowInstanceData {
/*    */   @ApiModelProperty("任务审核信息")
/*    */   private IBpmTaskOpinion opinion;
/*    */   
/*    */   public IBpmTaskOpinion getOpinion() {
/* 11 */     return this.opinion;
/*    */   }
/*    */   
/*    */   public void setOpinion(IBpmTaskOpinion opinion) {
/* 15 */     this.opinion = opinion;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/data/result/BpmFlowOptionData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */