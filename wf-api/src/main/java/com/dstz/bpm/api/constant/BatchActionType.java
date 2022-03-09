/*    */ package com.dstz.bpm.api.constant;
/*    */ 
/*    */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*    */ import com.dstz.base.api.exception.BusinessException;

/*    */
/*    */ 
/*    */ 
/*    */ public enum BatchActionType
/*    */ {
/* 10 */   AGREE("agree", "同意", "taskAgreeActionBatchHandler"),
/* 11 */   SIGNAGREE("signAgree", "会签同意", "taskSignAgreeActionBatchHandler"),
/* 12 */   SAVE("save", "保存", "taskSaveActionBatchHandler"),
/* 13 */   OPPOSE("oppose", "反对", "taskOpposeActionBatchHandler"),
/* 14 */   SIGNOPPOSE("signOppose", "会签反对", "taskSignOpposeActionBatchHandler"),
/* 15 */   REJECT("reject", "驳回", "taskRejectActionBatchHandler"),
/* 16 */   REJECT2START("reject2Start", "驳回发起人", "taskReject2StartActionBatchHandler"),
/* 17 */   RECOVER("recover", "撤销", "instanceRecoverActionBatchHandler"),
/* 18 */   DISPENDSE("dispense", "分发", "null"),
/* 19 */   MANUALEND("manualEnd", "人工终止", "instanceManualEndActionBatchHandler"),
/*    */   
/* 21 */   LOCK("lock", "锁定", "taskLockActionBatchHandler"),
/* 22 */   UNLOCK("unlock", "解锁", "taskUnlockActionBatchHandler"),
/*    */   
/* 24 */   TURN("turn", "转办", "taskTurnActionBatchHandler"),
/* 25 */   REMINDER("reminder", "催办", "instanceReminderActionBatchHandler"),
/* 26 */   RECALL("recall", "撤回", "instanceRecallActionBatchHandler"),
/*    */   
/* 28 */   ADDSIGN("addSign", "加签", "addSignActionBatchHandler"),
/* 29 */   ADDDO("addDo", "加办", "addDoActionBatchHandler"),
/* 30 */   ADDDOAGREE("addDoAgree", "处理加办", "addDoAgreeActionBatchHandler"),
/* 31 */   CARBONCOPY("carbonCopy", "抄送", "carbonCopyActionBatchHandler"),
/* 32 */   CARBONINSTCOPY("carbonInstCopy", "流程抄送", "carbonInstCopyActionBatchHandler");
/*    */ 
/*    */   
/* 35 */   private String key = "";
/*    */   
/* 37 */   private String name = "";
/*    */   
/* 39 */   private String beanId = "";
/*    */ 
/*    */   
/*    */   BatchActionType(String key, String name, String beanId) {
/* 43 */     this.key = key;
/* 44 */     this.name = name;
/* 45 */     this.beanId = beanId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 50 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 54 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 58 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getBeanId() {
/* 62 */     return this.beanId;
/*    */   }
/*    */   
/*    */   public void setName(String value) {
/* 66 */     this.name = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BatchActionType fromKey(String key) {
/* 81 */     for (BatchActionType c : values()) {
/* 82 */       if (c.getKey().equalsIgnoreCase(key))
/* 83 */         return c; 
/*    */     } 
/* 85 */     throw new BusinessException(BpmStatusCode.NO_TASK_ACTION);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/BatchActionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */