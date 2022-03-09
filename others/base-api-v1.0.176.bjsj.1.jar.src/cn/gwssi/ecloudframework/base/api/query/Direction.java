/*    */ package cn.gwssi.ecloudframework.base.api.query;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Direction
/*    */ {
/* 10 */   ASC, DESC;
/*    */ 
/*    */   
/*    */   public static Direction fromString(String value) {
/*    */     try {
/* 15 */       return valueOf(value.toUpperCase());
/* 16 */     } catch (Exception e) {
/* 17 */       return ASC;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/query/Direction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */