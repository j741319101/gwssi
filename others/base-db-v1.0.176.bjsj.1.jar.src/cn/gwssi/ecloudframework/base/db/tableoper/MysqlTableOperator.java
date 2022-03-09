/*     */ package cn.gwssi.ecloudframework.base.db.tableoper;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.constant.ColumnType;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.DbType;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Column;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Table;
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
/*     */ public class MysqlTableOperator
/*     */   extends TableOperator
/*     */ {
/*     */   public MysqlTableOperator(Table<? extends Column> table, JdbcTemplate jdbcTemplate) {
/*  25 */     super(table, jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  30 */     return DbType.MYSQL.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public void createTable() {
/*  35 */     if (isTableCreated()) {
/*  36 */       this.logger.debug("表[" + this.table.getName() + "(" + this.table.getComment() + ")]已存在数据库中，无需再次生成");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  41 */     StringBuilder sql = new StringBuilder();
/*  42 */     sql.append("CREATE TABLE " + this.table.getName() + " (\n");
/*  43 */     for (Column column : this.table.getColumns()) {
/*  44 */       sql.append(columnToSql(column) + ",\n");
/*     */     }
/*  46 */     sql.append("PRIMARY KEY (" + this.table.getPkColumn().getName() + ")\n)");
/*     */     
/*  48 */     sql.append(" ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin ");
/*  49 */     if (StringUtil.isNotEmpty(this.table.getComment())) {
/*  50 */       sql.append(" COMMENT='" + this.table.getComment() + "'");
/*     */     }
/*     */     
/*  53 */     sql.append(";");
/*  54 */     this.jdbcTemplate.execute(sql.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTableCreated() {
/*  59 */     String sql = "select count(1) from information_schema.TABLES t where table_name =?"; return 
/*  60 */       (((Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { this.table.getName() })).intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addColumn(Column column) {
/*  65 */     StringBuilder sql = new StringBuilder();
/*  66 */     sql.append("ALTER TABLE " + this.table.getName() + "");
/*  67 */     sql.append(" ADD COLUMN " + columnToSql(column) + ";");
/*  68 */     this.jdbcTemplate.execute(sql.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateColumn(Column column) {
/*  73 */     StringBuilder sql = new StringBuilder();
/*  74 */     sql.append("ALTER TABLE " + this.table.getName() + "");
/*  75 */     sql.append(" MODIFY COLUMN " + columnToSql(column) + ";");
/*  76 */     this.jdbcTemplate.execute(sql.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropColumn(String columnName) {
/*  81 */     StringBuilder sql = new StringBuilder();
/*  82 */     sql.append("ALTER TABLE " + this.table.getName() + "");
/*  83 */     sql.append(" DROP COLUMN " + columnName + ";");
/*  84 */     this.jdbcTemplate.execute(sql.toString());
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
/*  96 */     StringBuilder sb = new StringBuilder();
/*  97 */     sb.append("" + column.getName() + "");
/*  98 */     if (ColumnType.CLOB.equalsWithKey(column.getType())) {
/*  99 */       sb.append(" text");
/* 100 */     } else if (ColumnType.DATE.equalsWithKey(column.getType())) {
/* 101 */       sb.append(" datetime");
/* 102 */     } else if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 103 */       sb.append(" decimal(" + column.getLength() + "," + column.getDecimal() + ")");
/* 104 */     } else if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 105 */       sb.append(" varchar(" + column.getLength() + ")");
/*     */     } 
/*     */     
/* 108 */     if (column.isRequired() || column.isPrimary()) {
/* 109 */       sb.append(" NOT NULL");
/*     */     } else {
/* 111 */       sb.append(" NULL");
/*     */     } 
/* 113 */     if (StringUtil.isNotEmpty(column.getDefaultValue()));
/*     */ 
/*     */     
/* 116 */     sb.append(" COMMENT '" + column.getComment() + "'");
/* 117 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/MysqlTableOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */