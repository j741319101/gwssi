/*     */ package cn.gwssi.ecloudbpm.bus.model.sqlRule;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRule;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRuleParam;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.ColumnType;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.DbType;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ public class SqlRule
/*     */   implements ISqlRule
/*     */ {
/*     */   private static final long serialVersionUID = 700694295167942753L;
/*  24 */   private Logger LOG = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */   
/*     */   private String type;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  32 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  36 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSql(ISqlRuleParam sqlRuleParam) {
/*  41 */     String sql, dbType = sqlRuleParam.getDbType();
/*  42 */     DbType temp = DbType.getByKey(dbType);
/*  43 */     if (null == temp) {
/*  44 */       temp = DbType.DMSQL;
/*     */     }
/*     */     
/*  47 */     switch (temp) {
/*     */       case DATE:
/*  49 */         sql = getDmSql(sqlRuleParam);
/*     */         break;
/*     */       case VARCHAR:
/*  52 */         sql = getMysqlSql(sqlRuleParam);
/*     */         break;
/*     */       case NUMBER:
/*  55 */         sql = getOracleSql(sqlRuleParam);
/*     */         break;
/*     */       case CLOB:
/*  58 */         sql = getKingbaseSql(sqlRuleParam);
/*     */         break;
/*     */       default:
/*  61 */         sql = getDefaultSql(sqlRuleParam); break;
/*     */     } 
/*  63 */     if (null == sql) {
/*  64 */       sql = getDefaultSql(sqlRuleParam);
/*     */     }
/*  66 */     return sql;
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
/*     */   String getDmSql(ISqlRuleParam sqlRuleParam) {
/*  78 */     return getDefaultSql(sqlRuleParam);
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
/*     */   String getKingbaseSql(ISqlRuleParam sqlRuleParam) {
/*  90 */     return getDefaultSql(sqlRuleParam);
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
/*     */   String getOracleSql(ISqlRuleParam sqlRuleParam) {
/* 102 */     return getDefaultSql(sqlRuleParam);
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
/*     */   String getMysqlSql(ISqlRuleParam sqlRuleParam) {
/* 114 */     return getDefaultSql(sqlRuleParam);
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
/*     */   String getDefaultSql(ISqlRuleParam sqlRuleParam) {
/* 126 */     this.LOG.warn("类型:+" + getType() + " 数据库类型：" + sqlRuleParam.getDbType() + "不存在sql解析");
/* 127 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object getFormatValue(Object value, String type) {
/*     */     ColumnType columnType;
/*     */     SimpleDateFormat sf;
/* 138 */     Object o = "";
/*     */     
/* 140 */     if (StringUtils.isEmpty(type)) {
/* 141 */       columnType = ColumnType.VARCHAR;
/*     */     } else {
/* 143 */       columnType = ColumnType.getByKey(type);
/*     */     } 
/* 145 */     switch (columnType)
/*     */     { case DATE:
/* 147 */         sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*     */         try {
/* 149 */           o = sf.parse(String.valueOf(value));
/* 150 */         } catch (ParseException e) {
/* 151 */           this.LOG.warn("sql生成，时间格式转换失败" + value);
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 161 */         return o; }  o = value; return o;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/sqlRule/SqlRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */