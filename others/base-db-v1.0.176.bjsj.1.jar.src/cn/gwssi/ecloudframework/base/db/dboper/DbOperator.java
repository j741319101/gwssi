/*     */ package com.dstz.base.db.dboper;
/*     */ 
/*     */ import com.dstz.base.db.model.table.Column;
/*     */ import com.dstz.base.db.model.table.Table;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DbOperator
/*     */ {
/*  29 */   protected Logger logger = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JdbcTemplate jdbcTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbOperator(JdbcTemplate jdbcTemplate) {
/*  40 */     this.jdbcTemplate = jdbcTemplate;
/*     */     
/*  42 */     if (jdbcTemplate == null) {
/*  43 */       this.logger.warn("create DbOperator with error ! jdbcTemplate cannot be null!");
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
/*     */   
/*     */   public abstract String type();
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
/*     */   public abstract Map<String, String> getTableNames(String paramString);
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
/*     */   public abstract List<String> getViewNames(String paramString);
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
/*     */   public abstract Table<Column> getTable(String paramString);
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
/*     */   public abstract Table<Column> getView(String paramString);
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
/*     */   public abstract boolean supportPartition(String paramString);
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
/*     */   public abstract boolean isExsitPartition(String paramString1, String paramString2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void createPartition(String paramString1, String paramString2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getOrDefault(Map<?, ?> map, Object key, Object defVal) {
/* 143 */     if (!map.containsKey(key)) {
/* 144 */       return defVal;
/*     */     }
/* 146 */     Object val = map.get(key);
/* 147 */     if (val == null) {
/* 148 */       return defVal;
/*     */     }
/* 150 */     return val;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/dboper/DbOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */