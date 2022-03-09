/*    */ package cn.gwssi.ecloudbpm.wf.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum NodeType
/*    */ {
/* 11 */   START("StartNoneEvent", "开始节点"),
/* 12 */   END("EndNoneEvent", "结束节点"),
/* 13 */   USERTASK("UserTask", "用户任务节点"),
/* 14 */   SIGNTASK("SignTask", "会签任务节点"),
/* 15 */   SUBPROCESS("SubProcess", "子流程"),
/* 16 */   CALLACTIVITY("CallActivity", "外部子流程"),
/* 17 */   EXCLUSIVEGATEWAY("ExclusiveGateway", "分支网关"),
/* 18 */   PARALLELGATEWAY("ParallelGateway", "同步网关"),
/* 19 */   INCLUSIVEGATEWAY("InclusiveGateway", "条件网关"),
/* 20 */   SUBSTARTGATEWAY("SubStartGateway", "内嵌子流程开始网关"),
/* 21 */   SUBENDGATEWAY("SubEndGateway", "内嵌子流程结束网关"),
/* 22 */   SUBMULTISTARTGATEWAY("SubMultiStartGateway", "多实例内嵌子流程开始网关"),
/*    */   
/* 24 */   SERVICETASK("ServiceTask", "服务任务节点");
/*    */ 
/*    */   
/* 27 */   private String key = "";
/*    */   
/* 29 */   private String value = "";
/*    */ 
/*    */   
/*    */   NodeType(String key, String value) {
/* 33 */     this.key = key;
/* 34 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 39 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 43 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 47 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 51 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NodeType fromKey(String key) {
/* 66 */     for (NodeType c : values()) {
/* 67 */       if (c.getKey().equalsIgnoreCase(key))
/* 68 */         return c; 
/*    */     } 
/* 70 */     throw new IllegalArgumentException(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/NodeType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */