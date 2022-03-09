/*     */ package cn.gwssi.ecloudbpm.wf.api.constant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum EventType
/*     */ {
/*  11 */   START_EVENT("startEvent", "流程启动事件"),
/*     */ 
/*     */ 
/*     */   
/*  15 */   START_POST_EVENT("postStartEvent", "流程启动后置事件"),
/*     */ 
/*     */ 
/*     */   
/*  19 */   END_EVENT("endEvent", "流程结束事件"),
/*  20 */   MANUAL_END("manualEnd", "流程人工终止事件"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  25 */   RECOVER_EVENT("recoverEvent", "撤销事件"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   END_POST_EVENT("postEndEvent", "流程结束后置事件"),
/*     */   
/*  32 */   TASK_PRE_COMPLETE_EVENT("preTaskComplete", "任务完成前置事件"),
/*     */ 
/*     */ 
/*     */   
/*  36 */   PRE_SAVE_BUS_EVENT("preSaveBusEvent", "保存业务数据前"),
/*     */ 
/*     */ 
/*     */   
/*  40 */   TASK_CREATE_EVENT("taskCreate", "任务创建事件"),
/*     */ 
/*     */ 
/*     */   
/*  44 */   TASK_POST_CREATE_EVENT("postTaskCreate", "任务创建后置事件"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   TASK_COMPLETE_EVENT("taskComplete", "任务完成事件"),
/*     */ 
/*     */ 
/*     */   
/*  53 */   TASK_POST_COMPLETE_EVENT("postTaskComplete", "任务完成后置事件"),
/*     */ 
/*     */ 
/*     */   
/*  57 */   TASK_SIGN_CREATE_EVENT("taskSignCreate", "会签任务创建"),
/*     */ 
/*     */ 
/*     */   
/*  61 */   TASK_SIGN_POST_CREATE_EVENT("postTaskSignCreate", "会签任务创建后置事件"),
/*     */ 
/*     */ 
/*     */   
/*  65 */   DELETE_EVENT("deleteInstanceCreate", "流程删除事件");
/*     */ 
/*     */ 
/*     */   
/*  69 */   private String key = "";
/*     */   
/*  71 */   private String value = "";
/*     */ 
/*     */   
/*     */   EventType(String key, String value) {
/*  75 */     this.key = key;
/*  76 */     this.value = value;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  80 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  84 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  88 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/*  92 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  97 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EventType fromKey(String key) {
/* 107 */     for (EventType c : values()) {
/* 108 */       if (c.getKey().equalsIgnoreCase(key))
/* 109 */         return c; 
/*     */     } 
/* 111 */     throw new IllegalArgumentException(key);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/EventType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */