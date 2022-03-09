/*    */ package cn.gwssi.ecloudbpm.wf.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ExtractType
/*    */ {
/* 13 */   EXACT_NOEXACT("no", "不抽取"),
/*    */ 
/*    */ 
/*    */   
/* 17 */   EXACT_EXACT_USER("extract", "抽取用户");
/*    */ 
/*    */   
/* 20 */   private String key = "";
/*    */   
/* 22 */   private String value = "";
/*    */ 
/*    */   
/*    */   ExtractType(String key, String value) {
/* 26 */     this.key = key;
/* 27 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 32 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 36 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 40 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 44 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ExtractType fromKey(String key) {
/* 59 */     for (ExtractType c : values()) {
/* 60 */       if (c.getKey().equalsIgnoreCase(key))
/* 61 */         return c; 
/*    */     } 
/* 63 */     throw new IllegalArgumentException(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/ExtractType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */