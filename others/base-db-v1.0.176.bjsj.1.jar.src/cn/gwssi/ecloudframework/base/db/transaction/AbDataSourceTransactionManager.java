/*     */ package com.dstz.base.db.transaction;
/*     */ 
/*     */ import com.dstz.base.db.datasource.DataSourceUtil;
/*     */ import com.dstz.base.db.datasource.DbContextHolder;
/*     */ import com.dstz.base.db.datasource.DynamicDataSource;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Map;
/*     */ import javax.sql.DataSource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.jdbc.datasource.ConnectionHolder;
/*     */ import org.springframework.jdbc.datasource.DataSourceUtils;
/*     */ import org.springframework.transaction.CannotCreateTransactionException;
/*     */ import org.springframework.transaction.TransactionDefinition;
/*     */ import org.springframework.transaction.TransactionException;
/*     */ import org.springframework.transaction.TransactionSystemException;
/*     */ import org.springframework.transaction.support.AbstractPlatformTransactionManager;
/*     */ import org.springframework.transaction.support.DefaultTransactionStatus;
/*     */ import org.springframework.transaction.support.ResourceTransactionManager;
/*     */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AbDataSourceTransactionManager
/*     */   extends AbstractPlatformTransactionManager
/*     */   implements ResourceTransactionManager, InitializingBean
/*     */ {
/*  39 */   private static Logger log = LoggerFactory.getLogger(AbDataSourceTransactionManager.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private static ThreadLocal<AbDataSourceTransactionObject> threadLocalTopAbTxObject = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static ThreadLocal<Boolean> transactionActive = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private boolean enforceReadOnly = false;
/*     */ 
/*     */   
/*     */   private static DynamicDataSource dynamicDataSource;
/*     */ 
/*     */   
/*     */   private static AbDataSourceTransactionManager abDataSourceTransactionManager;
/*     */ 
/*     */ 
/*     */   
/*     */   public AbDataSourceTransactionManager() {
/*  64 */     setNestedTransactionAllowed(true);
/*     */   }
/*     */   
/*     */   public void setEnforceReadOnly(boolean enforceReadOnly) {
/*  68 */     this.enforceReadOnly = enforceReadOnly;
/*     */   }
/*     */   
/*     */   public boolean isEnforceReadOnly() {
/*  72 */     return this.enforceReadOnly;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/*  77 */     log.debug("ab的事务管理器已就绪");
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getResourceFactory() {
/*  82 */     return dynamicDataSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object doGetTransaction() {
/*  92 */     AbDataSourceTransactionObject abTxObject = new AbDataSourceTransactionObject();
/*     */     
/*  94 */     if (threadLocalTopAbTxObject.get() == null) {
/*  95 */       threadLocalTopAbTxObject.set(abTxObject);
/*  96 */       if (log.isDebugEnabled()) {
/*  97 */         log.debug("ab事务编号[" + abTxObject.getSerialNumber() + "]为顶层事务开始，调用AbDataSourceTransactionManager.addDataSource的数据源全部归于这里处理");
/*     */       }
/*     */     }
/* 100 */     else if (log.isDebugEnabled()) {
/* 101 */       log.debug("进入ab事务编号[" + abTxObject.getSerialNumber() + "]");
/*     */     } 
/*     */     
/* 104 */     return abTxObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isExistingTransaction(Object transaction) {
/* 112 */     if (transactionActive.get() == null) {
/* 113 */       return false;
/*     */     }
/* 115 */     return ((Boolean)transactionActive.get()).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
/* 125 */     AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject)transaction;
/* 126 */     if (log.isDebugEnabled()) {
/* 127 */       log.debug("ab事务编号[" + abTxObject.getSerialNumber() + "]开始");
/*     */     }
/* 129 */     abTxObject.setDefinition(definition);
/*     */     
/* 131 */     addGlobalDataSource("dataSource", (DataSource)dynamicDataSource, abTxObject);
/* 132 */     transactionActive.set(Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addGlobalDataSource(String dsKey, DataSource dataSource, AbDataSourceTransactionObject abTxObject) {
/*     */     try {
/* 146 */       if (dataSource == null) {
/* 147 */         System.out.println();
/*     */       }
/*     */ 
/*     */       
/* 151 */       DataSourceTransactionObject txObject = abTxObject.getDsTxObj(dsKey);
/* 152 */       if (txObject == null) {
/* 153 */         txObject = new DataSourceTransactionObject();
/* 154 */         abTxObject.putDsTxObj(dsKey, txObject);
/*     */       } 
/*     */ 
/*     */       
/* 158 */       if (!txObject.hasConnectionHolder() || txObject.getConnectionHolder().isSynchronizedWithTransaction()) {
/*     */         
/* 160 */         ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
/* 161 */         if (conHolder != null) {
/*     */           
/* 163 */           txObject.setConnectionHolder(conHolder, false);
/*     */         } else {
/* 165 */           Connection newCon = dataSource.getConnection();
/* 166 */           if (log.isDebugEnabled()) {
/* 167 */             log.debug("在ab事务编号[" + abTxObject.getSerialNumber() + "]中，数据源别名[" + dsKey + "]打开连接成功");
/*     */           }
/*     */           
/* 170 */           txObject.setConnectionHolder(new ConnectionHolder(newCon), true);
/*     */         } 
/*     */       } 
/*     */       
/* 174 */       txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
/* 175 */       Connection con = txObject.getConnectionHolder().getConnection();
/*     */       
/* 177 */       TransactionDefinition definition = abTxObject.getDefinition();
/* 178 */       Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
/* 179 */       txObject.setPreviousIsolationLevel(previousIsolationLevel);
/*     */ 
/*     */       
/* 182 */       if (con.getAutoCommit()) {
/* 183 */         txObject.setMustRestoreAutoCommit(true);
/* 184 */         if (log.isDebugEnabled()) {
/* 185 */           log.debug("在ab事务编号[" + abTxObject.getSerialNumber() + "]中，设置数据源别名为[" + dsKey + "]的链接为手动提交");
/*     */         }
/* 187 */         con.setAutoCommit(false);
/*     */       } 
/*     */       
/* 190 */       prepareTransactionalConnection(con, definition);
/*     */ 
/*     */       
/* 193 */       int timeout = staticDetermineTimeout(definition);
/* 194 */       if (timeout != -1) {
/* 195 */         txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
/*     */       }
/*     */ 
/*     */       
/* 199 */       if (txObject.isNewConnectionHolder()) {
/* 200 */         TransactionSynchronizationManager.bindResource(dataSource, txObject.getConnectionHolder());
/*     */       }
/* 202 */     } catch (Throwable ex) {
/* 203 */       ex.printStackTrace();
/* 204 */       DataSourceTransactionObject txObject = abTxObject.getDsTxObj(dsKey);
/*     */       
/* 206 */       if (txObject != null && txObject.isNewConnectionHolder()) {
/* 207 */         DataSource ds = DataSourceUtil.getDataSourceByAlias(dsKey);
/* 208 */         Connection con = txObject.getConnectionHolder().getConnection();
/* 209 */         DataSourceUtils.releaseConnection(con, ds);
/* 210 */         txObject.setConnectionHolder((ConnectionHolder)null, false);
/*     */       } 
/* 212 */       throw new CannotCreateTransactionException("在ab事务编号[" + abTxObject.getSerialNumber() + "]中，数据源别名[" + dsKey + "]打开连接错误", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addDataSource(String dsKey, DataSource dataSource) {
/* 227 */     if (dataSource == dynamicDataSource) {
/*     */       return;
/*     */     }
/*     */     
/* 231 */     if (DbContextHolder.getDataSource().equals(dsKey) && TransactionSynchronizationManager.getResource(dataSource) == null) {
/* 232 */       ConnectionHolder holder = ((AbDataSourceTransactionObject)threadLocalTopAbTxObject.get()).getDsTxObj("dataSource").getConnectionHolder();
/* 233 */       TransactionSynchronizationManager.bindResource(dataSource, holder);
/*     */       
/*     */       return;
/*     */     } 
/* 237 */     if (((AbDataSourceTransactionObject)threadLocalTopAbTxObject.get()).getDsTxObjMap().containsKey(dsKey)) {
/*     */       return;
/*     */     }
/*     */     
/* 241 */     addGlobalDataSource(dsKey, dataSource, threadLocalTopAbTxObject.get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object doSuspend(Object transaction) {
/* 255 */     return TransactionSynchronizationManager.unbindResource(dynamicDataSource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doResume(Object transaction, Object suspendedResources) {
/* 265 */     TransactionSynchronizationManager.bindResource(dynamicDataSource, suspendedResources);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCommit(DefaultTransactionStatus status) {
/* 270 */     AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject)status.getTransaction();
/* 271 */     StringBuilder sb = new StringBuilder();
/* 272 */     for (Map.Entry<String, DataSourceTransactionObject> entry : abTxObject.getDsTxObjMap().entrySet()) {
/*     */       try {
/* 274 */         ((DataSourceTransactionObject)entry.getValue()).getConnectionHolder().getConnection().commit();
/* 275 */         if (sb.length() > 0) {
/* 276 */           sb.append(",");
/*     */         }
/* 278 */         sb.append(entry.getKey());
/* 279 */         if (log.isDebugEnabled()) {
/* 280 */           log.debug("在ab事务编号[" + abTxObject.getSerialNumber() + "]中，数据源别名[" + (String)entry.getKey() + "]提交事务成功");
/*     */         }
/* 282 */       } catch (SQLException ex) {
/* 283 */         throw new TransactionSystemException("数据源别名[" + (String)entry.getKey() + "]提交事务失败，需要干预已提交成功的数据源别名[" + sb + "]的数据", ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
/* 293 */     AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject)status.getTransaction();
/* 294 */     for (Map.Entry<String, DataSourceTransactionObject> entry : abTxObject.getDsTxObjMap().entrySet()) {
/*     */       try {
/* 296 */         ((DataSourceTransactionObject)entry.getValue()).getConnectionHolder().getConnection().rollback();
/* 297 */         if (log.isDebugEnabled()) {
/* 298 */           log.debug("在ab事务编号[" + abTxObject.getSerialNumber() + "]中，数据源别名[" + (String)entry.getKey() + "]回滚事务成功");
/*     */         }
/* 300 */       } catch (SQLException ex) {
/* 301 */         throw new TransactionSystemException("数据源别名[" + (String)entry.getKey() + "]回滚事务失败", ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doSetRollbackOnly(DefaultTransactionStatus status) {
/* 313 */     AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject)status.getTransaction();
/* 314 */     for (Map.Entry<String, DataSourceTransactionObject> entry : abTxObject.getDsTxObjMap().entrySet()) {
/* 315 */       if (status.isDebug()) {
/* 316 */         log.debug("在ab事务编号[" + abTxObject.getSerialNumber() + "]中，修改数据别名为 [" + (String)entry.getKey() + "]的链接资源为 rollback-only");
/*     */       }
/* 318 */       ((DataSourceTransactionObject)entry.getValue()).setRollbackOnly();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doCleanupAfterCompletion(Object transaction) {
/* 327 */     AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject)transaction;
/* 328 */     for (Map.Entry<String, DataSourceTransactionObject> entry : abTxObject.getDsTxObjMap().entrySet()) {
/* 329 */       DataSourceTransactionObject txObject = entry.getValue();
/* 330 */       DataSource dataSource1 = DataSourceUtil.getDataSourceByAliasWithLoacl(entry.getKey());
/*     */       
/* 332 */       if (txObject.isNewConnectionHolder()) {
/* 333 */         TransactionSynchronizationManager.unbindResource(dataSource1);
/*     */       }
/*     */ 
/*     */       
/* 337 */       Connection con = txObject.getConnectionHolder().getConnection();
/*     */       try {
/* 339 */         if (txObject.isMustRestoreAutoCommit()) {
/* 340 */           con.setAutoCommit(true);
/*     */         }
/* 342 */         DataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel());
/* 343 */       } catch (Throwable ex) {
/* 344 */         log.debug("在ab事务编号[" + abTxObject.getSerialNumber() + "]中，在完成事务后，数据源别名为[" + (String)entry.getKey() + "]的属性无法被还原", ex);
/*     */       } 
/*     */       
/* 347 */       if (txObject.isNewConnectionHolder()) {
/* 348 */         if (log.isDebugEnabled()) {
/* 349 */           log.debug("在ab事务编号[" + abTxObject.getSerialNumber() + "]中，在完成事务后，释放数据源别名为[" + (String)entry.getKey() + "]的jdbc的链接");
/*     */         }
/* 351 */         DataSourceUtils.releaseConnection(con, dataSource1);
/*     */       } 
/*     */       
/* 354 */       txObject.getConnectionHolder().clear();
/*     */     } 
/*     */     
/* 357 */     DataSource dataSource = DataSourceUtil.getDataSourceByAlias(DbContextHolder.getDataSource());
/* 358 */     ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
/* 359 */     if (conHolder != null) {
/* 360 */       TransactionSynchronizationManager.unbindResource(dataSource);
/*     */     }
/*     */ 
/*     */     
/* 364 */     if (abTxObject == threadLocalTopAbTxObject.get()) {
/* 365 */       threadLocalTopAbTxObject.remove();
/* 366 */       transactionActive.remove();
/* 367 */       if (log.isDebugEnabled()) {
/* 368 */         log.debug("ab事务顶层事务编号[" + abTxObject.getSerialNumber() + "]结束");
/*     */       }
/*     */     }
/* 371 */     else if (log.isDebugEnabled()) {
/* 372 */       log.debug("ab事务编号[" + abTxObject.getSerialNumber() + "]结束");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void prepareTransactionalConnection(Connection con, TransactionDefinition definition) throws SQLException {
/* 388 */     boolean isEnforceReadOnly = abDataSourceTransactionManager.isEnforceReadOnly();
/* 389 */     if (isEnforceReadOnly && definition.isReadOnly()) {
/* 390 */       Statement stmt = con.createStatement();
/*     */       try {
/* 392 */         stmt.executeUpdate("SET TRANSACTION READ ONLY");
/*     */       } finally {
/* 394 */         stmt.close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int staticDetermineTimeout(TransactionDefinition definition) {
/* 408 */     if (definition.getTimeout() != -1) {
/* 409 */       return definition.getTimeout();
/*     */     }
/* 411 */     return abDataSourceTransactionManager.getDefaultTimeout();
/*     */   }
/*     */   
/*     */   @Autowired
/*     */   public void setDynamicDataSource(DynamicDataSource dynamicDataSource) {
/* 416 */     AbDataSourceTransactionManager.dynamicDataSource = dynamicDataSource;
/* 417 */     abDataSourceTransactionManager = this;
/* 418 */     DataSourceUtil.dynamicDataSource = dynamicDataSource;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/transaction/AbDataSourceTransactionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */