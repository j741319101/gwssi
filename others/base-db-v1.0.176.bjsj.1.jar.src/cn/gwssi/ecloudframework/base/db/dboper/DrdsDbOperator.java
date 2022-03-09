/*     */ package cn.gwssi.ecloudframework.base.db.dboper;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.constant.ColumnType;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.DbType;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Column;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Table;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class DrdsDbOperator
/*     */   extends DbOperator
/*     */ {
/*     */   public DrdsDbOperator(JdbcTemplate jdbcTemplate) {
/*  31 */     super(jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  36 */     return DbType.DRDS.getKey();
/*     */   }
/*     */   
/*     */   public Map<String, String> getTableNames(String tableName) {
/*     */     List<Map<String, Object>> list;
/*  41 */     String sql = "select table_name,table_comment from information_schema.tables t where t.table_type='BASE TABLE' AND t.table_schema=DATABASE()";
/*     */     
/*  43 */     if (StringUtils.isNotEmpty(tableName)) {
/*  44 */       sql = sql + " AND TABLE_NAME LIKE ?";
/*  45 */       list = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + tableName + "%" });
/*     */     } else {
/*  47 */       list = this.jdbcTemplate.queryForList(sql);
/*     */     } 
/*     */     
/*  50 */     Map<String, String> map = new LinkedHashMap<>();
/*  51 */     for (Map<String, Object> m : list) {
/*  52 */       map.put(m.get("table_name").toString(), m.get("table_comment").toString());
/*     */     }
/*     */     
/*  55 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getViewNames(String viewName) {
/*  60 */     String sql = "show table status where comment='view'";
/*  61 */     if (StringUtils.isNotEmpty(viewName))
/*  62 */       sql = sql + " AND NAME LIKE ?"; 
/*  63 */     List<String> list = new ArrayList<>();
/*  64 */     List<Map<String, Object>> results = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + viewName + "%" });
/*  65 */     for (Map<String, Object> line : results) {
/*  66 */       list.add(line.get("Name").toString());
/*     */     }
/*  68 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table<Column> getTable(String tableName) {
/*  73 */     Table<Column> table = new Table();
/*  74 */     Map<String, String> tableNames = getTableNames(tableName);
/*  75 */     if (tableNames.isEmpty()) {
/*  76 */       throw new BusinessException(String.format("根据表名[%s]获取不到表", new Object[] { tableName }));
/*     */     }
/*  78 */     table.setName(tableName);
/*  79 */     table.setComment(tableNames.get(tableName));
/*  80 */     table.setColumns(getColumns(tableName));
/*     */     
/*  82 */     return table;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table<Column> getView(String viewName) {
/*  87 */     Table<Column> table = new Table();
/*  88 */     List<String> viewNames = getViewNames(viewName);
/*  89 */     if (viewNames.isEmpty()) {
/*  90 */       throw new BusinessException(String.format("根据视图名[%s]获取不到视图", new Object[] { viewName }));
/*     */     }
/*  92 */     table.setName(viewName);
/*  93 */     table.setComment(viewName);
/*  94 */     table.setColumns(getColumns(viewName));
/*  95 */     return table;
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
/*     */   private List<Column> getColumns(String name) {
/* 108 */     String sql = "SELECT * FROM  INFORMATION_SCHEMA.COLUMNS  WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME=?";
/* 109 */     List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, new Object[] { name });
/* 110 */     List<Column> columns = new ArrayList<>();
/* 111 */     for (Map<String, Object> map : list) {
/* 112 */       Column column = new Column();
/* 113 */       column.setComment(getOrDefault(map, "COLUMN_COMMENT", "").toString());
/* 114 */       column.setDefaultValue((map.get("COLUMN_DEFAULT") == null) ? null : map.get("COLUMN_DEFAULT").toString());
/* 115 */       column.setName(getOrDefault(map, "COLUMN_NAME", "").toString());
/* 116 */       column.setPrimary("PRI".equals(getOrDefault(map, "COLUMN_KEY", "")));
/* 117 */       column.setRequired("NO".equals(getOrDefault(map, "IS_NULLABLE", "")));
/* 118 */       column.setType(ColumnType.getByDbDataType(map.get("DATA_TYPE").toString(), "字段[" + column.getComment() + "(" + column.getName() + ")]").getKey());
/* 119 */       if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 120 */         column.setLength(Integer.parseInt(getOrDefault(map, "CHARACTER_MAXIMUM_LENGTH", "0").toString()));
/*     */       }
/* 122 */       if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 123 */         column.setLength(Integer.parseInt(getOrDefault(map, "NUMERIC_PRECISION", "0").toString()));
/* 124 */         column.setDecimal(Integer.parseInt(getOrDefault(map, "NUMERIC_SCALE", "0").toString()));
/*     */       } 
/* 126 */       columns.add(column);
/*     */     } 
/* 128 */     return columns;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportPartition(String tableName) {
/* 133 */     String sql = "select count(*) from information_schema.partitions where table_name=?;";
/* 134 */     Integer rtn = (Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { tableName });
/* 135 */     return (rtn.intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExsitPartition(String tableName, String partName) {
/* 140 */     String sql = "select count(*) from information_schema.partitions where table_name=? and partition_name =?;";
/* 141 */     String[] args = new String[2];
/* 142 */     args[0] = tableName;
/* 143 */     args[1] = "P_" + partName.toUpperCase();
/* 144 */     Integer rtn = (Integer)this.jdbcTemplate.queryForObject(sql, (Object[])args, Integer.class);
/* 145 */     return (rtn.intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createPartition(String tableName, String partName) {
/* 150 */     String sql = "alter table " + tableName + " add partition (partition P_" + partName.toUpperCase() + " values in ('" + partName + "')) ";
/* 151 */     this.jdbcTemplate.execute(sql);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/dboper/DrdsDbOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */