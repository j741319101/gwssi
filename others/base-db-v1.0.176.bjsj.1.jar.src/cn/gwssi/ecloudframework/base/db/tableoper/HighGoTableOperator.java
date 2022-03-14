/*     */ package com.dstz.base.db.tableoper;
/*     */ 
/*     */ import com.dstz.base.api.constant.ColumnType;
/*     */ import com.dstz.base.api.constant.DbType;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.table.Column;
/*     */ import com.dstz.base.db.model.table.Table;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ public class HighGoTableOperator
/*     */   extends TableOperator
/*     */ {
/*     */   public HighGoTableOperator(Table<? extends Column> table, JdbcTemplate jdbcTemplate) {
/*  25 */     super(table, jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  30 */     return DbType.HIGHGO.getKey();
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
/*  42 */     sql.append("CREATE TABLE ").append(this.table.getName().toUpperCase()).append(" (").append("\n");
/*  43 */     for (Column column : this.table.getColumns()) {
/*  44 */       sql.append(columnToSql(column)).append(",\n");
/*     */     }
/*  46 */     sql.append("PRIMARY KEY (").append(this.table.getPkColumn().getName().toUpperCase()).append(")\n)");
/*     */     
/*  48 */     sql.append(";");
/*  49 */     this.jdbcTemplate.execute(sql.toString());
/*     */     
/*  51 */     if (StringUtil.isNotEmpty(this.table.getComment())) {
/*  52 */       String str = "COMMENT ON TABLE " + this.table.getName().toUpperCase() + " IS '" + this.table.getComment() + "'";
/*     */       
/*  54 */       this.jdbcTemplate.execute(str);
/*     */     } 
/*     */ 
/*     */     
/*  58 */     for (int i = 0; i < this.table.getColumns().size(); i++) {
/*  59 */       Column column = this.table.getColumns().get(i);
/*  60 */       updateComment(column);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateComment(Column column) {
/*  70 */     if (!StringUtil.isEmpty(column.getComment())) {
/*  71 */       String str = "COMMENT ON COLUMN " + this.table.getName().toUpperCase() + "." + column.getName().toUpperCase() + "  IS '" + column.getComment() + "'";
/*  72 */       this.jdbcTemplate.execute(str);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTableCreated() {
/*  78 */     String sql = "select count(1) from information_schema.TABLES t where upper(table_name) =?"; return 
/*  79 */       (((Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { this.table.getName().toUpperCase() })).intValue() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addColumn(Column column) {
/*  85 */     String sql = "ALTER TABLE " + this.table.getName().toUpperCase() + " ADD COLUMN " + columnToSql(column) + ";";
/*  86 */     this.jdbcTemplate.execute(sql);
/*  87 */     updateComment(column);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateColumn(Column column) {
/*  93 */     String sql = "ALTER TABLE " + this.table.getName().toUpperCase() + " ALTER COLUMN " + columnToSql(column, "type") + ";";
/*  94 */     this.jdbcTemplate.execute(sql);
/*  95 */     updateComment(column);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dropColumn(String columnName) {
/* 101 */     String sql = "ALTER TABLE " + this.table.getName().toUpperCase() + " DROP COLUMN " + columnName.toUpperCase() + ";";
/* 102 */     this.jdbcTemplate.execute(sql);
/*     */   }
/*     */   
/*     */   private String columnToSql(Column column) {
/* 106 */     return columnToSql(column, "");
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
/*     */   private String columnToSql(Column column, String type) {
/* 118 */     StringBuilder sb = new StringBuilder();
/* 119 */     sb.append(column.getName().toUpperCase()).append(" ").append(type);
/* 120 */     if (ColumnType.CLOB.equalsWithKey(column.getType())) {
/* 121 */       sb.append(" text");
/* 122 */     } else if (ColumnType.DATE.equalsWithKey(column.getType())) {
/* 123 */       sb.append(" TIMESTAMP");
/* 124 */     } else if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 125 */       sb.append(" decimal(").append(column.getLength()).append(",").append(column.getDecimal()).append(")");
/* 126 */     } else if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 127 */       sb.append(" varchar(").append(column.getLength()).append(")");
/*     */     } 
/*     */     
/* 130 */     if (column.isRequired() || (column.isPrimary() && StringUtils.isBlank(type))) {
/* 131 */       sb.append(" NOT NULL");
/*     */     }
/* 133 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/HighGoTableOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */