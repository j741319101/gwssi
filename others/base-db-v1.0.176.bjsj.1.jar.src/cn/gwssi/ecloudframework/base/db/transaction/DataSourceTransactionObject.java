/*    */ package com.dstz.base.db.transaction;
/*    */ 
/*    */ import org.springframework.jdbc.datasource.ConnectionHolder;
/*    */ import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
/*    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*    */ import org.springframework.transaction.support.TransactionSynchronizationUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataSourceTransactionObject
/*    */   extends JdbcTransactionObjectSupport
/*    */ {
/*    */   private boolean newConnectionHolder;
/*    */   private boolean mustRestoreAutoCommit;
/*    */   
/*    */   public void setConnectionHolder(ConnectionHolder connectionHolder, boolean newConnectionHolder) {
/* 21 */     setConnectionHolder(connectionHolder);
/* 22 */     this.newConnectionHolder = newConnectionHolder;
/*    */   }
/*    */   
/*    */   public boolean isNewConnectionHolder() {
/* 26 */     return this.newConnectionHolder;
/*    */   }
/*    */   
/*    */   public void setMustRestoreAutoCommit(boolean mustRestoreAutoCommit) {
/* 30 */     this.mustRestoreAutoCommit = mustRestoreAutoCommit;
/*    */   }
/*    */   
/*    */   public boolean isMustRestoreAutoCommit() {
/* 34 */     return this.mustRestoreAutoCommit;
/*    */   }
/*    */   
/*    */   public void setRollbackOnly() {
/* 38 */     getConnectionHolder().setRollbackOnly();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRollbackOnly() {
/* 43 */     return getConnectionHolder().isRollbackOnly();
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() {
/* 48 */     if (TransactionSynchronizationManager.isSynchronizationActive())
/* 49 */       TransactionSynchronizationUtils.triggerFlush(); 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/transaction/DataSourceTransactionObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */