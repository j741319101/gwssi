/*     */ package cn.gwssi.ecloudframework.base.db.datasource;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Map;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/*     */ import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
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
/*     */ public class DynamicDataSource
/*     */   extends AbstractRoutingDataSource
/*     */ {
/*     */   protected Object determineCurrentLookupKey() {
/*  35 */     return DbContextHolder.getDataSource();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTargetDataSources(Map<Object, Object> targetDataSources) {
/*  40 */     super.setTargetDataSources(targetDataSources);
/*     */     
/*  42 */     afterPropertiesSet();
/*     */   }
/*     */   
/*     */   public void setDefaultDbtype(String dbType) {
/*  46 */     DbContextHolder.setDataSource("dataSourceDefault", dbType);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object getValue(Object instance, String fieldName) {
/*     */     try {
/*  52 */       Field field = AbstractRoutingDataSource.class.getDeclaredField(fieldName);
/*     */       
/*  54 */       field.setAccessible(true);
/*  55 */       return field.get(instance);
/*  56 */     } catch (Exception e) {
/*  57 */       throw new BusinessException(e);
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
/*     */   public void addDataSource(String key, Object dataSource) {
/*     */     try {
/*  71 */       Map<Object, Object> targetDataSources = (Map<Object, Object>)getValue(this, "targetDataSources");
/*  72 */       boolean rtn = isDataSourceExist(key);
/*  73 */       if (rtn) {
/*  74 */         throw new DataSourceException("datasource name :" + key + "is exists!");
/*     */       }
/*  76 */       targetDataSources.put(key, dataSource);
/*  77 */       setTargetDataSources(targetDataSources);
/*  78 */     } catch (Exception e) {
/*  79 */       throw new BusinessException(e);
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
/*     */   public boolean isDataSourceExist(String key) {
/*     */     try {
/*  93 */       Map<Object, Object> targetDataSources = (Map<Object, Object>)getValue(this, "targetDataSources");
/*  94 */       return targetDataSources.containsKey(key);
/*  95 */     } catch (Exception e) {
/*  96 */       throw new BusinessException(e);
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
/*     */   public void removeDataSource(String key) {
/* 109 */     Map<Object, Object> targetDataSources = (Map<Object, Object>)getValue(this, "targetDataSources");
/*     */     
/* 111 */     if (key.equals("dataSource") || key.equals("dataSourceDefault")) {
/* 112 */       throw new DataSourceException("datasource name :" + key + " can't be removed!");
/*     */     }
/* 114 */     targetDataSources.remove(key);
/* 115 */     setTargetDataSources(targetDataSources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, DataSource> getDataSource() {
/* 126 */     Map<String, DataSource> targetDataSources = (Map<String, DataSource>)getValue(this, "targetDataSources");
/* 127 */     return targetDataSources;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
/* 133 */     super.setDefaultTargetDataSource(defaultTargetDataSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
/* 138 */     super.setDataSourceLookup(dataSourceLookup);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/datasource/DynamicDataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */