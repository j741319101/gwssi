/*    */ package cn.gwssi.ecloudbpm.wf.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum NodeStatus
/*    */ {
/* 11 */   SUBMIT("submit", "提交"),
/* 12 */   RE_SUBMIT("resubmit", "重新提交"),
/* 13 */   AGREE("agree", "同意"),
/* 14 */   PENDING("pending", "待审批"),
/* 15 */   OPPOSE("oppose", "反对"),
/* 16 */   BACK("back", "驳回"),
/* 17 */   BACK_TO_START("backToStart", "驳回到发起人"),
/* 18 */   COMPLETE("complete", "完成"),
/* 19 */   RECOVER("recover", "撤回"),
/* 20 */   RECOVER_TO_START("recoverToStart", "撤回到发起人"),
/* 21 */   SIGN_PASS("sign_pass", "会签通过"),
/* 22 */   SIGN_NO_PASS("sign_no_pass", "会签不通过"),
/* 23 */   MANUAL_END("manual_end", "人工终止"),
/* 24 */   ABANDON("abandon", "弃权"),
/* 25 */   SUSPEND("suspend", "挂起");
/*    */ 
/*    */   
/* 28 */   private String key = "";
/*    */   
/* 30 */   private String value = "";
/*    */ 
/*    */   
/*    */   NodeStatus(String key, String value) {
/* 34 */     this.key = key;
/* 35 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 40 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 44 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 48 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 52 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NodeStatus fromKey(String key) {
/* 67 */     for (NodeStatus c : values()) {
/* 68 */       if (c.getKey().equalsIgnoreCase(key))
/* 69 */         return c; 
/*    */     } 
/* 71 */     throw new IllegalArgumentException(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/NodeStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */