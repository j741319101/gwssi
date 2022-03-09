/*    */ package cn.gwssi.ecloudbpm.bus.model.sqlRule;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRule;
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRuleParam;
/*    */ import cn.gwssi.ecloudbpm.bus.constant.SqlRuleType;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class SqlRuleRelation
/*    */   extends SqlRule
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   List<ISqlRule> children;
/*    */   
/*    */   public List<ISqlRule> getChildren() {
/* 27 */     return this.children;
/*    */   }
/*    */   
/*    */   public void setChildren(List<ISqlRule> children) {
/* 31 */     this.children = children;
/*    */   }
/*    */ 
/*    */   
/*    */   String getDefaultSql(ISqlRuleParam sqlRuleParam) {
/* 36 */     List<String> lstSql = new ArrayList<>();
/* 37 */     if (null != this.children) {
/* 38 */       this.children.forEach(rule -> {
/*    */             if (null != rule) {
/*    */               String temp = rule.getSql(sqlRuleParam);
/*    */               if (!StringUtils.isEmpty(temp)) {
/*    */                 lstSql.add(temp);
/*    */               }
/*    */             } 
/*    */           });
/*    */     }
/* 47 */     String sql = String.join(" " + SqlRuleType.getByKey(getType()).getKeyword() + " ", (Iterable)lstSql);
/* 48 */     if (!StringUtils.isEmpty(sql)) {
/* 49 */       sql = "( " + sql + " )";
/*    */     }
/* 51 */     return sql;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/sqlRule/SqlRuleRelation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */