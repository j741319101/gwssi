/*     */ package com.github.pagehelper.page;
/*     */ import com.github.pagehelper.Dialect;
/*     */ import com.github.pagehelper.PageException;
/*     */ import com.github.pagehelper.dialect.AbstractHelperDialect;
/*     */ import com.github.pagehelper.dialect.helper.Db2Dialect;
/*     */ import com.github.pagehelper.dialect.helper.HsqldbDialect;
/*     */ import com.github.pagehelper.dialect.helper.InformixDialect;
/*     */ import com.github.pagehelper.dialect.helper.MySqlDialect;
/*     */ import com.github.pagehelper.dialect.helper.OracleDialect;
/*     */ import com.github.pagehelper.dialect.helper.SqlServer2012Dialect;
/*     */ import com.github.pagehelper.dialect.helper.SqlServerDialect;
import com.github.pagehelper.util.StringUtil;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.ibatis.mapping.MappedStatement;
/*     */ 
/*     */ public class PageAutoDialect {
/*  22 */   private static Map<String, Class<? extends Dialect>> dialectAliasMap = new HashMap<>();
/*     */   
/*     */   public static void registerDialectAlias(String alias, Class<? extends Dialect> dialectClass) {
/*  25 */     dialectAliasMap.put(alias, dialectClass);
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/*  30 */     registerDialectAlias("hsqldb", (Class)HsqldbDialect.class);
/*  31 */     registerDialectAlias("h2", (Class)HsqldbDialect.class);
/*  32 */     registerDialectAlias("postgresql", (Class)HsqldbDialect.class);
/*  33 */     registerDialectAlias("phoenix", (Class)HsqldbDialect.class);
/*     */     
/*  35 */     registerDialectAlias("mysql", (Class)MySqlDialect.class);
/*  36 */     registerDialectAlias("mariadb", (Class)MySqlDialect.class);
/*  37 */     registerDialectAlias("sqlite", (Class)MySqlDialect.class);
/*     */     
/*  39 */     registerDialectAlias("oracle", (Class)OracleDialect.class);
/*  40 */     registerDialectAlias("db2", (Class)Db2Dialect.class);
/*  41 */     registerDialectAlias("informix", (Class)InformixDialect.class);
/*     */     
/*  43 */     registerDialectAlias("informix-sqli", (Class)InformixDialect.class);
/*     */     
/*  45 */     registerDialectAlias("sqlserver", (Class) SqlServerDialect.class);
/*  46 */     registerDialectAlias("sqlserver2012", (Class)SqlServer2012Dialect.class);
/*     */     
/*  48 */     registerDialectAlias("derby", (Class)SqlServer2012Dialect.class);
/*     */     
/*  50 */     registerDialectAlias("dm", (Class)OracleDialect.class);
/*     */     
/*  52 */     registerDialectAlias("drds", (Class)MySqlDialect.class);
/*     */     
/*  54 */     registerDialectAlias("kingbase", (Class)MySqlDialect.class);
/*     */     
/*  56 */     registerDialectAlias("highgo", (Class)HsqldbDialect.class);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean autoDialect = true;
/*     */   
/*     */   private boolean closeConn = true;
/*     */   
/*     */   private Properties properties;
/*     */   
/*  66 */   private Map<String, AbstractHelperDialect> urlDialectMap = new ConcurrentHashMap<>();
/*  67 */   private ReentrantLock lock = new ReentrantLock();
/*     */   private AbstractHelperDialect delegate;
/*  69 */   private ThreadLocal<AbstractHelperDialect> dialectThreadLocal = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   public void initDelegateDialect(MappedStatement ms) {
/*  73 */     if (this.delegate == null) {
/*  74 */       if (this.autoDialect) {
/*  75 */         this.delegate = getDialect(ms);
/*     */       } else {
/*  77 */         this.dialectThreadLocal.set(getDialect(ms));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractHelperDialect getDelegate() {
/*  84 */     if (this.delegate != null) {
/*  85 */       return this.delegate;
/*     */     }
/*  87 */     return this.dialectThreadLocal.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearDelegate() {
/*  92 */     this.dialectThreadLocal.remove();
/*     */   }
/*     */   
/*     */   private String fromJdbcUrl(String jdbcUrl) {
/*  96 */     for (String dialect : dialectAliasMap.keySet()) {
/*  97 */       if (jdbcUrl.indexOf(":" + dialect) != -1) {
/*  98 */         return dialect;
/*     */       }
/*     */     } 
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class resloveDialectClass(String className) throws Exception {
/* 112 */     if (dialectAliasMap.containsKey(className.toLowerCase())) {
/* 113 */       return dialectAliasMap.get(className.toLowerCase());
/*     */     }
/* 115 */     return Class.forName(className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractHelperDialect initDialect(String dialectClass, Properties properties) {
/*     */     AbstractHelperDialect dialect;
/* 127 */     if (StringUtil.isEmpty(dialectClass)) {
/* 128 */       throw new PageException("使用 PageHelper 分页插件时，必须设置 helper 属性");
/*     */     }
/*     */     try {
/* 131 */       Class<?> sqlDialectClass = resloveDialectClass(dialectClass);
/* 132 */       if (AbstractHelperDialect.class.isAssignableFrom(sqlDialectClass)) {
/* 133 */         dialect = (AbstractHelperDialect)sqlDialectClass.newInstance();
/*     */       } else {
/* 135 */         throw new PageException("使用 PageHelper 时，方言必须是实现 " + AbstractHelperDialect.class.getCanonicalName() + " 接口的实现类!");
/*     */       } 
/* 137 */     } catch (Exception e) {
/* 138 */       throw new PageException("初始化 helper [" + dialectClass + "]时出错:" + e.getMessage(), e);
/*     */     } 
/* 140 */     dialect.setProperties(properties);
/* 141 */     return dialect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getUrl(DataSource dataSource) {
/* 151 */     Connection conn = null;
/*     */     try {
/* 153 */       conn = dataSource.getConnection();
/* 154 */       return conn.getMetaData().getURL();
/* 155 */     } catch (SQLException e) {
/* 156 */       throw new PageException(e);
/*     */     } finally {
/* 158 */       if (conn != null) {
/*     */         try {
/* 160 */           if (this.closeConn) {
/* 161 */             conn.close();
/*     */           }
/* 163 */         } catch (SQLException sQLException) {}
/*     */       }
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
/*     */   private AbstractHelperDialect getDialect(MappedStatement ms) {
/* 178 */     DataSource dataSource = ms.getConfiguration().getEnvironment().getDataSource();
/* 179 */     String url = getUrl(dataSource);
/* 180 */     if (this.urlDialectMap.containsKey(url)) {
/* 181 */       return this.urlDialectMap.get(url);
/*     */     }
/*     */     try {
/* 184 */       this.lock.lock();
/* 185 */       if (this.urlDialectMap.containsKey(url)) {
/* 186 */         return this.urlDialectMap.get(url);
/*     */       }
/* 188 */       if (StringUtil.isEmpty(url)) {
/* 189 */         throw new PageException("无法自动获取jdbcUrl，请在分页插件中配置dialect参数!");
/*     */       }
/* 191 */       String dialectStr = fromJdbcUrl(url);
/* 192 */       if (dialectStr == null) {
/* 193 */         throw new PageException("无法自动获取数据库类型，请通过 helperDialect 参数指定!");
/*     */       }
/* 195 */       AbstractHelperDialect dialect = initDialect(dialectStr, this.properties);
/* 196 */       this.urlDialectMap.put(url, dialect);
/* 197 */       return dialect;
/*     */     } finally {
/* 199 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setProperties(Properties properties) {
/* 205 */     String closeConn = properties.getProperty("closeConn");
/* 206 */     if (StringUtil.isNotEmpty(closeConn)) {
/* 207 */       this.closeConn = Boolean.parseBoolean(closeConn);
/*     */     }
/* 209 */     String dialectAlias = properties.getProperty("dialectAlias");
/* 210 */     if (StringUtil.isNotEmpty(dialectAlias)) {
/* 211 */       String[] alias = dialectAlias.split(";");
/* 212 */       for (int i = 0; i < alias.length; i++) {
/* 213 */         String[] kv = alias[i].split("=");
/* 214 */         if (kv.length != 2) {
/* 215 */           throw new IllegalArgumentException("dialectAlias 参数配置错误，请按照 alias1=xx.dialectClass;alias2=dialectClass2 的形式进行配置!");
/*     */         }
/*     */         
/* 218 */         for (int j = 0; j < kv.length; j++) {
/*     */           try {
/* 220 */             Class<? extends Dialect> diallectClass = (Class)Class.forName(kv[1]);
/*     */             
/* 222 */             registerDialectAlias(kv[0], diallectClass);
/* 223 */           } catch (ClassNotFoundException e) {
/* 224 */             throw new IllegalArgumentException("请确保 dialectAlias 配置的 Dialect 实现类存在!", e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 230 */     String dialect = properties.getProperty("helperDialect");
/*     */     
/* 232 */     String runtimeDialect = properties.getProperty("autoRuntimeDialect");
/*     */     
/* 234 */     if (StringUtil.isNotEmpty(runtimeDialect) && "TRUE".equalsIgnoreCase(runtimeDialect)) {
/* 235 */       this.autoDialect = false;
/* 236 */       this.properties = properties;
/*     */     
/*     */     }
/* 239 */     else if (StringUtil.isEmpty(dialect)) {
/* 240 */       this.autoDialect = true;
/* 241 */       this.properties = properties;
/*     */     }
/*     */     else {
/*     */       
/* 245 */       this.autoDialect = false;
/* 246 */       this.delegate = initDialect(dialect, properties);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/com/github/pagehelper/page/PageAutoDialect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */