/*    */ package cn.gwssi.ecloudframework.base.db.datasource;
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
/*    */ public class DataSourceException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 3148019938789322656L;
/*    */   
/*    */   public DataSourceException(String msg) {
/* 18 */     super(msg);
/*    */   }
/*    */   
/*    */   public DataSourceException(String msg, Throwable throwable) {
/* 22 */     super(msg, throwable);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/datasource/DataSourceException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */