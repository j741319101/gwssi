/*    */ package cn.gwssi.ecloudframework.base.db.datasource;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import java.sql.Connection;
/*    */ import java.sql.DatabaseMetaData;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
/*    */ import javax.sql.DataSource;
/*    */ import org.apache.ibatis.executor.BaseExecutor;
/*    */ import org.apache.ibatis.logging.Log;
/*    */ import org.apache.ibatis.logging.LogFactory;
/*    */ import org.apache.ibatis.mapping.DatabaseIdProvider;
/*    */ 
/*    */ public class EbDatabaseIdProvider
/*    */   implements DatabaseIdProvider {
/* 17 */   private static final Log log = LogFactory.getLog(BaseExecutor.class);
/*    */   
/*    */   private Properties properties;
/*    */ 
/*    */   
/*    */   public String getDatabaseId(DataSource dataSource) {
/* 23 */     if (dataSource == null) {
/* 24 */       throw new NullPointerException("dataSource cannot be null");
/*    */     }
/*    */     try {
/* 27 */       String dbType = DbContextHolder.getDbType();
/* 28 */       String produceName = this.properties.getProperty(dbType);
/* 29 */       if (StringUtil.isNotEmpty(produceName)) {
/* 30 */         return produceName;
/*    */       }
/* 32 */       return getDatabaseName(dataSource);
/* 33 */     } catch (Exception e) {
/* 34 */       log.error("Could not get a databaseId from dataSource", e);
/*    */       
/* 36 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setProperties(Properties p) {
/* 41 */     this.properties = p;
/*    */   }
/*    */   
/*    */   private String getDatabaseName(DataSource dataSource) throws SQLException {
/* 45 */     String productName = getDatabaseProductName(dataSource);
/* 46 */     if (this.properties != null) {
/* 47 */       for (Map.Entry<Object, Object> property : this.properties.entrySet()) {
/* 48 */         if (productName.contains((String)property.getKey())) {
/* 49 */           return (String)property.getValue();
/*    */         }
/*    */       } 
/* 52 */       return null;
/*    */     } 
/* 54 */     return productName;
/*    */   }
/*    */   
/*    */   private String getDatabaseProductName(DataSource dataSource) throws SQLException {
/* 58 */     Connection con = null;
/*    */     try {
/* 60 */       con = dataSource.getConnection();
/* 61 */       DatabaseMetaData metaData = con.getMetaData();
/* 62 */       return metaData.getDatabaseProductName();
/*    */     } finally {
/* 64 */       if (con != null)
/*    */         try {
/* 66 */           con.close();
/* 67 */         } catch (SQLException sQLException) {} 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/datasource/EbDatabaseIdProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */