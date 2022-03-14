/*     */ package com.dstz.base.db.dboper;
/*     */ 
/*     */ import com.dstz.base.api.constant.ColumnType;
/*     */ import com.dstz.base.api.constant.DbType;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.db.model.table.Column;
/*     */ import com.dstz.base.db.model.table.Table;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OracleDbOperator
/*     */   extends DbOperator
/*     */ {
/*     */   public OracleDbOperator(JdbcTemplate jdbcTemplate) {
/*  34 */     super(jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  39 */     return DbType.ORACLE.getKey();
/*     */   }
/*     */   
/*     */   public Map<String, String> getTableNames(String tableName) {
/*     */     List<Map<String, Object>> list;
/*  44 */     String sql = "select t.table_name,f.comments from user_tables t inner join user_tab_comments f on t.table_name = f.table_name";
/*     */     
/*  46 */     if (StringUtils.isNotEmpty(tableName)) {
/*  47 */       sql = sql + " AND t.table_name LIKE ?";
/*  48 */       list = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + tableName.toUpperCase() + "%" });
/*     */     } else {
/*  50 */       list = this.jdbcTemplate.queryForList(sql);
/*     */     } 
/*     */     
/*  53 */     Map<String, String> map = new LinkedHashMap<>();
/*  54 */     for (Map<String, Object> m : list) {
/*  55 */       map.put(m.get("table_name").toString(), getOrDefault(m, "comments", "").toString());
/*     */     }
/*     */     
/*  58 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getViewNames(String viewName) {
/*  63 */     String sql = "SELECT * FROM USER_VIEWS";
/*  64 */     if (StringUtils.isNotEmpty(viewName))
/*  65 */       sql = sql + " WHERE VIEW_NAME LIKE ?"; 
/*  66 */     List<String> list = new ArrayList<>();
/*  67 */     List<Map<String, Object>> results = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + viewName.toUpperCase() + "%" });
/*  68 */     for (Map<String, Object> line : results) {
/*  69 */       list.add(line.get("VIEW_NAME").toString());
/*     */     }
/*  71 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table<Column> getTable(String tableName) {
/*  76 */     Table<Column> table = new Table();
/*  77 */     Map<String, String> tableNames = getTableNames(tableName.toUpperCase());
/*  78 */     if (tableNames.isEmpty()) {
/*  79 */       throw new BusinessException(String.format("根据表名[%s]获取不到表", new Object[] { tableName.toUpperCase() }));
/*     */     }
/*  81 */     table.setName(tableName.toUpperCase());
/*  82 */     table.setComment(tableNames.get(tableName.toUpperCase()));
/*  83 */     table.setColumns(getColumns(tableName.toUpperCase()));
/*     */     
/*  85 */     return table;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table<Column> getView(String viewName) {
/*  90 */     Table<Column> table = new Table();
/*  91 */     List<String> viewNames = getViewNames(viewName);
/*  92 */     if (viewNames.isEmpty()) {
/*  93 */       throw new BusinessException(String.format("根据视图名[%s]获取不到视图", new Object[] { viewName.toUpperCase() }));
/*     */     }
/*  95 */     table.setName(viewName.toUpperCase());
/*  96 */     table.setComment(viewName.toUpperCase());
/*  97 */     table.setColumns(getColumns(viewName.toUpperCase()));
/*  98 */     return table;
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
/*     */   private List<Column> getColumns(String name) {
/* 112 */     String sqlT = "select col.column_name from user_constraints con,user_cons_columns col where con.constraint_name=col.constraint_name and con.constraint_type='P' and col.table_name= ?";
/* 113 */     List<Map<String, Object>> listT = this.jdbcTemplate.queryForList(sqlT, new Object[] { name.toUpperCase() });
/* 114 */     Set<String> pkNames = new HashSet<>();
/* 115 */     for (Map<String, Object> map : listT) {
/* 116 */       pkNames.add(getOrDefault(map, "COLUMN_NAME", "").toString());
/*     */     }
/*     */ 
/*     */     
/* 120 */     String sql = "select a.*,b.comments from user_tab_columns a inner join user_col_comments b on a.table_name = b.table_name and a.column_name = b.column_name and a.table_name = ? ";
/* 121 */     List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, new Object[] { name.toUpperCase() });
/* 122 */     List<Column> columns = new ArrayList<>();
/* 123 */     for (Map<String, Object> map : list) {
/* 124 */       Column column = new Column();
/* 125 */       column.setComment(getOrDefault(map, "COMMENTS", "").toString());
/* 126 */       Object defVal = map.get("DATA_DEFAULT");
/* 127 */       if (defVal != null && !defVal.toString().trim().equals("NULL")) {
/* 128 */         column.setDefaultValue(map.get("DATA_DEFAULT").toString());
/*     */       }
/* 130 */       column.setName(getOrDefault(map, "COLUMN_NAME", "").toString());
/* 131 */       column.setPrimary(pkNames.contains(column.getName().toUpperCase()));
/* 132 */       column.setRequired("N".equals(getOrDefault(map, "NULLABLE", "Y")));
/* 133 */       column.setType(ColumnType.getByDbDataType(map.get("DATA_TYPE").toString(), "字段[" + column.getComment() + "(" + column.getName().toUpperCase() + ")]").getKey());
/*     */       
/* 135 */       if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 136 */         column.setLength(Integer.parseInt(getOrDefault(map, "DATA_LENGTH", "0").toString()));
/*     */       }
/* 138 */       if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 139 */         column.setLength(Integer.parseInt(getOrDefault(map, "DATA_PRECISION", "0").toString()));
/* 140 */         column.setDecimal(Integer.parseInt(getOrDefault(map, "DATA_SCALE", "0").toString()));
/*     */       } 
/* 142 */       columns.add(column);
/*     */     } 
/* 144 */     return columns;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportPartition(String tableName) {
/* 149 */     String sql = "select count(*) from user_tab_partitions where table_name = ?";
/* 150 */     Integer rtn = (Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { tableName.toUpperCase() });
/* 151 */     return (rtn.intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExsitPartition(String tableName, String partName) {
/* 156 */     String sql = "select count(*) from user_tab_partitions where table_name = ? and partition_name = ?";
/* 157 */     String[] args = new String[2];
/* 158 */     args[0] = tableName.toUpperCase();
/* 159 */     args[1] = "P_" + partName.toUpperCase();
/* 160 */     Integer rtn = (Integer)this.jdbcTemplate.queryForObject(sql, (Object[])args, Integer.class);
/* 161 */     return (rtn.intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createPartition(String tableName, String partName) {
/* 166 */     String sql = "ALTER TABLE " + tableName.toUpperCase() + " ADD PARTITION P_" + partName.toUpperCase() + " VALUES ( '" + partName + "') NOCOMPRESS ";
/* 167 */     this.jdbcTemplate.update(sql);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/dboper/OracleDbOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */