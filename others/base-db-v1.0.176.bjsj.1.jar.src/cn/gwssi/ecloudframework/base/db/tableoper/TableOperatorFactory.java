/*    */ package cn.gwssi.ecloudframework.base.db.tableoper;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.constant.DbType;
/*    */ import cn.gwssi.ecloudframework.base.db.model.table.Table;
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
/*    */ public class TableOperatorFactory
/*    */ {
/*    */   public static TableOperator newOperator(String type, Table<?> table, JdbcTemplate jdbcTemplate) {
/* 29 */     if (DbType.MYSQL.equalsWithKey(type)) {
/* 30 */       return new MysqlTableOperator((Table)table, jdbcTemplate);
/*    */     }
/* 32 */     if (DbType.ORACLE.equalsWithKey(type)) {
/* 33 */       return new OracleTableOperator((Table)table, jdbcTemplate);
/*    */     }
/* 35 */     if (DbType.DMSQL.equalsWithKey(type)) {
/* 36 */       return new DmsqlTableOperator((Table)table, jdbcTemplate);
/*    */     }
/* 38 */     if (DbType.DRDS.equalsWithKey(type)) {
/* 39 */       return new DrdsTableOperator((Table)table, jdbcTemplate);
/*    */     }
/* 41 */     if (DbType.KINGBASE.equalsWithKey(type)) {
/* 42 */       return new KingBaseTableOperator((Table)table, jdbcTemplate);
/*    */     }
/* 44 */     if (DbType.HIGHGO.equalsWithKey(type)) {
/* 45 */       return new HighGoTableOperator((Table)table, jdbcTemplate);
/*    */     }
/* 47 */     throw new RuntimeException("找不到类型[" + type + "]的数据库处理者(TableOperator)");
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/TableOperatorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */