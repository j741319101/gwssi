/*    */ package com.dstz.bpm.api.model.remote;
/*    */ 
/*    */ import com.dstz.bus.api.remote.RemoteBusinessData;
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ApiModel(description = "流程远程业务对象DTO")
/*    */ public class BpmRemoteBusinessData<T>
/*    */   extends RemoteBusinessData<T>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   @ApiModelProperty("流程KEY")
/*    */   protected String flowKey;
/*    */   @ApiModelProperty("实例ID")
/*    */   protected String instanceId;
/*    */   @ApiModelProperty("节点ID")
/*    */   protected String nodeId;
/*    */   @ApiModelProperty("动作名")
/*    */   protected String actionName;
/*    */   
/*    */   public String getFlowKey() {
/* 40 */     return this.flowKey;
/*    */   }
/*    */   public void setFlowKey(String flowKey) {
/* 43 */     this.flowKey = flowKey;
/*    */   }
/*    */   public String getInstanceId() {
/* 46 */     return this.instanceId;
/*    */   }
/*    */   public void setInstanceId(String instanceId) {
/* 49 */     this.instanceId = instanceId;
/*    */   }
/*    */   public String getNodeId() {
/* 52 */     return this.nodeId;
/*    */   }
/*    */   public void setNodeId(String nodeId) {
/* 55 */     this.nodeId = nodeId;
/*    */   }
/*    */   public String getActionName() {
/* 58 */     return this.actionName;
/*    */   }
/*    */   public void setActionName(String actionName) {
/* 61 */     this.actionName = actionName;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/remote/BpmRemoteBusinessData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */