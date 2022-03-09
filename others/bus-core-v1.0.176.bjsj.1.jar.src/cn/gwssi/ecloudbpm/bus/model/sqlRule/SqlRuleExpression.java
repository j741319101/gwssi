/*    */ package cn.gwssi.ecloudbpm.bus.model.sqlRule;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRuleParam;
/*    */ import cn.gwssi.ecloudbpm.bus.constant.SqlRuleType;
/*    */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.lang3.StringUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SqlRuleExpression
/*    */   extends SqlRule
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   String filter;
/*    */   Boolean hidden;
/*    */   String value;
/*    */   
/*    */   public String getFilter() {
/* 34 */     return this.filter;
/*    */   }
/*    */   
/*    */   public void setFilter(String filter) {
/* 38 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 42 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 46 */     this.value = value;
/*    */   }
/*    */   
/*    */   public Boolean getHidden() {
/* 50 */     return this.hidden;
/*    */   }
/*    */   
/*    */   public void setHidden(Boolean hidden) {
/* 54 */     this.hidden = hidden;
/*    */   }
/*    */ 
/*    */   
/*    */   String getDefaultSql(ISqlRuleParam sqlRuleParam) {
/* 59 */     String id = IdUtil.getSuid();
/* 60 */     String defaultSql = this.filter + " " + SqlRuleType.getByKey(getType()).getKeyword() + " #{" + id + "}";
/* 61 */     return initSql(sqlRuleParam, defaultSql, id);
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
/*    */   String initSql(ISqlRuleParam sqlRuleParam, String defaultSql, String id) {
/* 73 */     String dbType = sqlRuleParam.getDbType();
/* 74 */     Map<String, Object> params = sqlRuleParam.getParams();
/* 75 */     Map<String, String> types = sqlRuleParam.getTypes();
/* 76 */     String sql = "";
/* 77 */     if (StringUtils.isNotEmpty(this.filter)) {
/* 78 */       if (null != this.hidden && true == this.hidden.booleanValue()) {
/* 79 */         sql = defaultSql;
/* 80 */         params.put(id, getFormatValue(this.value, types.get(this.filter)));
/*    */       }
/* 82 */       else if (params.containsKey(this.filter)) {
/* 83 */         sql = defaultSql;
/* 84 */         params.put(id, getFormatValue(params.get(this.filter), types.get(this.filter)));
/*    */       } 
/*    */     }
/*    */     
/* 88 */     return sql;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/sqlRule/SqlRuleExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */