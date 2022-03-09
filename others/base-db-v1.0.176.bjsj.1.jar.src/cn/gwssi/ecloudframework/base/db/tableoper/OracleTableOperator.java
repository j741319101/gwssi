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
/*     */ public class OracleTableOperator
/*     */   extends TableOperator
/*     */ {
/*     */   public OracleTableOperator(Table<? extends Column> table, JdbcTemplate jdbcTemplate) {
/*  25 */     super(table, jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  30 */     return DbType.ORACLE.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public void createTable() {
/*  35 */     if (isTableCreated()) {
/*  36 */       this.logger.debug("表[" + this.table.getName().toUpperCase() + "(" + this.table.getComment() + ")]已存在数据库中，无需再次生成");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  41 */     StringBuilder sql = new StringBuilder();
/*  42 */     sql.append("CREATE TABLE \"" + this.table.getName().toUpperCase() + "\" (\n");
/*  43 */     for (Column column : this.table.getColumns()) {
/*  44 */       sql.append(columnToSql(column) + ",\n");
/*     */     }
/*  46 */     sql.append("PRIMARY KEY (\"" + this.table.getPkColumn().getName().toUpperCase() + "\")\n)");
/*     */     
/*  48 */     this.jdbcTemplate.execute(sql.toString());
/*     */ 
/*     */     
/*  51 */     if (StringUtil.isNotEmpty(this.table.getComment())) {
/*  52 */       String str = "COMMENT ON TABLE \"" + this.table.getName().toUpperCase() + "\" IS '" + this.table.getComment() + "'";
/*  53 */       this.jdbcTemplate.execute(str);
/*     */     } 
/*     */ 
/*     */     
/*  57 */     for (int i = 0; i < this.table.getColumns().size(); i++) {
/*  58 */       Column column = this.table.getColumns().get(i);
/*  59 */       if (!StringUtil.isEmpty(column.getComment())) {
/*     */ 
/*     */         
/*  62 */         String str = "COMMENT ON COLUMN \"" + this.table.getName().toUpperCase() + "\".\"" + column.getName().toUpperCase() + "\"  IS '" + column.getComment() + "'";
/*  63 */         this.jdbcTemplate.execute(str);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isTableCreated() {
/*  69 */     String sql = "select count(1) from user_tables t where table_name =?"; return 
/*  70 */       (((Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { this.table.getName().toUpperCase() })).intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addColumn(Column column) {
/*  75 */     StringBuilder sql = new StringBuilder();
/*  76 */     sql.append("ALTER TABLE \"" + this.table.getName().toUpperCase() + "\"");
/*  77 */     sql.append(" ADD ( " + columnToSql(column) + " )");
/*  78 */     this.jdbcTemplate.execute(sql.toString());
/*     */ 
/*     */     
/*  81 */     if (StringUtil.isEmpty(column.getComment())) {
/*     */       return;
/*     */     }
/*  84 */     String str = "COMMENT ON COLUMN \"" + this.table.getName().toUpperCase() + "\".\"" + column.getName().toUpperCase() + "\"  IS '" + column.getComment() + "'";
/*  85 */     this.jdbcTemplate.execute(str);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateColumn(Column column) {
/*  90 */     StringBuilder sql = new StringBuilder();
/*  91 */     sql.append("ALTER TABLE \"" + this.table.getName().toUpperCase() + "\"");
/*  92 */     sql.append(" MODIFY( " + columnToSql(column) + " )");
/*  93 */     this.jdbcTemplate.execute(sql.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropColumn(String columnName) {
/*  98 */     StringBuilder sql = new StringBuilder();
/*  99 */     sql.append("ALTER TABLE \"" + this.table.getName().toUpperCase() + "\"");
/* 100 */     sql.append(" DROP(\"" + columnName + "\")");
/* 101 */     this.jdbcTemplate.execute(sql.toString());
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
/* 113 */     StringBuilder sb = new StringBuilder();
/* 114 */     sb.append("\"" + column.getName().toUpperCase() + "\"");
/* 115 */     if (ColumnType.CLOB.equalsWithKey(column.getType())) {
/* 116 */       sb.append(" CLOB");
/* 117 */     } else if (ColumnType.DATE.equalsWithKey(column.getType())) {
/* 118 */       sb.append(" TIMESTAMP");
/* 119 */     } else if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 120 */       sb.append(" NUMBER(" + column.getLength() + "," + column.getDecimal() + ")");
/* 121 */     } else if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 122 */       sb.append(" VARCHAR2(" + column.getLength() + ")");
/*     */     } 
/*     */     
/* 125 */     if (column.isRequired() || column.isPrimary()) {
/* 126 */       sb.append(" NOT NULL");
/*     */     } else {
/* 128 */       sb.append(" NULL");
/*     */     } 
/* 130 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/OracleTableOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */