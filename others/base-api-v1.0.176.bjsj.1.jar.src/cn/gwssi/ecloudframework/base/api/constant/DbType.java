/*    */ package com.dstz.base.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DbType
/*    */ {
/* 14 */   MYSQL("mysql", "mysql数据库", "com.mysql.jdbc.Driver", "jdbc:mysql://主机:3306/数据库名?useUnicode=true&characterEncoding=utf-8"),
/*    */ 
/*    */ 
/*    */   
/* 18 */   ORACLE("oracle", "oracle数据库", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@主机:1521:数据库实例"),
/*    */ 
/*    */ 
/*    */   
/* 22 */   DMSQL("dmsql", "达梦数据库", "dm.jdbc.driver.DmDriver", "jdbc:dm://主机:5326"),
/*    */ 
/*    */ 
/*    */   
/* 26 */   DRDS("drds", "DRDS数据源", "com.mysql.jdbc.Driver", "jdbc:mysql://主机:3306/数据库名?useUnicode=true&characterEncoding=utf-8"),
/*    */   
/* 28 */   KINGBASE("kingbase", "人大金仓数据源", "com.kingbase8.Driver", "jdbc:kingbase8://主机:3306/数据库名?"),
/*    */   
/* 30 */   HIGHGO("highgo", "瀚高数据源", "com.highgo.jdbc.Driver", "jdbc:highgo://主机:5866/数据库名"),
/*    */ 
/*    */ 
/*    */   
/* 34 */   SQLSERVER("sqlserver", "sqlserver数据库", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://主机:1433;databaseName=数据库名");
/*    */   
/*    */   private String key;
/*    */   
/*    */   private String desc;
/*    */   private String driverClassName;
/*    */   private String url;
/*    */   
/*    */   DbType(String key, String desc, String driverClassName, String url) {
/* 43 */     this.desc = desc;
/* 44 */     this.key = key;
/* 45 */     this.driverClassName = driverClassName;
/* 46 */     this.url = url;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 50 */     return this.desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 54 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDriverClassName() {
/* 58 */     return this.driverClassName;
/*    */   }
/*    */   
/*    */   public String getUrl() {
/* 62 */     return this.url;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equalsWithKey(String key) {
/* 74 */     return this.key.equals(key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DbType getByKey(String key) {
/* 85 */     for (DbType value : values()) {
/* 86 */       if (value.key.equals(key)) {
/* 87 */         return value;
/*    */       }
/*    */     } 
/* 90 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/constant/DbType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */