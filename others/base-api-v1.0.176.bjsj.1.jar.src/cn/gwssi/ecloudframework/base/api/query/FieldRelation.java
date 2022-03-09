/*    */ package cn.gwssi.ecloudframework.base.api.query;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum FieldRelation
/*    */ {
/*  9 */   AND("AND"), OR("OR"), NOT("NOT");
/*    */   private String val;
/*    */   
/*    */   FieldRelation(String _val) {
/* 13 */     this.val = _val;
/*    */   }
/*    */   
/*    */   public String value() {
/* 17 */     return this.val;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/query/FieldRelation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */