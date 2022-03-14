/*     */ package com.dstz.sys.core.manager.impl;
/*     */ 
/*     */ import com.dstz.base.api.constant.BaseStatusCode;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.BeanUtils;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.datasource.DataSourceUtil;
/*     */ import com.dstz.base.db.datasource.DbContextHolder;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.sys.core.dao.SysDataSourceDao;
/*     */ import com.dstz.sys.core.manager.SysDataSourceManager;
/*     */ import com.dstz.sys.core.model.SysDataSource;
/*     */ import com.dstz.sys.core.model.def.SysDataSourceDefAttribute;
/*     */ import cn.hutool.core.util.ReflectUtil;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.stereotype.Service;
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
/*     */ @Service
/*     */ public class SysDataSourceManagerImpl
/*     */   extends BaseManager<String, SysDataSource>
/*     */   implements SysDataSourceManager
/*     */ {
/*     */   @Autowired
/*     */   SysDataSourceDao sysDataSourceDao;
/*     */   
/*     */   public DataSource tranform2DataSource(SysDataSource sysDataSource) {
/*     */     try {
/*  45 */       Class<?> cls = Class.forName(sysDataSource.getClassPath());
/*  46 */       DataSource dataSource = (DataSource)cls.newInstance();
/*     */       
/*  48 */       for (SysDataSourceDefAttribute attribute : sysDataSource.getAttributes()) {
/*  49 */         if (StringUtil.isEmpty(attribute.getValue())) {
/*     */           continue;
/*     */         }
/*  52 */         Object value = BeanUtils.getValue(attribute.getType(), attribute.getValue());
/*  53 */         String setMethodName = "set" + StringUtil.toFirst(attribute.getName(), true);
/*  54 */         ReflectUtil.invoke(dataSource, setMethodName, new Object[] { value });
/*     */       } 
/*  56 */       return dataSource;
/*  57 */     } catch (Exception e) {
/*  58 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SysDataSource getByKey(String key) {
/*  64 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  65 */     defaultQueryFilter.addFilter("key_", key, QueryOP.EQUAL);
/*  66 */     return (SysDataSource)queryOne((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataSource getDataSourceByKey(String key, boolean add) {
/*  72 */     if (DbContextHolder.getDataSource().equals(key)) {
/*  73 */       return (DataSource)AppUtil.getBean("dataSource");
/*     */     }
/*     */ 
/*     */     
/*  77 */     DataSource dataSource = DataSourceUtil.getDataSourceByAlias(key);
/*  78 */     if (dataSource != null) {
/*  79 */       return dataSource;
/*     */     }
/*     */     
/*  82 */     SysDataSource sysDataSource = getByKey(key);
/*  83 */     if (sysDataSource != null) {
/*  84 */       dataSource = tranform2DataSource(sysDataSource);
/*  85 */       if (dataSource != null) {
/*  86 */         if (add) {
/*  87 */           DataSourceUtil.addDataSource(key, dataSource, sysDataSource.getDbType(), true);
/*     */         }
/*  89 */         return dataSource;
/*     */       } 
/*     */     } 
/*  92 */     throw new BusinessException("在系统中找不到key为[" + key + "]的数据源", BaseStatusCode.SYSTEM_ERROR);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataSource getDataSourceByKey(String key) {
/*  97 */     return getDataSourceByKey(key, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JdbcTemplate getJdbcTemplateByKey(String key) {
/* 103 */     if (DbContextHolder.getDataSource().equals(key)) {
/* 104 */       return (JdbcTemplate)AppUtil.getBean("jdbcTemplate");
/*     */     }
/*     */     
/* 107 */     return new JdbcTemplate(getDataSourceByKey(key));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SysDataSourceManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */