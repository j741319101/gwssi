/*     */ package cn.gwssi.ecloudbpm.wf.api.constant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum InstanceStatus
/*     */ {
/*  10 */   STATUS_DRAFT("draft", "草稿"),
/*     */ 
/*     */ 
/*     */   
/*  14 */   STATUS_RUNNING("running", "运行中"),
/*     */ 
/*     */ 
/*     */   
/*  18 */   STATUS_END("end", "结束"),
/*     */ 
/*     */ 
/*     */   
/*  22 */   STATUS_MANUAL_END("manualend", "人工结束"),
/*     */   
/*  24 */   STATUS_BACK("back", "驳回"),
/*     */   
/*  26 */   STATUS_UNDEFINED("undefined", "未定义"),
/*     */   
/*  28 */   STATUS_CANCELLED("cancelled", "进入边界"),
/*     */   
/*  30 */   STATUS_REVOKE("revoker", "撤销"),
/*     */   
/*  32 */   STATUS_DISCARD("discard", "废件"),
/*     */   
/*  34 */   STATUS_RECOVER("recover", "追回");
/*     */ 
/*     */ 
/*     */   
/*  38 */   private String key = "";
/*     */   
/*  40 */   private String value = "";
/*     */ 
/*     */   
/*     */   InstanceStatus(String key, String value) {
/*  44 */     this.key = key;
/*  45 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  50 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  54 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  58 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/*  62 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  67 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InstanceStatus fromKey(String key) {
/*  77 */     for (InstanceStatus c : values()) {
/*  78 */       if (c.getKey().equalsIgnoreCase(key))
/*  79 */         return c; 
/*     */     } 
/*  81 */     throw new IllegalArgumentException(key);
/*     */   }
/*     */   
/*     */   public static InstanceStatus getByActionName(String actionName) {
/*  85 */     ActionType action = ActionType.fromKey(actionName);
/*     */     
/*  87 */     switch (action) {
/*     */       case AGREE:
/*  89 */         return STATUS_RUNNING;
/*     */       case OPPOSE:
/*  91 */         return STATUS_RUNNING;
/*     */       case REJECT:
/*  93 */         return STATUS_BACK;
/*     */       case REJECT2START:
/*  95 */         return STATUS_BACK;
/*     */       case RECOVER:
/*  97 */         return STATUS_REVOKE;
/*     */       case TASKOPINION:
/*  99 */         return STATUS_RUNNING;
/*     */       case MANUALEND:
/* 101 */         return STATUS_MANUAL_END;
/*     */       case TASKCANCELLED:
/* 103 */         return STATUS_CANCELLED;
/*     */       case INCREASEDYNAMIC:
/* 105 */         return STATUS_RECOVER;
/*     */       case DECREASEDYNAMIC:
/* 107 */         return STATUS_RECOVER;
/*     */     } 
/* 109 */     return STATUS_RUNNING;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/InstanceStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */