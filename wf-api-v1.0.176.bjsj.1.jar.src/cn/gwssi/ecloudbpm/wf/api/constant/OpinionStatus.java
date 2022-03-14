/*     */ package com.dstz.bpm.api.constant;
/*     */ 
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum OpinionStatus
/*     */ {
/*  15 */   START("start", "提交"),
/*     */ 
/*     */ 
/*     */   
/*  19 */   CREATE("create", "创建"),
/*     */ 
/*     */ 
/*     */   
/*  23 */   END("end", "结束"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   AWAITING_CHECK("awaiting_check", "待审批"),
/*     */ 
/*     */ 
/*     */   
/*  32 */   AGREE("agree", "同意"),
/*     */ 
/*     */ 
/*     */   
/*  36 */   OPPOSE("oppose", "反对"),
/*     */ 
/*     */ 
/*     */   
/*  40 */   ABANDON("abandon", "弃权"),
/*     */ 
/*     */ 
/*     */   
/*  44 */   REJECT("reject", "驳回"),
/*     */ 
/*     */ 
/*     */   
/*  48 */   REJECT_TO_START("rejectToStart", "驳回到发起人"),
/*     */ 
/*     */ 
/*     */   
/*  52 */   REVOKER("revoker", "撤销"),
/*     */ 
/*     */ 
/*     */   
/*  56 */   REVOKER_TO_START("revoker_to_start", "撤回到发起人"),
/*     */ 
/*     */ 
/*     */   
/*  60 */   SIGN_PASSED("signPass", "会签通过"),
/*     */ 
/*     */ 
/*     */   
/*  64 */   SIGN_NOT_PASSED("signNotPass", "会签不通过"),
/*     */ 
/*     */ 
/*     */   
/*  68 */   SIGN_RECYCLE("signRecycle", "会签回收"),
/*     */ 
/*     */ 
/*     */   
/*  72 */   SKIP("skip", "跳过执行"),
/*     */   
/*  74 */   TURN("turn", "转办"),
/*     */ 
/*     */ 
/*     */   
/*  78 */   CANCELLED("cancelled", "任务取消"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   MANUAL_END("manualEnd", "人工终止"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   ADD_DO("addDo", "加办"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   ADD_DO_AGREE("addDoAgree", "处理加办"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   CARBON_COPY("carbonCopy", "抄送审阅"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   RECALL("recall", "撤回"),
/*     */   
/* 105 */   INCREASEDYNAMIC("increaseDynamic", "补签多实例"),
/*     */   
/* 107 */   DECREASEDYNAMIC("decreaseDynamic", "减签多实例"),
/*     */   
/* 109 */   TASK_FREE_JUMP("freeJump", "任务跳转");
/*     */   
/* 111 */   private String key = "";
/*     */   
/* 113 */   private String value = "";
/*     */   protected static Logger LOG;
/*     */   
/*     */   OpinionStatus(String key, String value) {
/* 117 */     this.key = key;
/* 118 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKey() {
/* 123 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/* 127 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 131 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/* 135 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 140 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 149 */     LOG = LoggerFactory.getLogger(OpinionStatus.class);
/*     */   }
/*     */   public static OpinionStatus fromKey(String key) {
/* 152 */     for (OpinionStatus c : values()) {
/* 153 */       if (c.getKey().equalsIgnoreCase(key))
/* 154 */         return c; 
/*     */     } 
/* 156 */     LOG.warn("OpinionStatus 转换失败！ 无法查找到对应的 值：{}", key);
/* 157 */     return null;
/*     */   }
/*     */   
/*     */   public static OpinionStatus getByActionName(String actionName) {
/* 161 */     ActionType action = ActionType.fromKey(actionName);
/*     */     
/* 163 */     switch (action) {
/*     */       case AGREE:
/* 165 */         return AGREE;
/*     */       case SIGNAGREE:
/* 167 */         return SIGN_PASSED;
/*     */       case OPPOSE:
/* 169 */         return OPPOSE;
/*     */       case SIGNOPPOSE:
/* 171 */         return SIGN_NOT_PASSED;
/*     */       case REJECT:
/* 173 */         return REJECT;
/*     */       case REJECT2START:
/* 175 */         return REJECT_TO_START;
/*     */       case RECOVER:
/* 177 */         return REVOKER;
/*     */       case START:
/* 179 */         return START;
/*     */       case MANUALEND:
/* 181 */         return MANUAL_END;
/*     */       case TASKCANCELLED:
/* 183 */         return CANCELLED;
/*     */       case RECALL:
/* 185 */         return RECALL;
/*     */       case DECREASEDYNAMIC:
/* 187 */         return DECREASEDYNAMIC;
/*     */       case INCREASEDYNAMIC:
/* 189 */         return AWAITING_CHECK;
/*     */       case TASK_FREE_JUMP:
/* 191 */         return TASK_FREE_JUMP;
/*     */     } 
/* 193 */     return AWAITING_CHECK;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/OpinionStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */