/*    */ package cn.gwssi.ecloudframework.base.core.id;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IdUtil
/*    */ {
/*    */   private static IdGenerator idGenerator;
/*    */   
/*    */   public void setIdGenerator(IdGenerator idGenerator_) {
/* 12 */     idGenerator = idGenerator_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Long getUId() {
/* 23 */     return idGenerator.getUId();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getSuid() {
/* 32 */     return idGenerator.getSuid();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/id/IdUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */