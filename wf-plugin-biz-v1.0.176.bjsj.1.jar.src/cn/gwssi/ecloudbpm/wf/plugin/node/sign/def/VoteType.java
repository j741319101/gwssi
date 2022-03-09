/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.sign.def;
/*    */ 
/*    */ public enum VoteType {
/*  4 */   AMOUNT("amount", "投票数"),
/*  5 */   PERCENT("percent", "百分比");
/*    */   
/*  7 */   private String key = "";
/*  8 */   private String value = "";
/*    */   
/*    */   VoteType(String key, String value) {
/* 11 */     this.key = key;
/* 12 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 17 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 21 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 25 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 29 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 34 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static VoteType fromKey(String key) {
/* 43 */     for (VoteType c : values()) {
/* 44 */       if (c.getKey().equalsIgnoreCase(key))
/* 45 */         return c; 
/*    */     } 
/* 47 */     throw new IllegalArgumentException(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/sign/def/VoteType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */