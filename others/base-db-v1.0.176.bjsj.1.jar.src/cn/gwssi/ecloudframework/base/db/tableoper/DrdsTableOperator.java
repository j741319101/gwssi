/*     */ package com.dstz.base.db.tableoper;
/*     */ 
/*     */ import com.dstz.base.api.constant.ColumnType;
/*     */ import com.dstz.base.api.constant.DbType;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.table.Column;
/*     */ import com.dstz.base.db.model.table.Table;
/*     */ import java.util.List;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
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
/*     */ public class DrdsTableOperator
/*     */   extends TableOperator
/*     */ {
/*     */   public DrdsTableOperator(Table<? extends Column> table, JdbcTemplate jdbcTemplate) {
/*  27 */     super(table, jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  32 */     return DbType.DRDS.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public void createTable() {
/*  37 */     if (isTableCreated()) {
/*  38 */       this.logger.debug("表[" + this.table.getName() + "(" + this.table.getComment() + ")]已存在数据库中，无需再次生成");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  43 */     StringBuilder sql = new StringBuilder();
/*  44 */     sql.append("CREATE TABLE " + this.table.getName() + " (\n");
/*  45 */     for (Column column : this.table.getColumns()) {
/*  46 */       sql.append(columnToSql(column) + ",\n");
/*     */     }
/*  48 */     sql.append("PRIMARY KEY (" + this.table.getPkColumn().getName() + ")\n)");
/*     */     
/*  50 */     sql.append(" ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin ");
/*  51 */     if (StringUtil.isNotEmpty(this.table.getComment())) {
/*  52 */       sql.append(" COMMENT='" + this.table.getComment() + "'");
/*     */     }
/*  54 */     List<GenerateSqlByDbType> generateSqlByDbTypes = AppUtil.getImplInstanceArray(GenerateSqlByDbType.class);
/*  55 */     for (GenerateSqlByDbType generateSqlByDbType : generateSqlByDbTypes) {
/*  56 */       if (generateSqlByDbType.getCreateTableSql(sql, this.table)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  61 */     sql.append(";");
/*  62 */     this.jdbcTemplate.execute(sql.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTableCreated() {
/*  67 */     String sql = "select count(1) from information_schema.TABLES t where table_name =?"; return 
/*  68 */       (((Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { this.table.getName() })).intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addColumn(Column column) {
/*  73 */     StringBuilder sql = new StringBuilder();
/*  74 */     sql.append("ALTER TABLE " + this.table.getName() + "");
/*  75 */     sql.append(" ADD COLUMN " + columnToSql(column) + ";");
/*  76 */     this.jdbcTemplate.execute(sql.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateColumn(Column column) {
/*  81 */     StringBuilder sql = new StringBuilder();
/*  82 */     sql.append("ALTER TABLE " + this.table.getName() + "");
/*  83 */     sql.append(" MODIFY COLUMN " + columnToSql(column) + ";");
/*  84 */     this.jdbcTemplate.execute(sql.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropColumn(String columnName) {
/*  89 */     StringBuilder sql = new StringBuilder();
/*  90 */     sql.append("ALTER TABLE " + this.table.getName() + "");
/*  91 */     sql.append(" DROP COLUMN " + columnName + ";");
/*  92 */     this.jdbcTemplate.execute(sql.toString());
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
/*     */   private String columnToSql(Column column) {
/* 104 */     StringBuilder sb = new StringBuilder();
/* 105 */     sb.append("" + column.getName() + "");
/* 106 */     if (ColumnType.CLOB.equalsWithKey(column.getType())) {
/* 107 */       sb.append(" text");
/* 108 */     } else if (ColumnType.DATE.equalsWithKey(column.getType())) {
/* 109 */       sb.append(" datetime");
/* 110 */     } else if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 111 */       sb.append(" decimal(" + column.getLength() + "," + column.getDecimal() + ")");
/* 112 */     } else if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 113 */       sb.append(" varchar(" + column.getLength() + ")");
/*     */     } 
/*     */     
/* 116 */     if (column.isRequired() || column.isPrimary()) {
/* 117 */       sb.append(" NOT NULL");
/*     */     } else {
/* 119 */       sb.append(" NULL");
/*     */     } 
/* 121 */     if (StringUtil.isNotEmpty(column.getDefaultValue()));
/*     */ 
/*     */     
/* 124 */     sb.append(" COMMENT '" + column.getComment() + "'");
/* 125 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/DrdsTableOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */