/*    */ package cn.gwssi.ecloudbpm.bus.model.sqlRule;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRuleParam;
/*    */ import cn.gwssi.ecloudbpm.bus.constant.SqlRuleType;
/*    */ import com.dstz.base.core.id.IdUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SqlRuleLfk
/*    */   extends SqlRuleExpression
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   
/*    */   public String getType() {
/* 20 */     return SqlRuleType.LFK.getKey();
/*    */   }
/*    */   
/*    */   String getDefaultSql(ISqlRuleParam sqlRuleParam) {
/* 24 */     String id = IdUtil.getSuid();
/* 25 */     String defaultSql = this.filter + " " + SqlRuleType.getByKey(getType()).getKeyword() + " CONCAT('%',#{" + id + "})";
/* 26 */     return initSql(sqlRuleParam, defaultSql, id);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/sqlRule/SqlRuleLfk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */