/*     */ package com.dstz.base.db.datasource;
/*     */ 
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.mybatis.spring.SqlSessionFactoryBean;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class DbContextHolder
/*     */ {
/*  30 */   private static final ThreadLocal<String> contextHolderAlias = new ThreadLocal<>();
/*  31 */   private static Map<String, String> dataSourceDbType = new ConcurrentHashMap<>();
/*     */   
/*  33 */   protected static final Logger LOG = LoggerFactory.getLogger(DbContextHolder.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDataSource(String dbAlias, String dbType) {
/*  42 */     contextHolderAlias.set(dbAlias);
/*  43 */     dataSourceDbType.put(dbAlias, dbType);
/*     */ 
/*     */     
/*     */     try {
/*  47 */       SqlSessionFactoryBean sqlSessionFactoryBean = (SqlSessionFactoryBean)AppUtil.getBean(SqlSessionFactoryBean.class);
/*  48 */       if (sqlSessionFactoryBean != null && sqlSessionFactoryBean.getObject() != null && sqlSessionFactoryBean.getObject().getConfiguration() != null) {
/*  49 */         sqlSessionFactoryBean.getObject().getConfiguration().setDatabaseId(dbType);
/*     */       }
/*  51 */     } catch (Exception e) {
/*  52 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setDefaultDataSource() {
/*  57 */     contextHolderAlias.set("dataSourceDefault");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDataSource() {
/*  66 */     String str = contextHolderAlias.get();
/*  67 */     return StringUtils.isEmpty(str) ? "dataSourceDefault" : str;
/*     */   }
/*     */   
/*     */   public static String getDbType() {
/*  71 */     String dataSourceAlias = contextHolderAlias.get();
/*  72 */     if (StringUtil.isEmpty(dataSourceAlias)) {
/*  73 */       dataSourceAlias = "dataSourceDefault";
/*     */     }
/*     */     
/*  76 */     String str = dataSourceDbType.get(dataSourceAlias);
/*  77 */     if (StringUtil.isEmpty(str)) {
/*  78 */       LOG.warn("cannot get current dataSourceDbType!");
/*     */     }
/*  80 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearDataSource() {
/*  87 */     contextHolderAlias.remove();
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
/*     */   public static void putDataSourceDbType(String dsKey, String dbType) {
/*  99 */     dataSourceDbType.put(dsKey, dbType);
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
/*     */   public static String getDataSourceDbType(String dsKey) {
/* 111 */     return dataSourceDbType.get(dsKey);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/datasource/DbContextHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */