/*    */ package com.dstz.bpm.api.constant;
/*    */ 
/*    */ public enum TaskSkipType {
/*  4 */   NO_SKIP("noSkip", "不跳过"),
/*  5 */   ALL_SKIP("allSkip", "所有节点跳过"),
/*  6 */   FIRSTNODE_SKIP("firstNodeSkip", "开始节点跳过"),
/*  7 */   SAME_USER_SKIP("sameUserSkip", "前一节点相同执行人跳过"),
/*  8 */   USER_EMPTY_SKIP("userEmptySkip", "执行人为空则跳过"),
/*  9 */   SCRIPT_SKIP("scriptSkip", "脚本跳过");
/*    */ 
/*    */   
/* 12 */   private String key = "";
/*    */   
/* 14 */   private String value = "";
/*    */ 
/*    */   
/*    */   TaskSkipType(String key, String value) {
/* 18 */     this.key = key;
/* 19 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 24 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 28 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 32 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 36 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TaskSkipType fromKey(String key) {
/* 51 */     for (TaskSkipType c : values()) {
/* 52 */       if (c.getKey().equalsIgnoreCase(key))
/* 53 */         return c; 
/*    */     } 
/* 55 */     throw new IllegalArgumentException(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/TaskSkipType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */