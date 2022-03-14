/*     */ package com.dstz.base.db.tableoper;
/*     */ 
/*     */ import com.dstz.base.api.constant.ColumnType;
/*     */ import com.dstz.base.api.constant.DbType;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.table.Column;
/*     */ import com.dstz.base.db.model.table.Table;
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
/*     */ public class DmsqlTableOperator
/*     */   extends TableOperator
/*     */ {
/*     */   public DmsqlTableOperator(Table<? extends Column> table, JdbcTemplate jdbcTemplate) {
/*  23 */     super(table, jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  28 */     return DbType.DMSQL.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public void createTable() {
/*  33 */     if (isTableCreated()) {
/*  34 */       this.logger.debug("表[" + this.table.getName().toUpperCase() + "(" + this.table.getComment() + ")]已存在数据库中，无需再次生成");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  39 */     StringBuilder sql = new StringBuilder();
/*  40 */     sql.append("CREATE TABLE \"").append(this.table.getName().toUpperCase()).append("\" (").append("\n");
/*  41 */     for (Column column : this.table.getColumns()) {
/*  42 */       sql.append(columnToSql(column)).append(",\n");
/*     */     }
/*  44 */     sql.append("NOT CLUSTER PRIMARY KEY (\"").append(this.table.getPkColumn().getName().toUpperCase()).append("\")").append("\n)");
/*     */     
/*  46 */     this.jdbcTemplate.execute(sql.toString());
/*     */ 
/*     */     
/*  49 */     if (StringUtil.isNotEmpty(this.table.getComment())) {
/*  50 */       String str = "COMMENT ON TABLE \"" + this.table.getName().toUpperCase() + "\" IS '" + this.table.getComment() + "'";
/*     */       
/*  52 */       this.jdbcTemplate.execute(str);
/*     */     } 
/*     */ 
/*     */     
/*  56 */     for (int i = 0; i < this.table.getColumns().size(); i++) {
/*  57 */       Column column = this.table.getColumns().get(i);
/*  58 */       updateComment(column);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTableCreated() {
/*  64 */     String sql = "select count(1) from user_tables t where table_name =?"; return 
/*  65 */       (((Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { this.table.getName().toUpperCase() })).intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addColumn(Column column) {
/*  70 */     String sql = "ALTER TABLE \"" + this.table.getName().toUpperCase() + "\" ADD ( " + columnToSql(column) + " )";
/*  71 */     this.jdbcTemplate.execute(sql);
/*  72 */     updateComment(column);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateColumn(Column column) {
/*  77 */     String sql = "ALTER TABLE \"" + this.table.getName().toUpperCase() + "\" MODIFY( " + columnToSql(column) + " )";
/*  78 */     this.jdbcTemplate.execute(sql);
/*  79 */     updateComment(column);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dropColumn(String columnName) {
/*  85 */     String sql = "ALTER TABLE \"" + this.table.getName().toUpperCase() + "\" DROP(\"" + columnName + "\")";
/*  86 */     this.jdbcTemplate.execute(sql);
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
/*  98 */     StringBuilder sb = new StringBuilder();
/*  99 */     sb.append("\"").append(column.getName().toUpperCase()).append("\"");
/* 100 */     if (ColumnType.CLOB.equalsWithKey(column.getType())) {
/* 101 */       sb.append(" CLOB");
/* 102 */     } else if (ColumnType.DATE.equalsWithKey(column.getType())) {
/* 103 */       sb.append(" TIMESTAMP");
/* 104 */     } else if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 105 */       sb.append(" NUMBER(").append(column.getLength()).append(",").append(column.getDecimal()).append(")");
/* 106 */     } else if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 107 */       sb.append(" VARCHAR2(").append(column.getLength()).append(")");
/*     */     } 
/*     */     
/* 110 */     if (column.isRequired() || column.isPrimary()) {
/* 111 */       sb.append(" NOT NULL");
/*     */     } else {
/* 113 */       sb.append(" NULL");
/*     */     } 
/* 115 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateComment(Column column) {
/* 124 */     if (!StringUtil.isEmpty(column.getComment())) {
/* 125 */       String str = "COMMENT ON COLUMN \"" + this.table.getName().toUpperCase() + "\".\"" + column.getName().toUpperCase() + "\"  IS '" + column.getComment() + "'";
/* 126 */       this.jdbcTemplate.execute(str);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/DmsqlTableOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */