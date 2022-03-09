/*    */ package cn.gwssi.ecloudframework.org.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum UserTypeConstant
/*    */ {
/* 13 */   NORMAL("1", "普通用户"),
/* 14 */   MANAGER("2", "管理员"),
/* 15 */   EXTERNAL("3", "外部人员");
/*    */   private String key;
/*    */   private String label;
/*    */   
/*    */   UserTypeConstant(String key, String label) {
/* 20 */     this.key = key;
/* 21 */     this.label = label;
/*    */   }
/*    */   
/*    */   public String key() {
/* 25 */     return this.key;
/*    */   }
/*    */   
/*    */   public String label() {
/* 29 */     return this.label;
/*    */   }
/*    */   
/*    */   public static UserTypeConstant fromStr(String key) {
/* 33 */     for (UserTypeConstant e : values()) {
/* 34 */       if (key.equals(e.key)) {
/* 35 */         return e;
/*    */       }
/*    */     } 
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/constant/UserTypeConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */