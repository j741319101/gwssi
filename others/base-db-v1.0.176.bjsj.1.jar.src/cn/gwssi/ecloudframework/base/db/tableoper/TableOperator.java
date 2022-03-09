/*     */ package cn.gwssi.ecloudframework.base.db.tableoper;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.dboper.DbOperator;
/*     */ import cn.gwssi.ecloudframework.base.db.dboper.DbOperatorFactory;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Column;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Table;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.dao.DataIntegrityViolationException;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TableOperator
/*     */ {
/*  34 */   protected Logger logger = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   CommonColumn commonColumn;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Table<? extends Column> table;
/*     */ 
/*     */ 
/*     */   
/*     */   protected JdbcTemplate jdbcTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TableOperator(Table<? extends Column> table, JdbcTemplate jdbcTemplate) {
/*  53 */     this.table = table;
/*  54 */     this.jdbcTemplate = jdbcTemplate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String type();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createTable() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dropTable() {
/*  81 */     if (!isTableCreated()) {
/*     */       return;
/*     */     }
/*  84 */     String sql = "drop table " + this.table.getName() + "";
/*  85 */     this.jdbcTemplate.execute(sql);
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
/*     */   public boolean isTableCreated() {
/*  97 */     return false;
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
/*     */   public void addColumn(Column column) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateColumn(Column column) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dropColumn(String columnName) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertData(Map<String, Object> data) {
/* 139 */     StringBuilder sql = new StringBuilder();
/* 140 */     sql.append("INSERT INTO " + this.table.getName());
/* 141 */     StringBuilder columnNameSql = new StringBuilder();
/* 142 */     StringBuilder paramNameSql = new StringBuilder();
/* 143 */     List<Object> param = new ArrayList();
/*     */     
/* 145 */     addCreateColumn(data);
/* 146 */     for (Map.Entry<String, Object> entry : data.entrySet()) {
/* 147 */       if (columnNameSql.length() > 0) {
/* 148 */         columnNameSql.append(",");
/* 149 */         paramNameSql.append(",");
/*     */       } 
/* 151 */       columnNameSql.append(entry.getKey());
/* 152 */       paramNameSql.append("?");
/* 153 */       param.add(entry.getValue());
/*     */     } 
/* 155 */     sql.append("(" + columnNameSql + ") VALUES(" + paramNameSql + ")");
/* 156 */     this.jdbcTemplate.update(sql.toString(), param.toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addCreateColumn(Map<String, Object> data) {
/* 165 */     if (null == this.commonColumn) {
/* 166 */       this.commonColumn = (CommonColumn)AppUtil.getBean(CommonColumn.class);
/*     */     }
/* 168 */     if (data.containsKey(this.commonColumn.getCreateUser().toUpperCase()) && this.table
/* 169 */       .getColumn(this.commonColumn.getCreateUser().toUpperCase()) != null) {
/* 170 */       data.put(this.commonColumn.getCreateUser().toUpperCase(), ContextUtil.getCurrentUserId());
/* 171 */     } else if (data.containsKey(this.commonColumn.getCreateUser()) && this.table
/* 172 */       .getColumn(this.commonColumn.getCreateUser()) != null) {
/* 173 */       data.put(this.commonColumn.getCreateUser(), ContextUtil.getCurrentUserId());
/* 174 */     } else if (this.table.getColumn(this.commonColumn.getCreateUser()) != null) {
/* 175 */       data.put(this.commonColumn.getCreateUser(), ContextUtil.getCurrentUserId());
/*     */     } 
/* 177 */     if (data.containsKey(this.commonColumn.getCreateTime().toUpperCase()) && this.table
/* 178 */       .getColumn(this.commonColumn.getCreateTime().toUpperCase()) != null) {
/* 179 */       data.put(this.commonColumn.getCreateTime().toUpperCase(), new Date());
/* 180 */     } else if (data.containsKey(this.commonColumn.getCreateTime()) && this.table
/* 181 */       .getColumn(this.commonColumn.getCreateTime()) != null) {
/* 182 */       data.put(this.commonColumn.getCreateTime(), new Date());
/* 183 */     } else if (this.table.getColumn(this.commonColumn.getCreateTime()) != null) {
/* 184 */       data.put(this.commonColumn.getCreateTime(), new Date());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addUpdateColumn(Map<String, Object> data) {
/* 194 */     if (null == this.commonColumn) {
/* 195 */       this.commonColumn = (CommonColumn)AppUtil.getBean(CommonColumn.class);
/*     */     }
/* 197 */     if (data.containsKey(this.commonColumn.getUpdateUser().toUpperCase()) && this.table
/* 198 */       .getColumn(this.commonColumn.getUpdateUser().toUpperCase()) != null) {
/* 199 */       data.put(this.commonColumn.getUpdateUser().toUpperCase(), ContextUtil.getCurrentUserId());
/* 200 */     } else if (data.containsKey(this.commonColumn.getUpdateUser()) && this.table
/* 201 */       .getColumn(this.commonColumn.getUpdateUser()) != null) {
/* 202 */       data.put(this.commonColumn.getUpdateUser(), ContextUtil.getCurrentUserId());
/* 203 */     } else if (this.table.getColumn(this.commonColumn.getUpdateUser()) != null) {
/* 204 */       data.put(this.commonColumn.getUpdateUser(), ContextUtil.getCurrentUserId());
/*     */     } 
/* 206 */     if (data.containsKey(this.commonColumn.getUpdateTime().toUpperCase()) && this.table
/* 207 */       .getColumn(this.commonColumn.getUpdateTime().toUpperCase()) != null) {
/* 208 */       data.put(this.commonColumn.getUpdateTime().toUpperCase(), new Date());
/* 209 */     } else if (data.containsKey(this.commonColumn.getUpdateTime()) && this.table
/* 210 */       .getColumn(this.commonColumn.getUpdateTime()) != null) {
/* 211 */       data.put(this.commonColumn.getUpdateTime(), new Date());
/* 212 */     } else if (this.table.getColumn(this.commonColumn.getUpdateTime()) != null) {
/* 213 */       data.put(this.commonColumn.getUpdateTime(), new Date());
/*     */     } 
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
/*     */   public void deleteData(Object id) {
/* 226 */     String sql = "DELETE FROM " + this.table.getName() + " where " + this.table.getPkColumn().getName() + " = ?";
/* 227 */     this.jdbcTemplate.update(sql, new Object[] { id });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteData(Map<String, Object> param) {
/* 238 */     if (param.isEmpty()) {
/* 239 */       throw new RuntimeException("操作删除表[" + this.table.getComment() + "(" + this.table.getName() + ")]时，条件参数为空(会导致全表数据清空)");
/*     */     }
/*     */     
/* 242 */     StringBuilder sql = new StringBuilder();
/* 243 */     sql.append("DELETE FROM " + this.table.getName() + " where ");
/* 244 */     List<Object> paramList = new ArrayList();
/* 245 */     for (Map.Entry<String, Object> entry : param.entrySet()) {
/* 246 */       if (sql.toString().endsWith("?")) {
/* 247 */         sql.append(" and ");
/*     */       }
/* 249 */       sql.append((String)entry.getKey() + " = ?");
/* 250 */       paramList.add(entry.getValue());
/*     */     } 
/* 252 */     this.jdbcTemplate.update(sql.toString(), paramList.toArray());
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
/*     */   public void updateData(Map<String, Object> data) {
/* 264 */     Object id = data.get(this.table.getPkColumn().getName());
/* 265 */     if (id == null) {
/* 266 */       throw new RuntimeException("操作更新表[" + this.table.getComment() + "(" + this.table.getName() + ")]时，参数中有没主键[" + this.table.getPkColumn().getComment() + "(" + this.table.getPkColumn().getName() + ")]");
/*     */     }
/*     */     
/* 269 */     addUpdateColumn(data);
/* 270 */     StringBuilder sql = new StringBuilder();
/* 271 */     sql.append("UPDATE " + this.table.getName() + " SET ");
/* 272 */     List<Object> param = new ArrayList();
/*     */     
/* 274 */     for (Map.Entry<String, Object> entry : data.entrySet()) {
/*     */       
/* 276 */       if (((String)entry.getKey()).equals(this.table.getPkColumn().getName())) {
/*     */         continue;
/*     */       }
/* 279 */       if (sql.toString().endsWith("?")) {
/* 280 */         sql.append(" , ");
/*     */       }
/* 282 */       param.add(entry.getValue());
/* 283 */       sql.append((String)entry.getKey() + " = ?");
/*     */     } 
/* 285 */     if (param.size() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 289 */     sql.append(" WHERE " + this.table.getPkColumn().getName() + " = ?");
/* 290 */     param.add(id);
/* 291 */     this.jdbcTemplate.update(sql.toString(), param.toArray());
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
/*     */   public Map<String, Object> selectData(List<String> columnName, Object id) {
/* 304 */     Map<String, Object> param = new HashMap<>();
/* 305 */     param.put(this.table.getPkColumn().getName(), id);
/* 306 */     List<Map<String, Object>> list = selectData(columnName, param);
/* 307 */     if (!list.isEmpty()) {
/* 308 */       return list.get(0);
/*     */     }
/* 310 */     return null;
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
/*     */   public Map<String, Object> selectData(Object id) {
/* 322 */     Map<String, Object> param = new HashMap<>();
/* 323 */     param.put(this.table.getPkColumn().getName(), id);
/* 324 */     List<Map<String, Object>> list = selectData(param);
/* 325 */     if (!list.isEmpty()) {
/* 326 */       return list.get(0);
/*     */     }
/* 328 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Map<String, Object>> selectData(Map<String, Object> param) {
/* 339 */     return selectData((List<String>)null, param);
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
/*     */   public List<Map<String, Object>> selectData(List<String> columnName, Map<String, Object> param) {
/* 351 */     StringBuilder sql = new StringBuilder();
/* 352 */     if (CollectionUtil.isEmpty(columnName)) {
/* 353 */       sql.append("SELECT * FROM " + this.table.getName());
/*     */     } else {
/* 355 */       sql.append("SELECT");
/* 356 */       for (String cn : columnName) {
/* 357 */         if (!sql.toString().endsWith("SELECT")) {
/* 358 */           sql.append(",");
/*     */         }
/* 360 */         sql.append(" " + cn);
/*     */       } 
/* 362 */       sql.append(" FROM " + this.table.getName());
/*     */     } 
/*     */     
/* 365 */     sql.append(" WHERE ");
/*     */     
/* 367 */     List<Object> paramList = new ArrayList();
/* 368 */     for (Map.Entry<String, Object> entry : param.entrySet()) {
/* 369 */       if (sql.toString().endsWith("?")) {
/* 370 */         sql.append(" and ");
/*     */       }
/* 372 */       sql.append((String)entry.getKey() + " = ?");
/* 373 */       paramList.add(entry.getValue());
/*     */     } 
/* 375 */     List<Map<String, Object>> map = new ArrayList<>();
/*     */     try {
/* 377 */       map = this.jdbcTemplate.queryForList(sql.toString(), paramList.toArray());
/* 378 */     } catch (DataIntegrityViolationException e) {
/* 379 */       Pattern pattern = Pattern.compile("无效的列名[^\\]]*]");
/* 380 */       Matcher m = pattern.matcher(e.getMessage());
/* 381 */       if (m.find()) {
/* 382 */         String msg = m.group(0).replaceAll("无效的列名", "");
/* 383 */         throw new BusinessException("表[" + this.table.getName() + "]中不存在" + msg + "字段,请同步实体");
/*     */       } 
/* 385 */       throw e;
/*     */     } 
/*     */     
/* 388 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void syncColumn() {
/* 399 */     if (!isTableCreated()) {
/*     */       return;
/*     */     }
/* 402 */     Set<String> dbColumnNames = new HashSet<>();
/* 403 */     Table<Column> dbTable = getDbTable();
/* 404 */     for (Column c : dbTable.getColumns()) {
/* 405 */       dbColumnNames.add(c.getName());
/*     */     }
/*     */     
/* 408 */     for (String columnName : dbColumnNames) {
/* 409 */       if (this.table.getColumn(columnName) == null) {
/* 410 */         dropColumn(columnName);
/*     */       }
/*     */     } 
/*     */     
/* 414 */     for (Column column : this.table.getColumns()) {
/* 415 */       boolean exits = false;
/* 416 */       for (String columnName : dbColumnNames) {
/* 417 */         if (columnName.equalsIgnoreCase(column.getName())) {
/* 418 */           exits = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 422 */       if (!exits) {
/* 423 */         addColumn(column); continue;
/* 424 */       }  if (!dbTable.getColumn(column.getName()).equals(column)) {
/* 425 */         updateColumn(column);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Table<Column> getDbTable() {
/* 431 */     DbOperator dbOperator = DbOperatorFactory.newOperator(type(), this.jdbcTemplate);
/* 432 */     return dbOperator.getTable(this.table.getName());
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
/*     */   public void saveIndex(List<String> columnNames) {
/* 444 */     if (columnNames.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 451 */     Map<String, String> indexMap = selectIndex();
/*     */     
/* 453 */     if (indexMap.containsValue(String.join(",", (Iterable)columnNames))) {
/* 454 */       this.logger.info("表【{}】已存在字段{}的索引", this.table.getName(), columnNames);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 461 */     createIndex(columnNames);
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
/*     */   public Map<String, String> selectIndex() {
/* 474 */     this.logger.info("请实现【{}】数据库类型的查找索引方法（selectIndex）", type());
/* 475 */     return new HashMap<>();
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
/*     */   public void createIndex(List<String> columnNames) {
/* 487 */     this.logger.info("请实现【{}】数据库类型的创建索引方法（createIndex）", type());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/TableOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */