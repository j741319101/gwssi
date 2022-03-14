/*     */ package com.dstz.base.api.constant;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import java.util.Arrays;
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
/*     */ public enum ColumnType
/*     */ {
/*  20 */   VARCHAR("varchar", "字符串", new String[] { "varchar", "varchar2", "char", "tinyblob", "tinytext"
/*     */ 
/*     */     
/*     */     }),
/*  24 */   CLOB("clob", "大文本", new String[] { "text", "clob", "blob", "mediumblob", "mediumtext", "longblob", "longtext"
/*     */ 
/*     */     
/*     */     }),
/*  28 */   NUMBER("number", "数字型", new String[] { "tinyint", "number", "smallint", "mediumint", "int", "integer", "bigint", "float", "double", "decimal", "numeric"
/*     */ 
/*     */     
/*     */     }),
/*  32 */   DATE("date", "日期型", new String[] { "date", "time", "year", "datetime", "timestamp" });
/*     */ 
/*     */ 
/*     */   
/*     */   private String key;
/*     */ 
/*     */   
/*     */   private String desc;
/*     */ 
/*     */   
/*     */   private String[] supports;
/*     */ 
/*     */ 
/*     */   
/*     */   ColumnType(String key, String desc, String[] supports) {
/*  47 */     this.key = key;
/*  48 */     this.desc = desc;
/*  49 */     this.supports = supports;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  53 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/*  57 */     return this.desc;
/*     */   }
/*     */   
/*     */   public String[] getSupports() {
/*  61 */     return this.supports;
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
/*     */   public boolean equalsWithKey(String key) {
/*  73 */     return this.key.equals(key);
/*     */   }
/*     */   
/*     */   public static ColumnType getByKey(String key) {
/*  77 */     for (ColumnType type : values()) {
/*  78 */       if (type.getKey().equals(key)) {
/*  79 */         return type;
/*     */       }
/*     */     } 
/*  82 */     throw new BusinessException(String.format("找不到key为[%s]的字段类型", new Object[] { key }));
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
/*     */   public static ColumnType getByDbDataType(String dbDataType, String errMsgApp) {
/*  96 */     for (ColumnType type : values()) {
/*  97 */       for (String support : Arrays.<String>asList(type.supports)) {
/*  98 */         if (dbDataType.toLowerCase().contains(support.toLowerCase())) {
/*  99 */           return type;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 104 */     throw new BusinessException(String.format("[%s]数据库类型[%s]转换不了系统支持的类型", new Object[] { errMsgApp, dbDataType }));
/*     */   }
/*     */   
/*     */   public static ColumnType getByDbDataType(String dbDataType) {
/* 108 */     return getByDbDataType(dbDataType, "");
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/constant/ColumnType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */