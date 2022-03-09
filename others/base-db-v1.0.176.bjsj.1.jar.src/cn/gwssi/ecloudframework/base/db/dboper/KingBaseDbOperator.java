/*     */ package cn.gwssi.ecloudframework.base.db.dboper;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.constant.BaseStatusCode;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.ColumnType;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.DbType;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Column;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Table;
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
/*     */ public class KingBaseDbOperator
/*     */   extends DbOperator
/*     */ {
/*     */   public KingBaseDbOperator(JdbcTemplate jdbcTemplate) {
/*  30 */     super(jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  35 */     return DbType.KINGBASE.getKey();
/*     */   }
/*     */   
/*     */   public Map<String, String> getTableNames(String tableName) {
/*     */     List<Map<String, Object>> list;
/*  40 */     String sql = "select t.table_name,f.comments from user_tables t inner join user_tab_comments f on t.table_name = f.table_name";
/*     */     
/*  42 */     if (StringUtils.isNotEmpty(tableName)) {
/*  43 */       sql = sql + " AND t.table_name LIKE ?";
/*  44 */       list = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + tableName.toUpperCase() + "%" });
/*     */     } else {
/*  46 */       list = this.jdbcTemplate.queryForList(sql);
/*     */     } 
/*     */     
/*  49 */     Map<String, String> map = new LinkedHashMap<>();
/*  50 */     for (Map<String, Object> m : list) {
/*  51 */       map.put(m.get("table_name").toString().toUpperCase(), getOrDefault(m, "comments", "").toString());
/*     */     }
/*     */     
/*  54 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getViewNames(String viewName) {
/*  59 */     String sql = "SELECT * FROM USER_VIEWS";
/*  60 */     if (StringUtils.isNotEmpty(viewName))
/*  61 */       sql = sql + " WHERE VIEW_NAME LIKE ?"; 
/*  62 */     List<String> list = new ArrayList<>();
/*  63 */     List<Map<String, Object>> results = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + viewName.toUpperCase() + "%" });
/*  64 */     for (Map<String, Object> line : results) {
/*  65 */       list.add(line.get("VIEW_NAME").toString());
/*     */     }
/*  67 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table<Column> getTable(String tableName) {
/*  72 */     Table<Column> table = new Table();
/*  73 */     Map<String, String> tableNames = getTableNames(tableName.toUpperCase());
/*  74 */     if (tableNames.isEmpty()) {
/*  75 */       throw new BusinessException(tableName + BaseStatusCode.TABLE_NOT_FOUND.getDesc(), BaseStatusCode.TABLE_NOT_FOUND);
/*     */     }
/*  77 */     table.setName(tableName.toUpperCase());
/*  78 */     table.setComment(tableNames.get(tableName.toUpperCase()));
/*  79 */     table.setColumns(getColumns(tableName.toUpperCase()));
/*     */     
/*  81 */     return table;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table<Column> getView(String viewName) {
/*  86 */     Table<Column> table = new Table();
/*  87 */     List<String> viewNames = getViewNames(viewName);
/*  88 */     if (viewNames.isEmpty()) {
/*  89 */       throw new BusinessException(BaseStatusCode.VIEW_NOT_FOUND);
/*     */     }
/*  91 */     table.setName(viewName.toUpperCase());
/*  92 */     table.setComment(viewName.toUpperCase());
/*  93 */     table.setColumns(getColumns(viewName.toUpperCase()));
/*  94 */     return table;
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
/* 108 */     String sqlT = "select col.column_name from user_constraints con,user_cons_columns col where con.constraint_name=col.constraint_name and con.constraint_type='P' and col.table_name= ?";
/* 109 */     List<Map<String, Object>> listT = this.jdbcTemplate.queryForList(sqlT, new Object[] { name.toUpperCase() });
/* 110 */     Set<String> pkNames = new HashSet<>();
/* 111 */     for (Map<String, Object> map : listT) {
/* 112 */       pkNames.add(getOrDefault(map, "COLUMN_NAME", "").toString().toUpperCase());
/*     */     }
/*     */ 
/*     */     
/* 116 */     String sql = "select a.*,b.comments from user_tab_columns a inner join user_col_comments b on a.table_name = b.table_name and a.column_name = b.column_name and a.table_name = ? ";
/* 117 */     List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, new Object[] { name.toUpperCase() });
/* 118 */     List<Column> columns = new ArrayList<>();
/* 119 */     for (Map<String, Object> map : list) {
/* 120 */       Column column = new Column();
/* 121 */       column.setComment(getOrDefault(map, "COMMENTS", "").toString());
/* 122 */       Object defVal = map.get("DATA_DEFAULT");
/* 123 */       if (defVal != null && !defVal.toString().trim().equals("NULL")) {
/* 124 */         column.setDefaultValue(map.get("DATA_DEFAULT").toString());
/*     */       }
/* 126 */       column.setName(getOrDefault(map, "COLUMN_NAME", "").toString());
/* 127 */       column.setPrimary(pkNames.contains(column.getName().toUpperCase()));
/* 128 */       column.setRequired("N".equals(getOrDefault(map, "NULLABLE", "Y")));
/* 129 */       column.setType(ColumnType.getByDbDataType(map.get("DATA_TYPE").toString(), "字段[" + column.getComment() + "(" + column.getName().toUpperCase() + ")]").getKey());
/*     */       
/* 131 */       if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 132 */         column.setLength(Integer.parseInt(getOrDefault(map, "DATA_LENGTH", "0").toString()));
/*     */       }
/* 134 */       if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 135 */         column.setLength(Integer.parseInt(getOrDefault(map, "DATA_PRECISION", "0").toString()));
/* 136 */         column.setDecimal(Integer.parseInt(getOrDefault(map, "DATA_SCALE", "0").toString()));
/*     */       } 
/* 138 */       columns.add(column);
/*     */     } 
/* 140 */     return columns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportPartition(String tableName) {
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExsitPartition(String tableName, String partName) {
/* 152 */     String sql = "select count(*) from user_tab_partitions where table_name = ? and partition_name = ?";
/* 153 */     String[] args = new String[2];
/* 154 */     args[0] = tableName.toUpperCase();
/* 155 */     args[1] = "P_" + partName.toUpperCase();
/* 156 */     Integer rtn = (Integer)this.jdbcTemplate.queryForObject(sql, (Object[])args, Integer.class);
/* 157 */     return (rtn.intValue() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createPartition(String tableName, String partName) {
/* 163 */     String sql = "ALTER TABLE " + tableName.toUpperCase() + " ADD PARTITION P_" + partName.toUpperCase() + " VALUES ( '" + partName + "')";
/* 164 */     this.jdbcTemplate.update(sql);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/dboper/KingBaseDbOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */