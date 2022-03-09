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
/*     */ 
/*     */ 
/*     */ public class KingBaseTableOperator
/*     */   extends TableOperator
/*     */ {
/*     */   public KingBaseTableOperator(Table<? extends Column> table, JdbcTemplate jdbcTemplate) {
/*  27 */     super(table, jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  32 */     return DbType.KINGBASE.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public void createTable() {
/*  37 */     if (isTableCreated()) {
/*  38 */       this.logger.debug("表[" + this.table.getName().toUpperCase() + "(" + this.table.getComment() + ")]已存在数据库中，无需再次生成");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  43 */     StringBuilder sql = new StringBuilder();
/*  44 */     sql.append("CREATE TABLE \"" + this.table.getName().toUpperCase() + "\" (\n");
/*  45 */     for (Column column : this.table.getColumns()) {
/*  46 */       sql.append(columnToSql(column) + ",\n");
/*     */     }
/*  48 */     sql.append("PRIMARY KEY (\"" + this.table.getPkColumn().getName().toUpperCase() + "\")\n)");
/*     */     
/*  50 */     this.jdbcTemplate.execute(sql.toString());
/*     */ 
/*     */     
/*  53 */     if (StringUtil.isNotEmpty(this.table.getComment())) {
/*  54 */       String str = "COMMENT ON TABLE \"" + this.table.getName().toUpperCase() + "\" IS '" + this.table.getComment() + "'";
/*  55 */       this.jdbcTemplate.execute(str);
/*     */     } 
/*     */ 
/*     */     
/*  59 */     for (int i = 0; i < this.table.getColumns().size(); i++) {
/*  60 */       Column column = this.table.getColumns().get(i);
/*  61 */       if (!StringUtil.isEmpty(column.getComment())) {
/*     */ 
/*     */         
/*  64 */         String str = "COMMENT ON COLUMN \"" + this.table.getName().toUpperCase() + "\".\"" + column.getName().toUpperCase() + "\"  IS '" + column.getComment() + "'";
/*  65 */         this.jdbcTemplate.execute(str);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isTableCreated() {
/*  71 */     String sql = "select count(1) from user_tables t where table_name =?"; return 
/*  72 */       (((Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { this.table.getName().toUpperCase() })).intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addColumn(Column column) {
/*  77 */     StringBuilder sql = new StringBuilder();
/*  78 */     sql.append("ALTER TABLE \"" + this.table.getName().toUpperCase() + "\"");
/*  79 */     sql.append(" ADD ( " + columnToSql(column) + " )");
/*  80 */     this.jdbcTemplate.execute(sql.toString());
/*     */ 
/*     */     
/*  83 */     if (StringUtil.isEmpty(column.getComment())) {
/*     */       return;
/*     */     }
/*  86 */     String str = "COMMENT ON COLUMN \"" + this.table.getName().toUpperCase() + "\".\"" + column.getName().toUpperCase() + "\"  IS '" + column.getComment() + "'";
/*  87 */     this.jdbcTemplate.execute(str);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateColumn(Column column) {
/*  92 */     StringBuilder sql = new StringBuilder();
/*  93 */     sql.append("ALTER TABLE \"" + this.table.getName().toUpperCase() + "\"");
/*  94 */     sql.append(" MODIFY( " + columnToSql(column) + " )");
/*  95 */     this.jdbcTemplate.execute(sql.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropColumn(String columnName) {
/* 100 */     StringBuilder sql = new StringBuilder();
/* 101 */     sql.append("ALTER TABLE \"" + this.table.getName().toUpperCase() + "\"");
/* 102 */     sql.append(" DROP(\"" + columnName + "\")");
/* 103 */     this.jdbcTemplate.execute(sql.toString());
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
/* 115 */     StringBuilder sb = new StringBuilder();
/* 116 */     sb.append("\"" + column.getName().toUpperCase() + "\"");
/* 117 */     if (ColumnType.CLOB.equalsWithKey(column.getType())) {
/* 118 */       sb.append(" CLOB");
/* 119 */     } else if (ColumnType.DATE.equalsWithKey(column.getType())) {
/* 120 */       sb.append(" TIMESTAMP");
/* 121 */     } else if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 122 */       sb.append(" NUMBER(" + column.getLength() + "," + column.getDecimal() + ")");
/* 123 */     } else if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 124 */       sb.append(" VARCHAR2(" + column.getLength() + ")");
/*     */     } 
/*     */     
/* 127 */     if (column.isRequired() || column.isPrimary()) {
/* 128 */       sb.append(" NOT NULL");
/*     */     } else {
/* 130 */       sb.append(" NULL");
/*     */     } 
/* 132 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/KingBaseTableOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */