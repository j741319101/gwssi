/*    */ package com.dstz.base.db.dboper;
/*    */ 
/*    */ import com.dstz.base.api.constant.DbType;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.base.db.datasource.DbContextHolder;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
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
/*    */ public class DbOperatorFactory
/*    */ {
/* 21 */   protected static final Logger LOG = LoggerFactory.getLogger(DbOperatorFactory.class);
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
/*    */ 
/*    */   
/*    */   public static DbOperator newOperator(String type, JdbcTemplate jdbcTemplate) {
/* 37 */     if (DbType.MYSQL.equalsWithKey(type)) {
/* 38 */       return new MysqlDbOperator(jdbcTemplate);
/*    */     }
/* 40 */     if (DbType.ORACLE.equalsWithKey(type)) {
/* 41 */       return new OracleDbOperator(jdbcTemplate);
/*    */     }
/* 43 */     if (DbType.DMSQL.equalsWithKey(type)) {
/* 44 */       return new DmsqlDbOperator(jdbcTemplate);
/*    */     }
/* 46 */     if (DbType.DRDS.equalsWithKey(type)) {
/* 47 */       return new DrdsDbOperator(jdbcTemplate);
/*    */     }
/* 49 */     if (DbType.KINGBASE.equalsWithKey(type)) {
/* 50 */       return new KingBaseDbOperator(jdbcTemplate);
/*    */     }
/* 52 */     if (DbType.HIGHGO.equalsWithKey(type)) {
/* 53 */       return new HighGoDbOperator(jdbcTemplate);
/*    */     }
/* 55 */     LOG.warn("cannot get DbOperator ! DbType:{}", type);
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DbOperator getLocal() {
/* 67 */     return newOperator(DbContextHolder.getDbType(), (JdbcTemplate)AppUtil.getBean(JdbcTemplate.class));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/dboper/DbOperatorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */