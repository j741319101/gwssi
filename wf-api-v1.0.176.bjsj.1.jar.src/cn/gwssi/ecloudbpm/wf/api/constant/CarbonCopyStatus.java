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
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CarbonCopyStatus
/*    */ {
/* 16 */   UNREAD("unread", "未读"),
/*    */ 
/*    */ 
/*    */   
/* 20 */   READ("read", "已读"),
/*    */ 
/*    */ 
/*    */   
/* 24 */   REVIEWED("reviewed", "已审阅");
/*    */   
/*    */   private String key;
/*    */   
/*    */   private String name;
/*    */ 
/*    */   
/*    */   CarbonCopyStatus(String key, String name) {
/* 32 */     this.key = "";
/*    */     
/* 34 */     this.name = "";
/*    */     this.key = key;
/*    */     this.name = name; } public String getKey() {
/* 37 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 41 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/CarbonCopyStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */