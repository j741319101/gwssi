/*     */ package cn.gwssi.ecloudframework.base.db.datasource;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import java.util.Map;
/*     */ import javax.sql.DataSource;
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
/*     */ public class DataSourceUtil
/*     */ {
/*     */   public static final String GLOBAL_DATASOURCE = "dataSource";
/*     */   public static final String DEFAULT_DATASOURCE = "dataSourceDefault";
/*     */   public static final String TARGET_DATASOURCES = "targetDataSources";
/*     */   public static DynamicDataSource dynamicDataSource;
/*     */   
/*     */   public static void addDataSource(String key, DataSource dataSource, boolean replace) {
/*  32 */     if (dynamicDataSource.isDataSourceExist(key)) {
/*  33 */       if (!replace)
/*     */         return; 
/*  35 */       dynamicDataSource.removeDataSource(key);
/*     */     } 
/*  37 */     dynamicDataSource.addDataSource(key, dataSource);
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
/*     */   public static void addDataSource(String key, DataSource dataSource, String dbType, boolean replace) {
/*  51 */     addDataSource(key, dataSource, replace);
/*  52 */     if (StringUtil.isNotEmpty(dbType)) {
/*  53 */       DbContextHolder.putDataSourceDbType(key, dbType);
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
/*     */   public static boolean isDataSourceExist(String key) {
/*  66 */     return dynamicDataSource.isDataSourceExist(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeDataSource(String key) throws IllegalAccessException, NoSuchFieldException {
/*  77 */     dynamicDataSource.removeDataSource(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, DataSource> getDataSources() {
/*  88 */     Map<String, DataSource> map = dynamicDataSource.getDataSource();
/*  89 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource getDataSourceByAlias(String alias) {
/* 100 */     Map<String, DataSource> map = getDataSources();
/* 101 */     return map.get(alias);
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
/*     */   public static DataSource getDataSourceByAliasWithLoacl(String alias) {
/* 114 */     if ("dataSource".equals(alias)) {
/* 115 */       return (DataSource)dynamicDataSource;
/*     */     }
/* 117 */     return getDataSourceByAlias(alias);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/datasource/DataSourceUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */