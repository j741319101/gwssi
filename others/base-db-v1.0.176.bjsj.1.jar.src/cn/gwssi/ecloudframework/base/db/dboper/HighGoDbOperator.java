/*     */ package com.dstz.base.db.dboper;
/*     */ 
/*     */ import com.dstz.base.api.constant.ColumnType;
/*     */ import com.dstz.base.api.constant.DbType;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.db.model.table.Column;
/*     */ import com.dstz.base.db.model.table.Table;
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
/*     */ public class HighGoDbOperator
/*     */   extends DbOperator
/*     */ {
/*     */   public HighGoDbOperator(JdbcTemplate jdbcTemplate) {
/*  31 */     super(jdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  36 */     return DbType.HIGHGO.getKey();
/*     */   }
/*     */   
/*     */   public Map<String, String> getTableNames(String tableName) {
/*     */     List<Map<String, Object>> list;
/*  41 */     String sql = "select relname as table_name,obj_description(c.oid) as table_comment from pg_class c where relname in (select tablename from pg_tables where schemaname=current_schema)";
/*     */     
/*  43 */     if (StringUtils.isNotEmpty(tableName)) {
/*  44 */       sql = sql + " AND upper(relname) LIKE ?";
/*  45 */       list = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + tableName.toUpperCase() + "%" });
/*     */     } else {
/*  47 */       list = this.jdbcTemplate.queryForList(sql);
/*     */     } 
/*     */     
/*  50 */     Map<String, String> map = new LinkedHashMap<>();
/*  51 */     for (Map<String, Object> m : list) {
/*  52 */       map.put(String.valueOf(m.get("table_name")), String.valueOf(m.get("table_comment")));
/*     */     }
/*     */     
/*  55 */     return map;
/*     */   }
/*     */   
/*     */   public List<String> getViewNames(String viewName) {
/*     */     List<Map<String, Object>> results;
/*  60 */     String sql = "SELECT viewname FROM pg_views WHERE schemaname=current_schema";
/*  61 */     List<String> list = new ArrayList<>();
/*     */     
/*  63 */     if (StringUtils.isNotEmpty(viewName)) {
/*  64 */       sql = sql + " AND upper(viewname) LIKE ?";
/*  65 */       results = this.jdbcTemplate.queryForList(sql, new Object[] { "%" + viewName.toUpperCase() + "%" });
/*     */     } else {
/*     */       
/*  68 */       results = this.jdbcTemplate.queryForList(sql);
/*     */     } 
/*  70 */     for (Map<String, Object> line : results) {
/*  71 */       list.add(line.get("viewname").toString());
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
/*     */   private List<Column> getColumns(String tableName) {
/* 112 */     String sql = "SELECT info.column_default,\n        info.column_name,\n        info.is_nullable,\n        info.udt_name as data_type,\n        info.character_maximum_length,\n        info.numeric_precision,\n        info.numeric_scale,\n        t.comment as column_comment,\n        t.column_key\nFROM  INFORMATION_SCHEMA.COLUMNS info\nLEFT JOIN (SELECT a.attname AS fieldName,\n        b.description AS comment,\n        case when s.pk is not null then 'PRI' \n        end as column_key    FROM pg_class c\n    INNER JOIN pg_attribute a\n        ON a.attrelid = c.oid\n    LEFT JOIN (SELECT conrelid, UNNEST(conkey) AS pk\n                FROM pg_constraint\n                WHERE contype = 'p') S \n         ON S.conrelid = C .oid\n         AND a.attnum = S.pk\n     LEFT JOIN pg_description b\n         ON a.attrelid = b.objoid\n         AND a.attnum = b.objsubid\n     where a.attnum > 0\n     and upper(c.relname) = ? ) t\nON info.column_name = t.fieldName\nWHERE info.TABLE_SCHEMA= current_schema\nAND upper(info.TABLE_NAME) = ? ";
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
/*     */     
/* 142 */     List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, new Object[] { tableName.toUpperCase(), tableName.toUpperCase() });
/* 143 */     List<Column> columns = new ArrayList<>();
/*     */     
/* 145 */     for (Map<String, Object> map : list) {
/* 146 */       Column column = new Column();
/* 147 */       column.setComment(getOrDefault(map, "column_comment", "").toString());
/* 148 */       column.setDefaultValue((map.get("column_default") == null) ? null : map.get("column_default").toString());
/* 149 */       column.setName(getOrDefault(map, "column_name", "").toString());
/* 150 */       column.setPrimary("PRI".equals(getOrDefault(map, "column_key", "")));
/* 151 */       column.setRequired("NO".equals(getOrDefault(map, "is_nullable", "")));
/* 152 */       column.setType(ColumnType.getByDbDataType(map.get("data_type").toString(), "字段[" + column.getComment() + "(" + column.getName() + ")]").getKey());
/* 153 */       if (ColumnType.VARCHAR.equalsWithKey(column.getType())) {
/* 154 */         column.setLength(Integer.parseInt(getOrDefault(map, "character_maximum_length", "0").toString()));
/*     */       }
/* 156 */       if (ColumnType.NUMBER.equalsWithKey(column.getType())) {
/* 157 */         column.setLength(Integer.parseInt(getOrDefault(map, "numeric_precision", "0").toString()));
/* 158 */         column.setDecimal(Integer.parseInt(getOrDefault(map, "numeric_scale", "0").toString()));
/*     */       } 
/* 160 */       columns.add(column);
/*     */     } 
/* 162 */     return columns;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportPartition(String tableName) {
/* 167 */     String sql = "SELECT count(*) FROM pg_inherits JOIN pg_class parent ON pg_inherits.inhparent = parent.oid WHERE upper(parent.relname) = ?";
/* 168 */     Integer rtn = (Integer)this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { tableName.toUpperCase() });
/* 169 */     return (rtn.intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExsitPartition(String tableName, String partName) {
/* 174 */     String sql = "SELECT count(*) FROM pg_inherits JOIN pg_class parent ON pg_inherits.inhparent = parent.oid JOIN pg_class child ON pg_inherits.inhrelid = child.oid WHERE upper(parent.relname) = ? AND upper(child.relname) = ?";
/* 175 */     String[] args = new String[2];
/* 176 */     args[0] = tableName.toUpperCase();
/* 177 */     args[1] = "P_" + partName.toUpperCase();
/* 178 */     Integer rtn = (Integer)this.jdbcTemplate.queryForObject(sql, (Object[])args, Integer.class);
/* 179 */     return (rtn.intValue() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createPartition(String tableName, String partName) {
/* 186 */     String sql = "CREATE TABLE P_" + partName.toUpperCase() + " () INHERITS (" + tableName.toUpperCase() + ")";
/* 187 */     this.jdbcTemplate.execute(sql);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/dboper/HighGoDbOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */