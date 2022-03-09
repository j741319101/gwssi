/*    */ package cn.gwssi.ecloudbpm.wf.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ScriptType
/*    */ {
/*  9 */   START("start", "开始脚本"),
/* 10 */   END("end", "结束脚本"),
/* 11 */   CREATE("create", "创建脚本"),
/* 12 */   COMPLETE("complete", "结束脚本"),
/* 13 */   MANUALEND("manualEnd", "人工终止");
/*    */ 
/*    */   
/* 16 */   private String key = "";
/*    */   
/* 18 */   private String value = "";
/*    */ 
/*    */   
/*    */   ScriptType(String key, String value) {
/* 22 */     this.key = key;
/* 23 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 28 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 32 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 36 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 40 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ScriptType fromKey(String key) {
/* 55 */     for (ScriptType c : values()) {
/* 56 */       if (c.getKey().equalsIgnoreCase(key))
/* 57 */         return c; 
/*    */     } 
/* 59 */     throw new IllegalArgumentException(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/ScriptType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */