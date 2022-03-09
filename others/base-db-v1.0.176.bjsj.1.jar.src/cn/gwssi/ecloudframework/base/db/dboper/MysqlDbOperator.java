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
/*     */ 
/*     */ public class MysqlDbOperator
/*     */   extends DbOperator
/*     */ {
/*     */   public MysqlDbOperator(JdbcTemplate jdbcTemplate) {
/*  32 */     super(jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  37 */     return DbType.MYSQL.getKey();
/*     */   }
/*     */   
/*     */   public Map<String, String> getTableNames(String tableName) {
/*     */     List<Map<String, Object>> list;
/*  42 */     String sql = "select table_name,table_comment from information_schema.tables t where t.table_type='BASE TABLE' AND t.table_schema=DATABASE()";
/*     */     
/*  44 */     if (StringUtils.isNotEmpty(tableName)) {
/*  45 */       sql = sql + " AND TABLE_NAME LIKE ?";
/*  46 */       list = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + tableName + "%" });
/*     */     } else {
/*  48 */       list = this.jdbcTemplate.queryForList(sql);
/*     */     } 
/*     */     
/*  51 */     Map<String, String> map = new LinkedHashMap<>();
/*  52 */     for (Map<String, Object> m : list) {
/*  53 */       map.put(m.get("table_name").toString(), m.get("table_comment").toString());
/*     */     }
/*     */     
/*  56 */     return map;
/*     */   }
/*     */   
/*     */   public List<String> getViewNames(String viewName) {
/*     */     List<Map<String, Object>> results;
/*  61 */     String sql = "show table status where comment='view'";
/*  62 */     List<String> list = new ArrayList<>();
/*     */     
/*  64 */     if (StringUtils.isNotEmpty(viewName)) {
/*  65 */       sql = sql + " AND NAME LIKE ?";
/*  66 */       results = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + viewName + "%" });
/*     */     } else {
/*  68 */       results = this.jdbcTemplate.queryForList(sql);
/*     */     } 
/*  70 */     for (Map<String, Object> line : results) {
/*  71 */       list.add(line.get("Name").toString());
/*     */     }
/*  73 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table<Column> getTable(String tableName) {
/*  78 */     Table<Column> table = new Table();
/*  79 */     Map<String, String> tableNames = getTableNames(tableName);
/*  80 */     if (tableNames.isEmpty()) {
/*  81 */       throw new BusinessException(String.format("根据表名[%s]获取不到表", new Object[] { tableName }));
/*     */     }
/*  83 */     table.setName(tableName);
/*  84 */     table.setComment(tableNames.get(tableName));
/*  85 */     table.setColumns(getColumns(tableName));
/*     */     
/*  87 */     return table;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table<Column> getView(String viewName) {
/*  92 */     Table<Column> table = new Table();
/*  93 */     List<String> viewNames = getViewNames(viewName);
/*  94 */     if (viewNames.isEmpty()) {
/*  95 */       throw new BusinessException(String.format("根据视图名[%s]获取不到视图", new Object[] { viewName }));
/*     */     }
/*  97 */     table.setName(viewName);
/*  98 */     table.setComment(viewName);
/*  99 */     table.setColumns(getColumns(viewName));
/* 100 */     return table;
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
/* 113 */     String sql = "SELECT * FROM  INFORMATION_SCHEMA.COLUMNS  WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME=?";
/* 114 */     List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, new Object[] { name });
/* 115 */     List<Column> columns = new ArrayList<>();
/* 116 */     for (Map<String, Object> map : list) {
/* 117 */       Column column = new Column();
/* 118 */       column.setComment(getOrDefault(map, "COLUMN_COMMENT", "").toString());
/* 119 */       column.setDefaultValue((map.get("COLUMN_DEFAULT") == null) ? null : map.get("COLUMN_DEFAULT").toString());
/* 120 */       column.setName(getOrDefault(map, "COLUMN_NAME", "").toString());
/* 121 */       column.setPrimary("PRI".equals(getOrDefault(map, "COLUMN_KEY", "")));
/* 122 */       column.setRequired("NO".equals(getOrDefault(map, "IS_NULLABLE", "")));
/* 123 */       column.setType(ColumnType.getByDbDataType(map.get("DATA_TYPE").toString(), "字段[" + column.getComment() + "(" + column.getName() + ")]").getKey());
/* 124 */       if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 125 */         column.setLength(Integer.parseInt(getOrDefault(map, "CHARACTER_MAXIMUM_LENGTH", "0").toString()));
/*     */       }
/* 127 */       if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 128 */         column.setLength(Integer.parseInt(getOrDefault(map, "NUMERIC_PRECISION", "0").toString()));
/* 129 */         column.setDecimal(Integer.parseInt(getOrDefault(map, "NUMERIC_SCALE", "0").toString()));
/*     */       } 
/* 131 */       columns.add(column);
/*     */     } 
/* 133 */     return columns;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportPartition(String tableName) {
/* 138 */     String sql = "select count(*) from information_schema.partitions where table_name=? and PARTITION_NAME is not null;";
/* 139 */     Integer rtn = (Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { tableName });
/* 140 */     return (rtn.intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExsitPartition(String tableName, String partName) {
/* 145 */     String sql = "select count(*) from information_schema.partitions where table_name=? and partition_name =?;";
/* 146 */     String[] args = new String[2];
/* 147 */     args[0] = tableName;
/* 148 */     args[1] = "P_" + partName.toUpperCase();
/* 149 */     Integer rtn = (Integer)this.jdbcTemplate.queryForObject(sql, (Object[])args, Integer.class);
/* 150 */     return (rtn.intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createPartition(String tableName, String partName) {
/* 155 */     String sql = "alter table " + tableName + " add partition (partition P_" + partName.toUpperCase() + " values in ('" + partName + "')) ";
/* 156 */     this.jdbcTemplate.execute(sql);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/dboper/MysqlDbOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */