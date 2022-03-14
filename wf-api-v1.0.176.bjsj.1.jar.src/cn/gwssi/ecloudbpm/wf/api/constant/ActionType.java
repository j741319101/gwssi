/*     */ package com.dstz.bpm.api.constant;
/*     */ 
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.util.DynamicEnumUtil;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ActionType
/*     */ {
/*  13 */   DRAFT("draft", "保存草稿", "instanceSaveActionHandler"),
/*  14 */   START("start", "启动", "instanceStartActionHandler"),
/*  15 */   AGREE("agree", "同意", "taskAgreeActionHandler"),
/*  16 */   SIGNAGREE("signAgree", "同意", "taskSignAgreeActionHandler"),
/*  17 */   SAVE("save", "保存", "taskSaveActionHandler"),
/*  18 */   OPPOSE("oppose", "反对", "taskOpposeActionHandler"),
/*  19 */   SIGNOPPOSE("signOppose", "否决", "taskSignOpposeActionHandler"),
/*  20 */   REJECT("reject", "驳回", "taskRejectActionHandler"),
/*  21 */   REJECT2START("reject2Start", "驳回发起人", "taskReject2StartActionHandler"),
/*     */ 
/*     */   
/*  24 */   RECOVER("recover", "撤销", "instanceRecoverActionHandler"),
/*  25 */   INCREASEDYNAMIC("increaseDynamic", "追加", "increaseTaskHandler"),
/*  26 */   DECREASEDYNAMIC("decreaseDynamic", "取回", "decreaseTaskHandler"),
/*  27 */   DISPENDSE("dispense", "分发", "null"),
/*  28 */   TASKOPINION("taskOpinion", "审批历史", "instanceTaskOpinionActionHandler"),
/*  29 */   FLOWIMAGE("flowImage", "流程图", "instanceImageActionHandler"),
/*  30 */   PRINT("print", "打印", "instancePrintActionHandler"),
/*  31 */   MANUALEND("manualEnd", "人工终止", "instanceManualEndActionHandler"),
/*  32 */   INSTANCE_END("instanceEnd", "超管终止", "instanceEndActionHandler"),
/*  33 */   TASK_FREE_JUMP("taskFreeJump", "超管跳转", "taskFreeJumpActionHandler"),
/*  34 */   INSTANCE_RESTART("instanceRestart", "重启实例", "instanceRestartActionHandler"),
/*     */   
/*  36 */   LOCK("lock", "锁定", "taskLockActionHandler"),
/*  37 */   UNLOCK("unlock", "解锁", "taskUnlockActionHandler"),
/*     */   
/*  39 */   TURN("turn", "转办", "taskTurnActionHandler"),
/*  40 */   REMINDER("reminder", "催办", "instanceReminderActionHandler"),
/*  41 */   RECALL("recall", "撤回", "instanceRecallActionHandler"),
/*     */   
/*  43 */   ADDSIGN("addSign", "加签", "addSignActionHandler"),
/*  44 */   ADDDO("addDo", "加办", "addDoActionHandler"),
/*  45 */   ADDDOAGREE("addDoAgree", "处理加办", "addDoAgreeActionHandler"),
/*     */   
/*  47 */   CREATE("create", "创建时", "null"),
/*  48 */   END("end", "流程结束", "null"),
/*  49 */   CLOSE("closeWindow", "返回", "null"),
/*  50 */   TASKCANCELLED("taskCancelled", "任务取消", "null"),
/*  51 */   CARBONCOPY("carbonCopy", "抄送", "carbonCopyActionHandler"),
/*  52 */   CARBONINSTCOPY("carbonInstCopy", "流程抄送", "carbonInstCopyActionHandler"),
/*  53 */   CARBONCOPYREVIEW("carbonCopyReview", "抄送审阅", "carbonCopyReviewActionHandler"),
/*  54 */   LEADERSAVE("leaderSave", "返回秘书", "taskLeaderSaveActionHandler"),
/*  55 */   SENDLEADER("sendLeader", "呈送领导", "taskSendLeaderActionHandle");
/*     */ 
/*     */ 
/*     */   
/*  59 */   private String key = "";
/*     */   
/*  61 */   private String name = "";
/*     */   
/*  63 */   private String beanId = "";
/*     */   private static final List<ActionType> actionTypes;
/*     */   
/*     */   ActionType(String key, String name, String beanId) {
/*  67 */     this.key = key;
/*  68 */     this.name = name;
/*  69 */     this.beanId = beanId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  74 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  78 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  82 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getBeanId() {
/*  86 */     return this.beanId;
/*     */   }
/*     */   
/*     */   public void setName(String value) {
/*  90 */     this.name = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     return this.key;
/*     */   }
/*     */   static {
/*  98 */     actionTypes = new ArrayList<>();
/*     */   }
/*     */   public static ActionType addEnum(String enumName, String key, String name, String beanId) {
/* 101 */     ActionType actionType = (ActionType)DynamicEnumUtil.addEnum(ActionType.class, enumName, new Class[] { String.class, String.class, String.class }, new Object[] { key, name, beanId });
/*     */     
/* 103 */     return actionType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ActionType fromKey(String key) {
/* 114 */     for (ActionType c : actionTypes) {
/* 115 */       if (c.getKey().equalsIgnoreCase(key))
/* 116 */         return c; 
/*     */     } 
/* 118 */     for (ActionType c : values()) {
/* 119 */       if (c.getKey().equalsIgnoreCase(key))
/* 120 */         return c; 
/*     */     } 
/* 122 */     throw new BusinessException(BpmStatusCode.NO_TASK_ACTION);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/ActionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */